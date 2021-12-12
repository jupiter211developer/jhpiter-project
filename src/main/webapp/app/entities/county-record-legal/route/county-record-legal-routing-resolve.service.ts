import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountyRecordLegal, CountyRecordLegal } from '../county-record-legal.model';
import { CountyRecordLegalService } from '../service/county-record-legal.service';

@Injectable({ providedIn: 'root' })
export class CountyRecordLegalRoutingResolveService implements Resolve<ICountyRecordLegal> {
  constructor(protected service: CountyRecordLegalService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountyRecordLegal> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((countyRecordLegal: HttpResponse<CountyRecordLegal>) => {
          if (countyRecordLegal.body) {
            return of(countyRecordLegal.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountyRecordLegal());
  }
}
