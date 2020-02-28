package com.finlabs.finexa.repository.impl;

import java.math.BigDecimal;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.GenericDao;
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
import com.finlabs.finexa.repository.ClientMasterDAO;

//@Repository
public class ClientMasterDAOImpl implements ClientMasterDAO {
	// @Autowired
	// GenericDao genericDao;

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientMaster> getAllClientsById(int clientid, Session session) throws FinexaDaoException {
		try {
			List<ClientMaster> clientMasterList = null;
			clientMasterList = (List<ClientMaster>) session
					.createQuery("SELECT cm FROM ClientMaster cm where cm.id=:clientId")
					.setInteger("clientId", clientid).list();
			// session.clear();
			return clientMasterList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientFixedIncome> getClientIncomeListById(int clientId, Session session) throws FinexaDaoException {

		try {

			List<ClientFixedIncome> masterincomeList = null;
			masterincomeList = (List<ClientFixedIncome>) session
					.createQuery("SELECT cm FROM ClientFixedIncome cm where cm.clientMaster.id=:clientId")
					.setInteger("clientId", clientId).list();
			// session.clear();
			return masterincomeList;
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientSmallSaving> getClientSmallSavingsListById(int clientId, Session session)
			throws FinexaDaoException {

		try {

			List<ClientSmallSaving> mastersmallSavingList = null;

			Criteria criteria = session.createCriteria(ClientSmallSaving.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientId));

			mastersmallSavingList = (List<ClientSmallSaving>) criteria.list();
			// session.clear();
			return mastersmallSavingList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAnnuity> getClientAnnuityListById(int clientId, Session session) throws FinexaDaoException {

		try {

			List<ClientAnnuity> clientAnnuityList = null;
			Criteria criteria = session.createCriteria(ClientAnnuity.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientId));
			clientAnnuityList = (List<ClientAnnuity>) criteria.list();
			// session.clear();
			return clientAnnuityList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientPPF> getClientPPFList(int clientId, Session session) throws FinexaDaoException {

		try {

			List<ClientPPF> clientPPFList = null;
			Criteria criteria = session.createCriteria(ClientPPF.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientId));
			clientPPFList = (List<ClientPPF>) criteria.list();
			// session.clear();
			return clientPPFList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAtalPensionYojana> getClientATPListById(int clientid, Session session) throws FinexaDaoException {

		try {

			List<ClientAtalPensionYojana> clientATPList = null;
			Criteria criteria = session.createCriteria(ClientAtalPensionYojana.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			clientATPList = (List<ClientAtalPensionYojana>) criteria.list();
			// session.clear();
			return clientATPList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientAtalPensionYojana> getClientFamilyATPListById(int clientid, Session session)
			throws FinexaDaoException {

		try {

			List<ClientAtalPensionYojana> clientATPList = null;
			Criteria criteria = session.createCriteria(ClientAtalPensionYojana.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			clientATPList = (List<ClientAtalPensionYojana>) criteria.list();
			// session.clear();
			return clientATPList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientEPF> getClientEpfListById(int clientid, Session session) throws FinexaDaoException {

		try {

			List<ClientEPF> clientEpfList = null;
			Criteria criteria = session.createCriteria(ClientEPF.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			clientEpfList = (List<ClientEPF>) criteria.list();
			// session.clear();
			return clientEpfList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@Override
	public List<MasterWealthRatioComment> getMasterWealthRatioCommentList(Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		List<MasterWealthRatioComment> masterWealthRatioCommentList = null;

		try {
			GenericDao genericDaoObj = new GenericDao();
			masterWealthRatioCommentList = genericDaoObj.loadAll(MasterWealthRatioComment.class, session);
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}
		// session.clear();
		return masterWealthRatioCommentList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientLumpsumInflow> getClientLumpsumListById(int clientid, Session session) throws FinexaDaoException {

		try {

			List<ClientLumpsumInflow> clientLumpsumInflowList = null;
			Criteria criteria = session.createCriteria(ClientLumpsumInflow.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientid));
			clientLumpsumInflowList = (List<ClientLumpsumInflow>) criteria.list();
			// session.clear();
			return clientLumpsumInflowList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ClientCash> getClientCashListbyId(int clientId, Session session) throws FinexaDaoException {
		// TODO Auto-generated method stub
		try {
			List<ClientCash> clientCashList = null;
			Criteria criteria = session.createCriteria(ClientCash.class);
			criteria.add(Restrictions.eq("clientMaster.id", (int) clientId));
			clientCashList = (List<ClientCash>) criteria.list();
			// session.clear();
			return clientCashList;

		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

	// @SuppressWarnings("unchecked")
	@Override
	public Double getCashLiquidReturn(Session session) throws FinexaDaoException {

		try {

			// 1 for Cash Liquid
			BigDecimal cashReturnBigDecimal = (BigDecimal) session
					.createQuery("SELECT cm.mlr FROM MasterSubAssetClassReturn cm where cm.id=1").uniqueResult();
			// session.clear();
			return (cashReturnBigDecimal != null ? cashReturnBigDecimal.doubleValue() : 0d);
		} catch (Exception e) {
			throw new FinexaDaoException(e.getMessage());

		}

	}

}