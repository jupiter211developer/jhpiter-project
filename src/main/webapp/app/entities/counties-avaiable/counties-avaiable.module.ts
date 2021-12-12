import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CountiesAvaiableComponent } from './list/counties-avaiable.component';
import { CountiesAvaiableDetailComponent } from './detail/counties-avaiable-detail.component';
import { CountiesAvaiableUpdateComponent } from './update/counties-avaiable-update.component';
import { CountiesAvaiableDeleteDialogComponent } from './delete/counties-avaiable-delete-dialog.component';
import { CountiesAvaiableRoutingModule } from './route/counties-avaiable-routing.module';

@NgModule({
  imports: [SharedModule, CountiesAvaiableRoutingModule],
  declarations: [
    CountiesAvaiableComponent,
    CountiesAvaiableDetailComponent,
    CountiesAvaiableUpdateComponent,
    CountiesAvaiableDeleteDialogComponent,
  ],
  entryComponents: [CountiesAvaiableDeleteDialogComponent],
})
export class CountiesAvaiableModule {}
