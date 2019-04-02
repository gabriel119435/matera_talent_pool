package controller.error;

import java.util.HashMap;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

// https://www.baeldung.com/properties-with-spring

// defining a bean to be used with @Autowired
@Component
// mapping only prefixed values
@ConfigurationProperties(prefix = "error")
// pointing source file
@PropertySource(value = "classpath:errorMessages.properties", encoding = "UTF-8")
@Validated
@Getter
@Setter
class ErrorConfig {

	@NotEmpty
	private HashMap<Long, String> messages = new HashMap<>();

	protected String getStandardText(long code) {
		return messages.get(code);
	}
}