package com.finlabs.finexa.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientFamilyIncomeOutput;
import com.finlabs.finexa.dto.FamilyMemberIncomeDetailOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientFamilyIncome;

public interface ClientIncomeService {

	public ClientFamilyIncomeOutput getCLientFamilyAllIncomes(int clientId, String mode, int financialPlanningFlag,
			Session session) throws FinexaBussinessException;

	public Map<Date, Double> getInterestRateFromProductCal(int clientId, Session session)
			throws FinexaBussinessException;

	public List<FamilyMemberIncomeDetailOutput> getIncomeOfFamilyMember(int memberId, Session session)
			throws FinexaBussinessException;

}
