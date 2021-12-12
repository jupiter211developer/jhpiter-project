import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { RealPropertyRecordsRoutingModule } from './real-property-records-routing.module';
import { RealPropertyRecordsComponent } from './real-property-records.component';
import { RprSearchComponent } from './rpr-search/rpr-search.component';
import { RprCountySelectorComponent } from './rpr-county-selector/rpr-county-selector.component';
import { InputTextModule } from 'primeng/inputtext';
import { ButtonModule } from 'primeng/button';
import { DropdownModule } from 'primeng/dropdown';
import { CalendarModule } from 'primeng/calendar';
import { PartyInputComponent } from './rpr-search/rpr-search-form/party-input/party-input.component';
import { RprSearchFormComponent } from './rpr-search/rpr-search-form/rpr-search-form.component';
import { RprSearchResultsComponent } from './rpr-search/rpr-search-results/rpr-search-results.component';

@NgModule({
  imports: [SharedModule, RealPropertyRecordsRoutingModule, InputTextModule, ButtonModule, DropdownModule, CalendarModule],
  declarations: [
    RealPropertyRecordsComponent,
    RprSearchComponent,
    RprCountySelectorComponent,
    PartyInputComponent,
    RprSearchFormComponent,
    RprSearchResultsComponent,
  ],
})
export class RealPropertyRecordsModule {}
