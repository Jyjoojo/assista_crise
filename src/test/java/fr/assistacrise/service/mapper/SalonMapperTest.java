package fr.assistacrise.service.mapper;

import static fr.assistacrise.domain.SalonAsserts.*;
import static fr.assistacrise.domain.SalonTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalonMapperTest {

    private SalonMapper salonMapper;

    @BeforeEach
    void setUp() {
        salonMapper = new SalonMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSalonSample1();
        var actual = salonMapper.toEntity(salonMapper.toDto(expected));
        assertSalonAllPropertiesEquals(expected, actual);
    }
}
