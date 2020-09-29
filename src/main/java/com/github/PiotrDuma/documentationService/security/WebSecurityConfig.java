package com.github.PiotrDuma.documentationService.security;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.github.PiotrDuma.documentationService.security.OAuth2Config.CustomAuthorizationRequestResolver;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_URL = "/logout";
	private static final String START_PAGE = "/start";

	@Autowired
    private ClientRegistrationRepository clientRegistrationRepository;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				//avoid token handler error
			.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() 
				.anyRequest().authenticated()
				.and()
			.oauth2Login()
				.authorizationEndpoint()
				    .authorizationRequestResolver(
				            new CustomAuthorizationRequestResolver(clientRegistrationRepository))
			    .and()
				.loginPage(LOGIN_URL)
//				.defaultSuccessUrl("/", true)
				.permitAll()
				.and()
			.logout()
				.logoutUrl(LOGOUT_URL)
				.logoutSuccessUrl(START_PAGE)
				.deleteCookies("JSESSIONID", "JWT").invalidateHttpSession(true)
				.permitAll()
//			.and().exceptionHandling().authenticationEntryPoint(new AuthEntryPoint())
    		.and().csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers("/start", // home page auth request unnecessary

				"/VAADIN/**", "/favicon.ico", "/icons/**", "/images/**", "/styles/**", "/frontend/**", "/webjars/**",
				"/sw.js");
	}

	//set entry point to starting page instead of login.
	private final class AuthEntryPoint implements AuthenticationEntryPoint {
		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			response.sendRedirect(START_PAGE);
		}
	}
}
