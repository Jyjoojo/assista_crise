package fr.assistacrise.service;

import fr.assistacrise.service.dto.ModerationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.assistacrise.domain.Moderation}.
 */
public interface ModerationService {
    /**
     * Save a moderation.
     *
     * @param moderationDTO the entity to save.
     * @return the persisted entity.
     */
    ModerationDTO save(ModerationDTO moderationDTO);

    /**
     * Updates a moderation.
     *
     * @param moderationDTO the entity to update.
     * @return the persisted entity.
     */
    ModerationDTO update(ModerationDTO moderationDTO);

    /**
     * Partially updates a moderation.
     *
     * @param moderationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ModerationDTO> partialUpdate(ModerationDTO moderationDTO);

    /**
     * Get all the moderations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ModerationDTO> findAll(Pageable pageable);

    /**
     * Get all the moderations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ModerationDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" moderation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ModerationDTO> findOne(Long id);

    /**
     * Delete the "id" moderation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
