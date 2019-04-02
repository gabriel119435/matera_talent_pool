package business.employeeBusiness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import business.impl.EmployeeBusiness;
import business.interfaces.EmployeeBusinessInterface;
import domain.entity.Employee;
import domain.entity.Employee.Status;
import domain.response.EmployeeResponse;
import repository.EmployeeRepository;
import utility.EmployeeFactory;

//  https://www.baeldung.com/spring-boot-testing
@RunWith(SpringRunner.class)
public class FindByIdAndFindAllTests {

	// EmployeeBusiness from test context
	@TestConfiguration
	static class EmployeeBusinessTestConfiguration {
		@Bean
		public EmployeeBusinessInterface getBusiness() {
			return new EmployeeBusiness();
		}
	}

	@Autowired
	private EmployeeBusinessInterface business;

	// fake repository
	@MockBean
	private EmployeeRepository repo;

	private final Employee e = EmployeeFactory.activeEmployeeWithId();
	private final List<Employee> list = EmployeeFactory.activeEmployeeListWithId();

	@Before
	public void setUp() {
		// method used by business.findById(id)
		when(repo.findByIdAndStatus(e.getId(), e.getStatus())).thenReturn(Optional.of(e));
		// method used by business.findAll()
		when(repo.findByStatus(Status.ACTIVE)).thenReturn(list);
	}

	@Test
	public void whenfindByIdThenFullNameMustMatch() {
		// given
		final Long id = e.getId();
		final String expectedFullName = e.getFirstName() + " " + e.getMiddleInitial() + " " + e.getLastName();
		// when
		final Optional<EmployeeResponse> opt = business.findById(id);
		// then
		assertTrue("employee not found with id: " + id, opt.isPresent());
		assertEquals(expectedFullName, opt.get().getFullName());
	}
	
	@Test
	public void whenFindAllThenSizeMustMatch() {
		// when
		final List<EmployeeResponse> employeeList = business.findAll();
		// then
		assertTrue("size didn't match: " + employeeList.size(), employeeList.size() == list.size());
	}


}
