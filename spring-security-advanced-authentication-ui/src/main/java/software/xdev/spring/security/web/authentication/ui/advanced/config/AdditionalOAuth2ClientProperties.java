/*
 * Copyright © 2024 XDEV Software (https://xdev.software)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package software.xdev.spring.security.web.authentication.ui.advanced.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

import software.xdev.spring.security.web.authentication.ui.advanced.AdditionalRegistrationData;


/**
 * Additional properties for OAuth2 client.
 *
 * @apiNote You may need to use {@link org.springframework.boot.context.properties.EnableConfigurationProperties}
 * @see org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties
 */
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
