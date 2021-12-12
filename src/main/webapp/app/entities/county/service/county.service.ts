import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICounty, getCountyIdentifier } from '../county.model';

export type EntityResponseType = HttpResponse<ICounty>;
export type EntityArrayResponseType = HttpResponse<ICounty[]>;

@Injectable({ providedIn: 'root' })
export class CountyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/counties');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(county: ICounty): Observable<EntityResponseType> {
    return this.http.post<ICounty>(this.resourceUrl, county, { observe: 'response' });
  }

  update(county: ICounty): Observable<EntityResponseType> {
    return this.http.put<ICounty>(`${this.resourceUrl}/${getCountyIdentifier(county) as number}`, county, { observe: 'response' });
  }

  partialUpdate(county: ICounty): Observable<EntityResponseType> {
    return this.http.patch<ICounty>(`${this.resourceUrl}/${getCountyIdentifier(county) as number}`, county, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICounty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICounty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountyToCollectionIfMissing(countyCollection: ICounty[], ...countiesToCheck: (ICounty | null | undefined)[]): ICounty[] {
    const counties: ICounty[] = countiesToCheck.filter(isPresent);
    if (counties.length > 0) {
      const countyCollectionIdentifiers = countyCollection.map(countyItem => getCountyIdentifier(countyItem)!);
      const countiesToAdd = counties.filter(countyItem => {
        const countyIdentifier = getCountyIdentifier(countyItem);
        if (countyIdentifier == null || countyCollectionIdentifiers.includes(countyIdentifier)) {
          return false;
        }
        countyCollectionIdentifiers.push(countyIdentifier);
        return true;
      });
      return [...countiesToAdd, ...countyCollection];
    }
    return countyCollection;
  }
}
