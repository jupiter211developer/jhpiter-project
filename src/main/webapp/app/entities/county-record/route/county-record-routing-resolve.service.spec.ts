jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountyRecord, CountyRecord } from '../county-record.model';
import { CountyRecordService } from '../service/county-record.service';

import { CountyRecordRoutingResolveService } from './county-record-routing-resolve.service';

describe('CountyRecord routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountyRecordRoutingResolveService;
  let service: CountyRecordService;
  let resultCountyRecord: ICountyRecord | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountyRecordRoutingResolveService);
    service = TestBed.inject(CountyRecordService);
    resultCountyRecord = undefined;
  });

  describe('resolve', () => {
    it('should return ICountyRecord returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyRecord).toEqual({ id: 123 });
    });

    it('should return new ICountyRecord if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecord = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountyRecord).toEqual(new CountyRecord());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountyRecord })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecord = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyRecord).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
