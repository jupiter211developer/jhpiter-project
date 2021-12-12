import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyDetailComponent } from './county-detail.component';

describe('County Management Detail Component', () => {
  let comp: CountyDetailComponent;
  let fixture: ComponentFixture<CountyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ county: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load county on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.county).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
