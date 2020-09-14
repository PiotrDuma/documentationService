package com.github.PiotrDuma.documentationService.frontend.field;

import org.springframework.context.annotation.Scope;

import com.github.PiotrDuma.documentationService.model.Field;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToLongConverter;
import com.vaadin.flow.shared.Registration;


@Scope("prototype")
@Tag(value = "fieldMenu")
public class FieldElement extends  VerticalLayout{
	private static final long serialVersionUID = 1L;

    private Field field;
    
	private VerticalLayout component = new VerticalLayout();   
    private Binder<Field> binder;

    private TextField name;
    private TextField fieldNumber;
    private ComboBox<Field.FieldClass> type;
    private TextArea note;
    
    private Button saveButton = new Button("save");
    private Button closeButton = new Button("close");
    private Button deleteButton = new Button("delete");
    private Button fowardButton = new Button("details ->");

    
    
    
	public FieldElement() {
		init();
		prepareComponent();
		add(component);
	}
	
	public FieldElement(Field field) {
		this();
		this.field = field;
		this.binder.readBean(field);
		
		showAdditionalButtons();
	}
	
	
	private void init() {
		this.field = new Field();
		this.binder = new BeanValidationBinder<>(Field.class);
		this.name = new TextField("field name");
		this.fieldNumber = new TextField("number");
		this.type = new ComboBox<>("type");
		this.note = new TextArea("Notatka");
		note.getStyle().set("maxHeight", "100px");
		fowardButton.addClickListener(c->UI.getCurrent().navigate("field/" + field.getId()));
		fowardButton.setVisible(false);
		deleteButton.setVisible(false);
	}

    private void prepareComponent() {
		type.setItems(Field.FieldClass.values());
		type.setItemLabelGenerator(Field.FieldClass::getValue);

		binder.forField(fieldNumber)
	    .withConverter(
	        new StringToLongConverter("Enter a number"))
	    .bind(Field::getFieldNumber, Field::setFieldNumber);

		binder.bindInstanceFields(this);

		component.add(name, fieldNumber, type, note, preparebuttons(),fowardButton);
    }
	
    private HorizontalLayout preparebuttons() {
    	this.saveButton.addClickListener(c ->{
    		try {
    			this.binder.writeBean(field);
    			if(binder.isValid()) {
    				getEventBus().fireEvent(new SaveEvent(this, this.field));
    			}else {
    				throw new RuntimeException("object fields are not valid.");
    			}
    			}catch(Exception exception) {
//    				exception.printStackTrace(); //dont print exception.
    			}
    	});
    	this.closeButton.addClickListener(c ->getEventBus().fireEvent(new CancelEvent(this, this.field)));
    	this.deleteButton.addClickListener(c ->getEventBus().fireEvent(new DeleteEvent(this, this.field)));
    	return new HorizontalLayout(saveButton,closeButton,deleteButton);
    }

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
		this.binder.readBean(field);
	}

	private void showAdditionalButtons() {
		deleteButton.setVisible(true);
		fowardButton.setVisible(true);
	}
	
	
	//Vaadin crm-tutorial
	public static abstract class callEvent extends ComponentEvent<FieldElement>{
		private static final long serialVersionUID = 1L;
		private Field field;

		protected callEvent(FieldElement source, Field field) {
			super(source, false); //server side call
			this.field = field;
		}
		
		public Field getField() {
			return field;
		}
	}
	
	public static class SaveEvent extends callEvent{
		private static final long serialVersionUID = 1L;
		public SaveEvent(FieldElement source, Field field) {
			super(source, field);
		}
	}
	
	public static class DeleteEvent extends callEvent{
		private static final long serialVersionUID = 1L;
		public DeleteEvent(FieldElement source, Field field) {
			super(source, field);
		}
	}
	
	public static class CancelEvent extends callEvent{
		private static final long serialVersionUID = 1L;
		public CancelEvent(FieldElement source, Field field) {
			super(source, field);
		}
	}
	
	@Override
	public <T extends ComponentEvent<?>>Registration addListener(Class<T> eventType, ComponentEventListener<T> listener) {
		return getEventBus().addListener(eventType, listener);
	}

}
