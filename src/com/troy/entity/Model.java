package com.troy.entity;

import java.util.Date;

/**
 * Model entity. 
 * @author Peter King
 */

public class Model implements java.io.Serializable {

	// Fields

	private String modelId;
	private Integer executeType;
	private String createUserId;
	private String modelName;
	private String modelDescription;
	private String hdfsLocation;
	private String subModel;
	private Integer status;
	private String version;
	private Date createDate;
	private Date updateDate;
	private Integer priority;
	private String projectId;
	private Boolean enable;
	private String iconpath;
	private String function;
	private String outputDir;
	private Boolean isPublic;
	private String inputDir;
	private Integer type;
	// Constructors

	

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	/** default constructor */
	public Model() {
	}

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	/** minimal constructor */
	public Model(String modelId, Integer executeType, String createUserId,
			String modelName, String hdfsLocation, Integer status,
			Date createDate, Date updateDate, String projectId,
			Boolean enable,Integer type) {
		this.modelId = modelId;
		this.executeType = executeType;
		this.createUserId = createUserId;
		this.modelName = modelName;
		this.hdfsLocation = hdfsLocation;
		this.status = status;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.projectId = projectId;
		this.enable = enable;
		this.type = type;
	}

	/** full constructor */
	public Model(String modelId, Integer executeType, String createUserId,
			String modelName, String modelDescription, String hdfsLocation,
			String subModel, Integer status, String version,
			Date createDate, Date updateDate, Integer priority,
			String projectId, Boolean enable, String iconpath, String function,
			String outputDir,Boolean isPublic,String inputDir,Integer type) {
		this.modelId = modelId;
		this.executeType = executeType;
		this.createUserId = createUserId;
		this.modelName = modelName;
		this.modelDescription = modelDescription;
		this.hdfsLocation = hdfsLocation;
		this.subModel = subModel;
		this.status = status;
		this.version = version;
		this.createDate = createDate;
		this.updateDate = updateDate;
		this.priority = priority;
		this.projectId = projectId;
		this.enable = enable;
		this.iconpath = iconpath;
		this.function = function;
		this.outputDir = outputDir;
		this.isPublic = isPublic;
		this.inputDir = inputDir;
		this.type = type;
	}

	// Property accessors
	public Boolean getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Boolean isPublic) {
		this.isPublic = isPublic;
	}
	
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

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
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

	public String getSubModel() {
		return this.subModel;
	}

	public void setSubModel(String subModel) {
		this.subModel = subModel;
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

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Integer getPriority() {
		return this.priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Boolean getEnable() {
		return this.enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
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

	public String getOutputDir() {
		return this.outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}
	
}