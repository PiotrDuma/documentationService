package com.github.PiotrDuma.documentationService.frontend;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
public class MainView extends VerticalLayout {

	private static final long serialVersionUID = 1L;

	@Autowired
	public MainView(UserNavigator userNavigator) {
		H1 h1 = new H1("welcome "+ userNavigator.getAppUserEmail());
		add(h1);
		
	}
	
}
