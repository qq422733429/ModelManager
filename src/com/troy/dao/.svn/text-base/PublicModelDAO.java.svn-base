package com.troy.dao;

import com.troy.entity.PublicModel;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * PublicModel entities. Transaction control of the save(), update() and
 * delete() operations can directly support Spring container-managed
 * transactions or they can be augmented to handle user-managed Spring
 * transactions. Each of these methods provides additional information for how
 * to configure it for the desired type of transaction control.
 * 
 * @see com.troy.entity.PublicModel
 * @author MyEclipse Persistence Tools
 */
public class PublicModelDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(PublicModelDAO.class);
	// property constants
	public static final String PRICE = "price";
	public static final String FREE = "free";

	public boolean save(PublicModel transientInstance) {
		log.debug("saving PublicModel instance");
		try {
			Transaction tran=getSession().beginTransaction();
			getSession().save(transientInstance);
			log.debug("save successful");

			tran.commit(); 
			return true;
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(PublicModel persistentInstance) {
		log.debug("deleting PublicModel instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public PublicModel findById(java.lang.String id) {
		log.debug("getting PublicModel instance with id: " + id);
		try {
			PublicModel instance = (PublicModel) getSession().get(
					"com.troy.entity.PublicModel", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(PublicModel instance) {
		log.debug("finding PublicModel instance by example");
		try {
			List results = getSession()
					.createCriteria("com.troy.entity.PublicModel")
					.add(Example.create(instance)).list();
			log.debug("find by example successful, result size: "
					+ results.size());
			return results;
		} catch (RuntimeException re) {
			log.error("find by example failed", re);
			throw re;
		}
	}

	public List findByProperty(String propertyName, Object value) {
		log.debug("finding PublicModel instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from PublicModel as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByPrice(Object price) {
		return findByProperty(PRICE, price);
	}

	public List findByFree(Object free) {
		return findByProperty(FREE, free);
	}

	public List findAll() {
		log.debug("finding all PublicModel instances");
		try {
			String queryString = "from PublicModel";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public PublicModel merge(PublicModel detachedInstance) {
		log.debug("merging PublicModel instance");
		try {
			PublicModel result = (PublicModel) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(PublicModel instance) {
		log.debug("attaching dirty PublicModel instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(PublicModel instance) {
		log.debug("attaching clean PublicModel instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}