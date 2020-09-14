package com.github.PiotrDuma.documentationService.frontend.ui;


import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.router.RouterLayout;


@CssImport("./styles/shared-styles.css")
@CssImport(value = "./styles/vaadin-text-field-styles.css", themeFor = "vaadin-text-field")
public class MainLayout extends AppLayout implements RouterLayout{

	private static final long serialVersionUID = 1L;

	private Header viewComponents;
	

	public MainLayout() {
		this.viewComponents = new Header();
		addToNavbar(viewComponents.getHeader());
    }
}
