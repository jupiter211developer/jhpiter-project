import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICountyRecord, CountyRecord } from '../county-record.model';
import { CountyRecordService } from '../service/county-record.service';
import { ICounty } from 'app/entities/county/county.model';
import { CountyService } from 'app/entities/county/service/county.service';

@Component({
  selector: 'jhi-county-record-update',
  templateUrl: './county-record-update.component.html',
})
export class CountyRecordUpdateComponent implements OnInit {
  isSaving = false;

  countiesSharedCollection: ICounty[] = [];

  editForm = this.fb.group({
    id: [],
    cat: [null, [Validators.maxLength(30)]],
    docNum: [null, [Validators.maxLength(50)]],
    docType: [null, [Validators.maxLength(50)]],
    book: [null, [Validators.maxLength(10)]],
    setAbbr: [null, [Validators.maxLength(10)]],
    vol: [null, [Validators.maxLength(10)]],
    pg: [null, [Validators.maxLength(10)]],
    filedDate: [],
    effDate: [],
    recordKey: [null, [Validators.required]],
    fips: [],
    pdfPath: [],
    county: [],
  });

  constructor(
    protected countyRecordService: CountyRecordService,
    protected countyService: CountyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyRecord }) => {
      if (countyRecord.id === undefined) {
        const today = dayjs().startOf('day');
        countyRecord.filedDate = today;
        countyRecord.effDate = today;
      }

      this.updateForm(countyRecord);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countyRecord = this.createFromForm();
    if (countyRecord.id !== undefined) {
      this.subscribeToSaveResponse(this.countyRecordService.update(countyRecord));
    } else {
      this.subscribeToSaveResponse(this.countyRecordService.create(countyRecord));
    }
  }

  trackCountyById(index: number, item: ICounty): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountyRecord>>): void {
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

  protected updateForm(countyRecord: ICountyRecord): void {
    this.editForm.patchValue({
      id: countyRecord.id,
      cat: countyRecord.cat,
      docNum: countyRecord.docNum,
      docType: countyRecord.docType,
      book: countyRecord.book,
      setAbbr: countyRecord.setAbbr,
      vol: countyRecord.vol,
      pg: countyRecord.pg,
      filedDate: countyRecord.filedDate ? countyRecord.filedDate.format(DATE_TIME_FORMAT) : null,
      effDate: countyRecord.effDate ? countyRecord.effDate.format(DATE_TIME_FORMAT) : null,
      recordKey: countyRecord.recordKey,
      fips: countyRecord.fips,
      pdfPath: countyRecord.pdfPath,
      county: countyRecord.county,
    });

    this.countiesSharedCollection = this.countyService.addCountyToCollectionIfMissing(this.countiesSharedCollection, countyRecord.county);
  }

  protected loadRelationshipsOptions(): void {
    this.countyService
      .query()
      .pipe(map((res: HttpResponse<ICounty[]>) => res.body ?? []))
      .pipe(map((counties: ICounty[]) => this.countyService.addCountyToCollectionIfMissing(counties, this.editForm.get('county')!.value)))
      .subscribe((counties: ICounty[]) => (this.countiesSharedCollection = counties));
  }

  protected createFromForm(): ICountyRecord {
    return {
      ...new CountyRecord(),
      id: this.editForm.get(['id'])!.value,
      cat: this.editForm.get(['cat'])!.value,
      docNum: this.editForm.get(['docNum'])!.value,
      docType: this.editForm.get(['docType'])!.value,
      book: this.editForm.get(['book'])!.value,
      setAbbr: this.editForm.get(['setAbbr'])!.value,
      vol: this.editForm.get(['vol'])!.value,
      pg: this.editForm.get(['pg'])!.value,
      filedDate: this.editForm.get(['filedDate'])!.value ? dayjs(this.editForm.get(['filedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      effDate: this.editForm.get(['effDate'])!.value ? dayjs(this.editForm.get(['effDate'])!.value, DATE_TIME_FORMAT) : undefined,
      recordKey: this.editForm.get(['recordKey'])!.value,
      fips: this.editForm.get(['fips'])!.value,
      pdfPath: this.editForm.get(['pdfPath'])!.value,
      county: this.editForm.get(['county'])!.value,
    };
  }
}
