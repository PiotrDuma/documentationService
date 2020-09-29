package com.github.PiotrDuma.documentationService.frontend.action.components.editor;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public final class ActionEditorTillage extends UniqueActionEditor {
	private static final long serialVersionUID = 1L;

	public ActionEditorTillage() {
		super();
		action.setType(ActionType.TILLAGE);
	}

	public ActionEditorTillage(Action action) {
		super(action);
	}
	
	@Override
	protected void init() {
		add(new H3("Nowa akcja: uprawa"));
		custom();
		
		FormLayout layout = new FormLayout();
		layout.addFormItem(details, "Rodzaj uprawy:");
		layout.addFormItem(datePicker, "Data:");
		layout.addFormItem(new HorizontalLayout(value, new Text("PLN")), "Koszt wykonania:");
		layout.addFormItem(note, "Szczegóły: ");
		add(layout);
	}
	
	private void custom() {
		details.setPlaceholder("np. uprawa/oranie");
		binder.removeBinding(quantity);
	}
}
