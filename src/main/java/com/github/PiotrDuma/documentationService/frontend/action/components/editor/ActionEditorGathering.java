package com.github.PiotrDuma.documentationService.frontend.action.components.editor;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public final class ActionEditorGathering extends UniqueActionEditor{
	private static final long serialVersionUID = 1L;
	
	public ActionEditorGathering() {
		super();
		action.setType(ActionType.GATHERING);
	}

	public ActionEditorGathering(Action action) {
		super(action);
	}
	
	@Override
	protected void init() {
		customVariables();
		
		add(new H3("Nowa akcja: zbiór"));
		FormLayout layout = new FormLayout();
		layout.addFormItem(details, "Typ nasienia:");
		layout.addFormItem(datePicker, "Data:");
		layout.addFormItem(new HorizontalLayout(value, new Text("PLN")), "Zysk:");
		layout.addFormItem(new HorizontalLayout(quantity, new Text("kg")), "Szacowana ilość: ");
		layout.addFormItem(note, "Szczegóły: ");
		add(layout);
	}
	
	
	private void customVariables() {
		
		super.binder.forField(quantity)
			.asRequired("Pole nie może byc puste.")
			.bind(Action::getQuantity, Action::setQuantity);
	}
}
