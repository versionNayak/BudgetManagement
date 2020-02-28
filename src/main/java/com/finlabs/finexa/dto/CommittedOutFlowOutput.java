package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommittedOutFlowOutput implements Serializable {

	private static final long serialVersionUID = 1L;
	private String projectionYear;
	private String premiumName;
	private double premiumAMount;
	private double premiumAmountHealth;
	private double premiumAmountGeneral;

	public double getPremiumAmountGeneral() {
		return premiumAmountGeneral;
	}

	public void setPremiumAmountGeneral(double premiumAmountGeneral) {
		this.premiumAmountGeneral = premiumAmountGeneral;
	}

	/**
	 * @return the premiumAmountHealth
	 */
	public double getPremiumAmountHealth() {
		return premiumAmountHealth;
	}

	/**
	 * @param premiumAmountHealth
	 *            the premiumAmountHealth to set
	 */
	public void setPremiumAmountHealth(double premiumAmountHealth) {
		this.premiumAmountHealth = premiumAmountHealth;
	}

	private double investmentAmount;
	private double totalOutFlow;

	private List<CommittedOutFlowOutput> outFlowList = new ArrayList<CommittedOutFlowOutput>();

	public double getTotalOutFlow() {
		return totalOutFlow;
	}

	public void setTotalOutFlow(double totalOutFlow) {
		this.totalOutFlow = totalOutFlow;
	}

	public List<CommittedOutFlowOutput> getOutFlowList() {
		return outFlowList;
	}

	public void setOutFlowList(List<CommittedOutFlowOutput> outFlowList) {
		this.outFlowList = outFlowList;
	}

	public String getProjectionYear() {
		return projectionYear;
	}

	public void setProjectionYear(String projectionYear) {
		this.projectionYear = projectionYear;
	}

	public String getPremiumName() {
		return premiumName;
	}

	public void setPremiumName(String premiumName) {
		this.premiumName = premiumName;
	}

	public double getPremiumAMount() {
		return premiumAMount;
	}

	public void setPremiumAMount(double premiumAMount) {
		this.premiumAMount = premiumAMount;
	}

	public double getInvestmentAmount() {
		return investmentAmount;
	}

	public void setInvestmentAmount(double investmentAmount) {
		this.investmentAmount = investmentAmount;
	}

}
