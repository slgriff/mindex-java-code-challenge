package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportingStructureUrl;
    
    private final String johnEmployeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private final String paulEmployeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";
    private final String ringoEmployeeId = "03aa1462-ffa9-4978-901b-7c001562cf6f";
    private final String peteEmployeeId = "62c1084e-6e34-4630-93fd-9153afb65309";
    private final String georgeEmployeeId = "c0c2293d-16bd-4603-8e08-638a9d18b22c";

    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
    	String baseUrl = "http://localhost:" + port;
        employeeUrl = baseUrl + "/employee";
        employeeIdUrl = employeeUrl + "/{id}";
        employeeReportingStructureUrl = employeeIdUrl + "/reportingstructure/";
    }

    @Test
    public void testCreateReadUpdate() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");

        // Create checks
        Employee createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();

        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEmployeeEquivalence(createdEmployee, readEmployee);


        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
    }
    
    @Test
    public void testReportingStructure() {
    	ReportingStructure john = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, johnEmployeeId).getBody();
    	assertEquals(4, john.getNumberOfReports());
    	
    	ReportingStructure paul = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, paulEmployeeId).getBody();
    	assertEquals(0, paul.getNumberOfReports());
    	
    	ReportingStructure ringo = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, ringoEmployeeId).getBody();
    	assertEquals(2, ringo.getNumberOfReports());
    	
    	ReportingStructure pete = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, peteEmployeeId).getBody();
    	assertEquals(0, pete.getNumberOfReports());
    	
    	ReportingStructure george = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, georgeEmployeeId).getBody();
    	assertEquals(0, george.getNumberOfReports());
    	
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
}
