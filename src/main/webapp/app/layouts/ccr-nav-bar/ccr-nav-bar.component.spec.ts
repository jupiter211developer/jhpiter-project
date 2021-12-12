import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CcrNavBarComponent } from './ccr-nav-bar.component';

describe('CcrNavBarComponent', () => {
  let component: CcrNavBarComponent;
  let fixture: ComponentFixture<CcrNavBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CcrNavBarComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(CcrNavBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
