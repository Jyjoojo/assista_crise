import dayjs from 'dayjs/esm';

import { ISalon } from 'app/entities/salon/salon.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IMessage {
  id: number;
  contenu?: string | null;
  dateEnvoi?: dayjs.Dayjs | null;
  estModere?: boolean | null;
  salon?: Pick<ISalon, 'id'> | null;
  auteur?: Pick<IUtilisateur, 'id' | 'login'> | null;
}

export type NewMessage = Omit<IMessage, 'id'> & { id: null };
