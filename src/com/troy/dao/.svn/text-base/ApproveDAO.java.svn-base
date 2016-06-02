package com.troy.dao;

import com.troy.entity.Approve;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for
 * Approve entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.troy.entity.Approve
 * @author MyEclipse Persistence Tools
 */
public class ApproveDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(ApproveDAO.class);
	// property constants
	public static final String SPONSOR_ID = "sponsorId";
	public static final String STATUS = "status";
	public static final String APPROVER_ID = "approverId";
	public static final String NOT_APPROVED_REASON = "notApprovedReason";

	public boolean save(Approve transientInstance) {
		log.debug("saving Approve instance");
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

	public void delete(Approve persistentInstance) {
		log.debug("deleting Approve instance");
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

	public Approve findById(java.lang.Integer id) {
		log.debug("getting Approve instance with id: " + id);
		try {
			Approve instance = (Approve) getSession().get(
					"com.troy.entity.Approve", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Approve instance) {
		log.debug("finding Approve instance by example");
		try {
			List results = getSession()
					.createCriteria("com.troy.entity.Approve")
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
		log.debug("finding Approve instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Approve as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findBySponsorId(Object sponsorId) {
		return findByProperty(SPONSOR_ID, sponsorId);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByApproverId(Object approverId) {
		return findByProperty(APPROVER_ID, approverId);
	}

	public List findByNotApprovedReason(Object notApprovedReason) {
		return findByProperty(NOT_APPROVED_REASON, notApprovedReason);
	}

	public List findAll() {
		log.debug("finding all Approve instances");
		try {
			String queryString = "from Approve";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Approve merge(Approve detachedInstance) {
		log.debug("merging Approve instance");
		try {
			Approve result = (Approve) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Approve instance) {
		log.debug("attaching dirty Approve instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Approve instance) {
		log.debug("attaching clean Approve instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}