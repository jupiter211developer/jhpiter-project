import { ICountyRecord } from 'app/entities/county-record/county-record.model';

export interface ICountyRecordLegal {
  id?: number;
  legal?: string | null;
  recordKey?: string;
  countyRecord?: ICountyRecord | null;
}

export class CountyRecordLegal implements ICountyRecordLegal {
  constructor(public id?: number, public legal?: string | null, public recordKey?: string, public countyRecord?: ICountyRecord | null) {}
}

export function getCountyRecordLegalIdentifier(countyRecordLegal: ICountyRecordLegal): number | undefined {
  return countyRecordLegal.id;
}
