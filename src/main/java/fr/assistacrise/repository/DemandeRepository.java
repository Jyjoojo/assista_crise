package fr.assistacrise.repository;

import fr.assistacrise.domain.Demande;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Demande entity.
 *
 * When extending this class, extend DemandeRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface DemandeRepository
    extends DemandeRepositoryWithBagRelationships, JpaRepository<Demande, Long>, JpaSpecificationExecutor<Demande>
{
    default Optional<Demande> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Demande> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Demande> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select demande from Demande demande left join fetch demande.sinistre left join fetch demande.crise",
        countQuery = "select count(demande) from Demande demande"
    )
    Page<Demande> findAllWithToOneRelationships(Pageable pageable);

    @Query("select demande from Demande demande left join fetch demande.sinistre left join fetch demande.crise")
    List<Demande> findAllWithToOneRelationships();

    @Query("select demande from Demande demande left join fetch demande.sinistre left join fetch demande.crise where demande.id =:id")
    Optional<Demande> findOneWithToOneRelationships(@Param("id") Long id);
}
