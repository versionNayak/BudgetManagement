package com.finlabs.finexa.repository.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.MasterIncomeGrowthRate;
import com.finlabs.finexa.repository.MasterdataIncomeGrowthRateDAO;

//@Repository
public class MasterdataIncomeGrowthRateDAOImpl implements MasterdataIncomeGrowthRateDAO {

	@Override
	public MasterIncomeGrowthRate getIncomeGrowthRate(int incomeHead, Date currDate, Session session)
			throws FinexaDaoException {

		try {

			MasterIncomeGrowthRate incomeGrowth = null;
			incomeGrowth = (MasterIncomeGrowthRate) session.createQuery(
					"FROM MasterIncomeGrowthRate masterIncGr where :currDate between masterIncGr.fromDate and masterIncGr.toDate and masterIncGr.id=:incomeHead")
					.setParameter("currDate", currDate).setParameter("incomeHead", incomeHead).uniqueResult();
			// session.clear();
			return incomeGrowth;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());
		}

	}

	@Override
	public MasterIncomeGrowthRate getIncomeGrowthRateByIncomeHead(int incomeType, Session session)
			throws FinexaDaoException {

		try {

			MasterIncomeGrowthRate growthRate = null;

			Criteria criteria = session.createCriteria(MasterIncomeGrowthRate.class);
			criteria.add(Restrictions.eq("id", (int) incomeType));

			growthRate = (MasterIncomeGrowthRate) criteria.uniqueResult();
			// session.clear();
			return growthRate;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}
	
	@Override
	public double getIncomeGrowthRateAvg(Date currDate, Session session)
			throws FinexaDaoException {

		try {

			double incomeGrowthRate;
			 incomeGrowthRate = (double) session.createQuery(
					"select avg(masterIncGr.cagr)FROM MasterIncomeGrowthRate masterIncGr where :currDate between masterIncGr.fromDate and masterIncGr.toDate")
					.setParameter("currDate", currDate).uniqueResult();
			// session.clear();
			return incomeGrowthRate;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());
		}

	}

}
