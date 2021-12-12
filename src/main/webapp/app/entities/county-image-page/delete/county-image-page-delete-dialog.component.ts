import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountyImagePage } from '../county-image-page.model';
import { CountyImagePageService } from '../service/county-image-page.service';

@Component({
  templateUrl: './county-image-page-delete-dialog.component.html',
})
export class CountyImagePageDeleteDialogComponent {
  countyImagePage?: ICountyImagePage;

  constructor(protected countyImagePageService: CountyImagePageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyImagePageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
