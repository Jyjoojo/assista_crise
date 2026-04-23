package fr.assistacrise.domain;

import static fr.assistacrise.domain.CriseTestSamples.*;
import static fr.assistacrise.domain.DemandeTestSamples.*;
import static fr.assistacrise.domain.OffreTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class OffreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Offre.class);
        Offre offre1 = getOffreSample1();
        Offre offre2 = new Offre();
        assertThat(offre1).isNotEqualTo(offre2);

        offre2.setId(offre1.getId());
        assertThat(offre1).isEqualTo(offre2);

        offre2 = getOffreSample2();
        assertThat(offre1).isNotEqualTo(offre2);
    }

    @Test
    void criseTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        offre.setCrise(criseBack);
        assertThat(offre.getCrise()).isEqualTo(criseBack);

        offre.crise(null);
        assertThat(offre.getCrise()).isNull();
    }

    @Test
    void aidantTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        offre.setAidant(utilisateurBack);
        assertThat(offre.getAidant()).isEqualTo(utilisateurBack);

        offre.aidant(null);
        assertThat(offre.getAidant()).isNull();
    }

    @Test
    void demandeTest() {
        Offre offre = getOffreRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        offre.addDemande(demandeBack);
        assertThat(offre.getDemandes()).containsOnly(demandeBack);
        assertThat(demandeBack.getOffres()).containsOnly(offre);

        offre.removeDemande(demandeBack);
        assertThat(offre.getDemandes()).doesNotContain(demandeBack);
        assertThat(demandeBack.getOffres()).doesNotContain(offre);

        offre.demandes(new HashSet<>(Set.of(demandeBack)));
        assertThat(offre.getDemandes()).containsOnly(demandeBack);
        assertThat(demandeBack.getOffres()).containsOnly(offre);

        offre.setDemandes(new HashSet<>());
        assertThat(offre.getDemandes()).doesNotContain(demandeBack);
        assertThat(demandeBack.getOffres()).doesNotContain(offre);
    }
}
