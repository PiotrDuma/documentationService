package com.github.PiotrDuma.documentationService.exception;

import org.springframework.security.access.AccessDeniedException;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;


public class AccessDeniedExceptionHandler extends AbstractExceptionHandler<AccessDeniedException>{
	private static final long serialVersionUID = 1L;

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<AccessDeniedException> parameter) {
		super.setHttpServletResponse(HTTPResponse.SC_FORBIDDEN);
		super.setMessage("Access denied. Cannot navigate to location without correct access rights.");
		add(super.getExceptionBody());
		return HTTPResponse.SC_FORBIDDEN;
	}
}