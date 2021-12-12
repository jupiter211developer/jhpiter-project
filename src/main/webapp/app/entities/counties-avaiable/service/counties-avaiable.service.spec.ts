import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ICountiesAvaiable, CountiesAvaiable } from '../counties-avaiable.model';

import { CountiesAvaiableService } from './counties-avaiable.service';

describe('CountiesAvaiable Service', () => {
  let service: CountiesAvaiableService;
  let httpMock: HttpTestingController;
  let elemDefault: ICountiesAvaiable;
  let expectedResult: ICountiesAvaiable | ICountiesAvaiable[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CountiesAvaiableService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      earliest: currentDate,
      latest: currentDate,
      recordCount: 0,
      fips: 'AAAAAAA',
      countyName: 'AAAAAAA',
      stateAbbr: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          earliest: currentDate.format(DATE_TIME_FORMAT),
          latest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CountiesAvaiable', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          earliest: currentDate.format(DATE_TIME_FORMAT),
          latest: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          earliest: currentDate,
          latest: currentDate,
        },
        returnedFromService
      );

      service.create(new CountiesAvaiable()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CountiesAvaiable', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          earliest: currentDate.format(DATE_TIME_FORMAT),
          latest: currentDate.format(DATE_TIME_FORMAT),
          recordCount: 1,
          fips: 'BBBBBB',
          countyName: 'BBBBBB',
          stateAbbr: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          earliest: currentDate,
          latest: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CountiesAvaiable', () => {
      const patchObject = Object.assign(
        {
          latest: currentDate.format(DATE_TIME_FORMAT),
          recordCount: 1,
          fips: 'BBBBBB',
        },
        new CountiesAvaiable()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          earliest: currentDate,
          latest: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CountiesAvaiable', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          earliest: currentDate.format(DATE_TIME_FORMAT),
          latest: currentDate.format(DATE_TIME_FORMAT),
          recordCount: 1,
          fips: 'BBBBBB',
          countyName: 'BBBBBB',
          stateAbbr: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          earliest: currentDate,
          latest: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CountiesAvaiable', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCountiesAvaiableToCollectionIfMissing', () => {
      it('should add a CountiesAvaiable to an empty array', () => {
        const countiesAvaiable: ICountiesAvaiable = { id: 123 };
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing([], countiesAvaiable);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countiesAvaiable);
      });

      it('should not add a CountiesAvaiable to an array that contains it', () => {
        const countiesAvaiable: ICountiesAvaiable = { id: 123 };
        const countiesAvaiableCollection: ICountiesAvaiable[] = [
          {
            ...countiesAvaiable,
          },
          { id: 456 },
        ];
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing(countiesAvaiableCollection, countiesAvaiable);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CountiesAvaiable to an array that doesn't contain it", () => {
        const countiesAvaiable: ICountiesAvaiable = { id: 123 };
        const countiesAvaiableCollection: ICountiesAvaiable[] = [{ id: 456 }];
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing(countiesAvaiableCollection, countiesAvaiable);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countiesAvaiable);
      });

      it('should add only unique CountiesAvaiable to an array', () => {
        const countiesAvaiableArray: ICountiesAvaiable[] = [{ id: 123 }, { id: 456 }, { id: 15159 }];
        const countiesAvaiableCollection: ICountiesAvaiable[] = [{ id: 123 }];
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing(countiesAvaiableCollection, ...countiesAvaiableArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const countiesAvaiable: ICountiesAvaiable = { id: 123 };
        const countiesAvaiable2: ICountiesAvaiable = { id: 456 };
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing([], countiesAvaiable, countiesAvaiable2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(countiesAvaiable);
        expect(expectedResult).toContain(countiesAvaiable2);
      });

      it('should accept null and undefined values', () => {
        const countiesAvaiable: ICountiesAvaiable = { id: 123 };
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing([], null, countiesAvaiable, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(countiesAvaiable);
      });

      it('should return initial array if no CountiesAvaiable is added', () => {
        const countiesAvaiableCollection: ICountiesAvaiable[] = [{ id: 123 }];
        expectedResult = service.addCountiesAvaiableToCollectionIfMissing(countiesAvaiableCollection, undefined, null);
        expect(expectedResult).toEqual(countiesAvaiableCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
