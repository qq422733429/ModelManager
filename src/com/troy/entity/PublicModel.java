package com.troy.entity;

import java.util.Date;




/**
 * PublicModel entity. @author MyEclipse Persistence Tools
 */

public class PublicModel implements java.io.Serializable {

	// Fields

	private String modelId;
	private Double price;
	private Short free;
	private Date publishDate;

	// Constructors

	/** default constructor */
	public PublicModel() {
	}

	/** minimal constructor */
	public PublicModel(String modelId, Short free) {
		this.modelId = modelId;
		this.free = free;
	}

	/** full constructor */
	public PublicModel(String modelId, Double price, Short free,
			Date publishDate) {
		this.modelId = modelId;
		this.price = price;
		this.free = free;
		this.publishDate = publishDate;
	}

	// Property accessors

	public String getModelId() {
		return this.modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Short getFree() {
		return this.free;
	}

	public void setFree(Short free) {
		this.free = free;
	}

	public Date getPublishDate() {
		return this.publishDate;
	}

	public void setPublishDate(Date publishDate) {
		this.publishDate = publishDate;
	}

}