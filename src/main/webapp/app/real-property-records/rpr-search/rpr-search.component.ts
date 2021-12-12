import { Component, OnInit } from '@angular/core';

interface DateSearchParam {
  name: string;
}

@Component({
  selector: 'jhi-rpr-search',
  templateUrl: './rpr-search.component.html',
  styleUrls: ['./rpr-search.component.scss'],
})
export class RprSearchComponent implements OnInit {
  constructor() {}

  ngOnInit(): void {}
}
