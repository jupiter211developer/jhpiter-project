import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RprSearchFormComponent } from './rpr-search-form.component';

describe('RprSearchFormComponent', () => {
  let component: RprSearchFormComponent;
  let fixture: ComponentFixture<RprSearchFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RprSearchFormComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RprSearchFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
