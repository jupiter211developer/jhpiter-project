import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICountiesAvaiable, CountiesAvaiable } from '../counties-avaiable.model';
import { CountiesAvaiableService } from '../service/counties-avaiable.service';
import { ICounty } from 'app/entities/county/county.model';
import { CountyService } from 'app/entities/county/service/county.service';

@Component({
  selector: 'jhi-counties-avaiable-update',
  templateUrl: './counties-avaiable-update.component.html',
})
export class CountiesAvaiableUpdateComponent implements OnInit {
  isSaving = false;

  countiesCollection: ICounty[] = [];

  editForm = this.fb.group({
    id: [],
    earliest: [],
    latest: [],
    recordCount: [],
    fips: [null, [Validators.maxLength(5)]],
    countyName: [null, [Validators.maxLength(50)]],
    stateAbbr: [null, [Validators.maxLength(2)]],
    county: [],
  });

  constructor(
    protected countiesAvaiableService: CountiesAvaiableService,
    protected countyService: CountyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countiesAvaiable }) => {
      if (countiesAvaiable.id === undefined) {
        const today = dayjs().startOf('day');
        countiesAvaiable.earliest = today;
        countiesAvaiable.latest = today;
      }

      this.updateForm(countiesAvaiable);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countiesAvaiable = this.createFromForm();
    if (countiesAvaiable.id !== undefined) {
      this.subscribeToSaveResponse(this.countiesAvaiableService.update(countiesAvaiable));
    } else {
      this.subscribeToSaveResponse(this.countiesAvaiableService.create(countiesAvaiable));
    }
  }

  trackCountyById(index: number, item: ICounty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountiesAvaiable>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(countiesAvaiable: ICountiesAvaiable): void {
    this.editForm.patchValue({
      id: countiesAvaiable.id,
      earliest: countiesAvaiable.earliest ? countiesAvaiable.earliest.format(DATE_TIME_FORMAT) : null,
      latest: countiesAvaiable.latest ? countiesAvaiable.latest.format(DATE_TIME_FORMAT) : null,
      recordCount: countiesAvaiable.recordCount,
      fips: countiesAvaiable.fips,
      countyName: countiesAvaiable.countyName,
      stateAbbr: countiesAvaiable.stateAbbr,
      county: countiesAvaiable.county,
    });

    this.countiesCollection = this.countyService.addCountyToCollectionIfMissing(this.countiesCollection, countiesAvaiable.county);
  }

  protected loadRelationshipsOptions(): void {
    this.countyService
      .query({ 'countiesAvaiableId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICounty[]>) => res.body ?? []))
      .pipe(map((counties: ICounty[]) => this.countyService.addCountyToCollectionIfMissing(counties, this.editForm.get('county')!.value)))
      .subscribe((counties: ICounty[]) => (this.countiesCollection = counties));
  }

  protected createFromForm(): ICountiesAvaiable {
    return {
      ...new CountiesAvaiable(),
      id: this.editForm.get(['id'])!.value,
      earliest: this.editForm.get(['earliest'])!.value ? dayjs(this.editForm.get(['earliest'])!.value, DATE_TIME_FORMAT) : undefined,
      latest: this.editForm.get(['latest'])!.value ? dayjs(this.editForm.get(['latest'])!.value, DATE_TIME_FORMAT) : undefined,
      recordCount: this.editForm.get(['recordCount'])!.value,
      fips: this.editForm.get(['fips'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      stateAbbr: this.editForm.get(['stateAbbr'])!.value,
      county: this.editForm.get(['county'])!.value,
    };
  }
}
