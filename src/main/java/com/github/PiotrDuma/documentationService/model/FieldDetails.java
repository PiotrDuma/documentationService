package com.github.PiotrDuma.documentationService.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "field_details")
public class FieldDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //Id is based on parent entity.
	@Column(name = "details_id", nullable = true)
	private long id;

	@OneToOne(fetch = FetchType.LAZY)
	private Field field;

	private String voivodeship;
	private String commune;
	private String village;
	private double realArea;
	private double recountedArea;

	public FieldDetails() {
	}

	public FieldDetails(String voivodeship, String commune, String village, double realArea, double recountedArea) {
		super();
		this.voivodeship = voivodeship;
		this.commune = commune;
		this.village = village;
		this.realArea = realArea;
		this.recountedArea = recountedArea;
	}

	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}

	public String getVoivodeship() {
		return voivodeship;
	}

	public void setVoivodeship(String voivodeship) {
		this.voivodeship = voivodeship;
	}

	public String getCommune() {
		return commune;
	}

	public void setCommune(String commune) {
		this.commune = commune;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public double getRealArea() {
		return realArea;
	}

	public void setRealArea(double realArea) {
		this.realArea = realArea;
	}

	public double getRecountedArea() {
		return recountedArea;
	}

	public void setRecountedArea(double recountedArea) {
		this.recountedArea = recountedArea;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	@Override
	public String toString() {
		return "FieldDetailsComponent [voivodeship=" + voivodeship + ", commune=" + commune + ", village=" + village
				+ ", realArea=" + realArea + ", recountedArea=" + recountedArea + "]";
	}
}
