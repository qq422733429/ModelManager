package com.troy.dao;

import com.troy.entity.Execute;

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
 * Execute entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.troy.entity.Execute
 * @author MyEclipse Persistence Tools
 */
public class ExecuteDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(ExecuteDAO.class);
	// property constants
	public static final String CREATE_USER_ID = "createUserId";
	public static final String MODEL_ID = "modelId";
	public static final String STATUS = "status";
	public static final String OUTPUT = "output";
	public static final String ERRORMESSAGE = "errormessage";
	public static final String ENABLE = "enable";

	public boolean save(Execute transientInstance) {
		log.debug("saving Execute instance");
		Transaction tran=getSession().beginTransaction();
		try {
			getSession().save(transientInstance);
			log.debug("save successful");
			tran.commit(); 
			return true;
		} catch (RuntimeException re) {
			log.error("save failed", re);
			throw re;
		}
	}

	public void delete(Execute persistentInstance) {
		log.debug("deleting Execute instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Execute findById(java.lang.String id) {
		log.debug("getting Execute instance with id: " + id);
		try {
			Execute instance = (Execute) getSession().get(
					"com.troy.entity.Execute", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Execute instance) {
		log.debug("finding Execute instance by example");
		try {
			List results = getSession()
					.createCriteria("com.troy.entity.Execute")
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
		log.debug("finding Execute instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Execute as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByCreateUserId(Object createUserId) {
		return findByProperty(CREATE_USER_ID, createUserId);
	}

	public List findByModelId(Object modelId) {
		return findByProperty(MODEL_ID, modelId);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByOutput(Object output) {
		return findByProperty(OUTPUT, output);
	}

	public List findByErrormessage(Object errormessage) {
		return findByProperty(ERRORMESSAGE, errormessage);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findAll() {
		log.debug("finding all Execute instances");
		try {
			String queryString = "from Execute";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Execute merge(Execute detachedInstance) {
		log.debug("merging Execute instance");
		try {
			Execute result = (Execute) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Execute instance) {
		log.debug("attaching dirty Execute instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Execute instance) {
		log.debug("attaching clean Execute instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}