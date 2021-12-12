import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IState, State } from '../state.model';
import { StateService } from '../service/state.service';

@Component({
  selector: 'jhi-state-update',
  templateUrl: './state-update.component.html',
})
export class StateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    stateName: [null, [Validators.maxLength(50)]],
    stateAbbr: [null, [Validators.maxLength(2)]],
    subRegion: [null, [Validators.maxLength(18)]],
    stFips: [null, [Validators.maxLength(2)]],
  });

  constructor(protected stateService: StateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ state }) => {
      this.updateForm(state);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const state = this.createFromForm();
    if (state.id !== undefined) {
      this.subscribeToSaveResponse(this.stateService.update(state));
    } else {
      this.subscribeToSaveResponse(this.stateService.create(state));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IState>>): void {
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

  protected updateForm(state: IState): void {
    this.editForm.patchValue({
      id: state.id,
      stateName: state.stateName,
      stateAbbr: state.stateAbbr,
      subRegion: state.subRegion,
      stFips: state.stFips,
    });
  }

  protected createFromForm(): IState {
    return {
      ...new State(),
      id: this.editForm.get(['id'])!.value,
      stateName: this.editForm.get(['stateName'])!.value,
      stateAbbr: this.editForm.get(['stateAbbr'])!.value,
      subRegion: this.editForm.get(['subRegion'])!.value,
      stFips: this.editForm.get(['stFips'])!.value,
    };
  }
}
