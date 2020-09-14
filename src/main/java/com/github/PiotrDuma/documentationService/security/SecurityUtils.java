package com.github.PiotrDuma.documentationService.security;

import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;


import com.vaadin.flow.server.ServletHelper.RequestType;
import com.vaadin.flow.shared.ApplicationConstants;



@SuppressWarnings("deprecation")
public final class SecurityUtils {

	// deprecated, but needed to handle oauth json token, based on vaadin bakery app.
	// https://github.com/vaadin/flow/issues/4212

	static boolean isFrameworkInternalRequest(HttpServletRequest request) {
		final String parameterValue = request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER);
		return parameterValue != null
				&& Stream.of(RequestType.values()).anyMatch(r -> r.getIdentifier().equals(parameterValue));
	}


}
