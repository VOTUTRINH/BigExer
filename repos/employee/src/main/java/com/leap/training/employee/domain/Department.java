package com.leap.training.employee.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Department.
 */
@Entity
@Table(name = "department")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Department implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "department_name")
    private String departmentName;

    @OneToMany(mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "subEmployees", "jobHistories", "managedDepartments", "job", "manager", "department" },
        allowSetters = true
    )
    private Set<Employee> employees = new HashSet<>();

    @OneToMany(mappedBy = "department")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee", "job", "department" }, allowSetters = true)
    private Set<JobHistory> jobHistories = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(
        value = { "subEmployees", "jobHistories", "managedDepartments", "job", "manager", "department" },
        allowSetters = true
    )
    private Employee manager;

    @ManyToOne
    @JsonIgnoreProperties(value = { "departments", "country" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Department id(Long id) {
        this.id = id;
        return this;
    }

    public String getDepartmentName() {
        return this.departmentName;
    }

    public Department departmentName(String departmentName) {
        this.departmentName = departmentName;
        return this;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Set<Employee> getEmployees() {
        return this.employees;
    }

    public Department employees(Set<Employee> employees) {
        this.setEmployees(employees);
        return this;
    }

    public Department addEmployees(Employee employee) {
        this.employees.add(employee);
        employee.setDepartment(this);
        return this;
    }

    public Department removeEmployees(Employee employee) {
        this.employees.remove(employee);
        employee.setDepartment(null);
        return this;
    }

    public void setEmployees(Set<Employee> employees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setDepartment(null));
        }
        if (employees != null) {
            employees.forEach(i -> i.setDepartment(this));
        }
        this.employees = employees;
    }

    public Set<JobHistory> getJobHistories() {
        return this.jobHistories;
    }

    public Department jobHistories(Set<JobHistory> jobHistories) {
        this.setJobHistories(jobHistories);
        return this;
    }

    public Department addJobHistories(JobHistory jobHistory) {
        this.jobHistories.add(jobHistory);
        jobHistory.setDepartment(this);
        return this;
    }

    public Department removeJobHistories(JobHistory jobHistory) {
        this.jobHistories.remove(jobHistory);
        jobHistory.setDepartment(null);
        return this;
    }

    public void setJobHistories(Set<JobHistory> jobHistories) {
        if (this.jobHistories != null) {
            this.jobHistories.forEach(i -> i.setDepartment(null));
        }
        if (jobHistories != null) {
            jobHistories.forEach(i -> i.setDepartment(this));
        }
        this.jobHistories = jobHistories;
    }

    public Employee getManager() {
        return this.manager;
    }

    public Department manager(Employee employee) {
        this.setManager(employee);
        return this;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public Location getLocation() {
        return this.location;
    }

    public Department location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Department)) {
            return false;
        }
        return id != null && id.equals(((Department) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Department{" +
            "id=" + getId() +
            ", departmentName='" + getDepartmentName() + "'" +
            "}";
    }
}
