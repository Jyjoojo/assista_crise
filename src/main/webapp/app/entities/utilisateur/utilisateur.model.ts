import dayjs from 'dayjs/esm';

import { RoleUtilisateur } from 'app/entities/enumerations/role-utilisateur.model';
import { ISalon } from 'app/entities/salon/salon.model';

export interface IUtilisateur {
  id: number;
  login?: string | null;
  email?: string | null;
  motDePasse?: string | null;
  prenom?: string | null;
  nom?: string | null;
  telephone?: string | null;
  role?: keyof typeof RoleUtilisateur | null;
  actif?: boolean | null;
  dateInscription?: dayjs.Dayjs | null;
  dateBannissement?: dayjs.Dayjs | null;
  estBanni?: boolean | null;
  salons?: Pick<ISalon, 'id'>[] | null;
}

export type NewUtilisateur = Omit<IUtilisateur, 'id'> & { id: null };
