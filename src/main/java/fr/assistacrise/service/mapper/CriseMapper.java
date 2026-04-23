package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Crise;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.CriseDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Crise} and its DTO {@link CriseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CriseMapper extends EntityMapper<CriseDTO, Crise> {
    @Mapping(target = "declarant", source = "declarant", qualifiedByName = "utilisateurLogin")
    CriseDTO toDto(Crise s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
