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
package software.xdev.spring.security.web.authentication.ui.extendable.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;


/**
 * Same as {@link DefaultLoginPageGeneratingFilter} but all fields and methods can be overridden
 */
@SuppressWarnings({"java:S2177", "java:S1192", "checkstyle:LineLength"})
public class ExtendableDefaultLoginPageGeneratingFilter
	extends DefaultLoginPageGeneratingFilter
	implements GeneratingFilterFillDataFrom<DefaultLoginPageGeneratingFilter>, ExtendableDefaultPageGeneratingFilter
{
	protected String loginPageUrl;
	
	protected String logoutSuccessUrl;
	
	protected String failureUrl;
	
	protected boolean formLoginEnabled;
	
	protected boolean oauth2LoginEnabled;
	
	protected boolean saml2LoginEnabled;
	
	protected String authenticationUrl;
	
	protected String usernameParameter;
	
	protected String passwordParameter;
	
	protected String rememberMeParameter;
	
	protected Map<String, String> oauth2AuthenticationUrlToClientName;
	
	protected Map<String, String> saml2AuthenticationUrlToProviderName;
	
	protected Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs =
		request -> Collections.emptyMap();
	
	@Override
	public void fillDataFrom(final DefaultLoginPageGeneratingFilter source)
	{
		this.fillDataFrom(DefaultLoginPageGeneratingFilter.class, source, Set.of(
			new CopyInfo<>("loginPageUrl", this::setLoginPageUrl),
			new CopyInfo<>("logoutSuccessUrl", this::setLogoutSuccessUrl),
			new CopyInfo<>("failureUrl", this::setFailureUrl),
			new CopyInfo<>("formLoginEnabled", this::setFormLoginEnabled),
			new CopyInfo<>("oauth2LoginEnabled", this::setOauth2LoginEnabled),
			new CopyInfo<>("saml2LoginEnabled", this::setSaml2LoginEnabled),
			new CopyInfo<>("authenticationUrl", this::setAuthenticationUrl),
			new CopyInfo<>("usernameParameter", this::setUsernameParameter),
			new CopyInfo<>("passwordParameter", this::setPasswordParameter),
			new CopyInfo<>("rememberMeParameter", this::setRememberMeParameter),
			new CopyInfo<>("oauth2AuthenticationUrlToClientName", this::setOauth2AuthenticationUrlToClientName),
			new CopyInfo<>("saml2AuthenticationUrlToProviderName", this::setSaml2AuthenticationUrlToProviderName),
			new CopyInfo<>("resolveHiddenInputs", this::setResolveHiddenInputs)
		));
	}
	
	/**
	 * Sets a Function used to resolve a Map of the hidden inputs where the key is the name of the input and the value
	 * is the value of the input. Typically this is used to resolve the CSRF token.
	 *
	 * @param resolveHiddenInputs the function to resolve the inputs
	 */
	@Override
	public void setResolveHiddenInputs(final Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs)
	{
		Assert.notNull(resolveHiddenInputs, "resolveHiddenInputs cannot be null");
		this.resolveHiddenInputs = resolveHiddenInputs;
	}
	
	@Override
	public boolean isEnabled()
	{
		return this.formLoginEnabled || this.oauth2LoginEnabled || this.saml2LoginEnabled;
	}
	
	@Override
	public void setLogoutSuccessUrl(final String logoutSuccessUrl)
	{
		this.logoutSuccessUrl = logoutSuccessUrl;
	}
	
	@Override
	public String getLoginPageUrl()
	{
		return this.loginPageUrl;
	}
	
	@Override
	public void setLoginPageUrl(final String loginPageUrl)
	{
		this.loginPageUrl = loginPageUrl;
	}
	
	@Override
	public void setFailureUrl(final String failureUrl)
	{
		this.failureUrl = failureUrl;
	}
	
	@Override
	public void setFormLoginEnabled(final boolean formLoginEnabled)
	{
		this.formLoginEnabled = formLoginEnabled;
	}
	
	@Override
	public void setOauth2LoginEnabled(final boolean oauth2LoginEnabled)
	{
		this.oauth2LoginEnabled = oauth2LoginEnabled;
	}
	
	@Override
	public void setSaml2LoginEnabled(final boolean saml2LoginEnabled)
	{
		this.saml2LoginEnabled = saml2LoginEnabled;
	}
	
	@Override
	public void setAuthenticationUrl(final String authenticationUrl)
	{
		this.authenticationUrl = authenticationUrl;
	}
	
	@Override
	public void setUsernameParameter(final String usernameParameter)
	{
		this.usernameParameter = usernameParameter;
	}
	
	@Override
	public void setPasswordParameter(final String passwordParameter)
	{
		this.passwordParameter = passwordParameter;
	}
	
	@Override
	public void setRememberMeParameter(final String rememberMeParameter)
	{
		this.rememberMeParameter = rememberMeParameter;
	}
	
	@Override
	public void setOauth2AuthenticationUrlToClientName(final Map<String, String> oauth2AuthenticationUrlToClientName)
	{
		this.oauth2AuthenticationUrlToClientName = oauth2AuthenticationUrlToClientName;
	}
	
	@Override
	public void setSaml2AuthenticationUrlToProviderName(final Map<String, String> saml2AuthenticationUrlToProviderName)
	{
		this.saml2AuthenticationUrlToProviderName = saml2AuthenticationUrlToProviderName;
	}
	
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
		throws IOException, ServletException
	{
		this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
	}
	
	protected void doFilter(
		final HttpServletRequest request, final HttpServletResponse response,
		final FilterChain chain)
		throws IOException, ServletException
	{
		final boolean loginError = this.isErrorPage(request);
		final boolean logoutSuccess = this.isLogoutSuccess(request);
		if(this.isLoginUrlRequest(request) || loginError || logoutSuccess)
		{
			final String loginPageHtml = this.generateLoginPageHtml(request, loginError, logoutSuccess);
			response.setContentType("text/html;charset=UTF-8");
			response.setContentLength(loginPageHtml.getBytes(StandardCharsets.UTF_8).length);
			response.getWriter().write(loginPageHtml);
			return;
		}
		chain.doFilter(request, response);
	}
	
	protected String generateLoginPageHtml(
		final HttpServletRequest request,
		final boolean loginError,
		final boolean logoutSuccess)
	{
		// @formatter:off
		final String errorMsg = loginError ? this.getLoginErrorMessage(request) : "Invalid credentials";
		final String contextPath = request.getContextPath();
		final StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang=\"en\">\n");
		sb.append("  <head>\n");
		sb.append("    <meta charset=\"utf-8\">\n");
		sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");
		sb.append("    <meta name=\"description\" content=\"\">\n");
		sb.append("    <meta name=\"author\" content=\"\">\n");
		sb.append("    <title>Please sign in</title>\n");
		sb.append("    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" "
			+ "rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" crossorigin=\"anonymous\">\n");
		sb.append("    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" "
			+ "rel=\"stylesheet\" integrity=\"sha384-oOE/3m0LUMPub4kaC09mrdEhIc+e3exm4xOGxAmuFXhBNF4hcg/6MiAXAf5p0P56\" crossorigin=\"anonymous\"/>\n");
		sb.append("  </head>\n");
		sb.append("  <body>\n");
		sb.append("     <div class=\"container\">\n");
		if (this.formLoginEnabled)
		{
			sb.append("      <form class=\"form-signin\" method=\"post\" action=\"" + contextPath
				+ this.authenticationUrl + "\">\n");
			sb.append("        <h2 class=\"form-signin-heading\">Please sign in</h2>\n");
			sb.append(this.createError(loginError, errorMsg) + this.createLogoutSuccess(logoutSuccess) + "        <p>\n");
			sb.append("          <label for=\"username\" class=\"sr-only\">Username</label>\n");
			sb.append("          <input type=\"text\" id=\"username\" name=\"" + this.usernameParameter
				+ "\" class=\"form-control\" placeholder=\"Username\" required autofocus>\n");
			sb.append("        </p>\n");
			sb.append("        <p>\n");
			sb.append("          <label for=\"password\" class=\"sr-only\">Password</label>\n");
			sb.append("          <input type=\"password\" id=\"password\" name=\"" + this.passwordParameter
				+ "\" class=\"form-control\" placeholder=\"Password\" required>\n");
			sb.append("        </p>\n");
			sb.append(this.createRememberMe(this.rememberMeParameter) + this.renderHiddenInputs(request));
			sb.append("        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Sign in</button>\n");
			sb.append("      </form>\n");
		}
		if (this.oauth2LoginEnabled)
		{
			sb.append("<h2 class=\"form-signin-heading\">Login with OAuth 2.0</h2>");
			sb.append(this.createError(loginError, errorMsg));
			sb.append(this.createLogoutSuccess(logoutSuccess));
			sb.append("<table class=\"table table-striped\">\n");
			for (final Map.Entry<String, String> clientAuthenticationUrlToClientName : this.oauth2AuthenticationUrlToClientName
				.entrySet())
			{
				sb.append(" <tr><td>");
				final String url = clientAuthenticationUrlToClientName.getKey();
				sb.append("<a href=\"").append(contextPath).append(url).append("\">");
				final String clientName = HtmlUtils.htmlEscape(clientAuthenticationUrlToClientName.getValue());
				sb.append(clientName);
				sb.append("</a>");
				sb.append("</td></tr>\n");
			}
			sb.append("</table>\n");
		}
		if (this.saml2LoginEnabled)
		{
			sb.append("<h2 class=\"form-signin-heading\">Login with SAML 2.0</h2>");
			sb.append(this.createError(loginError, errorMsg));
			sb.append(this.createLogoutSuccess(logoutSuccess));
			sb.append("<table class=\"table table-striped\">\n");
			for (final Map.Entry<String, String> relyingPartyUrlToName : this.saml2AuthenticationUrlToProviderName
				.entrySet())
			{
				sb.append(" <tr><td>");
				final String url = relyingPartyUrlToName.getKey();
				sb.append("<a href=\"").append(contextPath).append(url).append("\">");
				final String partyName = HtmlUtils.htmlEscape(relyingPartyUrlToName.getValue());
				sb.append(partyName);
				sb.append("</a>");
				sb.append("</td></tr>\n");
			}
			sb.append("</table>\n");
		}
		sb.append("</div>\n");
		sb.append("</body></html>");
		return sb.toString();
		// @formatter:on
	}
	
	protected String getLoginErrorMessage(final HttpServletRequest request)
	{
		final HttpSession session = request.getSession(false);
		if(session == null)
		{
			return "Invalid credentials";
		}
		if(!(session
			.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION) instanceof final AuthenticationException exception))
		{
			return "Invalid credentials";
		}
		if(!StringUtils.hasText(exception.getMessage()))
		{
			return "Invalid credentials";
		}
		return exception.getMessage();
	}
	
	protected String renderHiddenInputs(final HttpServletRequest request)
	{
		final StringBuilder sb = new StringBuilder();
		for(final Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet())
		{
			sb.append("<input name=\"");
			sb.append(input.getKey());
			sb.append("\" type=\"hidden\" value=\"");
			sb.append(input.getValue());
			sb.append("\" />\n");
		}
		return sb.toString();
	}
	
	protected String createRememberMe(final String paramName)
	{
		if(paramName == null)
		{
			return "";
		}
		return "<p><input type='checkbox' name='" + paramName + "'/> Remember me on this computer.</p>\n";
	}
	
	protected boolean isLogoutSuccess(final HttpServletRequest request)
	{
		return this.logoutSuccessUrl != null && this.matches(request, this.logoutSuccessUrl);
	}
	
	protected boolean isLoginUrlRequest(final HttpServletRequest request)
	{
		return this.matches(request, this.loginPageUrl);
	}
	
	protected boolean isErrorPage(final HttpServletRequest request)
	{
		return this.matches(request, this.failureUrl);
	}
	
	protected String createError(final boolean isError, final String message)
	{
		if(!isError)
		{
			return "";
		}
		return "<div class=\"alert alert-danger\" role=\"alert\">" + HtmlUtils.htmlEscape(message) + "</div>";
	}
	
	protected String createLogoutSuccess(final boolean isLogoutSuccess)
	{
		if(!isLogoutSuccess)
		{
			return "";
		}
		return "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>";
	}
	
	protected boolean matches(final HttpServletRequest request, final String url)
	{
		if(!"GET".equals(request.getMethod()) || url == null)
		{
			return false;
		}
		String uri = request.getRequestURI();
		final int pathParamIndex = uri.indexOf(';');
		if(pathParamIndex > 0)
		{
			// strip everything after the first semi-colon
			uri = uri.substring(0, pathParamIndex);
		}
		if(request.getQueryString() != null)
		{
			uri += "?" + request.getQueryString();
		}
		if("".equals(request.getContextPath()))
		{
			return uri.equals(url);
		}
		return uri.equals(request.getContextPath() + url);
	}
}
