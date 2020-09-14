package com.github.PiotrDuma.documentationService.frontend.field;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.PiotrDuma.documentationService.frontend.ui.MainLayout;
import com.github.PiotrDuma.documentationService.model.Field;
import com.github.PiotrDuma.documentationService.security.UserNavigator;
import com.github.PiotrDuma.documentationService.service.dao.FieldDao;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.Route;

@Route(value = "field", layout = MainLayout.class)
public class FieldView extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private final FieldDao fieldDao;
	private final UserNavigator userNavigator;

	private VerticalLayout layout;
	private VerticalLayout sideMenu;

	private FieldElement fieldElement;
	private Button addButton;

	private String comboBoxFilterValue;
	private TextField filterTextField;
	private Grid<Field> grid;

	@Autowired
	public FieldView(FieldDao fieldDao, UserNavigator userNavigator) {
		this.fieldDao = fieldDao;
		this.userNavigator = userNavigator;


		this.filterTextField = new TextField();

		this.addButton = new Button("+ add");
		this.fieldElement = new FieldElement();
		hideFieldElement();

		this.layout = prepareBody();
		layout.setSizeFull();

		setSizeFull();
		add(layout);
	}
	
	private VerticalLayout prepareBody() {
		prepareGrid();
		
		HorizontalLayout body = new HorizontalLayout();
		body.setSizeFull();

		VerticalLayout mainBody = new VerticalLayout();
		mainBody.add(prepareFilterPanel(),grid);
		mainBody.setSizeFull();
		mainBody.setWidth("70%");
		
		this.sideMenu = prepareSideMenu();
		sideMenu.setAlignItems(Alignment.CENTER);
		sideMenu.setWidth("30%");
		
		body.add(mainBody, sideMenu);
		return new VerticalLayout(body);
	}

	private VerticalLayout prepareSideMenu() {
		VerticalLayout sideMenu = new VerticalLayout();
		addButton.addClickListener(click -> openFieldElement(null));

		sideMenu.add(addButton, fieldElement);
		sideMenu.setAlignItems(Alignment.CENTER);
		hideFieldElement();
		return sideMenu;
	}

	private HorizontalLayout prepareFilterPanel() {
		HorizontalLayout filterPanel = new HorizontalLayout();
		Button filter = new Button("filter", e -> filterButtonAction());
		filterPanel.add(filterTextField, searchOption(), filter);
		filterPanel.setPadding(true);
		return filterPanel;
	}

	private void prepareGrid() {
		this.grid = new Grid<Field>(Field.class);
		
		grid.setHeightFull();
		grid.setSelectionMode(SelectionMode.SINGLE);
		grid.addSelectionListener(e -> {
			if (e.getFirstSelectedItem().isPresent()) {
				openFieldElement(e.getFirstSelectedItem().get());
			} else {
				hideFieldElement();
			}
		});
		grid.setColumns("name","created","type", "note");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
				
		grid.addColumn(new ComponentRenderer<>(item -> iconRedirect(item.getId()))).setHeader("details");
		grid.setItems(fieldDao.getAllByAppUser(userNavigator.getAppUser()));
	}

	private ComboBox<String> searchOption() {
		ComboBox<String> valueComboBox = new ComboBox<>();
		valueComboBox.setItems("id", "note");
		valueComboBox.setValue("id");
		this.comboBoxFilterValue = "id";
		valueComboBox.addValueChangeListener(event -> this.comboBoxFilterValue = event.getValue());
		return valueComboBox;
	}

	private void filterButtonAction() {
		List<Field> fields = fieldDao.getAllByAppUser(userNavigator.getAppUser());

		if (this.comboBoxFilterValue.equals("note")) {
			fields = fields.stream().filter(e -> e.getNote().contains(this.filterTextField.getValue()))
					.collect(Collectors.toList());
		} else if (this.comboBoxFilterValue.equals("id")) {
			fields = fields.stream().filter(e -> e.getId().toString().contains(this.filterTextField.getValue()))
					.collect(Collectors.toList());
		}
		grid.setItems(fields);
		grid.getDataProvider().refreshAll();
	}

	private void handleSaveEvent(FieldElement.SaveEvent event) {

		fieldDao.saveOrUpdate(event.getField());
		Notification.show("saved", 2000, Notification.Position.MIDDLE);
		
		hideFieldElement();
		grid.setItems(fieldDao.getAllByAppUser(userNavigator.getAppUser()));
		grid.getDataProvider().refreshAll();
	}

	private void handleDeleteEvent(FieldElement.DeleteEvent event) {
		
		fieldDao.delete(event.getField().getId());
		Notification.show("deleted", 2000, Notification.Position.MIDDLE);

		grid.setItems(fieldDao.getAllByAppUser(userNavigator.getAppUser()));
		grid.getDataProvider().refreshAll();
	}

	private void handleCancelEvent(FieldElement.CancelEvent event) {
		hideFieldElement();
		grid.deselectAll();
	}

	private void openFieldElement(Field field) {
		FieldElement temp = null;
		if (field == null) {
			temp = new FieldElement();
		} else {
			temp = new FieldElement(field);
		}
		sideMenu.replace(fieldElement, temp);
		fieldElement = temp;
		fieldElement.addListener(FieldElement.SaveEvent.class, this::handleSaveEvent);
		fieldElement.addListener(FieldElement.CancelEvent.class, this::handleCancelEvent);
		fieldElement.addListener(FieldElement.DeleteEvent.class, this::handleDeleteEvent);

		addButton.setVisible(false);
		fieldElement.setVisible(true);
	}

	private void hideFieldElement() {
		addButton.setVisible(true);
		fieldElement.setVisible(false);
	}


	private Icon iconRedirect(Long id) {
		Icon icon = new Icon(VaadinIcon.EXTERNAL_LINK);
		icon.addClickListener(e -> UI.getCurrent().navigate("field/"+id));
		return icon; 
	}
}