jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountyImagePageService } from '../service/county-image-page.service';
import { ICountyImagePage, CountyImagePage } from '../county-image-page.model';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';
import { ICountyImage } from 'app/entities/county-image/county-image.model';
import { CountyImageService } from 'app/entities/county-image/service/county-image.service';

import { CountyImagePageUpdateComponent } from './county-image-page-update.component';

describe('CountyImagePage Management Update Component', () => {
  let comp: CountyImagePageUpdateComponent;
  let fixture: ComponentFixture<CountyImagePageUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyImagePageService: CountyImagePageService;
  let countyRecordService: CountyRecordService;
  let countyImageService: CountyImageService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountyImagePageUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountyImagePageUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyImagePageUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyImagePageService = TestBed.inject(CountyImagePageService);
    countyRecordService = TestBed.inject(CountyRecordService);
    countyImageService = TestBed.inject(CountyImageService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call CountyRecord query and add missing value', () => {
      const countyImagePage: ICountyImagePage = { id: 456 };
      const countyRecord: ICountyRecord = { id: 1671 };
      countyImagePage.countyRecord = countyRecord;

      const countyRecordCollection: ICountyRecord[] = [{ id: 24102 }];
      jest.spyOn(countyRecordService, 'query').mockReturnValue(of(new HttpResponse({ body: countyRecordCollection })));
      const additionalCountyRecords = [countyRecord];
      const expectedCollection: ICountyRecord[] = [...additionalCountyRecords, ...countyRecordCollection];
      jest.spyOn(countyRecordService, 'addCountyRecordToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countyImagePage });
      comp.ngOnInit();

      expect(countyRecordService.query).toHaveBeenCalled();
      expect(countyRecordService.addCountyRecordToCollectionIfMissing).toHaveBeenCalledWith(
        countyRecordCollection,
        ...additionalCountyRecords
      );
      expect(comp.countyRecordsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call CountyImage query and add missing value', () => {
      const countyImagePage: ICountyImagePage = { id: 456 };
      const countyImage: ICountyImage = { id: 65313 };
      countyImagePage.countyImage = countyImage;

      const countyImageCollection: ICountyImage[] = [{ id: 67348 }];
      jest.spyOn(countyImageService, 'query').mockReturnValue(of(new HttpResponse({ body: countyImageCollection })));
      const additionalCountyImages = [countyImage];
      const expectedCollection: ICountyImage[] = [...additionalCountyImages, ...countyImageCollection];
      jest.spyOn(countyImageService, 'addCountyImageToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countyImagePage });
      comp.ngOnInit();

      expect(countyImageService.query).toHaveBeenCalled();
      expect(countyImageService.addCountyImageToCollectionIfMissing).toHaveBeenCalledWith(countyImageCollection, ...additionalCountyImages);
      expect(comp.countyImagesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const countyImagePage: ICountyImagePage = { id: 456 };
      const countyRecord: ICountyRecord = { id: 99035 };
      countyImagePage.countyRecord = countyRecord;
      const countyImage: ICountyImage = { id: 34481 };
      countyImagePage.countyImage = countyImage;

      activatedRoute.data = of({ countyImagePage });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countyImagePage));
      expect(comp.countyRecordsSharedCollection).toContain(countyRecord);
      expect(comp.countyImagesSharedCollection).toContain(countyImage);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyImagePage>>();
      const countyImagePage = { id: 123 };
      jest.spyOn(countyImagePageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyImagePage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyImagePage }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyImagePageService.update).toHaveBeenCalledWith(countyImagePage);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyImagePage>>();
      const countyImagePage = new CountyImagePage();
      jest.spyOn(countyImagePageService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyImagePage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countyImagePage }));
      saveSubject.complete();

      // THEN
      expect(countyImagePageService.create).toHaveBeenCalledWith(countyImagePage);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountyImagePage>>();
      const countyImagePage = { id: 123 };
      jest.spyOn(countyImagePageService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countyImagePage });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyImagePageService.update).toHaveBeenCalledWith(countyImagePage);
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

    describe('trackCountyImageById', () => {
      it('Should return tracked CountyImage primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackCountyImageById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
