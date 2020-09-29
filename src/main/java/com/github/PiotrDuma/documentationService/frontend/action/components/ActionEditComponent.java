package com.github.PiotrDuma.documentationService.frontend.action.components;

import org.springframework.context.annotation.Scope;

import com.github.PiotrDuma.documentationService.frontend.action.components.editor.AbstractActionEditor;
import com.github.PiotrDuma.documentationService.frontend.action.components.editor.ActionEditFactory;
import com.github.PiotrDuma.documentationService.model.Action;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

@Scope("prototype")
@Tag(value = "actionEditComponent")
public class ActionEditComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	
	private Action action;
	
	private AbstractActionEditor editor;
	private VerticalLayout body;
	private VerticalLayout editorDiv;


	public ActionEditComponent(Action action) {
		this.action = action;
		editor = ActionEditFactory.createActionEdit(action.getType(), action);
		initializeComponents();
	}

	public ActionEditComponent(Action.ActionType type) {
		this.action = new Action();
		action.setType(type);
		editor = ActionEditFactory.createActionEdit(type);
		initializeComponents();
	}
	
	private void initializeComponents() {
		editorDiv = new VerticalLayout(editor);
		editorDiv.setSizeFull();
		
		body = new VerticalLayout(editorDiv, buttons());
		add(body);
	}

	private HorizontalLayout buttons() {
		HorizontalLayout buttons = new HorizontalLayout();
		
		Button closeButton = new Button("Anuluj",c ->getEventBus().fireEvent(new CancelEvent(this, this.action)));
		Button saveButton = new Button("Zapisz",c -> saveEvent());
		
		buttons.add(saveButton, closeButton);
		buttons.setSizeFull();
		buttons.setAlignItems(Alignment.CENTER);
		return buttons;
	}
	
	private void saveEvent() {
		if(editor.isValid()) {
			action = editor.getAction();
			getEventBus().fireEvent(new SaveEvent(this, this.action));
		}else {
			Notification.show("Operacja nie powiodła się", 2000, Position.BOTTOM_CENTER);
		}
	}
	
	public void setAction(Action action) {
		this.action = action;
		this.editor.setAction(action);
	}
	
	public Action getAction() {
		return action;
	}

	public static class SaveEvent extends ComponentEvent<ActionEditComponent> {
		private static final long serialVersionUID = 1L;
		private Action action;

		public SaveEvent(ActionEditComponent source, Action action) {
			super(source, false); // server side call
			this.action = action;
		}

		public Action getAction() {
			return action;
		}
	}

	public static class CancelEvent extends ComponentEvent<ActionEditComponent> {
		private static final long serialVersionUID = 1L;
		private Action action;

		public CancelEvent(ActionEditComponent source, Action action) {
			super(source, false);
			this.action = action;
		}

		public Action getAction() {
			return action;
		}
	}

	@Override
	public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
			ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
