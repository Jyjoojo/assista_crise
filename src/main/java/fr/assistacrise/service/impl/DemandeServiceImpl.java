package fr.assistacrise.service.impl;

import fr.assistacrise.domain.Demande;
import fr.assistacrise.repository.DemandeRepository;
import fr.assistacrise.service.DemandeService;
import fr.assistacrise.service.dto.DemandeDTO;
import fr.assistacrise.service.mapper.DemandeMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.assistacrise.domain.Demande}.
 */
@Service
@Transactional
public class DemandeServiceImpl implements DemandeService {

    private static final Logger LOG = LoggerFactory.getLogger(DemandeServiceImpl.class);

    private final DemandeRepository demandeRepository;

    private final DemandeMapper demandeMapper;

    public DemandeServiceImpl(DemandeRepository demandeRepository, DemandeMapper demandeMapper) {
        this.demandeRepository = demandeRepository;
        this.demandeMapper = demandeMapper;
    }

    @Override
    public DemandeDTO save(DemandeDTO demandeDTO) {
        LOG.debug("Request to save Demande : {}", demandeDTO);
        Demande demande = demandeMapper.toEntity(demandeDTO);
        demande = demandeRepository.save(demande);
        return demandeMapper.toDto(demande);
    }

    @Override
    public DemandeDTO update(DemandeDTO demandeDTO) {
        LOG.debug("Request to update Demande : {}", demandeDTO);
        Demande demande = demandeMapper.toEntity(demandeDTO);
        demande = demandeRepository.save(demande);
        return demandeMapper.toDto(demande);
    }

    @Override
    public Optional<DemandeDTO> partialUpdate(DemandeDTO demandeDTO) {
        LOG.debug("Request to partially update Demande : {}", demandeDTO);

        return demandeRepository
            .findById(demandeDTO.getId())
            .map(existingDemande -> {
                demandeMapper.partialUpdate(existingDemande, demandeDTO);

                return existingDemande;
            })
            .map(demandeRepository::save)
            .map(demandeMapper::toDto);
    }

    public Page<DemandeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return demandeRepository.findAllWithEagerRelationships(pageable).map(demandeMapper::toDto);
    }

    /**
     *  Get all the demandes where Salon is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DemandeDTO> findAllWhereSalonIsNull() {
        LOG.debug("Request to get all demandes where Salon is null");
        return StreamSupport.stream(demandeRepository.findAll().spliterator(), false)
            .filter(demande -> demande.getSalon() == null)
            .map(demandeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DemandeDTO> findOne(Long id) {
        LOG.debug("Request to get Demande : {}", id);
        return demandeRepository.findOneWithEagerRelationships(id).map(demandeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Demande : {}", id);
        demandeRepository.deleteById(id);
    }
}
