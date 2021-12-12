import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RprSearchComponent } from './rpr-search.component';

describe('RprSearchComponent', () => {
  let component: RprSearchComponent;
  let fixture: ComponentFixture<RprSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RprSearchComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RprSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
