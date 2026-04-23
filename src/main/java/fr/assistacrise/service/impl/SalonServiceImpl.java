package fr.assistacrise.service.impl;

import fr.assistacrise.domain.Salon;
import fr.assistacrise.repository.SalonRepository;
import fr.assistacrise.service.SalonService;
import fr.assistacrise.service.dto.SalonDTO;
import fr.assistacrise.service.mapper.SalonMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.assistacrise.domain.Salon}.
 */
@Service
@Transactional
public class SalonServiceImpl implements SalonService {

    private static final Logger LOG = LoggerFactory.getLogger(SalonServiceImpl.class);

    private final SalonRepository salonRepository;

    private final SalonMapper salonMapper;

    public SalonServiceImpl(SalonRepository salonRepository, SalonMapper salonMapper) {
        this.salonRepository = salonRepository;
        this.salonMapper = salonMapper;
    }

    @Override
    public SalonDTO save(SalonDTO salonDTO) {
        LOG.debug("Request to save Salon : {}", salonDTO);
        Salon salon = salonMapper.toEntity(salonDTO);
        salon = salonRepository.save(salon);
        return salonMapper.toDto(salon);
    }

    @Override
    public SalonDTO update(SalonDTO salonDTO) {
        LOG.debug("Request to update Salon : {}", salonDTO);
        Salon salon = salonMapper.toEntity(salonDTO);
        salon = salonRepository.save(salon);
        return salonMapper.toDto(salon);
    }

    @Override
    public Optional<SalonDTO> partialUpdate(SalonDTO salonDTO) {
        LOG.debug("Request to partially update Salon : {}", salonDTO);

        return salonRepository
            .findById(salonDTO.getId())
            .map(existingSalon -> {
                salonMapper.partialUpdate(existingSalon, salonDTO);

                return existingSalon;
            })
            .map(salonRepository::save)
            .map(salonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalonDTO> findAll() {
        LOG.debug("Request to get all Salons");
        return salonRepository.findAll().stream().map(salonMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public Page<SalonDTO> findAllWithEagerRelationships(Pageable pageable) {
        return salonRepository.findAllWithEagerRelationships(pageable).map(salonMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalonDTO> findOne(Long id) {
        LOG.debug("Request to get Salon : {}", id);
        return salonRepository.findOneWithEagerRelationships(id).map(salonMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Salon : {}", id);
        salonRepository.deleteById(id);
    }
}
