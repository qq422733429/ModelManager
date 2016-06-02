package com.troy.entity;

import java.util.Date;


/**
 * BaseModel entity. @author MyEclipse Persistence Tools
 */

public class BaseModel implements java.io.Serializable {

	// Fields

	private String modelId;
	private Integer executeType;
	private String modelName;
	private String modelDescription;
	private String hdfsLocation;
	private Integer status;
	private String version;
	private Date createDate;
	private String projectId;
	private String iconpath;
	private String function;
	private Integer baseType;

	// Constructors

	/** default constructor */
	public BaseModel() {
	}

	/** minimal constructor */
	public BaseModel(String modelId, Integer executeType, String modelName,
			String hdfsLocation, Integer status, Date createDate,
			String projectId, Integer baseType) {
		this.modelId = modelId;
		this.executeType = executeType;
		this.modelName = modelName;
		this.hdfsLocation = hdfsLocation;
		this.status = status;
		this.createDate = createDate;
		this.projectId = projectId;
		this.baseType = baseType;
	}

	/** full constructor */
	public BaseModel(String modelId, Integer executeType, String modelName,
			String modelDescription, String hdfsLocation, Integer status,
			String version, Date createDate, String projectId,
			String iconpath, String function, Integer baseType) {
		this.modelId = modelId;
		this.executeType = executeType;
		this.modelName = modelName;
		this.modelDescription = modelDescription;
		this.hdfsLocation = hdfsLocation;
		this.status = status;
		this.version = version;
		this.createDate = createDate;
		this.projectId = projectId;
		this.iconpath = iconpath;
		this.function = function;
		this.baseType = baseType;
	}

	// Property accessors

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Integer getExecuteType() {
		return this.executeType;
	}

	public void setExecuteType(Integer executeType) {
		this.executeType = executeType;
	}

	public String getModelName() {
		return this.modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModelDescription() {
		return this.modelDescription;
	}

	public void setModelDescription(String modelDescription) {
		this.modelDescription = modelDescription;
	}

	public String getHdfsLocation() {
		return this.hdfsLocation;
	}

	public void setHdfsLocation(String hdfsLocation) {
		this.hdfsLocation = hdfsLocation;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public String getIconpath() {
		return this.iconpath;
	}

	public void setIconpath(String iconpath) {
		this.iconpath = iconpath;
	}

	public String getFunction() {
		return this.function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public Integer getBaseType() {
		return this.baseType;
	}

	public void setBaseType(Integer baseType) {
		this.baseType = baseType;
	}

}