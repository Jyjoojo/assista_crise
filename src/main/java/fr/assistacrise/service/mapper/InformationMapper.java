package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Crise;
import fr.assistacrise.domain.Information;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.CriseDTO;
import fr.assistacrise.service.dto.InformationDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Information} and its DTO {@link InformationDTO}.
 */
@Mapper(componentModel = "spring")
public interface InformationMapper extends EntityMapper<InformationDTO, Information> {
    @Mapping(target = "crise", source = "crise", qualifiedByName = "criseTitre")
    @Mapping(target = "auteur", source = "auteur", qualifiedByName = "utilisateurLogin")
    InformationDTO toDto(Information s);

    @Named("criseTitre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titre", source = "titre")
    CriseDTO toDtoCriseTitre(Crise crise);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
