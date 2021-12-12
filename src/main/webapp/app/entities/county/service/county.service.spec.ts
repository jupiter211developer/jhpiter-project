import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICounty, County } from '../county.model';

import { CountyService } from './county.service';

describe('County Service', () => {
  let service: CountyService;
  let httpMock: HttpTestingController;
  let elemDefault: ICounty;
  let expectedResult: ICounty | ICounty[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountyService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      countyName: 'AAAAAAA',
      cntyFips: 'AAAAAAA',
      stateAbbr: 'AAAAAAA',
      stFips: 'AAAAAAA',
      fips: 'AAAAAAA',
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

    it('should create a County', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new County()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a County', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countyName: 'BBBBBB',
          cntyFips: 'BBBBBB',
          stateAbbr: 'BBBBBB',
          stFips: 'BBBBBB',
          fips: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a County', () => {
      const patchObject = Object.assign(
        {
          cntyFips: 'BBBBBB',
          stateAbbr: 'BBBBBB',
          stFips: 'BBBBBB',
          fips: 'BBBBBB',
        },
        new County()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of County', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          countyName: 'BBBBBB',
          cntyFips: 'BBBBBB',
          stateAbbr: 'BBBBBB',
          stFips: 'BBBBBB',
          fips: 'BBBBBB',
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

    it('should delete a County', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountyToCollectionIfMissing', () => {
      it('should add a County to an empty array', () => {
        const county: ICounty = { id: 123 };
        expectedResult = service.addCountyToCollectionIfMissing([], county);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(county);
      });

      it('should not add a County to an array that contains it', () => {
        const county: ICounty = { id: 123 };
        const countyCollection: ICounty[] = [
          {
            ...county,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountyToCollectionIfMissing(countyCollection, county);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a County to an array that doesn't contain it", () => {
        const county: ICounty = { id: 123 };
        const countyCollection: ICounty[] = [{ id: 456 }];
        expectedResult = service.addCountyToCollectionIfMissing(countyCollection, county);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(county);
      });

      it('should add only unique County to an array', () => {
        const countyArray: ICounty[] = [{ id: 123 }, { id: 456 }, { id: 23604 }];
        const countyCollection: ICounty[] = [{ id: 123 }];
        expectedResult = service.addCountyToCollectionIfMissing(countyCollection, ...countyArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const county: ICounty = { id: 123 };
        const county2: ICounty = { id: 456 };
        expectedResult = service.addCountyToCollectionIfMissing([], county, county2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(county);
        expect(expectedResult).toContain(county2);
      });

      it('should accept null and undefined values', () => {
        const county: ICounty = { id: 123 };
        expectedResult = service.addCountyToCollectionIfMissing([], null, county, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(county);
      });

      it('should return initial array if no County is added', () => {
        const countyCollection: ICounty[] = [{ id: 123 }];
        expectedResult = service.addCountyToCollectionIfMissing(countyCollection, undefined, null);
        expect(expectedResult).toEqual(countyCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
