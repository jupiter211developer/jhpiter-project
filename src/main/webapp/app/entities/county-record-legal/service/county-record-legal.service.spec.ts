import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICountyRecordLegal, CountyRecordLegal } from '../county-record-legal.model';

import { CountyRecordLegalService } from './county-record-legal.service';

describe('CountyRecordLegal Service', () => {
  let service: CountyRecordLegalService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountyRecordLegal;
  let expectedResult: ICountyRecordLegal | ICountyRecordLegal[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyRecordLegalService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      legal: 'AAAAAAA',
      recordKey: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CountyRecordLegal', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CountyRecordLegal()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountyRecordLegal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          legal: 'BBBBBB',
          recordKey: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountyRecordLegal', () => {
      const patchObject = Object.assign(
        {
          legal: 'BBBBBB',
          recordKey: 'BBBBBB',
        },
        new CountyRecordLegal()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountyRecordLegal', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          legal: 'BBBBBB',
          recordKey: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CountyRecordLegal', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyRecordLegalToCollectionIfMissing', () => {
      it('should add a CountyRecordLegal to an empty array', () => {
        const countyRecordLegal: ICountyRecordLegal = { id: 123 };
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing([], countyRecordLegal);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyRecordLegal);
      });

      it('should not add a CountyRecordLegal to an array that contains it', () => {
        const countyRecordLegal: ICountyRecordLegal = { id: 123 };
        const countyRecordLegalCollection: ICountyRecordLegal[] = [
          {
            ...countyRecordLegal,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing(countyRecordLegalCollection, countyRecordLegal);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountyRecordLegal to an array that doesn't contain it", () => {
        const countyRecordLegal: ICountyRecordLegal = { id: 123 };
        const countyRecordLegalCollection: ICountyRecordLegal[] = [{ id: 456 }];
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing(countyRecordLegalCollection, countyRecordLegal);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyRecordLegal);
      });

      it('should add only unique CountyRecordLegal to an array', () => {
        const countyRecordLegalArray: ICountyRecordLegal[] = [{ id: 123 }, { id: 456 }, { id: 82887 }];
        const countyRecordLegalCollection: ICountyRecordLegal[] = [{ id: 123 }];
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing(countyRecordLegalCollection, ...countyRecordLegalArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countyRecordLegal: ICountyRecordLegal = { id: 123 };
        const countyRecordLegal2: ICountyRecordLegal = { id: 456 };
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing([], countyRecordLegal, countyRecordLegal2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyRecordLegal);
        expect(expectedResult).toContain(countyRecordLegal2);
      });

      it('should accept null and undefined values', () => {
        const countyRecordLegal: ICountyRecordLegal = { id: 123 };
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing([], null, countyRecordLegal, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyRecordLegal);
      });

      it('should return initial array if no CountyRecordLegal is added', () => {
        const countyRecordLegalCollection: ICountyRecordLegal[] = [{ id: 123 }];
        expectedResult = service.addCountyRecordLegalToCollectionIfMissing(countyRecordLegalCollection, undefined, null);
        expect(expectedResult).toEqual(countyRecordLegalCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
