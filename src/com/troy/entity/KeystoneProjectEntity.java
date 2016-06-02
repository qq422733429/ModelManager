package com.troy.entity;

public class KeystoneProjectEntity {
	private String description;
	private String projectName;
	private String projectID;
	private String enable;
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectID() {
		return projectID;
	}
	public void setProjectID(String projectID) {
		this.projectID = projectID;
	}
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getJson(){
		return "\"project\":{\"projectID\":\""+projectID+"\",\"projectName\":\""+projectName+"\",\"description\":\""+description+"\",\"enable\":\""+enable+"\"}";
	}
}