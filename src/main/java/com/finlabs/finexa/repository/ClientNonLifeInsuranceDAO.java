package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientFloaterCover;
import com.finlabs.finexa.model.ClientNonLifeInsurance;

public interface ClientNonLifeInsuranceDAO {

	public List<ClientNonLifeInsurance> getClientAllNonLifeInsurance(int clientid,Session session) throws FinexaDaoException;
	
	public List<ClientFloaterCover> getClientFloaterCover(int nonlifeId, Session session) throws FinexaDaoException;

	public List<ClientNonLifeInsurance> getClientAllNonLifeInsuranceForMember(int memberid, Session session) throws FinexaDaoException;
}
