import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PartyInputComponent } from './party-input.component';

describe('PartyInputComponentComponent', () => {
  let component: PartyInputComponent;
  let fixture: ComponentFixture<PartyInputComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PartyInputComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PartyInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
