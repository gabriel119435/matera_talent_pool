package service.validator;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import domain.entity.Employee;
import domain.entity.Employee.Status;
import domain.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import repository.EmployeeRepository;

@Slf4j
@Service
public class EmployeeValidator {

	@Autowired
	public EmployeeRepository repo;

	public Employee validateIfEmployeeExists(long id) {

		log.info(String.valueOf(id));

		Optional<Employee> optional = repo.findByIdAndStatus(id, Status.ACTIVE);

		if (optional.isPresent())
			return optional.get();
		else
			throw new BusinessException(1L, "id: " + id);
	}

}
