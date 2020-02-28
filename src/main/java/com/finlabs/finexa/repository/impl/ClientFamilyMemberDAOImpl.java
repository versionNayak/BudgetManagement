package com.finlabs.finexa.repository.impl;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.repository.ClientFamilyMemberDAO;

//@Repository
public class ClientFamilyMemberDAOImpl implements ClientFamilyMemberDAO {
	// @Autowired
	// GenericDao genericDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFamilyMember> getAllClientFamilyMemberByClientId(int clientId, Session session)
			throws FinexaDaoException {

		try {

			List<ClientFamilyMember> clientMasterList = null;
			clientMasterList = (List<ClientFamilyMember>) session.createQuery(
					"select  cfm from ClientFamilyMember cfm join cfm.clientMaster cm   where cm.id = :clientID")
					.setInteger("clientID", clientId).list();
			// session.clear();
			return clientMasterList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

}
