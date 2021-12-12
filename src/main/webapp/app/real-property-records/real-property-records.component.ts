import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CountiesAvaiableService } from '../entities/counties-avaiable/service/counties-avaiable.service';
import { RealPropertyRecordsService } from './service/real-property-records.service';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { CountiesAvaiable, ICountiesAvaiable } from '../entities/counties-avaiable/counties-avaiable.model';
import { ASC, DESC } from '../config/pagination.constants';

@Component({
  selector: 'jhi-real-property-records',
  templateUrl: './real-property-records.component.html',
  styleUrls: ['./real-property-records.component.scss'],
})
export class RealPropertyRecordsComponent implements OnInit {
  isLoading = false;
  countiesAvaiables?: ICountiesAvaiable[];

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal,
    protected realPropertyRecordsService: RealPropertyRecordsService
  ) {}

  ngOnInit(): void {
    this.loadCountiesAvaiable();
    this.isLoading = true;
  }

  loadCountiesAvaiable(): void {
    this.isLoading = true;
    this.realPropertyRecordsService.getAvaiableCountys().subscribe(
      (res: HttpResponse<ICountiesAvaiable[]>) => {
        this.isLoading = false;
        this.onSuccess(res.body, res.headers);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  onCountySelected(countyAvaiable: CountiesAvaiable): void {
    if (countyAvaiable.county) {
      if (countyAvaiable.county.id) {
        this.router.navigate(['real-property-records/' + countyAvaiable.county.id.toString() + '/search'], { state: { countyAvaiable } });
      }
    }
  }
  protected onSuccess(data: ICountiesAvaiable[] | null, headers: HttpHeaders): void {
    this.countiesAvaiables = data ?? [];
    console.log('got em');
    console.log(this.countiesAvaiables);
  }

  protected onError(): void {
    // this.ngbPaginationPage = this.page ?? 1;
  }
}
