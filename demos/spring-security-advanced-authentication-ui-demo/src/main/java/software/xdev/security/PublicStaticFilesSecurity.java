package software.xdev.security;

import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class PublicStaticFilesSecurity
{
	static final Set<String> PUBLIC_STATIC_FILES = Set.of(
		"/favicon.ico",
		"/assets/**",
		"/lib/**");
	
	@Bean
	@Order(2)
	public SecurityFilterChain configureStaticResources(final HttpSecurity http) throws Exception
	{
		// Static resources that require no authentication
		return http
			.securityMatcher(PUBLIC_STATIC_FILES.toArray(String[]::new))
			.authorizeHttpRequests(a -> a.anyRequest().permitAll())
			.sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.build();
	}
}
