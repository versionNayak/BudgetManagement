package com.finlabs.finexa.repository;

import java.util.List;

import org.hibernate.Session;

import com.finlabs.finexa.exception.FinexaDaoException;

public interface ClientFamilyNetSurplusDAO {

	public List<Object[]> getAllNetSurplusById(int clientid, Session session) throws FinexaDaoException;
}
