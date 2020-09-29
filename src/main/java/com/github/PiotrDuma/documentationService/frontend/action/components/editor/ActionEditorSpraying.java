package com.github.PiotrDuma.documentationService.frontend.action.components.editor;


import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public final class ActionEditorSpraying extends UniqueActionEditor {
	private static final long serialVersionUID = 1L;

	public ActionEditorSpraying() {
		super();
		action.setType(ActionType.SPRAYING);
	}

	public ActionEditorSpraying(Action action) {
		super(action);
	}
	
	@Override
	protected void init() {
		custom();
		add(new H3("Nowa akcja: pryskanie"));
		
		FormLayout layout = new FormLayout();
		layout.addFormItem(details, "Nazwa oprysku:");
		layout.addFormItem(datePicker, "Data:");
		layout.addFormItem(new HorizontalLayout(value, new Text("PLN")), "Koszt/cena:");
		layout.addFormItem(note, "Szczegóły: ");
		add(layout);
	}

	private void custom() {
		note.setPlaceholder("Zastosowana dawka/stężenie");
		binder.removeBinding(quantity);
	}
}
