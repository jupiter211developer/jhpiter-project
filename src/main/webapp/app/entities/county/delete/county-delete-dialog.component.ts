import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICounty } from '../county.model';
import { CountyService } from '../service/county.service';

@Component({
  templateUrl: './county-delete-dialog.component.html',
})
export class CountyDeleteDialogComponent {
  county?: ICounty;

  constructor(protected countyService: CountyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
