jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountyImagePage, CountyImagePage } from '../county-image-page.model';
import { CountyImagePageService } from '../service/county-image-page.service';

import { CountyImagePageRoutingResolveService } from './county-image-page-routing-resolve.service';

describe('CountyImagePage routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountyImagePageRoutingResolveService;
  let service: CountyImagePageService;
  let resultCountyImagePage: ICountyImagePage | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountyImagePageRoutingResolveService);
    service = TestBed.inject(CountyImagePageService);
    resultCountyImagePage = undefined;
  });

  describe('resolve', () => {
    it('should return ICountyImagePage returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyImagePage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyImagePage).toEqual({ id: 123 });
    });

    it('should return new ICountyImagePage if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyImagePage = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountyImagePage).toEqual(new CountyImagePage());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountyImagePage })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyImagePage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyImagePage).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
