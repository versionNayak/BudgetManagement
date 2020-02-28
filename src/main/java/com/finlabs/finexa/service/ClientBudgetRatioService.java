package com.finlabs.finexa.service;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientBudgetRatioOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;

public interface ClientBudgetRatioService {

	public ClientBudgetRatioOutput getClientBudgetRatioInfo(String token, int clientId, Session session,
			CacheInfoService cacheInfoService) throws FinexaBussinessException;

}
