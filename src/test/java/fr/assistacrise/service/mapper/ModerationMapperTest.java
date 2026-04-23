package fr.assistacrise.service.mapper;

import static fr.assistacrise.domain.ModerationAsserts.*;
import static fr.assistacrise.domain.ModerationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModerationMapperTest {

    private ModerationMapper moderationMapper;

    @BeforeEach
    void setUp() {
        moderationMapper = new ModerationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModerationSample1();
        var actual = moderationMapper.toEntity(moderationMapper.toDto(expected));
        assertModerationAllPropertiesEquals(expected, actual);
    }
}
