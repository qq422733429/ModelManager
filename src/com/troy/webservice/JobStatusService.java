package com.troy.webservice;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.troy.dao.ExecuteDAO;
import com.troy.dao.ModelDAO;
import com.troy.entity.Execute;
import com.troy.entity.Frequence;
import com.troy.entity.Model;
import com.troy.entity.UserSession;
import com.troy.sessionManager.JobStatusManager;
import com.troy.sessionManager.UserSessionManager;

/**
 * 该类包涵了所有调度相关的操作，包括开始调度等
 * 该类下的接口均以/authority开头
 * @author qq422
 * @version 2.1.0
 * @since <b>2015.10.09</b>
 */

@Controller
@RequestMapping(value = "/execute")
public class JobStatusService {
	/*
	 * 连接job管理表工具，已废弃
	 */
	ExecuteDAO executeStatusDAO = new ExecuteDAO();
	/*
	 * 登录用户多租户管理对象，有spring加载
	 */
	UserSessionManager userSessionManager;
	/*
	 * 调度服务器地址，有spring加载
	 */
	String oozie;
	/*
	 * 模型管理表工具类，有spring加载
	 */
	ModelDAO modelDAO;
	/*
	 * 调度服务器相关接口对象，由spring加载
	 */
	JobStatusManager jobStatusManager;
	static Logger logger = Logger.getLogger (JobStatusService.class.getName());
	

	public JobStatusManager getJobStatusManager() {
		return jobStatusManager;
	}

	public void setJobStatusManager(JobStatusManager jobStatusManager) {
		this.jobStatusManager = jobStatusManager;
	}

	public UserSessionManager getUserSessionManager() {
		return userSessionManager;
	}

	public void setUserSessionManager(UserSessionManager userSessionManager) {
		this.userSessionManager = userSessionManager;
	}

	public String getOozie() {
		return oozie;
	}

	public void setOozie(String oozie) {
		this.oozie = oozie;
	}

	public ModelDAO getModelDAO() {
		return modelDAO;
	}

	public void setModelDAO(ModelDAO modelDAO) {
		this.modelDAO = modelDAO;
	}
	
	/**
	 * 该接口接收作业执行完毕后返回的执行结果状态，1表示执行成功，2表示作业执行失败，3表示作业被killed，由模型服务器不再管理作业信息，该接口已经被废弃
	 * @param response
	 * @param executeStatus
	 */
	@Deprecated
	@RequestMapping(value = "/JobReport",method = RequestMethod.POST)
	public void JobReport(HttpServletResponse response,@RequestBody Execute executeStatus) {
		System.out.println("普通作业回复");
		response.setContentType("application/json;charset=utf-8");
		System.out.println(executeStatus.getExecuteId());
		try {
			Execute us = (Execute)executeStatusDAO.findById(executeStatus.getExecuteId());
			
				us.setStatus(executeStatus.getStatus());
				us.setEndTime(executeStatus.getEndTime());
				
				try {
					if(!executeStatus.getExecuteTime().equals(null))
						us.setExecuteTime(executeStatus.getExecuteTime());
				} catch (Exception e) {
					
				}
					
				us.setErrormessage(executeStatus.getErrormessage());
				if(executeStatusDAO.save(us)){
					response.getWriter().write("{\"status\":\"success\"}");
				}else{
					throw new RuntimeException("服务器内部错误");
				}
			
		} catch (IOException e) {
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
	 * 该接口接收作业解释完毕后回复的解释状态，status解释成功为3，失败和被killed为22，需要用户重新上传kjb，开始新的解释作业
	 * @param response
	 * @param executeStatus
	 */
	@RequestMapping(value = "/transCopyReport",method = RequestMethod.POST)
	public void transCopyReport(HttpServletResponse response,@RequestBody Execute executeStatus) {
		Model model;
		response.setContentType("application/json;charset=utf-8");
		try {
			model = modelDAO.findById(executeStatus.getModelId());
			if(executeStatus.getStatus().equals(1)){
				model.setStatus(3);
				modelDAO.save(model);
				logger.info("翻译项目"+model.getModelName()+"成功");
				response.getWriter().write("{\"status\":\"success\"}");
			}else if(executeStatus.getStatus().equals(2)){
				model.setStatus(22);
				modelDAO.save(model);
				logger.info("翻译项目"+model.getModelName()+"失败");
				response.getWriter().write("{\"status\":\"success\"}");
			}else if(executeStatus.getStatus().equals(3)){
				model.setStatus(22);
				modelDAO.save(model);
				logger.info("翻译项目"+model.getModelName()+"被杀死");
				response.getWriter().write("{\"status\":\"success\"}");
			}else{
				throw new RuntimeException("服务器内部错误");
			}
		} catch (IOException e) {
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
	 * 该接口提供用户定时自己的模型开始执行，模型必须解释成功，即status为3，并会同时返回jobid，此处不再对jobid进行管理，返回给用户后丢弃
	 * @param response
	 * @param token
	 * @param modelId
	 * @param time 具体格式由调度服务器给出，模型管理服务器不会做修改，直接发个调度服务器
	 */
	@RequestMapping(value = "/SetTimeJobStart/{token}/{modelId}/{time}",method = RequestMethod.PUT)
	public void SetTimeJobStart(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("modelId") String modelId,@PathVariable("time") String time) {
		response.setContentType("application/json;charset=utf-8");
		try {
			if (userSessionManager.mapSession.containsKey(token)){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
				if (us.getMapMyProjects().containsKey(modelId)) {
					Model model = modelDAO.findById(modelId);
					
					String temp="";
					if (model.getStatus().equals(3)) {
						temp = jobStatusManager.startSetTimeJob(oozie, us.getKue().getUserID(),model.getHdfsLocation(),time,model.getModelId(),model.getModelName());
						logger.info("用户"+us.getKue().getUserName()+"调度定时作业"+model.getModelName()+"成功,作业ID："+temp);
						response.getWriter().write("{\"status\":\"success\",\"jobID\":\""+temp+"\"}");
					}else{
						logger.info("用户"+us.getKue().getUserName()+"调度定时作业"+model.getModelName()+"失败：模型未编译成功");
						throw new RuntimeException("模型正在编译，请稍后调度!");
					}
					
				}else{
					logger.info("用户"+us.getKue().getUserName()+"调度定时作业失败：无权限调度模型");
					throw new AuthenticationException("用户权限不足!");
				}
			}else{
				logger.info("用户调度定时作业失败：未登录成功");
				throw new AuthenticationException("用户未登录或者token已失效");
			}
		} catch ( IOException | KeyManagementException | NoSuchAlgorithmException  e) {
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
	 * 该接口提供用户周期调度自己的模型开始执行，模型必须解释成功，即status为3，并会同时返回jobid，此处不再对jobid进行管理，返回给用户后丢弃
	 * @param response
	 * @param token
	 * @param frequence
	 */
	@RequestMapping(value = "/frequeceJobStart/{token}",method = RequestMethod.POST)
	public void frequeceJobStart(HttpServletResponse response,@PathVariable("token") String token,@RequestBody Frequence frequence) {
		response.setContentType("application/json;charset=utf-8");
		try {
			if (userSessionManager.mapSession.containsKey(token)){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
				if (us.getMapMyProjects().containsKey(frequence.getModelId())) {
					Model model = modelDAO.findById(frequence.getModelId());
					
					String temp="";
					if (model.getStatus().equals(3)) {
						temp = jobStatusManager.startFrequenceJob(oozie, us.getKue().getUserID(), model.getHdfsLocation(), model.getModelName(), model.getModelId(), frequence);
						logger.info("用户"+us.getKue().getUserName()+"调度周期作业"+model.getModelName()+"成功,作业ID："+temp);
						response.getWriter().write("{\"status\":\"success\",\"jobID\":\""+temp+"\"}");
					}else{
						logger.info("用户"+us.getKue().getUserName()+"调度周期作业"+model.getModelName()+"失败：模型未编译成功");
						throw new RuntimeException( "模型编译出错！");
					}
					
				}else{
					logger.info("用户"+us.getKue().getUserName()+"调度周期作业失败：无权限调度模型");
					throw new AuthenticationException( "用户权限不足!");
				}
			}else{
				logger.info("用户调度周期作业失败：未登录成功");
				throw new AuthenticationException( "用户未登录或者token已失效");
			}
		} catch ( IOException | KeyManagementException | NoSuchAlgorithmException  e) {
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
	 * 该接口提供用户调度自己的模型开始执行，模型必须解释成功，即status为3，并会同时返回jobid，此处不再对jobid进行管理，返回给用户后丢弃
	 * @param response
	 * @param token
	 * @param createUserId
	 * @param modelId
	 */
	@RequestMapping(value = "/JobStart/{token}/{createUserId}/{modelId}",method = RequestMethod.POST,produces="application/json")
	public void JobStart(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("createUserId") String createUserId,@PathVariable("modelId") String modelId) {
		response.setContentType("application/json;charset=utf-8");
		try {
			
			if (userSessionManager.mapSession.containsKey(token)){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
				if (us.getMapMyProjects().containsKey(modelId)) {
					Model model = modelDAO.findById(modelId);
					
					String temp="";
					if (model.getStatus().equals(3)) {
						temp = jobStatusManager.startJob(oozie, us.getKue().getUserID(),
								model.getHdfsLocation(),modelId,model.getModelName());
						logger.info("用户"+us.getKue().getUserName()+"调度普通作业"+model.getModelName()+"成功，jobid: "+temp);
						response.getWriter().write("{\"status\":\"success\",\"jobID\":\""+temp+"\"}");
					}else if(model.getStatus().equals(2)){
						logger.info("用户"+us.getKue().getUserName()+"调度普通作业"+model.getModelName()+"失败：模型编译未完成");
						throw new RuntimeException("模型正在编译，请稍后调度 !");
					}else{
						logger.info("用户"+us.getKue().getUserName()+"调度普通作业"+model.getModelName()+"失败：模型编译失败");
						throw new RuntimeException("模型编译失败");
					}
				}else{
					logger.info("用户"+us.getKue().getUserName()+"调度普通作业失败：无权限调度模型");
					throw new AuthenticationException("用户权限不足!");
				}
			}else{
				logger.info("用户调度普通作业失败：未登录成功");
				throw new AuthenticationException( "用户未登录或者token已失效");
			}
		} catch ( Exception  e) {
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
	 * 该接口用于杀死正在执行的作业，由于现在不知道jobID与用户的对应值，所以不能进行权限验证，该接口会直接将jobid发送给调度服务器，调度服务器会杀死jobid对应的作业，有一定风险未解决
	 * @param response
	 * @param token  
	 * @param jobId 此处jobid是由调度服务器提供
	 */
	@RequestMapping(value = "/killJob/{token}/{jobId}",method = RequestMethod.PUT)
	public void killJob(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("jobId") String jobId) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			
			if (userSessionManager.mapSession.containsKey(token)){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				
				switch(jobStatusManager.killJob(oozie, jobId)){
					case 200:
						logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"kill作业"+jobId+"成功");
						response.getWriter().write("{\"status\":\"success\"}");
						break;
					default:
						throw new HttpException();
				}
			}else{
				logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"kill作业"+jobId+"失败：用户未登录");
				throw new AuthenticationException("用户未登录或者token已失效");
			}
		} catch ( IOException | KeyManagementException | NoSuchAlgorithmException | HttpException e) {
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
	 * 该接口返回所有用户已执行和正在执行和执行失败的作业列表，现在作业信息不在模型管理服务器管理，该接口已废弃
	 * @param response
	 * @param token
	 * @return List<Execute>
	 */
	@Deprecated
	@RequestMapping(value = "/getMyJobList/{token}",method = RequestMethod.GET)
	public @ResponseBody List<Execute> getMyJobList(HttpServletResponse response,@PathVariable("token") String token) {
		
		response.setContentType("application/json;charset=utf-8");
		
			try {
				if (userSessionManager.mapSession.containsKey(token)){
					GregorianCalendar gc = new GregorianCalendar();
					gc.setTime(new Date());
					gc.add(Calendar.HOUR_OF_DAY, 2);
					((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
					UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
					List<Execute> list = executeStatusDAO.findByCreateUserId(us.getKue().getUserID());
					return list;
				}else{
					throw new RuntimeException();
				}
			} catch (Exception e) {
				StringBuffer stringBuffer = new StringBuffer();
				for (StackTraceElement element : e.getStackTrace()) {
					stringBuffer.append("\t"+element+"\n");
				}
				logger.error(e.getMessage()+"\n"+stringBuffer.toString());
				return null;
			}
	}
	/**
	 * 暂停作业
	 * @param response
	 * @param token
	 * @param jobId
	 */
	@RequestMapping(value = "/suspendJob/{token}/{jobId}",method = RequestMethod.PUT)
	public void suspendJob(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("jobId") String jobId) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			
			if (userSessionManager.mapSession.containsKey(token)){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				
				switch(jobStatusManager.suspendJob(oozie, jobId)){
					case 200:
						logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"suspend作业"+jobId+"成功");
						response.getWriter().write("{\"status\":\"success\"}");
						break;
					default:
						throw new HttpException();
				}
			}else{
				logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"suspend作业"+jobId+"失败：用户未登录");
				throw new AuthenticationException( "用户未登录或者token已失效");
			}
		} catch ( IOException | KeyManagementException | NoSuchAlgorithmException | HttpException e) {
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
	 * 恢复作业继续执行
	 * @param response
	 * @param token
	 * @param jobId
	 */
	@RequestMapping(value = "/resumeJob/{token}/{jobId}",method = RequestMethod.PUT)
	public void resumeJob(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("jobId") String jobId) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			
			if (userSessionManager.mapSession.containsKey(token)){
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(new Date());
				gc.add(Calendar.HOUR_OF_DAY, 2);
				((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
				
				switch(jobStatusManager.resumeJob(oozie, jobId)){
					case 200:
						logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"恢复作业"+jobId+"成功");
						response.getWriter().write("{\"status\":\"success\"}");
						break;
					case 400:
						throw new HttpException("");
				}
			}else{
				logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"恢复作业"+jobId+"失败：用户未登录");
				throw new AuthenticationException("用户未登录或者token已失效");
			}
		} catch ( IOException | KeyManagementException | NoSuchAlgorithmException | HttpException e) {
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
