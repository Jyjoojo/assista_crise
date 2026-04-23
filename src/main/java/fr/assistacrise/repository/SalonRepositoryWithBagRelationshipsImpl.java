package fr.assistacrise.repository;

import fr.assistacrise.domain.Salon;
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
public class SalonRepositoryWithBagRelationshipsImpl implements SalonRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String SALONS_PARAMETER = "salons";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Salon> fetchBagRelationships(Optional<Salon> salon) {
        return salon.map(this::fetchParticipants);
    }

    @Override
    public Page<Salon> fetchBagRelationships(Page<Salon> salons) {
        return new PageImpl<>(fetchBagRelationships(salons.getContent()), salons.getPageable(), salons.getTotalElements());
    }

    @Override
    public List<Salon> fetchBagRelationships(List<Salon> salons) {
        return Optional.of(salons).map(this::fetchParticipants).orElse(Collections.emptyList());
    }

    Salon fetchParticipants(Salon result) {
        return entityManager
            .createQuery("select salon from Salon salon left join fetch salon.participants where salon.id = :id", Salon.class)
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<Salon> fetchParticipants(List<Salon> salons) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, salons.size()).forEach(index -> order.put(salons.get(index).getId(), index));
        List<Salon> result = entityManager
            .createQuery("select salon from Salon salon left join fetch salon.participants where salon in :salons", Salon.class)
            .setParameter(SALONS_PARAMETER, salons)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
