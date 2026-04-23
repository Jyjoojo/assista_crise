package fr.assistacrise.web.rest;

import fr.assistacrise.repository.ModerationRepository;
import fr.assistacrise.service.ModerationService;
import fr.assistacrise.service.dto.ModerationDTO;
import fr.assistacrise.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.assistacrise.domain.Moderation}.
 */
@RestController
@RequestMapping("/api/moderations")
public class ModerationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ModerationResource.class);

    private static final String ENTITY_NAME = "moderation";

    @Value("${jhipster.clientApp.name:assistaCrise}")
    private String applicationName;

    private final ModerationService moderationService;

    private final ModerationRepository moderationRepository;

    public ModerationResource(ModerationService moderationService, ModerationRepository moderationRepository) {
        this.moderationService = moderationService;
        this.moderationRepository = moderationRepository;
    }

    /**
     * {@code POST  /moderations} : Create a new moderation.
     *
     * @param moderationDTO the moderationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moderationDTO, or with status {@code 400 (Bad Request)} if the moderation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ModerationDTO> createModeration(@Valid @RequestBody ModerationDTO moderationDTO) throws URISyntaxException {
        LOG.debug("REST request to save Moderation : {}", moderationDTO);
        if (moderationDTO.getId() != null) {
            throw new BadRequestAlertException("A new moderation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moderationDTO = moderationService.save(moderationDTO);
        return ResponseEntity.created(new URI("/api/moderations/" + moderationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moderationDTO.getId().toString()))
            .body(moderationDTO);
    }

    /**
     * {@code PUT  /moderations/:id} : Updates an existing moderation.
     *
     * @param id the id of the moderationDTO to save.
     * @param moderationDTO the moderationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationDTO,
     * or with status {@code 400 (Bad Request)} if the moderationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moderationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ModerationDTO> updateModeration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ModerationDTO moderationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Moderation : {}, {}", id, moderationDTO);
        if (moderationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moderationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moderationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moderationDTO = moderationService.update(moderationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moderationDTO.getId().toString()))
            .body(moderationDTO);
    }

    /**
     * {@code PATCH  /moderations/:id} : Partial updates given fields of an existing moderation, field will ignore if it is null
     *
     * @param id the id of the moderationDTO to save.
     * @param moderationDTO the moderationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moderationDTO,
     * or with status {@code 400 (Bad Request)} if the moderationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moderationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moderationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ModerationDTO> partialUpdateModeration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ModerationDTO moderationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Moderation partially : {}, {}", id, moderationDTO);
        if (moderationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moderationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moderationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ModerationDTO> result = moderationService.partialUpdate(moderationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moderationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /moderations} : get all the Moderations.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of Moderations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ModerationDTO>> getAllModerations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Moderations");
        Page<ModerationDTO> page;
        if (eagerload) {
            page = moderationService.findAllWithEagerRelationships(pageable);
        } else {
            page = moderationService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /moderations/:id} : get the "id" moderation.
     *
     * @param id the id of the moderationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moderationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ModerationDTO> getModeration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Moderation : {}", id);
        Optional<ModerationDTO> moderationDTO = moderationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moderationDTO);
    }

    /**
     * {@code DELETE  /moderations/:id} : delete the "id" moderation.
     *
     * @param id the id of the moderationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteModeration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Moderation : {}", id);
        moderationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
