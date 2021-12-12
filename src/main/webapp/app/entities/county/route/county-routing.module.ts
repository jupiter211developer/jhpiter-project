import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyComponent } from '../list/county.component';
import { CountyDetailComponent } from '../detail/county-detail.component';
import { CountyUpdateComponent } from '../update/county-update.component';
import { CountyRoutingResolveService } from './county-routing-resolve.service';

const countyRoute: Routes = [
  {
    path: '',
    component: CountyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyDetailComponent,
    resolve: {
      county: CountyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyUpdateComponent,
    resolve: {
      county: CountyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyUpdateComponent,
    resolve: {
      county: CountyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyRoute)],
  exports: [RouterModule],
})
export class CountyRoutingModule {}
