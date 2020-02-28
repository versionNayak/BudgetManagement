package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientFloaterCover;
import com.finlabs.finexa.model.ClientNonLifeInsurance;
import com.finlabs.finexa.repository.ClientNonLifeInsuranceDAO;

//@Repository
public class ClientNonLifeInsuranceDAOmpl implements ClientNonLifeInsuranceDAO {

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientNonLifeInsurance> getClientAllNonLifeInsurance(int clientid, Session session)
			throws FinexaDaoException {
		List<ClientNonLifeInsurance> nonLifeInsuranceList = null;
		try {

			Criteria criteria = session.createCriteria(ClientNonLifeInsurance.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			nonLifeInsuranceList = (List<ClientNonLifeInsurance>) criteria.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return nonLifeInsuranceList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientNonLifeInsurance> getClientAllNonLifeInsuranceForMember(int memberID, Session session)
			throws FinexaDaoException {
		List<ClientNonLifeInsurance> nonLifeInsuranceList = null;
		try {

			Criteria criteria = session.createCriteria(ClientNonLifeInsurance.class);
			criteria.add(Restrictions.eq("clientFamilyMember.id", (int) memberID));
			nonLifeInsuranceList = (List<ClientNonLifeInsurance>) criteria.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return nonLifeInsuranceList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFloaterCover> getClientFloaterCover(int nonlifeId, Session session) throws FinexaDaoException {
		List<ClientFloaterCover> clientFloaterCover = null;
		try {

			Criteria criteria = session.createCriteria(ClientFloaterCover.class);
			criteria.add(Restrictions.eq("clientNonLifeInsurance.id", (int) nonlifeId));
			clientFloaterCover = (List<ClientFloaterCover>) criteria.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e);

		}
		// session.clear();
		return clientFloaterCover;
	}
}
