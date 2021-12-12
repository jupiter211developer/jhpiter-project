import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyImagePageComponent } from '../list/county-image-page.component';
import { CountyImagePageDetailComponent } from '../detail/county-image-page-detail.component';
import { CountyImagePageUpdateComponent } from '../update/county-image-page-update.component';
import { CountyImagePageRoutingResolveService } from './county-image-page-routing-resolve.service';

const countyImagePageRoute: Routes = [
  {
    path: '',
    component: CountyImagePageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyImagePageDetailComponent,
    resolve: {
      countyImagePage: CountyImagePageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyImagePageUpdateComponent,
    resolve: {
      countyImagePage: CountyImagePageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyImagePageUpdateComponent,
    resolve: {
      countyImagePage: CountyImagePageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyImagePageRoute)],
  exports: [RouterModule],
})
export class CountyImagePageRoutingModule {}
