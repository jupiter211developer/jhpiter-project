import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountyRecord, getCountyRecordIdentifier } from '../county-record.model';

export type EntityResponseType = HttpResponse<ICountyRecord>;
export type EntityArrayResponseType = HttpResponse<ICountyRecord[]>;

@Injectable({ providedIn: 'root' })
export class CountyRecordService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/county-records');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countyRecord: ICountyRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyRecord);
    return this.http
      .post<ICountyRecord>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(countyRecord: ICountyRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyRecord);
    return this.http
      .put<ICountyRecord>(`${this.resourceUrl}/${getCountyRecordIdentifier(countyRecord) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(countyRecord: ICountyRecord): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyRecord);
    return this.http
      .patch<ICountyRecord>(`${this.resourceUrl}/${getCountyRecordIdentifier(countyRecord) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICountyRecord>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICountyRecord[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountyRecordToCollectionIfMissing(
    countyRecordCollection: ICountyRecord[],
    ...countyRecordsToCheck: (ICountyRecord | null | undefined)[]
  ): ICountyRecord[] {
    const countyRecords: ICountyRecord[] = countyRecordsToCheck.filter(isPresent);
    if (countyRecords.length > 0) {
      const countyRecordCollectionIdentifiers = countyRecordCollection.map(
        countyRecordItem => getCountyRecordIdentifier(countyRecordItem)!
      );
      const countyRecordsToAdd = countyRecords.filter(countyRecordItem => {
        const countyRecordIdentifier = getCountyRecordIdentifier(countyRecordItem);
        if (countyRecordIdentifier == null || countyRecordCollectionIdentifiers.includes(countyRecordIdentifier)) {
          return false;
        }
        countyRecordCollectionIdentifiers.push(countyRecordIdentifier);
        return true;
      });
      return [...countyRecordsToAdd, ...countyRecordCollection];
    }
    return countyRecordCollection;
  }

  protected convertDateFromClient(countyRecord: ICountyRecord): ICountyRecord {
    return Object.assign({}, countyRecord, {
      filedDate: countyRecord.filedDate?.isValid() ? countyRecord.filedDate.toJSON() : undefined,
      effDate: countyRecord.effDate?.isValid() ? countyRecord.effDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.filedDate = res.body.filedDate ? dayjs(res.body.filedDate) : undefined;
      res.body.effDate = res.body.effDate ? dayjs(res.body.effDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((countyRecord: ICountyRecord) => {
        countyRecord.filedDate = countyRecord.filedDate ? dayjs(countyRecord.filedDate) : undefined;
        countyRecord.effDate = countyRecord.effDate ? dayjs(countyRecord.effDate) : undefined;
      });
    }
    return res;
  }
}
