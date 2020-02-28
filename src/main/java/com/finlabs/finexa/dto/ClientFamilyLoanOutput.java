package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ClientFamilyLoanOutput implements Serializable, Cloneable {

	private static final long serialVersionUID = 1L;
	private int loanEmiNonEmi;
	private int loanId;
	private String loanType;
	private String loanProvider;
	private double originalPrincipal;
	private double outstandingPrincipal;
	private double interestRate;
	private double emi;
	private String loanStartDate;
	private String loanEndDate;
	private String InterestPayoutFreq;

	private String projectionYear;
	private double begningBal;
	private double principalPay;
	private double interestPay;
	private double endBal;
	private double emiAmount;
	private double totalPrincipalPaid;
	private double totalInterestPaid;
	
	//used in portfolio networth
	private String loanOriginalFlag;

	// private int loanProviderId;
	//private int loanProviderId;
	//new Code
	private String emiLoanProvider;

	private int loanCategoryId;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Calendar refDate;

	

	/**
	 * @param loanProviderId
	 *            the loanProviderId to set
	 */
	

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getLoanProvider() {
		return loanProvider;
	}

	public void setLoanProvider(String loanProvider) {
		this.loanProvider = loanProvider;
	}

	public double getOriginalPrincipal() {
		return originalPrincipal;
	}

	public void setOriginalPrincipal(double originalPrincipal) {
		this.originalPrincipal = originalPrincipal;
	}

	public double getOutstandingPrincipal() {
		return outstandingPrincipal;
	}

	public void setOutstandingPrincipal(double outstandingPrincipal) {
		this.outstandingPrincipal = outstandingPrincipal;
	}

	public double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(double interestRate) {
		this.interestRate = interestRate;
	}

	public double getEmi() {
		return emi;
	}

	public void setEmi(double emi) {
		this.emi = emi;
	}

	public String getLoanStartDate() {
		return loanStartDate;
	}

	public void setLoanStartDate(String loanStartDate) {
		this.loanStartDate = loanStartDate;
	}

	public String getLoanEndDate() {
		return loanEndDate;
	}

	public void setLoanEndDate(String loanEndDate) {
		this.loanEndDate = loanEndDate;
	}

	public String getInterestPayoutFreq() {
		return InterestPayoutFreq;
	}

	public void setInterestPayoutFreq(String interestPayoutFreq) {
		InterestPayoutFreq = interestPayoutFreq;
	}

	public String getProjectionYear() {
		return projectionYear;
	}

	public void setProjectionYear(String projectionYear) {
		this.projectionYear = projectionYear;
	}

	public double getBegningBal() {
		return begningBal;
	}

	public void setBegningBal(double begningBal) {
		this.begningBal = begningBal;
	}

	public double getPrincipalPay() {
		return principalPay;
	}

	public void setPrincipalPay(double principalPay) {
		this.principalPay = principalPay;
	}

	public double getInterestPay() {
		return interestPay;
	}

	public void setInterestPay(double interestPay) {
		this.interestPay = interestPay;
	}

	public double getEndBal() {
		return endBal;
	}

	public void setEndBal(double endBal) {
		this.endBal = endBal;
	}

	public double getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(double emiAmount) {
		this.emiAmount = emiAmount;
	}

	public double getTotalPrincipalPaid() {
		return totalPrincipalPaid;
	}

	public void setTotalPrincipalPaid(double totalPrincipalPaid) {
		this.totalPrincipalPaid = totalPrincipalPaid;
	}

	public double getTotalInterestPaid() {
		return totalInterestPaid;
	}

	public void setTotalInterestPaid(double totalInterestPaid) {
		this.totalInterestPaid = totalInterestPaid;
	}

	public void setTotalInterestPaid(int totalInterestPaid) {
		this.totalInterestPaid = totalInterestPaid;
	}

	public Calendar getRefDate() {
		return refDate;
	}

	public void setRefDate(Calendar refDate) {
		this.refDate = refDate;
	}

	public int getLoanCategoryId() {
		return loanCategoryId;
	}

	public void setLoanCategoryId(int loanCategoryId) {
		this.loanCategoryId = loanCategoryId;
	}

	public int getLoanEmiNonEmi() {
		return loanEmiNonEmi;
	}

	public void setLoanEmiNonEmi(int loanEmiNonEmi) {
		this.loanEmiNonEmi = loanEmiNonEmi;
	}

	public int getLoanId() {
		return loanId;
	}

	public void setLoanId(int loanId) {
		this.loanId = loanId;
	}

	public String getLoanOriginalFlag() {
		return loanOriginalFlag;
	}

	public void setLoanOriginalFlag(String loanOriginalFlag) {
		this.loanOriginalFlag = loanOriginalFlag;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return (ClientFamilyLoanOutput) super.clone();
	}

	public String getEmiLoanProvider() {
		return emiLoanProvider;
	}

	public void setEmiLoanProvider(String emiLoanProvider) {
		this.emiLoanProvider = emiLoanProvider;
	}
	
}
