package com.github.PiotrDuma.documentationService.frontend.action.components.editor;


import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public final class ActionEditorSeeding extends UniqueActionEditor {
	private static final long serialVersionUID = 1L;

	
	public ActionEditorSeeding() {
		super();
		action.setType(ActionType.GATHERING);
	}

	public ActionEditorSeeding(Action action) {
		super(action);
	}
	
	@Override
	protected void init() {
		custom();
		add(new H3("Nowa akcja: sadzenie"));
		FormLayout layout = new FormLayout();
		layout.addFormItem(details, "Typ nasienia:");
		layout.addFormItem(datePicker, "Data:");
		layout.addFormItem(new HorizontalLayout(value, new Text("PLN")), "Koszt/cena surowców:");
		layout.addFormItem(note, "Szczegóły: ");
		add(layout);
	}
	
	private void custom() {
		binder.removeBinding(quantity);
	}
}
