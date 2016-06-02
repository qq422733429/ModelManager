package com.troy.entity;



/**
 * Group entity. @author MyEclipse Persistence Tools
 */

public class Group  implements java.io.Serializable {


    // Fields    

     private String id;
     private String domainId;
     private String name;
     private String description;
     private String extra;


    // Constructors

    /** default constructor */
    public Group() {
    }

	/** minimal constructor */
    public Group(String id, String domainId, String name) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
    }
    
    /** full constructor */
    public Group(String id, String domainId, String name, String description, String extra) {
        this.id = id;
        this.domainId = domainId;
        this.name = name;
        this.description = description;
        this.extra = extra;
    }

   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getDomainId() {
        return this.domainId;
    }
    
    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getExtra() {
        return this.extra;
    }
    
    public void setExtra(String extra) {
        this.extra = extra;
    }
   








}