import dayjs from 'dayjs/esm';

import { IDemande } from 'app/entities/demande/demande.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface ISalon {
  id: number;
  dateCreation?: dayjs.Dayjs | null;
  demande?: Pick<IDemande, 'id'> | null;
  participants?: Pick<IUtilisateur, 'id' | 'login'>[] | null;
}

export type NewSalon = Omit<ISalon, 'id'> & { id: null };
