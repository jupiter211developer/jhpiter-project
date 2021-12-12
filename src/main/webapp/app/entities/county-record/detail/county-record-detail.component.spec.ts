import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyRecordDetailComponent } from './county-record-detail.component';

describe('CountyRecord Management Detail Component', () => {
  let comp: CountyRecordDetailComponent;
  let fixture: ComponentFixture<CountyRecordDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyRecordDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countyRecord: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyRecordDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyRecordDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countyRecord on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countyRecord).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
