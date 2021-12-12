jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountyRecordLegal, CountyRecordLegal } from '../county-record-legal.model';
import { CountyRecordLegalService } from '../service/county-record-legal.service';

import { CountyRecordLegalRoutingResolveService } from './county-record-legal-routing-resolve.service';

describe('CountyRecordLegal routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountyRecordLegalRoutingResolveService;
  let service: CountyRecordLegalService;
  let resultCountyRecordLegal: ICountyRecordLegal | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountyRecordLegalRoutingResolveService);
    service = TestBed.inject(CountyRecordLegalService);
    resultCountyRecordLegal = undefined;
  });

  describe('resolve', () => {
    it('should return ICountyRecordLegal returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecordLegal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyRecordLegal).toEqual({ id: 123 });
    });

    it('should return new ICountyRecordLegal if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecordLegal = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountyRecordLegal).toEqual(new CountyRecordLegal());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountyRecordLegal })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecordLegal = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyRecordLegal).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
