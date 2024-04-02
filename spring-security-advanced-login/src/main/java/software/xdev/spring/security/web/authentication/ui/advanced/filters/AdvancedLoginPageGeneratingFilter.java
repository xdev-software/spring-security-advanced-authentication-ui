package software.xdev.spring.security.web.authentication.ui.advanced.filters;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
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
	
	public AdvancedLoginPageGeneratingFilter header(final String header)
	{
		this.header = Objects.requireNonNullElse(header, "");
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
		return "  <head>"
			+ "    <meta charset='utf-8'>"
			+ this.headerMetas.entrySet()
			.stream()
			.map(e -> "    <meta name='" + e.getKey() + "' content='" + e.getValue() + "'>")
			.collect(Collectors.joining())
			+ Optional.ofNullable(this.pageTitle).map(t -> "    <title>" + t + "</title>").orElse("")
			+ this.headerElements.stream().map(s -> "    " + s).collect(Collectors.joining())
			+ "  </head>";
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
			+ this.additionalStylingData.classNames(AdditionalStylingData::body).orElse("")
			+ "'"
			+ this.additionalStylingData.style(AdditionalStylingData::body).map(s -> " style='" + s + "'").orElse("")
			+ ">";
	}
	
	protected String createContainerElement()
	{
		return "<div class='container"
			+ this.additionalStylingData.classNames(AdditionalStylingData::container).orElse("")
			+ "'"
			+ this.additionalStylingData.style(AdditionalStylingData::container)
			.map(s -> " style='" + s + "'")
			.orElse("")
			+ ">";
	}
	
	protected String createMainElement()
	{
		return "<main class='w-100 m-auto"
			+ this.additionalStylingData.classNames(AdditionalStylingData::main).orElse("")
			+ "' style='max-width: 21em; padding: 1rem;"
			+ this.additionalStylingData.style(AdditionalStylingData::main).orElse("")
			+ "'>";
	}
	
	protected String createFormLogin(final HttpServletRequest request, final String contextPath)
	{
		if(!this.formLoginEnabled)
		{
			return "";
		}
		
		return "<form class=\"mb-3\" method=\"post\" action=\"" + contextPath + this.authenticationUrl + "\">"
			+ "<div>"
			+ "  <label for='username'>Username</label>"
			+ "  <input type='text' class=\"form-control\" name=\"" + this.usernameParameter + "\""
			+ " id='username' placeholder=\"Username\" required autofocus>"
			+ "</div><div>"
			+ "  <label for='password'>Password</label>"
			+ "  <input type='password' class=\"form-control\" name=\"" + this.passwordParameter + "\""
			+ " id='password' placeholder=\"Password\" required>"
			+ "</div>"
			+ this.createRememberMe(this.rememberMeParameter)
			+ this.renderHiddenInputs(request)
			+ "<button class=\"btn btn-block btn-primary w-100\" type=\"submit\">Sign in</button>"
			+ "</form>";
	}
	
	@Override
	protected String createRememberMe(final String paramName)
	{
		return "<div class=\"form-check text-start my-2\">"
			+ "<label for='remember-me' class=\"form-check-label\">Remember me on this computer</label>"
			+ "<input class=\"form-check-input\" type='checkbox' name='" + paramName + "' id='remember-me'/>"
			+ "</div>";
	}
	
	protected boolean hasSSOLogin()
	{
		return this.oauth2LoginEnabled || this.saml2LoginEnabled;
	}
	
	protected String createHeaderLoginWith()
	{
		return "<h5 class=\"h5 mb-2 fw-normal\">Login with</h5>";
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
		public static AdditionalStylingData empty()
		{
			return new AdditionalStylingData(
				null,
				null,
				null);
		}
		
		public Optional<String> classNames(final Function<AdditionalStylingData, StylingDefinition> accessor)
		{
			return Optional.ofNullable(accessor.apply(this))
				.map(StylingDefinition::classNameString);
		}
		
		public Optional<String> style(final Function<AdditionalStylingData, StylingDefinition> accessor)
		{
			return Optional.ofNullable(accessor.apply(this))
				.map(StylingDefinition::style);
		}
	}
}