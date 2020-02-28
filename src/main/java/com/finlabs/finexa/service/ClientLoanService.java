package com.finlabs.finexa.service;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientFamilyLoanOutput;
import com.finlabs.finexa.dto.ClientLoanProjectionOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.model.ClientLoan;

public interface ClientLoanService {
	public List<ClientFamilyLoanOutput> getCLientFamilyAllLoans(int clientId, int fpFlag, Session session)
			throws FinexaBussinessException;

	public List<ClientFamilyLoanOutput> getClientLoanStatusDetails(ClientLoan loan, String mode, int fpFlag,
			Session session) throws FinexaBussinessException;

	public List<ClientFamilyLoanOutput> getClientAllLoanStatusDetails(int clientId, String mode, int fpFlag,
			Session session) throws FinexaBussinessException;

	public List<ClientLoanProjectionOutput> getClientLoanProjectionList(int clientId, String mode, int fpFlag,
			Session session) throws FinexaBussinessException;

	public List<ClientLoanProjectionOutput> getMemberLoanProjectionListForFamilyMember(int memberId, String mode,
			int fpFlag, Session session) throws FinexaBussinessException;

}
