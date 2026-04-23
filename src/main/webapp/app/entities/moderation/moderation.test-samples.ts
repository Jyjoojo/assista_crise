import dayjs from 'dayjs/esm';

import { IModeration, NewModeration } from './moderation.model';

export const sampleWithRequiredData: IModeration = {
  id: 30122,
  motif: 'ouah psitt',
  dateModeration: dayjs('2026-04-22T20:48'),
  action: 'BANNISSEMENT_UTILISATEUR',
};

export const sampleWithPartialData: IModeration = {
  id: 25615,
  motif: 'en faveur de',
  dateModeration: dayjs('2026-04-23T09:14'),
  action: 'BANNISSEMENT_UTILISATEUR',
};

export const sampleWithFullData: IModeration = {
  id: 7863,
  motif: 'marron charitable avant-hier',
  dateModeration: dayjs('2026-04-22T14:51'),
  action: 'SUPPRESSION_ANNONCE',
};

export const sampleWithNewData: NewModeration = {
  motif: 'avant que',
  dateModeration: dayjs('2026-04-23T08:42'),
  action: 'SUPPRESSION_ANNONCE',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
