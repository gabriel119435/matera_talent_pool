package domain.error;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor

//entity returned when an error occurs
public class GenericError {

	//codes and standardMessage are at controller/src/main/resources/errorMesssages.properties
	private Long code;
	private String standardMessage;
	private List<String> customMessage;

}
