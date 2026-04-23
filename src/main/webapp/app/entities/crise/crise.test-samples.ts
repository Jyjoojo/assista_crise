import dayjs from 'dayjs/esm';

import { ICrise, NewCrise } from './crise.model';

export const sampleWithRequiredData: ICrise = {
  id: 26287,
  titre: "pauvre d'après",
  type: 'ACCIDENT_INDUSTRIEL',
  statut: 'ACTIVE',
  dateDebut: dayjs('2026-04-23T05:41'),
  latitude: -52.18,
  longitude: -168.3,
};

export const sampleWithPartialData: ICrise = {
  id: 23891,
  titre: 'ding',
  type: 'GLISSEMENT_DE_TERRAIN',
  statut: 'ACTIVE',
  dateDebut: dayjs('2026-04-22T15:44'),
  dateFermeture: dayjs('2026-04-22T22:22'),
  latitude: -36.46,
  longitude: -72.68,
};

export const sampleWithFullData: ICrise = {
  id: 6698,
  titre: 'pas mal débarquer',
  description: 'infiniment aimable autour de',
  type: 'AUTRE',
  statut: 'ARCHIVEE',
  dateDebut: dayjs('2026-04-23T02:18'),
  dateFermeture: dayjs('2026-04-22T13:52'),
  latitude: -29.84,
  longitude: -11.9,
  rayonKm: 25816.07,
};

export const sampleWithNewData: NewCrise = {
  titre: 'défaire compléter',
  type: 'ACCIDENT_INDUSTRIEL',
  statut: 'FERMEE',
  dateDebut: dayjs('2026-04-23T03:59'),
  latitude: 19.57,
  longitude: -129.43,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
