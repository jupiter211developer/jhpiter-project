import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RprCountySelectorComponent } from './rpr-county-selector.component';

describe('RprCountySelectorComponent', () => {
  let component: RprCountySelectorComponent;
  let fixture: ComponentFixture<RprCountySelectorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RprCountySelectorComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RprCountySelectorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
