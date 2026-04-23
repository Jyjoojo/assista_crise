package fr.assistacrise.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import fr.assistacrise.domain.enumeration.RoleUtilisateur;
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
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "login", length = 50, nullable = false, unique = true)
    private String login;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    @Column(name = "mot_de_passe", length = 100, nullable = false)
    private String motDePasse;

    @NotNull
    @Size(max = 50)
    @Column(name = "prenom", length = 50, nullable = false)
    private String prenom;

    @NotNull
    @Size(max = 50)
    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Size(max = 20)
    @Column(name = "telephone", length = 20)
    private String telephone;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleUtilisateur role;

    @NotNull
    @Column(name = "actif", nullable = false)
    private Boolean actif;

    @NotNull
    @Column(name = "date_inscription", nullable = false)
    private Instant dateInscription;

    @Column(name = "date_bannissement")
    private Instant dateBannissement;

    @NotNull
    @Column(name = "est_banni", nullable = false)
    private Boolean estBanni;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "participants")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "demande", "participants" }, allowSetters = true)
    private Set<Salon> salons = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public Utilisateur login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return this.motDePasse;
    }

    public Utilisateur motDePasse(String motDePasse) {
        this.setMotDePasse(motDePasse);
        return this;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return this.telephone;
    }

    public Utilisateur telephone(String telephone) {
        this.setTelephone(telephone);
        return this;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public RoleUtilisateur getRole() {
        return this.role;
    }

    public Utilisateur role(RoleUtilisateur role) {
        this.setRole(role);
        return this;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Boolean getActif() {
        return this.actif;
    }

    public Utilisateur actif(Boolean actif) {
        this.setActif(actif);
        return this;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Instant getDateInscription() {
        return this.dateInscription;
    }

    public Utilisateur dateInscription(Instant dateInscription) {
        this.setDateInscription(dateInscription);
        return this;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Instant getDateBannissement() {
        return this.dateBannissement;
    }

    public Utilisateur dateBannissement(Instant dateBannissement) {
        this.setDateBannissement(dateBannissement);
        return this;
    }

    public void setDateBannissement(Instant dateBannissement) {
        this.dateBannissement = dateBannissement;
    }

    public Boolean getEstBanni() {
        return this.estBanni;
    }

    public Utilisateur estBanni(Boolean estBanni) {
        this.setEstBanni(estBanni);
        return this;
    }

    public void setEstBanni(Boolean estBanni) {
        this.estBanni = estBanni;
    }

    public Set<Salon> getSalons() {
        return this.salons;
    }

    public void setSalons(Set<Salon> salons) {
        if (this.salons != null) {
            this.salons.forEach(i -> i.removeParticipant(this));
        }
        if (salons != null) {
            salons.forEach(i -> i.addParticipant(this));
        }
        this.salons = salons;
    }

    public Utilisateur salons(Set<Salon> salons) {
        this.setSalons(salons);
        return this;
    }

    public Utilisateur addSalon(Salon salon) {
        this.salons.add(salon);
        salon.getParticipants().add(this);
        return this;
    }

    public Utilisateur removeSalon(Salon salon) {
        this.salons.remove(salon);
        salon.getParticipants().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return getId() != null && getId().equals(((Utilisateur) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", email='" + getEmail() + "'" +
            ", motDePasse='" + getMotDePasse() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", nom='" + getNom() + "'" +
            ", telephone='" + getTelephone() + "'" +
            ", role='" + getRole() + "'" +
            ", actif='" + getActif() + "'" +
            ", dateInscription='" + getDateInscription() + "'" +
            ", dateBannissement='" + getDateBannissement() + "'" +
            ", estBanni='" + getEstBanni() + "'" +
            "}";
    }
}
