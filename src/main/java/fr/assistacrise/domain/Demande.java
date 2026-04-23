package fr.assistacrise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.assistacrise.domain.enumeration.StatutDemande;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Demande.
 */
@Entity
@Table(name = "demande")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Demande implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "titre", length = 200, nullable = false)
    private String titre;

    @NotNull
    @Size(max = 2000)
    @Column(name = "description", length = 2000, nullable = false)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutDemande statut;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_mise_a_jour")
    private Instant dateMiseAJour;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @Column(name = "latitude")
    private Double latitude;

    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @Column(name = "longitude")
    private Double longitude;

    @NotNull
    @Column(name = "est_archivee", nullable = false)
    private Boolean estArchivee;

    @Column(name = "date_deferencement")
    private Instant dateDeferencement;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "salons" }, allowSetters = true)
    private Utilisateur sinistre;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "declarant" }, allowSetters = true)
    private Crise crise;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_demande__offre",
        joinColumns = @JoinColumn(name = "demande_id"),
        inverseJoinColumns = @JoinColumn(name = "offre_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "crise", "aidant", "demandes" }, allowSetters = true)
    private Set<Offre> offres = new HashSet<>();

    @JsonIgnoreProperties(value = { "demande", "participants" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "demande")
    private Salon salon;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Demande id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Demande titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Demande description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatutDemande getStatut() {
        return this.statut;
    }

    public Demande statut(StatutDemande statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Demande dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Instant getDateMiseAJour() {
        return this.dateMiseAJour;
    }

    public Demande dateMiseAJour(Instant dateMiseAJour) {
        this.setDateMiseAJour(dateMiseAJour);
        return this;
    }

    public void setDateMiseAJour(Instant dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Instant getDateFermeture() {
        return this.dateFermeture;
    }

    public Demande dateFermeture(Instant dateFermeture) {
        this.setDateFermeture(dateFermeture);
        return this;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Demande latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Demande longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getEstArchivee() {
        return this.estArchivee;
    }

    public Demande estArchivee(Boolean estArchivee) {
        this.setEstArchivee(estArchivee);
        return this;
    }

    public void setEstArchivee(Boolean estArchivee) {
        this.estArchivee = estArchivee;
    }

    public Instant getDateDeferencement() {
        return this.dateDeferencement;
    }

    public Demande dateDeferencement(Instant dateDeferencement) {
        this.setDateDeferencement(dateDeferencement);
        return this;
    }

    public void setDateDeferencement(Instant dateDeferencement) {
        this.dateDeferencement = dateDeferencement;
    }

    public Utilisateur getSinistre() {
        return this.sinistre;
    }

    public void setSinistre(Utilisateur utilisateur) {
        this.sinistre = utilisateur;
    }

    public Demande sinistre(Utilisateur utilisateur) {
        this.setSinistre(utilisateur);
        return this;
    }

    public Crise getCrise() {
        return this.crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Demande crise(Crise crise) {
        this.setCrise(crise);
        return this;
    }

    public Set<Offre> getOffres() {
        return this.offres;
    }

    public void setOffres(Set<Offre> offres) {
        this.offres = offres;
    }

    public Demande offres(Set<Offre> offres) {
        this.setOffres(offres);
        return this;
    }

    public Demande addOffre(Offre offre) {
        this.offres.add(offre);
        return this;
    }

    public Demande removeOffre(Offre offre) {
        this.offres.remove(offre);
        return this;
    }

    public Salon getSalon() {
        return this.salon;
    }

    public void setSalon(Salon salon) {
        if (this.salon != null) {
            this.salon.setDemande(null);
        }
        if (salon != null) {
            salon.setDemande(this);
        }
        this.salon = salon;
    }

    public Demande salon(Salon salon) {
        this.setSalon(salon);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Demande)) {
            return false;
        }
        return getId() != null && getId().equals(((Demande) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Demande{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", dateMiseAJour='" + getDateMiseAJour() + "'" +
            ", dateFermeture='" + getDateFermeture() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", estArchivee='" + getEstArchivee() + "'" +
            ", dateDeferencement='" + getDateDeferencement() + "'" +
            "}";
    }
}
