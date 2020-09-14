package com.github.PiotrDuma.documentationService.frontend;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route("login")
@PageTitle("Login")
public class LoginView extends VerticalLayout{
	private static final long serialVersionUID = 1L;
	
	private static final String URL = "/oauth2/authorization/google";
	public String errorParameter;

	public LoginView() {
		
			Button returnButton = new Button("Return", click-> UI.getCurrent().navigate("start"));
			H1 title = new H1("Login with Google");
			
			Image googleImg = new Image("images/google.png","google");
			googleImg.getStyle().set("border-style", "solid").set("border-width", "5px");
			Anchor googleLoginButton = new Anchor(URL, googleImg);
			
			VerticalLayout loginView = new VerticalLayout();
			loginView.add(title, googleLoginButton);
			loginView.setAlignItems(Alignment.CENTER);

			add(returnButton);
	        add(loginView);
	        setSizeFull();
	    }

}
