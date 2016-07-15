package com.troy.entity;

/**
 * ApplyTable entity. @author MyEclipse Persistence Tools
 */

public class ApplyTable implements java.io.Serializable {

	// Fields

	private Integer id;
	private String applyUser;
	private String ownerUser;
	private String modelId;
	private Integer status;
	private String message;
	private String role;
	
	
	

	// Constructors

	/** default constructor */
	public ApplyTable() {
	}

	/** minimal constructor */
	public ApplyTable(String applyUser, String ownerUser, String modelId,
			Integer status, String role) {
		this.applyUser = applyUser;
		this.ownerUser = ownerUser;
		this.modelId = modelId;
		this.status = status;
		this.role = role;
	}

	/** full constructor */
	public ApplyTable(String applyUser, String ownerUser, String modelId,
			Integer status, String message, String role) {
		this.applyUser = applyUser;
		this.ownerUser = ownerUser;
		this.modelId = modelId;
		this.status = status;
		this.message = message;
		this.role = role;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getApplyUser() {
		return this.applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getOwnerUser() {
		return this.ownerUser;
	}

	public void setOwnerUser(String ownerUser) {
		this.ownerUser = ownerUser;
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

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRole() {
		return this.role;
	}

	public void setRole(String role) {
		this.role = role;
	}

}