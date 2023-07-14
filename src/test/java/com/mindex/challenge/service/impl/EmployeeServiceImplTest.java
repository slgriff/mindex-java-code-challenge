package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Compensation;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceImplTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeReportingStructureUrl;
    private String employeeCompensationUrl;
    
    private final String johnLennonEmployeeId = "16a596ae-edd3-4847-99fe-c4518e82c86f";
    private final String paulMcCartneyEmployeeId = "b7839309-3348-463b-a7e3-5de1c168beb3";
    private final String ringoStarrEmployeeId = "03aa1462-ffa9-4978-901b-7c001562cf6f";
    private final String peteBestEmployeeId = "62c1084e-6e34-4630-93fd-9153afb65309";
    private final String georgeHarrisonEmployeeId = "c0c2293d-16bd-4603-8e08-638a9d18b22c";
    
    private final String mickJaggerEmployeeId = "mick-jagger";
    private final String georgeMartinEmployeeId = "george-martin";
    private final String bobDylanEmployeeId = "bob-dylan";

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
        employeeReportingStructureUrl = employeeIdUrl + "/reportingstructure";
        employeeCompensationUrl = employeeIdUrl + "/compensation";
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

        Employee notFoundEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, mickJaggerEmployeeId).getBody();
        
        assertEmployeeEquivalence(new Employee(), notFoundEmployee);
    }
    
    @Test
    public void testReportingStructure() {
    	ReportingStructure johnLennon = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, johnLennonEmployeeId).getBody();
    	assertEquals(4, johnLennon.getNumberOfReports());
    	assertEquals(johnLennonEmployeeId, johnLennon.getEmployee());
    	
    	ReportingStructure paulMcCartney = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, paulMcCartneyEmployeeId).getBody();
    	assertEquals(0, paulMcCartney.getNumberOfReports());
    	assertEquals(paulMcCartneyEmployeeId, paulMcCartney.getEmployee());
    	
    	ReportingStructure ringoStarr = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, ringoStarrEmployeeId).getBody();
    	assertEquals(2, ringoStarr.getNumberOfReports());
    	assertEquals(ringoStarrEmployeeId, ringoStarr.getEmployee());
    	
    	ReportingStructure peteBest = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, peteBestEmployeeId).getBody();
    	assertEquals(0, peteBest.getNumberOfReports());
    	assertEquals(peteBestEmployeeId, peteBest.getEmployee());
    	
    	ReportingStructure georgeHarrison = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, georgeHarrisonEmployeeId).getBody();
    	assertEquals(0, georgeHarrison.getNumberOfReports());
    	assertEquals(georgeHarrisonEmployeeId, georgeHarrison.getEmployee());

    	
    	ReportingStructure mickJagger = restTemplate.getForEntity(employeeReportingStructureUrl, ReportingStructure.class, mickJaggerEmployeeId).getBody();
    	assertEquals(0, mickJagger.getNumberOfReports());
    	assertNull(mickJagger.getEmployee());
    }
    
    @Test
    public void testCompensation() throws ParseException {
    	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	
    	Compensation johnLennonCompensation = new Compensation();
    	johnLennonCompensation.setSalary("10000000000000");
    	johnLennonCompensation.setEffectiveDate(dateFormat.parse("1970-01-01"));
    	
    	Compensation johnLennonCreatedCompensation = restTemplate.postForEntity(employeeCompensationUrl, johnLennonCompensation, Compensation.class, johnLennonEmployeeId).getBody();
    	assertEquals(johnLennonEmployeeId, johnLennonCreatedCompensation.getEmployee());
    	assertCompensationEquivalence(johnLennonCompensation, johnLennonCreatedCompensation);
    	
    	Compensation[] johnLennonReadCompensation = restTemplate.getForEntity(employeeCompensationUrl, Compensation[].class, johnLennonEmployeeId).getBody();
    	assertThat(johnLennonReadCompensation)
    	    .isNotEmpty()
    		.hasSize(1)
    		.contains(johnLennonCreatedCompensation);
    	
    	Compensation newJohnLennonCompensation = new Compensation();
    	newJohnLennonCompensation.setSalary("1");
    	newJohnLennonCompensation.setEffectiveDate(dateFormat.parse("2000-01-01"));
    	
    	Compensation newJohnLennonCreatedCompensation = restTemplate.postForEntity(employeeCompensationUrl, newJohnLennonCompensation, Compensation.class, johnLennonEmployeeId).getBody();
    	assertEquals(johnLennonEmployeeId, newJohnLennonCreatedCompensation.getEmployee());
    	assertCompensationEquivalence(newJohnLennonCompensation, newJohnLennonCreatedCompensation);
    	
    	johnLennonReadCompensation = restTemplate.getForEntity(employeeCompensationUrl, Compensation[].class, johnLennonEmployeeId).getBody();
    	assertThat(johnLennonReadCompensation)
	    	.isNotEmpty()
	    	.hasSize(2)
	    	.contains(johnLennonCreatedCompensation, newJohnLennonCreatedCompensation);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }
    
    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
    	assertEquals(expected.getSalary(), actual.getSalary());
    	assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
    }
}
