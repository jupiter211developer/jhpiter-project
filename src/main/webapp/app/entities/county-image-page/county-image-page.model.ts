import * as dayjs from 'dayjs';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { ICountyImage } from 'app/entities/county-image/county-image.model';

export interface ICountyImagePage {
  id?: number;
  recordKey?: string;
  fileSize?: number | null;
  pageNo?: number | null;
  fileName?: string | null;
  fileDate?: dayjs.Dayjs | null;
  filePath?: string | null;
  ocrScore?: number | null;
  md5Hash?: string | null;
  countyRecord?: ICountyRecord | null;
  countyImage?: ICountyImage | null;
}

export class CountyImagePage implements ICountyImagePage {
  constructor(
    public id?: number,
    public recordKey?: string,
    public fileSize?: number | null,
    public pageNo?: number | null,
    public fileName?: string | null,
    public fileDate?: dayjs.Dayjs | null,
    public filePath?: string | null,
    public ocrScore?: number | null,
    public md5Hash?: string | null,
    public countyRecord?: ICountyRecord | null,
    public countyImage?: ICountyImage | null
  ) {}
}

export function getCountyImagePageIdentifier(countyImagePage: ICountyImagePage): number | undefined {
  return countyImagePage.id;
}
