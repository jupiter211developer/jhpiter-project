import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICountyImage, CountyImage } from '../county-image.model';

import { CountyImageService } from './county-image.service';

describe('CountyImage Service', () => {
  let service: CountyImageService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountyImage;
  let expectedResult: ICountyImage | ICountyImage[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyImageService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      recordKey: 'AAAAAAA',
      fileSize: 0,
      fileName: 'AAAAAAA',
      pageCnt: 0,
      fileDate: currentDate,
      filePath: 'AAAAAAA',
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

    it('should create a CountyImage', () => {
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

      service.create(new CountyImage()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountyImage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordKey: 'BBBBBB',
          fileSize: 1,
          fileName: 'BBBBBB',
          pageCnt: 1,
          fileDate: currentDate.format(DATE_TIME_FORMAT),
          filePath: 'BBBBBB',
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

    it('should partial update a CountyImage', () => {
      const patchObject = Object.assign(
        {
          fileSize: 1,
          fileName: 'BBBBBB',
          fileDate: currentDate.format(DATE_TIME_FORMAT),
          filePath: 'BBBBBB',
          md5Hash: 'BBBBBB',
        },
        new CountyImage()
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

    it('should return a list of CountyImage', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordKey: 'BBBBBB',
          fileSize: 1,
          fileName: 'BBBBBB',
          pageCnt: 1,
          fileDate: currentDate.format(DATE_TIME_FORMAT),
          filePath: 'BBBBBB',
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

    it('should delete a CountyImage', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyImageToCollectionIfMissing', () => {
      it('should add a CountyImage to an empty array', () => {
        const countyImage: ICountyImage = { id: 123 };
        expectedResult = service.addCountyImageToCollectionIfMissing([], countyImage);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyImage);
      });

      it('should not add a CountyImage to an array that contains it', () => {
        const countyImage: ICountyImage = { id: 123 };
        const countyImageCollection: ICountyImage[] = [
          {
            ...countyImage,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyImageToCollectionIfMissing(countyImageCollection, countyImage);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountyImage to an array that doesn't contain it", () => {
        const countyImage: ICountyImage = { id: 123 };
        const countyImageCollection: ICountyImage[] = [{ id: 456 }];
        expectedResult = service.addCountyImageToCollectionIfMissing(countyImageCollection, countyImage);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyImage);
      });

      it('should add only unique CountyImage to an array', () => {
        const countyImageArray: ICountyImage[] = [{ id: 123 }, { id: 456 }, { id: 88979 }];
        const countyImageCollection: ICountyImage[] = [{ id: 123 }];
        expectedResult = service.addCountyImageToCollectionIfMissing(countyImageCollection, ...countyImageArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countyImage: ICountyImage = { id: 123 };
        const countyImage2: ICountyImage = { id: 456 };
        expectedResult = service.addCountyImageToCollectionIfMissing([], countyImage, countyImage2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyImage);
        expect(expectedResult).toContain(countyImage2);
      });

      it('should accept null and undefined values', () => {
        const countyImage: ICountyImage = { id: 123 };
        expectedResult = service.addCountyImageToCollectionIfMissing([], null, countyImage, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyImage);
      });

      it('should return initial array if no CountyImage is added', () => {
        const countyImageCollection: ICountyImage[] = [{ id: 123 }];
        expectedResult = service.addCountyImageToCollectionIfMissing(countyImageCollection, undefined, null);
        expect(expectedResult).toEqual(countyImageCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
