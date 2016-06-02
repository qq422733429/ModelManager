package com.troy.dao;

import com.troy.entity.TypeModelMapping;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 	* A data access object (DAO) providing persistence and search support for TypeModelMapping entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see com.troy.entity.TypeModelMapping
  * @author MyEclipse Persistence Tools 
 */
public class TypeModelMappingDAO extends BaseHibernateDAO  {
	     private static final Logger log = LoggerFactory.getLogger(TypeModelMappingDAO.class);
		//property constants
	public static final String MODEL_ID = "modelId";
	public static final String TYPE_ID = "typeId";



    
    public void save(TypeModelMapping transientInstance) {
        log.debug("saving TypeModelMapping instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(TypeModelMapping persistentInstance) {
        log.debug("deleting TypeModelMapping instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public TypeModelMapping findById( java.lang.Integer id) {
        log.debug("getting TypeModelMapping instance with id: " + id);
        try {
            TypeModelMapping instance = (TypeModelMapping) getSession()
                    .get("com.troy.entity.TypeModelMapping", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(TypeModelMapping instance) {
        log.debug("finding TypeModelMapping instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.troy.entity.TypeModelMapping")
                    .add(Example.create(instance))
            .list();
            log.debug("find by example successful, result size: " + results.size());
            return results;
        } catch (RuntimeException re) {
            log.error("find by example failed", re);
            throw re;
        }
    }    
    
    public List findByProperty(String propertyName, Object value) {
      log.debug("finding TypeModelMapping instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from TypeModelMapping as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByModelId(Object modelId
	) {
		return findByProperty(MODEL_ID, modelId
		);
	}
	
	public List findByTypeId(Object typeId
	) {
		return findByProperty(TYPE_ID, typeId
		);
	}
	

	public List findAll() {
		log.debug("finding all TypeModelMapping instances");
		try {
			String queryString = "from TypeModelMapping";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public TypeModelMapping merge(TypeModelMapping detachedInstance) {
        log.debug("merging TypeModelMapping instance");
        try {
            TypeModelMapping result = (TypeModelMapping) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(TypeModelMapping instance) {
        log.debug("attaching dirty TypeModelMapping instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(TypeModelMapping instance) {
        log.debug("attaching clean TypeModelMapping instance");
        try {
                      	getSession().lock(instance, LockMode.NONE);
                        log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}