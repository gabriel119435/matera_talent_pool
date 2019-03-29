package domain.request;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import domain.dto.BirthAndEmploymentDates;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeRequest {

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

}
