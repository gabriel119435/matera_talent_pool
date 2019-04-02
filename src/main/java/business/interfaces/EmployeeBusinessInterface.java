package business.interfaces;

import java.util.List;
import java.util.Optional;

import domain.entity.Employee;
import domain.response.EmployeeResponse;

public interface EmployeeBusinessInterface {

	List<EmployeeResponse> findAll();
	
	Optional<EmployeeResponse> findById(long id);
	
	Optional<Employee> findEntityById(long id);
	
	EmployeeResponse save(Employee e);
}
