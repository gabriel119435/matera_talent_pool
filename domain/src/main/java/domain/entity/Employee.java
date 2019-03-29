package domain.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import domain.dto.BirthAndEmploymentDates;
import domain.request.EmployeeRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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

	public Employee update(EmployeeRequest newOne) {
		this.setFirstName(        newOne.getFirstName() != null        ? newOne.getFirstName()        : this.getFirstName());
		this.setMiddleInitial(    newOne.getMiddleInitial() != null    ? newOne.getMiddleInitial()    : this.getMiddleInitial());
		this.setLastName(         newOne.getLastName() != null         ? newOne.getLastName()         : this.getLastName());
		this.setDateOfBirth(      newOne.getDateOfBirth() != null      ? newOne.getDateOfBirth()      : this.getDateOfBirth());
		this.setDateOfEmployment( newOne.getDateOfEmployment() != null ? newOne.getDateOfEmployment() : this.getDateOfEmployment());
		return this;
	}
	
	public Employee changeStatusToInactive() {
		this.status = Status.INACTIVE;
		return this;
	}

	public BirthAndEmploymentDates getDates() {
		return new BirthAndEmploymentDates(dateOfBirth, dateOfEmployment);
	}

}
