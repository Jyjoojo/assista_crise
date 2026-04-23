package fr.assistacrise.web.rest;

import static fr.assistacrise.domain.UtilisateurAsserts.*;
import static fr.assistacrise.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.assistacrise.IntegrationTest;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.domain.enumeration.RoleUtilisateur;
import fr.assistacrise.repository.UtilisateurRepository;
import fr.assistacrise.service.dto.UtilisateurDTO;
import fr.assistacrise.service.mapper.UtilisateurMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UtilisateurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UtilisateurResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "wy&U(@BAP.[";
    private static final String UPDATED_EMAIL = "dHkZ@=ddX.,p7H";

    private static final String DEFAULT_MOT_DE_PASSE = "AAAAAAAAAA";
    private static final String UPDATED_MOT_DE_PASSE = "BBBBBBBBBB";

    private static final String DEFAULT_PRENOM = "AAAAAAAAAA";
    private static final String UPDATED_PRENOM = "BBBBBBBBBB";

    private static final String DEFAULT_NOM = "AAAAAAAAAA";
    private static final String UPDATED_NOM = "BBBBBBBBBB";

    private static final String DEFAULT_TELEPHONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEPHONE = "BBBBBBBBBB";

    private static final RoleUtilisateur DEFAULT_ROLE = RoleUtilisateur.SINISTRE;
    private static final RoleUtilisateur UPDATED_ROLE = RoleUtilisateur.CITOYEN_AIDANT;

    private static final Boolean DEFAULT_ACTIF = false;
    private static final Boolean UPDATED_ACTIF = true;

    private static final Instant DEFAULT_DATE_INSCRIPTION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_INSCRIPTION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATE_BANNISSEMENT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_BANNISSEMENT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_EST_BANNI = false;
    private static final Boolean UPDATED_EST_BANNI = true;

    private static final String ENTITY_API_URL = "/api/utilisateurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUtilisateurMockMvc;

    private Utilisateur utilisateur;

    private Utilisateur insertedUtilisateur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createEntity() {
        return new Utilisateur()
            .login(DEFAULT_LOGIN)
            .email(DEFAULT_EMAIL)
            .motDePasse(DEFAULT_MOT_DE_PASSE)
            .prenom(DEFAULT_PRENOM)
            .nom(DEFAULT_NOM)
            .telephone(DEFAULT_TELEPHONE)
            .role(DEFAULT_ROLE)
            .actif(DEFAULT_ACTIF)
            .dateInscription(DEFAULT_DATE_INSCRIPTION)
            .dateBannissement(DEFAULT_DATE_BANNISSEMENT)
            .estBanni(DEFAULT_EST_BANNI);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Utilisateur createUpdatedEntity() {
        return new Utilisateur()
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .telephone(UPDATED_TELEPHONE)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .dateBannissement(UPDATED_DATE_BANNISSEMENT)
            .estBanni(UPDATED_EST_BANNI);
    }

    @BeforeEach
    void initTest() {
        utilisateur = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedUtilisateur != null) {
            utilisateurRepository.delete(insertedUtilisateur);
            insertedUtilisateur = null;
        }
    }

    @Test
    @Transactional
    void createUtilisateur() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);
        var returnedUtilisateurDTO = om.readValue(
            restUtilisateurMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UtilisateurDTO.class
        );

        // Validate the Utilisateur in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUtilisateur = utilisateurMapper.toEntity(returnedUtilisateurDTO);
        assertUtilisateurUpdatableFieldsEquals(returnedUtilisateur, getPersistedUtilisateur(returnedUtilisateur));

        insertedUtilisateur = returnedUtilisateur;
    }

    @Test
    @Transactional
    void createUtilisateurWithExistingId() throws Exception {
        // Create the Utilisateur with an existing ID
        utilisateur.setId(1L);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setLogin(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setEmail(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMotDePasseIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setMotDePasse(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrenomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setPrenom(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setNom(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRoleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setRole(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActifIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setActif(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDateInscriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setDateInscription(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstBanniIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        utilisateur.setEstBanni(null);

        // Create the Utilisateur, which fails.
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        restUtilisateurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUtilisateurs() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get all the utilisateurList
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(utilisateur.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].motDePasse").value(hasItem(DEFAULT_MOT_DE_PASSE)))
            .andExpect(jsonPath("$.[*].prenom").value(hasItem(DEFAULT_PRENOM)))
            .andExpect(jsonPath("$.[*].nom").value(hasItem(DEFAULT_NOM)))
            .andExpect(jsonPath("$.[*].telephone").value(hasItem(DEFAULT_TELEPHONE)))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].actif").value(hasItem(DEFAULT_ACTIF)))
            .andExpect(jsonPath("$.[*].dateInscription").value(hasItem(DEFAULT_DATE_INSCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].dateBannissement").value(hasItem(DEFAULT_DATE_BANNISSEMENT.toString())))
            .andExpect(jsonPath("$.[*].estBanni").value(hasItem(DEFAULT_EST_BANNI)));
    }

    @Test
    @Transactional
    void getUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        // Get the utilisateur
        restUtilisateurMockMvc
            .perform(get(ENTITY_API_URL_ID, utilisateur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(utilisateur.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.motDePasse").value(DEFAULT_MOT_DE_PASSE))
            .andExpect(jsonPath("$.prenom").value(DEFAULT_PRENOM))
            .andExpect(jsonPath("$.nom").value(DEFAULT_NOM))
            .andExpect(jsonPath("$.telephone").value(DEFAULT_TELEPHONE))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
            .andExpect(jsonPath("$.actif").value(DEFAULT_ACTIF))
            .andExpect(jsonPath("$.dateInscription").value(DEFAULT_DATE_INSCRIPTION.toString()))
            .andExpect(jsonPath("$.dateBannissement").value(DEFAULT_DATE_BANNISSEMENT.toString()))
            .andExpect(jsonPath("$.estBanni").value(DEFAULT_EST_BANNI));
    }

    @Test
    @Transactional
    void getNonExistingUtilisateur() throws Exception {
        // Get the utilisateur
        restUtilisateurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur
        Utilisateur updatedUtilisateur = utilisateurRepository.findById(utilisateur.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUtilisateur are not directly saved in db
        em.detach(updatedUtilisateur);
        updatedUtilisateur
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .telephone(UPDATED_TELEPHONE)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .dateBannissement(UPDATED_DATE_BANNISSEMENT)
            .estBanni(UPDATED_EST_BANNI);
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(updatedUtilisateur);

        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUtilisateurToMatchAllProperties(updatedUtilisateur);
    }

    @Test
    @Transactional
    void putNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur.login(UPDATED_LOGIN).motDePasse(UPDATED_MOT_DE_PASSE).telephone(UPDATED_TELEPHONE).role(UPDATED_ROLE);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUtilisateur, utilisateur),
            getPersistedUtilisateur(utilisateur)
        );
    }

    @Test
    @Transactional
    void fullUpdateUtilisateurWithPatch() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the utilisateur using partial update
        Utilisateur partialUpdatedUtilisateur = new Utilisateur();
        partialUpdatedUtilisateur.setId(utilisateur.getId());

        partialUpdatedUtilisateur
            .login(UPDATED_LOGIN)
            .email(UPDATED_EMAIL)
            .motDePasse(UPDATED_MOT_DE_PASSE)
            .prenom(UPDATED_PRENOM)
            .nom(UPDATED_NOM)
            .telephone(UPDATED_TELEPHONE)
            .role(UPDATED_ROLE)
            .actif(UPDATED_ACTIF)
            .dateInscription(UPDATED_DATE_INSCRIPTION)
            .dateBannissement(UPDATED_DATE_BANNISSEMENT)
            .estBanni(UPDATED_EST_BANNI);

        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUtilisateur.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUtilisateur))
            )
            .andExpect(status().isOk());

        // Validate the Utilisateur in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUtilisateurUpdatableFieldsEquals(partialUpdatedUtilisateur, getPersistedUtilisateur(partialUpdatedUtilisateur));
    }

    @Test
    @Transactional
    void patchNonExistingUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, utilisateurDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(utilisateurDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUtilisateur() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        utilisateur.setId(longCount.incrementAndGet());

        // Create the Utilisateur
        UtilisateurDTO utilisateurDTO = utilisateurMapper.toDto(utilisateur);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUtilisateurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(utilisateurDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Utilisateur in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUtilisateur() throws Exception {
        // Initialize the database
        insertedUtilisateur = utilisateurRepository.saveAndFlush(utilisateur);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the utilisateur
        restUtilisateurMockMvc
            .perform(delete(ENTITY_API_URL_ID, utilisateur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return utilisateurRepository.count();
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

    protected Utilisateur getPersistedUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.findById(utilisateur.getId()).orElseThrow();
    }

    protected void assertPersistedUtilisateurToMatchAllProperties(Utilisateur expectedUtilisateur) {
        assertUtilisateurAllPropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }

    protected void assertPersistedUtilisateurToMatchUpdatableProperties(Utilisateur expectedUtilisateur) {
        assertUtilisateurAllUpdatablePropertiesEquals(expectedUtilisateur, getPersistedUtilisateur(expectedUtilisateur));
    }
}
