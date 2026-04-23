package fr.assistacrise.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import fr.assistacrise.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ModerationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ModerationDTO.class);
        ModerationDTO moderationDTO1 = new ModerationDTO();
        moderationDTO1.setId(1L);
        ModerationDTO moderationDTO2 = new ModerationDTO();
        assertThat(moderationDTO1).isNotEqualTo(moderationDTO2);
        moderationDTO2.setId(moderationDTO1.getId());
        assertThat(moderationDTO1).isEqualTo(moderationDTO2);
        moderationDTO2.setId(2L);
        assertThat(moderationDTO1).isNotEqualTo(moderationDTO2);
        moderationDTO1.setId(null);
        assertThat(moderationDTO1).isNotEqualTo(moderationDTO2);
    }
}
