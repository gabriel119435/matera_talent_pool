package business.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import business.interfaces.EmployeeBusinessInterface;
import domain.entity.Employee;
import domain.entity.Employee.Status;
import domain.response.EmployeeResponse;
import repository.EmployeeRepository;

@Component
public class EmployeeBusiness implements EmployeeBusinessInterface {

	@Autowired
	private EmployeeRepository repo;

	@Override
	public List<EmployeeResponse> findAll() {
		// used stream and map to change List<Employee> to List<EmployeeResponse>
		return repo.findByStatus(Status.ACTIVE)
				.stream()
				.map(e -> e.toResponse())
				.collect(Collectors.toList());
	}

	@Override
	public Optional<EmployeeResponse> findById(long id) {
		return entityToResponse(findEntityById(id));
	}

	@Override
	public EmployeeResponse save(Employee e) {
		return (repo.save(e)).toResponse();
	}

	@Override
	public Optional<Employee> findEntityById(long id) {
		return repo.findByIdAndStatus(id,Status.ACTIVE);
	}

	private Optional<EmployeeResponse> entityToResponse(Optional<Employee> opt) {
		return opt.map(e -> e.toResponse());
	}

}
