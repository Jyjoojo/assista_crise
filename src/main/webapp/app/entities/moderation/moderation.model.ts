import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { ActionModeration } from 'app/entities/enumerations/action-moderation.model';
import { IOffre } from 'app/entities/offre/offre.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IModeration {
  id: number;
  motif?: string | null;
  dateModeration?: dayjs.Dayjs | null;
  action?: keyof typeof ActionModeration | null;
  administrateur?: Pick<IUtilisateur, 'id' | 'login'> | null;
  demande?: Pick<IDemande, 'id'> | null;
  offre?: Pick<IOffre, 'id'> | null;
  utilisateurCible?: Pick<IUtilisateur, 'id' | 'login'> | null;
}

export type NewModeration = Omit<IModeration, 'id'> & { id: null };
