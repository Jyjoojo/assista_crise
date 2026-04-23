package fr.assistacrise.service.dto;

import fr.assistacrise.domain.enumeration.ActionModeration;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.assistacrise.domain.Moderation} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ModerationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String motif;

    @NotNull
    private Instant dateModeration;

    @NotNull
    private ActionModeration action;

    @NotNull
    private UtilisateurDTO administrateur;

    private DemandeDTO demande;

    private OffreDTO offre;

    private UtilisateurDTO utilisateurCible;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Instant getDateModeration() {
        return dateModeration;
    }

    public void setDateModeration(Instant dateModeration) {
        this.dateModeration = dateModeration;
    }

    public ActionModeration getAction() {
        return action;
    }

    public void setAction(ActionModeration action) {
        this.action = action;
    }

    public UtilisateurDTO getAdministrateur() {
        return administrateur;
    }

    public void setAdministrateur(UtilisateurDTO administrateur) {
        this.administrateur = administrateur;
    }

    public DemandeDTO getDemande() {
        return demande;
    }

    public void setDemande(DemandeDTO demande) {
        this.demande = demande;
    }

    public OffreDTO getOffre() {
        return offre;
    }

    public void setOffre(OffreDTO offre) {
        this.offre = offre;
    }

    public UtilisateurDTO getUtilisateurCible() {
        return utilisateurCible;
    }

    public void setUtilisateurCible(UtilisateurDTO utilisateurCible) {
        this.utilisateurCible = utilisateurCible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ModerationDTO)) {
            return false;
        }

        ModerationDTO moderationDTO = (ModerationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, moderationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ModerationDTO{" +
            "id=" + getId() +
            ", motif='" + getMotif() + "'" +
            ", dateModeration='" + getDateModeration() + "'" +
            ", action='" + getAction() + "'" +
            ", administrateur=" + getAdministrateur() +
            ", demande=" + getDemande() +
            ", offre=" + getOffre() +
            ", utilisateurCible=" + getUtilisateurCible() +
            "}";
    }
}
