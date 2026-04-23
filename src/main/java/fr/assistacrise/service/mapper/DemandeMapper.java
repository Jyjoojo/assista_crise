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
 * Mapper for the entity {@link Demande} and its DTO {@link DemandeDTO}.
 */
@Mapper(componentModel = "spring")
public interface DemandeMapper extends EntityMapper<DemandeDTO, Demande> {
    @Mapping(target = "sinistre", source = "sinistre", qualifiedByName = "utilisateurLogin")
    @Mapping(target = "crise", source = "crise", qualifiedByName = "criseTitre")
    @Mapping(target = "offres", source = "offres", qualifiedByName = "offreIdSet")
    DemandeDTO toDto(Demande s);

    @Mapping(target = "removeOffre", ignore = true)
    Demande toEntity(DemandeDTO demandeDTO);

    @Named("utilisateurLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UtilisateurDTO toDtoUtilisateurLogin(Utilisateur utilisateur);

    @Named("criseTitre")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titre", source = "titre")
    CriseDTO toDtoCriseTitre(Crise crise);

    @Named("offreId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OffreDTO toDtoOffreId(Offre offre);

    @Named("offreIdSet")
    default Set<OffreDTO> toDtoOffreIdSet(Set<Offre> offre) {
        return offre.stream().map(this::toDtoOffreId).collect(Collectors.toSet());
    }
}
