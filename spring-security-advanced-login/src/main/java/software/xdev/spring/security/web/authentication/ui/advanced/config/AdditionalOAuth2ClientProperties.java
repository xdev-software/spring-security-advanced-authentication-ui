package software.xdev.spring.security.web.authentication.ui.advanced.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import software.xdev.spring.security.web.authentication.ui.advanced.AdditionalRegistrationData;


@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class AdditionalOAuth2ClientProperties
{
	private Map<String, AdditionalRegistrationData> registration = new HashMap<>();
	
	public Map<String, AdditionalRegistrationData> getRegistration()
	{
		return this.registration;
	}
	
	public void setRegistration(final Map<String, AdditionalRegistrationData> registration)
	{
		this.registration = registration;
	}
}
