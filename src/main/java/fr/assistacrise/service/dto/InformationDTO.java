package fr.assistacrise.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link fr.assistacrise.domain.Information} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String titre;

    @NotNull
    @Size(max = 5000)
    private String contenu;

    @NotNull
    private Instant datePublication;

    private Instant dateMiseAJour;

    @NotNull
    private Boolean estVisible;

    @NotNull
    private CriseDTO crise;

    @NotNull
    private UtilisateurDTO auteur;

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

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(Instant datePublication) {
        this.datePublication = datePublication;
    }

    public Instant getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(Instant dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Boolean getEstVisible() {
        return estVisible;
    }

    public void setEstVisible(Boolean estVisible) {
        this.estVisible = estVisible;
    }

    public CriseDTO getCrise() {
        return crise;
    }

    public void setCrise(CriseDTO crise) {
        this.crise = crise;
    }

    public UtilisateurDTO getAuteur() {
        return auteur;
    }

    public void setAuteur(UtilisateurDTO auteur) {
        this.auteur = auteur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InformationDTO)) {
            return false;
        }

        InformationDTO informationDTO = (InformationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, informationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationDTO{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            ", dateMiseAJour='" + getDateMiseAJour() + "'" +
            ", estVisible='" + getEstVisible() + "'" +
            ", crise=" + getCrise() +
            ", auteur=" + getAuteur() +
            "}";
    }
}
