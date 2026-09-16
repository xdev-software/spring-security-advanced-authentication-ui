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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultPageGeneratingFilter;


public interface AdvancedSharedPageGeneratingFilter<S extends AdvancedSharedPageGeneratingFilter<S>>
	extends ExtendableDefaultPageGeneratingFilter
{
	List<String> DEFAULT_HEADER_ELEMENTS = List.of(
		"<link "
			+ "href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css\" "
			+ "rel=\"stylesheet\" "
			+ "integrity=\"sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB\" "
			+ "crossorigin=\"anonymous\"/>");
	
	S setHeaderElements(List<String> headerElements);
	
	S addHeaderElement(String element);
	
	S pageTitle(String pageTitle);
	
	Map<String, String> DEFAULT_HEADER_METAS = Map.of(
		"viewport", "width=device-width, initial-scale=1, shrink-to-fit=no");
	
	S setHeaderMetas(Map<String, String> headerMetas);
	
	S addHeaderMeta(String name, String content);
	
	default String generateHeader(
		final Map<String, String> headerMetas,
		final String pageTitle,
		final List<String> headerElements)
	{
		return "  <head>"
			+ "    <meta charset='utf-8'>"
			+ headerMetas.entrySet()
			.stream()
			.map(e -> "    <meta name='" + e.getKey() + "' content='" + e.getValue() + "'>")
			.collect(Collectors.joining())
			+ Optional.ofNullable(pageTitle).map(t -> "    <title>" + t + "</title>").orElse("")
			+ headerElements.stream().map(s -> "    " + s).collect(Collectors.joining())
			+ "  </head>";
	}
	
	default String renderHiddenInputs(final Set<Map.Entry<String, String>> entries)
	{
		return entries.stream()
			.map(e -> "<input name=\"" + e.getKey() + "\" type=\"hidden\" value=\"" + e.getValue() + "\" />\n")
			.collect(Collectors.joining());
	}
}
