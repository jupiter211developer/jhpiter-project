import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyImagePageDetailComponent } from './county-image-page-detail.component';

describe('CountyImagePage Management Detail Component', () => {
  let comp: CountyImagePageDetailComponent;
  let fixture: ComponentFixture<CountyImagePageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyImagePageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countyImagePage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyImagePageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyImagePageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countyImagePage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countyImagePage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
