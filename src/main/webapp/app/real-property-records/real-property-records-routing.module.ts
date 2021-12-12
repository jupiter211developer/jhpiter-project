import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { RealPropertyRecordsComponent } from './real-property-records.component';
import { RprSearchComponent } from './rpr-search/rpr-search.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'real-property-records',
        data: { pageTitle: 'Real Property Records' },
        loadChildren: () => import('./real-property-records.module').then(m => m.RealPropertyRecordsModule),
        component: RealPropertyRecordsComponent,
      },
      {
        path: 'real-property-records/:id/search',
        data: { pageTitle: 'Real Property Records Search' },
        component: RprSearchComponent,
      },
    ]),
  ],
  exports: [RouterModule],
})
export class RealPropertyRecordsRoutingModule {}
