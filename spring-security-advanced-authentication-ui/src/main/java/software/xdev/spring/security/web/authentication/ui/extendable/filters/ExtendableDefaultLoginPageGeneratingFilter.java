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
import java.util.stream.Collectors;

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

// CPD-OFF - Upstream copy


/**
 * Same as {@link DefaultLoginPageGeneratingFilter} but all fields and methods can be overridden
 */
@SuppressWarnings({"java:S2177", "java:S1192", "java:S5663", "checkstyle:LineLength", "PMD.GodClass"})
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
	
	protected boolean passkeysEnabled;
	
	protected boolean oneTimeTokenEnabled;
	
	protected String authenticationUrl;
	
	protected String generateOneTimeTokenUrl;
	
	protected String usernameParameter;
	
	protected String passwordParameter;
	
	protected String rememberMeParameter;
	
	protected Map<String, String> oauth2AuthenticationUrlToClientName;
	
	protected Map<String, String> saml2AuthenticationUrlToProviderName;
	
	protected Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs =
		request -> Collections.emptyMap();
	
	protected Function<HttpServletRequest, Map<String, String>> resolveHeaders =
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
			new CopyInfo<>("passkeysEnabled", this::setPasskeysEnabled),
			new CopyInfo<>("oneTimeTokenEnabled", this::setOneTimeTokenEnabled),
			new CopyInfo<>("authenticationUrl", this::setAuthenticationUrl),
			new CopyInfo<>("generateOneTimeTokenUrl", this::setGenerateOneTimeTokenUrl),
			new CopyInfo<>("usernameParameter", this::setUsernameParameter),
			new CopyInfo<>("passwordParameter", this::setPasswordParameter),
			new CopyInfo<>("rememberMeParameter", this::setRememberMeParameter),
			new CopyInfo<>("oauth2AuthenticationUrlToClientName", this::setOauth2AuthenticationUrlToClientName),
			new CopyInfo<>("saml2AuthenticationUrlToProviderName", this::setSaml2AuthenticationUrlToProviderName),
			new CopyInfo<>("resolveHiddenInputs", this::setResolveHiddenInputs),
			new CopyInfo<>("resolveHeaders", this::setResolveHeaders)
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
	
	/**
	 * Sets a Function used to resolve a Map of the HTTP headers where the key is the name of the header and the value
	 * is the value of the header. Typically, this is used to resolve the CSRF token.
	 *
	 * @param resolveHeaders the function to resolve the headers
	 */
	@Override
	public void setResolveHeaders(final Function<HttpServletRequest, Map<String, String>> resolveHeaders)
	{
		Assert.notNull(resolveHeaders, "resolveHeaders cannot be null");
		this.resolveHeaders = resolveHeaders;
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
	public void setOneTimeTokenEnabled(final boolean oneTimeTokenEnabled)
	{
		this.oneTimeTokenEnabled = oneTimeTokenEnabled;
	}
	
	@Override
	public void setSaml2LoginEnabled(final boolean saml2LoginEnabled)
	{
		this.saml2LoginEnabled = saml2LoginEnabled;
	}
	
	@Override
	public void setPasskeysEnabled(final boolean passkeysEnabled)
	{
		this.passkeysEnabled = passkeysEnabled;
	}
	
	@Override
	public void setAuthenticationUrl(final String authenticationUrl)
	{
		this.authenticationUrl = authenticationUrl;
	}
	
	public void setGenerateOneTimeTokenUrl(final String generateOneTimeTokenUrl)
	{
		this.generateOneTimeTokenUrl = generateOneTimeTokenUrl;
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
			// strip everything after the first semicolon
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
	
	protected String generateLoginPageHtml(
		final HttpServletRequest request,
		final boolean loginError,
		final boolean logoutSuccess)
	{
		final String errorMsg = loginError ? this.getLoginErrorMessage(request) : "Invalid credentials";
		final String contextPath = request.getContextPath();
		
		return HtmlTemplates.fromTemplate(LOGIN_PAGE_TEMPLATE)
			.withRawHtml("contextPath", contextPath)
			.withRawHtml("javaScript", this.renderJavaScript(request, contextPath))
			.withRawHtml("formLogin", this.renderFormLogin(request, loginError, logoutSuccess, contextPath, errorMsg))
			.withRawHtml(
				"oneTimeTokenLogin",
				this.renderOneTimeTokenLogin(request, loginError, logoutSuccess, contextPath, errorMsg))
			.withRawHtml("oauth2Login", this.renderOAuth2Login(loginError, logoutSuccess, errorMsg, contextPath))
			.withRawHtml("saml2Login", this.renderSaml2Login(loginError, logoutSuccess, errorMsg, contextPath))
			.withRawHtml("passkeyLogin", this.renderPasskeyLogin())
			.render();
	}
	
	protected String renderJavaScript(final HttpServletRequest request, final String contextPath)
	{
		if(this.passkeysEnabled)
		{
			return HtmlTemplates.fromTemplate(PASSKEY_SCRIPT_TEMPLATE)
				.withValue("loginPageUrl", this.loginPageUrl)
				.withValue("contextPath", contextPath)
				.withRawHtml("csrfHeaders", this.renderHeaders(request))
				.render();
		}
		return "";
	}
	
	protected String renderPasskeyLogin()
	{
		if(this.passkeysEnabled)
		{
			return PASSKEY_FORM_TEMPLATE;
		}
		return "";
	}
	
	protected String renderHeaders(final HttpServletRequest request)
	{
		final StringBuilder javascriptHeadersEntries = new StringBuilder();
		final Map<String, String> headers = this.resolveHeaders.apply(request);
		for(final Map.Entry<String, String> header : headers.entrySet())
		{
			javascriptHeadersEntries.append(HtmlTemplates.fromTemplate(CSRF_HEADERS)
				.withValue("headerName", header.getKey())
				.withValue("headerValue", header.getValue())
				.render());
		}
		return javascriptHeadersEntries.toString();
	}
	
	protected String renderFormLogin(
		final HttpServletRequest request, final boolean loginError, final boolean logoutSuccess,
		final String contextPath, final String errorMsg)
	{
		if(!this.formLoginEnabled)
		{
			return "";
		}
		
		final String hiddenInputs = this.resolveHiddenInputs.apply(request)
			.entrySet()
			.stream()
			.map(inputKeyValue -> this.renderHiddenInput(inputKeyValue.getKey(), inputKeyValue.getValue()))
			.collect(Collectors.joining("\n"));
		
		return HtmlTemplates.fromTemplate(LOGIN_FORM_TEMPLATE)
			.withValue("loginUrl", contextPath + this.authenticationUrl)
			.withRawHtml("errorMessage", this.renderError(loginError, errorMsg))
			.withRawHtml("logoutMessage", this.renderSuccess(logoutSuccess))
			.withValue("usernameParameter", this.usernameParameter)
			.withValue("passwordParameter", this.passwordParameter)
			.withRawHtml("rememberMeInput", this.renderRememberMe(this.rememberMeParameter))
			.withRawHtml("hiddenInputs", hiddenInputs)
			.withRawHtml("autocomplete", this.passkeysEnabled ? "autocomplete=\"password webauthn\" " : "")
			.render();
	}
	
	protected String renderOneTimeTokenLogin(
		final HttpServletRequest request, final boolean loginError, final boolean logoutSuccess,
		final String contextPath, final String errorMsg)
	{
		if(!this.oneTimeTokenEnabled)
		{
			return "";
		}
		
		final String hiddenInputs = this.resolveHiddenInputs.apply(request)
			.entrySet()
			.stream()
			.map(inputKeyValue -> this.renderHiddenInput(inputKeyValue.getKey(), inputKeyValue.getValue()))
			.collect(Collectors.joining("\n"));
		
		return HtmlTemplates.fromTemplate(ONE_TIME_TEMPLATE)
			.withValue("generateOneTimeTokenUrl", contextPath + this.generateOneTimeTokenUrl)
			.withRawHtml("errorMessage", this.renderError(loginError, errorMsg))
			.withRawHtml("logoutMessage", this.renderSuccess(logoutSuccess))
			.withRawHtml("hiddenInputs", hiddenInputs)
			.render();
	}
	
	protected String renderOAuth2Login(
		final boolean loginError,
		final boolean logoutSuccess,
		final String errorMsg,
		final String contextPath)
	{
		if(!this.oauth2LoginEnabled)
		{
			return "";
		}
		
		final String oauth2Rows = this.oauth2AuthenticationUrlToClientName.entrySet()
			.stream()
			.map(urlToName -> renderOAuth2Row(contextPath, urlToName.getKey(), urlToName.getValue()))
			.collect(Collectors.joining("\n"));
		
		return HtmlTemplates.fromTemplate(OAUTH2_LOGIN_TEMPLATE)
			.withRawHtml("errorMessage", this.renderError(loginError, errorMsg))
			.withRawHtml("logoutMessage", this.renderSuccess(logoutSuccess))
			.withRawHtml("oauth2Rows", oauth2Rows)
			.render();
	}
	
	protected static String renderOAuth2Row(final String contextPath, final String url, final String clientName)
	{
		return HtmlTemplates.fromTemplate(OAUTH2_ROW_TEMPLATE)
			.withValue("url", contextPath + url)
			.withValue("clientName", clientName)
			.render();
	}
	
	protected String renderSaml2Login(
		final boolean loginError,
		final boolean logoutSuccess,
		final String errorMsg,
		final String contextPath)
	{
		if(!this.saml2LoginEnabled)
		{
			return "";
		}
		
		final String samlRows = this.saml2AuthenticationUrlToProviderName.entrySet()
			.stream()
			.map(urlToName -> renderSaml2Row(contextPath, urlToName.getKey(), urlToName.getValue()))
			.collect(Collectors.joining("\n"));
		
		return HtmlTemplates.fromTemplate(SAML_LOGIN_TEMPLATE)
			.withRawHtml("errorMessage", this.renderError(loginError, errorMsg))
			.withRawHtml("logoutMessage", this.renderSuccess(logoutSuccess))
			.withRawHtml("samlRows", samlRows)
			.render();
	}
	
	protected static String renderSaml2Row(final String contextPath, final String url, final String clientName)
	{
		return HtmlTemplates.fromTemplate(SAML_ROW_TEMPLATE)
			.withValue("url", contextPath + url)
			.withValue("clientName", clientName)
			.render();
	}
	
	protected String getLoginErrorMessage(final HttpServletRequest request)
	{
		final HttpSession session = request.getSession(false);
		if(session == null)
		{
			return "Invalid credentials";
		}
		if(!(session.getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION)
			instanceof final AuthenticationException exception))
		{
			return "Invalid credentials";
		}
		if(!StringUtils.hasText(exception.getMessage()))
		{
			return "Invalid credentials";
		}
		return exception.getMessage();
	}
	
	protected String renderHiddenInput(final String name, final String value)
	{
		return HtmlTemplates.fromTemplate(HIDDEN_HTML_INPUT_TEMPLATE)
			.withValue("name", name)
			.withValue("value", value)
			.render();
	}
	
	protected String renderRememberMe(final String paramName)
	{
		if(paramName == null)
		{
			return "";
		}
		return HtmlTemplates
			.fromTemplate("<p><input type='checkbox' name='{{paramName}}'/> Remember me on this computer.</p>")
			.withValue("paramName", paramName)
			.render();
	}
	
	protected String renderError(final boolean isError, final String message)
	{
		if(!isError)
		{
			return "";
		}
		return HtmlTemplates.fromTemplate(ALERT_TEMPLATE).withValue("message", message).render();
	}
	
	// Bade naming of original method -> Created overload
	protected String renderLogoutSuccess(final boolean isLogoutSuccess)
	{
		return this.renderSuccess(isLogoutSuccess);
	}
	
	protected String renderSuccess(final boolean isLogoutSuccess)
	{
		if(!isLogoutSuccess)
		{
			return "";
		}
		return "<div class=\"alert alert-success\" role=\"alert\">You have been signed out</div>";
	}
	
	protected static final String CSRF_HEADERS = """
		{"{{headerName}}" : "{{headerValue}}"}""";
	
	// Improved: Removed XML comment and fix format
	protected static final String PASSKEY_SCRIPT_TEMPLATE = """
		  <script type="text/javascript" src="{{contextPath}}/login/webauthn.js"></script>
		  <script type="text/javascript">
		  document.addEventListener("DOMContentLoaded",() => setupLogin({{csrfHeaders}}, "{{contextPath}}", document.getElementById('passkey-signin')));
		  </script>
		""";
	
	protected static final String PASSKEY_FORM_TEMPLATE = """
		<div class="login-form">
		<h2>Login with Passkeys</h2>
		<button id="passkey-signin" type="submit" class="primary">Sign in with a passkey</button>
		</form>
		""";
	
	protected static final String LOGIN_PAGE_TEMPLATE = """
		<!DOCTYPE html>
		<html lang="en">
		  <head>
		    <meta charset="utf-8">
		    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		    <meta name="description" content="">
		    <meta name="author" content="">
		    <title>Please sign in</title>
		    <link href="{{contextPath}}/default-ui.css" rel="stylesheet" />{{javaScript}}
		  </head>
		  <body>
		    <div class="content">
		{{formLogin}}
		{{oneTimeTokenLogin}}{{passkeyLogin}}
		{{oauth2Login}}
		{{saml2Login}}
		    </div>
		  </body>
		</html>""";
	
	protected static final String LOGIN_FORM_TEMPLATE = """
		      <form class="login-form" method="post" action="{{loginUrl}}">
		        <h2>Please sign in</h2>
		{{errorMessage}}{{logoutMessage}}
		        <p>
		          <label for="username" class="screenreader">Username</label>
		          <input type="text" id="username" name="{{usernameParameter}}" placeholder="Username" required autofocus>
		        </p>
		        <p>
		          <label for="password" class="screenreader">Password</label>
		          <input type="password" id="password" name="{{passwordParameter}}" placeholder="Password" {{autocomplete}}required>
		        </p>
		{{rememberMeInput}}
		{{hiddenInputs}}
		        <button type="submit" class="primary">Sign in</button>
		      </form>""";
	
	protected static final String HIDDEN_HTML_INPUT_TEMPLATE = """
		<input name="{{name}}" type="hidden" value="{{value}}" />
		""";
	
	protected static final String ALERT_TEMPLATE = """
		<div class="alert alert-danger" role="alert">{{message}}</div>""";
	
	protected static final String OAUTH2_LOGIN_TEMPLATE = """
		<h2>Login with OAuth 2.0</h2>
		{{errorMessage}}{{logoutMessage}}
		<table class="table table-striped">
		  {{oauth2Rows}}
		</table>""";
	
	protected static final String OAUTH2_ROW_TEMPLATE = """
		<tr><td><a href="{{url}}">{{clientName}}</a></td></tr>""";
	
	protected static final String SAML_LOGIN_TEMPLATE = """
		<h2>Login with SAML 2.0</h2>
		{{errorMessage}}{{logoutMessage}}
		<table class="table table-striped">
		  {{samlRows}}
		</table>""";
	
	protected static final String SAML_ROW_TEMPLATE = OAUTH2_ROW_TEMPLATE;
	
	protected static final String ONE_TIME_TEMPLATE = """
		      <form id="ott-form" class="login-form" method="post" action="{{generateOneTimeTokenUrl}}">
		        <h2>Request a One-Time Token</h2>
		{{errorMessage}}{{logoutMessage}}
		        <p>
		          <label for="ott-username" class="screenreader">Username</label>
		          <input type="text" id="ott-username" name="username" placeholder="Username" required>
		        </p>
		{{hiddenInputs}}
		        <button class="primary" type="submit" form="ott-form">Send Token</button>
		      </form>
		""";
}
