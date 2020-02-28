package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientLifeInsurance;

public interface ClientLifeInsuranceDAO {

	public List<ClientLifeInsurance> getClientAllLifeInsurance(int clientid, Session session) throws FinexaDaoException;

	public List<ClientLifeInsurance> getFamilyMemberLifeInsurance(int memberId, Session session)
			throws FinexaDaoException;
}
