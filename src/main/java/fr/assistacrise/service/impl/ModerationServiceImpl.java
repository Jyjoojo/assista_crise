package fr.assistacrise.service.impl;

import fr.assistacrise.domain.Moderation;
import fr.assistacrise.repository.ModerationRepository;
import fr.assistacrise.service.ModerationService;
import fr.assistacrise.service.dto.ModerationDTO;
import fr.assistacrise.service.mapper.ModerationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.assistacrise.domain.Moderation}.
 */
@Service
@Transactional
public class ModerationServiceImpl implements ModerationService {

    private static final Logger LOG = LoggerFactory.getLogger(ModerationServiceImpl.class);

    private final ModerationRepository moderationRepository;

    private final ModerationMapper moderationMapper;

    public ModerationServiceImpl(ModerationRepository moderationRepository, ModerationMapper moderationMapper) {
        this.moderationRepository = moderationRepository;
        this.moderationMapper = moderationMapper;
    }

    @Override
    public ModerationDTO save(ModerationDTO moderationDTO) {
        LOG.debug("Request to save Moderation : {}", moderationDTO);
        Moderation moderation = moderationMapper.toEntity(moderationDTO);
        moderation = moderationRepository.save(moderation);
        return moderationMapper.toDto(moderation);
    }

    @Override
    public ModerationDTO update(ModerationDTO moderationDTO) {
        LOG.debug("Request to update Moderation : {}", moderationDTO);
        Moderation moderation = moderationMapper.toEntity(moderationDTO);
        moderation = moderationRepository.save(moderation);
        return moderationMapper.toDto(moderation);
    }

    @Override
    public Optional<ModerationDTO> partialUpdate(ModerationDTO moderationDTO) {
        LOG.debug("Request to partially update Moderation : {}", moderationDTO);

        return moderationRepository
            .findById(moderationDTO.getId())
            .map(existingModeration -> {
                moderationMapper.partialUpdate(existingModeration, moderationDTO);

                return existingModeration;
            })
            .map(moderationRepository::save)
            .map(moderationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ModerationDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Moderations");
        return moderationRepository.findAll(pageable).map(moderationMapper::toDto);
    }

    public Page<ModerationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moderationRepository.findAllWithEagerRelationships(pageable).map(moderationMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ModerationDTO> findOne(Long id) {
        LOG.debug("Request to get Moderation : {}", id);
        return moderationRepository.findOneWithEagerRelationships(id).map(moderationMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Moderation : {}", id);
        moderationRepository.deleteById(id);
    }
}
