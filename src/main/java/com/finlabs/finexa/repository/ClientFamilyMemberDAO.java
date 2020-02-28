package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientFamilyMember;

public interface ClientFamilyMemberDAO {
	public List<ClientFamilyMember> getAllClientFamilyMemberByClientId(int clientid, Session session)
			throws FinexaDaoException;
}
