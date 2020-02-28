package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class PortfolioOverviewDto implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// getting the id from masterProduct classification
	private int productId;
	private String productName;
	private String productType;
	private String productDesc;
	private int productTypeId;
	private String assetClassification;
	private int assetClassificationId;
	private String subAssetClassification;
	private int subAssetClassificationId;
	private String investmentOrPerson;
	private String investmentOrPersonFlag;
	private String marketLinkOrFixedReturn;
	private double currentValue;
	private double currentPortfolioWeight;
	private String investmentDate;
	private double investmentValue;
	private String lockedInDate;
	private String maturityDate;
	private String timeToMaturity;
	private String st_ltClassification;
	private String cagr;
	private double assetWeightReturn;
	private String ptpReturns;
	private double gains;
	private double maturityAmount;
	private String clientName;
	private String investmentValueEshopEPF;
	private String gainsEshopEpf;
	private String productDescLong;
	private Map<String, Double> mfMaturityProfile;
	private Map<String, Double> riskMeasureMap;
	
	private String capRank;
	public String getCapRank() {
		return capRank;
	}

	public void setCapRank(String capRank) {
		this.capRank = capRank;
	}

	public String getCapRankValue() {
		return capRankValue;
	}

	public void setCapRankValue(String capRankValue) {
		this.capRankValue = capRankValue;
	}

	private String capRankValue;
	
	//MF market Cap Field
	private Double largeCapMFPerc;
	public Double getLargeCapMFPerc() {
		return largeCapMFPerc;
	}

	public void setLargeCapMFPerc(Double largeCapMFPerc) {
		this.largeCapMFPerc = largeCapMFPerc;
	}

	public Double getMidCapMFPerc() {
		return midCapMFPerc;
	}

	public void setMidCapMFPerc(Double midCapMFPerc) {
		this.midCapMFPerc = midCapMFPerc;
	}

	public Double getSmallcapMFPerc() {
		return smallcapMFPerc;
	}

	public void setSmallcapMFPerc(Double smallcapMFPerc) {
		this.smallcapMFPerc = smallcapMFPerc;
	}

	private Double midCapMFPerc;
	private Double smallcapMFPerc;
	private Double otherCapMFPerc;
	
	// equity market cap field
	private String marketCapName;
	public String getMarketCapName() {
		return marketCapName;
	}

	public void setMarketCapName(String marketCapName) {
		this.marketCapName = marketCapName;
	}

	public int getMarketCapId() {
		return marketCapId;
	}

	public void setMarketCapId(int marketCapId) {
		this.marketCapId = marketCapId;
	}

	private int marketCapId;
	
	// equity overview fields
	private double percTotal;
	private String researchRank;

	// For MF
	private Integer amfiCode;

	// For Equity
	private String isin;

	// for retirement schemes
	private String planType;

	private List<PortfolioSubAssetBencmarkDto> marketCapList;

	// used by financial management
	private int portFolioId;
	private String financialAssetName;
	private double notEarmarkedPercent;

	// for financial isurance purpose
	private String familyRelation;
	private String bankIssuerName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getAssetClassification() {
		return assetClassification;
	}

	public void setAssetClassification(String assetClassification) {
		this.assetClassification = assetClassification;
	}

	public String getSubAssetClassification() {
		return subAssetClassification;
	}

	public void setSubAssetClassification(String subAssetClassification) {
		this.subAssetClassification = subAssetClassification;
	}

	public String getInvestmentOrPerson() {
		return investmentOrPerson;
	}

	public void setInvestmentOrPerson(String investmentOrPerson) {
		this.investmentOrPerson = investmentOrPerson;
	}

	public String getMarketLinkOrFixedReturn() {
		return marketLinkOrFixedReturn;
	}

	public void setMarketLinkOrFixedReturn(String marketLinkOrFixedReturn) {
		this.marketLinkOrFixedReturn = marketLinkOrFixedReturn;
	}

	public double getCurrentValue() {
		return currentValue;
	}

	public void setCurrentValue(double currentValue) {
		this.currentValue = currentValue;
	}

	public double getCurrentPortfolioWeight() {
		return currentPortfolioWeight;
	}

	public void setCurrentPortfolioWeight(double currentPortfolioWeight) {
		this.currentPortfolioWeight = currentPortfolioWeight;
	}

	public String getInvestmentDate() {
		return investmentDate;
	}

	public void setInvestmentDate(String investmentDate) {
		this.investmentDate = investmentDate;
	}

	public double getInvestmentValue() {
		return investmentValue;
	}

	public void setInvestmentValue(double investmentValue) {
		this.investmentValue = investmentValue;
	}

	public String getLockedInDate() {
		return lockedInDate;
	}

	public void setLockedInDate(String lockedInDate) {
		this.lockedInDate = lockedInDate;
	}

	public String getTimeToMaturity() {
		return timeToMaturity;
	}

	public void setTimeToMaturity(String timeToMaturity) {
		this.timeToMaturity = timeToMaturity;
	}

	public String getSt_ltClassification() {
		return st_ltClassification;
	}

	public void setSt_ltClassification(String st_ltClassification) {
		this.st_ltClassification = st_ltClassification;
	}

	public String getCagr() {
		return cagr;
	}

	public void setCagr(String cagr) {
		this.cagr = cagr;
	}

	public double getAssetWeightReturn() {
		return assetWeightReturn;
	}

	public void setAssetWeightReturn(double assetWeightReturn) {
		this.assetWeightReturn = assetWeightReturn;
	}

	public String getPtpReturns() {
		return ptpReturns;
	}

	public void setPtpReturns(String ptpReturns) {
		this.ptpReturns = ptpReturns;
	}

	public double getGains() {
		return gains;
	}

	public void setGains(double gains) {
		this.gains = gains;
	}

	public double getMaturityAmount() {
		return maturityAmount;
	}

	public void setMaturityAmount(double maturityAmount) {
		this.maturityAmount = maturityAmount;
	}

	public String getMaturityDate() {
		return maturityDate;
	}

	public void setMaturityDate(String maturityDate) {
		this.maturityDate = maturityDate;
	}

	public double getPercTotal() {
		return percTotal;
	}

	public void setPercTotal(double percTotal) {
		this.percTotal = percTotal;
	}

	public String getResearchRank() {
		return researchRank;
	}

	public void setResearchRank(String researchRank) {
		this.researchRank = researchRank;
	}

	public Integer getAmfiCode() {
		return amfiCode;
	}

	public void setAmfiCode(Integer amfiCode) {
		this.amfiCode = amfiCode;
	}

	public List<PortfolioSubAssetBencmarkDto> getMarketCapList() {
		return marketCapList;
	}

	public void setMarketCapList(List<PortfolioSubAssetBencmarkDto> marketCapList) {
		this.marketCapList = marketCapList;
	}

	public String getIsin() {
		return isin;
	}

	public void setIsin(String isin) {
		this.isin = isin;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public String getInvestmentOrPersonFlag() {
		return investmentOrPersonFlag;
	}

	public void setInvestmentOrPersonFlag(String investmentOrPersonFlag) {
		this.investmentOrPersonFlag = investmentOrPersonFlag;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public int getAssetClassificationId() {
		return assetClassificationId;
	}

	public void setAssetClassificationId(int assetClassificationId) {
		this.assetClassificationId = assetClassificationId;
	}

	public int getSubAssetClassificationId() {
		return subAssetClassificationId;
	}

	public void setSubAssetClassificationId(int subAssetClassificationId) {
		this.subAssetClassificationId = subAssetClassificationId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getProductDesc() {
		return productDesc;
	}

	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}

	public int getPortFolioId() {
		return portFolioId;
	}

	public void setPortFolioId(int portFolioId) {
		this.portFolioId = portFolioId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getBankIssuerName() {
		return bankIssuerName;
	}

	public void setBankIssuerName(String bankIssuerName) {
		this.bankIssuerName = bankIssuerName;
	}

	public String getFinancialAssetName() {
		return financialAssetName;
	}

	public void setFinancialAssetName(String financialAssetName) {
		this.financialAssetName = financialAssetName;
	}

	public double getNotEarmarkedPercent() {
		return notEarmarkedPercent;
	}

	public void setNotEarmarkedPercent(double notEarmarkedPercent) {
		this.notEarmarkedPercent = notEarmarkedPercent;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getFamilyRelation() {
		return familyRelation;
	}

	public void setFamilyRelation(String familyRelation) {
		this.familyRelation = familyRelation;
	}

	public Map<String, Double> getMfMaturityProfile() {
		return mfMaturityProfile;
	}

	public void setMfMaturityProfile(Map<String, Double> mfMaturityProfile) {
		this.mfMaturityProfile = mfMaturityProfile;
	}

	public Map<String, Double> getRiskMeasureMap() {
		return riskMeasureMap;
	}

	public void setRiskMeasureMap(Map<String, Double> riskMeasureMap) {
		this.riskMeasureMap = riskMeasureMap;
	}

	
	public String getInvestmentValueEshopEPF() {
		return investmentValueEshopEPF;
	}

	public void setInvestmentValueEshopEPF(String investmentValueEshopEPF) {
		this.investmentValueEshopEPF = investmentValueEshopEPF;
	}

	public String getGainsEshopEpf() {
		return gainsEshopEpf;
	}

	public void setGainsEshopEpf(String gainsEshopEpf) {
		this.gainsEshopEpf = gainsEshopEpf;
	}

	public Double getOtherCapMFPerc() {
		return otherCapMFPerc;
	}

	public void setOtherCapMFPerc(Double otherCapMFPerc) {
		this.otherCapMFPerc = otherCapMFPerc;
	}

	public String getProductDescLong() {
		return productDescLong;
	}

	public void setProductDescLong(String productDescLong) {
		this.productDescLong = productDescLong;
	}




}
