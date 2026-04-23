import dayjs from 'dayjs/esm';

import { IDemande, NewDemande } from './demande.model';

export const sampleWithRequiredData: IDemande = {
  id: 12976,
  titre: 'dessous',
  description: 'concernant',
  statut: 'RESOLUE',
  dateCreation: dayjs('2026-04-22T15:40'),
  estArchivee: true,
};

export const sampleWithPartialData: IDemande = {
  id: 11287,
  titre: 'ouf',
  description: 'rédaction adversaire',
  statut: 'RESOLUE',
  dateCreation: dayjs('2026-04-23T11:44'),
  dateMiseAJour: dayjs('2026-04-22T12:33'),
  dateFermeture: dayjs('2026-04-23T00:28'),
  latitude: 24.87,
  estArchivee: true,
};

export const sampleWithFullData: IDemande = {
  id: 5647,
  titre: 'psitt aussi brave',
  description: 'zzzz concurrence',
  statut: 'RESOLUE',
  dateCreation: dayjs('2026-04-23T11:19'),
  dateMiseAJour: dayjs('2026-04-23T07:59'),
  dateFermeture: dayjs('2026-04-22T17:00'),
  latitude: -46.05,
  longitude: -39.25,
  estArchivee: false,
  dateDeferencement: dayjs('2026-04-22T15:23'),
};

export const sampleWithNewData: NewDemande = {
  titre: 'tsoin-tsoin hypocrite',
  description: 'pourvu que bof membre du personnel',
  statut: 'RESOLUE',
  dateCreation: dayjs('2026-04-22T15:55'),
  estArchivee: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
