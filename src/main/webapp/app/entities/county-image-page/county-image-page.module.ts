import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyImagePageComponent } from './list/county-image-page.component';
import { CountyImagePageDetailComponent } from './detail/county-image-page-detail.component';
import { CountyImagePageUpdateComponent } from './update/county-image-page-update.component';
import { CountyImagePageDeleteDialogComponent } from './delete/county-image-page-delete-dialog.component';
import { CountyImagePageRoutingModule } from './route/county-image-page-routing.module';

@NgModule({
  imports: [SharedModule, CountyImagePageRoutingModule],
  declarations: [
    CountyImagePageComponent,
    CountyImagePageDetailComponent,
    CountyImagePageUpdateComponent,
    CountyImagePageDeleteDialogComponent,
  ],
  entryComponents: [CountyImagePageDeleteDialogComponent],
})
export class CountyImagePageModule {}
