package controller.error;

import java.util.HashMap;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// https://www.baeldung.com/properties-with-spring
@Configuration
@ConfigurationProperties(prefix = "error")
@PropertySource(value = "classpath:errorMessages.properties", encoding = "UTF-8")
@Validated
@Getter
@Setter
@NoArgsConstructor
public class ErrorConfig {

	@NotEmpty
	private HashMap<Long, String> messages = new HashMap<>();

	public String getStandardText(long code) {
		return messages.get(code);
	}
}