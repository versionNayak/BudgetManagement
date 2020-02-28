package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.model.ClientAnnuity;
import com.finlabs.finexa.model.ClientAtalPensionYojana;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.ClientEPF;
import com.finlabs.finexa.model.ClientFixedIncome;
import com.finlabs.finexa.model.ClientLumpsumInflow;
import com.finlabs.finexa.model.ClientMaster;
import com.finlabs.finexa.model.ClientPPF;
import com.finlabs.finexa.model.ClientSmallSaving;
import com.finlabs.finexa.model.MasterWealthRatioComment;

public interface ClientMasterDAO {
	public List<ClientMaster> getAllClientsById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientFixedIncome> getClientIncomeListById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientSmallSaving> getClientSmallSavingsListById(int clientid, Session session)
			throws FinexaDaoException;

	public List<ClientAnnuity> getClientAnnuityListById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientAtalPensionYojana> getClientATPListById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientEPF> getClientEpfListById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientPPF> getClientPPFList(int clientid, Session session) throws FinexaDaoException;

	public List<ClientAtalPensionYojana> getClientFamilyATPListById(int clientid, Session session)
			throws FinexaDaoException;

	public List<MasterWealthRatioComment> getMasterWealthRatioCommentList(Session session) throws FinexaDaoException;

	public List<ClientLumpsumInflow> getClientLumpsumListById(int clientid, Session session) throws FinexaDaoException;

	public List<ClientCash> getClientCashListbyId(int clientId, Session session) throws FinexaDaoException;

	public Double getCashLiquidReturn(Session session) throws FinexaDaoException;
}