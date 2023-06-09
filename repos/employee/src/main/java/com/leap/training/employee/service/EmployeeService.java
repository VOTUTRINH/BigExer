package com.leap.training.employee.service;

import com.leap.training.employee.domain.Employee;
import com.leap.training.employee.domain.JobHistory;
import com.leap.training.employee.repository.EmployeeRepository;

import java.time.Instant;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeService.class);

    private final EmployeeRepository employeeRepository;
    private final JobHistoryService jobHistoryService;

    public EmployeeService(EmployeeRepository employeeRepository,JobHistoryService jobHistoryService) {
        this.employeeRepository = employeeRepository;
        this.jobHistoryService = jobHistoryService;
    }


    /**
     * Save a employee.
     *
     * @param employee the entity to save.
     * @return the persisted entity.
     */
    public Employee save(Employee employee) {
        log.debug("Request to save Employee : {}", employee);
        if(employee.getId() != null){ //edit

            Optional<Employee> temp = employeeRepository.findById(employee.getId());
            Employee oldEmployee = temp.isPresent() ? temp.get() : null; 
            boolean isChanged = false;
            //tracking changes
            if(employee.getJob() != null && !employee.getJob().equals(oldEmployee.getJob()) ) {
                    isChanged=true;
            }
            if(employee.getDepartment() != null && !employee.getDepartment().equals(oldEmployee.getDepartment())) {
                    isChanged =true;
            }
            if(!employee.getSalary().equals(oldEmployee.getSalary())){
                isChanged =true;
            }
            //add to jobhistories
            if(oldEmployee != null && ( isChanged == true  )){
                JobHistory jobHistory = new JobHistory();
                jobHistory.setStartDate(oldEmployee.getHireDate());
                jobHistory.setEndDate(Instant.now());
                jobHistory.setJob(oldEmployee.getJob());
                jobHistory.setDepartment(oldEmployee.getDepartment());
                jobHistory.setSalary(oldEmployee.getSalary());
                jobHistory.setEmployee(oldEmployee);
                jobHistoryService.save(jobHistory);
            }
        }
       
        return employeeRepository.save(employee);
    }

    /**
     * Partially update a employee.
     *
     * @param employee the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Employee> partialUpdate(Employee employee) {
        log.debug("Request to partially update Employee : {}", employee);

        return employeeRepository
            .findById(employee.getId())
            .map(
                existingEmployee -> {
                    if (employee.getFirstName() != null) {
                        existingEmployee.setFirstName(employee.getFirstName());
                    }
                    if (employee.getLastName() != null) {
                        existingEmployee.setLastName(employee.getLastName());
                    }
                    if (employee.getEmail() != null) {
                        existingEmployee.setEmail(employee.getEmail());
                    }
                    if (employee.getPhoneNumber() != null) {
                        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
                    }
                    if (employee.getHireDate() != null) {
                        existingEmployee.setHireDate(employee.getHireDate());
                    }
                    if (employee.getSalary() != null) {
                        existingEmployee.setSalary(employee.getSalary());
                    }
                    if (employee.getCommissionPct() != null) {
                        existingEmployee.setCommissionPct(employee.getCommissionPct());
                    }

                    return existingEmployee;
                }
            )
            .map(employeeRepository::save);
    }

    /**
     * Get all the employees.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Employee> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable);
    }

    /**
     * Get one employee by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Employee> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findById(id);
    }

    /**
     * Delete the employee by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}
