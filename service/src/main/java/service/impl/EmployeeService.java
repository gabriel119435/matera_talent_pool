package service.impl;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import business.interfaces.EmployeeBusinessInterface;
import domain.entity.Employee;
import domain.entity.Employee.Status;
import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import service.interfaces.EmployeeServiceInterface;
import service.validator.DateValidator;
import service.validator.EmployeeValidator;

@Slf4j
@Service
public class EmployeeService implements EmployeeServiceInterface {

	@Autowired
	EmployeeBusinessInterface business;
	
	@Autowired
	DateValidator dateValidator;
	
	@Autowired
	EmployeeValidator employeeValidator;

	@Override
	public List<EmployeeResponse> findAll() {
		List<EmployeeResponse> response = business.findAll();
		log.info("findAll returned " + response.size() + " people");
		return response;
	}

	@Override
	public EmployeeResponse getById(long id) {
		log.info("id: " + String.valueOf(id));
		EmployeeResponse response = business.findById(id);
		log.info("response: " + String.valueOf(response));
		return response;
	}

	@Override
	public EmployeeResponse register(EmployeeRequest request) {
		log.info("request: " + String.valueOf(request));
		dateValidator.validateIfEmploymentIsAfterBirth(request.getDates());
		EmployeeResponse response = business.save(requestToEntity(request));
		log.info("response: " + String.valueOf(response));
		return response;
	}
	
	@Override
	public EmployeeResponse update(Long id, @Valid EmployeeRequest request) {
		log.info("id: "      + String.valueOf(id));
		log.info("request: " + String.valueOf(request));
		
		Employee oldEmployee = employeeValidator.validateIfEmployeeExists(id);
		oldEmployee = oldEmployee.update(request);
		dateValidator.validateIfEmploymentIsAfterBirth(oldEmployee.getDates());
		
		// since changing its own attributes is an entity's responsibility, update method was moved to entity
		EmployeeResponse response = business.save(oldEmployee.update(request));
		
		log.info("response: " + String.valueOf(response));
		return response;
	}

	@Override
	public boolean delete(long id) {
		log.info("id: " + String.valueOf(id));
		Employee e = business.findEntityById(id);
		
		if(e==null)
			return false;
		
		// since changing its own attributes is an entity's responsibility, update method was moved to entity
		business.save(e.changeStatusToInactive());
		return true;

	}
	
	// builder pattern using Lombok https://projectlombok.org/
	private Employee requestToEntity(EmployeeRequest e) {
		return Employee.builder()
				.firstName(e.getFirstName())
				.middleInitial(e.getMiddleInitial())
				.lastName(e.getLastName())
				.dateOfBirth(e.getDateOfBirth())
				.dateOfEmployment(e.getDateOfEmployment())
				// requests always insert an active employee
				.status(Status.ACTIVE)
				.build();
	}
	
}
