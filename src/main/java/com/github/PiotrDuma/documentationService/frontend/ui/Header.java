package com.github.PiotrDuma.documentationService.frontend.ui;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.context.SecurityContextHolder;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.Component;

@Tag(value = "header")
@Scope("prototype") //every component class should be scoped to prototype or UI.
					//https://github.com/vaadin/flow/issues/5830
public class Header extends Component{
	private static final long serialVersionUID = 1L;
	
	private HorizontalLayout header;
	
	public Header() {
		this.header = header();
		header.getStyle().set("padding-right", "5%");
	}

	private HorizontalLayout header() {
		HorizontalLayout header = new HorizontalLayout();

		MenuBar accoutMenuBar = accoutMenu();

        Tabs menu = new Tabs();
        
        Tab logo = new Tab(logo());
        Tab fieldTab = new Tab("fields");
        
        Map<Tab, String> map = new HashMap<>();
        map.put(logo, "");
        map.put(fieldTab, "field");
        
        menu.add(logo, fieldTab);
        menu.addSelectedChangeListener(item -> {
        		UI.getCurrent().navigate(map.get(item.getSelectedTab()));
        });
        menu.setSizeFull();

        header.add(menu,accoutMenuBar);
        header.setAlignSelf(Alignment.CENTER, accoutMenuBar);
        header.setWidthFull();
        header.expand(accoutMenuBar);
		return header;
	}

	private Image logo() {
        Image img = new Image("images/logo.png", "logo");
        img.setMinHeight("0");
        return img;
	}

	private MenuBar accoutMenu(){
        MenuBar menu = new MenuBar();
        MenuItem account = menu.addItem("Account");
        account.getSubMenu().addItem("details", e -> UI.getCurrent().navigate("field"));
        account.getSubMenu().addItem("logout", e-> {
			SecurityContextHolder.clearContext();
			UI.getCurrent().getSession().close();
			UI.getCurrent().navigate("login");
			Notification.show("succesfully logged out");
        });
        menu.setOpenOnHover(false);
		return menu;
	}
	
	
	public HorizontalLayout getHeader() {
		return header;
	}

	public void setHeader(HorizontalLayout header) {
		this.header = header;
	}

}
