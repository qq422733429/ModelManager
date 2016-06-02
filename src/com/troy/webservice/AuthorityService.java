package com.troy.webservice;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.troy.dao.ApplyTableDAO;
import com.troy.dao.ApproveDAO;
import com.troy.entity.ApplyTable;
import com.troy.entity.Approve;
import com.troy.entity.PublicModel;
import com.troy.entity.UserSession;
import com.troy.sessionManager.ModelManager;
import com.troy.sessionManager.UserSessionManager;

@Controller
@RequestMapping(value = "/authority")
public class AuthorityService {
	/*
	 * 用户管理对象，由spring加载
	 */
	UserSessionManager userSessionManager;
	ApproveDAO approveDAO = new ApproveDAO();
	static Logger logger = Logger.getLogger (AuthorityService.class.getName());
	
	public UserSessionManager getUserSessionManager() {
		return userSessionManager;
	}

	public void setUserSessionManager(UserSessionManager userSessionManager) {
		this.userSessionManager = userSessionManager;
	}
	/**
	 * admin授权直接其他用户admin权限
	 * @param response
	 * @param token
	 * @param userID
	 */
	@RequestMapping(value = "/grantUserAdmin/{token}/{userID}",method = RequestMethod.PUT)
	public void grantUserAdmin(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userID") String userID) {
		System.out.println("分配admin");
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.grantUserAdmin(token, userID);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * admin批准他人的成为developer请求
	 * @param response
	 * @param token
	 * @param approve
	 */
	@RequestMapping(value = "/grantUserDeveloper/{token}",method = RequestMethod.POST)
	public void grantUserDeveloper(HttpServletResponse response,@PathVariable("token") String token,@RequestBody Approve approve) {
		System.out.println("批准成为developer");
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.grantUserDeveloper(token, approve);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 该接口提供给管理员，用户不需要自己再申请developer权限，管理员直接批准成为developer
	 * @param response
	 * @param token
	 * @param userId
	 */
	@RequestMapping(value = "/grantUserDeveloperWithoutApprove/{token}/{userId}",method = RequestMethod.PUT)
	public void grantUserDeveloperWithoutApprove(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userId") String userId) {
		System.out.println("直接批准成为developer");
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.grantUserDeveloperWithoutApprove(token, userId);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("403")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
	
	/**
	 * 管理员直接删除其他用户的developer权限
	 * @param response
	 * @param token
	 * @param userID
	 */
	@RequestMapping(value = "/deleteUserDeveloper/{token}/{userID}",method = RequestMethod.PUT)
	public void deleteUserDeveloper(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userID") String userID) {
		System.out.println("移除developer");
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.deleteUserDeveloper(token, userID);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * 该接口用于普通用户申请获得他人已经发布的模型，可以申请executer和writer权限
	 * @param response
	 * @param token 至少是developer权限
	 * @param modelId
	 * @param roleName 申请的权限名，executer或writer
	 */
	@RequestMapping(value = "/applyModel/{token}/{modelId}/{roleName}",method = RequestMethod.POST)
	 public void applyModel(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("modelId") String modelId,@PathVariable("roleName") String roleName) {
		 System.out.println("用户申请使用他人模型");
		 try {
			switch (userSessionManager.applyModelByOtherUser(token, modelId,roleName)) {
			case 201:
				response.getWriter().write("{\"status\":\"success\"}");
				break;
			case 400:
				response.sendError(response.SC_BAD_REQUEST, "the model has not publiced");
				break;
			case 404:
				response.sendError(response.SC_NOT_FOUND,"the user is offline");
				break;
			case 403:
				response.sendError(response.SC_FORBIDDEN, "the user have this model");
				break;
			case 500:
				response.sendError(response.SC_INTERNAL_SERVER_ERROR,"INTERNAL_SERVER_ERROR");
				break;

			default:
				response.sendError(response.SC_BAD_REQUEST, "unknown error");
				break;
			};
		}catch (Exception e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, "unknown error");
			} catch (IOException e1) {
				e1.printStackTrace();
				
			}
		}
		
	 }	 
	
	
	
	
	
	/**
	 * 该接口用于model的拥有者批准申请使用自己发布的model的请求
	 * @param response
	 * @param token model拥有者的token
	 * @param userID
	 * @param projectID
	 * @param roleName 批准的权限名，executer或writer
	 * @param id
	 */
	@RequestMapping(value = "/grantUserProject/{token}/{userID}/{projectID}/{roleName}/{id}",method = RequestMethod.PUT)
	public void grantUserProject(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userID") String userID,@PathVariable("projectID") String projectID,@PathVariable("roleName") String roleName,@PathVariable("id") int id) {
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.grantUserProject(token, userID, projectID, roleName,id);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else if (temp.equals("406")){
				response.sendError(response.SC_NOT_FOUND, "no role name is "+roleName);
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 该接口用于model的拥有者拒绝申请使用自己发布的model的请求
	 * @param response
	 * @param token
	 * @param applyTable
	 */
	@RequestMapping(value = "/disagreeApplyModel/{token}",method = RequestMethod.POST)
	public void disagreeApplyModel(HttpServletResponse response,@PathVariable("token") String token,@RequestBody ApplyTable applyTable) {
		System.out.println("拒绝项目权限");
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.disagreeApplyModel(token,applyTable);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else{
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, "SC_INTERNAL_SERVER_ERROR");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
	/**
	 * 该接口用于model的拥有者删除其他用户使用自己的模型的权限
	 * @param response
	 * @param token
	 * @param userID
	 * @param projectID
	 * @param roleName 删除的权限名，executer或writer
	 */
	@RequestMapping(value = "/deleteUserProject/{token}/{userID}/{projectID}/{roleName}",method = RequestMethod.DELETE)
	public void deleteUserProject(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userID") String userID,@PathVariable("projectID") String projectID,@PathVariable("roleName") String roleName) {
		System.out.println("删除项目权限");
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.deleteUserProject(token, userID, projectID, roleName);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else if (temp.equals("406")){
				response.sendError(response.SC_NOT_FOUND, "no role name is "+roleName);
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
	
	
	
	/**
	 * 该接口用于项目拥有者分配给某个组的项目的使用权限 
	 * @param response
	 * @param token
	 * @param groupID
	 * @param projectID
	 * @param roleName
	 */
	@RequestMapping(value = "/grantGroupProject/{token}/{groupID}/{projectID}/{roleName}",method = RequestMethod.PUT)
	public void grantGroupProject(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("groupID") String groupID,@PathVariable("projectID") String projectID,@PathVariable("roleName") String roleName) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.grantGroupProject(token, groupID, projectID, roleName);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else if (temp.equals("406")){
				response.sendError(response.SC_NOT_FOUND, "no role name is "+roleName);
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	/**
	 * 该接口用于项目拥有者删除分配给组的项目使用权限
	 * @param response
	 * @param token
	 * @param groupID
	 * @param projectID
	 * @param roleName
	 */
	@RequestMapping(value = "/deleteGroupProject/{token}/{groupID}/{projectID}/{roleName}",method = RequestMethod.DELETE)
	public void deleteGroupProject(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("groupID") String groupID,@PathVariable("projectID") String projectID,@PathVariable("roleName") String roleName) {
		response.setContentType("application/json;charset=utf-8");
		try {
			String temp =userSessionManager.deleteGroupProject(token, groupID, projectID, roleName);
			if (temp.equals("201")){
				response.getWriter().write("{\"status\":\"success\"}");
			}else if (temp.equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
			}else if (temp.equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
			}else if (temp.equals("406")){
				response.sendError(response.SC_NOT_FOUND, "no role name is "+roleName);
			}else{
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	
//	
//	@RequestMapping(value = "/checkUserRole/{token}/{userID}/{projectID}",method = RequestMethod.GET)
//	public void checkUserRole(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("userID") String userID,@PathVariable("projectID") String projectID) {
//	
//		response.setContentType("application/json;charset=utf-8");
//		try {
//			String temp =userSessionManager.checkUserRole(token, userID, projectID);
//			if (temp.equals("201")){
//				response.getWriter().write("{\"result\":\"true\"}");
//			}else if (temp.equals("401")){
//				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
//			}else if (temp.equals("404")){
//				response.sendError(response.SC_NOT_FOUND, "User is offline");
//			}else{
//				response.sendError(response.SC_BAD_REQUEST, "Internal error");
//			}
//		} catch (KeyManagementException | NoSuchAlgorithmException
//				 | IOException | HttpException e) {
//			e.printStackTrace();
//			try {
//				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//		}
//	}
	/**
	 * 该接口用于用户申请成为developer
	 * @param response
	 * @param token
	 */
	@RequestMapping(value = "/launchApprove/{token}",method = RequestMethod.GET)
	public void launchApprove(HttpServletResponse response,@PathVariable("token") String token) {
		System.out.println("申请成为developer");
		response.setContentType("application/json;charset=utf-8");
			try {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				if (userSessionManager.mapSession.containsKey(token)){
					UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
					Approve ad;
					try{
						ad = (Approve)approveDAO.findBySponsorId(us.getKue().getUserID()).get(0);
					}catch (Exception e){
						ad=null;
					}
					if(ad==null){
						if (!us.isIsdeveloper()) {
							Approve approve = new Approve(us.getKue().getUserID(),0);
							if (approveDAO.save(approve)) {
								response.getWriter().write("{\"status\":\"success\"}");
							}else{
								response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Internal error");
							}
						}else{
							response.sendError(response.SC_BAD_REQUEST, "User is already a developer");
						}
					}else{
						if(ad.getStatus()==1){
							if (!us.isIsdeveloper()) {
								Approve approve = new Approve(us.getKue().getUserID(),0);
								if (approveDAO.save(approve)) {
									response.getWriter().write("{\"status\":\"success\"}");
								}else{
									response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Internal error");
								}
							}else{
								response.sendError(response.SC_BAD_REQUEST, "User is already a developer");
							}	
						}else{
							response.sendError(response.SC_CONFLICT, "user has applied");
						}
						
						
					}
				}else{
					response.sendError(response.SC_NOT_FOUND, "User is offline");
				}
			} catch (IOException e) {
				StringBuffer stringBuffer = new StringBuffer();
				for (StackTraceElement element : e.getStackTrace()) {
					stringBuffer.append("\t"+element+"\n");
				}
				logger.error(e.getMessage()+"\n"+stringBuffer.toString());
				try {
					response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
					} catch (IOException e1) {
					e1.printStackTrace();
				}
				
			}
		
		
	}
	
	
	

}
