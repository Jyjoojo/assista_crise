package fr.assistacrise.repository;

import fr.assistacrise.domain.Demande;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class DemandeRepositoryWithBagRelationshipsImpl implements DemandeRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String DEMANDES_PARAMETER = "demandes";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Demande> fetchBagRelationships(Optional<Demande> demande) {
        return demande.map(this::fetchOffres);
    }

    @Override
    public Page<Demande> fetchBagRelationships(Page<Demande> demandes) {
        return new PageImpl<>(fetchBagRelationships(demandes.getContent()), demandes.getPageable(), demandes.getTotalElements());
    }

    @Override
    public List<Demande> fetchBagRelationships(List<Demande> demandes) {
        return Optional.of(demandes).map(this::fetchOffres).orElse(Collections.emptyList());
    }

    Demande fetchOffres(Demande result) {
        return entityManager
            .createQuery("select demande from Demande demande left join fetch demande.offres where demande.id = :id", Demande.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Demande> fetchOffres(List<Demande> demandes) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, demandes.size()).forEach(index -> order.put(demandes.get(index).getId(), index));
        List<Demande> result = entityManager
            .createQuery("select demande from Demande demande left join fetch demande.offres where demande in :demandes", Demande.class)
            .setParameter(DEMANDES_PARAMETER, demandes)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
