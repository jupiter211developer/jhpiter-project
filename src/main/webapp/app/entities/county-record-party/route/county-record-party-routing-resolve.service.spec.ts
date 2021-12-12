jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountyRecordParty, CountyRecordParty } from '../county-record-party.model';
import { CountyRecordPartyService } from '../service/county-record-party.service';

import { CountyRecordPartyRoutingResolveService } from './county-record-party-routing-resolve.service';

describe('CountyRecordParty routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountyRecordPartyRoutingResolveService;
  let service: CountyRecordPartyService;
  let resultCountyRecordParty: ICountyRecordParty | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountyRecordPartyRoutingResolveService);
    service = TestBed.inject(CountyRecordPartyService);
    resultCountyRecordParty = undefined;
  });

  describe('resolve', () => {
    it('should return ICountyRecordParty returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecordParty = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyRecordParty).toEqual({ id: 123 });
    });

    it('should return new ICountyRecordParty if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecordParty = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountyRecordParty).toEqual(new CountyRecordParty());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountyRecordParty })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyRecordParty = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyRecordParty).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
