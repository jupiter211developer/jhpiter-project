import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICountyRecord, CountyRecord } from '../county-record.model';

import { CountyRecordService } from './county-record.service';

describe('CountyRecord Service', () => {
  let service: CountyRecordService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountyRecord;
  let expectedResult: ICountyRecord | ICountyRecord[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyRecordService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      cat: 'AAAAAAA',
      docNum: 'AAAAAAA',
      docType: 'AAAAAAA',
      book: 'AAAAAAA',
      setAbbr: 'AAAAAAA',
      vol: 'AAAAAAA',
      pg: 'AAAAAAA',
      filedDate: currentDate,
      effDate: currentDate,
      recordKey: 'AAAAAAA',
      fips: 'AAAAAAA',
      pdfPath: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          filedDate: currentDate.format(DATE_TIME_FORMAT),
          effDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CountyRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          filedDate: currentDate.format(DATE_TIME_FORMAT),
          effDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          filedDate: currentDate,
          effDate: currentDate,
        },
        returnedFromService
      );

      service.create(new CountyRecord()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountyRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cat: 'BBBBBB',
          docNum: 'BBBBBB',
          docType: 'BBBBBB',
          book: 'BBBBBB',
          setAbbr: 'BBBBBB',
          vol: 'BBBBBB',
          pg: 'BBBBBB',
          filedDate: currentDate.format(DATE_TIME_FORMAT),
          effDate: currentDate.format(DATE_TIME_FORMAT),
          recordKey: 'BBBBBB',
          fips: 'BBBBBB',
          pdfPath: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          filedDate: currentDate,
          effDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountyRecord', () => {
      const patchObject = Object.assign(
        {
          cat: 'BBBBBB',
          vol: 'BBBBBB',
          filedDate: currentDate.format(DATE_TIME_FORMAT),
          pdfPath: 'BBBBBB',
        },
        new CountyRecord()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          filedDate: currentDate,
          effDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountyRecord', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          cat: 'BBBBBB',
          docNum: 'BBBBBB',
          docType: 'BBBBBB',
          book: 'BBBBBB',
          setAbbr: 'BBBBBB',
          vol: 'BBBBBB',
          pg: 'BBBBBB',
          filedDate: currentDate.format(DATE_TIME_FORMAT),
          effDate: currentDate.format(DATE_TIME_FORMAT),
          recordKey: 'BBBBBB',
          fips: 'BBBBBB',
          pdfPath: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          filedDate: currentDate,
          effDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CountyRecord', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyRecordToCollectionIfMissing', () => {
      it('should add a CountyRecord to an empty array', () => {
        const countyRecord: ICountyRecord = { id: 123 };
        expectedResult = service.addCountyRecordToCollectionIfMissing([], countyRecord);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyRecord);
      });

      it('should not add a CountyRecord to an array that contains it', () => {
        const countyRecord: ICountyRecord = { id: 123 };
        const countyRecordCollection: ICountyRecord[] = [
          {
            ...countyRecord,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyRecordToCollectionIfMissing(countyRecordCollection, countyRecord);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountyRecord to an array that doesn't contain it", () => {
        const countyRecord: ICountyRecord = { id: 123 };
        const countyRecordCollection: ICountyRecord[] = [{ id: 456 }];
        expectedResult = service.addCountyRecordToCollectionIfMissing(countyRecordCollection, countyRecord);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyRecord);
      });

      it('should add only unique CountyRecord to an array', () => {
        const countyRecordArray: ICountyRecord[] = [{ id: 123 }, { id: 456 }, { id: 50131 }];
        const countyRecordCollection: ICountyRecord[] = [{ id: 123 }];
        expectedResult = service.addCountyRecordToCollectionIfMissing(countyRecordCollection, ...countyRecordArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countyRecord: ICountyRecord = { id: 123 };
        const countyRecord2: ICountyRecord = { id: 456 };
        expectedResult = service.addCountyRecordToCollectionIfMissing([], countyRecord, countyRecord2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyRecord);
        expect(expectedResult).toContain(countyRecord2);
      });

      it('should accept null and undefined values', () => {
        const countyRecord: ICountyRecord = { id: 123 };
        expectedResult = service.addCountyRecordToCollectionIfMissing([], null, countyRecord, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyRecord);
      });

      it('should return initial array if no CountyRecord is added', () => {
        const countyRecordCollection: ICountyRecord[] = [{ id: 123 }];
        expectedResult = service.addCountyRecordToCollectionIfMissing(countyRecordCollection, undefined, null);
        expect(expectedResult).toEqual(countyRecordCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
