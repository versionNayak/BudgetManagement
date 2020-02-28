package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author vishwajeet
 *
 */
public class ClientGoalDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String description;
	private Double estimatedCostOfGoal;
	private int calculatedGoalPriority;
	private Byte priority;
	private String startMonthYear;
	private int clientId;
	private int clientFamilyMemberId;
	private Double assetEarmarkedFV;
	private Double goalAchieved;
	private Integer yearsToGoal;
	private String recurringFlag;
	private int goalTypeId;
	private Map<String, Double> pmtMap;
	private Map<String, List<GoalAssetUsedDTO>> goalAssetMapList;
	private Map<String, String> assetListUsedMap;
	

	// for iteration purpose
	private double percentReducedAfterIteration;

	public ClientGoalDTO() {

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getEstimatedCostOfGoal() {
		return estimatedCostOfGoal;
	}

	public void setEstimatedCostOfGoal(Double estimatedCostOfGoal) {
		this.estimatedCostOfGoal = estimatedCostOfGoal;
	}

	public int getCalculatedGoalPriority() {
		return calculatedGoalPriority;
	}

	public void setCalculatedGoalPriority(int calculatedGoalPriority) {
		this.calculatedGoalPriority = calculatedGoalPriority;
	}

	public Byte getPriority() {
		return priority;
	}

	public void setPriority(Byte priority) {
		this.priority = priority;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public int getClientFamilyMemberId() {
		return clientFamilyMemberId;
	}

	public void setClientFamilyMemberId(int clientFamilyMemberId) {
		this.clientFamilyMemberId = clientFamilyMemberId;
	}

	public String getStartMonthYear() {
		return startMonthYear;
	}

	public void setStartMonthYear(String startMonthYear) {
		this.startMonthYear = startMonthYear;
	}

	public Double getAssetEarmarkedFV() {
		return assetEarmarkedFV;
	}

	public void setAssetEarmarkedFV(Double assetEarmarkedFV) {
		this.assetEarmarkedFV = assetEarmarkedFV;
	}

	public Double getGoalAchieved() {
		return goalAchieved;
	}

	public void setGoalAchieved(Double goalAchieved) {
		this.goalAchieved = goalAchieved;
	}

	public Map<String, Double> getPmtMap() {
		return pmtMap;
	}

	public void setPmtMap(Map<String, Double> pmtMap) {
		this.pmtMap = pmtMap;
	}

	public double getPercentReducedAfterIteration() {
		return percentReducedAfterIteration;
	}

	public void setPercentReducedAfterIteration(double percentReducedAfterIteration) {
		this.percentReducedAfterIteration = percentReducedAfterIteration;
	}

	public Integer getYearsToGoal() {
		return yearsToGoal;
	}

	public void setYearsToGoal(Integer yearsToGoal) {
		this.yearsToGoal = yearsToGoal;
	}

	public String getRecurringFlag() {
		return recurringFlag;
	}

	public void setRecurringFlag(String recurringFlag) {
		this.recurringFlag = recurringFlag;
	}

	public int getGoalTypeId() {
		return goalTypeId;
	}

	public void setGoalTypeId(int goalTypeId) {
		this.goalTypeId = goalTypeId;
	}

	public Map<String, List<GoalAssetUsedDTO>> getGoalAssetMapList() {
		return goalAssetMapList;
	}

	public void setGoalAssetMapList(Map<String, List<GoalAssetUsedDTO>> goalAssetMapList) {
		this.goalAssetMapList = goalAssetMapList;
	}

	public Map<String, String> getAssetListUsedMap() {
		return assetListUsedMap;
	}

	public void setAssetListUsedMap(Map<String, String> assetListUsedMap) {
		this.assetListUsedMap = assetListUsedMap;
	}

	
}