import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICounty, County } from '../county.model';
import { CountyService } from '../service/county.service';
import { IState } from 'app/entities/state/state.model';
import { StateService } from 'app/entities/state/service/state.service';

@Component({
  selector: 'jhi-county-update',
  templateUrl: './county-update.component.html',
})
export class CountyUpdateComponent implements OnInit {
  isSaving = false;

  statesSharedCollection: IState[] = [];

  editForm = this.fb.group({
    id: [],
    countyName: [null, [Validators.maxLength(50)]],
    cntyFips: [null, [Validators.maxLength(3)]],
    stateAbbr: [null, [Validators.maxLength(2)]],
    stFips: [null, [Validators.maxLength(2)]],
    fips: [null, [Validators.maxLength(5)]],
    state: [],
  });

  constructor(
    protected countyService: CountyService,
    protected stateService: StateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ county }) => {
      this.updateForm(county);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const county = this.createFromForm();
    if (county.id !== undefined) {
      this.subscribeToSaveResponse(this.countyService.update(county));
    } else {
      this.subscribeToSaveResponse(this.countyService.create(county));
    }
  }

  trackStateById(index: number, item: IState): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICounty>>): void {
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

  protected updateForm(county: ICounty): void {
    this.editForm.patchValue({
      id: county.id,
      countyName: county.countyName,
      cntyFips: county.cntyFips,
      stateAbbr: county.stateAbbr,
      stFips: county.stFips,
      fips: county.fips,
      state: county.state,
    });

    this.statesSharedCollection = this.stateService.addStateToCollectionIfMissing(this.statesSharedCollection, county.state);
  }

  protected loadRelationshipsOptions(): void {
    this.stateService
      .query()
      .pipe(map((res: HttpResponse<IState[]>) => res.body ?? []))
      .pipe(map((states: IState[]) => this.stateService.addStateToCollectionIfMissing(states, this.editForm.get('state')!.value)))
      .subscribe((states: IState[]) => (this.statesSharedCollection = states));
  }

  protected createFromForm(): ICounty {
    return {
      ...new County(),
      id: this.editForm.get(['id'])!.value,
      countyName: this.editForm.get(['countyName'])!.value,
      cntyFips: this.editForm.get(['cntyFips'])!.value,
      stateAbbr: this.editForm.get(['stateAbbr'])!.value,
      stFips: this.editForm.get(['stFips'])!.value,
      fips: this.editForm.get(['fips'])!.value,
      state: this.editForm.get(['state'])!.value,
    };
  }
}
