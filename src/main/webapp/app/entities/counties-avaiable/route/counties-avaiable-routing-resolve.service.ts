import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICountiesAvaiable, CountiesAvaiable } from '../counties-avaiable.model';
import { CountiesAvaiableService } from '../service/counties-avaiable.service';

@Injectable({ providedIn: 'root' })
export class CountiesAvaiableRoutingResolveService implements Resolve<ICountiesAvaiable> {
  constructor(protected service: CountiesAvaiableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICountiesAvaiable> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((countiesAvaiable: HttpResponse<CountiesAvaiable>) => {
          if (countiesAvaiable.body) {
            return of(countiesAvaiable.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CountiesAvaiable());
  }
}
