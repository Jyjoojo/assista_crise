import dayjs from 'dayjs/esm';

import { IMessage, NewMessage } from './message.model';

export const sampleWithRequiredData: IMessage = {
  id: 10168,
  contenu: 'aussi',
  dateEnvoi: dayjs('2026-04-23T10:40'),
  estModere: false,
};

export const sampleWithPartialData: IMessage = {
  id: 53,
  contenu: 'de crainte que un peu concernant',
  dateEnvoi: dayjs('2026-04-23T02:12'),
  estModere: false,
};

export const sampleWithFullData: IMessage = {
  id: 2775,
  contenu: 'dégager appartenir reprocher',
  dateEnvoi: dayjs('2026-04-23T07:17'),
  estModere: true,
};

export const sampleWithNewData: NewMessage = {
  contenu: "à l'insu de jamais autrement",
  dateEnvoi: dayjs('2026-04-23T08:13'),
  estModere: true,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
