import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICountyImage, getCountyImageIdentifier } from '../county-image.model';

export type EntityResponseType = HttpResponse<ICountyImage>;
export type EntityArrayResponseType = HttpResponse<ICountyImage[]>;

@Injectable({ providedIn: 'root' })
export class CountyImageService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/county-images');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(countyImage: ICountyImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyImage);
    return this.http
      .post<ICountyImage>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(countyImage: ICountyImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyImage);
    return this.http
      .put<ICountyImage>(`${this.resourceUrl}/${getCountyImageIdentifier(countyImage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(countyImage: ICountyImage): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(countyImage);
    return this.http
      .patch<ICountyImage>(`${this.resourceUrl}/${getCountyImageIdentifier(countyImage) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICountyImage>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICountyImage[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCountyImageToCollectionIfMissing(
    countyImageCollection: ICountyImage[],
    ...countyImagesToCheck: (ICountyImage | null | undefined)[]
  ): ICountyImage[] {
    const countyImages: ICountyImage[] = countyImagesToCheck.filter(isPresent);
    if (countyImages.length > 0) {
      const countyImageCollectionIdentifiers = countyImageCollection.map(countyImageItem => getCountyImageIdentifier(countyImageItem)!);
      const countyImagesToAdd = countyImages.filter(countyImageItem => {
        const countyImageIdentifier = getCountyImageIdentifier(countyImageItem);
        if (countyImageIdentifier == null || countyImageCollectionIdentifiers.includes(countyImageIdentifier)) {
          return false;
        }
        countyImageCollectionIdentifiers.push(countyImageIdentifier);
        return true;
      });
      return [...countyImagesToAdd, ...countyImageCollection];
    }
    return countyImageCollection;
  }

  protected convertDateFromClient(countyImage: ICountyImage): ICountyImage {
    return Object.assign({}, countyImage, {
      fileDate: countyImage.fileDate?.isValid() ? countyImage.fileDate.toJSON() : undefined,
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
      res.body.forEach((countyImage: ICountyImage) => {
        countyImage.fileDate = countyImage.fileDate ? dayjs(countyImage.fileDate) : undefined;
      });
    }
    return res;
  }
}
