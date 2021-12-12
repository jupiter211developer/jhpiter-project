import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountyRecordParty } from '../county-record-party.model';
import { CountyRecordPartyService } from '../service/county-record-party.service';

@Component({
  templateUrl: './county-record-party-delete-dialog.component.html',
})
export class CountyRecordPartyDeleteDialogComponent {
  countyRecordParty?: ICountyRecordParty;

  constructor(protected countyRecordPartyService: CountyRecordPartyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyRecordPartyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
