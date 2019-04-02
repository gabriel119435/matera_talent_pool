package config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	// https://www.baeldung.com/spring-boot-internationalization
	@Bean
	LocaleResolver localeResolver() {
		// force english for Spring Security error messages
		return new FixedLocaleResolver(Locale.ENGLISH);
	}
	
	@Autowired
	private CustomConfig config;
	
	//used to avoid "Using generated security password: 5f2511ac-66c6-4538-a777-8fb6aa7a0b59" being printed https://stackoverflow.com/a/41856630/3026886
	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> authManagerBuilder = auth.inMemoryAuthentication();
		// adds all users registered
		for (User u : config.getUsers()) {
			// since authManagerBuilder cannot be final, I couldn't use stream api
			authManagerBuilder = authManagerBuilder
										.withUser(u.getUsername())
								   		// authn: to be authenticated user must know its password
					               		.password("{noop}" + u.getPassword())
					               		// authz: this user can only consume specified services
					               		.authorities(u.getAuthorities().toArray(new String[0]))
					               		// used to keep builder pattern to next users!
					               		.and();
		}
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()                      // disables csrf to allow post/delete/etc since threre's no web pages
		.authorizeRequests()
	        .anyRequest().authenticated()      // authn: all APIs should have an authenticated user. To be authenticated, the user must know its own password
	    .and().httpBasic()
	    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		}
	
}