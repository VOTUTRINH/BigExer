import { IEmployee } from 'app/entities/employee/employee.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';
import { ILocation } from 'app/entities/location/location.model';

export interface IDepartment {
  id?: number;
  departmentName?: string | null;
  employees?: IEmployee[] | null;
  jobHistories?: IJobHistory[] | null;
  manager?: IEmployee | null;
  location?: ILocation | null;
}

export class Department implements IDepartment {
  constructor(
    public id?: number,
    public departmentName?: string | null,
    public employees?: IEmployee[] | null,
    public jobHistories?: IJobHistory[] | null,
    public manager?: IEmployee | null,
    public location?: ILocation | null
  ) {}
}

export function getDepartmentIdentifier(department: IDepartment): number | undefined {
  return department.id;
}
