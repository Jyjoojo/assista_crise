package fr.assistacrise.domain;

import static fr.assistacrise.domain.MessageTestSamples.*;
import static fr.assistacrise.domain.SalonTestSamples.*;
import static fr.assistacrise.domain.UtilisateurTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Message.class);
        Message message1 = getMessageSample1();
        Message message2 = new Message();
        assertThat(message1).isNotEqualTo(message2);

        message2.setId(message1.getId());
        assertThat(message1).isEqualTo(message2);

        message2 = getMessageSample2();
        assertThat(message1).isNotEqualTo(message2);
    }

    @Test
    void salonTest() {
        Message message = getMessageRandomSampleGenerator();
        Salon salonBack = getSalonRandomSampleGenerator();

        message.setSalon(salonBack);
        assertThat(message.getSalon()).isEqualTo(salonBack);

        message.salon(null);
        assertThat(message.getSalon()).isNull();
    }

    @Test
    void auteurTest() {
        Message message = getMessageRandomSampleGenerator();
        Utilisateur utilisateurBack = getUtilisateurRandomSampleGenerator();

        message.setAuteur(utilisateurBack);
        assertThat(message.getAuteur()).isEqualTo(utilisateurBack);

        message.auteur(null);
        assertThat(message.getAuteur()).isNull();
    }
}
