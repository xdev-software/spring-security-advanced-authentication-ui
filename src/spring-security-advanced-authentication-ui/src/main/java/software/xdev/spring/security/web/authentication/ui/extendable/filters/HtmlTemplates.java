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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;


/**
 * Render HTML templates using string substitution. Intended for internal use. Variables can be templated using double
 * curly-braces: {@code {{name}}}.
 *
 * @author Daniel Garnier-Moiroux
 * @since 6.4
 */
public final class HtmlTemplates
{
	private HtmlTemplates()
	{
	}
	
	public static Builder fromTemplate(final String template)
	{
		return new Builder(template);
	}
	
	public static class Builder
	{
		// Improvement: Compile once
		protected static final Pattern UNUSED_PLACEHOLDER_PATTERN = Pattern.compile("\\{\\{([a-zA-Z0-9]+)}}");
		
		protected final String template;
		protected final Map<String, String> values = new HashMap<>();
		
		protected Builder(final String template)
		{
			this.template = template;
		}
		
		/**
		 * HTML-escape, and inject value {@code value} in every {@code {{key}}} placeholder.
		 *
		 * @param key   the placeholder name
		 * @param value the value to inject
		 * @return this instance for further templating
		 */
		public Builder withValue(final String key, final String value)
		{
			this.values.put(key, HtmlUtils.htmlEscape(value));
			return this;
		}
		
		/**
		 * Inject value {@code value} in every {@code {{key}}} placeholder without HTML-escaping. Useful for injecting
		 * "sub-templates".
		 *
		 * @param key   the placeholder name
		 * @param value the value to inject
		 * @return this instance for further templating
		 */
		@SuppressWarnings("checkstyle:FinalParameters")
		public Builder withRawHtml(final String key, String value)
		{
			if(!value.isEmpty() && value.charAt(value.length() - 1) == '\n')
			{
				value = value.substring(0, value.length() - 1);
			}
			this.values.put(key, value);
			return this;
		}
		
		/**
		 * Render the template. All placeholders MUST have a corresponding value. If a placeholder does not have a
		 * corresponding value, throws {@link IllegalStateException}.
		 *
		 * @return the rendered template
		 */
		@SuppressWarnings("java:S1117")
		public String render()
		{
			String template = this.template;
			for(final Map.Entry<String, String> entry : this.values.entrySet()) // Improvement: Use entrySet
			{
				final String pattern = Pattern.quote("{{" + entry.getKey() + "}}");
				template = template.replaceAll(pattern, entry.getValue());
			}
			
			final String unusedPlaceholders = UNUSED_PLACEHOLDER_PATTERN
				.matcher(template)
				.results()
				.map(result -> result.group(1))
				.collect(Collectors.joining(", "));
			if(StringUtils.hasLength(unusedPlaceholders))
			{
				throw new IllegalStateException("Unused placeholders in template: [%s]".formatted(unusedPlaceholders));
			}
			
			return template;
		}
	}
}
