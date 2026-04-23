import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import SalonResolve from './route/salon-routing-resolve.service';

const salonRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/salon').then(m => m.Salon),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/salon-detail').then(m => m.SalonDetail),
    resolve: {
      salon: SalonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/salon-update').then(m => m.SalonUpdate),
    resolve: {
      salon: SalonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/salon-update').then(m => m.SalonUpdate),
    resolve: {
      salon: SalonResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default salonRoute;
