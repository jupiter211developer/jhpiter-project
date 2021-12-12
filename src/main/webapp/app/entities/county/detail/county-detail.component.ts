import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICounty } from '../county.model';

@Component({
  selector: 'jhi-county-detail',
  templateUrl: './county-detail.component.html',
})
export class CountyDetailComponent implements OnInit {
  county: ICounty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ county }) => {
      this.county = county;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
