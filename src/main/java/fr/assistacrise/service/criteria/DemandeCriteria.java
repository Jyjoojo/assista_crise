package fr.assistacrise.service.criteria;

import fr.assistacrise.domain.enumeration.StatutDemande;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.assistacrise.domain.Demande} entity. This class is used
 * in {@link fr.assistacrise.web.rest.DemandeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /demandes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DemandeCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutDemande
     */
    public static class StatutDemandeFilter extends Filter<StatutDemande> {

        public StatutDemandeFilter() {}

        public StatutDemandeFilter(StatutDemandeFilter filter) {
            super(filter);
        }

        @Override
        public StatutDemandeFilter copy() {
            return new StatutDemandeFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titre;

    private StringFilter description;

    private StatutDemandeFilter statut;

    private InstantFilter dateCreation;

    private InstantFilter dateMiseAJour;

    private InstantFilter dateFermeture;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private BooleanFilter estArchivee;

    private InstantFilter dateDeferencement;

    private LongFilter sinistreId;

    private LongFilter criseId;

    private LongFilter offreId;

    private LongFilter salonId;

    private Boolean distinct;

    public DemandeCriteria() {}

    public DemandeCriteria(DemandeCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutDemandeFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.dateMiseAJour = other.optionalDateMiseAJour().map(InstantFilter::copy).orElse(null);
        this.dateFermeture = other.optionalDateFermeture().map(InstantFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(DoubleFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(DoubleFilter::copy).orElse(null);
        this.estArchivee = other.optionalEstArchivee().map(BooleanFilter::copy).orElse(null);
        this.dateDeferencement = other.optionalDateDeferencement().map(InstantFilter::copy).orElse(null);
        this.sinistreId = other.optionalSinistreId().map(LongFilter::copy).orElse(null);
        this.criseId = other.optionalCriseId().map(LongFilter::copy).orElse(null);
        this.offreId = other.optionalOffreId().map(LongFilter::copy).orElse(null);
        this.salonId = other.optionalSalonId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public DemandeCriteria copy() {
        return new DemandeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getTitre() {
        return titre;
    }

    public Optional<StringFilter> optionalTitre() {
        return Optional.ofNullable(titre);
    }

    public StringFilter titre() {
        if (titre == null) {
            setTitre(new StringFilter());
        }
        return titre;
    }

    public void setTitre(StringFilter titre) {
        this.titre = titre;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public StatutDemandeFilter getStatut() {
        return statut;
    }

    public Optional<StatutDemandeFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutDemandeFilter statut() {
        if (statut == null) {
            setStatut(new StatutDemandeFilter());
        }
        return statut;
    }

    public void setStatut(StatutDemandeFilter statut) {
        this.statut = statut;
    }

    public InstantFilter getDateCreation() {
        return dateCreation;
    }

    public Optional<InstantFilter> optionalDateCreation() {
        return Optional.ofNullable(dateCreation);
    }

    public InstantFilter dateCreation() {
        if (dateCreation == null) {
            setDateCreation(new InstantFilter());
        }
        return dateCreation;
    }

    public void setDateCreation(InstantFilter dateCreation) {
        this.dateCreation = dateCreation;
    }

    public InstantFilter getDateMiseAJour() {
        return dateMiseAJour;
    }

    public Optional<InstantFilter> optionalDateMiseAJour() {
        return Optional.ofNullable(dateMiseAJour);
    }

    public InstantFilter dateMiseAJour() {
        if (dateMiseAJour == null) {
            setDateMiseAJour(new InstantFilter());
        }
        return dateMiseAJour;
    }

    public void setDateMiseAJour(InstantFilter dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public InstantFilter getDateFermeture() {
        return dateFermeture;
    }

    public Optional<InstantFilter> optionalDateFermeture() {
        return Optional.ofNullable(dateFermeture);
    }

    public InstantFilter dateFermeture() {
        if (dateFermeture == null) {
            setDateFermeture(new InstantFilter());
        }
        return dateFermeture;
    }

    public void setDateFermeture(InstantFilter dateFermeture) {
        this.dateFermeture = dateFermeture;
    }

    public DoubleFilter getLatitude() {
        return latitude;
    }

    public Optional<DoubleFilter> optionalLatitude() {
        return Optional.ofNullable(latitude);
    }

    public DoubleFilter latitude() {
        if (latitude == null) {
            setLatitude(new DoubleFilter());
        }
        return latitude;
    }

    public void setLatitude(DoubleFilter latitude) {
        this.latitude = latitude;
    }

    public DoubleFilter getLongitude() {
        return longitude;
    }

    public Optional<DoubleFilter> optionalLongitude() {
        return Optional.ofNullable(longitude);
    }

    public DoubleFilter longitude() {
        if (longitude == null) {
            setLongitude(new DoubleFilter());
        }
        return longitude;
    }

    public void setLongitude(DoubleFilter longitude) {
        this.longitude = longitude;
    }

    public BooleanFilter getEstArchivee() {
        return estArchivee;
    }

    public Optional<BooleanFilter> optionalEstArchivee() {
        return Optional.ofNullable(estArchivee);
    }

    public BooleanFilter estArchivee() {
        if (estArchivee == null) {
            setEstArchivee(new BooleanFilter());
        }
        return estArchivee;
    }

    public void setEstArchivee(BooleanFilter estArchivee) {
        this.estArchivee = estArchivee;
    }

    public InstantFilter getDateDeferencement() {
        return dateDeferencement;
    }

    public Optional<InstantFilter> optionalDateDeferencement() {
        return Optional.ofNullable(dateDeferencement);
    }

    public InstantFilter dateDeferencement() {
        if (dateDeferencement == null) {
            setDateDeferencement(new InstantFilter());
        }
        return dateDeferencement;
    }

    public void setDateDeferencement(InstantFilter dateDeferencement) {
        this.dateDeferencement = dateDeferencement;
    }

    public LongFilter getSinistreId() {
        return sinistreId;
    }

    public Optional<LongFilter> optionalSinistreId() {
        return Optional.ofNullable(sinistreId);
    }

    public LongFilter sinistreId() {
        if (sinistreId == null) {
            setSinistreId(new LongFilter());
        }
        return sinistreId;
    }

    public void setSinistreId(LongFilter sinistreId) {
        this.sinistreId = sinistreId;
    }

    public LongFilter getCriseId() {
        return criseId;
    }

    public Optional<LongFilter> optionalCriseId() {
        return Optional.ofNullable(criseId);
    }

    public LongFilter criseId() {
        if (criseId == null) {
            setCriseId(new LongFilter());
        }
        return criseId;
    }

    public void setCriseId(LongFilter criseId) {
        this.criseId = criseId;
    }

    public LongFilter getOffreId() {
        return offreId;
    }

    public Optional<LongFilter> optionalOffreId() {
        return Optional.ofNullable(offreId);
    }

    public LongFilter offreId() {
        if (offreId == null) {
            setOffreId(new LongFilter());
        }
        return offreId;
    }

    public void setOffreId(LongFilter offreId) {
        this.offreId = offreId;
    }

    public LongFilter getSalonId() {
        return salonId;
    }

    public Optional<LongFilter> optionalSalonId() {
        return Optional.ofNullable(salonId);
    }

    public LongFilter salonId() {
        if (salonId == null) {
            setSalonId(new LongFilter());
        }
        return salonId;
    }

    public void setSalonId(LongFilter salonId) {
        this.salonId = salonId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DemandeCriteria that = (DemandeCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(description, that.description) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateMiseAJour, that.dateMiseAJour) &&
            Objects.equals(dateFermeture, that.dateFermeture) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(estArchivee, that.estArchivee) &&
            Objects.equals(dateDeferencement, that.dateDeferencement) &&
            Objects.equals(sinistreId, that.sinistreId) &&
            Objects.equals(criseId, that.criseId) &&
            Objects.equals(offreId, that.offreId) &&
            Objects.equals(salonId, that.salonId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            titre,
            description,
            statut,
            dateCreation,
            dateMiseAJour,
            dateFermeture,
            latitude,
            longitude,
            estArchivee,
            dateDeferencement,
            sinistreId,
            criseId,
            offreId,
            salonId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DemandeCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDateMiseAJour().map(f -> "dateMiseAJour=" + f + ", ").orElse("") +
            optionalDateFermeture().map(f -> "dateFermeture=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalEstArchivee().map(f -> "estArchivee=" + f + ", ").orElse("") +
            optionalDateDeferencement().map(f -> "dateDeferencement=" + f + ", ").orElse("") +
            optionalSinistreId().map(f -> "sinistreId=" + f + ", ").orElse("") +
            optionalCriseId().map(f -> "criseId=" + f + ", ").orElse("") +
            optionalOffreId().map(f -> "offreId=" + f + ", ").orElse("") +
            optionalSalonId().map(f -> "salonId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
