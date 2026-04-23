package fr.assistacrise.service.criteria;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link fr.assistacrise.domain.Information} entity. This class is used
 * in {@link fr.assistacrise.web.rest.InformationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /information?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InformationCriteria implements Serializable, Criteria {

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter titre;

    private StringFilter contenu;

    private InstantFilter datePublication;

    private InstantFilter dateMiseAJour;

    private BooleanFilter estVisible;

    private LongFilter criseId;

    private LongFilter auteurId;

    private Boolean distinct;

    public InformationCriteria() {}

    public InformationCriteria(InformationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.titre = other.optionalTitre().map(StringFilter::copy).orElse(null);
        this.contenu = other.optionalContenu().map(StringFilter::copy).orElse(null);
        this.datePublication = other.optionalDatePublication().map(InstantFilter::copy).orElse(null);
        this.dateMiseAJour = other.optionalDateMiseAJour().map(InstantFilter::copy).orElse(null);
        this.estVisible = other.optionalEstVisible().map(BooleanFilter::copy).orElse(null);
        this.criseId = other.optionalCriseId().map(LongFilter::copy).orElse(null);
        this.auteurId = other.optionalAuteurId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public InformationCriteria copy() {
        return new InformationCriteria(this);
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

    public StringFilter getContenu() {
        return contenu;
    }

    public Optional<StringFilter> optionalContenu() {
        return Optional.ofNullable(contenu);
    }

    public StringFilter contenu() {
        if (contenu == null) {
            setContenu(new StringFilter());
        }
        return contenu;
    }

    public void setContenu(StringFilter contenu) {
        this.contenu = contenu;
    }

    public InstantFilter getDatePublication() {
        return datePublication;
    }

    public Optional<InstantFilter> optionalDatePublication() {
        return Optional.ofNullable(datePublication);
    }

    public InstantFilter datePublication() {
        if (datePublication == null) {
            setDatePublication(new InstantFilter());
        }
        return datePublication;
    }

    public void setDatePublication(InstantFilter datePublication) {
        this.datePublication = datePublication;
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

    public BooleanFilter getEstVisible() {
        return estVisible;
    }

    public Optional<BooleanFilter> optionalEstVisible() {
        return Optional.ofNullable(estVisible);
    }

    public BooleanFilter estVisible() {
        if (estVisible == null) {
            setEstVisible(new BooleanFilter());
        }
        return estVisible;
    }

    public void setEstVisible(BooleanFilter estVisible) {
        this.estVisible = estVisible;
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

    public LongFilter getAuteurId() {
        return auteurId;
    }

    public Optional<LongFilter> optionalAuteurId() {
        return Optional.ofNullable(auteurId);
    }

    public LongFilter auteurId() {
        if (auteurId == null) {
            setAuteurId(new LongFilter());
        }
        return auteurId;
    }

    public void setAuteurId(LongFilter auteurId) {
        this.auteurId = auteurId;
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
        final InformationCriteria that = (InformationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(titre, that.titre) &&
            Objects.equals(contenu, that.contenu) &&
            Objects.equals(datePublication, that.datePublication) &&
            Objects.equals(dateMiseAJour, that.dateMiseAJour) &&
            Objects.equals(estVisible, that.estVisible) &&
            Objects.equals(criseId, that.criseId) &&
            Objects.equals(auteurId, that.auteurId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titre, contenu, datePublication, dateMiseAJour, estVisible, criseId, auteurId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InformationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalTitre().map(f -> "titre=" + f + ", ").orElse("") +
            optionalContenu().map(f -> "contenu=" + f + ", ").orElse("") +
            optionalDatePublication().map(f -> "datePublication=" + f + ", ").orElse("") +
            optionalDateMiseAJour().map(f -> "dateMiseAJour=" + f + ", ").orElse("") +
            optionalEstVisible().map(f -> "estVisible=" + f + ", ").orElse("") +
            optionalCriseId().map(f -> "criseId=" + f + ", ").orElse("") +
            optionalAuteurId().map(f -> "auteurId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
