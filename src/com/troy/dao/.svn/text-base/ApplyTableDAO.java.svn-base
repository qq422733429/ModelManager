package com.troy.dao;

import com.troy.entity.ApplyTable;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * ApplyTable entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.troy.entity.ApplyTable
 * @author MyEclipse Persistence Tools
 */
public class ApplyTableDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(ApplyTableDAO.class);
	// property constants
	public static final String APPLY_USER = "applyUser";
	public static final String OWNER_USER = "ownerUser";
	public static final String MODEL_ID = "modelId";
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String ROLE = "role";

	public boolean save(ApplyTable transientInstance) {
		log.debug("saving ApplyTable instance");
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

	public void delete(ApplyTable persistentInstance) {
		log.debug("deleting ApplyTable instance");
		try {
			Transaction tran=getSession().beginTransaction();
			getSession().delete(persistentInstance);
			log.debug("delete successful");
			tran.commit(); 
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public ApplyTable findById(java.lang.Integer id) {
		log.debug("getting ApplyTable instance with id: " + id);
		try {
			ApplyTable instance = (ApplyTable) getSession().get(
					"com.troy.entity.ApplyTable", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(ApplyTable instance) {
		log.debug("finding ApplyTable instance by example");
		try {
			List results = getSession()
					.createCriteria("com.troy.entity.ApplyTable")
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
		log.debug("finding ApplyTable instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from ApplyTable as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByApplyUser(Object applyUser) {
		return findByProperty(APPLY_USER, applyUser);
	}

	public List findByOwnerUser(Object ownerUser) {
		return findByProperty(OWNER_USER, ownerUser);
	}

	public List findByModelId(Object modelId) {
		return findByProperty(MODEL_ID, modelId);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByMessage(Object message) {
		return findByProperty(MESSAGE, message);
	}

	public List findByRole(Object role) {
		return findByProperty(ROLE, role);
	}

	public List findAll() {
		log.debug("finding all ApplyTable instances");
		try {
			String queryString = "from ApplyTable";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public ApplyTable merge(ApplyTable detachedInstance) {
		log.debug("merging ApplyTable instance");
		try {
			ApplyTable result = (ApplyTable) getSession().merge(
					detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(ApplyTable instance) {
		log.debug("attaching dirty ApplyTable instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(ApplyTable instance) {
		log.debug("attaching clean ApplyTable instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}