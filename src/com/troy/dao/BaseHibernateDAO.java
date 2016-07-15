package com.troy.dao;

import com.troy.util.HibernateSessionFactory;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;


/**
 * Data access object (DAO) for domain model
 * @author MyEclipse Persistence Tools
 */
public class BaseHibernateDAO implements IBaseHibernateDAO {
	
	public Session getSession() {
		HibernateSessionFactory.getSession().setCacheMode(CacheMode.REFRESH);
		HibernateSessionFactory.getSession().flush();
		return HibernateSessionFactory.getSession();
	}
	
}