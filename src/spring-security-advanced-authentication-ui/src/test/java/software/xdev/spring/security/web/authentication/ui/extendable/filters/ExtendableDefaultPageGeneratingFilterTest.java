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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;
import org.springframework.util.StringUtils;


class ExtendableDefaultPageGeneratingFilterTest
{
	@Test
	void checkLoginCompatibility()
	{
		Assertions.assertAll(this.check(
			DefaultLoginPageGeneratingFilter.class,
			ExtendableDefaultLoginPageGeneratingFilter.class));
	}
	
	@Test
	void checkLogoutCompatibility()
	{
		Assertions.assertAll(this.check(
			DefaultLogoutPageGeneratingFilter.class,
			ExtendableDefaultLogoutPageGeneratingFilter.class));
	}
	
	Stream<Executable> check(final Class<?> expectedClass, final Class<?> testClass)
	{
		final List<Field> expectedFields = Stream.of(expectedClass.getDeclaredFields())
			.filter(f -> Modifier.isPrivate(f.getModifiers()) && !Modifier.isStatic(f.getModifiers()))
			.toList();
		
		final Method[] testClassDeclaredMethods = testClass.getDeclaredMethods();
		
		return expectedFields.stream()
			.map(expectedField -> () -> {
				Assertions.assertDoesNotThrow(
					() -> testClass.getDeclaredField(expectedField.getName()),
					"Failed to find field " + expectedField.getName());
				
				final String setMethod = "set" + StringUtils.capitalize(expectedField.getName());
				Assertions.assertTrue(
					Stream.of(testClassDeclaredMethods)
						.anyMatch(m -> Objects.equals(setMethod, m.getName())),
					"Failed to find method " + expectedField.getName());
			});
	}
	
	@Test
	void copyFilterFunctionality()
	{
		final DefaultLoginPageGeneratingFilter defaultFilter = new DefaultLoginPageGeneratingFilter();
		defaultFilter.setOauth2LoginEnabled(true);
		final String loginPageUrl = "http://example.org";
		defaultFilter.setLoginPageUrl(loginPageUrl);
		
		final ExtendableDefaultLoginPageGeneratingFilter extendableFilter =
			new ExtendableDefaultLoginPageGeneratingFilter();
		extendableFilter.fillDataFrom(defaultFilter);
		
		Assertions.assertTrue(extendableFilter.isEnabled());
		Assertions.assertEquals(loginPageUrl, extendableFilter.getLoginPageUrl());
	}
}
