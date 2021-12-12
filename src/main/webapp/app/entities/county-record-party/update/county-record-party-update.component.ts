import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountyRecordParty, CountyRecordParty } from '../county-record-party.model';
import { CountyRecordPartyService } from '../service/county-record-party.service';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';

@Component({
  selector: 'jhi-county-record-party-update',
  templateUrl: './county-record-party-update.component.html',
})
export class CountyRecordPartyUpdateComponent implements OnInit {
  isSaving = false;

  countyRecordsSharedCollection: ICountyRecord[] = [];

  editForm = this.fb.group({
    id: [],
    recordKey: [null, [Validators.required]],
    partyName: [],
    partyRole: [],
    countyRecord: [],
  });

  constructor(
    protected countyRecordPartyService: CountyRecordPartyService,
    protected countyRecordService: CountyRecordService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyRecordParty }) => {
      this.updateForm(countyRecordParty);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countyRecordParty = this.createFromForm();
    if (countyRecordParty.id !== undefined) {
      this.subscribeToSaveResponse(this.countyRecordPartyService.update(countyRecordParty));
    } else {
      this.subscribeToSaveResponse(this.countyRecordPartyService.create(countyRecordParty));
    }
  }

  trackCountyRecordById(index: number, item: ICountyRecord): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountyRecordParty>>): void {
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

  protected updateForm(countyRecordParty: ICountyRecordParty): void {
    this.editForm.patchValue({
      id: countyRecordParty.id,
      recordKey: countyRecordParty.recordKey,
      partyName: countyRecordParty.partyName,
      partyRole: countyRecordParty.partyRole,
      countyRecord: countyRecordParty.countyRecord,
    });

    this.countyRecordsSharedCollection = this.countyRecordService.addCountyRecordToCollectionIfMissing(
      this.countyRecordsSharedCollection,
      countyRecordParty.countyRecord
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countyRecordService
      .query()
      .pipe(map((res: HttpResponse<ICountyRecord[]>) => res.body ?? []))
      .pipe(
        map((countyRecords: ICountyRecord[]) =>
          this.countyRecordService.addCountyRecordToCollectionIfMissing(countyRecords, this.editForm.get('countyRecord')!.value)
        )
      )
      .subscribe((countyRecords: ICountyRecord[]) => (this.countyRecordsSharedCollection = countyRecords));
  }

  protected createFromForm(): ICountyRecordParty {
    return {
      ...new CountyRecordParty(),
      id: this.editForm.get(['id'])!.value,
      recordKey: this.editForm.get(['recordKey'])!.value,
      partyName: this.editForm.get(['partyName'])!.value,
      partyRole: this.editForm.get(['partyRole'])!.value,
      countyRecord: this.editForm.get(['countyRecord'])!.value,
    };
  }
}
