package com.finlabs.finexa.dto;

import java.io.Serializable;

/**
 * @author vishwajeet
 *
 */
public class GoalAssetUsedDTO  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3077928814594289026L;
	private int goalId;
	private String goalName;
	private double assetValueUsed;
	// extra field added required for cashflows
	private double assetPercUsed;

	private double assetCurrentValueUsed;
	// extra field added required for cashflows
	private double assetCurrentPercUsed;
	private int maturityYear;
	private int prodClassificationId;
	private double notEarMarkedPercent;
	private double notEarMarkedAssetValue;
	private double totalGoalCorpusAfterEarmarking;
	private double pmt;

	private int productId;
	private String productName;

	public int getGoalId() {
		return goalId;
	}

	public void setGoalId(int goalId) {
		this.goalId = goalId;
	}

	public String getGoalName() {
		return goalName;
	}

	public void setGoalName(String goalName) {
		this.goalName = goalName;
	}

	public double getAssetValueUsed() {
		return assetValueUsed;
	}

	public void setAssetValueUsed(double assetValueUsed) {
		this.assetValueUsed = assetValueUsed;
	}

	public int getMaturityYear() {
		return maturityYear;
	}

	public void setMaturityYear(int maturityYear) {
		this.maturityYear = maturityYear;
	}

	public double getNotEarMarkedPercent() {
		return notEarMarkedPercent;
	}

	public void setNotEarMarkedPercent(double notEarMarkedPercent) {
		this.notEarMarkedPercent = notEarMarkedPercent;
	}

	public double getNotEarMarkedAssetValue() {
		return notEarMarkedAssetValue;
	}

	public void setNotEarMarkedAssetValue(double notEarMarkedAssetValue) {
		this.notEarMarkedAssetValue = notEarMarkedAssetValue;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getAssetPercUsed() {
		return assetPercUsed;
	}

	public void setAssetPercUsed(double assetPercUsed) {
		this.assetPercUsed = assetPercUsed;
	}

	public int getProdClassificationId() {
		return prodClassificationId;
	}

	public void setProdClassificationId(int prodClassificationId) {
		this.prodClassificationId = prodClassificationId;
	}

	public double getTotalGoalCorpusAfterEarmarking() {
		return totalGoalCorpusAfterEarmarking;
	}

	public void setTotalGoalCorpusAfterEarmarking(double totalGoalCorpusAfterEarmarking) {
		this.totalGoalCorpusAfterEarmarking = totalGoalCorpusAfterEarmarking;
	}

	public double getPmt() {
		return pmt;
	}

	public void setPmt(double pmt) {
		this.pmt = pmt;
	}

	public double getAssetCurrentValueUsed() {
		return assetCurrentValueUsed;
	}

	public void setAssetCurrentValueUsed(double assetCurrentValueUsed) {
		this.assetCurrentValueUsed = assetCurrentValueUsed;
	}

	public double getAssetCurrentPercUsed() {
		return assetCurrentPercUsed;
	}

	public void setAssetCurrentPercUsed(double assetCurrentPercUsed) {
		this.assetCurrentPercUsed = assetCurrentPercUsed;
	}

}