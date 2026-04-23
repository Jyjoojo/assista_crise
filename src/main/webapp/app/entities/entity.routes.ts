import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'assistaCriseApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'user-management',
    data: { pageTitle: 'userManagement.home.title' },
    loadChildren: () => import('./admin/user-management/user-management.routes'),
  },
  {
    path: 'utilisateur',
    data: { pageTitle: 'assistaCriseApp.utilisateur.home.title' },
    loadChildren: () => import('./utilisateur/utilisateur.routes'),
  },
  {
    path: 'crise',
    data: { pageTitle: 'assistaCriseApp.crise.home.title' },
    loadChildren: () => import('./crise/crise.routes'),
  },
  {
    path: 'demande',
    data: { pageTitle: 'assistaCriseApp.demande.home.title' },
    loadChildren: () => import('./demande/demande.routes'),
  },
  {
    path: 'offre',
    data: { pageTitle: 'assistaCriseApp.offre.home.title' },
    loadChildren: () => import('./offre/offre.routes'),
  },
  {
    path: 'information',
    data: { pageTitle: 'assistaCriseApp.information.home.title' },
    loadChildren: () => import('./information/information.routes'),
  },
  {
    path: 'salon',
    data: { pageTitle: 'assistaCriseApp.salon.home.title' },
    loadChildren: () => import('./salon/salon.routes'),
  },
  {
    path: 'message',
    data: { pageTitle: 'assistaCriseApp.message.home.title' },
    loadChildren: () => import('./message/message.routes'),
  },
  {
    path: 'moderation',
    data: { pageTitle: 'assistaCriseApp.moderation.home.title' },
    loadChildren: () => import('./moderation/moderation.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
