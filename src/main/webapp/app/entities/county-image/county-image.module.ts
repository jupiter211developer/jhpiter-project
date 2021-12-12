import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyImageComponent } from './list/county-image.component';
import { CountyImageDetailComponent } from './detail/county-image-detail.component';
import { CountyImageUpdateComponent } from './update/county-image-update.component';
import { CountyImageDeleteDialogComponent } from './delete/county-image-delete-dialog.component';
import { CountyImageRoutingModule } from './route/county-image-routing.module';

@NgModule({
  imports: [SharedModule, CountyImageRoutingModule],
  declarations: [CountyImageComponent, CountyImageDetailComponent, CountyImageUpdateComponent, CountyImageDeleteDialogComponent],
  entryComponents: [CountyImageDeleteDialogComponent],
})
export class CountyImageModule {}
