package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientExpense;
import com.finlabs.finexa.model.ClientFamilyMember;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.MasterExpenseIndustryStandard;

public interface ClientFamilyExpenseDAO {

	public ClientMaster getClientById(int clientid, Session session) throws FinexaDaoException;

	public ClientFamilyMember getClientFromFamilyMemberById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientFamilyMember> getAllFamilyMembersOfClient(int clientid, Session session)
			throws FinexaDaoException;

	public List<ClientExpense> getExpenseListOfClient(int clientId, Session session) throws FinexaDaoException;

	public List<MasterExpenseIndustryStandard> getExpenseIndustryStandard(Session session) throws FinexaDaoException;
}
