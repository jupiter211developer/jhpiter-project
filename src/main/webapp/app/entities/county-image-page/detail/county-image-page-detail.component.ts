import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountyImagePage } from '../county-image-page.model';

@Component({
  selector: 'jhi-county-image-page-detail',
  templateUrl: './county-image-page-detail.component.html',
})
export class CountyImagePageDetailComponent implements OnInit {
  countyImagePage: ICountyImagePage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyImagePage }) => {
      this.countyImagePage = countyImagePage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
