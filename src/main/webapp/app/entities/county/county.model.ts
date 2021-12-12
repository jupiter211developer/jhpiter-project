import { IState } from 'app/entities/state/state.model';
import { ICountiesAvaiable } from 'app/entities/counties-avaiable/counties-avaiable.model';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';

export interface ICounty {
  id?: number;
  countyName?: string | null;
  cntyFips?: string | null;
  stateAbbr?: string | null;
  stFips?: string | null;
  fips?: string | null;
  state?: IState | null;
  countiesAvaiable?: ICountiesAvaiable | null;
  countyRecords?: ICountyRecord[] | null;
}

export class County implements ICounty {
  constructor(
    public id?: number,
    public countyName?: string | null,
    public cntyFips?: string | null,
    public stateAbbr?: string | null,
    public stFips?: string | null,
    public fips?: string | null,
    public state?: IState | null,
    public countiesAvaiable?: ICountiesAvaiable | null,
    public countyRecords?: ICountyRecord[] | null
  ) {}
}

export function getCountyIdentifier(county: ICounty): number | undefined {
  return county.id;
}
