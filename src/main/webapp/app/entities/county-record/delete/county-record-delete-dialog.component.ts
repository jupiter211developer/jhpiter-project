import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountyRecord } from '../county-record.model';
import { CountyRecordService } from '../service/county-record.service';

@Component({
  templateUrl: './county-record-delete-dialog.component.html',
})
export class CountyRecordDeleteDialogComponent {
  countyRecord?: ICountyRecord;

  constructor(protected countyRecordService: CountyRecordService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyRecordService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
