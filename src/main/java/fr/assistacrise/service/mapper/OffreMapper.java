package fr.assistacrise.service.mapper;

import fr.assistacrise.domain.Crise;
import fr.assistacrise.domain.Demande;
import fr.assistacrise.domain.Offre;
import fr.assistacrise.domain.Utilisateur;
import fr.assistacrise.service.dto.CriseDTO;
import fr.assistacrise.service.dto.DemandeDTO;
import fr.assistacrise.service.dto.OffreDTO;
import fr.assistacrise.service.dto.UtilisateurDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Offre} and its DTO {@link OffreDTO}.
 */
@Mapper(componentModel = "spring")
public interface OffreMapper extends EntityMapper<OffreDTO, Offre> {
    @Mapping(target = "crise", source = "crise", qualifiedByName = "criseTitre")
    @Mapping(target = "aidant", source = "aidant", qualifiedByName = "utilisateurLogin")
    @Mapping(target = "demandes", source = "demandes", qualifiedByName = "demandeIdSet")
    OffreDTO toDto(Offre s);

    @Mapping(target = "demandes", ignore = true)
    @Mapping(target = "removeDemande", ignore = true)
    Offre toEntity(OffreDTO offreDTO);

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

    @Named("demandeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DemandeDTO toDtoDemandeId(Demande demande);

    @Named("demandeIdSet")
    default Set<DemandeDTO> toDtoDemandeIdSet(Set<Demande> demande) {
        return demande.stream().map(this::toDtoDemandeId).collect(Collectors.toSet());
    }
}
