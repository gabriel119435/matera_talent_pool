package repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import domain.entity.Employee;
import utility.EmployeeFactory;

// https://stackoverflow.com/a/43659328/3026886
@ContextConfiguration(classes = app.Application.class)
// https://www.baeldung.com/spring-boot-testing
@RunWith(SpringRunner.class)
@DataJpaTest
public class GenericRepositoryTests {

	@Autowired
	private EmployeeRepository repo;

	@Test
	public void whenInsertAndFindById_ThenReturnEmployee() {
		// given
		final Employee e = repo.save(EmployeeFactory.activeEmployeeWithoutId());
		// when
		final Optional<Employee> opt = repo.findById(e.getId());
		// then
		assertTrue("employee not found with id: " + e.getId(), opt.isPresent());
		assertEquals(e, opt.get());
	}

	@Test
	public void whenDeleteAll_ThenReturnNothing() {
		// given
		repo.deleteAll();
		// when
		final List<Employee> list = repo.findAll();
		// then
		assertTrue("list not empty", list.size() == 0);
	}

}
