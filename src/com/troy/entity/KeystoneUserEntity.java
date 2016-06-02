package com.troy.entity;

public class KeystoneUserEntity {
	private String email;
	private String description;
	private String userName;
	private String enable;
	private String userID;
	private String password;
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEnable() {
		return enable;
	}
	public void setEnable(String enable) {
		this.enable = enable;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getJson(){
		return "\"User\":{\"userID\":\""+userID+"\",\"userName\":\""+userName+"\",\"description\":\""+description+"\",\"email\":\""+email+"\",\"enable\":\""+enable+"\"}";
	}

}
