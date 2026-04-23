import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import UtilisateurResolve from './route/utilisateur-routing-resolve.service';

const utilisateurRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/utilisateur').then(m => m.Utilisateur),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/utilisateur-detail').then(m => m.UtilisateurDetail),
    resolve: {
      utilisateur: UtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/utilisateur-update').then(m => m.UtilisateurUpdate),
    resolve: {
      utilisateur: UtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/utilisateur-update').then(m => m.UtilisateurUpdate),
    resolve: {
      utilisateur: UtilisateurResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default utilisateurRoute;
