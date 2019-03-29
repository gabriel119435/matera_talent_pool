package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

@SpringBootApplication(scanBasePackages = { "config", "controller", "service", "business" })
@EntityScan(basePackages = { "domain" })
@EnableJpaRepositories(basePackages = { "repository" })

//allows @PreAuthorize("hasAuthority('someAuthority')") to be used
@EnableGlobalMethodSecurity(prePostEnabled = true)

public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
