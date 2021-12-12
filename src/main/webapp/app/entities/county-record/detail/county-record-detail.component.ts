import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountyRecord } from '../county-record.model';

@Component({
  selector: 'jhi-county-record-detail',
  templateUrl: './county-record-detail.component.html',
})
export class CountyRecordDetailComponent implements OnInit {
  countyRecord: ICountyRecord | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyRecord }) => {
      this.countyRecord = countyRecord;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
