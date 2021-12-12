import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyComponent } from './list/county.component';
import { CountyDetailComponent } from './detail/county-detail.component';
import { CountyUpdateComponent } from './update/county-update.component';
import { CountyDeleteDialogComponent } from './delete/county-delete-dialog.component';
import { CountyRoutingModule } from './route/county-routing.module';

@NgModule({
  imports: [SharedModule, CountyRoutingModule],
  declarations: [CountyComponent, CountyDetailComponent, CountyUpdateComponent, CountyDeleteDialogComponent],
  entryComponents: [CountyDeleteDialogComponent],
})
export class CountyModule {}
