package com.github.PiotrDuma.documentationService.frontend;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.github.PiotrDuma.documentationService.frontend.ui.Footer;
import com.github.PiotrDuma.documentationService.service.mail.MailService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import de.mekaso.vaadin.addons.Carousel;

@Route(value = "start")
@PageTitle("DocService | start")
public class Start extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private VerticalLayout layout;

	private final MailService mail;
	final HorizontalLayout header;
	final VerticalLayout content;
	final HorizontalLayout footer;

	@Autowired
	public Start(@Qualifier("mailSupport") MailService mail) {
		this.mail = mail;
		this.layout = new VerticalLayout();

		header = prepareHeader();
		content = prepareContent();
		footer = new Footer().getFooter();
		initView();
	}

	private void initView() {
		header.setSizeFull();
		header.setAlignItems(Alignment.CENTER);
		header.getStyle().set("padding-bottom", "5px").set("padding-left", "20%").set("padding-right", "20%");
		header.getStyle().set("position", "sticky").set("top", "0px").set("background-color", "#F8F8F8");
		header.getStyle().set("z-index", "1000");

		content.setSizeFull();
		content.getStyle().set("padding-left", "15%").set("padding-right", "15%");

		footer.getStyle().set("background-color", "#F5F5F5");
		footer.setSizeFull();

		// MAIN CONTAINER
		layout.setSizeFull();
		layout.setMargin(true);
		layout.setSpacing(false);
		layout.setPadding(false);

		layout.add(header, content, footer);
		getStyle().set("border-style", "solid").set("border-color", "#4CAF50");
		add(layout);
	}

	private HorizontalLayout prepareHeader() {

		HorizontalLayout header = new HorizontalLayout();
		Tabs menu = prepareMenu();

		Image img = new Image("images/logo.png", "DocumentationService");
		img.addClickListener(click -> UI.getCurrent().getPage().reload());
		img.setMinHeight("0");

		Button login = new Button("Zaloguj się", e -> UI.getCurrent().navigate("login"));
		login.getStyle().set("border-radius", "12px").set("background-color", "#4CAF50").set("color", "white");

		header.add(img, menu, login);
		header.expand(menu);
		return header;
	}

	private Tabs prepareMenu() {
		Tabs tabs = new Tabs();

		Tab start = new Tab("Start");
		Tab funkcje = new Tab("Funkcje");
		Tab kontakt = new Tab("Kontakt");

		Map<Tab, String> map = new HashMap<>();
		map.put(start, "document.getElementById(\"start\").scrollIntoView()");
		map.put(funkcje, "document.getElementById(\"functions\").scrollIntoView()");
		map.put(kontakt, "document.getElementById(\"contact\").scrollIntoView()");

		tabs.add(start, funkcje, kontakt);
		tabs.addSelectedChangeListener(item -> {
			UI.getCurrent().getPage().executeJs(map.get(item.getSelectedTab()));
		});

		tabs.setFlexGrowForEnclosedTabs(0);
		return tabs;
	}

	private VerticalLayout prepareContent() {
		VerticalLayout content = new VerticalLayout();

		VerticalLayout section1 = startContent();
		section1.setId("start");

		VerticalLayout section2 = new VerticalLayout();
		section2.setId("functions");
		section2.add(section2Text());
		section2.add(applicationContentExpose());

		VerticalLayout section3 = new VerticalLayout();
		section3.getStyle().set("margin-top", "50px");
		section3.setId("contact");
		section3.add(contactSection());

		content.add(section1, section2, section3);
		return content;
	}

	private VerticalLayout startContent() {
		HorizontalLayout body = new HorizontalLayout();
		Button showMore = new Button("Czytaj więcej",
				e -> UI.getCurrent().getPage().executeJs("document.getElementById(\"application\").scrollIntoView()"));
		showMore.getStyle().set("border-radius", "12px").set("background-color", "#4CAF50").set("color", "white");

		VerticalLayout description = new VerticalLayout();
		description.setWidth("250px");
		description.add(new H4("Wypróbuj aplikację do tworzenia dokumentacji rolicznej ZA DARMO."));
		description.add(showMore);
		description.setHeightFull();

		Image image = new Image("images/start/image.jpg", "presenter image");
		image.setWidth("250px");
		image.setHeight("250px");
		body.add(description, image);

		VerticalLayout out = new VerticalLayout(body);
		out.setSizeFull();
		out.setAlignItems(Alignment.CENTER);
		return out;
	}

	private VerticalLayout section2Text() {
		H3 header = new H3("Dostępne funkcje aplikacji");
		Text text = new Text("Aplikacja umożliwa tworzenie dokumentacji rolniczej w oparciu"
				+ "o nowoczense systemy informatyczne. Korzystając z niej będziesz miał możliwość"
				+ "dodawania i monitorowania swoich pól w zależności od szacowanego bilansu funansowego, "
				+ "a także wykonanych na nich akcji, co ułatwi przyszłe planowanie działań oraz wypełnianie"
				+ "wniosków i dokumentów.");

		return new VerticalLayout(header, text);
	}

	private Carousel applicationContentExpose() {
		Carousel carousel = Carousel.create();
		carousel.setWidthFull();
		carousel.add(new Image("images/start/screen3.png", "screen1"));
		carousel.add(new Image("images/start/screen3.png", "screen2"));
		carousel.add(new Image("images/start/screen3.png", "screen3"));
		return carousel;
	}

	private VerticalLayout contactSection() {
		HorizontalLayout body = new HorizontalLayout();
		body.setWidthFull();

		VerticalLayout section = new VerticalLayout();
		section.add(new H3("Kontakt"));
		section.add(new Text("Masz problem lub wątpliwości? A może masz pomysl jak "
				+ "usprawnić działanie aplikacji, bądz sposób na nowe funkcjonalności."
				+ "Nie wahaj się! Skontaktuj się z nami!!"));
		section.add(body);

		TextArea textBox = new TextArea();
		textBox.setWidthFull();
		textBox.setHeight("200px");
		textBox.setMaxLength(1000);

		EmailField emailField = new EmailField();
		emailField.setErrorMessage("Niepoprawny format email");
		System.out.println(emailField.getValue());
		Button send = new Button("Send", e -> {
				if(!emailField.isInvalid()) {
					mailSupport("Message from: " + emailField.getValue(),textBox.getValue());
				}								
			});		

		VerticalLayout sidePanel = new VerticalLayout(new H5("Wyślij nam maila."));
		sidePanel.add(emailField, send);
		sidePanel.setWidth("30%");
		
		body.add(sidePanel);
		body.add(new VerticalLayout(new H6("Treść maila"), textBox));
		return section;
	}

	private void mailSupport(String subject, String txt) {
		Dialog dialog = new Dialog();
		dialog.open();
		Text dialogTxt = new Text("");
		Button dialogButton = new Button("Ok", e -> dialog.close());

		dialog.add(new VerticalLayout(dialogTxt, dialogButton));
		if (subject.isEmpty() || txt.isEmpty()) {
			dialogTxt.setText("Email lub treść nie może być pusta.");
		} else {
			if (mail.sendMail(subject, txt)) {
				dialogTxt.setText("Wiadomość została wysłana.");
			} else {
				dialogTxt.setText("Coś poszło nie tak. Spróbuj ponownie.");
			}
		}
	}
}
