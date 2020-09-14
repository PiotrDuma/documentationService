package com.github.PiotrDuma.documentationService.frontend.ui;

import org.springframework.context.annotation.Scope;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H6;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


@Tag(value = "footer")
@Scope("prototype")
public class Footer extends Component{
	private static final long serialVersionUID = 1L;
	private HorizontalLayout footer;

	public Footer() {
		footer = new HorizontalLayout();
		
		VerticalLayout temp = new VerticalLayout();
		temp.add(new H6("Documentation Service Project 2020."));
		temp.add(new Text("created by: Piotr Duma"));
		temp.add(prepareIcons());
		temp.setSizeFull();
		temp.setAlignItems(Alignment.CENTER);
 
		footer.add(temp);
	}

	private HorizontalLayout prepareIcons() {
		
		
		Image github = new Image("images/github.png","github");
		github.addClickListener(click -> UI.getCurrent().getPage().open("https://github.com/PiotrDuma/documentationService"));
		github.setHeight("30px");
		github.setWidth("30px");

		Image linkedIn = new Image("images/linkedin.png","linkedIn");
		linkedIn.addClickListener(click -> UI.getCurrent().getPage().open("https://www.linkedin.com/in/piotr-duma-69822719a/"));
		linkedIn.setHeight("30px");
		linkedIn.setWidth("30px");
		
		HorizontalLayout bar = new HorizontalLayout();
		bar.add(github, linkedIn);
		bar.setAlignItems(Alignment.CENTER);
		return bar;
	}

	public HorizontalLayout getFooter() {
		return footer;
	}


	public void setFooter(HorizontalLayout footer) {
		this.footer = footer;
	}
}
