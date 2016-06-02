package com.troy.entity;



/**
 * User entity. @author MyEclipse Persistence Tools
 */

public class User  implements java.io.Serializable {


    // Fields    

     private String id;
     private String name;
     private String extra;
     private String password;
     private Boolean enabled;
     private String domainId;
     private String defaultProjectId;


    // Constructors

    /** default constructor */
    public User() {
    }

	/** minimal constructor */
    public User(String id, String name, String domainId) {
        this.id = id;
        this.name = name;
        this.domainId = domainId;
    }
    
    /** full constructor */
    public User(String id, String name, String extra, String password, Boolean enabled, String domainId, String defaultProjectId) {
        this.id = id;
        this.name = name;
        this.extra = extra;
        this.password = password;
        this.enabled = enabled;
        this.domainId = domainId;
        this.defaultProjectId = defaultProjectId;
    }

   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getExtra() {
        return this.extra;
    }
    
    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getEnabled() {
        return this.enabled;
    }
    
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getDomainId() {
        return this.domainId;
    }
    
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getDefaultProjectId() {
        return this.defaultProjectId;
    }
    
    public void setDefaultProjectId(String defaultProjectId) {
        this.defaultProjectId = defaultProjectId;
    }
   








}