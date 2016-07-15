package com.troy.webservice;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.deser.std.StdDeserializer.StackTraceElementDeserializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.troy.dao.UserDAO;
import com.troy.entity.KeystoneGroupEntity;
import com.troy.entity.KeystoneProjectEntity;
import com.troy.entity.KeystoneUserEntity;
import com.troy.entity.User;
import com.troy.entity.UserSession;
import com.troy.sessionManager.UserSessionManager;

import javax.mail.*;

/**
 * 该类包涵了所有相关用户的操作，包括用户登录，多租户信息管理，密码管理，获取用户详情和组相关管理等
 * 该类下的接口均以/user开头
 * @author qq422
 * @version 2.1.1
 * @since <b>2015.10.09</b>
 */
@Controller
@RequestMapping(value = "/user")
public class UserService {
	/*
	 * 用户管理的对象，由spring加载，里面包含了已登录用户，角色以及权限的所有信息。
	 * 
	 */
	UserSessionManager userSessionManager;
	static Logger logger = Logger.getLogger (UserService.class.getName());

	
	public UserSessionManager getUserSessionManager() {
		return userSessionManager;
	}


	public void setUserSessionManager(UserSessionManager userSessionManager) {
		this.userSessionManager = userSessionManager;
	}

	/**
	 * 用户带token以及userId验证登录。
	 * 该接口会读取用户的所有权限并保存在userSessionManager对象中，有效信息保存两小时（距最后一次操作）
	 * 登录时期内若有权限变更不一定会立刻生效，如果想要立即生效需要重新登录
	 * @param response
	 * @param userID
	 * @param token
	 */
	@RequestMapping(value = "/login/{userID}/{token}",method = RequestMethod.GET)
	public void loginByToken(HttpServletResponse response,@PathVariable("userID") String userID,@PathVariable("token") String token){
			response.setContentType("application/json;charset=utf-8");
			try {
				if (userSessionManager.UserLogin(userID, token)){
					String jsonData =((UserSession)userSessionManager.getMapSession().get(token)).getJson();
					logger.info("用户"+userID+"登录成功,token为"+token);
					response.getWriter().write(jsonData);
				}else{
					logger.error("用户"+userID+"登录失败,token与用户名不符合");
					throw new AuthenticationException("token与用户名不符合");
				}
			} catch (KeyManagementException | NoSuchAlgorithmException
					 | IOException | HttpException e) {
				StringBuffer stringBuffer = new StringBuffer();
				for (StackTraceElement element : e.getStackTrace()) {
					stringBuffer.append("\t"+element+"\n");
				}
				logger.error(e.getMessage()+"\n"+stringBuffer.toString());
				try {
					response.setStatus(400);
					response.getWriter().write(e.getMessage());	
				} catch (IOException e1)  {
					e1.printStackTrace();
				}
			}
	}
	
	
	
	
	/**
	 * 获取指定用户的详情
	 * @param response
	 * @param userID
	 */
	@RequestMapping(value = "/getUser/{userID}",method = RequestMethod.GET)
	public void getSpecialUser(HttpServletResponse response,@PathVariable("userID") String userID) {
		
		response.setContentType("application/json;charset=utf-8");
		KeystoneUserEntity kue;
		try {
			kue = userSessionManager.getSpecialUser(userID);
			logger.info("获取用户 "+userID+" 详细信息");
			response.getWriter().write("{"+kue.getJson()+"}");
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	

	/**
	 * 获取指定用户名的详情
	 * @param response
	 * @param userID
	 */
	@RequestMapping(value = "/getUserByName/{userName}",method = RequestMethod.GET)
	public void getUserByName(HttpServletResponse response,@PathVariable("userName") String userName) {
		
		response.setContentType("application/json;charset=utf-8");
		KeystoneUserEntity kue;
		try {
			kue = userSessionManager.getUserByUserName(userName);
			logger.info("获取用户 "+userName+" 详细信息");
			response.getWriter().write("{"+kue.getJson()+"}");
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 通过组名获取组ID
	 * @param response
	 * @param userName
	 */
	@RequestMapping(value = "/getGroupByName/{groupName}",method = RequestMethod.GET)
	public void getGroupByName(HttpServletResponse response,@PathVariable("groupName") String groupName) {
		
		response.setContentType("application/json;charset=utf-8");
		KeystoneGroupEntity kue;
		try {
			kue = userSessionManager.getGroupByName(groupName);
			logger.info("获取组 "+groupName+" 详细信息");
			response.getWriter().write("{"+kue.getJson()+"}");
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 获取指定模型的详情
	 * @param response
	 * @param projectID
	 */
	@RequestMapping(value = "/getProject/{projectID}",method = RequestMethod.GET)
	public void getSpecialProject(HttpServletResponse response,@PathVariable("projectID") String projectID) {
		
		response.setContentType("application/json;charset=utf-8");
		KeystoneProjectEntity kue;
		try {
			kue = userSessionManager.getSpecialProject(projectID);
			logger.info("获取项目 "+projectID+" 详细信息");
			response.getWriter().write("{"+kue.getJson()+"}");
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 获取指定组的详情
	 * @param response
	 * @param groupID
	 */
	@RequestMapping(value = "/getGroup/{groupID}",method = RequestMethod.GET)
	public void getSpecialgroup(HttpServletResponse response,@PathVariable("groupID") String groupID) {
		response.setContentType("application/json;charset=utf-8");
		KeystoneGroupEntity kue;
		try {
			kue = userSessionManager.getSpecialGroup(groupID);
			logger.info("获取组 "+groupID+" 详细信息");
			response.getWriter().write("{"+kue.getJson()+"}");
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 将某个用户添加进组，该操作必须由管理员发起，不能由组内成员发起
	 * @param response
	 * @param groupID
	 * @param token 必须是admin权限
	 * @param UserID
	 */
	@RequestMapping(value = "/insertUserToGroup/{token}/{UserID}/{GroupID}",method = RequestMethod.GET)
	public void insertUserToGroup(HttpServletResponse response,@PathVariable("GroupID") String groupID,@PathVariable("token") String token,@PathVariable("UserID") String UserID) {
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.insertUserToGroup(groupID, token, UserID);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				throw new AuthenticationException("用户权限不足");
			}else if (temp.equals("404")){
				throw new AuthenticationException("用户未登录或者token已失效");
			}else{
				throw new RuntimeException("服务器内部错误");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 将用户从指定组删除，该操作必须由管理员发起，不能由组内成员发起
	 * @param response
	 * @param groupID
	 * @param token 必须admin权限
	 * @param UserID
	 */
	@RequestMapping(value = "/deleteUserToGroup/{token}/{UserID}/{GroupID}",method = RequestMethod.DELETE)
	public void DeleteUserToGroup(HttpServletResponse response,@PathVariable("GroupID") String groupID,@PathVariable("token") String token,@PathVariable("UserID") String UserID) {
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.DeleteUserToGroup(groupID, token, UserID);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				throw new AuthenticationException("用户权限不足");
			}else if (temp.equals("404")){
				throw new AuthenticationException("用户未登录或者token已失效");
			}else{
				throw new RuntimeException("服务器内部错误");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * 用户登出，token直接从userSessionManager对象中移除，所有相关信息全部失效
	 * @param response
	 * @param token
	 */
	@RequestMapping(value = "/loginOut/{token}",method = RequestMethod.GET)
	public void loginOut(HttpServletResponse response,HttpServletRequest request,@PathVariable("token") String token) {
		try {
			response.setContentType("application/json;charset=utf-8");
			if(userSessionManager.getMapSession().containsKey(token)){
				userSessionManager.getMapSession().remove(token);
				logger.info("token"+token+"登出成功");
				response.getWriter().write("{\"status\":\"success\"}");
			}else
				throw new RuntimeException("用户未登录,token:"+token+",IP"+request.getRemoteHost());
			
		} catch (Exception e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	
	
	}
	
	/**
	 * 创建组，一个公司拥有一个组，组内成员可以共享模型的使用权限，组内成员管理统一由管理员操作，包括添加、删除
	 * @param response
	 * @param token 必须是admin权限
	 * @param kge
	 */
	@RequestMapping(value = "/creategroup/{token}",method = RequestMethod.POST)
	public void creategroup(HttpServletResponse response,@PathVariable("token") String token,@RequestBody KeystoneGroupEntity kge) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.createGroup(token, kge);
			if (temp.equals("401")){
				throw new AuthenticationException("用户权限不足");
			}else if (temp.equals("404")){
				throw new AuthenticationException("用户未登录或者token已失效");
			}else{
				response.getWriter().write(temp);
				
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	/**
	 * 如果用户忘记密码有两种方法可以重置密码，第一种发送验证码邮件，通过核对验证码获得重置密码的权限，第二种为通知管理员，由管理员直接将密码设置为默认密码123456
	 * 该接口为第一种方法的第一步，输入用户名，系统会自动向注册时填写的邮箱发送验证码
	 * @param response 200 验证码已经成功发出
	 * @param userName
	 */
	@RequestMapping(value = "/sendChangePasswordMail/{userName}",method = RequestMethod.POST)
	public void sendChangePasswordMail(HttpServletResponse response,@PathVariable("userName") String userName) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			userSessionManager.sendChangePasswordMail(userName);
			response.getWriter().write("{\"status\":\"success\"}");
		} catch ( MessagingException | KeyManagementException | NoSuchAlgorithmException  | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 该接口为重置密码的第一种方法的第二步，输入用户名、邮件里的验证码、新密码。
	 * 若验证码正确则会将密码设置为新密码，完成密码重置功能
	 * @param response
	 * @param userName
	 * @param identifyingCode 通过邮件发送的验证码
	 * @param newPassword
	 */
	@RequestMapping(value = "/changePasswordIdentifying/{userName}/{identifyingCode}/{newPassword}",method = RequestMethod.POST)
	public void changePasswordIdentifying(HttpServletResponse response,@PathVariable("userName") String userName,@PathVariable("identifyingCode") int identifyingCode,@PathVariable("newPassword") String newPassword) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			int temp=userSessionManager.changePasswordIdentifying(userName,identifyingCode,newPassword);
			switch(temp){
			case 200:
				logger.info("用户"+userName+"重置密码成功！");
				response.getWriter().write("{\"status\":\"success\"}");
				break;
			case 400:
				logger.info("用户"+userName+"重置密码失败：用户未发起重置请求");
				throw new RuntimeException("用户"+userName+"重置密码失败：用户未发起重置请求");
			case 403:
				logger.info("用户"+userName+"重置密码失败：验证码不正确");
				throw new AuthenticationException("用户"+userName+"重置密码失败：验证码不正确");
			default:
				logger.info("用户"+userName+"重置密码失败");
				throw new AuthenticationException("用户"+userName+"重置密码失败");
			}
		} catch (HttpException | KeyManagementException | NoSuchAlgorithmException | IOException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 该接口为重置密码的第二种方法，由管理员直接将密码设置为默认密码123456
	 * @param response
	 * @param token 必须有admin权限
	 * @param userName 需要重置密码的用户通知管理员
	 */
	@RequestMapping(value = "/adminChangePassword/{token}/{userName}",method = RequestMethod.PUT)
	public void adminChangePassword(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userName") String userName) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			int temp=userSessionManager.adminChangePassword(token, userName);
			switch(temp){
			case 200:
				response.getWriter().write("{\"status\":\"success\"}");
				break;
			case 403:
				throw new AuthenticationException("用户权限不足");
			case 404:
				throw new AuthenticationException("用户未登录或者token已失效");
			default:
				throw new RuntimeException("服务器内部错误");
			}
		} catch (HttpException | KeyManagementException | NoSuchAlgorithmException | IOException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.setStatus(400);
				response.getWriter().write(e.getMessage());	
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
