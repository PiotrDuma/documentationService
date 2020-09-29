package com.github.PiotrDuma.documentationService.frontend.action;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;

import com.github.PiotrDuma.documentationService.frontend.action.components.ActionEditComponent;
import com.github.PiotrDuma.documentationService.frontend.action.components.ActionPresenter;
import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.github.PiotrDuma.documentationService.model.Action;
import com.github.PiotrDuma.documentationService.model.Action.ActionType;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.dao.ActionDao;
import com.github.PiotrDuma.documentationService.service.dao.FieldDao;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route(value = "field", layout = MainLayout.class)
public class FieldActions extends VerticalLayout
		implements HasUrlParameter<Long>, AfterNavigationObserver, BeforeEnterObserver {
	private static final long serialVersionUID = 1L;

	private final FieldDao fieldDao;
	private final ActionDao actionDao;
	private final UserNavigator userNavigator;
	private Field field;

	public Long fieldId; // URL variable must be public!

	private VerticalLayout newItemBody;
	private ActionEditComponent editorComponent;
	private VerticalLayout actionContainer;
	private List<Action> actionList;

	Component replacement = null;

	@Autowired
	public FieldActions(FieldDao fieldDao, ActionDao actionDao, UserNavigator userNavigator) {
		this.fieldDao = fieldDao;
		this.actionDao = actionDao;
		this.userNavigator = userNavigator;
		this.actionList = new LinkedList<Action>();		
		this.actionContainer = new VerticalLayout();
	}
	
	private HorizontalLayout mainLayout() {
		actionContainer.setSizeFull();
		prepareNewItemBody();
		
		VerticalLayout mainPanel = new VerticalLayout();		
		mainPanel.add(backward(), newItemBody,new H4("Dodane akcje:"), actionContainer);
		mainPanel.setWidth("60%");
		
		VerticalLayout sidePanel = new VerticalLayout();
		sidePanel.setWidth("40%");
		
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();
		layout.add(mainPanel, sidePanel);
		
		return layout;
	}

	private HorizontalLayout backward() {
		Button fieldNameButton = new Button(field.getName(),e->UI.getCurrent().navigate("field"));
		Icon icon = VaadinIcon.ARROW_BACKWARD.create();
		icon.addClickListener(e->UI.getCurrent().navigate("field"));
		return new HorizontalLayout(icon,fieldNameButton);
	}
	
	private void prepareNewItemBody() {
		this.newItemBody = new VerticalLayout();

		this.editorComponent = new ActionEditComponent(ActionType.SEEDING);
		editorComponent.setVisible(false);

		Image seeding = new Image("icons/actions/seeding.png", "seed");
		seeding.addClickListener(click -> initEditorNewItem(ActionType.SEEDING));

		Image spraying = new Image("icons/actions/spraying.png", "spray");
		spraying.addClickListener(click -> initEditorNewItem(ActionType.SPRAYING));

		Image tillage = new Image("icons/actions/tillage.png", "tillage");
		tillage.addClickListener(click -> initEditorNewItem(ActionType.TILLAGE));

		Image gathering = new Image("icons/actions/harvest.png", "harvest");
		gathering.addClickListener(click -> initEditorNewItem(ActionType.GATHERING));
		Image[] imgs = { seeding, spraying, tillage, gathering };

		for (Image img : imgs) {
			img.getStyle().set("border-style", "solid");
		}
		HorizontalLayout menu = new HorizontalLayout();
		menu.add(seeding, spraying, tillage, gathering);
		H3 textHeader = new H3("Dodaj nową akcję:");
		newItemBody.add(textHeader,menu, editorComponent);
		newItemBody.getStyle().set("border-style", "ridge");
	}

	private void initEditorNewItem(ActionType type) {
		ActionEditComponent item = new ActionEditComponent(type);
		item.addListener(ActionEditComponent.SaveEvent.class, this::editorSaveEvent);
		item.addListener(ActionEditComponent.CancelEvent.class, this::editorCancelEvent);

		newItemBody.replace(editorComponent, item);
		editorComponent = item;
	}
	
	private ActionEditComponent initEditorReplace(Action action) {
		ActionEditComponent item = new ActionEditComponent(action);
		item.addListener(ActionEditComponent.SaveEvent.class, this::editorSaveEventReplace);
		item.addListener(ActionEditComponent.CancelEvent.class, this::editorCancelEventReplace);
		return item;
	}

	private ActionPresenter initPresenter(Action action) {
		ActionPresenter presenter = new ActionPresenter(action);
		presenter.addListener(ActionPresenter.DeleteEvent.class, this::presenterDeleteEvent);
		presenter.addListener(ActionPresenter.EditEvent.class, this::presenterEditEventReplace);
		return presenter;
	}

	/********  event handlers  *********/
	//new item editor
	private void editorSaveEvent(ActionEditComponent.SaveEvent event) {
		Action action = event.getAction();
		action.setField(field);
		actionDao.save(action);
		actionList.add(action);
		
		this.actionContainer.addComponentAsFirst(initPresenter(action));
		editorComponent.setVisible(false);
		Notification.show("Dodano", 2000, Position.BOTTOM_START);
	}

	private void editorCancelEvent(ActionEditComponent.CancelEvent event) {
		editorComponent.setVisible(false);
	}
	
	//existing item editor replace
	private void editorSaveEventReplace(ActionEditComponent.SaveEvent event) {
		actionDao.update(event.getAction().getId(), event.getAction());
		
		Component source = event.getSource();
		ActionPresenter presenter = initPresenter(event.getAction());
		presenter.setOpened(true);
		actionContainer.replace(source, presenter);
		source = presenter;
		
	}

	private void editorCancelEventReplace(ActionEditComponent.CancelEvent event) {
		Component source = event.getSource();
		ActionPresenter presenter = initPresenter(event.getAction());
		actionContainer.replace(source, presenter);
		source = presenter;
	}
	
	//presenter events
	private void presenterEditEventReplace(ActionPresenter.EditEvent event) {
		Component source = event.getSource();
		ActionEditComponent item = initEditorReplace(event.getAction());
		actionContainer.replace(source, item);
		source = item;
	}
	
	private void presenterDeleteEvent(ActionPresenter.DeleteEvent event) {
		actionDao.delete(event.getAction().getId());
		event.getSource().setVisible(false);
		
		Notification.show("Usunięto", 2000, Position.BOTTOM_START);
	}
	
	////////////////////////////////////////
	@Override
	public void setParameter(BeforeEvent event, Long parameter) {
		this.fieldId = parameter;
	}

	@Override
	public void afterNavigation(AfterNavigationEvent event) {
		this.field = fieldDao.get(fieldId).get();
		
		actionList.addAll(actionDao.getAll(field, false));
		actionList.forEach(item -> actionContainer.add(initPresenter(item)));
		add(mainLayout());
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		if (!fieldDao.isPreset(fieldId)) {
			throw new EntityNotFoundException();
		}
		if (!fieldDao.get(fieldId).get().getAppUser().equals(userNavigator.getAppUser())) {
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
