import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PomocComponent } from './list/pomoc.component';
import { PomocRoutingModule } from './route/pomoc-routing.module';

@NgModule({
  imports: [SharedModule, PomocRoutingModule],
  declarations: [PomocComponent],
})
export class PomocModule {}
