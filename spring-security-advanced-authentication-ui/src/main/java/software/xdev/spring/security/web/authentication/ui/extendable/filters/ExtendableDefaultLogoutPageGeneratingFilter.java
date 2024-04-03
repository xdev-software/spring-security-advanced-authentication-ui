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
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.core.log.LogMessage;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;


/**
 * Same as {@link DefaultLogoutPageGeneratingFilter} but all fields and methods can be overridden
 */
@SuppressWarnings({"java:S2177", "checkstyle:LineLength"})
public class ExtendableDefaultLogoutPageGeneratingFilter
	extends DefaultLogoutPageGeneratingFilter
	implements GeneratingFilterFillDataFrom<DefaultLogoutPageGeneratingFilter>, ExtendableDefaultPageGeneratingFilter
{
	protected RequestMatcher matcher = new AntPathRequestMatcher("/logout", "GET");
	
	protected Function<HttpServletRequest, Map<String, String>> resolveHiddenInputs =
		request -> Collections.emptyMap();
	
	@Override
	public void fillDataFrom(final DefaultLogoutPageGeneratingFilter source)
	{
		this.fillDataFrom(DefaultLogoutPageGeneratingFilter.class, source, Set.of(
			new CopyInfo<>("matcher", this::setMatcher),
			new CopyInfo<>("resolveHiddenInputs", this::setResolveHiddenInputs)
		));
	}
	
	@Override
	protected void doFilterInternal(
		final HttpServletRequest request,
		final HttpServletResponse response,
		final FilterChain filterChain)
		throws ServletException, IOException
	{
		if(this.matcher.matches(request))
		{
			this.renderLogout(request, response);
		}
		else
		{
			if(this.logger.isTraceEnabled())
			{
				this.logger.trace(LogMessage.format(
					"Did not render default logout page since request did not match [%s]",
					this.matcher));
			}
			filterChain.doFilter(request, response);
		}
	}
	
	protected void renderLogout(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		// @formatter:off
		final StringBuilder sb = new StringBuilder();
		sb.append("<!DOCTYPE html>\n");
		sb.append("<html lang=\"en\">\n");
		sb.append("  <head>\n");
		sb.append("    <meta charset=\"utf-8\">\n");
		sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n");
		sb.append("    <meta name=\"description\" content=\"\">\n");
		sb.append("    <meta name=\"author\" content=\"\">\n");
		sb.append("    <title>Confirm Log Out?</title>\n");
		sb.append("    <link href=\"https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-beta/css/bootstrap.min.css\" "
			+ "rel=\"stylesheet\" integrity=\"sha384-/Y6pD6FV/Vv2HJnA6t+vslU6fwYXjCFtcEpHbNJ0lyAFsXTsjBbfaDjzALeQsN6M\" "
			+ "crossorigin=\"anonymous\">\n");
		sb.append("    <link href=\"https://getbootstrap.com/docs/4.0/examples/signin/signin.css\" "
			+ "rel=\"stylesheet\" integrity=\"sha384-oOE/3m0LUMPub4kaC09mrdEhIc+e3exm4xOGxAmuFXhBNF4hcg/6MiAXAf5p0P56\" crossorigin=\"anonymous\"/>\n");
		sb.append("  </head>\n");
		sb.append("  <body>\n");
		sb.append("     <div class=\"container\">\n");
		sb.append("      <form class=\"form-signin\" method=\"post\" action=\"" + request.getContextPath()
			+ "/logout\">\n");
		sb.append("        <h2 class=\"form-signin-heading\">Are you sure you want to log out?</h2>\n");
		sb.append(this.renderHiddenInputs(request)
			+ "        <button class=\"btn btn-lg btn-primary btn-block\" type=\"submit\">Log Out</button>\n");
		sb.append("      </form>\n");
		sb.append("    </div>\n");
		sb.append("  </body>\n");
		sb.append("</html>");
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(sb.toString());
		// @formatter:on
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
	
	// Method is missing on upstream
	public void setMatcher(final RequestMatcher matcher)
	{
		this.matcher = matcher;
	}
}
