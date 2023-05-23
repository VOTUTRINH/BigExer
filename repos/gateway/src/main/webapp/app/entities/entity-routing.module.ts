import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'country',
        data: { pageTitle: 'gatewayApp.country.home.title' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'department',
        data: { pageTitle: 'gatewayApp.department.home.title' },
        loadChildren: () => import('./department/department.module').then(m => m.DepartmentModule),
      },
      {
        path: 'document',
        data: { pageTitle: 'gatewayApp.document.home.title' },
        loadChildren: () => import('./document/document.module').then(m => m.DocumentModule),
      },
      {
        path: 'document-type',
        data: { pageTitle: 'gatewayApp.documentType.home.title' },
        loadChildren: () => import('./document-type/document-type.module').then(m => m.DocumentTypeModule),
      },
      {
        path: 'employee',
        data: { pageTitle: 'gatewayApp.employee.home.title' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'job',
        data: { pageTitle: 'gatewayApp.job.home.title' },
        loadChildren: () => import('./job/job.module').then(m => m.JobModule),
      },
      {
        path: 'job-history',
        data: { pageTitle: 'gatewayApp.jobHistory.home.title' },
        loadChildren: () => import('./job-history/job-history.module').then(m => m.JobHistoryModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'gatewayApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      {
        path: 'region',
        data: { pageTitle: 'gatewayApp.region.home.title' },
        loadChildren: () => import('./region/region.module').then(m => m.RegionModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
