jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountyImageService } from '../service/county-image.service';
import { ICountyImage, CountyImage } from '../county-image.model';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';

import { CountyImageUpdateComponent } from './county-image-update.component';

describe('CountyImage Management Update Component', () => {
  let comp: CountyImageUpdateComponent;
  let fixture: ComponentFixture<CountyImageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyImageService: CountyImageService;
  let countyRecordService: CountyRecordService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountyImageUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountyImageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyImageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyImageService = TestBed.inject(CountyImageService);
    countyRecordService = TestBed.inject(CountyRecordService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call countyRecord query and add missing value', () => {
      const countyImage: ICountyImage = { id: 456 };
      const countyRecord: ICountyRecord = { id: 48376 };
      countyImage.countyRecord = countyRecord;

      const countyRecordCollection: ICountyRecord[] = [{ id: 80716 }];
      jest.spyOn(countyRecordService, 'query').mockReturnValue(of(new HttpResponse({ body: countyRecordCollection })));
      const expectedCollection: ICountyRecord[] = [countyRecord, ...countyRecordCollection];
      jest.spyOn(countyRecordService, 'addCountyRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countyImage });
      comp.ngOnInit();

      expect(countyRecordService.query).toHaveBeenCalled();
      expect(countyRecordService.addCountyRecordToCollectionIfMissing).toHaveBeenCalledWith(countyRecordCollection, countyRecord);
      expect(comp.countyRecordsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const countyImage: ICountyImage = { id: 456 };
      const countyRecord: ICountyRecord = { id: 16794 };
      countyImage.countyRecord = countyRecord;

      activatedRoute.data = of({ countyImage });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countyImage));
      expect(comp.countyRecordsCollection).toContain(countyRecord);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyImage>>();
      const countyImage = { id: 123 };
      jest.spyOn(countyImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyImage }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyImageService.update).toHaveBeenCalledWith(countyImage);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyImage>>();
      const countyImage = new CountyImage();
      jest.spyOn(countyImageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyImage }));
      saveSubject.complete();

      // THEN
      expect(countyImageService.create).toHaveBeenCalledWith(countyImage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyImage>>();
      const countyImage = { id: 123 };
      jest.spyOn(countyImageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyImage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyImageService.update).toHaveBeenCalledWith(countyImage);
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
