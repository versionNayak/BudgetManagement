package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class AnnualExpensesDetailed implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String finYear;
	private double groceries_amt;
	private double utilities_amt;
	private double transport_amt;
	private double houseHoldPersonal_amt;
	private double housing_amt;
	private double communication_amt;
	private double childrenFees_amt;
	private double healthCare_amt;
	private double lifeStyle_amt;
	private double apparels_amt;
	private double others_amt;
	private double livingExpense;
	private double discretionaryExpense;
	private double totalFamilyExpense;
	/**
   * @return the discretionaryExpense
   */
  public double getDiscretionaryExpense() {
    return discretionaryExpense;
  }

  /**
   * @param discretionaryExpense the discretionaryExpense to set
   */
  public void setDiscretionaryExpense(double discretionaryExpense) {
    this.discretionaryExpense = discretionaryExpense;
  }

  private ArrayList<AnnualExpensesDetailed> expenseProjectionList; 
	
	/**
   * @return the groceries_amt
   */
  public double getGroceries_amt() {
    return groceries_amt;
  }

  /**
   * @param groceries_amt the groceries_amt to set
   */
  public void setGroceries_amt(double groceries_amt) {
    this.groceries_amt = groceries_amt;
  }

  /**
   * @return the utilities_amt
   */
  public double getUtilities_amt() {
    return utilities_amt;
  }

  /**
   * @param utilities_amt the utilities_amt to set
   */
  public void setUtilities_amt(double utilities_amt) {
    this.utilities_amt = utilities_amt;
  }

  /**
   * @return the transport_amt
   */
  public double getTransport_amt() {
    return transport_amt;
  }

  /**
   * @param transport_amt the transport_amt to set
   */
  public void setTransport_amt(double transport_amt) {
    this.transport_amt = transport_amt;
  }

  /**
   * @return the houseHoldPersonal_amt
   */
  public double getHouseHoldPersonal_amt() {
    return houseHoldPersonal_amt;
  }

  /**
   * @param houseHoldPersonal_amt the houseHoldPersonal_amt to set
   */
  public void setHouseHoldPersonal_amt(double houseHoldPersonal_amt) {
    this.houseHoldPersonal_amt = houseHoldPersonal_amt;
  }

  /**
   * @return the housing_amt
   */
  public double getHousing_amt() {
    return housing_amt;
  }

  /**
   * @param housing_amt the housing_amt to set
   */
  public void setHousing_amt(double housing_amt) {
    this.housing_amt = housing_amt;
  }

  /**
   * @return the communication_amt
   */
  public double getCommunication_amt() {
    return communication_amt;
  }

  /**
   * @param communication_amt the communication_amt to set
   */
  public void setCommunication_amt(double communication_amt) {
    this.communication_amt = communication_amt;
  }

  /**
   * @return the childrenFees_amt
   */
  public double getChildrenFees_amt() {
    return childrenFees_amt;
  }

  /**
   * @param childrenFees_amt the childrenFees_amt to set
   */
  public void setChildrenFees_amt(double childrenFees_amt) {
    this.childrenFees_amt = childrenFees_amt;
  }

  /**
   * @return the healthCare_amt
   */
  public double getHealthCare_amt() {
    return healthCare_amt;
  }

  /**
   * @param healthCare_amt the healthCare_amt to set
   */
  public void setHealthCare_amt(double healthCare_amt) {
    this.healthCare_amt = healthCare_amt;
  }

  /**
   * @return the lifeStyle_amt
   */
  public double getLifeStyle_amt() {
    return lifeStyle_amt;
  }

  /**
   * @param lifeStyle_amt the lifeStyle_amt to set
   */
  public void setLifeStyle_amt(double lifeStyle_amt) {
    this.lifeStyle_amt = lifeStyle_amt;
  }

  /**
   * @return the apparels_amt
   */
  public double getApparels_amt() {
    return apparels_amt;
  }

  /**
   * @param apparels_amt the apparels_amt to set
   */
  public void setApparels_amt(double apparels_amt) {
    this.apparels_amt = apparels_amt;
  }

  /**
   * @return the others_amt
   */
  public double getOthers_amt() {
    return others_amt;
  }

  /**
   * @param others_amt the others_amt to set
   */
  public void setOthers_amt(double others_amt) {
    this.others_amt = others_amt;
  }

  /**
   * @return the livingExpense
   */
  public double getLivingExpense() {
    return livingExpense;
  }

  /**
   * @param livingExpense the livingExpense to set
   */
  public void setLivingExpense(double livingExpense) {
    this.livingExpense = livingExpense;
  }

  /**
   * @return the totalExpense
   */
  public double getTotalExpense() {
    return totalExpense;
  }

  /**
   * @param totalExpense the totalExpense to set
   */
  public void setTotalExpense(double totalExpense) {
    this.totalExpense = totalExpense;
  }
 
  private double totalExpense;
	

	/**
   * @return the expenseProjectionList
   */
  public ArrayList<AnnualExpensesDetailed> getExpenseProjectionList() {
    return expenseProjectionList;
  }

  /**
   * @param expenseProjectionList the expenseProjectionList to set
   */
  public void setExpenseProjectionList(ArrayList<AnnualExpensesDetailed> expenseProjectionList) {
    this.expenseProjectionList = expenseProjectionList;
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

	public double getTotalFamilyExpense() {
		return totalFamilyExpense;
	}

	public void setTotalFamilyExpense(double totalFamilyExpense) {
		this.totalFamilyExpense = totalFamilyExpense;
	}

}
