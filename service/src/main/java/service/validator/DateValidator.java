package service.validator;

import org.springframework.stereotype.Service;

import domain.dto.BirthAndEmploymentDates;
import domain.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DateValidator {

	public void validateIfEmploymentIsAfterBirth(BirthAndEmploymentDates dates) {

		log.info(dates.toString());

		if (dates.getEmployment().isBefore(dates.getBirth()))
			throw new BusinessException(100, dates.toString());
	}

}
