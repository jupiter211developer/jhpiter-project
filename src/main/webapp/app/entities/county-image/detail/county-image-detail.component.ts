import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountyImage } from '../county-image.model';

@Component({
  selector: 'jhi-county-image-detail',
  templateUrl: './county-image-detail.component.html',
})
export class CountyImageDetailComponent implements OnInit {
  countyImage: ICountyImage | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyImage }) => {
      this.countyImage = countyImage;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
