import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RprSearchResultsComponent } from './rpr-search-results.component';

describe('RprSearchResultsComponent', () => {
  let component: RprSearchResultsComponent;
  let fixture: ComponentFixture<RprSearchResultsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RprSearchResultsComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RprSearchResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
