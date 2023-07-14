package com.mindex.challenge.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.mindex.challenge.data.Compensation;

import java.util.List;

public interface CompensationRepository extends MongoRepository<Compensation, String> {
	List<Compensation> findByEmployeeId(String employeeId);

}
