import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyRecordPartyComponent } from '../list/county-record-party.component';
import { CountyRecordPartyDetailComponent } from '../detail/county-record-party-detail.component';
import { CountyRecordPartyUpdateComponent } from '../update/county-record-party-update.component';
import { CountyRecordPartyRoutingResolveService } from './county-record-party-routing-resolve.service';

const countyRecordPartyRoute: Routes = [
  {
    path: '',
    component: CountyRecordPartyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyRecordPartyDetailComponent,
    resolve: {
      countyRecordParty: CountyRecordPartyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyRecordPartyUpdateComponent,
    resolve: {
      countyRecordParty: CountyRecordPartyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyRecordPartyUpdateComponent,
    resolve: {
      countyRecordParty: CountyRecordPartyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyRecordPartyRoute)],
  exports: [RouterModule],
})
export class CountyRecordPartyRoutingModule {}
