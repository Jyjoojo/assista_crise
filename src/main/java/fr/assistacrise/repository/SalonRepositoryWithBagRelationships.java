package fr.assistacrise.repository;

import fr.assistacrise.domain.Salon;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface SalonRepositoryWithBagRelationships {
    Optional<Salon> fetchBagRelationships(Optional<Salon> salon);

    List<Salon> fetchBagRelationships(List<Salon> salons);

    Page<Salon> fetchBagRelationships(Page<Salon> salons);
}
