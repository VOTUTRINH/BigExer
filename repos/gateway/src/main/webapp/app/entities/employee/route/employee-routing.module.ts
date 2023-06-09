import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EmployeeComponent } from '../list/employee.component';
import { EmployeeDetailComponent } from '../detail/employee-detail.component';
import { EmployeeUpdateComponent } from '../update/employee-update.component';
import { EmployeeRoutingResolveService } from './employee-routing-resolve.service';

import { DocumentComponent } from '../../document/list/document.component';

const employeeRoute: Routes = [
  {
    path: '',
    component: EmployeeComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmployeeDetailComponent,
    resolve: {
      employee: EmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmployeeUpdateComponent,
    resolve: {
      employee: EmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmployeeUpdateComponent,
    resolve: {
      employee: EmployeeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/document',
    component: DocumentComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeeRoute)],
  exports: [RouterModule],
})
export class EmployeeRoutingModule {}
