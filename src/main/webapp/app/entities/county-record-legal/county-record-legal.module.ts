import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyRecordLegalComponent } from './list/county-record-legal.component';
import { CountyRecordLegalDetailComponent } from './detail/county-record-legal-detail.component';
import { CountyRecordLegalUpdateComponent } from './update/county-record-legal-update.component';
import { CountyRecordLegalDeleteDialogComponent } from './delete/county-record-legal-delete-dialog.component';
import { CountyRecordLegalRoutingModule } from './route/county-record-legal-routing.module';

@NgModule({
  imports: [SharedModule, CountyRecordLegalRoutingModule],
  declarations: [
    CountyRecordLegalComponent,
    CountyRecordLegalDetailComponent,
    CountyRecordLegalUpdateComponent,
    CountyRecordLegalDeleteDialogComponent,
  ],
  entryComponents: [CountyRecordLegalDeleteDialogComponent],
})
export class CountyRecordLegalModule {}
