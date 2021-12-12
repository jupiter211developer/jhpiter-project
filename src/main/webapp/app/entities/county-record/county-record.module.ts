import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyRecordComponent } from './list/county-record.component';
import { CountyRecordDetailComponent } from './detail/county-record-detail.component';
import { CountyRecordUpdateComponent } from './update/county-record-update.component';
import { CountyRecordDeleteDialogComponent } from './delete/county-record-delete-dialog.component';
import { CountyRecordRoutingModule } from './route/county-record-routing.module';

@NgModule({
  imports: [SharedModule, CountyRecordRoutingModule],
  declarations: [CountyRecordComponent, CountyRecordDetailComponent, CountyRecordUpdateComponent, CountyRecordDeleteDialogComponent],
  entryComponents: [CountyRecordDeleteDialogComponent],
})
export class CountyRecordModule {}
