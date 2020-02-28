package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.repository.ClientFamilyIncomeDAO;

//@Repository
public class ClientFamilyIncomeDAOImpl implements ClientFamilyIncomeDAO {
	//@Autowired
	//GenericDao genericDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFamilyIncome> getAllIncomeById(int clientid, Session session) throws FinexaDaoException {
		try {
			List<ClientFamilyIncome> incomeList = null;
			Criteria criteria = session.createCriteria(ClientFamilyIncome.class);
			incomeList = (List<ClientFamilyIncome>) criteria.add(Restrictions.eq("clientMaster.id", (int) clientid))
					.createCriteria("clientFamilyMember").list();
			//session.clear();
			return incomeList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFamilyIncome> getFamilyMemberIncome(int memberId, Session session) throws FinexaDaoException {

		try {

			List<ClientFamilyIncome> incomeList = null;

			Criteria criteria = session.createCriteria(ClientFamilyIncome.class);
			criteria.addOrder(Order.asc("incomeType"));
			incomeList = (List<ClientFamilyIncome>) criteria
					.add(Restrictions.eq("clientFamilyMember.id", (int) memberId)).list();
			// session.clear();
			return incomeList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

}
