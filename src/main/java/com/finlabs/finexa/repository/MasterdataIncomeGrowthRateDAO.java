package com.finlabs.finexa.repository;

import java.util.Date;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;

public interface MasterdataIncomeGrowthRateDAO {

	public MasterIncomeGrowthRate getIncomeGrowthRate(int incomeHead, Date currDate, Session session)
			throws FinexaDaoException;

	public MasterIncomeGrowthRate getIncomeGrowthRateByIncomeHead(int incomeType, Session session)
			throws FinexaDaoException;
	
	public double getIncomeGrowthRateAvg(Date currDate, Session session)
			throws FinexaDaoException;
}
