import dayjs from 'dayjs/esm';

import { StatutCrise } from 'app/entities/enumerations/statut-crise.model';
import { TypeCrise } from 'app/entities/enumerations/type-crise.model';
import { IUtilisateur } from 'app/entities/utilisateur/utilisateur.model';

export interface ICrise {
  id: number;
  titre?: string | null;
  description?: string | null;
  type?: keyof typeof TypeCrise | null;
  statut?: keyof typeof StatutCrise | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFermeture?: dayjs.Dayjs | null;
  latitude?: number | null;
  longitude?: number | null;
  rayonKm?: number | null;
  declarant?: Pick<IUtilisateur, 'id' | 'login'> | null;
}

export type NewCrise = Omit<ICrise, 'id'> & { id: null };
