import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICountyRecordLegal, CountyRecordLegal } from '../county-record-legal.model';
import { CountyRecordLegalService } from '../service/county-record-legal.service';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';

@Component({
  selector: 'jhi-county-record-legal-update',
  templateUrl: './county-record-legal-update.component.html',
})
export class CountyRecordLegalUpdateComponent implements OnInit {
  isSaving = false;

  countyRecordsSharedCollection: ICountyRecord[] = [];

  editForm = this.fb.group({
    id: [],
    legal: [null, [Validators.maxLength(255)]],
    recordKey: [null, [Validators.required]],
    countyRecord: [],
  });

  constructor(
    protected countyRecordLegalService: CountyRecordLegalService,
    protected countyRecordService: CountyRecordService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyRecordLegal }) => {
      this.updateForm(countyRecordLegal);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countyRecordLegal = this.createFromForm();
    if (countyRecordLegal.id !== undefined) {
      this.subscribeToSaveResponse(this.countyRecordLegalService.update(countyRecordLegal));
    } else {
      this.subscribeToSaveResponse(this.countyRecordLegalService.create(countyRecordLegal));
    }
  }

  trackCountyRecordById(index: number, item: ICountyRecord): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountyRecordLegal>>): void {
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

  protected updateForm(countyRecordLegal: ICountyRecordLegal): void {
    this.editForm.patchValue({
      id: countyRecordLegal.id,
      legal: countyRecordLegal.legal,
      recordKey: countyRecordLegal.recordKey,
      countyRecord: countyRecordLegal.countyRecord,
    });

    this.countyRecordsSharedCollection = this.countyRecordService.addCountyRecordToCollectionIfMissing(
      this.countyRecordsSharedCollection,
      countyRecordLegal.countyRecord
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

  protected createFromForm(): ICountyRecordLegal {
    return {
      ...new CountyRecordLegal(),
      id: this.editForm.get(['id'])!.value,
      legal: this.editForm.get(['legal'])!.value,
      recordKey: this.editForm.get(['recordKey'])!.value,
      countyRecord: this.editForm.get(['countyRecord'])!.value,
    };
  }
}
