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
import org.springframework.http.HttpMethod;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

// CPD-OFF - Upstream copy
/**
 * Same as {@link DefaultLogoutPageGeneratingFilter} but all fields and methods can be overridden
 */
@SuppressWarnings({"java:S2177", "checkstyle:LineLength"})
public class ExtendableDefaultLogoutPageGeneratingFilter
	extends DefaultLogoutPageGeneratingFilter
	implements GeneratingFilterFillDataFrom<DefaultLogoutPageGeneratingFilter>, ExtendableDefaultPageGeneratingFilter
{
	protected RequestMatcher matcher = PathPatternRequestMatcher.withDefaults().matcher(HttpMethod.GET, "/logout");
	
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
	
	@SuppressWarnings({
		"PMD.ConsecutiveLiteralAppends",
		"PMD.ConsecutiveAppendsShouldReuse",
		"PMD.InefficientStringBuffering",
		"checkstyle:MagicNumber"})
	protected void renderLogout(final HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		final String renderedPage = HtmlTemplates.fromTemplate(LOGOUT_PAGE_TEMPLATE)
			.withValue("contextPath", request.getContextPath())
			.withRawHtml("hiddenInputs", this.renderHiddenInputs(request).indent(8))
			.render();
		response.setContentType("text/html;charset=UTF-8");
		response.getWriter().write(renderedPage);
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
	
	@SuppressWarnings({
		"PMD.ConsecutiveLiteralAppends",
		"PMD.ConsecutiveAppendsShouldReuse",
		"PMD.InefficientStringBuffering"})
	protected String renderHiddenInputs(final HttpServletRequest request)
	{
		final StringBuilder sb = new StringBuilder();
		for(final Map.Entry<String, String> input : this.resolveHiddenInputs.apply(request).entrySet())
		{
			final String inputElement = HtmlTemplates.fromTemplate(HIDDEN_HTML_INPUT_TEMPLATE)
				.withValue("name", input.getKey())
				.withValue("value", input.getValue())
				.render();
			sb.append(inputElement);
		}
		return sb.toString();
	}
	
	// Method is missing on upstream
	public void setMatcher(final RequestMatcher matcher)
	{
		this.matcher = matcher;
	}
	
	protected static final String LOGOUT_PAGE_TEMPLATE = """
		<!DOCTYPE html>
		<html lang="en">
		  <head>
		    <meta charset="utf-8">
		    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
		    <meta name="description" content="">
		    <meta name="author" content="">
		    <title>Confirm Log Out?</title>
		    <link href="{{contextPath}}/default-ui.css" rel="stylesheet" />
		  </head>
		  <body>
		    <div class="content">
		      <form class="logout-form" method="post" action="{{contextPath}}/logout">
		        <h2>Are you sure you want to log out?</h2>
		{{hiddenInputs}}
		        <button class="primary" type="submit">Log Out</button>
		      </form>
		    </div>
		  </body>
		</html>""";
	
	public static final String HIDDEN_HTML_INPUT_TEMPLATE = """
		<input name="{{name}}" type="hidden" value="{{value}}" />
		""";
}
