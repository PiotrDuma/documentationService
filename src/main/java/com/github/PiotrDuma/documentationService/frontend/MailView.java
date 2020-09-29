package com.github.PiotrDuma.documentationService.frontend;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.github.PiotrDuma.documentationService.service.mail.MailService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route(value = "mail", layout = MainLayout.class)
public class MailView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private final MailService mail;
	private TextField subject;
	private TextArea message;
	private Dialog dialog;
	public String mess;
	public String sub;

	@Autowired
	public MailView(MailService mail) {
		this.mail = mail;
		createDialog();
		
		VerticalLayout body = prepareBody();
		body.getStyle().set("padding-left", "25%").set("padding-right", "25%");
		add(body);
	}

	private VerticalLayout prepareBody() {
		H3 header = new H3("Masz problem? Skontaktuj się z nami.");

		prepareSubject();
		prepareMessage();

		Button sendButton = new Button("Wyślij", e -> {
			if (!subject.isEmpty() && !message.isEmpty()) {
				dialog.open();
			} else {
				Notification.show("Tytuł lub treść jest pusta", 2000, Position.BOTTOM_CENTER);
			}
		});

		return new VerticalLayout(header, subject, message, sendButton);
	}

	private void prepareSubject() {
		this.subject = new TextField("Tytuł maila");
		subject.setRequired(true);
		subject.setErrorMessage("Pole nie może być puste");
		subject.setMinLength(1);
		subject.setMaxLength(64);
		subject.setWidthFull();
	}

	private void prepareMessage() {
		this.message = new TextArea("Treść");
		message.setRequired(true);
		message.setErrorMessage("Pole nie może być puste");
		message.setMinLength(1);
		message.setMaxLength(2000);
		message.setHeight("200px");
		message.setWidthFull();
	}

	private void sendMail() {
		if(mail.sendMail(subject.getValue(), message.getValue())) {
			subject.clear();
			message.clear();
			dialog.close();
			Notification.show("Wiadomość wysłana", 5000, Position.MIDDLE);
		}else {
			Notification.show("Coś poszło nie tak.", 2000, Position.MIDDLE);
		}
	}

	private void createDialog() {
		dialog = new Dialog();
		dialog.setCloseOnEsc(true);
		dialog.setCloseOnOutsideClick(true);

		H4 text = new H4("Potwierdz");

		Button sendButton = new Button("Wyślij", VaadinIcon.ENVELOPE_OPEN.create());
		sendButton.addClickListener(event -> sendMail());
		sendButton.getElement().setAttribute("theme", "primary");

		Button cancelButton = new Button("Anuluj", event -> {
			dialog.close();
			Notification.show("Anulowano", 2000, Position.BOTTOM_START);
		});

		dialog.add(text, new HorizontalLayout(sendButton, cancelButton));
	}
}
