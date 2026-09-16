package software.xdev.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class RootController
{
	@GetMapping
	public Result respond()
	{
		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return new Result(
			"/logout",
			"/webauthn/register",
			authentication != null ? authentication.getClass().getName() : null,
			authentication);
	}
	
	public record Result(
		String logoutUrl,
		String passKeyRegistrationUrl,
		String authenticationClass,
		Object authentication
	)
	{
	
	}
}
