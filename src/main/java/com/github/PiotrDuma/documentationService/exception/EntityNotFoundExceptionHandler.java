package com.github.PiotrDuma.documentationService.exception;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;

public class EntityNotFoundExceptionHandler extends AbstractExceptionHandler<EntityNotFoundException>{
	private static final long serialVersionUID = 1L;

	@Override
	public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<EntityNotFoundException> parameter) {
		super.setHttpServletResponse(HttpServletResponse.SC_NOT_FOUND);
		super.setMessage("Entity not found.");
		add(super.getExceptionBody());
		return HttpServletResponse.SC_NOT_FOUND;
	}
}