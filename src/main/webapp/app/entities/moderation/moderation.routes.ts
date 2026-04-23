import { Routes } from '@angular/router';

import { ASC } from 'app/config/navigation.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';

import ModerationResolve from './route/moderation-routing-resolve.service';

const moderationRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/moderation').then(m => m.Moderation),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/moderation-detail').then(m => m.ModerationDetail),
    resolve: {
      moderation: ModerationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/moderation-update').then(m => m.ModerationUpdate),
    resolve: {
      moderation: ModerationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/moderation-update').then(m => m.ModerationUpdate),
    resolve: {
      moderation: ModerationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default moderationRoute;
