package fr.assistacrise.service.dto;

import fr.assistacrise.domain.enumeration.StatutOffre;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link fr.assistacrise.domain.Offre} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String titre;

    @NotNull
    @Size(max = 2000)
    private String description;

    @NotNull
    private StatutOffre statut;

    @NotNull
    private Instant dateCreation;

    private Instant dateMiseAJour;

    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    private Double latitude;

    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    private Double longitude;

    @NotNull
    private Boolean estArchivee;

    private Instant dateDeferencement;

    @NotNull
    private CriseDTO crise;

    @NotNull
    private UtilisateurDTO aidant;

    private Set<DemandeDTO> demandes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatutOffre getStatut() {
        return statut;
    }

    public void setStatut(StatutOffre statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(Instant dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getEstArchivee() {
        return estArchivee;
    }

    public void setEstArchivee(Boolean estArchivee) {
        this.estArchivee = estArchivee;
    }

    public Instant getDateDeferencement() {
        return dateDeferencement;
    }

    public void setDateDeferencement(Instant dateDeferencement) {
        this.dateDeferencement = dateDeferencement;
    }

    public CriseDTO getCrise() {
        return crise;
    }

    public void setCrise(CriseDTO crise) {
        this.crise = crise;
    }

    public UtilisateurDTO getAidant() {
        return aidant;
    }

    public void setAidant(UtilisateurDTO aidant) {
        this.aidant = aidant;
    }

    public Set<DemandeDTO> getDemandes() {
        return demandes;
    }

    public void setDemandes(Set<DemandeDTO> demandes) {
        this.demandes = demandes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OffreDTO)) {
            return false;
        }

        OffreDTO offreDTO = (OffreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, offreDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMiseAJour='" + getDateMiseAJour() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", estArchivee='" + getEstArchivee() + "'" +
            ", dateDeferencement='" + getDateDeferencement() + "'" +
            ", crise=" + getCrise() +
            ", aidant=" + getAidant() +
            ", demandes=" + getDemandes() +
            "}";
    }
}
