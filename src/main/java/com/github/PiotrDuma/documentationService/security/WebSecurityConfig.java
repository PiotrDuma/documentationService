package com.github.PiotrDuma.documentationService.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final String LOGIN_URL = "/login";
	private static final String LOGOUT_URL = "/logout";
	private static final String START_PAGE = "/start";
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				//avoid token handler error
			.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll() 
				.anyRequest().authenticated()
				.and()
			.oauth2Login()
				.loginPage(LOGIN_URL)
//				.defaultSuccessUrl("/", true)
				.permitAll()
				.and()
			.logout()
				.logoutUrl(LOGOUT_URL)
				.logoutSuccessUrl(START_PAGE)
				.permitAll()
//			.and().exceptionHandling().authenticationEntryPoint(new AuthEntryPoint())
    		.and().csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) {
	    web.ignoring().antMatchers(
	    	"/start", //home page auth request unnecessary
	    	
	        "/VAADIN/**",
			"/favicon.ico",
	        "/icons/**",
	        "/images/**",
	        "/styles/**",
			"/frontend/**",
			"/webjars/**",
			"/sw.js");
	}
	
	private final class AuthEntryPoint implements AuthenticationEntryPoint{

		@Override
		public void commence(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException authException) throws IOException, ServletException {
			response.sendRedirect(START_PAGE);	
		}
		
	}
	
}
