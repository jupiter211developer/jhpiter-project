jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CountyService } from '../service/county.service';
import { ICounty, County } from '../county.model';
import { IState } from 'app/entities/state/state.model';
import { StateService } from 'app/entities/state/service/state.service';

import { CountyUpdateComponent } from './county-update.component';

describe('County Management Update Component', () => {
  let comp: CountyUpdateComponent;
  let fixture: ComponentFixture<CountyUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let countyService: CountyService;
  let stateService: StateService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [CountyUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(CountyUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CountyUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    countyService = TestBed.inject(CountyService);
    stateService = TestBed.inject(StateService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call State query and add missing value', () => {
      const county: ICounty = { id: 456 };
      const state: IState = { id: 82866 };
      county.state = state;

      const stateCollection: IState[] = [{ id: 11745 }];
      jest.spyOn(stateService, 'query').mockReturnValue(of(new HttpResponse({ body: stateCollection })));
      const additionalStates = [state];
      const expectedCollection: IState[] = [...additionalStates, ...stateCollection];
      jest.spyOn(stateService, 'addStateToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ county });
      comp.ngOnInit();

      expect(stateService.query).toHaveBeenCalled();
      expect(stateService.addStateToCollectionIfMissing).toHaveBeenCalledWith(stateCollection, ...additionalStates);
      expect(comp.statesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const county: ICounty = { id: 456 };
      const state: IState = { id: 98108 };
      county.state = state;

      activatedRoute.data = of({ county });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(county));
      expect(comp.statesSharedCollection).toContain(state);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<County>>();
      const county = { id: 123 };
      jest.spyOn(countyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ county });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: county }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(countyService.update).toHaveBeenCalledWith(county);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<County>>();
      const county = new County();
      jest.spyOn(countyService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ county });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: county }));
      saveSubject.complete();

      // THEN
      expect(countyService.create).toHaveBeenCalledWith(county);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<County>>();
      const county = { id: 123 };
      jest.spyOn(countyService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ county });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(countyService.update).toHaveBeenCalledWith(county);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackStateById', () => {
      it('Should return tracked State primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStateById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
