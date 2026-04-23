package fr.assistacrise.repository;

import fr.assistacrise.domain.Moderation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Moderation entity.
 */
@Repository
public interface ModerationRepository extends JpaRepository<Moderation, Long> {
    default Optional<Moderation> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Moderation> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Moderation> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select moderation from Moderation moderation left join fetch moderation.administrateur left join fetch moderation.utilisateurCible",
        countQuery = "select count(moderation) from Moderation moderation"
    )
    Page<Moderation> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select moderation from Moderation moderation left join fetch moderation.administrateur left join fetch moderation.utilisateurCible"
    )
    List<Moderation> findAllWithToOneRelationships();

    @Query(
        "select moderation from Moderation moderation left join fetch moderation.administrateur left join fetch moderation.utilisateurCible where moderation.id =:id"
    )
    Optional<Moderation> findOneWithToOneRelationships(@Param("id") Long id);
}
