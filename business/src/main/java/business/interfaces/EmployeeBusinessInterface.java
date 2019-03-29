package business.interfaces;

import java.util.List;

import org.springframework.stereotype.Component;

import domain.entity.Employee;
import domain.response.EmployeeResponse;

@Component
public interface EmployeeBusinessInterface {

	public List<EmployeeResponse> findAll();
	
	public EmployeeResponse findById(long id);
	
	public Employee findEntityById(long id);
	
	public EmployeeResponse save(Employee e);
}
