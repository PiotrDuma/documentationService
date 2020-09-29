package com.github.PiotrDuma.documentationService.frontend.action.components.editor;

import com.github.PiotrDuma.documentationService.model.Action;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractActionEditor extends VerticalLayout{
	private static final long serialVersionUID = 1L;

	abstract public VerticalLayout getBody();

	public abstract boolean isValid();
	public abstract void setAction(Action action);
	public abstract Action getAction();
}
