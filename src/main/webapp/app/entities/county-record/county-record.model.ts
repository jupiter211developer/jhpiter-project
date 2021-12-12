import * as dayjs from 'dayjs';
import { ICounty } from 'app/entities/county/county.model';
import { ICountyImage } from 'app/entities/county-image/county-image.model';
import { ICountyImagePage } from 'app/entities/county-image-page/county-image-page.model';
import { ICountyRecordParty } from 'app/entities/county-record-party/county-record-party.model';
import { ICountyRecordLegal } from 'app/entities/county-record-legal/county-record-legal.model';

export interface ICountyRecord {
  id?: number;
  cat?: string | null;
  docNum?: string | null;
  docType?: string | null;
  book?: string | null;
  setAbbr?: string | null;
  vol?: string | null;
  pg?: string | null;
  filedDate?: dayjs.Dayjs | null;
  effDate?: dayjs.Dayjs | null;
  recordKey?: string;
  fips?: string | null;
  pdfPath?: string | null;
  county?: ICounty | null;
  countyImage?: ICountyImage | null;
  countyImagePages?: ICountyImagePage[] | null;
  countyRecordParties?: ICountyRecordParty[] | null;
  countyRecordLegals?: ICountyRecordLegal[] | null;
}

export class CountyRecord implements ICountyRecord {
  constructor(
    public id?: number,
    public cat?: string | null,
    public docNum?: string | null,
    public docType?: string | null,
    public book?: string | null,
    public setAbbr?: string | null,
    public vol?: string | null,
    public pg?: string | null,
    public filedDate?: dayjs.Dayjs | null,
    public effDate?: dayjs.Dayjs | null,
    public recordKey?: string,
    public fips?: string | null,
    public pdfPath?: string | null,
    public county?: ICounty | null,
    public countyImage?: ICountyImage | null,
    public countyImagePages?: ICountyImagePage[] | null,
    public countyRecordParties?: ICountyRecordParty[] | null,
    public countyRecordLegals?: ICountyRecordLegal[] | null
  ) {}
}

export function getCountyRecordIdentifier(countyRecord: ICountyRecord): number | undefined {
  return countyRecord.id;
}
