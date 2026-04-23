package fr.assistacrise.domain;

import static fr.assistacrise.domain.CriseTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CriseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crise.class);
        Crise crise1 = getCriseSample1();
        Crise crise2 = new Crise();
        assertThat(crise1).isNotEqualTo(crise2);

        crise2.setId(crise1.getId());
        assertThat(crise1).isEqualTo(crise2);

        crise2 = getCriseSample2();
        assertThat(crise1).isNotEqualTo(crise2);
    }

    @Test
    void declarantTest() {
        Crise crise = getCriseRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        crise.setDeclarant(utilisateurBack);
        assertThat(crise.getDeclarant()).isEqualTo(utilisateurBack);

        crise.declarant(null);
        assertThat(crise.getDeclarant()).isNull();
    }
}
