import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICountyImagePage, CountyImagePage } from '../county-image-page.model';

import { CountyImagePageService } from './county-image-page.service';

describe('CountyImagePage Service', () => {
  let service: CountyImagePageService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountyImagePage;
  let expectedResult: ICountyImagePage | ICountyImagePage[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyImagePageService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      recordKey: 'AAAAAAA',
      fileSize: 0,
      pageNo: 0,
      fileName: 'AAAAAAA',
      fileDate: currentDate,
      filePath: 'AAAAAAA',
      ocrScore: 0,
      md5Hash: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          fileDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CountyImagePage', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          fileDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fileDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CountyImagePage()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountyImagePage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordKey: 'BBBBBB',
          fileSize: 1,
          pageNo: 1,
          fileName: 'BBBBBB',
          fileDate: currentDate.format(DATE_TIME_FORMAT),
          filePath: 'BBBBBB',
          ocrScore: 1,
          md5Hash: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fileDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountyImagePage', () => {
      const patchObject = Object.assign(
        {
          fileName: 'BBBBBB',
          ocrScore: 1,
          md5Hash: 'BBBBBB',
        },
        new CountyImagePage()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          fileDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountyImagePage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordKey: 'BBBBBB',
          fileSize: 1,
          pageNo: 1,
          fileName: 'BBBBBB',
          fileDate: currentDate.format(DATE_TIME_FORMAT),
          filePath: 'BBBBBB',
          ocrScore: 1,
          md5Hash: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          fileDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CountyImagePage', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyImagePageToCollectionIfMissing', () => {
      it('should add a CountyImagePage to an empty array', () => {
        const countyImagePage: ICountyImagePage = { id: 123 };
        expectedResult = service.addCountyImagePageToCollectionIfMissing([], countyImagePage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyImagePage);
      });

      it('should not add a CountyImagePage to an array that contains it', () => {
        const countyImagePage: ICountyImagePage = { id: 123 };
        const countyImagePageCollection: ICountyImagePage[] = [
          {
            ...countyImagePage,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyImagePageToCollectionIfMissing(countyImagePageCollection, countyImagePage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountyImagePage to an array that doesn't contain it", () => {
        const countyImagePage: ICountyImagePage = { id: 123 };
        const countyImagePageCollection: ICountyImagePage[] = [{ id: 456 }];
        expectedResult = service.addCountyImagePageToCollectionIfMissing(countyImagePageCollection, countyImagePage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyImagePage);
      });

      it('should add only unique CountyImagePage to an array', () => {
        const countyImagePageArray: ICountyImagePage[] = [{ id: 123 }, { id: 456 }, { id: 16449 }];
        const countyImagePageCollection: ICountyImagePage[] = [{ id: 123 }];
        expectedResult = service.addCountyImagePageToCollectionIfMissing(countyImagePageCollection, ...countyImagePageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countyImagePage: ICountyImagePage = { id: 123 };
        const countyImagePage2: ICountyImagePage = { id: 456 };
        expectedResult = service.addCountyImagePageToCollectionIfMissing([], countyImagePage, countyImagePage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyImagePage);
        expect(expectedResult).toContain(countyImagePage2);
      });

      it('should accept null and undefined values', () => {
        const countyImagePage: ICountyImagePage = { id: 123 };
        expectedResult = service.addCountyImagePageToCollectionIfMissing([], null, countyImagePage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyImagePage);
      });

      it('should return initial array if no CountyImagePage is added', () => {
        const countyImagePageCollection: ICountyImagePage[] = [{ id: 123 }];
        expectedResult = service.addCountyImagePageToCollectionIfMissing(countyImagePageCollection, undefined, null);
        expect(expectedResult).toEqual(countyImagePageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
