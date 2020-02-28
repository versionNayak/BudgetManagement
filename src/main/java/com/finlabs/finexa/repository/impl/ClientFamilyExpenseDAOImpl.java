package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterExpenseIndustryStandard;
import com.finlabs.finexa.repository.ClientFamilyExpenseDAO;

//@Repository
public class ClientFamilyExpenseDAOImpl implements ClientFamilyExpenseDAO {
	// @Autowired
	// GenericDao genericDao;

	@Override
	public ClientMaster getClientById(int clientid, Session session) throws FinexaDaoException {
		try {
			ClientMaster clientMaster = (ClientMaster) session.get(ClientMaster.class, new Integer(clientid));
			return clientMaster;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@Override
	public ClientFamilyMember getClientFromFamilyMemberById(int clientid, Session session) throws FinexaDaoException {
		try {
			ClientFamilyMember clientFamilyMember = null;

			Criteria criteria = session.createCriteria(ClientFamilyMember.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			criteria.add(Restrictions.eq("lookupRelation.id", (byte) 0));

			clientFamilyMember = (ClientFamilyMember) criteria.uniqueResult();
			return clientFamilyMember;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFamilyMember> getAllFamilyMembersOfClient(int clientid, Session session)
			throws FinexaDaoException {
		try {
			List<ClientFamilyMember> clientFamilyMemberList = null;

			Criteria criteria = session.createCriteria(ClientFamilyMember.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			criteria.addOrder(Order.asc("lookupRelation.id"));

			clientFamilyMemberList = (List<ClientFamilyMember>) criteria.list();
			return clientFamilyMemberList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientExpense> getExpenseListOfClient(int clientId, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		List<ClientExpense> expenseList = null;
		try {
			/*
			 * expenseList = genericDao.getSession().
			 * createSQLQuery("SELECT * FROM clientExpense ce,lookupExpenseCategory lec where "
			 * + "ce.expenseType=lec.id order by lec.displayOrder").list();
			 */
			Criteria criteria = session.createCriteria(ClientExpense.class);
			criteria.addOrder(Order.asc("expenseType"));
			expenseList = (List<ClientExpense>) criteria.add(Restrictions.eq("clientMaster.id", (int) clientId)).list();
			// session.clear();
			return expenseList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());
		}
	}

	@Override
	public List<MasterExpenseIndustryStandard> getExpenseIndustryStandard(Session session) throws FinexaDaoException {
		try {

			List<MasterExpenseIndustryStandard> expenseIndustryList = null;
			GenericDao genericDao = new GenericDao();
			expenseIndustryList = genericDao.loadAll(MasterExpenseIndustryStandard.class, session);
			// session.clear();
			return expenseIndustryList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}
}
