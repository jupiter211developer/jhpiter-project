import * as dayjs from 'dayjs';
import { ICounty } from 'app/entities/county/county.model';

export interface ICountiesAvaiable {
  id?: number;
  earliest?: dayjs.Dayjs | null;
  latest?: dayjs.Dayjs | null;
  recordCount?: number | null;
  fips?: string | null;
  countyName?: string | null;
  stateAbbr?: string | null;
  county?: ICounty | null;
}

export class CountiesAvaiable implements ICountiesAvaiable {
  constructor(
    public id?: number,
    public earliest?: dayjs.Dayjs | null,
    public latest?: dayjs.Dayjs | null,
    public recordCount?: number | null,
    public fips?: string | null,
    public countyName?: string | null,
    public stateAbbr?: string | null,
    public county?: ICounty | null
  ) {}
}

export function getCountiesAvaiableIdentifier(countiesAvaiable: ICountiesAvaiable): number | undefined {
  return countiesAvaiable.id;
}
