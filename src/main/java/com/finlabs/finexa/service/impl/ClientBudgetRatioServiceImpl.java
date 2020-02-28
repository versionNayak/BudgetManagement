package com.finlabs.finexa.service.impl;

import java.text.DecimalFormat;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.AnnualExpensesDetailed;
import com.finlabs.finexa.dto.ClientBudgetRatioOutput;
import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;
import com.finlabs.finexa.dto.ClientFamilyNetSurplusOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;
import com.finlabs.finexa.genericDao.CacheInfoService;
import com.finlabs.finexa.model.ClientCash;
import com.finlabs.finexa.model.MasterWealthRatioComment;
import com.finlabs.finexa.repository.ClientMasterDAO;
import com.finlabs.finexa.repository.impl.ClientMasterDAOImpl;
import com.finlabs.finexa.service.ClientBudgetRatioService;
import com.finlabs.finexa.service.ClientExpenseService;
import com.finlabs.finexa.service.ClientIncomeService;
import com.finlabs.finexa.service.ClientNetSurplusService;

//@Transactional
//@Service
public class ClientBudgetRatioServiceImpl implements ClientBudgetRatioService {

	// @Autowired
	// private ClientIncomeService clientIncomeSerive;

	// @Autowired
	// private ClientNetSurplusService clientNetSurplusService;

	// @Autowired
	// private ClientExpenseService clientExpenseSerive;

	// @Autowired
	// private ClientMasterDAO clientMasterDao;

	@Override
	public ClientBudgetRatioOutput getClientBudgetRatioInfo(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException {
		ClientBudgetRatioOutput clientBudgetRatioOutput = new ClientBudgetRatioOutput();
		ClientIncomeService clientIncomeSeriveImpl = new ClientIncomeServiceImpl();
		ClientNetSurplusService clientNetSurplusServiceImpl = new ClientNetSurplusServiceImpl();
		ClientExpenseService clientExpenseSeriveImpl = new ClientExpenseServiceImpl();
		ClientMasterDAO clientMasterDaoImpl = new ClientMasterDAOImpl();
		try {
			ClientFamilyIncomeOutput clientFamilyIncomeOutput = null;
			List<ClientFamilyNetSurplusOutput> clientFamilyNetSurplusOutputList = null;
			try {
				clientFamilyIncomeOutput = clientIncomeSeriveImpl.getCLientFamilyAllIncomes(clientId, "yearly", 0,
						session);
			} catch (Exception e) {
			}

			try {
				clientFamilyNetSurplusOutputList = clientNetSurplusServiceImpl.getClientNetSurplusInfo(token, clientId,
						"yearly", 0, session, cacheInfoService);
			} catch (Exception e) {
			}

			double currentYearIncome = 0.0;
			if (clientFamilyIncomeOutput != null) {
				currentYearIncome = clientFamilyIncomeOutput.getTotalIncome();
			}
			double currentYearNetSurplus = 0.0;
			double currentYearEMI = 0.0;
			if (clientFamilyNetSurplusOutputList != null && clientFamilyNetSurplusOutputList.size() > 0) {
				currentYearNetSurplus = clientFamilyNetSurplusOutputList.get(0).getNet_surplus();
				currentYearEMI = clientFamilyNetSurplusOutputList.get(0).getLoan_outflows();
			}
			AnnualExpensesDetailed monthlyExpense = null;
			double housingExpense = 0.0;
			try {
				monthlyExpense = clientExpenseSeriveImpl.getAnnualExpensesDetailed(clientId, "yearly", 0, session);

			} catch (Exception e) {
			}

			if (monthlyExpense != null) {
				housingExpense = monthlyExpense.getTotalExpense() / 12;
			}
			double savingsRatioPerc = 0.0;
			double debtServicingRatio = 0.0;
			double housingExpenseRatio = 0.0;
			if (currentYearIncome > 0) {
				savingsRatioPerc = round(((currentYearNetSurplus / currentYearIncome)), 2);
				debtServicingRatio = round(((currentYearEMI / currentYearIncome)), 2);
				housingExpenseRatio = round(((housingExpense / currentYearIncome)), 2);
			}
			double basicLiquidityRatio = 0.0;
			// code addition for financial planning
			if (housingExpenseRatio > 0) {
				double cash = 0.0;
				List<ClientCash> clientCashList = clientMasterDaoImpl.getClientCashListbyId(clientId, session);
				if (clientCashList != null && clientCashList.size() > 0) {
					for (ClientCash obj : clientCashList) {
						cash = cash + obj.getCurrentBalance().doubleValue();
					}
				}
				basicLiquidityRatio = round(((cash / housingExpense) * 100), 2);
			}

			clientBudgetRatioOutput.setSavingsRatioPerc(savingsRatioPerc * 100);
			clientBudgetRatioOutput.setDebtServicingRatioPerc(debtServicingRatio * 100);
			clientBudgetRatioOutput.setHousingExpenseRatioPerc(housingExpenseRatio * 100);
			clientBudgetRatioOutput.setBasicLiquidityRatio(basicLiquidityRatio);
			// generating comment from master table
			List<MasterWealthRatioComment> masterWealthRatioCommentList = null;
			try {
				masterWealthRatioCommentList = clientMasterDaoImpl.getMasterWealthRatioCommentList(session);
			} catch (FinexaDaoException e) {
			}

			String savingsRatioPercComment = "";
			String debtServicingRatioComment = "";
			String housingExpenseRatioComment = "";
			//String basicLiquidityRatioComment = "";
			String savingsRatioLogicRationale = "";
			String debtServicingRatioLogicRationale = "";
			String housingExpenseRatioLogicRationale = "";
			//String basicLiquidityRatioLogicRationale = "";

			if (masterWealthRatioCommentList != null) {
				for (MasterWealthRatioComment obj : masterWealthRatioCommentList) {
					if (obj.getFromRange() != null && obj.getToRange() != null) {
						// for savings ratio
						if (obj.getMasterWealthRatio().getId() == 1) {
							if (savingsRatioPerc >= obj.getFromRange().doubleValue()
									&& savingsRatioPerc <= obj.getToRange().doubleValue()) {
								savingsRatioPercComment = obj.getCommentMaster();
								savingsRatioLogicRationale = obj.getMasterWealthRatioLogicRationale().getLogicRationale();
							}
						}
						// for debt servicing ratio
						if (obj.getMasterWealthRatio().getId() == 6) {
							if (debtServicingRatio >= obj.getFromRange().doubleValue()
									&& debtServicingRatio <= obj.getToRange().doubleValue()) {
								debtServicingRatioComment = obj.getCommentMaster();
								debtServicingRatioLogicRationale = obj.getMasterWealthRatioLogicRationale().getLogicRationale();
							}
						}
						// for housing expense ratio
						if (obj.getMasterWealthRatio().getId() == 9) {
							if (housingExpenseRatio >= obj.getFromRange().doubleValue()
									&& housingExpenseRatio <= obj.getToRange().doubleValue()) {
								housingExpenseRatioComment = obj.getCommentMaster();
								housingExpenseRatioLogicRationale = obj.getMasterWealthRatioLogicRationale().getLogicRationale();
							}
						}
						
						/*// for Basic Liquidity Ratio
						if (obj.getMasterWealthRatio().getId() == 8) {
							if(obj.getToRange() != null) {
								if(basicLiquidityRatio >= obj.getFromRange().doubleValue() && basicLiquidityRatio <= obj.getToRange().doubleValue()) {
									basicLiquidityRatioComment = obj.getCommentMaster();
									basicLiquidityRatioLogicRationale = obj.getMasterWealthRatioLogicRationale().getLogicRationale();
								}
							} else {
								basicLiquidityRatioComment = "deploy excess liquid assets in ideal allocation";
								basicLiquidityRatioLogicRationale = "Cash/ monthly expense";
							}
							
						}*/
					}

				}
			}
			clientBudgetRatioOutput.setSavingsRatioPercComment(savingsRatioPercComment);
			clientBudgetRatioOutput.setDebtServicingRatioPercComment(debtServicingRatioComment);
			clientBudgetRatioOutput.setHousingExpenseRatioPercComment(housingExpenseRatioComment);
			//clientBudgetRatioOutput.setBasicLiquidityRatioPercComment(basicLiquidityRatioComment);
			clientBudgetRatioOutput.setSavingsRatioLogicRationale(savingsRatioLogicRationale);
			clientBudgetRatioOutput.setDebtServicingLogicRationale(debtServicingRatioLogicRationale);
			clientBudgetRatioOutput.setHousingExpenseRatioLogicRationale(housingExpenseRatioLogicRationale);
			//clientBudgetRatioOutput.setBasicLiquidityRatioLogicRationale(basicLiquidityRatioLogicRationale);

			return clientBudgetRatioOutput;
		} catch (Exception e) {
		}

		return clientBudgetRatioOutput;

	}

	public double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		DecimalFormat df = new DecimalFormat("#.00");
		df.setMaximumFractionDigits(2);
		df.setMinimumFractionDigits(2);
		String valueString = df.format(value);
		value = Double.parseDouble(valueString);
		return value;
	}
}
