package com.github.PiotrDuma.documentationService.frontend;

import java.util.LinkedList;
import java.util.List;

import com.github.PiotrDuma.documentationService.frontend.ui.Footer;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "start")
@PageTitle("DocService | start")
public class Start extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	private VerticalLayout layout;

	public Start() {
		this.layout = new VerticalLayout();
		final HorizontalLayout header = prepareHeader();
		final VerticalLayout content = prepareContent();
		final HorizontalLayout footer = new Footer().getFooter();

		header.setSizeFull();
		header.setAlignItems(Alignment.CENTER);
		header.getStyle().set("padding-bottom", "5px").set("padding-left", "20%").set("padding-right", "20%");
		header.getStyle().set("position", "sticky").set("top", "0px").set("background-color", "#F8F8F8");

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

		List<Button> list = new LinkedList<Button>();
		
		list.add(new Button("Start",
				(e) -> UI.getCurrent().getPage().executeJs("document.getElementById(\"start\").scrollIntoView()")));
		list.add(new Button("Aplikacja", (e) -> UI.getCurrent().getPage()
				.executeJs("document.getElementById(\"application\").scrollIntoView()")));
		list.add(new Button("Funkcje",
				(e) -> UI.getCurrent().getPage().executeJs("document.getElementById(\"functions\").scrollIntoView()")));
		list.add(new Button("Kontakt",
				(e) -> UI.getCurrent().getPage().executeJs("document.getElementById(\"contact\").scrollIntoView()")));

		list.forEach(c -> c.setSizeFull());
		list.stream().map(c -> {return new Tab(c);}).forEach(c -> tabs.add(c));
		tabs.setFlexGrowForEnclosedTabs(0);
		
		return tabs;
	}

	private VerticalLayout prepareContent() {

		VerticalLayout content = new VerticalLayout();

		VerticalLayout v1 = new VerticalLayout();
		v1.setId("start");
		v1.add(new H3("Lorem Ipsum"));
		v1.add(new Text("\r\n" + "\r\n"
				+ "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur at euismod est, a elementum ligula. Ut vehicula placerat eros, eu sollicitudin nunc hendrerit non. Vivamus a augue diam. Nam tincidunt, urna ac tempor vulputate, dolor metus lacinia orci, eget suscipit nunc libero sit amet justo. Nunc rutrum imperdiet nisi, in condimentum nunc feugiat ut. Pellentesque commodo erat viverra erat tincidunt, vel pulvinar arcu congue. In congue et tellus at pretium. Ut a laoreet est. Sed sed quam bibendum, fermentum est quis, rutrum mauris.\r\n"
				+ "\r\n"
				+ "Sed libero purus, commodo vitae vulputate ut, ultricies et nibh. Cras id lectus id dui tincidunt varius nec eu leo. Nulla bibendum luctus neque, a semper tellus cursus non. Sed lectus eros, tempus id ex quis, fringilla maximus nunc. Cras justo diam, ornare a erat sed, ullamcorper vestibulum libero. Ut ornare vel felis eu eleifend. Etiam eget sem risus. Vivamus elit nisl, luctus in libero a, mattis aliquet ligula. Integer ac tellus pretium massa viverra porta. Mauris vel mattis velit, sed maximus eros. Fusce sodales elementum mollis. Nam id lectus bibendum, laoreet magna eget, ultricies urna. Sed pellentesque sit amet massa vel ullamcorper.\r\n"
				+ "\r\n"
				+ "Cras hendrerit, ipsum eget hendrerit eleifend, lacus felis porta sapien, vitae pulvinar ante diam in magna. Aliquam ac eleifend est. Vestibulum fermentum, libero vitae condimentum porta, ex massa mattis erat, sit amet laoreet lorem nisl in purus. Integer pulvinar libero est, vitae laoreet eros cursus at. Integer mollis in tellus nec aliquet. Sed id libero accumsan, pretium tortor quis, condimentum nulla. Aliquam vel viverra velit, eu vulputate purus. Proin mattis mollis mattis.\r\n"
				+ "\r\n"
				+ "Sed pulvinar ex metus, quis tempus dolor sollicitudin ac. Donec venenatis mattis massa sit amet ornare. Sed sollicitudin diam enim, quis faucibus justo mattis suscipit. Sed diam elit, cursus eu rutrum vitae, semper non sem. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Etiam sollicitudin erat non tempus molestie. Nunc ornare sed nisi viverra finibus. Fusce sollicitudin viverra arcu, eget vehicula ipsum ultricies vel. Nulla posuere elit odio, non porttitor nulla vehicula sit amet. Nullam leo diam, pellentesque sed erat sit amet, pharetra ultrices purus.\r\n"
				+ "\r\n"
				+ "Pellentesque eget euismod urna. Aliquam condimentum arcu sit amet est bibendum, non congue lacus tincidunt. Mauris lacinia ultricies lacus ut eleifend. Duis eget vestibulum orci. Integer interdum justo sed scelerisque imperdiet. Phasellus vel lacus ut orci pharetra rutrum et nec ante. Nam eleifend purus et tempor porttitor. "));

		VerticalLayout v2 = new VerticalLayout();
		v2.setId("application");
		v2.add(new H3("Integer vitae turpis at erat luctus maximus ac imperdiet mauris."
				+ " Donec fringilla eleifend tellus vel luctus. Sed malesuada risus vitae"
				+ " consectetur feugiat. Pellentesque leo eros, luctus sit amet consequat"
				+ " ut, convallis sed lacus. Phasellus pellentesque fermentum justo, lacinia "
				+ "tempus nibh dictum ac. Aliquam ultricies nunc at est dignissim tristique. "
				+ "Sed sit amet venenatis purus. "));

		VerticalLayout v3 = new VerticalLayout();
		v3.setId("functions");
		v3.add(new H3("Chapter3"));
		v3.add(new Text("Etiam at dolor dui. Ut fermentum nulla sed sapien sollicitudin "
				+ "pharetra. Aliquam lectus odio, fermentum sit amet pretium et, efficitur "
				+ "at nunc. Vivamus ut rhoncus leo, nec tincidunt massa. Suspendisse ac nisi "
				+ "quis velit tempus vulputate eu et lectus. Integer semper magna tempus justo "
				+ "cursus tristique. Donec suscipit mi enim. Vestibulum velit turpis, euismod sit "
				+ "amet odio sit amet, commodo auctor turpis. Fusce vel dapibus lectus. Proin nisi "
				+ "lorem, euismod sit amet iaculis ac, consectetur sed tortor. "));

		VerticalLayout v4 = new VerticalLayout();
		v4.setId("contact");
		v4.add(new Text("Etiam at dolor dui. Ut fermentum nulla sed sapien sollicitudin "
				+ "pharetra. Aliquam lectus odio, fermentum sit amet pretium et, efficitur "
				+ "at nunc. Vivamus ut rhoncus leo, nec tincidunt massa. Suspendisse ac nisi "
				+ "quis velit tempus vulputate eu et lectus. Integer semper magna tempus justo "
				+ "cursus tristique. Donec suscipit mi enim. Vestibulum velit turpis, euismod sit "
				+ "pharetra. Aliquam lectus odio, fermentum sit amet pretium et, efficitur "
				+ "at nunc. Vivamus ut rhoncus leo, nec tincidunt massa. Suspendisse ac nisi "
				+ "quis velit tempus vulputate eu et lectus. Integer semper magna tempus justo "
				+ "amet odio sit amet, commodo auctor turpis. Fusce vel dapibus lectus. Proin nisi "
				+ "lorem, euismod sit amet iaculis ac, consectetur sed tortor. "));

		content.add(v1, v2, v3, v4);
		return content;
	}

}
