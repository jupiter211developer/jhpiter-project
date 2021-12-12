import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountyRecordPartyComponent } from './list/county-record-party.component';
import { CountyRecordPartyDetailComponent } from './detail/county-record-party-detail.component';
import { CountyRecordPartyUpdateComponent } from './update/county-record-party-update.component';
import { CountyRecordPartyDeleteDialogComponent } from './delete/county-record-party-delete-dialog.component';
import { CountyRecordPartyRoutingModule } from './route/county-record-party-routing.module';

@NgModule({
  imports: [SharedModule, CountyRecordPartyRoutingModule],
  declarations: [
    CountyRecordPartyComponent,
    CountyRecordPartyDetailComponent,
    CountyRecordPartyUpdateComponent,
    CountyRecordPartyDeleteDialogComponent,
  ],
  entryComponents: [CountyRecordPartyDeleteDialogComponent],
})
export class CountyRecordPartyModule {}
