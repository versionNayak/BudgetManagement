package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class MasterExpenseIndustryStandardDTO implements Serializable {
	private int id;
	private String description;
	private double perc;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public double getPerc() {
		return perc;
	}
	public void setPerc(double perc) {
		this.perc = perc;
	}
	
}