import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountyImage, CountyImage } from '../county-image.model';
import { CountyImageService } from '../service/county-image.service';

@Injectable({ providedIn: 'root' })
export class CountyImageRoutingResolveService implements Resolve<ICountyImage> {
  constructor(protected service: CountyImageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountyImage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((countyImage: HttpResponse<CountyImage>) => {
          if (countyImage.body) {
            return of(countyImage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountyImage());
  }
}
