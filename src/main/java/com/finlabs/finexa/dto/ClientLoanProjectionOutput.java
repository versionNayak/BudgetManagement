package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;

public class ClientLoanProjectionOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String providerId;
	private String providerName;
	private String categoryName;
	private int categoryId;
	// identify whose relation
	private String relation;

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	private List<ClientFamilyLoanOutput> clientFamilyLoanOutputList;

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public List<ClientFamilyLoanOutput> getClientFamilyLoanOutputList() {
		return clientFamilyLoanOutputList;
	}

	public void setClientFamilyLoanOutputList(List<ClientFamilyLoanOutput> clientFamilyLoanOutputList) {
		this.clientFamilyLoanOutputList = clientFamilyLoanOutputList;
	}

}