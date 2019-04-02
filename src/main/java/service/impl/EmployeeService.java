package service.impl;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import business.interfaces.EmployeeBusinessInterface;
import domain.entity.Employee;
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
	private EmployeeBusinessInterface business;
	
	@Autowired
	private DateValidator dateValidator;
	
	@Autowired
	private EmployeeValidator employeeValidator;

	@Override
	public List<EmployeeResponse> findAll() {
		final List<EmployeeResponse> response = business.findAll();
		log.info("findAll returned " + response.size() + " people");
		return response;
	}

	@Override
	public Optional<EmployeeResponse> getById(long id) {
		log.info("id: " + String.valueOf(id));
		final Optional<EmployeeResponse> opt = business.findById(id);
		opt.ifPresent(e -> log.info(String.valueOf(e)));
		return opt;
	}

	@Override
	public EmployeeResponse register(EmployeeRequest request) {
		log.info("request: " + String.valueOf(request));
		dateValidator.validateIfEmploymentIsAfterBirth(request.getDates());
		final EmployeeResponse response = business.save(request.toEntity());
		log.info("response: " + String.valueOf(response));
		return response;
	}
	
	@Override
	public EmployeeResponse update(Long id, @Valid EmployeeRequest request) {
		log.info("id: "      + String.valueOf(id));
		log.info("request: " + String.valueOf(request));
		
		final Employee e = employeeValidator.validateIfEmployeeExists(id);
		
		// since changing its own attributes is an entity's responsibility, update method was moved to entity
		e.update(request);
		dateValidator.validateIfEmploymentIsAfterBirth(e.getDates());
		
		final EmployeeResponse response = business.save(e);
		
		log.info("response: " + String.valueOf(response));
		return response;
	}

	@Override
	public void delete(long id) {
		log.info("id: " + String.valueOf(id));
		final Optional<Employee> opt = business.findEntityById(id);
		// since changing its own attributes is an entity's responsibility, update method was moved to entity
		opt.ifPresent(e -> { e.changeStatusToInactive();
							 business.save(e);});
	}
	
}
