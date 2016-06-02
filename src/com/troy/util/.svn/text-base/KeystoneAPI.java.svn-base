package com.troy.util;

import com.troy.util.HttpClientUtil;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.Header;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

import com.troy.dao.ModelDAO;
import com.troy.entity.KeystoneGroupEntity;
import com.troy.entity.KeystoneProjectEntity;
import com.troy.entity.KeystoneRoleEntity;
import com.troy.entity.KeystoneUserEntity;
import com.troy.entity.ProjectRole;
import com.troy.entity.UserSession;

public class KeystoneAPI {
	private String hostURL = null;
	HttpClientUtil hcu = new HttpClientUtil();
	public Map mapRoleByName = new HashMap<String,ProjectRole>();
	public Map mapRoleByID = new HashMap<String,ProjectRole>();
	ModelDAO modeldao;
	String domainId;
	
	public String getDomainId() {
		return domainId;
	}
	public void setDomainId(String domainId) {
		this.domainId = domainId;
	}
	public ModelDAO getModeldao() {
		return modeldao;
	}
	public void setModeldao(ModelDAO modeldao) {
		this.modeldao = modeldao;
	}
	//全部添加https
	public KeystoneAPI(String url,String X_TOKEN) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException, ClientProtocolException, IOException, HttpException{
		
		this.hostURL =url;
		hcu.init(X_TOKEN);
		getRoleList();
		
	}
	private final static String[] hexDigits = {"0", "1", "2", "3", "4",  
        "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"}; 
	private final static String byteToHexString(byte b){  
	        int n = b;  
	        if (n < 0)  
	            n = 256 + n;  
	        int d1 = n / 16;  
	        int d2 = n % 16;  
	        return hexDigits[d1] + hexDigits[d2];  
	    }  
	private final static String md5Method(String pwd){
		try {
            byte[] btInput = pwd.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            StringBuffer resultSb = new StringBuffer();  
            for (int i = 0; i < md.length; i++){  
                resultSb.append(byteToHexString(md[i]));  
            }  
            
            return resultSb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
		
	}
	
	/**
	 * 注册新用户，已废弃
	 * @param KeystoneUserEntity
	 * @return String
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 */
	@Deprecated
	public String register(KeystoneUserEntity kue) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		URL url = new URL("https://"+hostURL+"users"); 
        String content = "{\"user\": {\"description\":\""+kue.getDescription()+"\",\"domain_id\": \""+domainId+"\",\"email\": \""+kue.getEmail()+"\",\"enabled\": true,\"name\": \""+kue.getUserName()+"\",\"password\": \""+md5Method(kue.getPassword())+"\"}}";
       
        HttpResponse hrp =hcu.doPost(url.toString(), content);
        if((hrp.getStatusLine().getStatusCode())==201){
        	JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
        	EntityUtils.consume(hrp.getEntity());
        	jsonObj=jsonObj.getJSONObject("user");
        	return jsonObj.get("id").toString();
        	
        }else{
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
        }
	}
	
	
	
	
	
	public void getRoleList() throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		URL url = new URL("http://"+hostURL+"roles");
		HttpResponse hrp=hcu.doGet(url.toString());
	    if((hrp.getStatusLine().getStatusCode()) != 200){
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
			
	    }
	    String aa = EntityUtils.toString(hrp.getEntity());
	    EntityUtils.consume(hrp.getEntity());
	    JSONObject jsonObj =new JSONObject().fromObject(aa);	    
	    for (Object item : (List)jsonObj.get("roles")) {
	    	JSONObject jno = new JSONObject().fromObject(item);
	    	KeystoneRoleEntity kre =new KeystoneRoleEntity();
	    	kre.setRoleID(jno.get("id").toString());
	    	kre.setRoleName(jno.get("name").toString());
	    	mapRoleByID.put(kre.getRoleID(), kre);
	    	mapRoleByName.put(kre.getRoleName(), kre);
	    }
	}
	
	public KeystoneUserEntity checkLogin(String token) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		
			URL url = new URL("http://"+hostURL+"auth/tokens");
		   
	        String content = "{\"auth\":{\"identity\":{\"methods\": [\"token\"],\"token\":{\"id\":\""+token+"\"}}}}";
	        HttpResponse hrp =hcu.doPost(url.toString(), content);
	        
	        
		    if((hrp.getStatusLine().getStatusCode()) == 201){
			    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
			    EntityUtils.consume(hrp.getEntity());
			    return getSpecialUser(jsonObj.getJSONObject("token").getJSONObject("user").getString("id"));
			   
		    }else{
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t Can not find token "+token);
			}
		
	}
	
	public KeystoneUserEntity findUserByUserName(String username) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException{
		
		URL url = new URL("http://"+hostURL+"users?name="+username);
	   
        HttpResponse hrp =hcu.doGet(url.toString());
        
        
	    if((hrp.getStatusLine().getStatusCode()) == 200){
		    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
		    EntityUtils.consume(hrp.getEntity());
		    jsonObj=jsonObj.getJSONArray("users").getJSONObject(0);
		    KeystoneUserEntity kue = new KeystoneUserEntity();
		    kue.setUserID(jsonObj.getString("id"));
		    kue.setDescription(jsonObj.getString("description"));
		    kue.setEmail(jsonObj.getString("email"));
		    kue.setUserName(jsonObj.getString("name"));
		    return kue;
		    
	    }else{
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
		}
	
	}
	
	public KeystoneGroupEntity getGroupIDbyGroupName(String groupName) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException{
		
		URL url = new URL("http://"+hostURL+"groups?name="+groupName);
	   
        HttpResponse hrp =hcu.doGet(url.toString());
        
        
	    if((hrp.getStatusLine().getStatusCode()) == 200){
		    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
		    EntityUtils.consume(hrp.getEntity());
		    jsonObj=jsonObj.getJSONArray("groups").getJSONObject(0);
		    KeystoneGroupEntity kue = new KeystoneGroupEntity();
		    kue.setGroupID(jsonObj.getString("id"));
		    kue.setGroupDescription(jsonObj.getString("description"));
		    kue.setGroupName(jsonObj.getString("name"));
		    return kue;
		    
	    }else{
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
		}
	
	}
	
	public KeystoneUserEntity getSpecialUser(String UserID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
			URL url = new URL("http://"+hostURL+"users/"+UserID);
			HttpResponse hrp=hcu.doGet(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 200){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
		    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
		    EntityUtils.consume(hrp.getEntity());
		    jsonObj = jsonObj.getJSONObject("user");
		    KeystoneUserEntity kue = new KeystoneUserEntity();
		    kue.setDescription(jsonObj.getString("description"));
		    kue.setEmail(jsonObj.getString("email"));
		    kue.setEnable("true");
		    kue.setUserID(jsonObj.getString("id"));
		    kue.setUserName(jsonObj.getString("name"));
		    return kue;
		
	}
	

	
	
	public KeystoneProjectEntity getSpecialProject(String ProjectID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		URL url = new URL("http://"+hostURL+"projects/"+ProjectID);
		HttpResponse hrp=hcu.doGet(url.toString());
	    if((hrp.getStatusLine().getStatusCode()) != 200){
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
			
	    }
	    
	    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
	    EntityUtils.consumeQuietly(hrp.getEntity());
	    jsonObj = jsonObj.getJSONObject("project");
	    KeystoneProjectEntity kue = new KeystoneProjectEntity();
	    kue.setDescription(jsonObj.getString("description"));
	    kue.setProjectID(jsonObj.getString("id"));
	    kue.setProjectName(jsonObj.getString("name"));
	    kue.setEnable(jsonObj.getString("enabled"));
	    return kue;
	
	}
	
	public KeystoneGroupEntity getSpecialGroup(String groupID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		URL url = new URL("http://"+hostURL+"groups/"+groupID);
		HttpResponse hrp=hcu.doGet(url.toString());
	    if((hrp.getStatusLine().getStatusCode()) != 200){
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
			
	    }
	    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
	    EntityUtils.consume(hrp.getEntity());
	    jsonObj = jsonObj.getJSONObject("group");
	    KeystoneGroupEntity kue = new KeystoneGroupEntity();
	    kue.setGroupDescription(jsonObj.getString("description"));
	    kue.setGroupID(jsonObj.getString("id"));
	    kue.setGroupName(jsonObj.getString("name"));
	    return kue;
	
	}
	
	
	
	
	public void getAllGrant(String userID,UserSession us) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (!(getAllGrantByGroup(userID,us)&&getAllGrantByUser(userID,us))){
			throw new IOException("get grant fail!");
		}
	}
	private boolean getAllGrantByUser(String userID,UserSession us) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			URL url = new URL("http://"+hostURL+"role_assignments?user.id="+userID);
			HttpResponse hrp=hcu.doGet(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 200){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
		    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
		    EntityUtils.consume(hrp.getEntity());
		     for (Object item : jsonObj.getJSONArray("role_assignments")) {
		    	 if (((JSONObject)item).getJSONObject("scope").containsKey("domain")){
		    		 String aa = ((JSONObject)item).getJSONObject("role").getString("id");
		    		 if(((KeystoneRoleEntity)mapRoleByName.get("developer")).getRoleID().equals(aa)){
		    			 us.setIsdeveloper(true);
		    		 }
		    		 if(((KeystoneRoleEntity)mapRoleByName.get("admin")).getRoleID().equals(aa)){
		    			 us.setIsadmin(true);
		    		 }
		    	 }else{
			    	String id=((JSONObject)item).getJSONObject("scope").getJSONObject("project").getString("id");
					ProjectRole prtemp = new ProjectRole();
					
			    	if(us.getMapMyProjects().containsKey(id)){
						prtemp=(ProjectRole)us.getMapMyProjects().get(id);
						if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item).getJSONObject("role").getString("id")))).getRoleName().equals("owner")){
							prtemp.setOwner(true);
						}
						if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item).getJSONObject("role").getString("id")))).getRoleName().equals("writer")){
							prtemp.setWriter(true);
						}
						if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item).getJSONObject("role").getString("id")))).getRoleName().equals("executer")){
							prtemp.setExecuter(true);
						}
					}else{
						prtemp.setKpe(getSpecialProject(id));
						if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item).getJSONObject("role").getString("id")))).getRoleName().equals("owner")){
							prtemp.setOwner(true);
						}
						if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item).getJSONObject("role").getString("id")))).getRoleName().equals("writer")){
							prtemp.setWriter(true);
						}
						if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item).getJSONObject("role").getString("id")))).getRoleName().equals("executer")){
							prtemp.setExecuter(true);
						}
						prtemp.setModel(modeldao.findById(id));
						us.getMapMyProjects().put(id, prtemp);
					}
			    	
			    	
		    	 }
			    }
			    return true;
		    
	}
	
	private boolean getAllGrantByGroup(String userID,UserSession us) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			URL url = new URL("http://"+hostURL+"users/"+userID+"/groups");
			HttpResponse hrp=hcu.doGet(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 200){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
		    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
		    EntityUtils.consume(hrp.getEntity());
			    
			    for (Object item : jsonObj.getJSONArray("groups")) {
					KeystoneGroupEntity kge =new KeystoneGroupEntity();
					kge.setGroupDescription(((JSONObject)item).getString("description"));
					kge.setGroupName(((JSONObject)item).getString("name"));
					kge.setGroupID(((JSONObject)item).getString("id"));
					us.getMapMyGroups().put(kge.getGroupID(), kge);
				}
			    for (Object item : us.getMapMyGroups().values()) {
					
						URL url1 = new URL("http://"+hostURL+"role_assignments?group.id="+((KeystoneGroupEntity)item).getGroupID());
						HttpResponse hrp2=hcu.doGet(url1.toString());
					    if((hrp2.getStatusLine().getStatusCode()) != 200){
					        throw new IOException("get grant by group fail");
					    }
						    JSONObject jsonObj1 =new JSONObject().fromObject(EntityUtils.toString(hrp2.getEntity()));
						    for (Object item2 : jsonObj1.getJSONArray("role_assignments")) {
						    	String id=((JSONObject)item2).getJSONObject("scope").getJSONObject("project").getString("id");
								
						    	if(us.getMapMyProjects().containsKey(id)){
						    		ProjectRole prtemp=(ProjectRole)us.getMapMyProjects().get(id);
									if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item2).getJSONObject("role").getString("id")))).getRoleName().equals("owner")){
										prtemp.setOwner(true);
									}
									if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item2).getJSONObject("role").getString("id")))).getRoleName().equals("writer")){
										prtemp.setWriter(true);
									}
									if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item2).getJSONObject("role").getString("id")))).getRoleName().equals("executer")){
										prtemp.setExecuter(true);
									}
								}else{
									ProjectRole pr =new ProjectRole();
									pr.setKpe(getSpecialProject(id));
									if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item2).getJSONObject("role").getString("id")))).getRoleName().equals("owner")){
										pr.setOwner(true);
									}
									if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item2).getJSONObject("role").getString("id")))).getRoleName().equals("writer")){
										pr.setWriter(true);
									}
									if(((KeystoneRoleEntity)mapRoleByID.get((((JSONObject)item2).getJSONObject("role").getString("id")))).getRoleName().equals("executer")){
										pr.setExecuter(true);
									}
									pr.setModel(modeldao.findById(id));
									us.getMapMyProjects().put(id, pr);
								}
						    }
			    }
						    
				return true;	    
		
			   
		
	}
	
	public boolean removeUser(String userID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			URL url = new URL("http://"+hostURL+"users/"+userID);
			HttpResponse hrp=hcu.doDelete(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 204){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());

		    }
		   return true;
	}
		
	public String createProject(KeystoneProjectEntity kpe,String userID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
			URL url = new URL("http://"+hostURL+"projects");
		    String content = "{\"project\": {\"description\": \""+kpe.getDescription()+"\",\"domain_id\": \""+domainId+"\",\"enabled\": true,\"name\": \""+kpe.getProjectName()+"\"}}";
		    HttpResponse hrp =hcu.doPost(url.toString(), content);
	        if((hrp.getStatusLine().getStatusCode())!=201){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
	        }
		    
	        JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));
	        EntityUtils.consume(hrp.getEntity());
	        
	        jsonObj=jsonObj.getJSONObject("project");
		    URL url1 = new URL("http://"+hostURL+"projects/"+jsonObj.get("id").toString()+"/users/"+userID+"/roles/"+((KeystoneRoleEntity)mapRoleByName.get("owner")).getRoleID());
		    HttpResponse hrp2 =hcu.doPut(url1.toString());
	        if((hrp2.getStatusLine().getStatusCode())!=204){
	        	throw new IOException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
	        }
		    return jsonObj.get("id").toString();
			
		}
	
	public boolean changePasswd(String userID,String oldPwd,String newPwd) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			URL url = new URL("http://"+hostURL+"users/"+userID+"/password");
	        String content = "{\"user\":{\"password\":\""+md5Method(newPwd)+"\",\"original_password\":\""+md5Method(oldPwd)+"\"}}";
	        HttpResponse hrp =hcu.doPost(url.toString(), content);
	        if((hrp.getStatusLine().getStatusCode())==204){
		    	return true;
			}else{
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
			}
	}
	public boolean updatePasswd(String userID,String newPwd) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		URL url = new URL("http://"+hostURL+"users/"+userID);
        String content = "{\"user\":{\"password\":\""+md5Method(newPwd)+"\"}}";
        HttpResponse hrp =hcu.doPatch(url.toString(), content);
        if((hrp.getStatusLine().getStatusCode())==200){
	    	return true;
		}else{
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
		}
}
	
	
	public boolean grantProjectRoleToUser(String userID,String projecteID,String roleID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
	
					URL url = new URL("http://"+hostURL+"projects/"+projecteID+"/users/"+userID+"/roles/"+roleID);
					HttpResponse hrp =hcu.doPut(url.toString());
				    if((hrp.getStatusLine().getStatusCode()) != 204){
			        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
						
				    }
				   return true;
				
	}
	public boolean removeProjectRoleToUser(String userID,String projecteID,String roleID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
					URL url = new URL("http://"+hostURL+"projects/"+projecteID+"/users/"+userID+"/roles/"+roleID);
					HttpResponse hrp =hcu.doDelete(url.toString());
				    if((hrp.getStatusLine().getStatusCode()) != 204){
			        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
						
				    }
				   return true;
				
	}

	public String createGroup(KeystoneGroupEntity kge) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{
		
			URL url = new URL("http://"+hostURL+"groups");
	        String content = "{\"group\":{\"description\":\""+kge.getGroupDescription()+"\",\"domain_id\":\""+domainId+"\",\"name\":\""+kge.getGroupName()+"\"}}";
	        HttpResponse hrp =hcu.doPost(url.toString(), content);
	        if((hrp.getStatusLine().getStatusCode())!=201){
	        	throw new IOException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
	        }
		    
	        String temp = EntityUtils.toString(hrp.getEntity());
	        EntityUtils.consume(hrp.getEntity());
			return temp;
	}
	public boolean insertUserToGroup(String userID,String groupID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		
			URL url = new URL("http://"+hostURL+"groups/"+groupID+"/users/"+userID);
			HttpResponse hrp =hcu.doPut(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 204){
		    	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
		   return true;
		
	
	}
	public boolean deleteUserToGroup(String userID,String groupID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			URL url = new URL("http://"+hostURL+"groups/"+groupID+"/users/"+userID);
			HttpResponse hrp =hcu.doDelete(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 204){
		    	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
		  
		   return true;
		
	
	}
	
	public boolean grantProjectRoleToGroup(String groupID,String projecteID,String roleID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
					URL url = new URL("http://"+hostURL+"projects/"+projecteID+"/groups/"+groupID+"/roles/"+roleID);
					HttpResponse hrp =hcu.doPut(url.toString());
				    if((hrp.getStatusLine().getStatusCode()) != 204){
				    	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
					 }
				   return true;
				
	}
	
	public boolean removeProjectRoleToGroup(String groupID,String projecteID,String roleID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
					URL url = new URL("http://"+hostURL+"projects/"+projecteID+"/groups/"+groupID+"/roles/"+roleID);
					HttpResponse hrp =hcu.doDelete(url.toString());
				    if((hrp.getStatusLine().getStatusCode()) != 204){
				    	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
					 }
				   return true;
				
	}
	
	public boolean createRole(String roleName) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			URL url = new URL("http://"+hostURL+"roles");
		    
	        String content = "{\"role\": {\"name\": \""+roleName+"\"}}";
	         HttpResponse hrp =hcu.doPost(url.toString(), content);
	        if((hrp.getStatusLine().getStatusCode())!=201){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
	        }
				return true;
			
	}
	
	public boolean grantDomainRoleToUser(String userID,String roleID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		
		URL url = new URL("http://"+hostURL+"domains/"+domainId+"/users/"+userID+"/roles/"+roleID);
		HttpResponse hrp =hcu.doPut(url.toString());
	    if((hrp.getStatusLine().getStatusCode()) != 204){
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
			
	    }
	   return true;
	
	}
	
	public boolean deleteDomainRoleToUser(String userID,String roleID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		
		URL url = new URL("http://"+hostURL+"domains/"+domainId+"/users/"+userID+"/roles/"+roleID);
		HttpResponse hrp =hcu.doDelete(url.toString());
	    if((hrp.getStatusLine().getStatusCode()) != 204){
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
		}
	   return true;
	
	}
	
	
	public boolean check(String userID,String projectID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		
		URL url = new URL("http://"+hostURL+"role_assignments?user.id="+userID+"&scope.project.id="+projectID);
		HttpResponse hrp =hcu.doGet(url.toString());
	    if((hrp.getStatusLine().getStatusCode()) != 200){
        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
			
	    }
	    JSONObject jsonObj =new JSONObject().fromObject(EntityUtils.toString(hrp.getEntity()));

        EntityUtils.consume(hrp.getEntity());
	     if((jsonObj.getJSONArray("role_assignments")).size()>0) {
	    	   	
	    	   		return true;
	    	   	}else{
	    	   		return false;
	    	   	}
				
	    	   	
		    	
		    	
	    	 
	
	}
	
	
	
	
	
	
}
