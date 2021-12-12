import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RealPropertyRecordsComponent } from './real-property-records.component';

describe('RealPropertyRecordsComponent', () => {
  let component: RealPropertyRecordsComponent;
  let fixture: ComponentFixture<RealPropertyRecordsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RealPropertyRecordsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RealPropertyRecordsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
