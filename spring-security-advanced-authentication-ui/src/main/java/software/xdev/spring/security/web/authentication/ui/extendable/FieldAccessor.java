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
package software.xdev.spring.security.web.authentication.ui.extendable;

import java.lang.reflect.Field;


public class FieldAccessor
{
	@SuppressWarnings({"java:S3011"})
	public void setValue(
		final Class<?> fieldContainer,
		final Object object,
		final String fieldName,
		final Object value)
	{
		try
		{
			final Field field = fieldContainer.getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
		}
		catch(final NoSuchFieldException | IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
	}
	
	@SuppressWarnings({"java:S3011", "unchecked"})
	public <T> T getValue(
		final Class<?> fieldContainer,
		final Object object,
		final String fieldName)
	{
		try
		{
			final Field field = fieldContainer.getDeclaredField(fieldName);
			field.setAccessible(true);
			final Object value = field.get(object);
			return (T)value;
		}
		catch(final NoSuchFieldException | IllegalAccessException e)
		{
			throw new IllegalStateException(e);
		}
	}
}
