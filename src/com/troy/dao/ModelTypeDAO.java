package com.troy.dao;

import com.troy.entity.ModelType;

import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 	* A data access object (DAO) providing persistence and search support for ModelType entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see com.troy.entity.ModelType
  * @author MyEclipse Persistence Tools 
 */
public class ModelTypeDAO extends BaseHibernateDAO  {
	     private static final Logger log = LoggerFactory.getLogger(ModelTypeDAO.class);
		//property constants
	public static final String TYPE_NAME = "typeName";
	public static final String TYPE_DESC = "typeDesc";
	public static final String PARENT_TYPE_ID = "parentTypeId";



    
    public Boolean save(ModelType transientInstance) {
        log.debug("saving ModelType instance");
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
    
	public void delete(ModelType persistentInstance) {
        log.debug("deleting ModelType instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public ModelType findById( java.lang.Integer id) {
        log.debug("getting ModelType instance with id: " + id);
        try {
            ModelType instance = (ModelType) getSession()
                    .get("com.troy.entity.ModelType", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(ModelType instance) {
        log.debug("finding ModelType instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.troy.entity.ModelType")
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
      log.debug("finding ModelType instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from ModelType as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByTypeName(Object typeName
	) {
		return findByProperty(TYPE_NAME, typeName
		);
	}
	
	public List findByTypeDesc(Object typeDesc
	) {
		return findByProperty(TYPE_DESC, typeDesc
		);
	}
	
	public List findByParentTypeId(Object parentTypeId
	) {
		return findByProperty(PARENT_TYPE_ID, parentTypeId
		);
	}
	

	public List findAll() {
		log.debug("finding all ModelType instances");
		try {
			String queryString = "from ModelType";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public ModelType merge(ModelType detachedInstance) {
        log.debug("merging ModelType instance");
        try {
            ModelType result = (ModelType) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(ModelType instance) {
        log.debug("attaching dirty ModelType instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(ModelType instance) {
        log.debug("attaching clean ModelType instance");
        try {
                      	getSession().lock(instance, LockMode.NONE);
                        log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}