package com.github.PiotrDuma.documentationService.frontend.action;


import java.time.LocalDate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.dao.impl.ActionDaoImpl;
import com.github.PiotrDuma.documentationService.service.dao.impl.FieldDaoImpl;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "field", layout = MainLayout.class)
public class FieldActions extends VerticalLayout implements HasUrlParameter<Long>, 
			AfterNavigationObserver, BeforeEnterObserver {
	private static final long serialVersionUID = 1L;
	
	private Field field;
	private final FieldDaoImpl fieldDao;
	private final ActionDaoImpl actionDao;
	private final UserNavigator userNavigator;

	public Long fieldId; //URL variable must be public!
	private VerticalLayout layoutBody;

	Text text;

	Button button = new Button("click");
	
	@Autowired
	public FieldActions(FieldDaoImpl fieldDao, ActionDaoImpl actionDao, UserNavigator userNavigator) {
		this.fieldDao = fieldDao;
		this.actionDao = actionDao;
		this.userNavigator = userNavigator;
		
		this.layoutBody = new VerticalLayout();
		
		add(new H1("TODO"));
	}

	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		this.fieldId = parameter;
		System.out.println("Parameter mapping runs"+ fieldId);
	}


	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		System.out.println("after navigation runs"+ fieldId);
		
		text = new Text(""+ fieldId);
		
		this.field = fieldDao.get(fieldId).get();
		Action action = new Action(LocalDate.now(), "note",123,field,ActionType.GARTHERING,"details");
		button.addClickListener(e-> text.setText("clicked"));
		text.setText(action.toString());
		button.addClickListener(click->{actionDao.save(action);
			text.setText(actionDao.getAll().toString());
		});
		
		layoutBody.add(new H1("url:" +fieldId));
		layoutBody.add(button, text);
		
		add(layoutBody);
	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		System.out.println("before navigation runs" + fieldId);
		if(!fieldDao.get(fieldId).get().getAppUser().equals(userNavigator.getAppUser())) {
			throw new AccessDeniedException("");
		}
	}
	
	
	public Long getFieldId() {
		return fieldId;
	}

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

}
