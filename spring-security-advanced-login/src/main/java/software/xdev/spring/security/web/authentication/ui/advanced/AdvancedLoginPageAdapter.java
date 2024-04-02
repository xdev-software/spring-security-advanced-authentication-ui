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
