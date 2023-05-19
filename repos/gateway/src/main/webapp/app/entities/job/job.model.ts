import { IEmployee } from 'app/entities/employee/employee.model';
import { IJobHistory } from 'app/entities/job-history/job-history.model';

export interface IJob {
  id?: number;
  jobTitle?: string | null;
  minSalary?: number | null;
  maxSalary?: number | null;
  employees?: IEmployee[] | null;
  jobHistories?: IJobHistory[] | null;
}

export class Job implements IJob {
  constructor(
    public id?: number,
    public jobTitle?: string | null,
    public minSalary?: number | null,
    public maxSalary?: number | null,
    public employees?: IEmployee[] | null,
    public jobHistories?: IJobHistory[] | null
  ) {}
}

export function getJobIdentifier(job: IJob): number | undefined {
  return job.id;
}
