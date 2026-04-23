package fr.assistacrise.service.dto;

import fr.assistacrise.domain.enumeration.RoleUtilisateur;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link fr.assistacrise.domain.Utilisateur} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UtilisateurDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 3, max = 50)
    private String login;

    @NotNull
    @Pattern(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")
    private String email;

    @NotNull
    @Size(min = 8, max = 100)
    private String motDePasse;

    @NotNull
    @Size(max = 50)
    private String prenom;

    @NotNull
    @Size(max = 50)
    private String nom;

    @Size(max = 20)
    private String telephone;

    @NotNull
    private RoleUtilisateur role;

    @NotNull
    private Boolean actif;

    @NotNull
    private Instant dateInscription;

    private Instant dateBannissement;

    @NotNull
    private Boolean estBanni;

    private Set<SalonDTO> salons = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public RoleUtilisateur getRole() {
        return role;
    }

    public void setRole(RoleUtilisateur role) {
        this.role = role;
    }

    public Boolean getActif() {
        return actif;
    }

    public void setActif(Boolean actif) {
        this.actif = actif;
    }

    public Instant getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Instant dateInscription) {
        this.dateInscription = dateInscription;
    }

    public Instant getDateBannissement() {
        return dateBannissement;
    }

    public void setDateBannissement(Instant dateBannissement) {
        this.dateBannissement = dateBannissement;
    }

    public Boolean getEstBanni() {
        return estBanni;
    }

    public void setEstBanni(Boolean estBanni) {
        this.estBanni = estBanni;
    }

    public Set<SalonDTO> getSalons() {
        return salons;
    }

    public void setSalons(Set<SalonDTO> salons) {
        this.salons = salons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtilisateurDTO)) {
            return false;
        }

        UtilisateurDTO utilisateurDTO = (UtilisateurDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utilisateurDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtilisateurDTO{" +
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
            ", salons=" + getSalons() +
            "}";
    }
}
