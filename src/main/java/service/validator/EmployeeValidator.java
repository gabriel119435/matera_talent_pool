package service.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.entity.Employee;
import domain.entity.Employee.Status;
import domain.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import repository.EmployeeRepository;

@Slf4j
@Service
public class EmployeeValidator {

	@Autowired
	private EmployeeRepository repo;

	public Employee validateIfEmployeeExists(long id) {

		log.info(String.valueOf(id));

		final Optional<Employee> optional = repo.findByIdAndStatus(id, Status.ACTIVE);

		return optional.map(o -> o)
	      .orElseThrow(() -> new EntityNotFoundException("id: " + id));
	}

}
