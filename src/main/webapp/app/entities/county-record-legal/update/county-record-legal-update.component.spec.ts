jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountyRecordLegalService } from '../service/county-record-legal.service';
import { ICountyRecordLegal, CountyRecordLegal } from '../county-record-legal.model';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';

import { CountyRecordLegalUpdateComponent } from './county-record-legal-update.component';

describe('CountyRecordLegal Management Update Component', () => {
  let comp: CountyRecordLegalUpdateComponent;
  let fixture: ComponentFixture<CountyRecordLegalUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyRecordLegalService: CountyRecordLegalService;
  let countyRecordService: CountyRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountyRecordLegalUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountyRecordLegalUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyRecordLegalUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyRecordLegalService = TestBed.inject(CountyRecordLegalService);
    countyRecordService = TestBed.inject(CountyRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CountyRecord query and add missing value', () => {
      const countyRecordLegal: ICountyRecordLegal = { id: 456 };
      const countyRecord: ICountyRecord = { id: 76114 };
      countyRecordLegal.countyRecord = countyRecord;

      const countyRecordCollection: ICountyRecord[] = [{ id: 51604 }];
      jest.spyOn(countyRecordService, 'query').mockReturnValue(of(new HttpResponse({ body: countyRecordCollection })));
      const additionalCountyRecords = [countyRecord];
      const expectedCollection: ICountyRecord[] = [...additionalCountyRecords, ...countyRecordCollection];
      jest.spyOn(countyRecordService, 'addCountyRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countyRecordLegal });
      comp.ngOnInit();

      expect(countyRecordService.query).toHaveBeenCalled();
      expect(countyRecordService.addCountyRecordToCollectionIfMissing).toHaveBeenCalledWith(
        countyRecordCollection,
        ...additionalCountyRecords
      );
      expect(comp.countyRecordsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const countyRecordLegal: ICountyRecordLegal = { id: 456 };
      const countyRecord: ICountyRecord = { id: 63312 };
      countyRecordLegal.countyRecord = countyRecord;

      activatedRoute.data = of({ countyRecordLegal });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countyRecordLegal));
      expect(comp.countyRecordsSharedCollection).toContain(countyRecord);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecordLegal>>();
      const countyRecordLegal = { id: 123 };
      jest.spyOn(countyRecordLegalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecordLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyRecordLegal }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyRecordLegalService.update).toHaveBeenCalledWith(countyRecordLegal);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecordLegal>>();
      const countyRecordLegal = new CountyRecordLegal();
      jest.spyOn(countyRecordLegalService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecordLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyRecordLegal }));
      saveSubject.complete();

      // THEN
      expect(countyRecordLegalService.create).toHaveBeenCalledWith(countyRecordLegal);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecordLegal>>();
      const countyRecordLegal = { id: 123 };
      jest.spyOn(countyRecordLegalService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecordLegal });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyRecordLegalService.update).toHaveBeenCalledWith(countyRecordLegal);
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
