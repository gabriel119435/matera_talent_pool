package domain.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class BirthAndEmploymentDates {

	LocalDate birth;
	LocalDate employment;

}
