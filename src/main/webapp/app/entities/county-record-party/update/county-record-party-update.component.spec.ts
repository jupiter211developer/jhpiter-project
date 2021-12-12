jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountyRecordPartyService } from '../service/county-record-party.service';
import { ICountyRecordParty, CountyRecordParty } from '../county-record-party.model';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';

import { CountyRecordPartyUpdateComponent } from './county-record-party-update.component';

describe('CountyRecordParty Management Update Component', () => {
  let comp: CountyRecordPartyUpdateComponent;
  let fixture: ComponentFixture<CountyRecordPartyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyRecordPartyService: CountyRecordPartyService;
  let countyRecordService: CountyRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountyRecordPartyUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountyRecordPartyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyRecordPartyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyRecordPartyService = TestBed.inject(CountyRecordPartyService);
    countyRecordService = TestBed.inject(CountyRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CountyRecord query and add missing value', () => {
      const countyRecordParty: ICountyRecordParty = { id: 456 };
      const countyRecord: ICountyRecord = { id: 32162 };
      countyRecordParty.countyRecord = countyRecord;

      const countyRecordCollection: ICountyRecord[] = [{ id: 8688 }];
      jest.spyOn(countyRecordService, 'query').mockReturnValue(of(new HttpResponse({ body: countyRecordCollection })));
      const additionalCountyRecords = [countyRecord];
      const expectedCollection: ICountyRecord[] = [...additionalCountyRecords, ...countyRecordCollection];
      jest.spyOn(countyRecordService, 'addCountyRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countyRecordParty });
      comp.ngOnInit();

      expect(countyRecordService.query).toHaveBeenCalled();
      expect(countyRecordService.addCountyRecordToCollectionIfMissing).toHaveBeenCalledWith(
        countyRecordCollection,
        ...additionalCountyRecords
      );
      expect(comp.countyRecordsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const countyRecordParty: ICountyRecordParty = { id: 456 };
      const countyRecord: ICountyRecord = { id: 48658 };
      countyRecordParty.countyRecord = countyRecord;

      activatedRoute.data = of({ countyRecordParty });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countyRecordParty));
      expect(comp.countyRecordsSharedCollection).toContain(countyRecord);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecordParty>>();
      const countyRecordParty = { id: 123 };
      jest.spyOn(countyRecordPartyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecordParty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyRecordParty }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyRecordPartyService.update).toHaveBeenCalledWith(countyRecordParty);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecordParty>>();
      const countyRecordParty = new CountyRecordParty();
      jest.spyOn(countyRecordPartyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecordParty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyRecordParty }));
      saveSubject.complete();

      // THEN
      expect(countyRecordPartyService.create).toHaveBeenCalledWith(countyRecordParty);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecordParty>>();
      const countyRecordParty = { id: 123 };
      jest.spyOn(countyRecordPartyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecordParty });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyRecordPartyService.update).toHaveBeenCalledWith(countyRecordParty);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCountyRecordById', () => {
      it('Should return tracked CountyRecord primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountyRecordById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
