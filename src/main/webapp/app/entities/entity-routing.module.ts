import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'state',
        data: { pageTitle: 'States' },
        loadChildren: () => import('./state/state.module').then(m => m.StateModule),
      },
      {
        path: 'county',
        data: { pageTitle: 'Counties' },
        loadChildren: () => import('./county/county.module').then(m => m.CountyModule),
      },
      {
        path: 'counties-avaiable',
        data: { pageTitle: 'CountiesAvaiables' },
        loadChildren: () => import('./counties-avaiable/counties-avaiable.module').then(m => m.CountiesAvaiableModule),
      },
      {
        path: 'county-record',
        data: { pageTitle: 'CountyRecords' },
        loadChildren: () => import('./county-record/county-record.module').then(m => m.CountyRecordModule),
      },
      {
        path: 'county-image',
        data: { pageTitle: 'CountyImages' },
        loadChildren: () => import('./county-image/county-image.module').then(m => m.CountyImageModule),
      },
      {
        path: 'county-image-page',
        data: { pageTitle: 'CountyImagePages' },
        loadChildren: () => import('./county-image-page/county-image-page.module').then(m => m.CountyImagePageModule),
      },
      {
        path: 'county-record-legal',
        data: { pageTitle: 'CountyRecordLegals' },
        loadChildren: () => import('./county-record-legal/county-record-legal.module').then(m => m.CountyRecordLegalModule),
      },
      {
        path: 'county-record-party',
        data: { pageTitle: 'CountyRecordParties' },
        loadChildren: () => import('./county-record-party/county-record-party.module').then(m => m.CountyRecordPartyModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
