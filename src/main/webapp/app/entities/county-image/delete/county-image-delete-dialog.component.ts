import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICountyImage } from '../county-image.model';
import { CountyImageService } from '../service/county-image.service';

@Component({
  templateUrl: './county-image-delete-dialog.component.html',
})
export class CountyImageDeleteDialogComponent {
  countyImage?: ICountyImage;

  constructor(protected countyImageService: CountyImageService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.countyImageService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
