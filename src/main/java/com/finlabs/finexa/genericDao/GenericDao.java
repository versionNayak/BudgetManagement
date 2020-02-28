package com.finlabs.finexa.genericDao;

import java.util.List;

import org.hibernate.Session;

public class GenericDao {

	public <T> T save(T entity, Session session) throws RuntimeException {
		session.persist(entity);
		return entity;
	}

	public <T> T saveOrUpdate(final T entity, Session session) throws RuntimeException {
		session.saveOrUpdate(entity);
		return entity;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObjectById(final Class<T> type, Object key, Session session) throws RuntimeException {
		return (T) session.get(type, (String.valueOf(key)));
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getObjectListById(final Class<T> type, Object key, Session session) throws RuntimeException {
		return session.createCriteria(type, (String.valueOf(key))).list();
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> loadAll(final Class<T> entity, Session session) throws RuntimeException {
		return session.createCriteria(entity).list();
	}

	public void delete(Object obj, Session session) throws RuntimeException {
		session.delete(obj);
	}

}
