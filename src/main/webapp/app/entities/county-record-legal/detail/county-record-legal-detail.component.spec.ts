import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyRecordLegalDetailComponent } from './county-record-legal-detail.component';

describe('CountyRecordLegal Management Detail Component', () => {
  let comp: CountyRecordLegalDetailComponent;
  let fixture: ComponentFixture<CountyRecordLegalDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyRecordLegalDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countyRecordLegal: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyRecordLegalDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyRecordLegalDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countyRecordLegal on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countyRecordLegal).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
