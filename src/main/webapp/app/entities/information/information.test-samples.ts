import dayjs from 'dayjs/esm';

import { IInformation, NewInformation } from './information.model';

export const sampleWithRequiredData: IInformation = {
  id: 30844,
  titre: 'vlan toutefois assister',
  contenu: 'outre applaudir longtemps',
  datePublication: dayjs('2026-04-22T17:43'),
  estVisible: false,
};

export const sampleWithPartialData: IInformation = {
  id: 1158,
  titre: 'blablabla vivace',
  contenu: 'reporter foule repasser',
  datePublication: dayjs('2026-04-23T09:48'),
  estVisible: false,
};

export const sampleWithFullData: IInformation = {
  id: 23818,
  titre: 'entre',
  contenu: "aujourd'hui même si atchoum",
  datePublication: dayjs('2026-04-23T06:56'),
  dateMiseAJour: dayjs('2026-04-23T03:58'),
  estVisible: false,
};

export const sampleWithNewData: NewInformation = {
  titre: 'au-dessous beaucoup tellement',
  contenu: 'gestionnaire',
  datePublication: dayjs('2026-04-22T17:43'),
  estVisible: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
