package config;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
final class User {

	@NotBlank
	private String username;

	@NotBlank
	private String password;

	private List<String> authorities = new ArrayList<>();
}
