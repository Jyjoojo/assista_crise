package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Message;
import fr.assistacrise.domain.Salon;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.MessageDTO;
import fr.assistacrise.service.dto.SalonDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Message} and its DTO {@link MessageDTO}.
 */
@Mapper(componentModel = "spring")
public interface MessageMapper extends EntityMapper<MessageDTO, Message> {
    @Mapping(target = "salon", source = "salon", qualifiedByName = "salonId")
    @Mapping(target = "auteur", source = "auteur", qualifiedByName = "utilisateurLogin")
    MessageDTO toDto(Message s);

    @Named("salonId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SalonDTO toDtoSalonId(Salon salon);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);
}
