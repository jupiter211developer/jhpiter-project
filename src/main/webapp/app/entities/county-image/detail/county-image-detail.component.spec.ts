import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyImageDetailComponent } from './county-image-detail.component';

describe('CountyImage Management Detail Component', () => {
  let comp: CountyImageDetailComponent;
  let fixture: ComponentFixture<CountyImageDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyImageDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countyImage: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyImageDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyImageDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countyImage on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countyImage).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
