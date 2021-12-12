import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountiesAvaiableComponent } from '../list/counties-avaiable.component';
import { CountiesAvaiableDetailComponent } from '../detail/counties-avaiable-detail.component';
import { CountiesAvaiableUpdateComponent } from '../update/counties-avaiable-update.component';
import { CountiesAvaiableRoutingResolveService } from './counties-avaiable-routing-resolve.service';

const countiesAvaiableRoute: Routes = [
  {
    path: '',
    component: CountiesAvaiableComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountiesAvaiableDetailComponent,
    resolve: {
      countiesAvaiable: CountiesAvaiableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountiesAvaiableUpdateComponent,
    resolve: {
      countiesAvaiable: CountiesAvaiableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountiesAvaiableUpdateComponent,
    resolve: {
      countiesAvaiable: CountiesAvaiableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countiesAvaiableRoute)],
  exports: [RouterModule],
})
export class CountiesAvaiableRoutingModule {}
