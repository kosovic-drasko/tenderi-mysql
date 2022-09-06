import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PrvorangiraniComponent } from './list/prvorangirani.component';
import { PrvorangiraniDetailComponent } from './detail/prvorangirani-detail.component';
import { PrvorangiraniRoutingModule } from './route/prvorangirani-routing.module';
import { MatTableExporterModule } from 'mat-table-exporter';
import { MatSortModule } from '@angular/material/sort';
import { MatSidenavModule } from '@angular/material/sidenav';
import { JhMaterialModule } from '../../shared/jh-material.module';

@NgModule({
  imports: [SharedModule, PrvorangiraniRoutingModule, MatTableExporterModule, MatSortModule, MatSidenavModule, JhMaterialModule],
  declarations: [PrvorangiraniComponent, PrvorangiraniDetailComponent],
  exports: [PrvorangiraniComponent],
})
export class PrvorangiraniModule {}
