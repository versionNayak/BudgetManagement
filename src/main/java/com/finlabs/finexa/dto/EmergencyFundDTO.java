package com.finlabs.finexa.dto;

import java.io.Serializable;

public class EmergencyFundDTO implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int month;
	private String year;
	private int clientAge;
	private double emergContigencyFundRequired;
	private double contigencyFundAvail;
	private double netplusDeflict;
	private double annualSurplus;
	private double emergencyFundAllocated;
	private double totalContigencyFund;

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getClientAge() {
		return clientAge;
	}

	public void setClientAge(int clientAge) {
		this.clientAge = clientAge;
	}

	public double getEmergContigencyFundRequired() {
		return emergContigencyFundRequired;
	}

	public void setEmergContigencyFundRequired(double emergContigencyFundRequired) {
		this.emergContigencyFundRequired = emergContigencyFundRequired;
	}

	public double getContigencyFundAvail() {
		return contigencyFundAvail;
	}

	public void setContigencyFundAvail(double contigencyFundAvail) {
		this.contigencyFundAvail = contigencyFundAvail;
	}

	public double getNetplusDeflict() {
		return netplusDeflict;
	}

	public void setNetplusDeflict(double netplusDeflict) {
		this.netplusDeflict = netplusDeflict;
	}

	public double getAnnualSurplus() {
		return annualSurplus;
	}

	public void setAnnualSurplus(double annualSurplus) {
		this.annualSurplus = annualSurplus;
	}

	public double getEmergencyFundAllocated() {
		return emergencyFundAllocated;
	}

	public void setEmergencyFundAllocated(double emergencyFundAllocated) {
		this.emergencyFundAllocated = emergencyFundAllocated;
	}

	public double getTotalContigencyFund() {
		return totalContigencyFund;
	}

	public void setTotalContigencyFund(double totalContigencyFund) {
		this.totalContigencyFund = totalContigencyFund;
	}

}