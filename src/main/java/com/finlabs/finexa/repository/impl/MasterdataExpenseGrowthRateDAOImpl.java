package com.finlabs.finexa.repository.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.MasterExpenseGrowthRate;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;
import com.finlabs.finexa.repository.MasterdataExpenseGrowthRateDAO;

//@Repository
public class MasterdataExpenseGrowthRateDAOImpl implements MasterdataExpenseGrowthRateDAO {

	@Override
	public MasterExpenseGrowthRate getExpenseGrowthRateByIncomeHead(int expenseType, Session session)
			throws FinexaDaoException {
		try {
			MasterExpenseGrowthRate growthRate = null;

			Criteria criteria = session.createCriteria(MasterExpenseGrowthRate.class);
			criteria.add(Restrictions.eq("id", (int) expenseType));

			growthRate = (MasterExpenseGrowthRate) criteria.uniqueResult();
			// session.clear();
			return growthRate;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}
	
	@Override
	public double getExpenseGrowthRateAvg(Date currDate, Session session)
			throws FinexaDaoException {

		try {

			double expenseGrowthrate;
			expenseGrowthrate = (double) session.createQuery(
					"select avg(masterExpGr.cagr)FROM MasterExpenseGrowthRate masterExpGr where :currDate between masterExpGr.fromDate and masterExpGr.toDate")
					.setParameter("currDate", currDate).uniqueResult();
			// session.clear();
			return expenseGrowthrate;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());
		}

	}

}
