import { Component, OnInit } from '@angular/core';
import { CountiesAvaiable } from '../../../entities/counties-avaiable/counties-avaiable.model';
import { FormArray, FormBuilder, FormGroup } from '@angular/forms';
import { PartyInputComponent } from './party-input/party-input.component';
import { Router } from '@angular/router';
import { CountyRecord } from '../../../entities/county-record/county-record.model';

interface DateSearchParam {
  name: string;
}

@Component({
  selector: 'jhi-rpr-search-form',
  templateUrl: './rpr-search-form.component.html',
  styleUrls: ['./rpr-search-form.component.scss'],
})
export class RprSearchFormComponent implements OnInit {
  selectedCounty?: CountiesAvaiable;

  publicRecordSearchForm!: FormGroup;
  partyInput: PartyInputComponent = new PartyInputComponent();

  dateLabel = 'Exact Date';
  showSecondDate = false;
  dateSearchParams: DateSearchParam[];
  True = true;
  False = false;
  maxDate: Date = new Date();
  minDate: Date = new Date('1800/01/01');

  constructor(private fb: FormBuilder, private router: Router) {
    if (this.router.getCurrentNavigation()?.extras.state?.countyAvaiable) {
      this.selectedCounty = this.router.getCurrentNavigation()?.extras.state?.countyAvaiable;
      if (this.selectedCounty?.latest) {
        this.maxDate = this.selectedCounty.latest.toDate();
      }
      if (this.selectedCounty?.earliest) {
        this.minDate = this.selectedCounty.earliest.toDate();
      }
    } else {
      console.log('need to get county data from url');
    }

    this.dateSearchParams = [
      { name: 'Equals' },
      { name: 'Between' },
      { name: 'Before' },
      { name: 'After' },
      { name: 'Within 1 Week' },
      { name: 'Within 1 Month' },
      { name: 'Within 1 Year' },
    ];
  }

  get partyInputs(): FormArray {
    return this.publicRecordSearchForm.get('partyInputs') as FormArray;
  }

  get lglInputs(): FormArray {
    return this.publicRecordSearchForm.get('lglInputs') as FormArray;
  }

  ngOnInit(): void {
    this.initializeForm();
  }

  initializeForm(): void {
    this.publicRecordSearchForm = this.setupFormValue();
    this.addPartyInput();
  }
  setupFormValue(): FormGroup {
    return this.fb.group({
      partyInputs: this.fb.array([]),
      docNum: '',
      bk: '',
      vol: '',
      pg: '',
      docType: '',
      datePredicate: 'Equals',
      fileDateMin: '',
      fileDateMax: '',
      lglInputs: '',
      county: this.selectedCounty,
    });
  }
  getPartyControlName(val: number): string {
    return 'party' + val.toString();
  }

  getPartyTypeControlName(val: number): string {
    return 'partyType' + val.toString();
  }

  addPartyInput(): void {
    this.partyInputs.push(this.fb.control({ name: '', type: 'GRANTOR\\GRANTEE' }));
  }

  removePartyInput(index: number): void {
    this.partyInputs.removeAt(index);
    if (this.partyInputs.length === 0) {
      this.addPartyInput();
    }
  }

  addLglInput(): void {
    this.lglInputs.push(this.fb.control(''));
  }

  removeLglInput(index: number): void {
    this.lglInputs.removeAt(index);
    if (this.lglInputs.length === 0) {
      this.addLglInput();
    }
  }

  getQueryParams(): any {
    const docNum = 'docNum.contains=';
    const bk = 'bk.contains=';
    const vol = 'vol.contains=';
    const pg = 'pg.contains=';

    return {
      docNum: docNum.concat(this.publicRecordSearchForm.value.docNum),
      // bk: bk.concat(this.publicRecordSearchForm.value.bk),
      // vol: vol.concat(this.publicRecordSearchForm.value.vol),
      // pg: pg.concat(this.publicRecordSearchForm.value.pg)
    };
  }

  submit(): void {
    console.log(this.getQueryParams());
  }

  cancel(): void {
    this.router.navigateByUrl('/real-property-records');
  }

  dateSearchPredicateOnChange(e: any): void {
    if (e.value.name === 'Between') {
      this.dateLabel = 'Start Date';
      this.showSecondDate = true;
    } else {
      this.dateLabel = 'Exact Date';
      this.showSecondDate = false;
    }
    this.fb.control('datePredicate').setValue(e.value.name);
    console.log(e.value);
  }
}
