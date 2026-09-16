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

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;
import org.springframework.security.web.authentication.ui.DefaultLogoutPageGeneratingFilter;

import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultLoginPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultLogoutPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.extendable.filters.ExtendableDefaultPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.extendable.filters.GeneratingFilterFillDataFrom;


@SuppressWarnings("java:S119")
public abstract class ExtendableLoginPageAdapter<
	SELF extends ExtendableLoginPageAdapter<SELF, SHARED_FILTER, LOGIN_FILTER, LOGOUT_FILTER, H>,
	SHARED_FILTER extends ExtendableDefaultPageGeneratingFilter,
	LOGIN_FILTER extends ExtendableDefaultLoginPageGeneratingFilter,
	LOGOUT_FILTER extends ExtendableDefaultLogoutPageGeneratingFilter,
	H extends HttpSecurityBuilder<H>>
	extends AbstractHttpConfigurer<SELF, H>
{
	protected final LOGIN_FILTER loginPageGeneratingFilter;
	protected final LOGOUT_FILTER logoutPageGeneratingFilter;
	protected final boolean copyDataFromExistingFilters;
	protected final boolean installUsingInitMethod;
	
	private Consumer<SHARED_FILTER> customizePages;
	protected Consumer<LOGIN_FILTER> customizeLoginPage;
	protected Consumer<LOGOUT_FILTER> customizeLogoutPage;
	
	
	public static class InitConfiguration<
		LOGIN_FILTER extends ExtendableDefaultLoginPageGeneratingFilter,
		LOGOUT_FILTER extends ExtendableDefaultLogoutPageGeneratingFilter>
	{
		/**
		 * Used for constructor initialization
		 */
		protected HttpSecurity httpSecurity;
		protected Supplier<LOGIN_FILTER> loginPageGeneratingFilterSupplier;
		protected Supplier<LOGOUT_FILTER> logoutPageGeneratingFilterSupplier;
		/**
		 * Copy data over from existing PageGeneratingFilters (required when configuration using
		 * {@link DefaultLoginPageConfigurer} was already done)
		 */
		protected boolean copyDataFromExistingFilters = true;
		protected InstallWith installWith;
		
		public InitConfiguration(
			final Supplier<LOGIN_FILTER> loginPageGeneratingFilterSupplier,
			final Supplier<LOGOUT_FILTER> logoutPageGeneratingFilterSupplier)
		{
			this.loginPageGeneratingFilterSupplier = loginPageGeneratingFilterSupplier;
			this.logoutPageGeneratingFilterSupplier = logoutPageGeneratingFilterSupplier;
		}
		
		public InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER> httpSecurity(final HttpSecurity httpSecurity)
		{
			this.httpSecurity = httpSecurity;
			return this;
		}
		
		public InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER> loginPageGeneratingFilterSupplier(
			final Supplier<LOGIN_FILTER> loginPageGeneratingFilterSupplier)
		{
			this.loginPageGeneratingFilterSupplier = loginPageGeneratingFilterSupplier;
			return this;
		}
		
		public InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER> logoutPageGeneratingFilterSupplier(
			final Supplier<LOGOUT_FILTER> logoutPageGeneratingFilterSupplier)
		{
			this.logoutPageGeneratingFilterSupplier = logoutPageGeneratingFilterSupplier;
			return this;
		}
		
		public InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER> copyDataFromExistingFilters(
			final boolean copyDataFromExistingFilters)
		{
			this.copyDataFromExistingFilters = copyDataFromExistingFilters;
			return this;
		}
		
		public InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER> installWith(final InstallWith installWith)
		{
			this.installWith = installWith;
			return this;
		}
		
		public HttpSecurity getHttpSecurity()
		{
			return this.httpSecurity;
		}
		
		public Supplier<LOGIN_FILTER> getLoginPageGeneratingFilterSupplier()
		{
			return this.loginPageGeneratingFilterSupplier;
		}
		
		public Supplier<LOGOUT_FILTER> getLogoutPageGeneratingFilterSupplier()
		{
			return this.logoutPageGeneratingFilterSupplier;
		}
		
		public boolean isCopyDataFromExistingFilters()
		{
			return this.copyDataFromExistingFilters;
		}
		
		public InstallWith getInstallWith()
		{
			return this.installWith;
		}
	}
	
	
	public enum InstallWith
	{
		/**
		 * Install inside the constructor.
		 * <p>
		 * This is the recommended way as it occurs before {@link DefaultLoginPageConfigurer#init(HttpSecurityBuilder)}
		 * is executed and therefore init is only done once. <br/> Note however that you need to ensure that this is
		 * executed BEFORE {@link DefaultLoginPageConfigurer#init(HttpSecurityBuilder)}, which is usually the case.
		 * </p>
		 *
		 * @implNote This method is automatically chosen when {@link InitConfiguration#httpSecurity} is NOT
		 * <code>null</code>
		 */
		CONSTRUCTOR,
		/**
		 * Install inside the {@link ExtendableLoginPageAdapter#init(HttpSecurityBuilder)} method.
		 * <p>
		 * Not recommended as {@link DefaultLoginPageConfigurer#init(HttpSecurityBuilder)} AND
		 * {@link ExtendableLoginPageAdapter#init(HttpSecurityBuilder)} are executed
		 * </p>
		 */
		INIT_METHOD
	}
	
	protected ExtendableLoginPageAdapter(
		final Supplier<InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER>> s,
		final Consumer<InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER>> c)
	{
		this(crateAndApplyConsumer(s, c));
	}
	
	protected static <T> T crateAndApplyConsumer(
		final Supplier<T> supplier,
		final Consumer<T> consumer)
	{
		final T obj = supplier.get();
		if(consumer != null)
		{
			consumer.accept(obj);
		}
		return obj;
	}
	
	protected ExtendableLoginPageAdapter(final InitConfiguration<LOGIN_FILTER, LOGOUT_FILTER> initConfig)
	{
		this(
			initConfig.getHttpSecurity(),
			initConfig.getLoginPageGeneratingFilterSupplier(),
			initConfig.getLogoutPageGeneratingFilterSupplier(),
			initConfig.isCopyDataFromExistingFilters(),
			initConfig.getInstallWith());
	}
	
	@SuppressWarnings("unchecked")
	protected ExtendableLoginPageAdapter(
		final HttpSecurity httpSecurity,
		final Supplier<LOGIN_FILTER> loginPageGeneratingFilterSupplier,
		final Supplier<LOGOUT_FILTER> logoutPageGeneratingFilterSupplier,
		final boolean copyDataFromExistingFilters,
		final InstallWith installWith)
	{
		this.loginPageGeneratingFilter = loginPageGeneratingFilterSupplier.get();
		this.logoutPageGeneratingFilter = logoutPageGeneratingFilterSupplier.get();
		this.copyDataFromExistingFilters = copyDataFromExistingFilters;
		
		this.installUsingInitMethod = installWith == InstallWith.INIT_METHOD
			|| installWith == null && httpSecurity == null;
		
		if(!this.installUsingInitMethod)
		{
			this.install(httpSecurity.getConfigurer(DefaultLoginPageConfigurer.class));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(final H http)
	{
		if(!this.installUsingInitMethod)
		{
			return;
		}
		
		this.install(http.getConfigurer(DefaultLoginPageConfigurer.class));
		// Override DefaultLoginPageConfigurer behavior
		http.setSharedObject(DefaultLoginPageGeneratingFilter.class, this.loginPageGeneratingFilter);
	}
	
	@SuppressWarnings("rawtypes")
	protected void install(final DefaultLoginPageConfigurer loginPageConfigurer)
	{
		Optional.ofNullable(loginPageConfigurer)
			.ifPresent(c -> {
				final FieldAccessor fieldAccessor = new FieldAccessor();
				
				Stream.of(
						new ProcessInfo<>(
							"loginPageGeneratingFilter",
							DefaultLoginPageGeneratingFilter.class,
							this.loginPageGeneratingFilter),
						new ProcessInfo<>(
							"logoutPageGeneratingFilter",
							DefaultLogoutPageGeneratingFilter.class,
							this.logoutPageGeneratingFilter))
					.forEach(p -> {
						final Class<DefaultLoginPageConfigurer> clazz = DefaultLoginPageConfigurer.class;
						if(this.copyDataFromExistingFilters)
						{
							p.filterFillDataFrom(fieldAccessor.getValue(clazz, c, p.fieldName()));
						}
						fieldAccessor.setValue(clazz, c, p.fieldName(), p.filter());
					});
			});
	}
	
	protected record ProcessInfo<B, F extends GeneratingFilterFillDataFrom<B>>(
		String fieldName,
		Class<B> baseFilterClass,
		F filter)
	{
		protected void filterFillDataFrom(final Object source)
		{
			Optional.ofNullable(source)
				.filter(this.baseFilterClass()::isInstance)
				.map(this.baseFilterClass()::cast)
				.ifPresent(this.filter()::fillDataFrom);
		}
	}
	
	@Override
	public void configure(final H builder)
	{
		Optional.ofNullable(this.customizePages).ifPresent(c ->
			Stream.of(this.loginPageGeneratingFilter, this.logoutPageGeneratingFilter)
				// Cast to SHARED_FILTER is required as it can't be specified in Generics
				.forEach(f -> c.accept((SHARED_FILTER)f)));
		Optional.ofNullable(this.customizeLoginPage).ifPresent(c -> c.accept(this.loginPageGeneratingFilter));
		Optional.ofNullable(this.customizeLogoutPage).ifPresent(c -> c.accept(this.logoutPageGeneratingFilter));
	}
	
	public SELF customizePages(final Consumer<SHARED_FILTER> customizePages)
	{
		this.customizePages = customizePages;
		return this.self();
	}
	
	public SELF customizeLoginPage(final Consumer<LOGIN_FILTER> customizeLoginPage)
	{
		this.customizeLoginPage = customizeLoginPage;
		return this.self();
	}
	
	public SELF customizeLogoutPage(final Consumer<LOGOUT_FILTER> customizeLogoutPage)
	{
		this.customizeLogoutPage = customizeLogoutPage;
		return this.self();
	}
	
	@SuppressWarnings("unchecked")
	protected SELF self()
	{
		return (SELF)this;
	}
}
