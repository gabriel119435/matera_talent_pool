package config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

//defining a bean to be used with @Autowired
@Component
@Getter
@Setter
@Validated
// mapping only prefixed values
@ConfigurationProperties(prefix = "custom")

class CustomConfig {

	@NotEmpty
	private List<User> users = new ArrayList<>();
}