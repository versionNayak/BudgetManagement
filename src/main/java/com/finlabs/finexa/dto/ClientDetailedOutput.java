package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;

public class ClientDetailedOutput implements Serializable {
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	private long memberId;
	private int relation;
	private int clientId;
	private Integer lifeExp;
	private String genderString;
	private List<ClientDetailedOutput> outputList;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * @return the relation
	 */
	public int getRelation() {
		return relation;
	}
	/**
	 * @param relation the relation to set
	 */
	public void setRelation(int relation) {
		this.relation = relation;
	}
	/**
	 * @return the memberId
	 */
	public long getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(long memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}
	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(int clientId) {
		this.clientId = clientId;
	}
	/**
	 * @return the lifeExp
	 */
	public Integer getLifeExp() {
		return lifeExp;
	}
	/**
	 * @param lifeExp the lifeExp to set
	 */
	public void setLifeExp(Integer lifeExp) {
		this.lifeExp = lifeExp;
	}
	/**
	 * @return the outputList
	 */
	public List<ClientDetailedOutput> getOutputList() {
		return outputList;
	}
	/**
	 * @param outputList the outputList to set
	 */
	public void setOutputList(List<ClientDetailedOutput> outputList) {
		this.outputList = outputList;
	}
	public String getGenderString() {
		return genderString;
	}
	public void setGenderString(String genderString) {
		this.genderString = genderString;
	}

}