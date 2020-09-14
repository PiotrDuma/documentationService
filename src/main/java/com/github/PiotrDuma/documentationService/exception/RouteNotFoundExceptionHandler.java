package com.github.PiotrDuma.documentationService.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;


public class RouteNotFoundExceptionHandler extends AbstractExceptionHandler<NotFoundException>{
	private static final long serialVersionUID = 1L;
	
	VaadinRequest vaadinRequest = VaadinService.getCurrentRequest();
	HttpServletRequest httpServletRequest = ((VaadinServletRequest)vaadinRequest).getHttpServletRequest();
	
	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<NotFoundException> parameter) {
		super.setHttpServletResponse(HttpServletResponse.SC_NOT_FOUND);
		super.setMessage("Could not navigate to '"+ httpServletRequest.getRequestURL().toString()+"'.");
		add(super.getExceptionBody());
		return HttpServletResponse.SC_NOT_FOUND;
	}
}