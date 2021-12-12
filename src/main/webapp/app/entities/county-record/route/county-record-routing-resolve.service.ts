import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountyRecord, CountyRecord } from '../county-record.model';
import { CountyRecordService } from '../service/county-record.service';

@Injectable({ providedIn: 'root' })
export class CountyRecordRoutingResolveService implements Resolve<ICountyRecord> {
  constructor(protected service: CountyRecordService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountyRecord> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((countyRecord: HttpResponse<CountyRecord>) => {
          if (countyRecord.body) {
            return of(countyRecord.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountyRecord());
  }
}
