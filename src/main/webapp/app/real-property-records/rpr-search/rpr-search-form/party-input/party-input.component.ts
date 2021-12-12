import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

interface PartyRole {
  name: string;
}

export interface PartyInputValue {
  name: string;
  type: string;
}

@Component({
  selector: 'jhi-party-input',
  templateUrl: './party-input.component.html',
  styleUrls: ['./party-input.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: PartyInputComponent,
      multi: true,
    },
  ],
})
export class PartyInputComponent implements OnInit, ControlValueAccessor {
  partyRoles: PartyRole[];
  partyInputValue!: string;

  partyInputTypeSelectedValue!: string;

  value: PartyInputValue = this.getDefaultValue();

  constructor() {
    this.partyRoles = [{ name: 'Grantor/Grantee' }, { name: 'Grantor' }, { name: 'Grantee' }];
  }

  onChange = (): void => {};
  onTouched = (): void => {};

  ngOnInit(): void {}

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    //do nothing!
  }

  writeValue(value: any): void {
    console.log(typeof value);
    this.value = value;
  }

  getDefaultValue(): PartyInputValue {
    return { name: '', type: 'Grantor\\Grantee' };
  }

  partyInputChange(value: any): void {
    this.value.name = value.value;
  }

  partyInputTypeChange(value: any): void {
    this.value.type = value.value;
  }
}
