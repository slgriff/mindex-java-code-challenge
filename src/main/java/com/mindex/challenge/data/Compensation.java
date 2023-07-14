package com.mindex.challenge.data;

import java.util.Objects;

public class Compensation {
	private String employeeId;
	private String salary;
	private String effectiveDate;
	
	public String getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public String getSalary() {
		return salary;
	}
	
	public void setSalary(String salary) {
		this.salary = salary;
	}
	
	public String getEffectiveDate() {
		return effectiveDate;
	}
	
	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	@Override
	public String toString() {
		return "Compensation [employeeId=" + employeeId + ", salary=" + salary + ", effectiveDate=" + effectiveDate
				+ "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(employeeId, salary, effectiveDate);
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
		
		return Objects.equals(employeeId, c.employeeId)
				&& Objects.equals(salary, c.salary)
				&& Objects.equals(effectiveDate, c.effectiveDate);
	}
}
