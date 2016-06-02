package com.troy.sessionManager;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PathVariable;

import com.troy.dao.ApplyTableDAO;
import com.troy.dao.ApproveDAO;
import com.troy.dao.BaseModelDAO;
import com.troy.dao.ChanePasswordCodeTableDAO;
import com.troy.dao.ModelDAO;
import com.troy.entity.ApplyTable;
import com.troy.entity.Approve;
import com.troy.entity.BaseModel;
import com.troy.entity.ChanePasswordCodeTable;
import com.troy.entity.KeystoneGroupEntity;
import com.troy.entity.KeystoneProjectEntity;
import com.troy.entity.KeystoneRoleEntity;
import com.troy.entity.KeystoneUserEntity;
import com.troy.entity.Model;
import com.troy.entity.ProjectRole;
import com.troy.entity.User;
import com.troy.entity.UserSession;
import com.troy.util.KeystoneAPI;
import com.troy.util.SessionManager;

public class UserSessionManager  {
	BaseModelDAO bmd = new BaseModelDAO();
	KeystoneAPI ka;
	public Map mapSession;
	ApproveDAO approveDAO = new ApproveDAO();
	ApplyTableDAO applyTableDAO=new ApplyTableDAO();
	ModelDAO modelDAO = new ModelDAO();
	public List<BaseModel> baseList;
	static Logger logger = Logger.getLogger (UserSessionManager.class.getName());
	


	public KeystoneAPI getKa() {
		return ka;
	}


	public void setKa(KeystoneAPI ka) {
		this.ka = ka;
	}
	
	
	
	
	public Map getMapSession() {
		return mapSession;
	}


	public void setMapSession(Map mapSession) {
		this.mapSession = mapSession;
	}
	
	public UserSessionManager(){
		mapSession = new HashMap<String,UserSession>();
		SessionManager t = new SessionManager(mapSession);
		baseList = bmd.findAll();
		t.start();
		File file =new File("kjbFolder");  
		if  (!file .exists()  && !file .isDirectory())      
		{
		    file.mkdir();    
		} 
		
	}
	
	
	/**
	 * 用户登录成功返回201
	 * @param uid
	 * @param token
	 * @return
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 */
	public String UserLogin(String uid,String token) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
			//验证用户的token是否正确，若不对则直接抛出异常
			KeystoneUserEntity kue = ka.checkLogin(token);
			if (kue.getUserID().equals(uid)){
				//验证用户是否已经登录
				if (!mapSession.containsKey(uid)) {
					UserSession us = new UserSession();
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(new Date());
					gc.add(Calendar.HOUR_OF_DAY, 2);
					us.setExpires_at(gc);
					us.setKue(kue);
					us.baseList=baseList;
					ka.getAllGrant(uid, us);
					mapSession.put(token, us);
					return "201";
				}else{
					return "201";
				}
			}else{
				return "404";
			}
			
			
		
	}
	
	
	

	public KeystoneUserEntity getSpecialUser(String userID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		return ka.getSpecialUser(userID);
	}
	public KeystoneUserEntity getUserByUserName(String userName) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException{
		return ka.findUserByUserName(userName);
	}
	public KeystoneGroupEntity getGroupByName(String name) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException{
		return ka.getGroupIDbyGroupName(name);
	}
	
	
	public KeystoneProjectEntity getSpecialProject(String projectID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		return ka.getSpecialProject(projectID);
	}
	
	public KeystoneGroupEntity getSpecialGroup(String groupID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		return ka.getSpecialGroup(groupID);
	}
	
	public String insertUserToGroup(String groupID,String token,String UserID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin())
				if(ka.insertUserToGroup(UserID, groupID)){
					logger.info("将用户"+UserID+"添加进组"+groupID+"成功");
					return "201";
				}
				else{
					logger.error("将用户"+UserID+"添加进组"+groupID+"失败");
					return "500";
				}
			else{
				logger.error("将用户"+UserID+"添加进组"+groupID+"失败：token用户"+((UserSession)mapSession.get(token)).getKue().getUserName()+"不是admin");
				return "401";
			}
			
		}else{
			logger.error("token"+token+"未登录");
			return "404";
			
		}
	}
	
	public String DeleteUserToGroup(String groupID,String token,String UserID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin())
				if(ka.deleteUserToGroup(UserID, groupID)){
					logger.info("将用户"+UserID+"从组"+groupID+"删除成功");
					return "201";
				}
				else{
					logger.error("将用户"+UserID+"从组"+groupID+"删除失败");
					return "500";
				}
			else{
				logger.error("将用户"+UserID+"从组"+groupID+"删除失败：token用户"+((UserSession)mapSession.get(token)).getKue().getUserName()+"不是admin");
				return "401";
			}
			
		}else{
			logger.error("token"+token+"未登录");
			return "404";
			
		}
	}
	
	
	
	
	public String grantUserAdmin(String token,String UserID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin())
				if(ka.grantDomainRoleToUser(UserID, ((KeystoneRoleEntity)ka.mapRoleByName.get("admin")).getRoleID()))
					return "201";
				else
					return "500";
			else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
	public String deleteUserAdmin(String token,String UserID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin())
				if(ka.deleteDomainRoleToUser(UserID,((KeystoneRoleEntity)ka.mapRoleByName.get("admin")).getRoleID()))
					return "201";
				else
					return "500";
			else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
	public String grantUserProject(String token,String UserID,String projectID,String role,int id) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).getMapMyProjects().containsKey(projectID)) {
				if (((ProjectRole)((UserSession)mapSession.get(token)).getMapMyProjects().get(projectID)).isOwner()) {
					String roleID;
					if (ka.mapRoleByName.containsKey(role)) {
						roleID = ((KeystoneRoleEntity) ka.mapRoleByName.get(role)).getRoleID();
					}else{
						return "406";
					}  
					if (role.equals("developer")||role.equals("executer")) {
						if (ka.grantProjectRoleToUser(UserID, projectID, roleID)){
							ApplyTable applyTable = applyTableDAO.findById(id);
							applyTable.setStatus(1);
							if (applyTableDAO.save(applyTable))
								return "201";
							else
								return "500";
						}
						else
							return "500";
					}else{
							return "401";
					}
				}else{
					return "401";
				}
			} else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	public String disagreeApplyModel(String token,ApplyTable applyTable) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
	
		String message = applyTable.getMessage();
		applyTable = applyTableDAO.findById(applyTable.getId());
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).getMapMyProjects().containsKey(applyTable.getModelId())) {
				applyTable.setStatus(2);
				applyTable.setMessage(message);
				if (applyTableDAO.save(applyTable))
					return "201";
				else
					return "500";
			} else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
	public String deleteUserProject(String token,String UserID,String projectID,String role) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).getMapMyProjects().containsKey(projectID)) {
				if (((ProjectRole)((UserSession)mapSession.get(token)).getMapMyProjects().get(projectID)).isOwner()) {
					String roleID;
					if (ka.mapRoleByName.containsKey(role)) {
						roleID = ((KeystoneRoleEntity) ka.mapRoleByName
								.get(role)).getRoleID();
					}else{
						return "406";
					}  
					if (role.equals("developer")||role.equals("executer")) {
						if (ka.removeProjectRoleToUser(UserID, projectID, roleID))
							return "201";
						else
							return "500";
					}else{
							return "401";
					}
				}else{
					return "401";
				}
			} else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
	
	
	public String grantGroupProject(String token,String GroupID,String projectID,String role) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).getMapMyProjects().containsKey(projectID)) {
				if (((ProjectRole)((UserSession)mapSession.get(token)).getMapMyProjects().get(projectID)).isOwner()) {
					String roleID;
					if (ka.mapRoleByName.containsKey(role)) {
						roleID = ((KeystoneRoleEntity) ka.mapRoleByName
								.get(role)).getRoleID();
					}else{
						return "406";
					} 
					if (role.equals("developer")||role.equals("executer")) {
						if (ka.grantProjectRoleToGroup(GroupID, projectID, roleID))
							return "201";
						else
							return "500";
					}else{
							return "401";
					}
				}else{
					return "401";
				}
			} else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
	public String deleteGroupProject(String token,String GroupID,String projectID,String role) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).getMapMyProjects().containsKey(projectID)) {
				if (((ProjectRole)((UserSession)mapSession.get(token)).getMapMyProjects().get(projectID)).isOwner()) {
					String roleID;
					if (ka.mapRoleByName.containsKey(role)) {
						roleID = ((KeystoneRoleEntity) ka.mapRoleByName
								.get(role)).getRoleID();
					}else{
						return "406";
					} 
					if (role.equals("developer")||role.equals("executer")) {
						if (ka.removeProjectRoleToGroup(GroupID, projectID, roleID))
							return "201";
						else
							return "500";
					}else{
							return "401";
					}
				}else{
					return "401";
				}
			} else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
//	
//	public String checkUserRole(String token,String UserID,String projectID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
//		if(token.equals(backDoor)){
//			if(ka.check(UserID, projectID)){
//				return "201";
//			}else{
//				return "404";
//			}
//				
//			
//		}else{
//			return "401";
//		}
//	}
	
	public String deleteUserDeveloper(String token,String UserID) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin())
				if(ka.deleteDomainRoleToUser(UserID, ((KeystoneRoleEntity)ka.mapRoleByName.get("developer")).getRoleID()))
					return "201";
				else
					return "500";
			else
				return "401";
		}else{
			return "404";
			
		}
	}
	
	
	
	
	
	public String grantUserDeveloper(String token,Approve approve) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin()){
				List temp = approveDAO.findBySponsorId(approve.getSponsorId());
				Approve item = (Approve)temp.get(temp.size()-1);
				item.setStatus(approve.getStatus());
				item.setApproverId(((UserSession)mapSession.get(token)).getKue().getUserID());
				item.setNotApprovedReason(approve.getNotApprovedReason());
				
				if(item.getStatus().equals(1)){
					if(ka.grantDomainRoleToUser(approve.getSponsorId(), ((KeystoneRoleEntity)ka.mapRoleByName.get("developer")).getRoleID())&&approveDAO.save(item)){
						return "201";
					}else
						return "500";
				}else{
					if(approveDAO.save(item))
						return "201";
					else
						return "500";
				}	
			}else
				return "401";
		}else{
			return "404";
			
		}
	}
	public String grantUserDeveloperWithoutApprove(String token,String userId) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin()){
				if(ka.grantDomainRoleToUser(userId, ((KeystoneRoleEntity)ka.mapRoleByName.get("developer")).getRoleID())){
						return "201";
				}else
						return "500";
			}else
				return "403";
		}else{
			return "404";
		}
	}
	
	public String createGroup(String token,KeystoneGroupEntity kge) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin())
				
					return ka.createGroup(kge);
				
			else
				return "401";
			
		}else{
			return "404";
			
		}
	}
	
	public int applyModelByOtherUser(String token,String modelId,String roleName){
		
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			Model model = modelDAO.findById(modelId);
			try{
				if(model.getIsPublic()){
					model.setIsPublic(model.getIsPublic());
				}
					
			}catch (NullPointerException e){
				model.setIsPublic(false);
			} 
			if(model.getIsPublic()){
				if(model.getCreateUserId()!=((UserSession)mapSession.get(token)).getKue().getUserID()){
					ApplyTable applyTable = new ApplyTable(((UserSession)mapSession.get(token)).getKue().getUserID(),model.getCreateUserId(), modelId, 0, "", roleName);
					if(applyTableDAO.save(applyTable)){
						return 201;
					}else
						return 500;
				}else{
					return 403;
				}	
			}else{
				return 400;
			}
		}else{
			return 404;
			
		}
	}
	
	public void sendChangePasswordMail(String userName) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException, MessagingException{
		KeystoneUserEntity kue = ka.findUserByUserName(userName);
		int num=0;
		do{
			num = RandomUtils.nextInt(999999);
		}while(num<=99999);
		
		ChanePasswordCodeTableDAO chanePasswordCodeTableDAO = new ChanePasswordCodeTableDAO();
		ChanePasswordCodeTable chanePasswordCodeTable=chanePasswordCodeTableDAO.findById(kue.getUserID());
		try {
			if(chanePasswordCodeTable.equals(null)){
				chanePasswordCodeTable = new ChanePasswordCodeTable(kue.getUserID(), num);
			}
		} catch (Exception e) {
			chanePasswordCodeTable = new ChanePasswordCodeTable(kue.getUserID(), num);
		}
		chanePasswordCodeTable.setCode(num);
		chanePasswordCodeTableDAO.save(chanePasswordCodeTable);
		 // 配置发送邮件的环境属性
        final Properties props = new Properties();
        /*
         * 可用的属性： mail.store.protocol / mail.transport.protocol / mail.host /
         * mail.user / mail.from
         */
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.port", "587");
        // 发件人的账号
        props.put("mail.user", "422733429@qq.com");
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", "wervdowxoribbjgj");

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人
        InternetAddress to = new InternetAddress(kue.getEmail());
        message.setRecipient(RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject("troy AI 用户密码修改");

        // 设置邮件的内容体
        message.setContent("您修改密码的验证码："+chanePasswordCodeTable.getCode()+"，如果不是您本人发起的密码修改服务，请忽略该邮件。", "text/html;charset=UTF-8");

        // 发送邮件
        Transport.send(message);
	}
	
	public int changePasswordIdentifying(String userName,int code,String newPassword) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException {
		KeystoneUserEntity kue = ka.findUserByUserName(userName);
		
		ChanePasswordCodeTableDAO chanePasswordCodeTableDAO = new ChanePasswordCodeTableDAO();
		ChanePasswordCodeTable chanePasswordCodeTable=chanePasswordCodeTableDAO.findById(kue.getUserID());
		if(chanePasswordCodeTable!=null){
			if (code==chanePasswordCodeTable.getCode()) {
				if(ka.updatePasswd(kue.getUserID(), newPassword)){
					chanePasswordCodeTableDAO.delete(chanePasswordCodeTable);
					return 200;
				}else{
					return 500;
				}
			}else{
				return 403;
			}
		}else{
			return 400;
		}
		
		
	}
	
	public int adminChangePassword(String token,String userName) throws KeyManagementException, NoSuchAlgorithmException, IOException, HttpException{
		KeystoneUserEntity kue = ka.findUserByUserName(userName);
		if (mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)mapSession.get(token)).setExpires_at(gc);
			if(((UserSession)mapSession.get(token)).isIsadmin()){
				if(ka.updatePasswd(kue.getUserID(), "123456")){
					return 200;
				}else{
					return 500;
				}
			}else{
				return 403;
			}
		}else{
			return 404;
		}
			
		
		
	}
	
	
}
