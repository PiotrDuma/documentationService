package com.github.PiotrDuma.documentationService.frontend.action.components.editor;

import java.time.LocalDate;

import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public class UniqueActionEditor extends AbstractActionEditor {
	private static final long serialVersionUID = 1L;

	protected Action action;

	protected DatePicker datePicker;
	protected TextField details;
	protected TextArea note;
	protected NumberField value;
	protected NumberField quantity;

	protected Binder<Action> binder;

	public UniqueActionEditor() {
		this.binder = new Binder<Action>(Action.class);
		this.action = new Action();
		this.action.setType(ActionType.UNKNOWN);
		prepareVariables();
		init();
	}

	public UniqueActionEditor(Action action) {
		this.binder = new Binder<Action>(Action.class);
		this.action = action;
		prepareVariables();
		binder.readBean(action);
		init();
	}
	
	protected void init() {
		add(new H3("Nowa akcja: zbiór"));
		FormLayout layout = new FormLayout();
		layout.addFormItem(details, "Typ nasienia:");
		layout.addFormItem(datePicker, "Data:");
		layout.addFormItem(new HorizontalLayout(value, new Text("PLN")), "Zysk:");
		layout.addFormItem(note, "Szacowana ilość: ");
		add(layout);
	}

	protected void prepareVariables() {
		
		this.details = new TextField();
		details.setRequired(true);
		details.setPlaceholder("np. Pszenica/Buraki");
		this.datePicker = new DatePicker(LocalDate.now());
		this.value = new NumberField();
		this.quantity = new NumberField();
		this.note = new TextArea();
		note.setMaxHeight("150px");
		
		binder.forField(details)
		.asRequired("Pole nie może byc puste")
		.bind(Action::getDetails, Action::setDetails);
		
		binder.forField(datePicker)
		.asRequired("Pole nie może byc puste")
		.bind(Action::getDate, Action::setDate);
		
		binder.forField(value)
		.asRequired("Nieprawidłowa wartość")
		.bind(Action::getValue, Action::setValue);
		
		binder.forField(quantity).bind(Action::getQuantity, Action::setQuantity);
		binder.forField(note).bind(Action::getNote, Action::setNote);

		binder.bindInstanceFields(this);
	}
	
	
	@Override
	public VerticalLayout getBody() {
		return this;
	}

	@Override
	public boolean isValid() {
		try {
			binder.writeBean(action);
			return binder.isValid();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return false;		
	}

	@Override
	public void setAction(Action action) {
		this.action = action;
		binder.readBean(action);
	}

	@Override
	public Action getAction() {
		try {
			this.binder.writeBean(action);
			if (binder.isValid())
				
				return action;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}
}
