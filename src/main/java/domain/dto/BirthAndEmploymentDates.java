package domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString

// used to pass dates to validation, not entire object
public final class BirthAndEmploymentDates {

	private final LocalDate birth;

	private final LocalDate employment;

}
