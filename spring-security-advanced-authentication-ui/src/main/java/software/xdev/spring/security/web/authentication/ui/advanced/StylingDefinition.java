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
package software.xdev.spring.security.web.authentication.ui.advanced;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;


public record StylingDefinition(
	Set<String> classNames,
	Set<String> styles
)
{
	public static class Builder
	{
		protected Set<String> classNames = new LinkedHashSet<>();
		protected Set<String> styles = new LinkedHashSet<>();
		
		public Builder classNames(final Set<String> classNames)
		{
			this.classNames = classNames;
			return this;
		}
		
		public Builder classNames(final String... classNames)
		{
			return this.classNames(Set.of(classNames));
		}
		
		public Builder className(final String className)
		{
			this.classNames.add(className);
			return this;
		}
		
		public Builder styles(final Set<String> styles)
		{
			this.styles = styles;
			return this;
		}
		
		public Builder styles(final String... styles)
		{
			return this.styles(Set.of(styles));
		}
		
		public Builder style(final String style)
		{
			this.styles.add(style);
			return this;
		}
		
		public StylingDefinition build()
		{
			return new StylingDefinition(this.classNames, this.styles);
		}
	}
	
	public String classNameString()
	{
		return this.classNames().stream().map(s -> " " + s).collect(Collectors.joining());
	}
	
	public String styleString()
	{
		return this.styles().stream().map(s -> s + ";").collect(Collectors.joining());
	}
}
