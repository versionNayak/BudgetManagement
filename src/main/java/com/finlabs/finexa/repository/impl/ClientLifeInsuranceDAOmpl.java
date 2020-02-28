package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientLifeInsurance;
import com.finlabs.finexa.repository.ClientLifeInsuranceDAO;

//@Repository
public class ClientLifeInsuranceDAOmpl implements ClientLifeInsuranceDAO {
	//@Autowired
	//GenericDao genericDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientLifeInsurance> getClientAllLifeInsurance(int clientid, Session session) throws FinexaDaoException {
		List<ClientLifeInsurance> loanList = null;
		try {

			Criteria criteria = session.createCriteria(ClientLifeInsurance.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			loanList = (List<ClientLifeInsurance>) criteria.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		//session.clear();
		return loanList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ClientLifeInsurance> getFamilyMemberLifeInsurance(int memberId, Session session) throws FinexaDaoException {

		List<ClientLifeInsurance> loanList = null;
		try {

			Criteria criteria = session.createCriteria(ClientLifeInsurance.class);
			criteria.add(Restrictions.eq("clientFamilyMember.id", (int) memberId));
			loanList = (List<ClientLifeInsurance>) criteria.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return loanList;

	}

}
