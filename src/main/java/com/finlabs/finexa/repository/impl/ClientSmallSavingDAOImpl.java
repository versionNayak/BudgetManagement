package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
import com.finlabs.finexa.model.ClientFamilyIncome;
import com.finlabs.finexa.repository.ClientSmallSavingDAO;

//@Repository
public class ClientSmallSavingDAOImpl implements ClientSmallSavingDAO {
	// @Autowired
	// GenericDao genericDao;

	@Override
	public List<ClientFamilyIncome> getAllIncomeById(int clientid, Session session) throws FinexaDaoException {
		try {

			List<ClientFamilyIncome> incomeList = null;
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
