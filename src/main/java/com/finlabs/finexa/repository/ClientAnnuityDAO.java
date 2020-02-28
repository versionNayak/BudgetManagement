package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientFamilyIncome;

public interface ClientAnnuityDAO {

	public List<ClientFamilyIncome> getAllIncomeById(int clientid, Session session) throws FinexaDaoException;
}
