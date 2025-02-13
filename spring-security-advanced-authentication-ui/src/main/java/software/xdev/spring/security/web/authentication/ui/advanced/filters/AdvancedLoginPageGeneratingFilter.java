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
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.util.HtmlUtils;

import software.xdev.spring.security.web.authentication.ui.advanced.AdditionalRegistrationData;
import software.xdev.spring.security.web.authentication.ui.advanced.StylingDefinition;
import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultLoginPageGeneratingFilter;


@SuppressWarnings("unused") // This is an API ;)
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
	
	protected String passKeysWebAuthnScriptLocation = "/login/webauthn.js";
	
	protected String formLoginUsernameText = "Username";
	
	protected String formLoginPasswordText = "Password";
	
	protected String formLoginRememberMeText = "Remember me on this computer";
	
	protected String formLoginSignInText = "Sign in";
	
	protected String oneTimeTokenHeaderText = "Request a One-Time Token";
	
	protected String oneTimeTokenUsernameParameter = "username";
	
	protected String oneTimeTokenUsernameText = "Username";
	
	protected String oneTimeTokenSendTokenText = "Send token";
	
	protected String passkeyLoginHeaderText = "Login with Passkeys";
	
	protected String passkeySignInSubmitText = "Sign in with a passkey";
	
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
	
	public AdvancedLoginPageGeneratingFilter oneTimeTokenHeaderText(final String oneTimeTokenHeaderText)
	{
		this.oneTimeTokenHeaderText = oneTimeTokenHeaderText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter oneTimeTokenUsernameText(final String oneTimeTokenUsernameText)
	{
		this.oneTimeTokenUsernameText = oneTimeTokenUsernameText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter oneTimeTokenUsernameParameter(final String oneTimeTokenUsernameParameter)
	{
		this.oneTimeTokenUsernameParameter = oneTimeTokenUsernameParameter;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter oneTimeTokenSendTokenText(final String oneTimeTokenSendTokenText)
	{
		this.oneTimeTokenSendTokenText = oneTimeTokenSendTokenText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter passkeyLoginHeaderText(final String passkeyLoginHeaderText)
	{
		this.passkeyLoginHeaderText = passkeyLoginHeaderText;
		return this;
	}
	
	public AdvancedLoginPageGeneratingFilter passkeySignInSubmitText(final String passkeySignInSubmitText)
	{
		this.passkeySignInSubmitText = passkeySignInSubmitText;
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
		final String contextPath = request.getContextPath();
		
		return "<!DOCTYPE html>"
			+ "<html lang='en' style='height:100%'>"
			+ this.generateHeader(request, contextPath)
			+ this.generateBody(request, contextPath, loginError, logoutSuccess)
			+ "</html>";
	}
	
	// region Header
	
	protected String generateHeader(
		final HttpServletRequest request,
		final String contextPath)
	{
		return this.generateHeader(
			this.headerMetas,
			this.pageTitle,
			this.requiresAdditionalHeaderElements()
				? Stream.concat(
					this.headerElements.stream(),
					this.additionalHeaderElements(request, contextPath).stream())
				.toList()
				: this.headerElements);
	}
	
	protected boolean requiresAdditionalHeaderElements()
	{
		return this.passkeysEnabled;
	}
	
	protected List<String> additionalHeaderElements(
		final HttpServletRequest request,
		final String contextPath)
	{
		final List<String> additionalHeaderElements = new ArrayList<>();
		
		if(this.passkeysEnabled)
		{
			additionalHeaderElements.addAll(this.createPassKeyHeaderElements(request, contextPath));
		}
		
		return additionalHeaderElements;
	}
	
	protected List<String> createPassKeyHeaderElements(
		final HttpServletRequest request,
		final String contextPath)
	{
		return List.of(
			"<script type=\"text/javascript\" src=\"" + contextPath + this.passKeysWebAuthnScriptLocation
				+ "\"></script>",
			this.createPassKeyScript(request, contextPath)
		);
	}
	
	protected String createPassKeyScript(
		final HttpServletRequest request,
		final String contextPath)
	{
		return """
			<script type="text/javascript">
			document.addEventListener("DOMContentLoaded",\
			() => setupLogin(%s, "%s", document.getElementById('passkey-signin')));
			</script>
			""".formatted(this.renderHeadersForFetchAPI(request), contextPath);
	}
	
	protected String renderHeadersForFetchAPI(final HttpServletRequest request)
	{
		return "{ "
			+ this.resolveHeaders.apply(request).entrySet()
			.stream()
			.map(e -> "\"" + e.getKey() + "\": \"" + e.getValue() + "\"")
			.collect(Collectors.joining(", "))
			+ " }";
	}
	
	// endregion
	// region Body
	
	protected String generateBody(
		final HttpServletRequest request,
		final String contextPath,
		final boolean loginError,
		final boolean logoutSuccess)
	{
		final String errorMsg = loginError ? this.getLoginErrorMessage(request) : "Invalid credentials";
		
		return "  " + this.createBodyElement()
			+ "     " + this.createContainerElement()
			+ "     " + this.createMainElement()
			+ this.renderError(loginError, errorMsg)
			+ this.renderLogoutSuccess(logoutSuccess)
			+ this.header
			+ this.createFormLogin(request, contextPath)
			+ this.createOneTimeTokenLogin(request, contextPath)
			+ this.createPasskeyFormLogin()
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
	
	@Override
	protected String renderError(final boolean isError, final String message)
	{
		if(!isError)
		{
			return "";
		}
		return "<div class=\"alert alert-danger\" role=\"alert\">" + HtmlUtils.htmlEscape(message) + "</div>";
	}
	
	// region Render FormLogin
	
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
	
	protected String createRememberMe(final String paramName)
	{
		return "<div class=\"form-check text-start mt-1\">"
			+ "<label for='remember-me' class=\"form-check-label\">"
			+ this.formLoginRememberMeText
			+ "</label>"
			+ "<input class=\"form-check-input\" type='checkbox' name='" + paramName + "' id='remember-me'/>"
			+ "</div>";
	}
	
	protected String createFormLoginSignInButton()
	{
		return this.createFormSubmitButton(this.formLoginSignInText);
	}
	
	// endregion
	// region Render OTT
	
	protected String createOneTimeTokenLogin(final HttpServletRequest request, final String contextPath)
	{
		if(!this.oneTimeTokenEnabled)
		{
			return "";
		}
		
		return this.createOneTimeTokenLoginHeader()
			+ "<form id=\"ott-form\" class=\"mb-3\" method=\"post\" action=\"" + contextPath
			+ this.generateOneTimeTokenUrl + "\">"
			+ "<div class='form-floating'>"
			+ "  <input type='text' class='form-control' name=\"" + this.oneTimeTokenUsernameParameter + "\""
			+ " id='ott-username' placeholder=\"" + this.oneTimeTokenUsernameText + "\" required>"
			+ "  <label for='username'>" + this.oneTimeTokenUsernameParameter + "</label>"
			+ "</div>"
			+ this.renderHiddenInputs(request)
			+ this.createOneTimeTokenLoginSignInButton()
			+ "</form>";
	}
	
	protected String createOneTimeTokenLoginHeader()
	{
		return this.createLoginMethodHeader(this.oneTimeTokenHeaderText);
	}
	
	protected String createOneTimeTokenLoginSignInButton()
	{
		return this.createFormSubmitButton(this.oneTimeTokenSendTokenText);
	}
	
	// endregion
	// region Render Passkeys
	
	protected String createPasskeyFormLogin()
	{
		if(!this.passkeysEnabled)
		{
			return "";
		}
		
		return this.createPasskeyFormLoginHeader()
			// This needs to be a div and not a form
			+ "<div id=\"passkey-form\" class=\"mb-3 login-form\">"
			+ this.createPasskeyForSignInButton()
			+ "</div>";
	}
	
	protected String createPasskeyFormLoginHeader()
	{
		return this.createLoginMethodHeader(this.passkeyLoginHeaderText);
	}
	
	protected String createPasskeyForSignInButton()
	{
		return "<button id=\"passkey-signin\" class=\"btn btn-block btn-primary w-100 mt-1\" type=\"submit\">"
			+ this.passkeySignInSubmitText
			+ "</button>";
	}
	
	// endregion
	// region Render SSO
	
	protected boolean hasSSOLogin()
	{
		return this.oauth2LoginEnabled || this.saml2LoginEnabled;
	}
	
	protected String createHeaderLoginWith()
	{
		return this.createLoginMethodHeader(this.ssoLoginHeaderText);
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
	
	// endregion
	
	protected String createLoginMethodHeader(final String text)
	{
		return "<h5 class=\"h5 mb-2 fw-normal\">" + text + "</h5>";
	}
	
	public String createFormSubmitButton(final String text)
	{
		return "<button class=\"btn btn-block btn-primary w-100 mt-1\" type=\"submit\">"
			+ text
			+ "</button>";
	}
	
	protected String renderHiddenInputs(final HttpServletRequest request)
	{
		return this.renderHiddenInputs(this.resolveHiddenInputs.apply(request).entrySet());
	}
	
	// endregion
	
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
