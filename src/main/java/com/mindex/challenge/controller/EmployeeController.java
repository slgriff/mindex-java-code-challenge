package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }
    
    @GetMapping("/{id}/reportingstructure")
    public ReportingStructure getReportingStructure(@PathVariable String id) {
    	LOG.debug("Received reporting structure get request for employee id [{}]", id);
    	
    	int numberOfReports = employeeService.getNumberOfReports(id);
    	
    	return new ReportingStructure(id, numberOfReports);
    }
    
    @PostMapping("/{id}/compensation")
    public Compensation createCompensation(@PathVariable String id, @RequestBody Compensation compensation) {
    	LOG.debug("Received compensation create request for id [{}] and compensation [{}]", id, compensation);
    	
    	compensation.setEmployeeId(id);
    	
    	return employeeService.createCompensation(compensation);
    }
    
    @GetMapping("/{id}/compensation")
    public Compensation getCompensation(@PathVariable String id) {
    	LOG.debug("Received compensation get request for employee id [{}]", id);
    	
    	return employeeService.getCompensation(id);
    }
}
