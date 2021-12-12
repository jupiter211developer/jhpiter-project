import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CountyRecordPartyDetailComponent } from './county-record-party-detail.component';

describe('CountyRecordParty Management Detail Component', () => {
  let comp: CountyRecordPartyDetailComponent;
  let fixture: ComponentFixture<CountyRecordPartyDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CountyRecordPartyDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ countyRecordParty: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CountyRecordPartyDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CountyRecordPartyDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load countyRecordParty on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.countyRecordParty).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
