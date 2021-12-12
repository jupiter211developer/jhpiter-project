import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICountiesAvaiable } from '../counties-avaiable.model';

@Component({
  selector: 'jhi-counties-avaiable-detail',
  templateUrl: './counties-avaiable-detail.component.html',
})
export class CountiesAvaiableDetailComponent implements OnInit {
  countiesAvaiable: ICountiesAvaiable | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countiesAvaiable }) => {
      this.countiesAvaiable = countiesAvaiable;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
