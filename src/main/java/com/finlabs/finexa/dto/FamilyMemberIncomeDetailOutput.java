package com.finlabs.finexa.dto;

import java.io.Serializable;

public class FamilyMemberIncomeDetailOutput implements Serializable {
  
  private String incomeCategory;
  /**
   * @return the incomeCategory
   */
  public String getIncomeCategory() {
    return incomeCategory;
  }
  /**
   * @param incomeCategory the incomeCategory to set
   */
  public void setIncomeCategory(String incomeCategory) {
    this.incomeCategory = incomeCategory;
  }
  private int memberId;
  /**
   * @return the memberId
   */
  public int getMemberId() {
    return memberId;
  }
  /**
   * @param memberId the memberId to set
   */
  public void setMemberId(int memberId) {
    this.memberId = memberId;
  }
  /**
   * @return the income
   */
  public double getIncome() {
    return income;
  }
  /**
   * @param income the income to set
   */
  public void setIncome(double income) {
    this.income = income;
  }
  /**
   * @return the frequency
   */
  public String getFrequency() {
    return frequency;
  }
  /**
   * @param frequency the frequency to set
   */
  public void setFrequency(String frequency) {
    this.frequency = frequency;
  }
  /**
   * @return the continueUpto
   */
  public String getContinueUpto() {
    return continueUpto;
  }
  /**
   * @param continueUpto the continueUpto to set
   */
  public void setContinueUpto(String continueUpto) {
    this.continueUpto = continueUpto;
  }
  /**
   * @return the annualIncomeGrowthRate
   */
  public double getAnnualIncomeGrowthRate() {
    return annualIncomeGrowthRate;
  }
  /**
   * @param annualIncomeGrowthRate the annualIncomeGrowthRate to set
   */
  public void setAnnualIncomeGrowthRate(double annualIncomeGrowthRate) {
    this.annualIncomeGrowthRate = annualIncomeGrowthRate;
  }
  private double income;
  private String frequency;
  private String continueUpto;
  private String referenceMonth;
  private double annualIncomeGrowthRate;
public String getReferenceMonth() {
	return referenceMonth;
}
public void setReferenceMonth(String referenceMonth) {
	this.referenceMonth = referenceMonth;
}

}
