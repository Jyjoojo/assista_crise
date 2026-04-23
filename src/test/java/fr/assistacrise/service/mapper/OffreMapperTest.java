package fr.assistacrise.service.mapper;

import static fr.assistacrise.domain.OffreAsserts.*;
import static fr.assistacrise.domain.OffreTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OffreMapperTest {

    private OffreMapper offreMapper;

    @BeforeEach
    void setUp() {
        offreMapper = new OffreMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOffreSample1();
        var actual = offreMapper.toEntity(offreMapper.toDto(expected));
        assertOffreAllPropertiesEquals(expected, actual);
    }
}
