package com.finlabs.finexa.dto;

import java.io.Serializable;
import java.util.List;

import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;

public class ClientFamilyIncomeOutput implements Serializable {

	  private static final long serialVersionUID = 1L;
	  private double salaryIncome;
	  private double bonousIncome;
	  private double professionalFee;
	  private double bussinessIncome;
	  private double rentalIncome;
	  private double pension;
	  private double otherIncome;
	  private double interestIncome;
	  private double individualTotalIncome;
	  private double lumpsumInflow;
	  private double totalIncome;
	  private String year;
	  private String currentDate;
	  private int age;

	  public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	List<ClientFamilyIncomeOutput> clientFamilyList;

	  public ClientFamilyIncomeOutput() {

	  }

	  public double getSalaryIncome() {
	    return salaryIncome;
	  }

	  public void setSalaryIncome(double salaryIncome) {
	    this.salaryIncome = salaryIncome;
	  }

	  public double getBonousIncome() {
	    return bonousIncome;
	  }

	  public void setBonousIncome(double bonousIncome) {
	    this.bonousIncome = bonousIncome;
	  }

	  public double getProfessionalFee() {
	    return professionalFee;
	  }

	  public void setProfessionalFee(double professionalFee) {
	    this.professionalFee = professionalFee;
	  }

	  public double getBussinessIncome() {
	    return bussinessIncome;
	  }

	  public void setBussinessIncome(double bussinessIncome) {
	    this.bussinessIncome = bussinessIncome;
	  }

	  public double getRentalIncome() {
	    return rentalIncome;
	  }

	  public void setRentalIncome(double rentalIncome) {
	    this.rentalIncome = rentalIncome;
	  }

	  public double getPension() {
	    return pension;
	  }

	  public void setPension(double pension) {
	    this.pension = pension;
	  }

	  public double getOtherIncome() {
	    return otherIncome;
	  }

	  public void setOtherIncome(double otherIncome) {
	    this.otherIncome = otherIncome;
	  }

	  public double getTotalIncome() {
	    return totalIncome;
	  }

	  public void setTotalIncome(double totalIncome) {
	    this.totalIncome = totalIncome;
	  }

	  public String getYear() {
	    return year;
	  }

	  public void setYear(String year) {
	    this.year = year;
	  }

	  public List<ClientFamilyIncomeOutput> getClientFamilyList() {
	    return clientFamilyList;
	  }

	  public void setClientFamilyList(List<ClientFamilyIncomeOutput> clientFamilyList) {
	    this.clientFamilyList = clientFamilyList;
	  }

	  public String getCurrentDate() {
	    return currentDate;
	  }

	  public void setCurrentDate(String currentDate) {
	    this.currentDate = currentDate;
	  }

	  public double getInterestIncome() {
	    return interestIncome;
	  }

	  public void setInterestIncome(double interestIncome) {
	    this.interestIncome = interestIncome;
	  }

	public double getIndividualTotalIncome() {
		return individualTotalIncome;
	}

	public void setIndividualTotalIncome(double individualTotalIncome) {
		this.individualTotalIncome = individualTotalIncome;
	}

	public double getLumpsumInflow() {
		return lumpsumInflow;
	}

	public void setLumpsumInflow(double lumpsumInflow) {
		this.lumpsumInflow = lumpsumInflow;
	}

}
