package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.repository.ClientFixedIncomeDAO;

@Repository
public class ClientFixedIncomeDAOImpl implements ClientFixedIncomeDAO {

	// @Autowired
	// GenericDao genericDao;

	@Override
	public List<ClientFamilyIncome> getAllIncomeById(int clientid, Session session) throws FinexaDaoException {
		try {

			List<ClientFamilyIncome> incomeList = null;
			// sample query
			// ClientFamilyIncome in = (ClientFamilyIncome)
			// genericDao.getSession()
			// .createQuery("from ClientFamilyIncome inc where inc.clientId =
			// :clientId").setInteger("clientId", 1)
			// .uniqueResult();
			// ClientFamilyIncome in =
			// (ClientFamilyIncome)
			// genericDao.getObjectById(ClientFamilyIncome.class, 1);
			// incomeList = (List<ClientFamilyIncome>) genericDao.getSession()
			// .createQuery("SELECT inc FROM ClientFamilyIncome inc");
			GenericDao genericDaoObj = new GenericDao();
			incomeList = (List<ClientFamilyIncome>) genericDaoObj.getObjectListById(ClientFamilyIncome.class, clientid,
					session);
			// session.clear();
			return incomeList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
	}

}
