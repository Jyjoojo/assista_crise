package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Demande;
import fr.assistacrise.domain.Salon;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.DemandeDTO;
import fr.assistacrise.service.dto.SalonDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Salon} and its DTO {@link SalonDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalonMapper extends EntityMapper<SalonDTO, Salon> {
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    @Mapping(target = "participants", source = "participants", qualifiedByName = "utilisateurLoginSet")
    SalonDTO toDto(Salon s);

    @Mapping(target = "removeParticipant", ignore = true)
    Salon toEntity(SalonDTO salonDTO);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);

    @Named("utilisateurLoginSet")
    default Set<UtilisateurDTO> toDtoUtilisateurLoginSet(Set<Utilisateur> utilisateur) {
        return utilisateur.stream().map(this::toDtoUtilisateurLogin).collect(Collectors.toSet());
    }
}
