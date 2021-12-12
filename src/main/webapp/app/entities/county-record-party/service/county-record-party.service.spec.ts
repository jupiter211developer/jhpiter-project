import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICountyRecordParty, CountyRecordParty } from '../county-record-party.model';

import { CountyRecordPartyService } from './county-record-party.service';

describe('CountyRecordParty Service', () => {
  let service: CountyRecordPartyService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountyRecordParty;
  let expectedResult: ICountyRecordParty | ICountyRecordParty[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyRecordPartyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      recordKey: 'AAAAAAA',
      partyName: 'AAAAAAA',
      partyRole: 0,
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

    it('should create a CountyRecordParty', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CountyRecordParty()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountyRecordParty', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordKey: 'BBBBBB',
          partyName: 'BBBBBB',
          partyRole: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountyRecordParty', () => {
      const patchObject = Object.assign(
        {
          partyRole: 1,
        },
        new CountyRecordParty()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountyRecordParty', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          recordKey: 'BBBBBB',
          partyName: 'BBBBBB',
          partyRole: 1,
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

    it('should delete a CountyRecordParty', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyRecordPartyToCollectionIfMissing', () => {
      it('should add a CountyRecordParty to an empty array', () => {
        const countyRecordParty: ICountyRecordParty = { id: 123 };
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing([], countyRecordParty);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyRecordParty);
      });

      it('should not add a CountyRecordParty to an array that contains it', () => {
        const countyRecordParty: ICountyRecordParty = { id: 123 };
        const countyRecordPartyCollection: ICountyRecordParty[] = [
          {
            ...countyRecordParty,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing(countyRecordPartyCollection, countyRecordParty);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountyRecordParty to an array that doesn't contain it", () => {
        const countyRecordParty: ICountyRecordParty = { id: 123 };
        const countyRecordPartyCollection: ICountyRecordParty[] = [{ id: 456 }];
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing(countyRecordPartyCollection, countyRecordParty);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyRecordParty);
      });

      it('should add only unique CountyRecordParty to an array', () => {
        const countyRecordPartyArray: ICountyRecordParty[] = [{ id: 123 }, { id: 456 }, { id: 93421 }];
        const countyRecordPartyCollection: ICountyRecordParty[] = [{ id: 123 }];
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing(countyRecordPartyCollection, ...countyRecordPartyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countyRecordParty: ICountyRecordParty = { id: 123 };
        const countyRecordParty2: ICountyRecordParty = { id: 456 };
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing([], countyRecordParty, countyRecordParty2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countyRecordParty);
        expect(expectedResult).toContain(countyRecordParty2);
      });

      it('should accept null and undefined values', () => {
        const countyRecordParty: ICountyRecordParty = { id: 123 };
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing([], null, countyRecordParty, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countyRecordParty);
      });

      it('should return initial array if no CountyRecordParty is added', () => {
        const countyRecordPartyCollection: ICountyRecordParty[] = [{ id: 123 }];
        expectedResult = service.addCountyRecordPartyToCollectionIfMissing(countyRecordPartyCollection, undefined, null);
        expect(expectedResult).toEqual(countyRecordPartyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
