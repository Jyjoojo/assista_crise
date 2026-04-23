package fr.assistacrise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.assistacrise.domain.enumeration.StatutCrise;
import fr.assistacrise.domain.enumeration.TypeCrise;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Crise.
 */
@Entity
@Table(name = "crise")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Crise implements Serializable {

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

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeCrise type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutCrise statut;

    @NotNull
    @Column(name = "date_debut", nullable = false)
    private Instant dateDebut;

    @Column(name = "date_fermeture")
    private Instant dateFermeture;

    @NotNull
    @DecimalMin(value = "-90")
    @DecimalMax(value = "90")
    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @NotNull
    @DecimalMin(value = "-180")
    @DecimalMax(value = "180")
    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @DecimalMin(value = "0")
    @Column(name = "rayon_km")
    private Double rayonKm;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "salons" }, allowSetters = true)
    private Utilisateur declarant;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Crise id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Crise titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return this.description;
    }

    public Crise description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeCrise getType() {
        return this.type;
    }

    public Crise type(TypeCrise type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeCrise type) {
        this.type = type;
    }

    public StatutCrise getStatut() {
        return this.statut;
    }

    public Crise statut(StatutCrise statut) {
        this.setStatut(statut);
        return this;
    }

    public void setStatut(StatutCrise statut) {
        this.statut = statut;
    }

    public Instant getDateDebut() {
        return this.dateDebut;
    }

    public Crise dateDebut(Instant dateDebut) {
        this.setDateDebut(dateDebut);
        return this;
    }

    public void setDateDebut(Instant dateDebut) {
        this.dateDebut = dateDebut;
    }

    public Instant getDateFermeture() {
        return this.dateFermeture;
    }

    public Crise dateFermeture(Instant dateFermeture) {
        this.setDateFermeture(dateFermeture);
        return this;
    }

    public void setDateFermeture(Instant dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public Crise latitude(Double latitude) {
        this.setLatitude(latitude);
        return this;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public Crise longitude(Double longitude) {
        this.setLongitude(longitude);
        return this;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getRayonKm() {
        return this.rayonKm;
    }

    public Crise rayonKm(Double rayonKm) {
        this.setRayonKm(rayonKm);
        return this;
    }

    public void setRayonKm(Double rayonKm) {
        this.rayonKm = rayonKm;
    }

    public Utilisateur getDeclarant() {
        return this.declarant;
    }

    public void setDeclarant(Utilisateur utilisateur) {
        this.declarant = utilisateur;
    }

    public Crise declarant(Utilisateur utilisateur) {
        this.setDeclarant(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Crise)) {
            return false;
        }
        return getId() != null && getId().equals(((Crise) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Crise{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", description='" + getDescription() + "'" +
            ", type='" + getType() + "'" +
            ", statut='" + getStatut() + "'" +
            ", dateDebut='" + getDateDebut() + "'" +
            ", dateFermeture='" + getDateFermeture() + "'" +
            ", latitude=" + getLatitude() +
            ", longitude=" + getLongitude() +
            ", rayonKm=" + getRayonKm() +
            "}";
    }
}
