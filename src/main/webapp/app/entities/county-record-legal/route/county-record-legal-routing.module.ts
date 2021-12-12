import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyRecordLegalComponent } from '../list/county-record-legal.component';
import { CountyRecordLegalDetailComponent } from '../detail/county-record-legal-detail.component';
import { CountyRecordLegalUpdateComponent } from '../update/county-record-legal-update.component';
import { CountyRecordLegalRoutingResolveService } from './county-record-legal-routing-resolve.service';

const countyRecordLegalRoute: Routes = [
  {
    path: '',
    component: CountyRecordLegalComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyRecordLegalDetailComponent,
    resolve: {
      countyRecordLegal: CountyRecordLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyRecordLegalUpdateComponent,
    resolve: {
      countyRecordLegal: CountyRecordLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyRecordLegalUpdateComponent,
    resolve: {
      countyRecordLegal: CountyRecordLegalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyRecordLegalRoute)],
  exports: [RouterModule],
})
export class CountyRecordLegalRoutingModule {}
