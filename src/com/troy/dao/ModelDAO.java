package com.troy.dao;

import com.troy.entity.Model;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A data access object (DAO) providing persistence and search support for Model
 * entities. Transaction control of the save(), update() and delete() operations
 * can directly support Spring container-managed transactions or they can be
 * augmented to handle user-managed Spring transactions. Each of these methods
 * provides additional information for how to configure it for the desired type
 * of transaction control.
 * 
 * @see com.troy.entity.Model
 * @author MyEclipse Persistence Tools
 */
public class ModelDAO extends BaseHibernateDAO {
	private static final Logger log = LoggerFactory.getLogger(ModelDAO.class);
	// property constants
	public static final String EXECUTE_TYPE = "executeType";
	public static final String CREATE_USER_ID = "createUserId";
	public static final String MODEL_NAME = "modelName";
	public static final String MODEL_DESCRIPTION = "modelDescription";
	public static final String HDFS_LOCATION = "hdfsLocation";
	public static final String SUB_MODEL = "subModel";
	public static final String STATUS = "status";
	public static final String VERSION = "version";
	public static final String PRIORITY = "priority";
	public static final String PROJECT_ID = "projectId";
	public static final String ENABLE = "enable";
	public static final String ICONPATH = "iconpath";
	public static final String FUNCTION = "function";
	public static final String OUTPUT_DIR = "outputDir";

	public boolean save(Model transientInstance) {
		log.debug("saving Model instance");
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

	public void delete(Model persistentInstance) {
		log.debug("deleting Model instance");
		try {
			Transaction tran=getSession().beginTransaction();
			getSession().delete(persistentInstance);
			tran.commit();
			log.debug("delete successful");
		} catch (RuntimeException re) {
			log.error("delete failed", re);
			throw re;
		}
	}

	public Model findById(java.lang.String id) {
		log.debug("getting Model instance with id: " + id);
		try {
			Model instance = (Model) getSession().get("com.troy.entity.Model",
					id);
			return instance;
		} catch (RuntimeException re) {
			log.error("get failed", re);
			throw re;
		}
	}

	public List findByExample(Model instance) {
		log.debug("finding Model instance by example");
		try {
			List results = getSession().createCriteria("com.troy.entity.Model")
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
		log.debug("finding Model instance with property: " + propertyName
				+ ", value: " + value);
		try {
			String queryString = "from Model as model where model."
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
	
	public List findByType(Object type) {
		return findByProperty(EXECUTE_TYPE, type);
	}

	public List findByCreateUserId(Object createUserId) {
		return findByProperty(CREATE_USER_ID, createUserId);
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

	public List findBySubModel(Object subModel) {
		return findByProperty(SUB_MODEL, subModel);
	}

	public List findByStatus(Object status) {
		return findByProperty(STATUS, status);
	}

	public List findByVersion(Object version) {
		return findByProperty(VERSION, version);
	}

	public List findByPriority(Object priority) {
		return findByProperty(PRIORITY, priority);
	}

	public List findByProjectId(Object projectId) {
		return findByProperty(PROJECT_ID, projectId);
	}

	public List findByEnable(Object enable) {
		return findByProperty(ENABLE, enable);
	}

	public List findByIconpath(Object iconpath) {
		return findByProperty(ICONPATH, iconpath);
	}

	public List findByFunction(Object function) {
		return findByProperty(FUNCTION, function);
	}

	public List findByOutputDir(Object outputDir) {
		return findByProperty(OUTPUT_DIR, outputDir);
	}

	public List findAll() {
		log.debug("finding all Model instances");
		try {
			String queryString = "from Model";
			Query queryObject = getSession().createQuery(queryString);
			return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}

	public Model merge(Model detachedInstance) {
		log.debug("merging Model instance");
		try {
			Model result = (Model) getSession().merge(detachedInstance);
			log.debug("merge successful");
			return result;
		} catch (RuntimeException re) {
			log.error("merge failed", re);
			throw re;
		}
	}

	public void attachDirty(Model instance) {
		log.debug("attaching dirty Model instance");
		try {
			getSession().saveOrUpdate(instance);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}

	public void attachClean(Model instance) {
		log.debug("attaching clean Model instance");
		try {
			getSession().lock(instance, LockMode.NONE);
			log.debug("attach successful");
		} catch (RuntimeException re) {
			log.error("attach failed", re);
			throw re;
		}
	}
}