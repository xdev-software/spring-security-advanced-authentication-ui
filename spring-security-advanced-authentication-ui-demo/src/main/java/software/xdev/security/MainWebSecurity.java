package software.xdev.security;

import static java.util.Map.entry;
import static software.xdev.security.CSP.POLICY_NONE;
import static software.xdev.security.CSP.POLICY_SELF;

import java.time.Year;
import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.security.web.savedrequest.NullRequestCache;

import software.xdev.spring.security.web.authentication.ui.advanced.AdvancedLoginPageAdapter;
import software.xdev.spring.security.web.authentication.ui.advanced.config.AdditionalOAuth2ClientProperties;


@EnableWebSecurity
@Configuration
// Load additional OAuth2ClientProperties
@EnableConfigurationProperties(AdditionalOAuth2ClientProperties.class)
public class MainWebSecurity
{
	@Bean(name = "mainSecurityFilterChainBean")
	public SecurityFilterChain configure(
		final HttpSecurity http,
		final AdditionalOAuth2ClientProperties additionalOAuth2ClientProperties) throws Exception
	{
		http.with(new AdvancedLoginPageAdapter<>(http), c -> c
				.customizePages(p -> p
					// No remote communication -> Use local resources
					.setHeaderElements(List.of(
						"<link href=\"/lib/bootstrap-5.3.3.min.css\" rel=\"stylesheet\"/>",
						"<link href=\"/lib/theme.css\" rel=\"stylesheet\"/>",
						"<script src=\"/lib/bootstrap-5.3.3.bundle.min.js\"></script>",
						"<script src=\"/lib/theme.js\"></script>"
					)))
				.customizeLoginPage(p -> p
					.additionalOAuth2RegistrationProperties(additionalOAuth2ClientProperties.getRegistration())
					.additionalStylingData(sd -> sd
						.body(s -> s.styles("background-image:url(\"/assets/sky.avif\")", "background-size:cover"))
						.container(s -> s.classNames("d-flex", "h-100"))
						.main(s -> s.classNames("align-middle").styles("background:var(--bs-body-bg)")))
					.header("<div class='d-flex justify-content-center'>"
						+ "<a href='https://xdev.software'>"
						+ "  <img src='/assets/XDEV_LOGO.svg' alt='XDEV' style='max-width:100%;"
						+ "height:calc(var(--bs-body-font-size) * 2.5)'></img>"
						+ "</a>"
						+ "</div>"
						+ "<h2 class='h2 mb-3 text-center'>Advanced Auth UI Demo</h2>")
					.footer("<p class='mt-4 mb-1 text-body-secondary text-center'>© " + Year.now().getValue()
						+ "  <a class='link-underline link-underline-opacity-0 link-underline-opacity-100-hover'"
						+ " href='https://xdev.software' target='_blank'>"
						+ "    XDEV Software"
						+ "  </a>"
						+ "</p>")))
			.headers(h -> h
				.referrerPolicy(r -> r.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN))
				.contentSecurityPolicy(csp -> csp.policyDirectives(this.getCSP())))
			.formLogin(Customizer.withDefaults())
			.oauth2Login(c -> c.defaultSuccessUrl("/"))
			.authorizeHttpRequests(urlRegistry -> urlRegistry.anyRequest().authenticated())
			.requestCache(c -> c.requestCache(new NullRequestCache()));
		
		return http.build();
	}
	
	// Example CSP
	protected String getCSP()
	{
		return CSP.build(Map.ofEntries(
			entry(
				"default-src",
				POLICY_SELF
					+ (this.isDevMode()
					// Allow ws://localhost:* in Demo mode for SpringbootDevTools
					? " ws://localhost:*"
					: "")),
			entry("script-src", POLICY_SELF + " 'unsafe-inline'"),
			entry("style-src", POLICY_SELF + " 'unsafe-inline'"),
			entry("font-src", POLICY_SELF),
			entry("img-src", POLICY_SELF + " data:"),
			// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/object-src
			// https://csp.withgoogle.com/docs/strict-csp.html
			entry("object-src", POLICY_NONE),
			// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/base-uri
			entry("base-uri", POLICY_SELF),
			// https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Content-Security-Policy/form-action
			// When using 'self':
			// * Webkit based Browsers have problems here: https://github.com/w3c/webappsec-csp/issues/8
			// * Firefox is
			// As of 2024-03 CSP3 added 'unsafe-allow-redirects' however it's not implemented by any browser yet
			// Fallback for now '*'
			entry("form-action", "*"),
			// Replaces X-Frame-Options
			entry("frame-src", POLICY_SELF),
			entry("frame-ancestors", POLICY_SELF)));
	}
	
	protected boolean isDevMode()
	{
		try
		{
			Class.forName("org.springframework.boot.devtools.settings.DevToolsSettings");
			return true;
		}
		catch(final ClassNotFoundException nf)
		{
			return false;
		}
	}
}
