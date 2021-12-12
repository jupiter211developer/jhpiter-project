import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyRecordComponent } from '../list/county-record.component';
import { CountyRecordDetailComponent } from '../detail/county-record-detail.component';
import { CountyRecordUpdateComponent } from '../update/county-record-update.component';
import { CountyRecordRoutingResolveService } from './county-record-routing-resolve.service';

const countyRecordRoute: Routes = [
  {
    path: '',
    component: CountyRecordComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyRecordDetailComponent,
    resolve: {
      countyRecord: CountyRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyRecordUpdateComponent,
    resolve: {
      countyRecord: CountyRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyRecordUpdateComponent,
    resolve: {
      countyRecord: CountyRecordRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyRecordRoute)],
  exports: [RouterModule],
})
export class CountyRecordRoutingModule {}
