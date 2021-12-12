import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountiesAvaiable, getCountiesAvaiableIdentifier } from '../counties-avaiable.model';

export type EntityResponseType = HttpResponse<ICountiesAvaiable>;
export type EntityArrayResponseType = HttpResponse<ICountiesAvaiable[]>;

@Injectable({ providedIn: 'root' })
export class CountiesAvaiableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counties-avaiables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countiesAvaiable: ICountiesAvaiable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countiesAvaiable);
    return this.http
      .post<ICountiesAvaiable>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(countiesAvaiable: ICountiesAvaiable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countiesAvaiable);
    return this.http
      .put<ICountiesAvaiable>(`${this.resourceUrl}/${getCountiesAvaiableIdentifier(countiesAvaiable) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(countiesAvaiable: ICountiesAvaiable): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countiesAvaiable);
    return this.http
      .patch<ICountiesAvaiable>(`${this.resourceUrl}/${getCountiesAvaiableIdentifier(countiesAvaiable) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICountiesAvaiable>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICountiesAvaiable[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountiesAvaiableToCollectionIfMissing(
    countiesAvaiableCollection: ICountiesAvaiable[],
    ...countiesAvaiablesToCheck: (ICountiesAvaiable | null | undefined)[]
  ): ICountiesAvaiable[] {
    const countiesAvaiables: ICountiesAvaiable[] = countiesAvaiablesToCheck.filter(isPresent);
    if (countiesAvaiables.length > 0) {
      const countiesAvaiableCollectionIdentifiers = countiesAvaiableCollection.map(
        countiesAvaiableItem => getCountiesAvaiableIdentifier(countiesAvaiableItem)!
      );
      const countiesAvaiablesToAdd = countiesAvaiables.filter(countiesAvaiableItem => {
        const countiesAvaiableIdentifier = getCountiesAvaiableIdentifier(countiesAvaiableItem);
        if (countiesAvaiableIdentifier == null || countiesAvaiableCollectionIdentifiers.includes(countiesAvaiableIdentifier)) {
          return false;
        }
        countiesAvaiableCollectionIdentifiers.push(countiesAvaiableIdentifier);
        return true;
      });
      return [...countiesAvaiablesToAdd, ...countiesAvaiableCollection];
    }
    return countiesAvaiableCollection;
  }

  protected convertDateFromClient(countiesAvaiable: ICountiesAvaiable): ICountiesAvaiable {
    return Object.assign({}, countiesAvaiable, {
      earliest: countiesAvaiable.earliest?.isValid() ? countiesAvaiable.earliest.toJSON() : undefined,
      latest: countiesAvaiable.latest?.isValid() ? countiesAvaiable.latest.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.earliest = res.body.earliest ? dayjs(res.body.earliest) : undefined;
      res.body.latest = res.body.latest ? dayjs(res.body.latest) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((countiesAvaiable: ICountiesAvaiable) => {
        countiesAvaiable.earliest = countiesAvaiable.earliest ? dayjs(countiesAvaiable.earliest) : undefined;
        countiesAvaiable.latest = countiesAvaiable.latest ? dayjs(countiesAvaiable.latest) : undefined;
      });
    }
    return res;
  }
}
