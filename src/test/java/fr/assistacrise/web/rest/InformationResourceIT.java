package fr.assistacrise.web.rest;

import static fr.assistacrise.domain.InformationAsserts.*;
import static fr.assistacrise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.assistacrise.IntegrationTest;
import fr.assistacrise.domain.Crise;
import fr.assistacrise.domain.Information;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.repository.InformationRepository;
import fr.assistacrise.service.InformationService;
import fr.assistacrise.service.dto.InformationDTO;
import fr.assistacrise.service.mapper.InformationMapper;
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
 * Integration tests for the {@link InformationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InformationResourceIT {

    private static final String DEFAULT_TITRE = "AAAAAAAAAA";
    private static final String UPDATED_TITRE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENU = "AAAAAAAAAA";
    private static final String UPDATED_CONTENU = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_PUBLICATION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_PUBLICATION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_MISE_A_JOUR = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_MISE_A_JOUR = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_EST_VISIBLE = false;
    private static final Boolean UPDATED_EST_VISIBLE = true;

    private static final String ENTITY_API_URL = "/api/information";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InformationRepository informationRepository;

    @Mock
    private InformationRepository informationRepositoryMock;

    @Autowired
    private InformationMapper informationMapper;

    @Mock
    private InformationService informationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInformationMockMvc;

    private Information information;

    private Information insertedInformation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createEntity(EntityManager em) {
        Information information = new Information()
            .titre(DEFAULT_TITRE)
            .contenu(DEFAULT_CONTENU)
            .datePublication(DEFAULT_DATE_PUBLICATION)
            .dateMiseAJour(DEFAULT_DATE_MISE_A_JOUR)
            .estVisible(DEFAULT_EST_VISIBLE);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createEntity(em);
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        information.setCrise(crise);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        information.setAuteur(utilisateur);
        return information;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Information createUpdatedEntity(EntityManager em) {
        Information updatedInformation = new Information()
            .titre(UPDATED_TITRE)
            .contenu(UPDATED_CONTENU)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .dateMiseAJour(UPDATED_DATE_MISE_A_JOUR)
            .estVisible(UPDATED_EST_VISIBLE);
        // Add required entity
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            crise = CriseResourceIT.createUpdatedEntity(em);
            em.persist(crise);
            em.flush();
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        updatedInformation.setCrise(crise);
        // Add required entity
        Utilisateur utilisateur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            utilisateur = UtilisateurResourceIT.createUpdatedEntity();
            em.persist(utilisateur);
            em.flush();
        } else {
            utilisateur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        updatedInformation.setAuteur(utilisateur);
        return updatedInformation;
    }

    @BeforeEach
    void initTest() {
        information = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedInformation != null) {
            informationRepository.delete(insertedInformation);
            insertedInformation = null;
        }
    }

    @Test
    @Transactional
    void createInformation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);
        var returnedInformationDTO = om.readValue(
            restInformationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InformationDTO.class
        );

        // Validate the Information in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInformation = informationMapper.toEntity(returnedInformationDTO);
        assertInformationUpdatableFieldsEquals(returnedInformation, getPersistedInformation(returnedInformation));

        insertedInformation = returnedInformation;
    }

    @Test
    @Transactional
    void createInformationWithExistingId() throws Exception {
        // Create the Information with an existing ID
        information.setId(1L);
        InformationDTO informationDTO = informationMapper.toDto(information);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        information.setTitre(null);

        // Create the Information, which fails.
        InformationDTO informationDTO = informationMapper.toDto(information);

        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContenuIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        information.setContenu(null);

        // Create the Information, which fails.
        InformationDTO informationDTO = informationMapper.toDto(information);

        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDatePublicationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        information.setDatePublication(null);

        // Create the Information, which fails.
        InformationDTO informationDTO = informationMapper.toDto(information);

        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstVisibleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        information.setEstVisible(null);

        // Create the Information, which fails.
        InformationDTO informationDTO = informationMapper.toDto(information);

        restInformationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInformations() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList
        restInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(information.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())))
            .andExpect(jsonPath("$.[*].dateMiseAJour").value(hasItem(DEFAULT_DATE_MISE_A_JOUR.toString())))
            .andExpect(jsonPath("$.[*].estVisible").value(hasItem(DEFAULT_EST_VISIBLE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInformationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(informationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInformationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(informationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInformationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(informationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInformationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(informationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInformation() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get the information
        restInformationMockMvc
            .perform(get(ENTITY_API_URL_ID, information.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(information.getId().intValue()))
            .andExpect(jsonPath("$.titre").value(DEFAULT_TITRE))
            .andExpect(jsonPath("$.contenu").value(DEFAULT_CONTENU))
            .andExpect(jsonPath("$.datePublication").value(DEFAULT_DATE_PUBLICATION.toString()))
            .andExpect(jsonPath("$.dateMiseAJour").value(DEFAULT_DATE_MISE_A_JOUR.toString()))
            .andExpect(jsonPath("$.estVisible").value(DEFAULT_EST_VISIBLE));
    }

    @Test
    @Transactional
    void getInformationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        Long id = information.getId();

        defaultInformationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInformationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInformationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInformationsByTitreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where titre equals to
        defaultInformationFiltering("titre.equals=" + DEFAULT_TITRE, "titre.equals=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllInformationsByTitreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where titre in
        defaultInformationFiltering("titre.in=" + DEFAULT_TITRE + "," + UPDATED_TITRE, "titre.in=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllInformationsByTitreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where titre is not null
        defaultInformationFiltering("titre.specified=true", "titre.specified=false");
    }

    @Test
    @Transactional
    void getAllInformationsByTitreContainsSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where titre contains
        defaultInformationFiltering("titre.contains=" + DEFAULT_TITRE, "titre.contains=" + UPDATED_TITRE);
    }

    @Test
    @Transactional
    void getAllInformationsByTitreNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where titre does not contain
        defaultInformationFiltering("titre.doesNotContain=" + UPDATED_TITRE, "titre.doesNotContain=" + DEFAULT_TITRE);
    }

    @Test
    @Transactional
    void getAllInformationsByContenuIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where contenu equals to
        defaultInformationFiltering("contenu.equals=" + DEFAULT_CONTENU, "contenu.equals=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllInformationsByContenuIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where contenu in
        defaultInformationFiltering("contenu.in=" + DEFAULT_CONTENU + "," + UPDATED_CONTENU, "contenu.in=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllInformationsByContenuIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where contenu is not null
        defaultInformationFiltering("contenu.specified=true", "contenu.specified=false");
    }

    @Test
    @Transactional
    void getAllInformationsByContenuContainsSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where contenu contains
        defaultInformationFiltering("contenu.contains=" + DEFAULT_CONTENU, "contenu.contains=" + UPDATED_CONTENU);
    }

    @Test
    @Transactional
    void getAllInformationsByContenuNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where contenu does not contain
        defaultInformationFiltering("contenu.doesNotContain=" + UPDATED_CONTENU, "contenu.doesNotContain=" + DEFAULT_CONTENU);
    }

    @Test
    @Transactional
    void getAllInformationsByDatePublicationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where datePublication equals to
        defaultInformationFiltering(
            "datePublication.equals=" + DEFAULT_DATE_PUBLICATION,
            "datePublication.equals=" + UPDATED_DATE_PUBLICATION
        );
    }

    @Test
    @Transactional
    void getAllInformationsByDatePublicationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where datePublication in
        defaultInformationFiltering(
            "datePublication.in=" + DEFAULT_DATE_PUBLICATION + "," + UPDATED_DATE_PUBLICATION,
            "datePublication.in=" + UPDATED_DATE_PUBLICATION
        );
    }

    @Test
    @Transactional
    void getAllInformationsByDatePublicationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where datePublication is not null
        defaultInformationFiltering("datePublication.specified=true", "datePublication.specified=false");
    }

    @Test
    @Transactional
    void getAllInformationsByDateMiseAJourIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where dateMiseAJour equals to
        defaultInformationFiltering("dateMiseAJour.equals=" + DEFAULT_DATE_MISE_A_JOUR, "dateMiseAJour.equals=" + UPDATED_DATE_MISE_A_JOUR);
    }

    @Test
    @Transactional
    void getAllInformationsByDateMiseAJourIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where dateMiseAJour in
        defaultInformationFiltering(
            "dateMiseAJour.in=" + DEFAULT_DATE_MISE_A_JOUR + "," + UPDATED_DATE_MISE_A_JOUR,
            "dateMiseAJour.in=" + UPDATED_DATE_MISE_A_JOUR
        );
    }

    @Test
    @Transactional
    void getAllInformationsByDateMiseAJourIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where dateMiseAJour is not null
        defaultInformationFiltering("dateMiseAJour.specified=true", "dateMiseAJour.specified=false");
    }

    @Test
    @Transactional
    void getAllInformationsByEstVisibleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where estVisible equals to
        defaultInformationFiltering("estVisible.equals=" + DEFAULT_EST_VISIBLE, "estVisible.equals=" + UPDATED_EST_VISIBLE);
    }

    @Test
    @Transactional
    void getAllInformationsByEstVisibleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where estVisible in
        defaultInformationFiltering(
            "estVisible.in=" + DEFAULT_EST_VISIBLE + "," + UPDATED_EST_VISIBLE,
            "estVisible.in=" + UPDATED_EST_VISIBLE
        );
    }

    @Test
    @Transactional
    void getAllInformationsByEstVisibleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        // Get all the informationList where estVisible is not null
        defaultInformationFiltering("estVisible.specified=true", "estVisible.specified=false");
    }

    @Test
    @Transactional
    void getAllInformationsByCriseIsEqualToSomething() throws Exception {
        Crise crise;
        if (TestUtil.findAll(em, Crise.class).isEmpty()) {
            informationRepository.saveAndFlush(information);
            crise = CriseResourceIT.createEntity(em);
        } else {
            crise = TestUtil.findAll(em, Crise.class).get(0);
        }
        em.persist(crise);
        em.flush();
        information.setCrise(crise);
        informationRepository.saveAndFlush(information);
        Long criseId = crise.getId();
        // Get all the informationList where crise equals to criseId
        defaultInformationShouldBeFound("criseId.equals=" + criseId);

        // Get all the informationList where crise equals to (criseId + 1)
        defaultInformationShouldNotBeFound("criseId.equals=" + (criseId + 1));
    }

    @Test
    @Transactional
    void getAllInformationsByAuteurIsEqualToSomething() throws Exception {
        Utilisateur auteur;
        if (TestUtil.findAll(em, Utilisateur.class).isEmpty()) {
            informationRepository.saveAndFlush(information);
            auteur = UtilisateurResourceIT.createEntity();
        } else {
            auteur = TestUtil.findAll(em, Utilisateur.class).get(0);
        }
        em.persist(auteur);
        em.flush();
        information.setAuteur(auteur);
        informationRepository.saveAndFlush(information);
        Long auteurId = auteur.getId();
        // Get all the informationList where auteur equals to auteurId
        defaultInformationShouldBeFound("auteurId.equals=" + auteurId);

        // Get all the informationList where auteur equals to (auteurId + 1)
        defaultInformationShouldNotBeFound("auteurId.equals=" + (auteurId + 1));
    }

    private void defaultInformationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInformationShouldBeFound(shouldBeFound);
        defaultInformationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInformationShouldBeFound(String filter) throws Exception {
        restInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(information.getId().intValue())))
            .andExpect(jsonPath("$.[*].titre").value(hasItem(DEFAULT_TITRE)))
            .andExpect(jsonPath("$.[*].contenu").value(hasItem(DEFAULT_CONTENU)))
            .andExpect(jsonPath("$.[*].datePublication").value(hasItem(DEFAULT_DATE_PUBLICATION.toString())))
            .andExpect(jsonPath("$.[*].dateMiseAJour").value(hasItem(DEFAULT_DATE_MISE_A_JOUR.toString())))
            .andExpect(jsonPath("$.[*].estVisible").value(hasItem(DEFAULT_EST_VISIBLE)));

        // Check, that the count call also returns 1
        restInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInformationShouldNotBeFound(String filter) throws Exception {
        restInformationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInformationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInformation() throws Exception {
        // Get the information
        restInformationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInformation() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the information
        Information updatedInformation = informationRepository.findById(information.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInformation are not directly saved in db
        em.detach(updatedInformation);
        updatedInformation
            .titre(UPDATED_TITRE)
            .contenu(UPDATED_CONTENU)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .dateMiseAJour(UPDATED_DATE_MISE_A_JOUR)
            .estVisible(UPDATED_EST_VISIBLE);
        InformationDTO informationDTO = informationMapper.toDto(updatedInformation);

        restInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(informationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInformationToMatchAllProperties(updatedInformation);
    }

    @Test
    @Transactional
    void putNonExistingInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, informationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInformationWithPatch() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the information using partial update
        Information partialUpdatedInformation = new Information();
        partialUpdatedInformation.setId(information.getId());

        partialUpdatedInformation.dateMiseAJour(UPDATED_DATE_MISE_A_JOUR);

        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInformation))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInformationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInformation, information),
            getPersistedInformation(information)
        );
    }

    @Test
    @Transactional
    void fullUpdateInformationWithPatch() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the information using partial update
        Information partialUpdatedInformation = new Information();
        partialUpdatedInformation.setId(information.getId());

        partialUpdatedInformation
            .titre(UPDATED_TITRE)
            .contenu(UPDATED_CONTENU)
            .datePublication(UPDATED_DATE_PUBLICATION)
            .dateMiseAJour(UPDATED_DATE_MISE_A_JOUR)
            .estVisible(UPDATED_EST_VISIBLE);

        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInformation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInformation))
            )
            .andExpect(status().isOk());

        // Validate the Information in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInformationUpdatableFieldsEquals(partialUpdatedInformation, getPersistedInformation(partialUpdatedInformation));
    }

    @Test
    @Transactional
    void patchNonExistingInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, informationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(informationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInformation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        information.setId(longCount.incrementAndGet());

        // Create the Information
        InformationDTO informationDTO = informationMapper.toDto(information);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInformationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(informationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Information in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInformation() throws Exception {
        // Initialize the database
        insertedInformation = informationRepository.saveAndFlush(information);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the information
        restInformationMockMvc
            .perform(delete(ENTITY_API_URL_ID, information.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return informationRepository.count();
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

    protected Information getPersistedInformation(Information information) {
        return informationRepository.findById(information.getId()).orElseThrow();
    }

    protected void assertPersistedInformationToMatchAllProperties(Information expectedInformation) {
        assertInformationAllPropertiesEquals(expectedInformation, getPersistedInformation(expectedInformation));
    }

    protected void assertPersistedInformationToMatchUpdatableProperties(Information expectedInformation) {
        assertInformationAllUpdatablePropertiesEquals(expectedInformation, getPersistedInformation(expectedInformation));
    }
}
