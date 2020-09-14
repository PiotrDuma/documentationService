package com.github.PiotrDuma.documentationService.exception.security;

import org.springframework.security.core.context.SecurityContextHolder;

import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;

public class AuthenticationExceptionHandler extends Div implements HasErrorParameter<AuthenticationException>{
	private static final long serialVersionUID = 1L;

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AuthenticationException> parameter) {
		SecurityContextHolder.clearContext();
		UI.getCurrent().getSession().close();
		UI.getCurrent().navigate("login");

		return HTTPResponse.SC_UNAUTHORIZED;
	}

}
