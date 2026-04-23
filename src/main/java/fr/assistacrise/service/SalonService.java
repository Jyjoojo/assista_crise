package fr.assistacrise.service;

import fr.assistacrise.service.dto.SalonDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.assistacrise.domain.Salon}.
 */
public interface SalonService {
    /**
     * Save a salon.
     *
     * @param salonDTO the entity to save.
     * @return the persisted entity.
     */
    SalonDTO save(SalonDTO salonDTO);

    /**
     * Updates a salon.
     *
     * @param salonDTO the entity to update.
     * @return the persisted entity.
     */
    SalonDTO update(SalonDTO salonDTO);

    /**
     * Partially updates a salon.
     *
     * @param salonDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SalonDTO> partialUpdate(SalonDTO salonDTO);

    /**
     * Get all the salons.
     *
     * @return the list of entities.
     */
    List<SalonDTO> findAll();

    /**
     * Get all the salons with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SalonDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" salon.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SalonDTO> findOne(Long id);

    /**
     * Delete the "id" salon.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
