package com.finlabs.finexa.repository;

import java.util.Date;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.MasterExpenseGrowthRate;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;

public interface MasterdataExpenseGrowthRateDAO {

	public MasterExpenseGrowthRate getExpenseGrowthRateByIncomeHead(int expenseType, Session session)
			throws FinexaDaoException;
	
	public double getExpenseGrowthRateAvg(Date currDate, Session session)
			throws FinexaDaoException;
}
