package config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
// used on instantiation
@Setter
@NoArgsConstructor

// lauches error if there's no user registered
@Validated

// used by Spring @Autowired
@Component

// reads it from 'custom' key
@ConfigurationProperties(prefix = "custom")

public class CustomConfig {

	@NotEmpty
	private List<User> users = new ArrayList<>();
}