package com.finlabs.finexa.dto;

import java.io.Serializable;

public class PortfolioSubAssetBencmarkDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String benchMark;
	private Double month1Value;
	private Double month3Value;
	private Double month6Value;
	private Double year1Value;
	private Double year3Value;
	private Double year5Value;
	private Double riskStdDev;

	// for market cap
	private String capName;

	private Double portfolioPerExposure;

	public String getBenchMark() {
		return benchMark;
	}

	public void setBenchMark(String benchMark) {
		this.benchMark = benchMark;
	}

	public Double getMonth1Value() {
		return month1Value;
	}

	public void setMonth1Value(Double month1Value) {
		this.month1Value = month1Value;
	}

	public Double getMonth3Value() {
		return month3Value;
	}

	public void setMonth3Value(Double month3Value) {
		this.month3Value = month3Value;
	}

	public Double getMonth6Value() {
		return month6Value;
	}

	public void setMonth6Value(Double month6Value) {
		this.month6Value = month6Value;
	}

	public Double getYear1Value() {
		return year1Value;
	}

	public void setYear1Value(Double year1Value) {
		this.year1Value = year1Value;
	}

	public Double getYear3Value() {
		return year3Value;
	}

	public void setYear3Value(Double year3Value) {
		this.year3Value = year3Value;
	}

	public Double getYear5Value() {
		return year5Value;
	}

	public void setYear5Value(Double year5Value) {
		this.year5Value = year5Value;
	}

	public Double getRiskStdDev() {
		return riskStdDev;
	}

	public void setRiskStdDev(Double riskStdDev) {
		this.riskStdDev = riskStdDev;
	}

	public String getCapName() {
		return capName;
	}

	public void setCapName(String capName) {
		this.capName = capName;
	}

	public Double getPortfolioPerExposure() {
		return portfolioPerExposure;
	}

	public void setPortfolioPerExposure(Double portfolioPerExposure) {
		this.portfolioPerExposure = portfolioPerExposure;
	}

}
