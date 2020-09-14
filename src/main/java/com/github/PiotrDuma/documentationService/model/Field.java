package com.github.PiotrDuma.documentationService.model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


/**
 *	bidirectional: appUser - field.
 *	bidirectional field - fieldDetails.
 */
@Entity
@Table(name = "fields")
public class Field {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "field_id")
	private Long id;

	@NotNull
	@NotEmpty
	private String name;
	private long fieldNumber;
	
	@DateTimeFormat
	@CreationTimestamp
	private LocalDate created;
	
	@Enumerated(EnumType.STRING)
	private Field.FieldClass type;
	
	private String note;

	//eager load, field user always available
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private AppUser appUser;

	@OneToOne(mappedBy = "field",cascade = CascadeType.ALL,fetch = FetchType.LAZY, orphanRemoval = true)
	private FieldDetails fieldDetails;

	public Field() {
		this.setFieldDetails(new FieldDetails());
	}
	
	public Field(String name, long fieldNumber, FieldClass type, String note, AppUser appUser) {
		this();
		this.name = name;
		this.fieldNumber = fieldNumber;
		this.type = type;
		this.note = note;
		this.appUser = appUser;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	public LocalDate getCreated() {
		return created;
	}

	public AppUser getAppUser() {
		return appUser;
	}

	public void setAppUser(AppUser owner) {
		this.appUser = owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getFieldNumber() {
		return fieldNumber;
	}

	public void setFieldNumber(long fieldNumber) {
		this.fieldNumber = fieldNumber;
	}
	
	public FieldClass getType() {
		return type;
	}

	public void setType(FieldClass type) {
		this.type = type;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public LocalDate getOnCreate() {
		return created;
	}

	public void setOnCreate(LocalDate onCreate) {
		this.created = onCreate;
	}

	public FieldDetails getFieldDetails() {
		return fieldDetails;
	}

	public void setFieldDetails(FieldDetails fieldDetails) {
		this.fieldDetails = fieldDetails;
		fieldDetails.setField(this);
	}

	public void removeFieldDetails(FieldDetails fieldDetails) {
		this.fieldDetails = null;
		fieldDetails.setField(null);
	}
	
	@Override
	public String toString() {
		return "Field [id=" + id + ", note=" + note + ", appUser=" + appUser.getEmail() + "]";
	}

	public enum FieldClass {
		KLASA1("KLASA I"),
		KLASA2("KLASA II"),
		KLASA3("KLASA III a/b"),
		KLASA4("KLASA IV a/b"),
		KLASA5("KLASA V"),
		KLASA6("KLASA VI");

		private String value;

		public String getValue() {
			return this.value;
		}

		private FieldClass(String value) {
			this.value = value;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((appUser == null) ? 0 : appUser.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + (int) (fieldNumber ^ (fieldNumber >>> 32));
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Field other = (Field) obj;
		if (appUser == null) {
			if (other.appUser != null)
				return false;
		} else if (!appUser.equals(other.appUser))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (fieldNumber != other.fieldNumber)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
