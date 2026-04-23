package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Salon;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.SalonDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utilisateur} and its DTO {@link UtilisateurDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends EntityMapper<UtilisateurDTO, Utilisateur> {
    @Mapping(target = "salons", source = "salons", qualifiedByName = "salonIdSet")
    UtilisateurDTO toDto(Utilisateur s);

    @Mapping(target = "salons", ignore = true)
    @Mapping(target = "removeSalon", ignore = true)
    Utilisateur toEntity(UtilisateurDTO utilisateurDTO);

    @Named("salonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalonDTO toDtoSalonId(Salon salon);

    @Named("salonIdSet")
    default Set<SalonDTO> toDtoSalonIdSet(Set<Salon> salon) {
        return salon.stream().map(this::toDtoSalonId).collect(Collectors.toSet());
    }
}
