package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientLoan;

public interface ClientFamilyLoanDAO {

	public List<Object[]> getAllLoanById(int clientid, Session session) throws FinexaDaoException;

	public ClientLoan getLoanClientDetailsById(int clientId, String providerId, int categoryId, Session session)
			throws FinexaDaoException;

	public List<ClientLoan> getLoanClientAllDetailsById(int clientId, Session session) throws FinexaDaoException;

	public List<ClientLoan> getLoanClientAllDetailsByMemberId(int memberId, Session session) throws FinexaDaoException;
}
