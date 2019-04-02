package utility;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import domain.entity.Employee;
import domain.entity.Employee.Status;
import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;

public class EmployeeFactory {

	public static List<Employee> activeEmployeeListWithId() {
		return Arrays.asList(
				new Employee(1L, "firstName1", 'a', "lastName1", LocalDate.now(), LocalDate.now(), Status.ACTIVE),
				new Employee(2L, "firstName2", 'b', "lastName2", LocalDate.now(), LocalDate.now(), Status.ACTIVE),
				new Employee(3L, "firstName3", 'c', "lastName3", LocalDate.now(), LocalDate.now(), Status.ACTIVE));
	}

	public static List<EmployeeResponse> activeEmployeeResponseListWithId() {
		return activeEmployeeListWithId().stream().map(e -> e.toResponse()).collect(Collectors.toList());
	}

	public static Employee activeEmployeeWithId() {
		return new Employee(4L, "firstName4", 'd', "lastName4", LocalDate.now(), LocalDate.now(), Status.ACTIVE);
	}

	public static Employee activeEmployeeWithoutId() {
		return new Employee(null, "firstName5", 'e', "lastName5", LocalDate.now(), LocalDate.now(), Status.ACTIVE);
	}
	
	public static EmployeeRequest employeeResquest() {
		return new EmployeeRequest("firstName6", 'f', "lastName6", LocalDate.now(), LocalDate.now());
	}

}
