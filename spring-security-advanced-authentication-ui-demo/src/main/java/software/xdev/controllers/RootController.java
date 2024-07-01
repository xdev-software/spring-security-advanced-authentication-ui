package software.xdev.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
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
		if(SecurityContextHolder.getContext()
			.getAuthentication()
			.getPrincipal() instanceof final DefaultOidcUser oidcUser)
		{
			return new Result(oidcUser.getFullName(), oidcUser.getEmail(), "/logout");
		}
		return null;
	}
	
	public record Result(
		String name,
		String email,
		String logoutUrl
	)
	{
	
	}
}
