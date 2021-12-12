import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { ICountyImagePage, CountyImagePage } from '../county-image-page.model';
import { CountyImagePageService } from '../service/county-image-page.service';
import { ICountyRecord } from 'app/entities/county-record/county-record.model';
import { CountyRecordService } from 'app/entities/county-record/service/county-record.service';
import { ICountyImage } from 'app/entities/county-image/county-image.model';
import { CountyImageService } from 'app/entities/county-image/service/county-image.service';

@Component({
  selector: 'jhi-county-image-page-update',
  templateUrl: './county-image-page-update.component.html',
})
export class CountyImagePageUpdateComponent implements OnInit {
  isSaving = false;

  countyRecordsSharedCollection: ICountyRecord[] = [];
  countyImagesSharedCollection: ICountyImage[] = [];

  editForm = this.fb.group({
    id: [],
    recordKey: [null, [Validators.required]],
    fileSize: [],
    pageNo: [],
    fileName: [],
    fileDate: [],
    filePath: [],
    ocrScore: [],
    md5Hash: [],
    countyRecord: [],
    countyImage: [],
  });

  constructor(
    protected countyImagePageService: CountyImagePageService,
    protected countyRecordService: CountyRecordService,
    protected countyImageService: CountyImageService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ countyImagePage }) => {
      if (countyImagePage.id === undefined) {
        const today = dayjs().startOf('day');
        countyImagePage.fileDate = today;
      }

      this.updateForm(countyImagePage);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const countyImagePage = this.createFromForm();
    if (countyImagePage.id !== undefined) {
      this.subscribeToSaveResponse(this.countyImagePageService.update(countyImagePage));
    } else {
      this.subscribeToSaveResponse(this.countyImagePageService.create(countyImagePage));
    }
  }

  trackCountyRecordById(index: number, item: ICountyRecord): number {
    return item.id!;
  }

  trackCountyImageById(index: number, item: ICountyImage): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICountyImagePage>>): void {
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

  protected updateForm(countyImagePage: ICountyImagePage): void {
    this.editForm.patchValue({
      id: countyImagePage.id,
      recordKey: countyImagePage.recordKey,
      fileSize: countyImagePage.fileSize,
      pageNo: countyImagePage.pageNo,
      fileName: countyImagePage.fileName,
      fileDate: countyImagePage.fileDate ? countyImagePage.fileDate.format(DATE_TIME_FORMAT) : null,
      filePath: countyImagePage.filePath,
      ocrScore: countyImagePage.ocrScore,
      md5Hash: countyImagePage.md5Hash,
      countyRecord: countyImagePage.countyRecord,
      countyImage: countyImagePage.countyImage,
    });

    this.countyRecordsSharedCollection = this.countyRecordService.addCountyRecordToCollectionIfMissing(
      this.countyRecordsSharedCollection,
      countyImagePage.countyRecord
    );
    this.countyImagesSharedCollection = this.countyImageService.addCountyImageToCollectionIfMissing(
      this.countyImagesSharedCollection,
      countyImagePage.countyImage
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

    this.countyImageService
      .query()
      .pipe(map((res: HttpResponse<ICountyImage[]>) => res.body ?? []))
      .pipe(
        map((countyImages: ICountyImage[]) =>
          this.countyImageService.addCountyImageToCollectionIfMissing(countyImages, this.editForm.get('countyImage')!.value)
        )
      )
      .subscribe((countyImages: ICountyImage[]) => (this.countyImagesSharedCollection = countyImages));
  }

  protected createFromForm(): ICountyImagePage {
    return {
      ...new CountyImagePage(),
      id: this.editForm.get(['id'])!.value,
      recordKey: this.editForm.get(['recordKey'])!.value,
      fileSize: this.editForm.get(['fileSize'])!.value,
      pageNo: this.editForm.get(['pageNo'])!.value,
      fileName: this.editForm.get(['fileName'])!.value,
      fileDate: this.editForm.get(['fileDate'])!.value ? dayjs(this.editForm.get(['fileDate'])!.value, DATE_TIME_FORMAT) : undefined,
      filePath: this.editForm.get(['filePath'])!.value,
      ocrScore: this.editForm.get(['ocrScore'])!.value,
      md5Hash: this.editForm.get(['md5Hash'])!.value,
      countyRecord: this.editForm.get(['countyRecord'])!.value,
      countyImage: this.editForm.get(['countyImage'])!.value,
    };
  }
}
