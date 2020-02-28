package com.finlabs.finexa.dto;

import java.io.Serializable;

public class ClientFamilyNetSurplusOutput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String finYear;
	private double income;
	private double expense;
	private double committed_outflows;
	private double loan_outflows;
	private double net_surplus;
	
	// new types added for financial planning
	private int age;
	private double livingExpense;
	private double discreExpense;
	private double otherCO;
	private double investment;
	
	//for new FP report
	private String incomeForReport;
	private String expenseForReport;
	private String netSurplusForReport;
	
	public double getIncome() {
		return income;
	}

	public void setIncome(double income) {
		this.income = income;
	}

	public double getExpense() {
		return expense;
	}

	public void setExpense(double expense) {
		this.expense = expense;
	}

	public double getCommitted_outflows() {
		return committed_outflows;
	}

	public void setCommitted_outflows(double committed_outflows) {
		this.committed_outflows = committed_outflows;
	}

	public double getLoan_outflows() {
		return loan_outflows;
	}

	public void setLoan_outflows(double loan_outflows) {
		this.loan_outflows = loan_outflows;
	}

	public double getNet_surplus() {
		return net_surplus;
	}

	public void setNet_surplus(double net_surplus) {
		this.net_surplus = net_surplus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getFinYear() {
		return finYear;
	}

	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}

	public double getLivingExpense() {
		return livingExpense;
	}

	public void setLivingExpense(double livingExpense) {
		this.livingExpense = livingExpense;
	}

	public double getDiscreExpense() {
		return discreExpense;
	}

	public void setDiscreExpense(double discreExpense) {
		this.discreExpense = discreExpense;
	}

	public double getOtherCO() {
		return otherCO;
	}

	public void setOtherCO(double otherCO) {
		this.otherCO = otherCO;
	}

	public double getInvestment() {
		return investment;
	}

	public void setInvestment(double investment) {
		this.investment = investment;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public String getIncomeForReport() {
		return incomeForReport;
	}

	public void setIncomeForReport(String incomeForReport) {
		this.incomeForReport = incomeForReport;
	}
	
	public String getExpenseForReport() {
		return expenseForReport;
	}

	public void setExpenseForReport(String expenseForReport) {
		this.expenseForReport = expenseForReport;
	}
	
	public String getNetSurplusForReport() {
		return netSurplusForReport;
	}

	public void setNetSurplusForReport(String netSurplusForReport) {
		this.netSurplusForReport = netSurplusForReport;
	}

	@Override
	public String toString() {
		return "ClientFamilyNetSurplusOutput [finYear=" + finYear + ", income=" + income + ", expense=" + expense
				+ ", committed_outflows=" + committed_outflows + ", loan_outflows=" + loan_outflows + ", net_surplus="
				+ net_surplus + ", age=" + age + ", livingExpense=" + livingExpense + ", discreExpense=" + discreExpense
				+ ", otherCO=" + otherCO + ", investment=" + investment + ", incomeForReport=" + incomeForReport
				+ ", expenseForReport=" + expenseForReport + ", netSurplusForReport=" + netSurplusForReport + "]";
	}

}
