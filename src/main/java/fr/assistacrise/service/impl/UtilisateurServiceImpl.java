package fr.assistacrise.service.impl;

import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.repository.UtilisateurRepository;
import fr.assistacrise.service.UtilisateurService;
import fr.assistacrise.service.dto.UtilisateurDTO;
import fr.assistacrise.service.mapper.UtilisateurMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.assistacrise.domain.Utilisateur}.
 */
@Service
@Transactional
public class UtilisateurServiceImpl implements UtilisateurService {

    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurServiceImpl.class);

    private final UtilisateurRepository utilisateurRepository;

    private final UtilisateurMapper utilisateurMapper;

    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository, UtilisateurMapper utilisateurMapper) {
        this.utilisateurRepository = utilisateurRepository;
        this.utilisateurMapper = utilisateurMapper;
    }

    @Override
    public UtilisateurDTO save(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to save Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    @Override
    public UtilisateurDTO update(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to update Utilisateur : {}", utilisateurDTO);
        Utilisateur utilisateur = utilisateurMapper.toEntity(utilisateurDTO);
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurMapper.toDto(utilisateur);
    }

    @Override
    public Optional<UtilisateurDTO> partialUpdate(UtilisateurDTO utilisateurDTO) {
        LOG.debug("Request to partially update Utilisateur : {}", utilisateurDTO);

        return utilisateurRepository
            .findById(utilisateurDTO.getId())
            .map(existingUtilisateur -> {
                utilisateurMapper.partialUpdate(existingUtilisateur, utilisateurDTO);

                return existingUtilisateur;
            })
            .map(utilisateurRepository::save)
            .map(utilisateurMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UtilisateurDTO> findAll() {
        LOG.debug("Request to get all Utilisateurs");
        return utilisateurRepository.findAll().stream().map(utilisateurMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UtilisateurDTO> findOne(Long id) {
        LOG.debug("Request to get Utilisateur : {}", id);
        return utilisateurRepository.findById(id).map(utilisateurMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
    }
}
