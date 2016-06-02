package com.troy.dao;

import com.troy.entity.BaseModel;

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
 * BaseModel entities. Transaction control of the save(), update() and delete()
 * operations can directly support Spring container-managed transactions or they
 * can be augmented to handle user-managed Spring transactions. Each of these
 * methods provides additional information for how to configure it for the
 * desired type of transaction control.
 * 
 * @see com.troy.entity.BaseModel
 * @author MyEclipse Persistence Tools
 */
public class BaseModelDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory
			.getLogger(BaseModelDAO.class);
	// property constants
	public static final String EXECUTE_TYPE = "executeType";
	public static final String MODEL_NAME = "modelName";
	public static final String MODEL_DESCRIPTION = "modelDescription";
	public static final String HDFS_LOCATION = "hdfsLocation";
	public static final String STATUS = "status";
	public static final String VERSION = "version";
	public static final String PROJECT_ID = "projectId";
	public static final String ICONPATH = "iconpath";
	public static final String FUNCTION = "function";
	public static final String BASE_TYPE = "baseType";

	public Boolean save(BaseModel transientInstance) {
		log.debug("saving BaseModel instance");
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

	public void delete(BaseModel persistentInstance) {
		log.debug("deleting BaseModel instance");
		try {
			getSession().delete(persistentInstance);
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public BaseModel findById(java.lang.String id) {
		log.debug("getting BaseModel instance with id: " + id);
		try {
			BaseModel instance = (BaseModel) getSession().get(
					"com.troy.entity.BaseModel", id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(BaseModel instance) {
		log.debug("finding BaseModel instance by example");
		try {
			List results = getSession()
					.createCriteria("com.troy.entity.BaseModel")
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
		log.debug("finding BaseModel instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from BaseModel as model where model."
					+ propertyName + "= ?";
			Query queryObject = getSession().createQuery(queryString);
			queryObject.setParameter(0, value);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find by property name failed", re);
			throw re;
		}
	}

	public List findByExecuteType(Object executeType) {
		return findByProperty(EXECUTE_TYPE, executeType);
	}

	public List findByModelName(Object modelName) {
		return findByProperty(MODEL_NAME, modelName);
	}

	public List findByModelDescription(Object modelDescription) {
		return findByProperty(MODEL_DESCRIPTION, modelDescription);
	}

	public List findByHdfsLocation(Object hdfsLocation) {
		return findByProperty(HDFS_LOCATION, hdfsLocation);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findByProjectId(Object projectId) {
		return findByProperty(PROJECT_ID, projectId);
	}

	public List findByIconpath(Object iconpath) {
		return findByProperty(ICONPATH, iconpath);
	}

	public List findByFunction(Object function) {
		return findByProperty(FUNCTION, function);
	}

	public List findByBaseType(Object baseType) {
		return findByProperty(BASE_TYPE, baseType);
	}

	public List findAll() {
		log.debug("finding all BaseModel instances");
		try {
			String queryString = "from BaseModel";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public BaseModel merge(BaseModel detachedInstance) {
		log.debug("merging BaseModel instance");
		try {
			BaseModel result = (BaseModel) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(BaseModel instance) {
		log.debug("attaching dirty BaseModel instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(BaseModel instance) {
		log.debug("attaching clean BaseModel instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}