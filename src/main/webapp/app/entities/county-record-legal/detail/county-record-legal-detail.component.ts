import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountyRecordLegal } from '../county-record-legal.model';

@Component({
  selector: 'jhi-county-record-legal-detail',
  templateUrl: './county-record-legal-detail.component.html',
})
export class CountyRecordLegalDetailComponent implements OnInit {
  countyRecordLegal: ICountyRecordLegal | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyRecordLegal }) => {
      this.countyRecordLegal = countyRecordLegal;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
