import { Pipe, PipeTransform } from '@angular/core';

import * as dayjs from 'dayjs';

@Pipe({
  name: 'formatDateNts',
})
export class FormatDateNts implements PipeTransform {
  transform(day: dayjs.Dayjs | null | undefined): string {
    return day ? day.toISOString().substr(0, 10) : '';
  }
}
