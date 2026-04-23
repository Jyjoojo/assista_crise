package fr.assistacrise.web.rest;

import static fr.assistacrise.domain.SalonAsserts.*;
import static fr.assistacrise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.assistacrise.IntegrationTest;
import fr.assistacrise.domain.Demande;
import fr.assistacrise.domain.Salon;
import fr.assistacrise.repository.SalonRepository;
import fr.assistacrise.service.SalonService;
import fr.assistacrise.service.dto.SalonDTO;
import fr.assistacrise.service.mapper.SalonMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SalonResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SalonResourceIT {

    private static final Instant DEFAULT_DATE_CREATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_CREATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/salons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SalonRepository salonRepository;

    @Mock
    private SalonRepository salonRepositoryMock;

    @Autowired
    private SalonMapper salonMapper;

    @Mock
    private SalonService salonServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSalonMockMvc;

    private Salon salon;

    private Salon insertedSalon;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salon createEntity(EntityManager em) {
        Salon salon = new Salon().dateCreation(DEFAULT_DATE_CREATION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).get(0);
        }
        salon.setDemande(demande);
        return salon;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Salon createUpdatedEntity(EntityManager em) {
        Salon updatedSalon = new Salon().dateCreation(UPDATED_DATE_CREATION);
        // Add required entity
        Demande demande;
        if (TestUtil.findAll(em, Demande.class).isEmpty()) {
            demande = DemandeResourceIT.createUpdatedEntity(em);
            em.persist(demande);
            em.flush();
        } else {
            demande = TestUtil.findAll(em, Demande.class).get(0);
        }
        updatedSalon.setDemande(demande);
        return updatedSalon;
    }

    @BeforeEach
    void initTest() {
        salon = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSalon != null) {
            salonRepository.delete(insertedSalon);
            insertedSalon = null;
        }
    }

    @Test
    @Transactional
    void createSalon() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);
        var returnedSalonDTO = om.readValue(
            restSalonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SalonDTO.class
        );

        // Validate the Salon in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSalon = salonMapper.toEntity(returnedSalonDTO);
        assertSalonUpdatableFieldsEquals(returnedSalon, getPersistedSalon(returnedSalon));

        insertedSalon = returnedSalon;
    }

    @Test
    @Transactional
    void createSalonWithExistingId() throws Exception {
        // Create the Salon with an existing ID
        salon.setId(1L);
        SalonDTO salonDTO = salonMapper.toDto(salon);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSalonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateCreationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        salon.setDateCreation(null);

        // Create the Salon, which fails.
        SalonDTO salonDTO = salonMapper.toDto(salon);

        restSalonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSalons() throws Exception {
        // Initialize the database
        insertedSalon = salonRepository.saveAndFlush(salon);

        // Get all the salonList
        restSalonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(salon.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateCreation").value(hasItem(DEFAULT_DATE_CREATION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalonsWithEagerRelationshipsIsEnabled() throws Exception {
        when(salonServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(salonServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSalonsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(salonServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSalonMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(salonRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSalon() throws Exception {
        // Initialize the database
        insertedSalon = salonRepository.saveAndFlush(salon);

        // Get the salon
        restSalonMockMvc
            .perform(get(ENTITY_API_URL_ID, salon.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(salon.getId().intValue()))
            .andExpect(jsonPath("$.dateCreation").value(DEFAULT_DATE_CREATION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSalon() throws Exception {
        // Get the salon
        restSalonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSalon() throws Exception {
        // Initialize the database
        insertedSalon = salonRepository.saveAndFlush(salon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salon
        Salon updatedSalon = salonRepository.findById(salon.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSalon are not directly saved in db
        em.detach(updatedSalon);
        updatedSalon.dateCreation(UPDATED_DATE_CREATION);
        SalonDTO salonDTO = salonMapper.toDto(updatedSalon);

        restSalonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salonDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDTO))
            )
            .andExpect(status().isOk());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSalonToMatchAllProperties(updatedSalon);
    }

    @Test
    @Transactional
    void putNonExistingSalon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salon.setId(longCount.incrementAndGet());

        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, salonDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSalon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salon.setId(longCount.incrementAndGet());

        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(salonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSalon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salon.setId(longCount.incrementAndGet());

        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(salonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSalonWithPatch() throws Exception {
        // Initialize the database
        insertedSalon = salonRepository.saveAndFlush(salon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salon using partial update
        Salon partialUpdatedSalon = new Salon();
        partialUpdatedSalon.setId(salon.getId());

        restSalonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalon))
            )
            .andExpect(status().isOk());

        // Validate the Salon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalonUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSalon, salon), getPersistedSalon(salon));
    }

    @Test
    @Transactional
    void fullUpdateSalonWithPatch() throws Exception {
        // Initialize the database
        insertedSalon = salonRepository.saveAndFlush(salon);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the salon using partial update
        Salon partialUpdatedSalon = new Salon();
        partialUpdatedSalon.setId(salon.getId());

        partialUpdatedSalon.dateCreation(UPDATED_DATE_CREATION);

        restSalonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSalon.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSalon))
            )
            .andExpect(status().isOk());

        // Validate the Salon in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSalonUpdatableFieldsEquals(partialUpdatedSalon, getPersistedSalon(partialUpdatedSalon));
    }

    @Test
    @Transactional
    void patchNonExistingSalon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salon.setId(longCount.incrementAndGet());

        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSalonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, salonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSalon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salon.setId(longCount.incrementAndGet());

        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(salonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSalon() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        salon.setId(longCount.incrementAndGet());

        // Create the Salon
        SalonDTO salonDTO = salonMapper.toDto(salon);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSalonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(salonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Salon in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSalon() throws Exception {
        // Initialize the database
        insertedSalon = salonRepository.saveAndFlush(salon);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the salon
        restSalonMockMvc
            .perform(delete(ENTITY_API_URL_ID, salon.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return salonRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Salon getPersistedSalon(Salon salon) {
        return salonRepository.findById(salon.getId()).orElseThrow();
    }

    protected void assertPersistedSalonToMatchAllProperties(Salon expectedSalon) {
        assertSalonAllPropertiesEquals(expectedSalon, getPersistedSalon(expectedSalon));
    }

    protected void assertPersistedSalonToMatchUpdatableProperties(Salon expectedSalon) {
        assertSalonAllUpdatablePropertiesEquals(expectedSalon, getPersistedSalon(expectedSalon));
    }
}
