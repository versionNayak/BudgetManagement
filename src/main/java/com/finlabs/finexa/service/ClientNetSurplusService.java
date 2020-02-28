package com.finlabs.finexa.service;

import java.util.Hashtable;
import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.dto.ClientFamilyNetSurplusOutput;
import com.finlabs.finexa.exception.FinexaBussinessException;
import com.finlabs.finexa.genericDao.CacheInfoService;

public interface ClientNetSurplusService {

	public List<ClientFamilyNetSurplusOutput> getClientNetSurplusInfo(String token, int clientId, String mode, int fpFlag,
			Session session,CacheInfoService cacheInfoService) throws FinexaBussinessException;

	public Hashtable<Integer, Double> getNetSurplusYearWiseMap(String token, int clientId, Session session,CacheInfoService cacheInfoService)
			throws FinexaBussinessException;

}
