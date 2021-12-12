import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountiesAvaiableDetailComponent } from './counties-avaiable-detail.component';

describe('CountiesAvaiable Management Detail Component', () => {
  let comp: CountiesAvaiableDetailComponent;
  let fixture: ComponentFixture<CountiesAvaiableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountiesAvaiableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countiesAvaiable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountiesAvaiableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountiesAvaiableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countiesAvaiable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countiesAvaiable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
