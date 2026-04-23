package fr.assistacrise.domain;

import static fr.assistacrise.domain.SalonTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UtilisateurTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Utilisateur.class);
        Utilisateur utilisateur1 = getUtilisateurSample1();
        Utilisateur utilisateur2 = new Utilisateur();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);

        utilisateur2.setId(utilisateur1.getId());
        assertThat(utilisateur1).isEqualTo(utilisateur2);

        utilisateur2 = getUtilisateurSample2();
        assertThat(utilisateur1).isNotEqualTo(utilisateur2);
    }

    @Test
    void salonTest() {
        Utilisateur utilisateur = getUtilisateurRandomSampleGenerator();
        Salon salonBack = getSalonRandomSampleGenerator();

        utilisateur.addSalon(salonBack);
        assertThat(utilisateur.getSalons()).containsOnly(salonBack);
        assertThat(salonBack.getParticipants()).containsOnly(utilisateur);

        utilisateur.removeSalon(salonBack);
        assertThat(utilisateur.getSalons()).doesNotContain(salonBack);
        assertThat(salonBack.getParticipants()).doesNotContain(utilisateur);

        utilisateur.salons(new HashSet<>(Set.of(salonBack)));
        assertThat(utilisateur.getSalons()).containsOnly(salonBack);
        assertThat(salonBack.getParticipants()).containsOnly(utilisateur);

        utilisateur.setSalons(new HashSet<>());
        assertThat(utilisateur.getSalons()).doesNotContain(salonBack);
        assertThat(salonBack.getParticipants()).doesNotContain(utilisateur);
    }
}
