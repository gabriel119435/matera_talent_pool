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
	EmployeeRepository repo;

	@Override
	public List<EmployeeResponse> findAll() {
		// used stream and map to change List<Employee> to List<EmployeeResponse>
		return repo.findByStatus(Status.ACTIVE)
				.stream()
				.map(e -> entityToResponse(e))
				.collect(Collectors.toList());
	}

	@Override
	public EmployeeResponse findById(long id) {
		Employee e = findEntityById(id);
		if (e != null)
			return entityToResponse(e);
		return null;
	}
	
	@Override
	public EmployeeResponse save(Employee e) {
		return entityToResponse(repo.save(e));
	}

	@Override
	public Employee findEntityById(long id) {
		Optional<Employee> optional = repo.findByIdAndStatus(id,Status.ACTIVE);
		if (optional.isPresent())
			return optional.get();
		return null;
	}

	private EmployeeResponse entityToResponse(Employee e) {
		return EmployeeResponse.builder()
				.id(e.getId())
				// a full name attribute is more readable than 3 separate attributes
				.fullName(e.getFirstName() + " " + e.getMiddleInitial() + " " + e.getLastName())
				.dateOfBirth(e.getDateOfBirth())
				.dateOfEmployment(e.getDateOfEmployment())
				// since no inactive employee is returned, no need to show this attribute
				.build();
	}

}
