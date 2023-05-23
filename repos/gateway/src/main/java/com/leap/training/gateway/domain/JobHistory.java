package com.leap.training.gateway.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * A JobHistory.
 */
@Table("job_history")
public class JobHistory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

    @Column("start_date")
    private Instant startDate;

    @Column("end_date")
    private Instant endDate;

    @Column("salary")
    private Long salary;

    @JsonIgnoreProperties(
        value = { "subEmployees", "jobHistories", "managedDepartments", "job", "manager", "department" },
        allowSetters = true
    )
    @Transient
    private Employee employee;

    @Column("employee_id")
    private Long employeeId;

    @JsonIgnoreProperties(value = { "employees", "jobHistories" }, allowSetters = true)
    @Transient
    private Job job;

    @Column("job_id")
    private Long jobId;

    @JsonIgnoreProperties(value = { "employees", "jobHistories", "manager", "location" }, allowSetters = true)
    @Transient
    private Department department;

    @Column("department_id")
    private Long departmentId;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public JobHistory id(Long id) {
        this.id = id;
        return this;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public JobHistory startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public JobHistory endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getSalary() {
        return this.salary;
    }

    public JobHistory salary(Long salary) {
        this.salary = salary;
        return this;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public JobHistory employee(Employee employee) {
        this.setEmployee(employee);
        this.employeeId = employee != null ? employee.getId() : null;
        return this;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
        this.employeeId = employee != null ? employee.getId() : null;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employee) {
        this.employeeId = employee;
    }

    public Job getJob() {
        return this.job;
    }

    public JobHistory job(Job job) {
        this.setJob(job);
        this.jobId = job != null ? job.getId() : null;
        return this;
    }

    public void setJob(Job job) {
        this.job = job;
        this.jobId = job != null ? job.getId() : null;
    }

    public Long getJobId() {
        return this.jobId;
    }

    public void setJobId(Long job) {
        this.jobId = job;
    }

    public Department getDepartment() {
        return this.department;
    }

    public JobHistory department(Department department) {
        this.setDepartment(department);
        this.departmentId = department != null ? department.getId() : null;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
        this.departmentId = department != null ? department.getId() : null;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(Long department) {
        this.departmentId = department;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof JobHistory)) {
            return false;
        }
        return id != null && id.equals(((JobHistory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JobHistory{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", salary=" + getSalary() +
            "}";
    }
}
