package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class ClientFamilyExpenseOutput implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String expenseId;
	private String endYear;
	private BigDecimal expenseAmount;
	private int expenseType;
	private int frequency;
	private String referenceMonth;
	public String getExpenseId() {
		return expenseId;
	}
	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}
	public String getEndYear() {
		return endYear;
	}
	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}
	public BigDecimal getExpenseAmount() {
		return expenseAmount;
	}
	public void setExpenseAmount(BigDecimal expenseAmount) {
		this.expenseAmount = expenseAmount;
	}
	public int getExpenseType() {
		return expenseType;
	}
	public void setExpenseType(int expenseType) {
		this.expenseType = expenseType;
	}
	public int getFrequency() {
		return frequency;
	}
	public void setFrequency(int frequency) {
		this.frequency = frequency;
	}
	public String getReferenceMonth() {
		return referenceMonth;
	}
	public void setReferenceMonth(String referenceMonth) {
		this.referenceMonth = referenceMonth;
	}
     
}
