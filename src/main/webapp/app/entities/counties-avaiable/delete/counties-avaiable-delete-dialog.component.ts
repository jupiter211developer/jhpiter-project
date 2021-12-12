import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountiesAvaiable } from '../counties-avaiable.model';
import { CountiesAvaiableService } from '../service/counties-avaiable.service';

@Component({
  templateUrl: './counties-avaiable-delete-dialog.component.html',
})
export class CountiesAvaiableDeleteDialogComponent {
  countiesAvaiable?: ICountiesAvaiable;

  constructor(protected countiesAvaiableService: CountiesAvaiableService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countiesAvaiableService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
