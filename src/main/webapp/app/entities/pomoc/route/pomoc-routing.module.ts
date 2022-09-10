import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PomocComponent } from '../list/pomoc.component';

const pomocRoute: Routes = [
  {
    path: '',
    component: PomocComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pomocRoute)],
  exports: [RouterModule],
})
export class PomocRoutingModule {}
