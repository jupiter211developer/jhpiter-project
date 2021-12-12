import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountyImagePage, getCountyImagePageIdentifier } from '../county-image-page.model';

export type EntityResponseType = HttpResponse<ICountyImagePage>;
export type EntityArrayResponseType = HttpResponse<ICountyImagePage[]>;

@Injectable({ providedIn: 'root' })
export class CountyImagePageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/county-image-pages');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countyImagePage: ICountyImagePage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyImagePage);
    return this.http
      .post<ICountyImagePage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(countyImagePage: ICountyImagePage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyImagePage);
    return this.http
      .put<ICountyImagePage>(`${this.resourceUrl}/${getCountyImagePageIdentifier(countyImagePage) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(countyImagePage: ICountyImagePage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyImagePage);
    return this.http
      .patch<ICountyImagePage>(`${this.resourceUrl}/${getCountyImagePageIdentifier(countyImagePage) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICountyImagePage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICountyImagePage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountyImagePageToCollectionIfMissing(
    countyImagePageCollection: ICountyImagePage[],
    ...countyImagePagesToCheck: (ICountyImagePage | null | undefined)[]
  ): ICountyImagePage[] {
    const countyImagePages: ICountyImagePage[] = countyImagePagesToCheck.filter(isPresent);
    if (countyImagePages.length > 0) {
      const countyImagePageCollectionIdentifiers = countyImagePageCollection.map(
        countyImagePageItem => getCountyImagePageIdentifier(countyImagePageItem)!
      );
      const countyImagePagesToAdd = countyImagePages.filter(countyImagePageItem => {
        const countyImagePageIdentifier = getCountyImagePageIdentifier(countyImagePageItem);
        if (countyImagePageIdentifier == null || countyImagePageCollectionIdentifiers.includes(countyImagePageIdentifier)) {
          return false;
        }
        countyImagePageCollectionIdentifiers.push(countyImagePageIdentifier);
        return true;
      });
      return [...countyImagePagesToAdd, ...countyImagePageCollection];
    }
    return countyImagePageCollection;
  }

  protected convertDateFromClient(countyImagePage: ICountyImagePage): ICountyImagePage {
    return Object.assign({}, countyImagePage, {
      fileDate: countyImagePage.fileDate?.isValid() ? countyImagePage.fileDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fileDate = res.body.fileDate ? dayjs(res.body.fileDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((countyImagePage: ICountyImagePage) => {
        countyImagePage.fileDate = countyImagePage.fileDate ? dayjs(countyImagePage.fileDate) : undefined;
      });
    }
    return res;
  }
}
