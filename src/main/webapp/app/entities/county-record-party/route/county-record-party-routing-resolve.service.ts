import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountyRecordParty, CountyRecordParty } from '../county-record-party.model';
import { CountyRecordPartyService } from '../service/county-record-party.service';

@Injectable({ providedIn: 'root' })
export class CountyRecordPartyRoutingResolveService implements Resolve<ICountyRecordParty> {
  constructor(protected service: CountyRecordPartyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountyRecordParty> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((countyRecordParty: HttpResponse<CountyRecordParty>) => {
          if (countyRecordParty.body) {
            return of(countyRecordParty.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountyRecordParty());
  }
}
