package fr.assistacrise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
 * A Salon.
 */
@Entity
@Table(name = "salon")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Salon implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @JsonIgnoreProperties(value = { "sinistre", "crise", "offres", "salon" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @NotNull
    @JoinColumn(unique = true)
    private Demande demande;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_salon__participant",
        joinColumns = @JoinColumn(name = "salon_id"),
        inverseJoinColumns = @JoinColumn(name = "participant_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "salons" }, allowSetters = true)
    private Set<Utilisateur> participants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Salon id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDateCreation() {
        return this.dateCreation;
    }

    public Salon dateCreation(Instant dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(Instant dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Demande getDemande() {
        return this.demande;
    }

    public void setDemande(Demande demande) {
        this.demande = demande;
    }

    public Salon demande(Demande demande) {
        this.setDemande(demande);
        return this;
    }

    public Set<Utilisateur> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<Utilisateur> utilisateurs) {
        this.participants = utilisateurs;
    }

    public Salon participants(Set<Utilisateur> utilisateurs) {
        this.setParticipants(utilisateurs);
        return this;
    }

    public Salon addParticipant(Utilisateur utilisateur) {
        this.participants.add(utilisateur);
        return this;
    }

    public Salon removeParticipant(Utilisateur utilisateur) {
        this.participants.remove(utilisateur);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Salon)) {
            return false;
        }
        return getId() != null && getId().equals(((Salon) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Salon{" +
            "id=" + getId() +
            ", dateCreation='" + getDateCreation() + "'" +
            "}";
    }
}
