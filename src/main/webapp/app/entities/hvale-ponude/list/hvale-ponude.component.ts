import { AfterViewInit, Component, Input, OnChanges, OnInit, ViewChild } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { IHvalePonude } from '../hvale-ponude.model';
import { HvalePonudeService } from '../service/hvale-ponude.service';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';

@Component({
  selector: 'jhi-hvale-ponude',
  templateUrl: './hvale-ponude.component.html',
  styleUrls: ['./hvale-ponude.component.scss'],
})
export class HvalePonudeComponent implements AfterViewInit, OnChanges {
  hvalePonudes?: any;
  ukupnaProcijenjena?: number | null | undefined;
  isLoading = false;
  public displayedColumns = [
    'sifra postupka',
    'broj partije',
    'inn',
    'farmaceutski oblik',
    'pakovanje',
    'kolicina',
    'procijenjena vrijednost',
  ];

  public dataSource = new MatTableDataSource<IHvalePonude>();
  sifraPostupka?: any;
  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @Input() postupak: any;

  constructor(protected hvaleService: HvalePonudeService) {}

  public getSifraHvali(): void {
    this.hvaleService.hvali(this.postupak).subscribe((res: IHvalePonude[]) => {
      this.dataSource.data = res;
      this.hvalePonudes = res;
      this.getTotalProcijenjena();
    });
  }

  ngAfterViewInit(): void {
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  public doFilter = (value: string): any => {
    this.dataSource.filter = value.trim().toLocaleLowerCase();
  };

  getTotalProcijenjena(): any {
    return (this.ukupnaProcijenjena = this.dataSource.filteredData
      .map(t => t.procijenjenaVrijednost)
      .reduce((acc, value) => acc! + value!, 0));
  }

  ngOnChanges(): void {
    this.getSifraHvali();
  }
}
