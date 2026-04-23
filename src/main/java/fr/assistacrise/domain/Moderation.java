package fr.assistacrise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.assistacrise.domain.enumeration.ActionModeration;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Moderation.
 */
@Entity
@Table(name = "moderation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Moderation implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "motif", length = 500, nullable = false)
    private String motif;

    @NotNull
    @Column(name = "date_moderation", nullable = false)
    private Instant dateModeration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ActionModeration action;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "salons" }, allowSetters = true)
    private Utilisateur administrateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "sinistre", "crise", "offres", "salon" }, allowSetters = true)
    private Demande demande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "crise", "aidant", "demandes" }, allowSetters = true)
    private Offre offre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "salons" }, allowSetters = true)
    private Utilisateur utilisateurCible;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Moderation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMotif() {
        return this.motif;
    }

    public Moderation motif(String motif) {
        this.setMotif(motif);
        return this;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public Instant getDateModeration() {
        return this.dateModeration;
    }

    public Moderation dateModeration(Instant dateModeration) {
        this.setDateModeration(dateModeration);
        return this;
    }

    public void setDateModeration(Instant dateModeration) {
        this.dateModeration = dateModeration;
    }

    public ActionModeration getAction() {
        return this.action;
    }

    public Moderation action(ActionModeration action) {
        this.setAction(action);
        return this;
    }

    public void setAction(ActionModeration action) {
        this.action = action;
    }

    public Utilisateur getAdministrateur() {
        return this.administrateur;
    }

    public void setAdministrateur(Utilisateur utilisateur) {
        this.administrateur = utilisateur;
    }

    public Moderation administrateur(Utilisateur utilisateur) {
        this.setAdministrateur(utilisateur);
        return this;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Moderation demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    public Offre getOffre() {
        return this.offre;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }

    public Moderation offre(Offre offre) {
        this.setOffre(offre);
        return this;
    }

    public Utilisateur getUtilisateurCible() {
        return this.utilisateurCible;
    }

    public void setUtilisateurCible(Utilisateur utilisateur) {
        this.utilisateurCible = utilisateur;
    }

    public Moderation utilisateurCible(Utilisateur utilisateur) {
        this.setUtilisateurCible(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Moderation)) {
            return false;
        }
        return getId() != null && getId().equals(((Moderation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Moderation{" +
            "id=" + getId() +
            ", motif='" + getMotif() + "'" +
            ", dateModeration='" + getDateModeration() + "'" +
            ", action='" + getAction() + "'" +
            "}";
    }
}
