package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientLoan;
import com.finlabs.finexa.repository.ClientFamilyLoanDAO;

@Repository
public class ClientFamilyLoanDAOImpl implements ClientFamilyLoanDAO {
	// @Autowired
	// GenericDao genericDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllLoanById(int clientid, Session session) throws FinexaDaoException {
		try {
			List<Object[]> incomeList = null;
			incomeList = session
					.createQuery("SELECT cl,lc,lp FROM ClientLoan cl , LookupLoanCategory lc ,MasterLoanProvider lp "
							+ "WHERE cl.clientMaster.id=" + clientid
							+ "and lc.id=cl.lookupLoanCategory.id AND lp.id=cl.masterLoanProvider.id")
					.list();
			// session.clear();
			return incomeList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@Override
	public ClientLoan getLoanClientDetailsById(int clientId, String providerId, int categoryId, Session session)
			throws FinexaDaoException {

		ClientLoan loan = null;
		try {
			Criteria criteria = session.createCriteria(ClientLoan.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientId));
			criteria.add(Restrictions.eq("masterLoanProvider.id", Integer.parseInt(providerId)));
			criteria.add(Restrictions.eq("lookupLoanCategory.id", (byte) categoryId));
			loan = (ClientLoan) criteria.uniqueResult();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return loan;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientLoan> getLoanClientAllDetailsById(int clientId, Session session) throws FinexaDaoException {

		List<ClientLoan> loanList = null;
		try {
			Criteria criteria = session.createCriteria(ClientLoan.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientId));
			loanList = (List<ClientLoan>) criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return loanList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientLoan> getLoanClientAllDetailsByMemberId(int memberId, Session session) throws FinexaDaoException {

		List<ClientLoan> loanList = null;
		try {
			Criteria criteria = session.createCriteria(ClientLoan.class);
			criteria.add(Restrictions.eq("clientFamilyMember.id", (int) memberId));
			loanList = (List<ClientLoan>) criteria.list();
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return loanList;
	}

}
