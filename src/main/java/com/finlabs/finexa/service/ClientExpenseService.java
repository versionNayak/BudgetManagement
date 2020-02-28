package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.finlabs.finexa.dto.AnnualExpensesDetailed;
import com.finlabs.finexa.dto.ClientDetailedOutput;
import com.finlabs.finexa.dto.CommittedOutFlowOutput;
import com.finlabs.finexa.dto.FamilyMemberIncomeDetailOutput;
import com.finlabs.finexa.dto.MasterExpenseIndustryStandardDTO;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.exception.FinexaDaoException;

public interface ClientExpenseService {

	public AnnualExpensesDetailed getAnnualExpensesDetailed(int clientId, String mode, int fpFlag, Session session)
			throws FinexaBussinessException, FinexaDaoException;

	public CommittedOutFlowOutput getClientCommitedOutFlows(int clientId, String mode, int fpFlag, Session session)
			throws FinexaBussinessException;

	public Map<Date, Double> getDepositAmountFromProductCal(int clientId, Session session)
			throws FinexaBussinessException;

	public ClientDetailedOutput getClientDetails(int clientId, Session session) throws FinexaBussinessException;

	public List<FamilyMemberIncomeDetailOutput> getExpenseOfFamilyMember(int memberId, Session session)
			throws FinexaBussinessException;

	public List<MasterExpenseIndustryStandardDTO> getExpenseIndustryStandard(Session session)
			throws FinexaBussinessException;
}
