package com.troy.entity;

import java.util.HashSet;
import java.util.Set;


/**
 * Project entity. @author MyEclipse Persistence Tools
 */

public class Project  implements java.io.Serializable {


    // Fields    

     private String id;
     private Project project;
     private String name;
     private String extra;
     private String description;
     private Boolean enabled;
     private String domainId;
     private Boolean isDomain;
     private Set projects = new HashSet(0);


    // Constructors

    /** default constructor */
    public Project() {
    }

	/** minimal constructor */
    public Project(String id, String name, String domainId, Boolean isDomain) {
        this.id = id;
        this.name = name;
        this.domainId = domainId;
        this.isDomain = isDomain;
    }
    
    /** full constructor */
    public Project(String id, Project project, String name, String extra, String description, Boolean enabled, String domainId, Boolean isDomain, Set projects) {
        this.id = id;
        this.project = project;
        this.name = name;
        this.extra = extra;
        this.description = description;
        this.enabled = enabled;
        this.domainId = domainId;
        this.isDomain = isDomain;
        this.projects = projects;
    }

   
    // Property accessors

    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public Project getProject() {
        return this.project;
    }
    
    public void setProject(Project project) {
        this.project = project;
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

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
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

    public Boolean getIsDomain() {
        return this.isDomain;
    }
    
    public void setIsDomain(Boolean isDomain) {
        this.isDomain = isDomain;
    }

    public Set getProjects() {
        return this.projects;
    }
    
    public void setProjects(Set projects) {
        this.projects = projects;
    }
   








}