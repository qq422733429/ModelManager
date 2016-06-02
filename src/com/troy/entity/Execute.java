package com.troy.entity;

import java.util.Date;

/**
 * Execute entity. @author MyEclipse Persistence Tools
 */

public class Execute implements java.io.Serializable {

	// Fields

	private String executeId;
	private String createUserId;
	private String modelId;
	private Integer status;
	private String output;
	private String errormessage;
	private Date executeTime;
	private Date endTime;
	private Short enable;

	// Constructors

	/** default constructor */
	public Execute() {
	}

	/** minimal constructor */
	public Execute(String executeId, String createUserId, String modelId,
			Integer status, Date executeTime) {
		this.executeId = executeId;
		this.createUserId = createUserId;
		this.modelId = modelId;
		this.status = status;
		this.executeTime = executeTime;
	}

	/** full constructor */
	public Execute(String executeId, String createUserId, String modelId,
			Integer status, String output, String errormessage,
			Date executeTime, Date endTime, Short enable) {
		this.executeId = executeId;
		this.createUserId = createUserId;
		this.modelId = modelId;
		this.status = status;
		this.output = output;
		this.errormessage = errormessage;
		this.executeTime = executeTime;
		this.endTime = endTime;
		this.enable = enable;
	}

	// Property accessors

	public String getExecuteId() {
		return this.executeId;
	}

	public void setExecuteId(String executeId) {
		this.executeId = executeId;
	}

	public String getCreateUserId() {
		return this.createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOutput() {
		return this.output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getErrormessage() {
		return this.errormessage;
	}

	public void setErrormessage(String errormessage) {
		this.errormessage = errormessage;
	}

	public Date getExecuteTime() {
		return this.executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Short getEnable() {
		return this.enable;
	}

	public void setEnable(Short enable) {
		this.enable = enable;
	}

}