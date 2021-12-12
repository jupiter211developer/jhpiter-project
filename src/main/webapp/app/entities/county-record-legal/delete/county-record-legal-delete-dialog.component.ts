import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountyRecordLegal } from '../county-record-legal.model';
import { CountyRecordLegalService } from '../service/county-record-legal.service';

@Component({
  templateUrl: './county-record-legal-delete-dialog.component.html',
})
export class CountyRecordLegalDeleteDialogComponent {
  countyRecordLegal?: ICountyRecordLegal;

  constructor(protected countyRecordLegalService: CountyRecordLegalService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyRecordLegalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
