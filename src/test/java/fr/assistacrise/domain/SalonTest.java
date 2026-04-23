package fr.assistacrise.domain;

import static fr.assistacrise.domain.DemandeTestSamples.*;
import static fr.assistacrise.domain.SalonTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SalonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Salon.class);
        Salon salon1 = getSalonSample1();
        Salon salon2 = new Salon();
        assertThat(salon1).isNotEqualTo(salon2);

        salon2.setId(salon1.getId());
        assertThat(salon1).isEqualTo(salon2);

        salon2 = getSalonSample2();
        assertThat(salon1).isNotEqualTo(salon2);
    }

    @Test
    void demandeTest() {
        Salon salon = getSalonRandomSampleGenerator();
        Demande demandeBack = getDemandeRandomSampleGenerator();

        salon.setDemande(demandeBack);
        assertThat(salon.getDemande()).isEqualTo(demandeBack);

        salon.demande(null);
        assertThat(salon.getDemande()).isNull();
    }

    @Test
    void participantTest() {
        Salon salon = getSalonRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        salon.addParticipant(utilisateurBack);
        assertThat(salon.getParticipants()).containsOnly(utilisateurBack);

        salon.removeParticipant(utilisateurBack);
        assertThat(salon.getParticipants()).doesNotContain(utilisateurBack);

        salon.participants(new HashSet<>(Set.of(utilisateurBack)));
        assertThat(salon.getParticipants()).containsOnly(utilisateurBack);

        salon.setParticipants(new HashSet<>());
        assertThat(salon.getParticipants()).doesNotContain(utilisateurBack);
    }
}
