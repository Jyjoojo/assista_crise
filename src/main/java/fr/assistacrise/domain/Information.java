package fr.assistacrise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Information.
 */
@Entity
@Table(name = "information")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Information implements Serializable {

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
    @Size(max = 5000)
    @Column(name = "contenu", length = 5000, nullable = false)
    private String contenu;

    @NotNull
    @Column(name = "date_publication", nullable = false)
    private Instant datePublication;

    @Column(name = "date_mise_a_jour")
    private Instant dateMiseAJour;

    @NotNull
    @Column(name = "est_visible", nullable = false)
    private Boolean estVisible;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "declarant" }, allowSetters = true)
    private Crise crise;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "salons" }, allowSetters = true)
    private Utilisateur auteur;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Information id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return this.titre;
    }

    public Information titre(String titre) {
        this.setTitre(titre);
        return this;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return this.contenu;
    }

    public Information contenu(String contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public Instant getDatePublication() {
        return this.datePublication;
    }

    public Information datePublication(Instant datePublication) {
        this.setDatePublication(datePublication);
        return this;
    }

    public void setDatePublication(Instant datePublication) {
        this.datePublication = datePublication;
    }

    public Instant getDateMiseAJour() {
        return this.dateMiseAJour;
    }

    public Information dateMiseAJour(Instant dateMiseAJour) {
        this.setDateMiseAJour(dateMiseAJour);
        return this;
    }

    public void setDateMiseAJour(Instant dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public Boolean getEstVisible() {
        return this.estVisible;
    }

    public Information estVisible(Boolean estVisible) {
        this.setEstVisible(estVisible);
        return this;
    }

    public void setEstVisible(Boolean estVisible) {
        this.estVisible = estVisible;
    }

    public Crise getCrise() {
        return this.crise;
    }

    public void setCrise(Crise crise) {
        this.crise = crise;
    }

    public Information crise(Crise crise) {
        this.setCrise(crise);
        return this;
    }

    public Utilisateur getAuteur() {
        return this.auteur;
    }

    public void setAuteur(Utilisateur utilisateur) {
        this.auteur = utilisateur;
    }

    public Information auteur(Utilisateur utilisateur) {
        this.setAuteur(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Information)) {
            return false;
        }
        return getId() != null && getId().equals(((Information) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Information{" +
            "id=" + getId() +
            ", titre='" + getTitre() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", datePublication='" + getDatePublication() + "'" +
            ", dateMiseAJour='" + getDateMiseAJour() + "'" +
            ", estVisible='" + getEstVisible() + "'" +
            "}";
    }
}
