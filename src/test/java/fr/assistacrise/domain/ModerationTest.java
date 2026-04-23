package fr.assistacrise.domain;

import static fr.assistacrise.domain.DemandeTestSamples.*;
import static fr.assistacrise.domain.ModerationTestSamples.*;
import static fr.assistacrise.domain.OffreTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModerationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Moderation.class);
        Moderation moderation1 = getModerationSample1();
        Moderation moderation2 = new Moderation();
        assertThat(moderation1).isNotEqualTo(moderation2);

        moderation2.setId(moderation1.getId());
        assertThat(moderation1).isEqualTo(moderation2);

        moderation2 = getModerationSample2();
        assertThat(moderation1).isNotEqualTo(moderation2);
    }

    @Test
    void administrateurTest() {
        Moderation moderation = getModerationRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        moderation.setAdministrateur(utilisateurBack);
        assertThat(moderation.getAdministrateur()).isEqualTo(utilisateurBack);

        moderation.administrateur(null);
        assertThat(moderation.getAdministrateur()).isNull();
    }

    @Test
    void demandeTest() {
        Moderation moderation = getModerationRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        moderation.setDemande(demandeBack);
        assertThat(moderation.getDemande()).isEqualTo(demandeBack);

        moderation.demande(null);
        assertThat(moderation.getDemande()).isNull();
    }

    @Test
    void offreTest() {
        Moderation moderation = getModerationRandomSampleGenerator();
        Offre offreBack = getOffreRandomSampleGenerator();

        moderation.setOffre(offreBack);
        assertThat(moderation.getOffre()).isEqualTo(offreBack);

        moderation.offre(null);
        assertThat(moderation.getOffre()).isNull();
    }

    @Test
    void utilisateurCibleTest() {
        Moderation moderation = getModerationRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        moderation.setUtilisateurCible(utilisateurBack);
        assertThat(moderation.getUtilisateurCible()).isEqualTo(utilisateurBack);

        moderation.utilisateurCible(null);
        assertThat(moderation.getUtilisateurCible()).isNull();
    }
}
