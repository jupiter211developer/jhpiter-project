import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountyRecordParty, getCountyRecordPartyIdentifier } from '../county-record-party.model';

export type EntityResponseType = HttpResponse<ICountyRecordParty>;
export type EntityArrayResponseType = HttpResponse<ICountyRecordParty[]>;

@Injectable({ providedIn: 'root' })
export class CountyRecordPartyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/county-record-parties');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countyRecordParty: ICountyRecordParty): Observable<EntityResponseType> {
    return this.http.post<ICountyRecordParty>(this.resourceUrl, countyRecordParty, { observe: 'response' });
  }

  update(countyRecordParty: ICountyRecordParty): Observable<EntityResponseType> {
    return this.http.put<ICountyRecordParty>(
      `${this.resourceUrl}/${getCountyRecordPartyIdentifier(countyRecordParty) as number}`,
      countyRecordParty,
      { observe: 'response' }
    );
  }

  partialUpdate(countyRecordParty: ICountyRecordParty): Observable<EntityResponseType> {
    return this.http.patch<ICountyRecordParty>(
      `${this.resourceUrl}/${getCountyRecordPartyIdentifier(countyRecordParty) as number}`,
      countyRecordParty,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICountyRecordParty>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICountyRecordParty[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountyRecordPartyToCollectionIfMissing(
    countyRecordPartyCollection: ICountyRecordParty[],
    ...countyRecordPartiesToCheck: (ICountyRecordParty | null | undefined)[]
  ): ICountyRecordParty[] {
    const countyRecordParties: ICountyRecordParty[] = countyRecordPartiesToCheck.filter(isPresent);
    if (countyRecordParties.length > 0) {
      const countyRecordPartyCollectionIdentifiers = countyRecordPartyCollection.map(
        countyRecordPartyItem => getCountyRecordPartyIdentifier(countyRecordPartyItem)!
      );
      const countyRecordPartiesToAdd = countyRecordParties.filter(countyRecordPartyItem => {
        const countyRecordPartyIdentifier = getCountyRecordPartyIdentifier(countyRecordPartyItem);
        if (countyRecordPartyIdentifier == null || countyRecordPartyCollectionIdentifiers.includes(countyRecordPartyIdentifier)) {
          return false;
        }
        countyRecordPartyCollectionIdentifiers.push(countyRecordPartyIdentifier);
        return true;
      });
      return [...countyRecordPartiesToAdd, ...countyRecordPartyCollection];
    }
    return countyRecordPartyCollection;
  }
}
