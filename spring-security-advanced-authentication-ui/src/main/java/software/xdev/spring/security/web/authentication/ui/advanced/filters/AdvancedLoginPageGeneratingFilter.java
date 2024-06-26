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
package software.xdev.spring.security.web.authentication.ui.advanced.filters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.util.HtmlUtils;

import software.xdev.spring.security.web.authentication.ui.advanced.AdditionalRegistrationData;
import software.xdev.spring.security.web.authentication.ui.advanced.StylingDefinition;
import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultLoginPageGeneratingFilter;


public class AdvancedLoginPageGeneratingFilter
	extends ExtendableDefaultLoginPageGeneratingFilter
	implements AdvancedSharedPageGeneratingFilter<AdvancedLoginPageGeneratingFilter>
{
	protected List<String> headerElements = new ArrayList<>(DEFAULT_HEADER_ELEMENTS);
	
	protected String pageTitle = "Please sign in";
	
	protected Map<String, String> headerMetas = new LinkedHashMap<>(DEFAULT_HEADER_METAS);
	
	protected Map<String, AdditionalRegistrationData> additionalOAuth2RegistrationProperties;
	
	protected Map<String, AdditionalRegistrationData> additionalSaml2RegistrationProperties;
	
	protected AdditionalStylingData additionalStylingData;
	
	protected String header = "";
	
	protected String formLoginUsernameText = "Username";
	
	protected String formLoginPasswordText = "Password";
	
	protected String formLoginRememberMeText = "Remember me on this computer";
	
	protected String formLoginSignInText = "Sign in";
	
	protected String ssoLoginHeaderText = "Login with";
	
	protected String footer = "";
	
	@Override
	public AdvancedLoginPageGeneratingFilter setHeaderElements(final List<String> headerElements)
	{
		this.headerElements = headerElements;
		return this;
	}
	
	@Override
	public AdvancedLoginPageGeneratingFilter addHeaderElement(final String element)
	{
		this.headerElements.add(element);
		return this;
	}
	
	@Override
	public AdvancedLoginPageGeneratingFilter pageTitle(final String pageTitle)
	{
		this.pageTitle = pageTitle;
		return this;
	}
	
	@Override
	public AdvancedLoginPageGeneratingFilter setHeaderMetas(final Map<String, String> headerMetas)
	{
		this.headerMetas = headerMetas;
		return this;
	}
	
	@Override
	public AdvancedLoginPageGeneratingFilter addHeaderMeta(final String name, final String content)
	{
		this.headerMetas.put(name, content);
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter additionalOAuth2RegistrationProperties(
		final Map<String, AdditionalRegistrationData> additionalOAuth2RegistrationProperties)
	{
		this.additionalOAuth2RegistrationProperties = additionalOAuth2RegistrationProperties;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter additionalSaml2RegistrationProperties(
		final Map<String, AdditionalRegistrationData> additionalSaml2RegistrationProperties)
	{
		this.additionalSaml2RegistrationProperties = additionalSaml2RegistrationProperties;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter additionalStylingData(final AdditionalStylingData additionalStylingData)
	{
		this.additionalStylingData = additionalStylingData;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter additionalStylingData(
		final Consumer<AdditionalStylingData.Builder> builderConsumer)
	{
		final AdditionalStylingData.Builder builder = new AdditionalStylingData.Builder();
		builderConsumer.accept(builder);
		return this.additionalStylingData(builder.build());
	}
	
	public AdvancedLoginPageGeneratingFilter header(final String header)
	{
		this.header = Objects.requireNonNullElse(header, "");
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter formLoginUsernameText(final String formLoginUsernameText)
	{
		this.formLoginUsernameText = formLoginUsernameText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter formLoginPasswordText(final String formLoginPasswordText)
	{
		this.formLoginPasswordText = formLoginPasswordText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter formLoginRememberMeText(final String formLoginRememberMeText)
	{
		this.formLoginRememberMeText = formLoginRememberMeText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter formLoginSignInText(final String formLoginSignInText)
	{
		this.formLoginSignInText = formLoginSignInText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter ssoLoginHeaderText(final String ssoLoginHeaderText)
	{
		this.ssoLoginHeaderText = ssoLoginHeaderText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter footer(final String footer)
	{
		this.footer = Objects.requireNonNullElse(footer, "");
		return this;
	}
	
	protected Optional<AdditionalRegistrationData> additionalRegistrationData(
		final Map<String, AdditionalRegistrationData> additionalRegistrationProperties,
		final String url)
	{
		return Optional.ofNullable(additionalRegistrationProperties)
			.map(registrations -> {
				final int index = url.lastIndexOf('/');
				return registrations.get(index != -1 ? url.substring(index + 1) : url);
			});
	}
	
	@Override
	protected String generateLoginPageHtml(
		final HttpServletRequest request,
		final boolean loginError,
		final boolean logoutSuccess)
	{
		return "<!DOCTYPE html>"
			+ "<html lang='en' style='height:100%'>"
			+ this.generateHeader()
			+ this.generateBody(request, loginError, logoutSuccess)
			+ "</html>";
	}
	
	protected String generateHeader()
	{
		return this.generateHeader(this.headerMetas, this.pageTitle, this.headerElements);
	}
	
	protected String generateBody(
		final HttpServletRequest request,
		final boolean loginError,
		final boolean logoutSuccess)
	{
		final String errorMsg = loginError ? this.getLoginErrorMessage(request) : "Invalid credentials";
		final String contextPath = request.getContextPath();
		
		return "  " + this.createBodyElement()
			+ "     " + this.createContainerElement()
			+ "     " + this.createMainElement()
			+ this.createError(loginError, errorMsg)
			+ this.createLogoutSuccess(logoutSuccess)
			+ this.header
			+ this.createFormLogin(request, contextPath)
			+ (this.hasSSOLogin() ? this.createHeaderLoginWith() : "")
			+ this.createOAuth2LoginPagePart(contextPath)
			+ this.createSaml2LoginPagePart(contextPath)
			+ this.footer
			+ "    </main>"
			+ "    </div>"
			+ "  </body>";
	}
	
	protected String createBodyElement()
	{
		return "<body class='h-100"
			+ Optional.ofNullable(this.additionalStylingData)
			.map(AdditionalStylingData::body)
			.map(StylingDefinition::classNameString)
			.orElse("")
			+ "'"
			+ Optional.ofNullable(this.additionalStylingData)
			.map(AdditionalStylingData::body)
			.map(sd -> " style='" + sd.styleString() + "'")
			.orElse("")
			+ ">";
	}
	
	protected String createContainerElement()
	{
		return "<div class='container"
			+ Optional.ofNullable(this.additionalStylingData)
			.map(AdditionalStylingData::container)
			.map(StylingDefinition::classNameString)
			.orElse("")
			+ "'"
			+ Optional.ofNullable(this.additionalStylingData)
			.map(AdditionalStylingData::container)
			.map(sd -> " style='" + sd.styleString() + "'")
			.orElse("")
			+ ">";
	}
	
	protected String createMainElement()
	{
		return "<main class='w-100 m-auto"
			+ Optional.ofNullable(this.additionalStylingData)
			.map(AdditionalStylingData::main)
			.map(StylingDefinition::classNameString)
			.orElse("")
			+ "' style='max-width: 21em; padding: 1rem;"
			+ Optional.ofNullable(this.additionalStylingData)
			.map(AdditionalStylingData::main)
			.map(StylingDefinition::styleString)
			.orElse("")
			+ "'>";
	}
	
	@SuppressWarnings("java:S1192")
	protected String createFormLogin(final HttpServletRequest request, final String contextPath)
	{
		if(!this.formLoginEnabled)
		{
			return "";
		}
		
		return "<form class=\"mb-3\" method=\"post\" action=\"" + contextPath + this.authenticationUrl + "\">"
			+ "<div class='form-floating'>"
			+ "  <input type='text' class='form-control' name=\"" + this.usernameParameter + "\""
			+ " id='username' placeholder=\"" + this.formLoginUsernameText + "\" required autofocus>"
			+ "  <label for='username'>" + this.formLoginUsernameText + "</label>"
			+ "</div><div class='form-floating'>"
			+ "  <input type='password' class='form-control' name=\"" + this.passwordParameter + "\""
			+ " id='password' placeholder=\"" + this.formLoginPasswordText + "\" required>"
			+ "  <label for='password'>" + this.formLoginPasswordText + "</label>"
			+ "</div>"
			+ this.createRememberMe(this.rememberMeParameter)
			+ this.renderHiddenInputs(request)
			+ this.createFormLoginSignInButton()
			+ "</form>";
	}
	
	protected String createFormLoginSignInButton()
	{
		return "<button class=\"btn btn-block btn-primary w-100\" type=\"submit\">"
			+ this.formLoginSignInText
			+ "</button>";
	}
	
	@Override
	protected String createRememberMe(final String paramName)
	{
		return "<div class=\"form-check text-start my-2\">"
			+ "<label for='remember-me' class=\"form-check-label\">"
			+ this.formLoginRememberMeText
			+ "</label>"
			+ "<input class=\"form-check-input\" type='checkbox' name='" + paramName + "' id='remember-me'/>"
			+ "</div>";
	}
	
	protected boolean hasSSOLogin()
	{
		return this.oauth2LoginEnabled || this.saml2LoginEnabled;
	}
	
	protected String createHeaderLoginWith()
	{
		return "<h5 class=\"h5 mb-2 fw-normal\">"
			+ this.ssoLoginHeaderText
			+ "</h5>";
	}
	
	protected String createOAuth2LoginPagePart(final String contextPath)
	{
		if(!this.oauth2LoginEnabled)
		{
			return "";
		}
		return this.createSSOLoginPagePart(
			contextPath,
			"login-oauth2",
			this.oauth2AuthenticationUrlToClientName,
			this.additionalOAuth2RegistrationProperties);
	}
	
	protected String createSaml2LoginPagePart(final String contextPath)
	{
		if(!this.saml2LoginEnabled)
		{
			return "";
		}
		return this.createSSOLoginPagePart(
			contextPath,
			"login-saml2",
			this.saml2AuthenticationUrlToProviderName,
			this.additionalSaml2RegistrationProperties);
	}
	
	protected String createSSOLoginPagePart(
		final String contextPath,
		final String className,
		final Map<String, String> authenticationUrlToClientName,
		final Map<String, AdditionalRegistrationData> additionalRegistrationData)
	{
		return "<table class=\"table table-sm table-borderless " + className + "\">"
			+ authenticationUrlToClientName.entrySet().stream()
			.map(e -> new ButtonBuildingData(
				e.getKey(),
				e.getValue(),
				this.additionalRegistrationData(additionalRegistrationData, e.getKey())))
			.map(data -> " <tr><td class=\"px-0\">"
				+ "<a class=\"btn btn-block btn-secondary w-100 align-items-center d-inline-flex "
				+ "justify-content-center\""
				+ data.registrationData().map(AdditionalRegistrationData::getColor)
				.map(color -> " style=\"background-color:" + color + "\"")
				.orElse("")
				+ " href=\"" + contextPath + data.url() + "\">"
				+ data.registrationData().map(AdditionalRegistrationData::getIconSrc)
				.map(iconSrc -> "<img src=\"" + iconSrc
					+ "\" style=\"height:var(--bs-btn-font-size)\"></img>&nbsp;")
				.orElse("")
				+ "<span" + data.registrationData.map(AdditionalRegistrationData::isInvertTextColor)
				.filter(b -> b)
				.map(x -> " class='text-dark'")
				.orElse("") + ">" + HtmlUtils.htmlEscape(data.name()) + "</span>"
				+ "</a>"
				+ "</td></tr>")
			.collect(Collectors.joining())
			+ "</table>";
	}
	
	protected record ButtonBuildingData(
		String url,
		String name,
		Optional<AdditionalRegistrationData> registrationData)
	{
	}
	
	
	public record AdditionalStylingData(
		StylingDefinition body,
		StylingDefinition container,
		StylingDefinition main
	)
	{
		public static class Builder
		{
			protected StylingDefinition body;
			protected StylingDefinition container;
			protected StylingDefinition main;
			
			public Builder body(final StylingDefinition body)
			{
				this.body = body;
				return this;
			}
			
			public Builder body(final Consumer<StylingDefinition.Builder> builderConsumer)
			{
				return this.body(this.buildStylingDefinition(builderConsumer));
			}
			
			public Builder container(final StylingDefinition container)
			{
				this.container = container;
				return this;
			}
			
			public Builder container(final Consumer<StylingDefinition.Builder> builderConsumer)
			{
				return this.container(this.buildStylingDefinition(builderConsumer));
			}
			
			public Builder main(final StylingDefinition main)
			{
				this.main = main;
				return this;
			}
			
			public Builder main(final Consumer<StylingDefinition.Builder> builderConsumer)
			{
				return this.main(this.buildStylingDefinition(builderConsumer));
			}
			
			protected StylingDefinition buildStylingDefinition(
				final Consumer<StylingDefinition.Builder> builderConsumer)
			{
				final StylingDefinition.Builder builder = new StylingDefinition.Builder();
				builderConsumer.accept(builder);
				return builder.build();
			}
			
			public AdditionalStylingData build()
			{
				return new AdditionalStylingData(this.body, this.container, this.main);
			}
		}
	}
}
