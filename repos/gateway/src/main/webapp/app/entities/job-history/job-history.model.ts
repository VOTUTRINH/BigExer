import * as dayjs from 'dayjs';
import { IEmployee } from 'app/entities/employee/employee.model';
import { IJob } from 'app/entities/job/job.model';
import { IDepartment } from 'app/entities/department/department.model';

export interface IJobHistory {
  id?: number;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  salary?: number | null;
  employee?: IEmployee | null;
  job?: IJob | null;
  department?: IDepartment | null;
}

export class JobHistory implements IJobHistory {
  constructor(
    public id?: number,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public salary?: number | null,
    public employee?: IEmployee | null,
    public job?: IJob | null,
    public department?: IDepartment | null
  ) {}
}

export function getJobHistoryIdentifier(jobHistory: IJobHistory): number | undefined {
  return jobHistory.id;
}
