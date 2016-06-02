package com.troy.dao;

import com.troy.entity.Project;

import java.util.List;
import java.util.Set;

import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 	* A data access object (DAO) providing persistence and search support for Project entities.
 			* Transaction control of the save(), update() and delete() operations 
		can directly support Spring container-managed transactions or they can be augmented	to handle user-managed Spring transactions. 
		Each of these methods provides additional information for how to configure it for the desired type of transaction control. 	
	 * @see com.troy.entity.Project
  * @author MyEclipse Persistence Tools 
 */
public class ProjectDAO extends BaseHibernateDAO  {
	     private static final Logger log = LoggerFactory.getLogger(ProjectDAO.class);
		//property constants
	public static final String NAME = "name";
	public static final String EXTRA = "extra";
	public static final String DESCRIPTION = "description";
	public static final String ENABLED = "enabled";
	public static final String DOMAIN_ID = "domainId";
	public static final String IS_DOMAIN = "isDomain";



    
    public void save(Project transientInstance) {
        log.debug("saving Project instance");
        try {
            getSession().save(transientInstance);
            log.debug("save successful");
        } catch (RuntimeException re) {
            log.error("save failed", re);
            throw re;
        }
    }
    
	public void delete(Project persistentInstance) {
        log.debug("deleting Project instance");
        try {
            getSession().delete(persistentInstance);
            log.debug("delete successful");
        } catch (RuntimeException re) {
            log.error("delete failed", re);
            throw re;
        }
    }
    
    public Project findById( java.lang.String id) {
        log.debug("getting Project instance with id: " + id);
        try {
            Project instance = (Project) getSession()
                    .get("com.troy.entity.Project", id);
            return instance;
        } catch (RuntimeException re) {
            log.error("get failed", re);
            throw re;
        }
    }
    
    
    public List findByExample(Project instance) {
        log.debug("finding Project instance by example");
        try {
            List results = getSession()
                    .createCriteria("com.troy.entity.Project")
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
      log.debug("finding Project instance with property: " + propertyName
            + ", value: " + value);
      try {
         String queryString = "from Project as model where model." 
         						+ propertyName + "= ?";
         Query queryObject = getSession().createQuery(queryString);
		 queryObject.setParameter(0, value);
		 return queryObject.list();
      } catch (RuntimeException re) {
         log.error("find by property name failed", re);
         throw re;
      }
	}

	public List findByName(Object name
	) {
		return findByProperty(NAME, name
		);
	}
	
	public List findByExtra(Object extra
	) {
		return findByProperty(EXTRA, extra
		);
	}
	
	public List findByDescription(Object description
	) {
		return findByProperty(DESCRIPTION, description
		);
	}
	
	public List findByEnabled(Object enabled
	) {
		return findByProperty(ENABLED, enabled
		);
	}
	
	public List findByDomainId(Object domainId
	) {
		return findByProperty(DOMAIN_ID, domainId
		);
	}
	
	public List findByIsDomain(Object isDomain
	) {
		return findByProperty(IS_DOMAIN, isDomain
		);
	}
	

	public List findAll() {
		log.debug("finding all Project instances");
		try {
			String queryString = "from Project";
	         Query queryObject = getSession().createQuery(queryString);
			 return queryObject.list();
		} catch (RuntimeException re) {
			log.error("find all failed", re);
			throw re;
		}
	}
	
    public Project merge(Project detachedInstance) {
        log.debug("merging Project instance");
        try {
            Project result = (Project) getSession()
                    .merge(detachedInstance);
            log.debug("merge successful");
            return result;
        } catch (RuntimeException re) {
            log.error("merge failed", re);
            throw re;
        }
    }

    public void attachDirty(Project instance) {
        log.debug("attaching dirty Project instance");
        try {
            getSession().saveOrUpdate(instance);
            log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
    
    public void attachClean(Project instance) {
        log.debug("attaching clean Project instance");
        try {
                      	getSession().lock(instance, LockMode.NONE);
                        log.debug("attach successful");
        } catch (RuntimeException re) {
            log.error("attach failed", re);
            throw re;
        }
    }
}