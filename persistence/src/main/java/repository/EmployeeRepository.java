package repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.entity.Employee;
import domain.entity.Employee.Status;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	//here a default method with fixed Status.ACTIVE could be used, but it was preferred to keep interface without default methods
	
	// query methods https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.query-creation
	
	public List<Employee> findByStatus(Status status);
	
	public Optional<Employee> findByIdAndStatus(Long id, Status status);

}
