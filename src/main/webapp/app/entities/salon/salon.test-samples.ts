import dayjs from 'dayjs/esm';

import { ISalon, NewSalon } from './salon.model';

export const sampleWithRequiredData: ISalon = {
  id: 26452,
  dateCreation: dayjs('2026-04-22T19:21'),
};

export const sampleWithPartialData: ISalon = {
  id: 10277,
  dateCreation: dayjs('2026-04-22T21:21'),
};

export const sampleWithFullData: ISalon = {
  id: 13715,
  dateCreation: dayjs('2026-04-22T18:17'),
};

export const sampleWithNewData: NewSalon = {
  dateCreation: dayjs('2026-04-22T14:45'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
