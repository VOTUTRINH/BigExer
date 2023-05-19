import * as dayjs from 'dayjs';
import { IJobHistory } from 'app/entities/job-history/job-history.model';
import { IDepartment } from 'app/entities/department/department.model';
import { IJob } from 'app/entities/job/job.model';

export interface IEmployee {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  email?: string | null;
  phoneNumber?: string | null;
  hireDate?: dayjs.Dayjs | null;
  salary?: number | null;
  commissionPct?: number | null;
  subEmployees?: IEmployee[] | null;
  jobHistories?: IJobHistory[] | null;
  managedDepartments?: IDepartment[] | null;
  job?: IJob | null;
  manager?: IEmployee | null;
  department?: IDepartment | null;
}

export class Employee implements IEmployee {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public email?: string | null,
    public phoneNumber?: string | null,
    public hireDate?: dayjs.Dayjs | null,
    public salary?: number | null,
    public commissionPct?: number | null,
    public subEmployees?: IEmployee[] | null,
    public jobHistories?: IJobHistory[] | null,
    public managedDepartments?: IDepartment[] | null,
    public job?: IJob | null,
    public manager?: IEmployee | null,
    public department?: IDepartment | null
  ) {}
}

export function getEmployeeIdentifier(employee: IEmployee): number | undefined {
  return employee.id;
}
