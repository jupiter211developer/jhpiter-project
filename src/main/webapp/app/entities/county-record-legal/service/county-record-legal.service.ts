import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountyRecordLegal, getCountyRecordLegalIdentifier } from '../county-record-legal.model';

export type EntityResponseType = HttpResponse<ICountyRecordLegal>;
export type EntityArrayResponseType = HttpResponse<ICountyRecordLegal[]>;

@Injectable({ providedIn: 'root' })
export class CountyRecordLegalService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/county-record-legals');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countyRecordLegal: ICountyRecordLegal): Observable<EntityResponseType> {
    return this.http.post<ICountyRecordLegal>(this.resourceUrl, countyRecordLegal, { observe: 'response' });
  }

  update(countyRecordLegal: ICountyRecordLegal): Observable<EntityResponseType> {
    return this.http.put<ICountyRecordLegal>(
      `${this.resourceUrl}/${getCountyRecordLegalIdentifier(countyRecordLegal) as number}`,
      countyRecordLegal,
      { observe: 'response' }
    );
  }

  partialUpdate(countyRecordLegal: ICountyRecordLegal): Observable<EntityResponseType> {
    return this.http.patch<ICountyRecordLegal>(
      `${this.resourceUrl}/${getCountyRecordLegalIdentifier(countyRecordLegal) as number}`,
      countyRecordLegal,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICountyRecordLegal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountyRecordLegal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountyRecordLegalToCollectionIfMissing(
    countyRecordLegalCollection: ICountyRecordLegal[],
    ...countyRecordLegalsToCheck: (ICountyRecordLegal | null | undefined)[]
  ): ICountyRecordLegal[] {
    const countyRecordLegals: ICountyRecordLegal[] = countyRecordLegalsToCheck.filter(isPresent);
    if (countyRecordLegals.length > 0) {
      const countyRecordLegalCollectionIdentifiers = countyRecordLegalCollection.map(
        countyRecordLegalItem => getCountyRecordLegalIdentifier(countyRecordLegalItem)!
      );
      const countyRecordLegalsToAdd = countyRecordLegals.filter(countyRecordLegalItem => {
        const countyRecordLegalIdentifier = getCountyRecordLegalIdentifier(countyRecordLegalItem);
        if (countyRecordLegalIdentifier == null || countyRecordLegalCollectionIdentifiers.includes(countyRecordLegalIdentifier)) {
          return false;
        }
        countyRecordLegalCollectionIdentifiers.push(countyRecordLegalIdentifier);
        return true;
      });
      return [...countyRecordLegalsToAdd, ...countyRecordLegalCollection];
    }
    return countyRecordLegalCollection;
  }
}
