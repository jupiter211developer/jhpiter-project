jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountyRecordService } from '../service/county-record.service';
import { ICountyRecord, CountyRecord } from '../county-record.model';
import { ICounty } from 'app/entities/county/county.model';
import { CountyService } from 'app/entities/county/service/county.service';

import { CountyRecordUpdateComponent } from './county-record-update.component';

describe('CountyRecord Management Update Component', () => {
  let comp: CountyRecordUpdateComponent;
  let fixture: ComponentFixture<CountyRecordUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyRecordService: CountyRecordService;
  let countyService: CountyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountyRecordUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountyRecordUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyRecordUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyRecordService = TestBed.inject(CountyRecordService);
    countyService = TestBed.inject(CountyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call County query and add missing value', () => {
      const countyRecord: ICountyRecord = { id: 456 };
      const county: ICounty = { id: 74113 };
      countyRecord.county = county;

      const countyCollection: ICounty[] = [{ id: 13591 }];
      jest.spyOn(countyService, 'query').mockReturnValue(of(new HttpResponse({ body: countyCollection })));
      const additionalCounties = [county];
      const expectedCollection: ICounty[] = [...additionalCounties, ...countyCollection];
      jest.spyOn(countyService, 'addCountyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countyRecord });
      comp.ngOnInit();

      expect(countyService.query).toHaveBeenCalled();
      expect(countyService.addCountyToCollectionIfMissing).toHaveBeenCalledWith(countyCollection, ...additionalCounties);
      expect(comp.countiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const countyRecord: ICountyRecord = { id: 456 };
      const county: ICounty = { id: 69853 };
      countyRecord.county = county;

      activatedRoute.data = of({ countyRecord });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countyRecord));
      expect(comp.countiesSharedCollection).toContain(county);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecord>>();
      const countyRecord = { id: 123 };
      jest.spyOn(countyRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyRecord }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyRecordService.update).toHaveBeenCalledWith(countyRecord);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecord>>();
      const countyRecord = new CountyRecord();
      jest.spyOn(countyRecordService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyRecord }));
      saveSubject.complete();

      // THEN
      expect(countyRecordService.create).toHaveBeenCalledWith(countyRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyRecord>>();
      const countyRecord = { id: 123 };
      jest.spyOn(countyRecordService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyRecord });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyRecordService.update).toHaveBeenCalledWith(countyRecord);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackCountyById', () => {
      it('Should return tracked County primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountyById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
