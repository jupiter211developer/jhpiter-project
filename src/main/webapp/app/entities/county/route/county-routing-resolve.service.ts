import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICounty, County } from '../county.model';
import { CountyService } from '../service/county.service';

@Injectable({ providedIn: 'root' })
export class CountyRoutingResolveService implements Resolve<ICounty> {
  constructor(protected service: CountyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICounty> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((county: HttpResponse<County>) => {
          if (county.body) {
            return of(county.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new County());
  }
}
