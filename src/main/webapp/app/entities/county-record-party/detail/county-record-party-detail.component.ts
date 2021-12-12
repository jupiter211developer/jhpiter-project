import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountyRecordParty } from '../county-record-party.model';

@Component({
  selector: 'jhi-county-record-party-detail',
  templateUrl: './county-record-party-detail.component.html',
})
export class CountyRecordPartyDetailComponent implements OnInit {
  countyRecordParty: ICountyRecordParty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyRecordParty }) => {
      this.countyRecordParty = countyRecordParty;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
