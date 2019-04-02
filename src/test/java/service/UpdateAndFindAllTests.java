package service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import business.interfaces.EmployeeBusinessInterface;
import domain.entity.Employee;
import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;
import service.impl.EmployeeService;
import service.interfaces.EmployeeServiceInterface;
import service.validator.DateValidator;
import service.validator.EmployeeValidator;
import utility.EmployeeFactory;

@RunWith(SpringRunner.class)
public class UpdateAndFindAllTests {

	// EmployeeService from test context
	@TestConfiguration
	static class EmployeeServiceTestConfiguration {
		@Bean
		public EmployeeServiceInterface getService() {
			return new EmployeeService();
		}
	}

	@Autowired
	private EmployeeServiceInterface service;
	
	// since multiple level of mocks defeat the purpose of mocking, only one layer below was mocked
	// https://stackoverflow.com/questions/6300439/multiple-levels-of-mock-and-injectmocks
	// all dependencies should be mocked

	@MockBean
	private EmployeeBusinessInterface business;

	@MockBean
	private DateValidator dateValidator;

	@MockBean
	private EmployeeValidator employeeValidator;

	private final Employee employee = EmployeeFactory.activeEmployeeWithId();
	
	private final String firstNameUpdated = "updatedfirstName";
	private final String lastNameUpdated = "lastNameUpdated";
	private final Character middleInitialUpdated = 't';
	private final EmployeeRequest request = EmployeeRequest.builder()
														   .firstName(firstNameUpdated)
														   .lastName(lastNameUpdated)
														   .middleInitial(middleInitialUpdated).build();

	@Before
	public void setUp() {
		// dictates fake dependencies behavior called on service.update
		when(employeeValidator.validateIfEmployeeExists(employee.getId())).thenReturn(employee);
		Mockito.doNothing().when(dateValidator).validateIfEmploymentIsAfterBirth(employee.getDates());
		when(business.findAll()).thenReturn(EmployeeFactory.activeEmployeeResponseListWithId());
		
		// updateEmployee will have updated values
		final Employee updatedEmployee = employee.copy();
		updatedEmployee.update(request);
		when(business.save(updatedEmployee)).thenReturn(updatedEmployee.toResponse());
	}

	@Test
	public void whenUpdate_ThenAttributesShouldChange() {
		// when
		EmployeeResponse response = service.update(employee.getId(), request);
		// then
		assertEquals(response.getFullName(),
				request.getFirstName() + " " + request.getMiddleInitial() + " " + request.getLastName());
	}
	
	@Test
	public void whenFindAll_ThenSizeMustBeCorrect() {
		// when
		final List<EmployeeResponse> list = service.findAll();
		// then
		assertTrue("size didn't match: " + list.size(),
				EmployeeFactory.activeEmployeeListWithId().size() == list.size());
	}
}
