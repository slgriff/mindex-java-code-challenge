package com.mindex.challenge.data;

import java.util.Date;
import java.util.Objects;

public class Compensation {
	private String employee;
	private String salary;
	private Date effectiveDate;
	
	public String getEmployee() {
		return employee;
	}
	
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	
	public String getSalary() {
		return salary;
	}
	
	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	public Date getEffectiveDate() {
		return effectiveDate;
	}
	
	public void setEffectiveDate(Date effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public String toString() {
		return "Compensation [employee=" + employee + ", salary=" + salary + ", effectiveDate=" + effectiveDate
				+ "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(employee, salary, effectiveDate);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		
		if (o == this) {
			return true;
		}
		
		if (!(o instanceof Compensation)) {
			return false;
		}
		
		Compensation c = (Compensation) o;
		
		return Objects.equals(employee, c.employee)
				&& Objects.equals(salary, c.salary)
				&& Objects.equals(effectiveDate, c.effectiveDate);
	}
}
