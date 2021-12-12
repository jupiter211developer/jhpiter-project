import * as dayjs from 'dayjs';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { ICountyImagePage } from 'app/entities/county-image-page/county-image-page.model';

export interface ICountyImage {
  id?: number;
  recordKey?: string;
  fileSize?: number | null;
  fileName?: string | null;
  pageCnt?: number | null;
  fileDate?: dayjs.Dayjs | null;
  filePath?: string | null;
  md5Hash?: string | null;
  countyRecord?: ICountyRecord | null;
  countyImagePages?: ICountyImagePage[] | null;
}

export class CountyImage implements ICountyImage {
  constructor(
    public id?: number,
    public recordKey?: string,
    public fileSize?: number | null,
    public fileName?: string | null,
    public pageCnt?: number | null,
    public fileDate?: dayjs.Dayjs | null,
    public filePath?: string | null,
    public md5Hash?: string | null,
    public countyRecord?: ICountyRecord | null,
    public countyImagePages?: ICountyImagePage[] | null
  ) {}
}

export function getCountyImageIdentifier(countyImage: ICountyImage): number | undefined {
  return countyImage.id;
}
