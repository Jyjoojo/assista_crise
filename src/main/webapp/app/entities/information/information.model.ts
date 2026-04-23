import dayjs from 'dayjs/esm';

import { ICrise } from 'app/entities/crise/crise.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface IInformation {
  id: number;
  titre?: string | null;
  contenu?: string | null;
  datePublication?: dayjs.Dayjs | null;
  dateMiseAJour?: dayjs.Dayjs | null;
  estVisible?: boolean | null;
  crise?: Pick<ICrise, 'id' | 'titre'> | null;
  auteur?: Pick<IUtilisateur, 'id' | 'login'> | null;
}

export type NewInformation = Omit<IInformation, 'id'> & { id: null };
