jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountiesAvaiable, CountiesAvaiable } from '../counties-avaiable.model';
import { CountiesAvaiableService } from '../service/counties-avaiable.service';

import { CountiesAvaiableRoutingResolveService } from './counties-avaiable-routing-resolve.service';

describe('CountiesAvaiable routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountiesAvaiableRoutingResolveService;
  let service: CountiesAvaiableService;
  let resultCountiesAvaiable: ICountiesAvaiable | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountiesAvaiableRoutingResolveService);
    service = TestBed.inject(CountiesAvaiableService);
    resultCountiesAvaiable = undefined;
  });

  describe('resolve', () => {
    it('should return ICountiesAvaiable returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountiesAvaiable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountiesAvaiable).toEqual({ id: 123 });
    });

    it('should return new ICountiesAvaiable if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountiesAvaiable = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountiesAvaiable).toEqual(new CountiesAvaiable());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountiesAvaiable })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountiesAvaiable = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountiesAvaiable).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
