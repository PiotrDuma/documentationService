package com.github.PiotrDuma.documentationService.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *	bidirectional implementation of appUser and field entities.
 */
@Entity
@Table(name = "users")
public class AppUser {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id", unique = true, nullable = false)
	private long id;

	@Column(nullable = false)
	private String username;

	@Email
	@NotNull
	@NotEmpty
	@Column(nullable = false, unique = true)
	private String email;

	@OneToMany( mappedBy = "appUser", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@Fetch(FetchMode.JOIN)
	@JsonIgnore
	private List<Field> fields = new ArrayList<Field>();

	public AppUser() {
	}

	public AppUser(String username, @Email @NotNull String email) {
		super();
		this.username = username;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public List<Field> getFields() {
		return fields;
	}

	//(?) hibernate prepares its own setting method when its not public.
	protected void setFields(List<Field> list) {
		this.fields = list;
		list.forEach(e -> e.setAppUser(this));
	}

	public void addField(Field field) {
		fields.add(field);
		field.setAppUser(this);
	}

	public void removeField(Field field) {
		fields.remove(field);
		field.setAppUser(null);
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + ", username=" + username + ", email=" + email + ", fields="
				+ fields.stream().map(Field::getId).collect(Collectors.toList()) + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppUser other = (AppUser) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id != other.id)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

}
