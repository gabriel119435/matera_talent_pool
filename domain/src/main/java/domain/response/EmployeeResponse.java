package domain.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse {

	private long id;

	private String fullName;

	private LocalDate dateOfBirth;

	private LocalDate dateOfEmployment;

}
