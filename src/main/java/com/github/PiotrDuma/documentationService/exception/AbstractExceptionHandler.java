package com.github.PiotrDuma.documentationService.exception;

import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.ParentLayout;

@ParentLayout(MainLayout.class)
public abstract class AbstractExceptionHandler <T extends Exception> extends Div implements HasErrorParameter<T>{
	private static final long serialVersionUID = 1L;

	private Div exceptionBody = prepareBody();
	private String message = "Unexpected error";
	private int httpServletResponse = 500;

	@Override
	public abstract int setErrorParameter(BeforeEnterEvent event, ErrorParameter<T> parameter);

	private Div prepareBody() {
		Div body = new Div();
		body.add(new H5("Something went wrong."));
		body.add(new H1("" + httpServletResponse));
		body.add(new H5(message));
		body.add(new Button("Return", click-> UI.getCurrent().navigate("")));
		
		body.getStyle().set("padding-left", "20%").set("padding-right", "20%").set("padding-top", "5%");
		return new Div(body);
	}
	
	protected String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	protected int getHttpServletResponse() {
		return httpServletResponse;
	}

	public void setHttpServletResponse(int httpServletResponse) {
		this.httpServletResponse = httpServletResponse;
	}

	protected Div getExceptionBody() {
		this.exceptionBody = prepareBody();
		return exceptionBody;
	}
	
	protected void setExceptionBody(Div exceptionBody) {
		this.exceptionBody = exceptionBody;
	}
}
