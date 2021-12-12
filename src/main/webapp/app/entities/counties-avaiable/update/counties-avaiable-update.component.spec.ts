jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountiesAvaiableService } from '../service/counties-avaiable.service';
import { ICountiesAvaiable, CountiesAvaiable } from '../counties-avaiable.model';
import { ICounty } from 'app/entities/county/county.model';
import { CountyService } from 'app/entities/county/service/county.service';

import { CountiesAvaiableUpdateComponent } from './counties-avaiable-update.component';

describe('CountiesAvaiable Management Update Component', () => {
  let comp: CountiesAvaiableUpdateComponent;
  let fixture: ComponentFixture<CountiesAvaiableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countiesAvaiableService: CountiesAvaiableService;
  let countyService: CountyService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountiesAvaiableUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountiesAvaiableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountiesAvaiableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countiesAvaiableService = TestBed.inject(CountiesAvaiableService);
    countyService = TestBed.inject(CountyService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call county query and add missing value', () => {
      const countiesAvaiable: ICountiesAvaiable = { id: 456 };
      const county: ICounty = { id: 64199 };
      countiesAvaiable.county = county;

      const countyCollection: ICounty[] = [{ id: 7698 }];
      jest.spyOn(countyService, 'query').mockReturnValue(of(new HttpResponse({ body: countyCollection })));
      const expectedCollection: ICounty[] = [county, ...countyCollection];
      jest.spyOn(countyService, 'addCountyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ countiesAvaiable });
      comp.ngOnInit();

      expect(countyService.query).toHaveBeenCalled();
      expect(countyService.addCountyToCollectionIfMissing).toHaveBeenCalledWith(countyCollection, county);
      expect(comp.countiesCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const countiesAvaiable: ICountiesAvaiable = { id: 456 };
      const county: ICounty = { id: 19156 };
      countiesAvaiable.county = county;

      activatedRoute.data = of({ countiesAvaiable });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(countiesAvaiable));
      expect(comp.countiesCollection).toContain(county);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountiesAvaiable>>();
      const countiesAvaiable = { id: 123 };
      jest.spyOn(countiesAvaiableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countiesAvaiable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countiesAvaiable }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countiesAvaiableService.update).toHaveBeenCalledWith(countiesAvaiable);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountiesAvaiable>>();
      const countiesAvaiable = new CountiesAvaiable();
      jest.spyOn(countiesAvaiableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countiesAvaiable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: countiesAvaiable }));
      saveSubject.complete();

      // THEN
      expect(countiesAvaiableService.create).toHaveBeenCalledWith(countiesAvaiable);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CountiesAvaiable>>();
      const countiesAvaiable = { id: 123 };
      jest.spyOn(countiesAvaiableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ countiesAvaiable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countiesAvaiableService.update).toHaveBeenCalledWith(countiesAvaiable);
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
