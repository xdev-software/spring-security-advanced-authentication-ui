/*
 * Copyright © 2023 XDEV Software (https://xdev.software)
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

import java.util.Set;
import java.util.stream.Collectors;


public record StylingDefinition(
	Set<String> classNames,
	String style
)
{
	public String classNameString()
	{
		return this.classNames().stream().map(s -> " " + s).collect(Collectors.joining());
	}
	
	public static StylingDefinition classNames(final String... classNames)
	{
		return new StylingDefinition(Set.of(classNames), null);
	}
	
	public static StylingDefinition style(final String style)
	{
		return new StylingDefinition(Set.of(), style);
	}
}
