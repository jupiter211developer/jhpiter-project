import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountyImagePage, CountyImagePage } from '../county-image-page.model';
import { CountyImagePageService } from '../service/county-image-page.service';

@Injectable({ providedIn: 'root' })
export class CountyImagePageRoutingResolveService implements Resolve<ICountyImagePage> {
  constructor(protected service: CountyImagePageService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountyImagePage> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((countyImagePage: HttpResponse<CountyImagePage>) => {
          if (countyImagePage.body) {
            return of(countyImagePage.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountyImagePage());
  }
}
