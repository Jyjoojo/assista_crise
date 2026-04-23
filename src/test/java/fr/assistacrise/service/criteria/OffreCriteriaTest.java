package fr.assistacrise.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OffreCriteriaTest {

    @Test
    void newOffreCriteriaHasAllFiltersNullTest() {
        var offreCriteria = new OffreCriteria();
        assertThat(offreCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void offreCriteriaFluentMethodsCreatesFiltersTest() {
        var offreCriteria = new OffreCriteria();

        setAllFilters(offreCriteria);

        assertThat(offreCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void offreCriteriaCopyCreatesNullFilterTest() {
        var offreCriteria = new OffreCriteria();
        var copy = offreCriteria.copy();

        assertThat(offreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCriteria)
        );
    }

    @Test
    void offreCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var offreCriteria = new OffreCriteria();
        setAllFilters(offreCriteria);

        var copy = offreCriteria.copy();

        assertThat(offreCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(offreCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var offreCriteria = new OffreCriteria();

        assertThat(offreCriteria).hasToString("OffreCriteria{}");
    }

    private static void setAllFilters(OffreCriteria offreCriteria) {
        offreCriteria.id();
        offreCriteria.titre();
        offreCriteria.description();
        offreCriteria.statut();
        offreCriteria.dateCreation();
        offreCriteria.dateMiseAJour();
        offreCriteria.latitude();
        offreCriteria.longitude();
        offreCriteria.estArchivee();
        offreCriteria.dateDeferencement();
        offreCriteria.criseId();
        offreCriteria.aidantId();
        offreCriteria.demandeId();
        offreCriteria.distinct();
    }

    private static Condition<OffreCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTitre()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStatut()) &&
                condition.apply(criteria.getDateCreation()) &&
                condition.apply(criteria.getDateMiseAJour()) &&
                condition.apply(criteria.getLatitude()) &&
                condition.apply(criteria.getLongitude()) &&
                condition.apply(criteria.getEstArchivee()) &&
                condition.apply(criteria.getDateDeferencement()) &&
                condition.apply(criteria.getCriseId()) &&
                condition.apply(criteria.getAidantId()) &&
                condition.apply(criteria.getDemandeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OffreCriteria> copyFiltersAre(OffreCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTitre(), copy.getTitre()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStatut(), copy.getStatut()) &&
                condition.apply(criteria.getDateCreation(), copy.getDateCreation()) &&
                condition.apply(criteria.getDateMiseAJour(), copy.getDateMiseAJour()) &&
                condition.apply(criteria.getLatitude(), copy.getLatitude()) &&
                condition.apply(criteria.getLongitude(), copy.getLongitude()) &&
                condition.apply(criteria.getEstArchivee(), copy.getEstArchivee()) &&
                condition.apply(criteria.getDateDeferencement(), copy.getDateDeferencement()) &&
                condition.apply(criteria.getCriseId(), copy.getCriseId()) &&
                condition.apply(criteria.getAidantId(), copy.getAidantId()) &&
                condition.apply(criteria.getDemandeId(), copy.getDemandeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
