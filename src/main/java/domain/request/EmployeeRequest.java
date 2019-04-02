package domain.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import domain.dto.BirthAndEmploymentDates;
import domain.entity.Employee;
import domain.entity.Employee.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public final class EmployeeRequest {

	@NotNull
	private String firstName;

	@NotNull
	private Character middleInitial;

	@NotNull
	private String lastName;

	@NotNull
	private LocalDate dateOfBirth;

	@NotNull
	private LocalDate dateOfEmployment;

	public BirthAndEmploymentDates getDates() {
		return new BirthAndEmploymentDates(dateOfBirth, dateOfEmployment);
	}
	
	public Employee toEntity() {
		return Employee.builder()
				.firstName(firstName)
				.middleInitial(middleInitial)
				.lastName(lastName)
				.dateOfBirth(dateOfBirth)
				.dateOfEmployment(dateOfEmployment)
				// requests always insert an active employee
				.status(Status.ACTIVE).build();
	}
}
