import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICountyImage, CountyImage } from '../county-image.model';
import { CountyImageService } from '../service/county-image.service';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';

@Component({
  selector: 'jhi-county-image-update',
  templateUrl: './county-image-update.component.html',
})
export class CountyImageUpdateComponent implements OnInit {
  isSaving = false;

  countyRecordsCollection: ICountyRecord[] = [];

  editForm = this.fb.group({
    id: [],
    recordKey: [null, [Validators.required]],
    fileSize: [],
    fileName: [],
    pageCnt: [],
    fileDate: [],
    filePath: [],
    md5Hash: [],
    countyRecord: [],
  });

  constructor(
    protected countyImageService: CountyImageService,
    protected countyRecordService: CountyRecordService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyImage }) => {
      if (countyImage.id === undefined) {
        const today = dayjs().startOf('day');
        countyImage.fileDate = today;
      }

      this.updateForm(countyImage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countyImage = this.createFromForm();
    if (countyImage.id !== undefined) {
      this.subscribeToSaveResponse(this.countyImageService.update(countyImage));
    } else {
      this.subscribeToSaveResponse(this.countyImageService.create(countyImage));
    }
  }

  trackCountyRecordById(index: number, item: ICountyRecord): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountyImage>>): void {
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

  protected updateForm(countyImage: ICountyImage): void {
    this.editForm.patchValue({
      id: countyImage.id,
      recordKey: countyImage.recordKey,
      fileSize: countyImage.fileSize,
      fileName: countyImage.fileName,
      pageCnt: countyImage.pageCnt,
      fileDate: countyImage.fileDate ? countyImage.fileDate.format(DATE_TIME_FORMAT) : null,
      filePath: countyImage.filePath,
      md5Hash: countyImage.md5Hash,
      countyRecord: countyImage.countyRecord,
    });

    this.countyRecordsCollection = this.countyRecordService.addCountyRecordToCollectionIfMissing(
      this.countyRecordsCollection,
      countyImage.countyRecord
    );
  }

  protected loadRelationshipsOptions(): void {
    this.countyRecordService
      .query({ 'countyImageId.specified': 'false' })
      .pipe(map((res: HttpResponse<ICountyRecord[]>) => res.body ?? []))
      .pipe(
        map((countyRecords: ICountyRecord[]) =>
          this.countyRecordService.addCountyRecordToCollectionIfMissing(countyRecords, this.editForm.get('countyRecord')!.value)
        )
      )
      .subscribe((countyRecords: ICountyRecord[]) => (this.countyRecordsCollection = countyRecords));
  }

  protected createFromForm(): ICountyImage {
    return {
      ...new CountyImage(),
      id: this.editForm.get(['id'])!.value,
      recordKey: this.editForm.get(['recordKey'])!.value,
      fileSize: this.editForm.get(['fileSize'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      pageCnt: this.editForm.get(['pageCnt'])!.value,
      fileDate: this.editForm.get(['fileDate'])!.value ? dayjs(this.editForm.get(['fileDate'])!.value, DATE_TIME_FORMAT) : undefined,
      filePath: this.editForm.get(['filePath'])!.value,
      md5Hash: this.editForm.get(['md5Hash'])!.value,
      countyRecord: this.editForm.get(['countyRecord'])!.value,
    };
  }
}
