package fr.assistacrise.service;

import fr.assistacrise.domain.*; // for static metamodels
import fr.assistacrise.domain.Crise;
import fr.assistacrise.repository.CriseRepository;
import fr.assistacrise.service.criteria.CriseCriteria;
import fr.assistacrise.service.dto.CriseDTO;
import fr.assistacrise.service.mapper.CriseMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Crise} entities in the database.
 * The main input is a {@link CriseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CriseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CriseQueryService extends QueryService<Crise> {

    private static final Logger LOG = LoggerFactory.getLogger(CriseQueryService.class);

    private final CriseRepository criseRepository;

    private final CriseMapper criseMapper;

    public CriseQueryService(CriseRepository criseRepository, CriseMapper criseMapper) {
        this.criseRepository = criseRepository;
        this.criseMapper = criseMapper;
    }

    /**
     * Return a {@link List} of {@link CriseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CriseDTO> findByCriteria(CriseCriteria criteria) {
        LOG.debug("find by criteria : {}", criteria);
        final Specification<Crise> specification = createSpecification(criteria);
        return criseMapper.toDto(criseRepository.findAll(specification));
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CriseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Crise> specification = createSpecification(criteria);
        return criseRepository.count(specification);
    }

    /**
     * Function to convert {@link CriseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Crise> createSpecification(CriseCriteria criteria) {
        Specification<Crise> specification = Specification.unrestricted();
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : Specification.unrestricted(),
                buildRangeSpecification(criteria.getId(), Crise_.id),
                buildStringSpecification(criteria.getTitre(), Crise_.titre),
                buildStringSpecification(criteria.getDescription(), Crise_.description),
                buildSpecification(criteria.getType(), Crise_.type),
                buildSpecification(criteria.getStatut(), Crise_.statut),
                buildRangeSpecification(criteria.getDateDebut(), Crise_.dateDebut),
                buildRangeSpecification(criteria.getDateFermeture(), Crise_.dateFermeture),
                buildRangeSpecification(criteria.getLatitude(), Crise_.latitude),
                buildRangeSpecification(criteria.getLongitude(), Crise_.longitude),
                buildRangeSpecification(criteria.getRayonKm(), Crise_.rayonKm),
                buildSpecification(criteria.getDeclarantId(), root -> root.join(Crise_.declarant, JoinType.LEFT).get(Utilisateur_.id))
            );
        }
        return specification;
    }
}
