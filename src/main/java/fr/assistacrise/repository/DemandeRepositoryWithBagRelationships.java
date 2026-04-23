package fr.assistacrise.repository;

import fr.assistacrise.domain.Demande;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface DemandeRepositoryWithBagRelationships {
    Optional<Demande> fetchBagRelationships(Optional<Demande> demande);

    List<Demande> fetchBagRelationships(List<Demande> demandes);

    Page<Demande> fetchBagRelationships(Page<Demande> demandes);
}
