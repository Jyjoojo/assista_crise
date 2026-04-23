package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Demande;
import fr.assistacrise.domain.Moderation;
import fr.assistacrise.domain.Offre;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.DemandeDTO;
import fr.assistacrise.service.dto.ModerationDTO;
import fr.assistacrise.service.dto.OffreDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Moderation} and its DTO {@link ModerationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ModerationMapper extends EntityMapper<ModerationDTO, Moderation> {
    @Mapping(target = "administrateur", source = "administrateur", qualifiedByName = "utilisateurLogin")
    @Mapping(target = "demande", source = "demande", qualifiedByName = "demandeId")
    @Mapping(target = "offre", source = "offre", qualifiedByName = "offreId")
    @Mapping(target = "utilisateurCible", source = "utilisateurCible", qualifiedByName = "utilisateurLogin")
    ModerationDTO toDto(Moderation s);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    @Named("offreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OffreDTO toDtoOffreId(Offre offre);
}
