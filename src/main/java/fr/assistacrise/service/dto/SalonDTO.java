package fr.assistacrise.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link fr.assistacrise.domain.Salon} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SalonDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant dateCreation;

    @NotNull
    private DemandeDTO demande;

    private Set<UtilisateurDTO> participants = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public DemandeDTO getDemande() {
        return demande;
    }

    public void setDemande(DemandeDTO demande) {
        this.demande = demande;
    }

    public Set<UtilisateurDTO> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<UtilisateurDTO> participants) {
        this.participants = participants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalonDTO)) {
            return false;
        }

        SalonDTO salonDTO = (SalonDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, salonDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalonDTO{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            ", demande=" + getDemande() +
            ", participants=" + getParticipants() +
            "}";
    }
}
