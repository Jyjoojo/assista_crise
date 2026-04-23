package fr.assistacrise.domain;

import static fr.assistacrise.domain.CriseTestSamples.*;
import static fr.assistacrise.domain.DemandeTestSamples.*;
import static fr.assistacrise.domain.OffreTestSamples.*;
import static fr.assistacrise.domain.SalonTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DemandeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Demande.class);
        Demande demande1 = getDemandeSample1();
        Demande demande2 = new Demande();
        assertThat(demande1).isNotEqualTo(demande2);

        demande2.setId(demande1.getId());
        assertThat(demande1).isEqualTo(demande2);

        demande2 = getDemandeSample2();
        assertThat(demande1).isNotEqualTo(demande2);
    }

    @Test
    void sinistreTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        demande.setSinistre(utilisateurBack);
        assertThat(demande.getSinistre()).isEqualTo(utilisateurBack);

        demande.sinistre(null);
        assertThat(demande.getSinistre()).isNull();
    }

    @Test
    void criseTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Crise criseBack = getCriseRandomSampleGenerator();

        demande.setCrise(criseBack);
        assertThat(demande.getCrise()).isEqualTo(criseBack);

        demande.crise(null);
        assertThat(demande.getCrise()).isNull();
    }

    @Test
    void offreTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Offre offreBack = getOffreRandomSampleGenerator();

        demande.addOffre(offreBack);
        assertThat(demande.getOffres()).containsOnly(offreBack);

        demande.removeOffre(offreBack);
        assertThat(demande.getOffres()).doesNotContain(offreBack);

        demande.offres(new HashSet<>(Set.of(offreBack)));
        assertThat(demande.getOffres()).containsOnly(offreBack);

        demande.setOffres(new HashSet<>());
        assertThat(demande.getOffres()).doesNotContain(offreBack);
    }

    @Test
    void salonTest() {
        Demande demande = getDemandeRandomSampleGenerator();
        Salon salonBack = getSalonRandomSampleGenerator();

        demande.setSalon(salonBack);
        assertThat(demande.getSalon()).isEqualTo(salonBack);
        assertThat(salonBack.getDemande()).isEqualTo(demande);

        demande.salon(null);
        assertThat(demande.getSalon()).isNull();
        assertThat(salonBack.getDemande()).isNull();
    }
}
