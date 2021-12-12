jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICountyImage, CountyImage } from '../county-image.model';
import { CountyImageService } from '../service/county-image.service';

import { CountyImageRoutingResolveService } from './county-image-routing-resolve.service';

describe('CountyImage routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CountyImageRoutingResolveService;
  let service: CountyImageService;
  let resultCountyImage: ICountyImage | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CountyImageRoutingResolveService);
    service = TestBed.inject(CountyImageService);
    resultCountyImage = undefined;
  });

  describe('resolve', () => {
    it('should return ICountyImage returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyImage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyImage).toEqual({ id: 123 });
    });

    it('should return new ICountyImage if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyImage = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCountyImage).toEqual(new CountyImage());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CountyImage })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCountyImage = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCountyImage).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
