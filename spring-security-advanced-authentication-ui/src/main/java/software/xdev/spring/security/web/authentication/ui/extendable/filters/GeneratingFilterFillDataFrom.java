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
package software.xdev.spring.security.web.authentication.ui.extendable.filters;

import java.util.Collection;
import java.util.function.Consumer;

import software.xdev.spring.security.web.authentication.ui.extendable.FieldAccessor;


public interface GeneratingFilterFillDataFrom<F>
{
	void fillDataFrom(F source);
	
	default void fillDataFrom(
		final Class<F> fieldContainer,
		final F source,
		final Collection<CopyInfo<?>> copyInfos)
	{
		final FieldAccessor fieldAccessor = new FieldAccessor();
		copyInfos.forEach(c -> c.setter().accept(fieldAccessor.getValue(fieldContainer, source, c.fieldName())));
	}
	
	record CopyInfo<T>(
		String fieldName,
		Consumer<T> setter)
	{
	}
}
