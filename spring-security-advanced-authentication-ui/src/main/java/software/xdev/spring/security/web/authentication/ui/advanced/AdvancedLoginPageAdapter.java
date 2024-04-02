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

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import software.xdev.spring.security.web.authentication.ui.advanced.filters.AdvancedLoginPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.advanced.filters.AdvancedLogoutPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.advanced.filters.AdvancedSharedPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.extendable.ExtendableLoginPageAdapter;


public class AdvancedLoginPageAdapter<H extends HttpSecurityBuilder<H>>
	extends ExtendableLoginPageAdapter<
	AdvancedLoginPageAdapter<H>,
	AdvancedSharedPageGeneratingFilter<?>,
	AdvancedLoginPageGeneratingFilter,
	AdvancedLogoutPageGeneratingFilter,
	H>
{
	public AdvancedLoginPageAdapter(
		final Consumer<InitConfiguration<AdvancedLoginPageGeneratingFilter, AdvancedLogoutPageGeneratingFilter>> c)
	{
		super(() -> new InitConfiguration<>(
			AdvancedLoginPageGeneratingFilter::new,
			AdvancedLogoutPageGeneratingFilter::new), c);
	}
	
	public AdvancedLoginPageAdapter(
		final InitConfiguration<AdvancedLoginPageGeneratingFilter, AdvancedLogoutPageGeneratingFilter> initConfig)
	{
		super(initConfig);
	}
	
	public AdvancedLoginPageAdapter(
		final HttpSecurity httpSecurity,
		final Supplier<AdvancedLoginPageGeneratingFilter> loginPageGeneratingFilterSupplier,
		final Supplier<AdvancedLogoutPageGeneratingFilter> logoutPageGeneratingFilterSupplier,
		final boolean copyDataFromExistingFilters,
		final InstallWith installWith)
	{
		super(
			httpSecurity,
			loginPageGeneratingFilterSupplier,
			logoutPageGeneratingFilterSupplier,
			copyDataFromExistingFilters,
			installWith);
	}
}
