import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CountyImageComponent } from '../list/county-image.component';
import { CountyImageDetailComponent } from '../detail/county-image-detail.component';
import { CountyImageUpdateComponent } from '../update/county-image-update.component';
import { CountyImageRoutingResolveService } from './county-image-routing-resolve.service';

const countyImageRoute: Routes = [
  {
    path: '',
    component: CountyImageComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CountyImageDetailComponent,
    resolve: {
      countyImage: CountyImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CountyImageUpdateComponent,
    resolve: {
      countyImage: CountyImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CountyImageUpdateComponent,
    resolve: {
      countyImage: CountyImageRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(countyImageRoute)],
  exports: [RouterModule],
})
export class CountyImageRoutingModule {}
