package com.github.PiotrDuma.documentationService.frontend.action.components;

import org.springframework.context.annotation.Scope;

import com.github.PiotrDuma.documentationService.model.Action;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

@Scope("prototype")
public class ActionPresenter extends Details {
	private static final long serialVersionUID = 1L;

	private final Action action;
	private H3 title;
	
	private Text details;
	private Text date;
	private Text value;
	private Text note;
	private Text quantity;

	public ActionPresenter(Action action) {
		this.action = action;
		initVariables();

		VerticalLayout summary = summaryDetails();
		summary.setSizeFull();
		FormLayout content = contentDetails();
		setSummary(summary);
		setContent(content);
	}
	
	private void initVariables() {
		title = new H3("< action >");
		
		this.details = new Text(action.getDetails()==null?"":action.getDetails());
		this.date = new Text(action.getDate()==null?"":action.getDate().toString());
		this.value = new Text("" + action.getValue() + " PLN");
		this.note = new Text(action.getNote()==null?"":action.getNote());
		this.quantity = new Text("" + action.getQuantity()+" kg");
	}

	private VerticalLayout summaryDetails() {
		VerticalLayout body = new VerticalLayout();

		Button edit = editButton();
		Button delete = deleteButton();

		HorizontalLayout header = new HorizontalLayout(title, edit, delete);
		header.expand(edit);
		header.setSizeFull();

		Text date = new Text("Utworzono: " + action.getCreated().toString());

		body.add(header, date);
		body.setSizeFull();
		return body;
	}

	private FormLayout contentDetails() {
		FormLayout body = null;
		switch (action.getType()) {
		case SEEDING:
			title.setText("Sadzenie");
			body = contentSeeding();
			break;

		case SPRAYING:
			title.setText("Pryskanie");
			body = contentSpraying();
			break;

		case TILLAGE:
			title.setText("Uprawa");
			body = contentTillage();
			break;

		case GATHERING:
			title.setText("Zbiór");
			body = contentGathering();
			break;
		default:
			title.setText("<TITLE>");
			body = new FormLayout();
			break;
		}
		return body;
	}

	private FormLayout contentSeeding() {
		FormLayout layout = new FormLayout();

		layout.addFormItem(details, "Typ nasienia:");
		layout.addFormItem(date, "Data:");
		layout.addFormItem(value, "Koszt/cena surowców:");
		layout.addFormItem(note, "Szczegóły:");
		return layout;
	}
	
	private FormLayout contentSpraying() {
		FormLayout layout = new FormLayout();

		layout.addFormItem(details, "Nazwa oprysku:");
		layout.addFormItem(date, "Data:");
		layout.addFormItem(value, "Koszt:");
		layout.addFormItem(note, "Szczegóły/dawka:");
		return layout;
	}

	private FormLayout contentTillage() {
		FormLayout layout = new FormLayout();

		layout.addFormItem(details, "Zabieg rolny:");
		layout.addFormItem(date, "Data:");
		layout.addFormItem(value, "Koszt:");
		layout.addFormItem(note, "Szczegóły:");
		return layout;
	}
	
	private FormLayout contentGathering() {
		FormLayout layout = new FormLayout();

		layout.addFormItem(details, "Nazwa oprysku:");
		layout.addFormItem(date, "Data:");
		layout.addFormItem(value, "Zysk:");
		layout.addFormItem(quantity, "Szacowana ilość:");
		layout.addFormItem(note, "Szczegóły:");		
		return layout;
	}
	private Button editButton() {
		Button edit = new Button(VaadinIcon.EDIT.create());
		edit.addClickListener(click -> getEventBus().fireEvent(new EditEvent(this, this.action)));
		edit.getStyle().set("background-color", "LightGray");
		return edit;
	}
	
	private Button deleteButton() {
		Button delete = new Button(VaadinIcon.FILE_REMOVE.create());
		delete.addClickListener(click ->presenterDeleteEvent());
		delete.getStyle().set("background-color", "#ff6347");
		return delete;
	}

	private void presenterDeleteEvent() {
		Dialog dialog = new Dialog();
		dialog.setCloseOnEsc(true);
		dialog.setCloseOnOutsideClick(true);

		H4 text = new H4("Potwierdz");
		Button deleteButton = new Button("Usuń", VaadinIcon.TRASH.create());
		deleteButton.addClickListener(e -> {
			getEventBus().fireEvent(new DeleteEvent(this, this.action));
			dialog.close();
		});
		deleteButton.getElement().setAttribute("theme", "error tertiary");
		Button cancelButton = new Button("Anuluj", e -> dialog.close());

		dialog.add(text, new HorizontalLayout(deleteButton, cancelButton));
		dialog.open();
}
	
	public Details getComponent() {
		return this;
	}

	public static class EditEvent extends ComponentEvent<ActionPresenter> {
		private static final long serialVersionUID = 1L;
		private Action action;

		public EditEvent(ActionPresenter source, Action action) {
			super(source, false);
			this.action = action;
		}

		public Action getAction() {
			return action;
		}
	}

	public static class DeleteEvent extends ComponentEvent<ActionPresenter> {
		private static final long serialVersionUID = 1L;
		private Action action;

		public DeleteEvent(ActionPresenter source, Action action) {
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
