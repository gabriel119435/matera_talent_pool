package domain.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import domain.dto.BirthAndEmploymentDates;
import domain.request.EmployeeRequest;
import domain.response.EmployeeResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@Entity
public final class Employee {

	// http://www.postgresqltutorial.com/postgresql-serial/
	// https://stackoverflow.com/a/20605392/3026886
	// With this PostgreSQL is able to automatically generate id for new entity
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	private String firstName;

	private Character middleInitial;

	private String lastName;

	private LocalDate dateOfBirth;

	private LocalDate dateOfEmployment;

	public enum Status {
		ACTIVE, INACTIVE
	}

	private Status status;

	
	// changing own properties is its own responsibility
	public void update(EmployeeRequest newOne) {
		this.setFirstName(        newOne.getFirstName() != null        ? newOne.getFirstName()        : this.getFirstName());
		this.setMiddleInitial(    newOne.getMiddleInitial() != null    ? newOne.getMiddleInitial()    : this.getMiddleInitial());
		this.setLastName(         newOne.getLastName() != null         ? newOne.getLastName()         : this.getLastName());
		this.setDateOfBirth(      newOne.getDateOfBirth() != null      ? newOne.getDateOfBirth()      : this.getDateOfBirth());
		this.setDateOfEmployment( newOne.getDateOfEmployment() != null ? newOne.getDateOfEmployment() : this.getDateOfEmployment());
	}
	
	public void changeStatusToInactive() {
		this.status = Status.INACTIVE;
	}

	public BirthAndEmploymentDates getDates() {
		return new BirthAndEmploymentDates(dateOfBirth, dateOfEmployment);
	}
	
	public EmployeeResponse toResponse() {
		return EmployeeResponse.builder()
				.id(id)
				// a full name attribute is more readable than 3 separate attributes
				.fullName(firstName + " " + middleInitial + " " + lastName)
				.dateOfBirth(dateOfBirth)
				.dateOfEmployment(dateOfEmployment)
				// since no inactive employee is returned, no need to show this attribute
				.build();
	}
	
	public Employee copy() {
		return Employee.builder()
					   .id(id)
					   .firstName(firstName)
					   .middleInitial(middleInitial)
					   .lastName(lastName)
					   .dateOfBirth(dateOfBirth)
					   .dateOfEmployment(dateOfEmployment)
					   .status(status)
					   .build();
	}

}
