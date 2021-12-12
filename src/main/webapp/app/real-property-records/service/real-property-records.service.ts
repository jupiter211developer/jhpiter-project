import { Injectable } from '@angular/core';
import { CountiesAvaiableService, EntityArrayResponseType } from '../../entities/counties-avaiable/service/counties-avaiable.service';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class RealPropertyRecordsService {
  constructor(protected countysAvaiable: CountiesAvaiableService) {}

  getAvaiableCountys(): Observable<EntityArrayResponseType> {
    return this.countysAvaiable.query();
  }
}
