package com.finlabs.finexa.dto;

import java.io.Serializable;

public class FutureLoanProjectionDTO  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String finYear;
	public double netSurplus;
	public double actualEmi;
	public double achievableEmi;
	public int noOfMonths;
	public double ifEmiNotMet;
	public String getFinYear() {
		return finYear;
	}
	public void setFinYear(String finYear) {
		this.finYear = finYear;
	}
	public double getNetSurplus() {
		return netSurplus;
	}
	public void setNetSurplus(double netSurplus) {
		this.netSurplus = netSurplus;
	}
	public double getActualEmi() {
		return actualEmi;
	}
	public void setActualEmi(double actualEmi) {
		this.actualEmi = actualEmi;
	}
	public double getAchievableEmi() {
		return achievableEmi;
	}
	public void setAchievableEmi(double achievableEmi) {
		this.achievableEmi = achievableEmi;
	}
	public int getNoOfMonths() {
		return noOfMonths;
	}
	public void setNoOfMonths(int noOfMonths) {
		this.noOfMonths = noOfMonths;
	}
	public double getIfEmiNotMet() {
		return ifEmiNotMet;
	}
	public void setIfEmiNotMet(double ifEmiNotMet) {
		this.ifEmiNotMet = ifEmiNotMet;
	}
}