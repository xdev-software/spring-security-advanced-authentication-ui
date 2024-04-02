package software.xdev.spring.security.web.authentication.ui.advanced;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.DefaultLoginPageConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter;

import software.xdev.spring.security.web.authentication.ui.advanced.filters.AdvancedLoginPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.advanced.filters.AdvancedLogoutPageGeneratingFilter;
import software.xdev.spring.security.web.authentication.ui.extendable.ExtendableLoginPageAdapter;


class AdapterTest
{
	public static final String EXAMPLE_URL = "https://example.org";
	
	@Test
	void checkInstallAdapterConstructor() throws Exception
	{
		final CheckInstallResult result = this.checkInstallAdapter(
			f -> f.setLoginPageUrl(EXAMPLE_URL),
			AdvancedLoginPageAdapter::new);
		
		Assertions.assertEquals(EXAMPLE_URL, result.advancedLoginPageGeneratingFilter().getLoginPageUrl());
	}
	
	@Test
	void checkInstallAdapterInitMethod() throws Exception
	{
		final CheckInstallResult result = this.checkInstallAdapter(
			f -> f.setLoginPageUrl(EXAMPLE_URL),
			http -> new AdvancedLoginPageAdapter<>(b -> b
				.installWith(ExtendableLoginPageAdapter.InstallWith.INIT_METHOD)));
		
		Assertions.assertEquals(EXAMPLE_URL, result.advancedLoginPageGeneratingFilter().getLoginPageUrl());
	}
	
	@Test
	void checkInstallAdapterNoCopy() throws Exception
	{
		final CheckInstallResult result = this.checkInstallAdapter(
			f -> f.setLoginPageUrl(EXAMPLE_URL),
			http -> new AdvancedLoginPageAdapter<>(b -> b
				.loginPageGeneratingFilterSupplier(() -> new AdvancedLoginPageGeneratingFilter()
				{
					// Test problem: No one - e.g. http.formLogin(...)#init or .oAuth2Login(...)#init
					// - enables the filter
					// Therefore it's not installed in DefaultLoginPageConfigurer#configure
					// Workaround: Force enable
					@Override
					public boolean isEnabled()
					{
						return true;
					}
				})
				.copyDataFromExistingFilters(false)));
		
		Assertions.assertNull(result.advancedLoginPageGeneratingFilter().getLoginPageUrl());
	}
	
	@SuppressWarnings({"unchecked", "rawtypes"})
	protected CheckInstallResult checkInstallAdapter(
		final Consumer<DefaultLoginPageGeneratingFilter> loginPageGeneratingFilterConsumer,
		final Function<HttpSecurity, AdvancedLoginPageAdapter<? extends HttpSecurityBuilder<?>>> adapterFunc)
		throws Exception
	{
		final ObjectPostProcessor<Object> dummyPostProcessor = new ObjectPostProcessor<>()
		{
			@Override
			public <O> O postProcess(final O object)
			{
				return object;
			}
		};
		
		final HttpSecurity http = new HttpSecurity(
			dummyPostProcessor,
			new AuthenticationManagerBuilder(dummyPostProcessor)
			{
			},
			new HashMap<>());
		
		final DefaultLoginPageConfigurer<HttpSecurity> defaultLoginPageConfigurer = new DefaultLoginPageConfigurer<>();
		http
			.with(defaultLoginPageConfigurer, Customizer.withDefaults())
			.logout(Customizer.withDefaults());
		
		final Field fLoginPageGeneratingFilter =
			DefaultLoginPageConfigurer.class.getDeclaredField("loginPageGeneratingFilter");
		fLoginPageGeneratingFilter.setAccessible(true);
		final DefaultLoginPageGeneratingFilter defaultLoginPageGeneratingFilter =
			(DefaultLoginPageGeneratingFilter)fLoginPageGeneratingFilter.get(defaultLoginPageConfigurer);
		// Enable manually as filter is otherwise not applied (see isEnabled)
		defaultLoginPageGeneratingFilter.setOauth2LoginEnabled(true);
		
		Optional.ofNullable(loginPageGeneratingFilterConsumer)
			.ifPresent(c -> c.accept(defaultLoginPageGeneratingFilter));
		
		final AdvancedLoginPageAdapter<?> adapter = adapterFunc.apply(http);
		final List<AbstractHttpConfigurer> configurersToInvoke =
			List.of(defaultLoginPageConfigurer, adapter);
		
		configurersToInvoke.forEach(c -> Assertions.assertDoesNotThrow(() -> c.init(http)));
		configurersToInvoke.forEach(c -> Assertions.assertDoesNotThrow(() -> c.configure(http)));
		
		final Method mPerformBuild = HttpSecurity.class.getDeclaredMethod("performBuild");
		mPerformBuild.setAccessible(true);
		final DefaultSecurityFilterChain filterChain = (DefaultSecurityFilterChain)mPerformBuild.invoke(http);
		
		final AdvancedLoginPageGeneratingFilter advancedLoginPageGeneratingFilter =
			getFromFilterChain(filterChain, AdvancedLoginPageGeneratingFilter.class);
		final AdvancedLogoutPageGeneratingFilter advancedLogoutPageGeneratingFilter =
			getFromFilterChain(filterChain, AdvancedLogoutPageGeneratingFilter.class);
		
		Assertions.assertNotNull(advancedLoginPageGeneratingFilter, "AdvancedLoginPageGeneratingFilter not found");
		Assertions.assertNotNull(advancedLogoutPageGeneratingFilter, "AdvancedLogoutPageGeneratingFilter not found");
		
		return new CheckInstallResult(
			http,
			adapter,
			advancedLoginPageGeneratingFilter,
			advancedLogoutPageGeneratingFilter
		);
	}
	
	static <F> F getFromFilterChain(final DefaultSecurityFilterChain filterChain, final Class<F> clazz)
	{
		return filterChain.getFilters()
			.stream()
			.filter(clazz::isInstance)
			.map(clazz::cast)
			.findFirst()
			.orElse(null);
	}
	
	record CheckInstallResult(
		HttpSecurity http,
		AdvancedLoginPageAdapter<?> adapter,
		AdvancedLoginPageGeneratingFilter advancedLoginPageGeneratingFilter,
		AdvancedLogoutPageGeneratingFilter advancedLogoutPageGeneratingFilter
	)
	{
	}
}
