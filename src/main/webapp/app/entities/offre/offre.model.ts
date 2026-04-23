import dayjs from 'dayjs/esm';

import { ICrise } from 'app/entities/crise/crise.model';
import { IDemande } from 'app/entities/demande/demande.model';
import { StatutOffre } from 'app/entities/enumerations/statut-offre.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IOffre {
  id: number;
  titre?: string | null;
  description?: string | null;
  statut?: keyof typeof StatutOffre | null;
  dateCreation?: dayjs.Dayjs | null;
  dateMiseAJour?: dayjs.Dayjs | null;
  latitude?: number | null;
  longitude?: number | null;
  estArchivee?: boolean | null;
  dateDeferencement?: dayjs.Dayjs | null;
  crise?: Pick<ICrise, 'id' | 'titre'> | null;
  aidant?: Pick<IUtilisateur, 'id' | 'login'> | null;
  demandes?: Pick<IDemande, 'id'>[] | null;
}

export type NewOffre = Omit<IOffre, 'id'> & { id: null };
