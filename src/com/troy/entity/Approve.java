package com.troy.entity;

/**
 * Approve entity. @author MyEclipse Persistence Tools
 */

public class Approve implements java.io.Serializable {

	// Fields

	private Integer id;
	private String sponsorId;
	private Integer status;
	private String approverId;
	private String notApprovedReason;

	// Constructors

	/** default constructor */
	public Approve() {
	}

	/** minimal constructor */
	public Approve(String sponsorId, Integer status) {
		this.sponsorId = sponsorId;
		this.status = status;
	}

	/** full constructor */
	public Approve(String sponsorId, Integer status, String approverId,
			String notApprovedReason) {
		this.sponsorId = sponsorId;
		this.status = status;
		this.approverId = approverId;
		this.notApprovedReason = notApprovedReason;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSponsorId() {
		return this.sponsorId;
	}

	public void setSponsorId(String sponsorId) {
		this.sponsorId = sponsorId;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getApproverId() {
		return this.approverId;
	}

	public void setApproverId(String approverId) {
		this.approverId = approverId;
	}

	public String getNotApprovedReason() {
		return this.notApprovedReason;
	}

	public void setNotApprovedReason(String notApprovedReason) {
		this.notApprovedReason = notApprovedReason;
	}

}