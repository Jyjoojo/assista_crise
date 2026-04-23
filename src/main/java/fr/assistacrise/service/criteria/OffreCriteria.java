package fr.assistacrise.service.criteria;

import fr.assistacrise.domain.enumeration.StatutOffre;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.assistacrise.domain.Offre} entity. This class is used
 * in {@link fr.assistacrise.web.rest.OffreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /offres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OffreCriteria implements Serializable, Criteria {

    /**
     * Class for filtering StatutOffre
     */
    public static class StatutOffreFilter extends Filter<StatutOffre> {

        public StatutOffreFilter() {}

        public StatutOffreFilter(StatutOffreFilter filter) {
            super(filter);
        }

        @Override
        public StatutOffreFilter copy() {
            return new StatutOffreFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titre;

    private StringFilter description;

    private StatutOffreFilter statut;

    private InstantFilter dateCreation;

    private InstantFilter dateMiseAJour;

    private DoubleFilter latitude;

    private DoubleFilter longitude;

    private BooleanFilter estArchivee;

    private InstantFilter dateDeferencement;

    private LongFilter criseId;

    private LongFilter aidantId;

    private LongFilter demandeId;

    private Boolean distinct;

    public OffreCriteria() {}

    public OffreCriteria(OffreCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.statut = other.optionalStatut().map(StatutOffreFilter::copy).orElse(null);
        this.dateCreation = other.optionalDateCreation().map(InstantFilter::copy).orElse(null);
        this.dateMiseAJour = other.optionalDateMiseAJour().map(InstantFilter::copy).orElse(null);
        this.latitude = other.optionalLatitude().map(DoubleFilter::copy).orElse(null);
        this.longitude = other.optionalLongitude().map(DoubleFilter::copy).orElse(null);
        this.estArchivee = other.optionalEstArchivee().map(BooleanFilter::copy).orElse(null);
        this.dateDeferencement = other.optionalDateDeferencement().map(InstantFilter::copy).orElse(null);
        this.criseId = other.optionalCriseId().map(LongFilter::copy).orElse(null);
        this.aidantId = other.optionalAidantId().map(LongFilter::copy).orElse(null);
        this.demandeId = other.optionalDemandeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OffreCriteria copy() {
        return new OffreCriteria(this);
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

    public StatutOffreFilter getStatut() {
        return statut;
    }

    public Optional<StatutOffreFilter> optionalStatut() {
        return Optional.ofNullable(statut);
    }

    public StatutOffreFilter statut() {
        if (statut == null) {
            setStatut(new StatutOffreFilter());
        }
        return statut;
    }

    public void setStatut(StatutOffreFilter statut) {
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

    public LongFilter getAidantId() {
        return aidantId;
    }

    public Optional<LongFilter> optionalAidantId() {
        return Optional.ofNullable(aidantId);
    }

    public LongFilter aidantId() {
        if (aidantId == null) {
            setAidantId(new LongFilter());
        }
        return aidantId;
    }

    public void setAidantId(LongFilter aidantId) {
        this.aidantId = aidantId;
    }

    public LongFilter getDemandeId() {
        return demandeId;
    }

    public Optional<LongFilter> optionalDemandeId() {
        return Optional.ofNullable(demandeId);
    }

    public LongFilter demandeId() {
        if (demandeId == null) {
            setDemandeId(new LongFilter());
        }
        return demandeId;
    }

    public void setDemandeId(LongFilter demandeId) {
        this.demandeId = demandeId;
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
        final OffreCriteria that = (OffreCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(description, that.description) &&
            Objects.equals(statut, that.statut) &&
            Objects.equals(dateCreation, that.dateCreation) &&
            Objects.equals(dateMiseAJour, that.dateMiseAJour) &&
            Objects.equals(latitude, that.latitude) &&
            Objects.equals(longitude, that.longitude) &&
            Objects.equals(estArchivee, that.estArchivee) &&
            Objects.equals(dateDeferencement, that.dateDeferencement) &&
            Objects.equals(criseId, that.criseId) &&
            Objects.equals(aidantId, that.aidantId) &&
            Objects.equals(demandeId, that.demandeId) &&
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
            latitude,
            longitude,
            estArchivee,
            dateDeferencement,
            criseId,
            aidantId,
            demandeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OffreCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStatut().map(f -> "statut=" + f + ", ").orElse("") +
            optionalDateCreation().map(f -> "dateCreation=" + f + ", ").orElse("") +
            optionalDateMiseAJour().map(f -> "dateMiseAJour=" + f + ", ").orElse("") +
            optionalLatitude().map(f -> "latitude=" + f + ", ").orElse("") +
            optionalLongitude().map(f -> "longitude=" + f + ", ").orElse("") +
            optionalEstArchivee().map(f -> "estArchivee=" + f + ", ").orElse("") +
            optionalDateDeferencement().map(f -> "dateDeferencement=" + f + ", ").orElse("") +
            optionalCriseId().map(f -> "criseId=" + f + ", ").orElse("") +
            optionalAidantId().map(f -> "aidantId=" + f + ", ").orElse("") +
            optionalDemandeId().map(f -> "demandeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
