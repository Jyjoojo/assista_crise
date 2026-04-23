import dayjs from 'dayjs/esm';

import { IOffre, NewOffre } from './offre.model';

export const sampleWithRequiredData: IOffre = {
  id: 11043,
  titre: 'vouh verger abandonner',
  description: 'auprès de grandement quoique',
  statut: 'DISPONIBLE',
  dateCreation: dayjs('2026-04-22T20:25'),
  estArchivee: false,
};

export const sampleWithPartialData: IOffre = {
  id: 23571,
  titre: 'sous à condition que porte-parole',
  description: 'jusqu’à ce que électorat',
  statut: 'CLOTUREE',
  dateCreation: dayjs('2026-04-23T02:41'),
  estArchivee: true,
  dateDeferencement: dayjs('2026-04-22T19:29'),
};

export const sampleWithFullData: IOffre = {
  id: 14500,
  titre: 'raser vu que bzzz',
  description: 'spécialiste hebdomadaire',
  statut: 'EN_COURS',
  dateCreation: dayjs('2026-04-22T15:47'),
  dateMiseAJour: dayjs('2026-04-22T14:24'),
  latitude: -67.39,
  longitude: 30.51,
  estArchivee: true,
  dateDeferencement: dayjs('2026-04-22T21:20'),
};

export const sampleWithNewData: NewOffre = {
  titre: 'tellement',
  description: 'sous',
  statut: 'EN_COURS',
  dateCreation: dayjs('2026-04-23T05:58'),
  estArchivee: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
