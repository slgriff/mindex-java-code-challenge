package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.exception.EmployeeNotFoundException;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Getting employee with id [{}]", id);

        return getEmployee(id);
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }
    
    @Override
    public int getNumberOfReports(String id) {
    	LOG.debug("Getting number of reports for employee id [{}]", id);
    	
    	Employee employee = getEmployee(id);
    	
    	return countNumberOfReports(employee);
    }
    
    private Employee getEmployee(String id) {
    	Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new EmployeeNotFoundException("Invalid employeeId: " + id);
        }

        return employee;
    }
    
    private int countNumberOfReports(Employee employee) {
    	int count = 0;
    	
    	List<Employee> reports = employee.getDirectReports();
    	
    	if (reports != null && reports.size() > 0) {
    		count += reports.size();
    		
    		for (Employee report : reports) {
    			count += countNumberOfReports(report);
    		}
    	}
    	
    	return count;
    }
}
