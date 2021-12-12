import { ICountyRecord } from 'app/entities/county-record/county-record.model';

export interface ICountyRecordParty {
  id?: number;
  recordKey?: string;
  partyName?: string | null;
  partyRole?: number | null;
  countyRecord?: ICountyRecord | null;
}

export class CountyRecordParty implements ICountyRecordParty {
  constructor(
    public id?: number,
    public recordKey?: string,
    public partyName?: string | null,
    public partyRole?: number | null,
    public countyRecord?: ICountyRecord | null
  ) {}
}

export function getCountyRecordPartyIdentifier(countyRecordParty: ICountyRecordParty): number | undefined {
  return countyRecordParty.id;
}
