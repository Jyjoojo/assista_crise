import dayjs from 'dayjs/esm';

import { IUtilisateur, NewUtilisateur } from './utilisateur.model';

export const sampleWithRequiredData: IUtilisateur = {
  id: 7635,
  login: 'vaste derechef',
  email: 'i"@j}OD4.Xv',
  motDePasse: 'aussitôt que',
  prenom: 'sans que pendant',
  nom: 'coupable bzzz',
  role: 'CITOYEN_AIDANT',
  actif: false,
  dateInscription: dayjs('2026-04-23T05:26'),
  estBanni: false,
};

export const sampleWithPartialData: IUtilisateur = {
  id: 25426,
  login: 'propre raser au cas où',
  email: '.@!"q.UUE1:K',
  motDePasse: 'partenaire large sous',
  prenom: 'affable tant que',
  nom: 'super ressentir',
  role: 'ADMINISTRATEUR',
  actif: true,
  dateInscription: dayjs('2026-04-22T14:31'),
  estBanni: true,
};

export const sampleWithFullData: IUtilisateur = {
  id: 18250,
  login: 'si bien que concernant',
  email: '0@{.4Hnk{',
  motDePasse: 'fonctionnaire',
  prenom: 'parvenir pschitt',
  nom: 'patientèle',
  telephone: '0492841706',
  role: 'ADMINISTRATEUR',
  actif: false,
  dateInscription: dayjs('2026-04-22T22:04'),
  dateBannissement: dayjs('2026-04-22T23:46'),
  estBanni: false,
};

export const sampleWithNewData: NewUtilisateur = {
  login: 'hebdomadaire',
  email: ',VC@4s00zR.~s,Z',
  motDePasse: 'au cas où snob splendide',
  prenom: 'mordre toc afin que',
  nom: 'orange',
  role: 'ADMINISTRATEUR',
  actif: true,
  dateInscription: dayjs('2026-04-23T08:29'),
  estBanni: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
