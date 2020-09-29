package com.github.PiotrDuma.documentationService.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

/**
 * onedirectional ManyToOne action -> field.
 *
 */
@Entity
@Table(name = "actions")
public class Action {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "action_id")
	private Long id;

	@Column(name = "date")
	private LocalDate date;

	@CreationTimestamp
	@Column(name = "created")
	private LocalDate created;

	private String details;
	
	private double value;
	private double quantity;
	private String note;

	@ManyToOne(optional = false)
	@JoinColumn(name = "field_id", nullable = false)
	private Field field;

	@Enumerated(EnumType.STRING)
	private Action.ActionType type;

	public Action() {
		this.details = "";
		this.date = null;
		this.value = 0;
		this.quantity = 0;
		this.field = null;
		this.type = ActionType.UNKNOWN;
		this.note = "";
	}

	public Action(LocalDate date, String note, double value, double quantity, Field field, ActionType type, String details) {
		super();
		this.details = details;
		this.date = date;
		this.value = value;
		this.quantity = quantity;
		this.field = field;
		this.type = type;
		this.note = note;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getCreated() {
		return created;
	}

	public void setCreated(LocalDate created) {
		this.created = created;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Action [id=" + id + ", date=" + date + ", name=" + note + ", field=" + field.getId() + ", type=" + type
				+ ", details=" + details + "]";
	}
	
	public enum ActionType {
		SEEDING, SPRAYING, TILLAGE, GATHERING, UNKNOWN
	}

}
