package fr.assistacrise.web.rest;

import static fr.assistacrise.domain.ModerationAsserts.*;
import static fr.assistacrise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.assistacrise.IntegrationTest;
import fr.assistacrise.domain.Moderation;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.domain.enumeration.ActionModeration;
import fr.assistacrise.repository.ModerationRepository;
import fr.assistacrise.service.ModerationService;
import fr.assistacrise.service.dto.ModerationDTO;
import fr.assistacrise.service.mapper.ModerationMapper;
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
 * Integration tests for the {@link ModerationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ModerationResourceIT {

    private static final String DEFAULT_MOTIF = "AAAAAAAAAA";
    private static final String UPDATED_MOTIF = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_MODERATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MODERATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ActionModeration DEFAULT_ACTION = ActionModeration.AVERTISSEMENT;
    private static final ActionModeration UPDATED_ACTION = ActionModeration.SUPPRESSION_ANNONCE;

    private static final String ENTITY_API_URL = "/api/moderations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ModerationRepository moderationRepository;

    @Mock
    private ModerationRepository moderationRepositoryMock;

    @Autowired
    private ModerationMapper moderationMapper;

    @Mock
    private ModerationService moderationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restModerationMockMvc;

    private Moderation moderation;

    private Moderation insertedModeration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moderation createEntity(EntityManager em) {
        Moderation moderation = new Moderation().motif(DEFAULT_MOTIF).dateModeration(DEFAULT_DATE_MODERATION).action(DEFAULT_ACTION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        moderation.setAdministrateur(utilisateur);
        return moderation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moderation createUpdatedEntity(EntityManager em) {
        Moderation updatedModeration = new Moderation().motif(UPDATED_MOTIF).dateModeration(UPDATED_DATE_MODERATION).action(UPDATED_ACTION);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedModeration.setAdministrateur(utilisateur);
        return updatedModeration;
    }

    @BeforeEach
    void initTest() {
        moderation = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedModeration != null) {
            moderationRepository.delete(insertedModeration);
            insertedModeration = null;
        }
    }

    @Test
    @Transactional
    void createModeration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);
        var returnedModerationDTO = om.readValue(
            restModerationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ModerationDTO.class
        );

        // Validate the Moderation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedModeration = moderationMapper.toEntity(returnedModerationDTO);
        assertModerationUpdatableFieldsEquals(returnedModeration, getPersistedModeration(returnedModeration));

        insertedModeration = returnedModeration;
    }

    @Test
    @Transactional
    void createModerationWithExistingId() throws Exception {
        // Create the Moderation with an existing ID
        moderation.setId(1L);
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restModerationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMotifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moderation.setMotif(null);

        // Create the Moderation, which fails.
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        restModerationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateModerationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moderation.setDateModeration(null);

        // Create the Moderation, which fails.
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        restModerationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moderation.setAction(null);

        // Create the Moderation, which fails.
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        restModerationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllModerations() throws Exception {
        // Initialize the database
        insertedModeration = moderationRepository.saveAndFlush(moderation);

        // Get all the moderationList
        restModerationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moderation.getId().intValue())))
            .andExpect(jsonPath("$.[*].motif").value(hasItem(DEFAULT_MOTIF)))
            .andExpect(jsonPath("$.[*].dateModeration").value(hasItem(DEFAULT_DATE_MODERATION.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModerationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(moderationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModerationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moderationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllModerationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moderationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restModerationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moderationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getModeration() throws Exception {
        // Initialize the database
        insertedModeration = moderationRepository.saveAndFlush(moderation);

        // Get the moderation
        restModerationMockMvc
            .perform(get(ENTITY_API_URL_ID, moderation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moderation.getId().intValue()))
            .andExpect(jsonPath("$.motif").value(DEFAULT_MOTIF))
            .andExpect(jsonPath("$.dateModeration").value(DEFAULT_DATE_MODERATION.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingModeration() throws Exception {
        // Get the moderation
        restModerationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingModeration() throws Exception {
        // Initialize the database
        insertedModeration = moderationRepository.saveAndFlush(moderation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderation
        Moderation updatedModeration = moderationRepository.findById(moderation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedModeration are not directly saved in db
        em.detach(updatedModeration);
        updatedModeration.motif(UPDATED_MOTIF).dateModeration(UPDATED_DATE_MODERATION).action(UPDATED_ACTION);
        ModerationDTO moderationDTO = moderationMapper.toDto(updatedModeration);

        restModerationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moderationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedModerationToMatchAllProperties(updatedModeration);
    }

    @Test
    @Transactional
    void putNonExistingModeration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderation.setId(longCount.incrementAndGet());

        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moderationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchModeration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderation.setId(longCount.incrementAndGet());

        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moderationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamModeration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderation.setId(longCount.incrementAndGet());

        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moderationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateModerationWithPatch() throws Exception {
        // Initialize the database
        insertedModeration = moderationRepository.saveAndFlush(moderation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderation using partial update
        Moderation partialUpdatedModeration = new Moderation();
        partialUpdatedModeration.setId(moderation.getId());

        partialUpdatedModeration.motif(UPDATED_MOTIF).dateModeration(UPDATED_DATE_MODERATION).action(UPDATED_ACTION);

        restModerationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModeration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModeration))
            )
            .andExpect(status().isOk());

        // Validate the Moderation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModerationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedModeration, moderation),
            getPersistedModeration(moderation)
        );
    }

    @Test
    @Transactional
    void fullUpdateModerationWithPatch() throws Exception {
        // Initialize the database
        insertedModeration = moderationRepository.saveAndFlush(moderation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moderation using partial update
        Moderation partialUpdatedModeration = new Moderation();
        partialUpdatedModeration.setId(moderation.getId());

        partialUpdatedModeration.motif(UPDATED_MOTIF).dateModeration(UPDATED_DATE_MODERATION).action(UPDATED_ACTION);

        restModerationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedModeration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedModeration))
            )
            .andExpect(status().isOk());

        // Validate the Moderation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertModerationUpdatableFieldsEquals(partialUpdatedModeration, getPersistedModeration(partialUpdatedModeration));
    }

    @Test
    @Transactional
    void patchNonExistingModeration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderation.setId(longCount.incrementAndGet());

        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restModerationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moderationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moderationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchModeration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderation.setId(longCount.incrementAndGet());

        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moderationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamModeration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moderation.setId(longCount.incrementAndGet());

        // Create the Moderation
        ModerationDTO moderationDTO = moderationMapper.toDto(moderation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restModerationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moderationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moderation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteModeration() throws Exception {
        // Initialize the database
        insertedModeration = moderationRepository.saveAndFlush(moderation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moderation
        restModerationMockMvc
            .perform(delete(ENTITY_API_URL_ID, moderation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moderationRepository.count();
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

    protected Moderation getPersistedModeration(Moderation moderation) {
        return moderationRepository.findById(moderation.getId()).orElseThrow();
    }

    protected void assertPersistedModerationToMatchAllProperties(Moderation expectedModeration) {
        assertModerationAllPropertiesEquals(expectedModeration, getPersistedModeration(expectedModeration));
    }

    protected void assertPersistedModerationToMatchUpdatableProperties(Moderation expectedModeration) {
        assertModerationAllUpdatablePropertiesEquals(expectedModeration, getPersistedModeration(expectedModeration));
    }
}
