package com.troy.entity;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.troy.util.KeystoneAPI;

public class UserSession {

	
	private GregorianCalendar expires_at;
	private boolean isdeveloper;
	private boolean isadmin;
	
	private KeystoneUserEntity kue = new KeystoneUserEntity();
	private Map mapMyProjects =new HashMap<String,ProjectRole>();
	private Map mapMyGroups=new HashMap<String,KeystoneGroupEntity>();
	List<ProjectRole> myList = new ArrayList<ProjectRole>();
	List<ProjectRole> paidList = new ArrayList<ProjectRole>();
	public List<BaseModel> baseList;
	
	public String getJson(){
		return "{\"UserSession\":{\"expires_at\":\""+expires_at.getTime()+"\",\"isdeveloper\":\""+isdeveloper+"\",\"isadmin\":\""+isadmin+"\","+kue.getJson()+","+getProjectListJson()+","+getGroupListJson()+"}}";
	}
	
	public String getProjectListJson(){
		
		for (Object item : mapMyProjects.values()) {
			ProjectRole pr = (ProjectRole)item;
			if(pr.isOwner())
				myList.add(pr);
			else
				paidList.add(pr);	
		}
		
		
		
		return GetMyJson(myList)+","+GetPaidJson(paidList)+","+getBaseJson();
		
		
	}
	private String getBaseJson(){
		JSONArray object = JSONArray.fromObject(baseList);
		return "\"BaseProjectList\":"+object.toString();
		
	}
	
	
	private String GetMyJson(List<ProjectRole> myList){

		String temp="\"MyProjectList\":[";
		if (!myList.isEmpty()){
			int cout = myList.size();
			int count = 1;
			for (Object item : myList) {
				ProjectRole pr = (ProjectRole)item;
				temp+="{"+pr.getJson()+"}";
				if(cout!=count){
					count++;
					temp+=",";
				}else{
					temp+="]";
				}
					
			}
		}else{
			temp+="]";
		}
		return temp;
	}
	
	private String GetPaidJson(List<ProjectRole> myList){

		String temp="\"PaidProjectList\":[";
		if (!myList.isEmpty()){
			int cout = myList.size();
			int count = 1;
			for (Object item : myList) {
				ProjectRole pr = (ProjectRole)item;
				temp+="{"+pr.getJson()+"}";
				if(cout!=count){
					count++;
					temp+=",";
				}else{
					temp+="]";
				}
					
			}
		}else{
			temp+="]";
		}
		return temp;
	}
	
	public String getGroupListJson(){
		String temp="\"MyGroupList\":[";
		if (!mapMyGroups.isEmpty()){
			int cout = mapMyGroups.size();
			int count = 1;
			for (Object item : mapMyGroups.values()) {
				KeystoneGroupEntity pr = (KeystoneGroupEntity)item;
				temp+="{"+pr.getJson()+"}";
				if(cout!=count){
					count++;
					temp+=",";
				}else{
					temp+="]";
				}
					
			}
		}else{
			temp+="]";
		}
		return temp;
		
	}
	
	public boolean isIsadmin() {
		return isadmin;
	}

	public void setIsadmin(boolean isadmin) {
		this.isadmin = isadmin;
	}
	
	public GregorianCalendar getExpires_at() {
		return expires_at;
	}
	public void setExpires_at(GregorianCalendar expires_at) {
		this.expires_at = expires_at;
	}
	public boolean isIsdeveloper() {
		return isdeveloper;
	}
	public void setIsdeveloper(boolean isdeveloper) {
		this.isdeveloper = isdeveloper;
	}
	public KeystoneUserEntity getKue() {
		return kue;
	}
	public void setKue(KeystoneUserEntity kue) {
		this.kue = kue;
	}
	public Map getMapMyProjects() {
		return mapMyProjects;
	}
	public void setMapMyProjects(Map mapMyProjects) {
		this.mapMyProjects = mapMyProjects;
	}
	public Map getMapMyGroups() {
		return mapMyGroups;
	}
	public void setMapMyGroups(Map mapMyGroups) {
		this.mapMyGroups = mapMyGroups;
	}
	
	
	
	
	
	
}
