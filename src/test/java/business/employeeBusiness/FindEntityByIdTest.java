package business.employeeBusiness;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

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
import domain.response.EmployeeResponse;
import repository.EmployeeRepository;
import utility.EmployeeFactory;

//https://www.baeldung.com/spring-boot-testing
@RunWith(SpringRunner.class)
public class FindEntityByIdTest {

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

	@Before
	public void setUp() {
		// since this mocks same method as FindByIdAndFindAllTests, it should be a separate class test with different test behavior
		when(repo.findByIdAndStatus(e.getId(), e.getStatus())).thenReturn(Optional.of(e));
	}

	@Test
	public void whenFindAll_ThenSizeMustBeCorrect() {
		// given
		final EmployeeResponse expectedResponse = e.toResponse();

		// when
		final Optional<Employee> opt = business.findEntityById(e.getId());

		// then
		assertTrue("employee not found with id: " + e.getId(), opt.isPresent());
		assertEquals(expectedResponse, opt.get().toResponse());
	}

}