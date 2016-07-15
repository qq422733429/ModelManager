package com.troy.sessionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.naming.NamingException;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jspsmart.upload.Files;
import com.troy.dao.BaseModelDAO;
import com.troy.dao.ModelDAO;
import com.troy.dao.ModelTypeDAO;
import com.troy.dao.PublicModelDAO;
import com.troy.entity.BaseModel;
import com.troy.entity.Execute;
import com.troy.entity.FindModelList;
import com.troy.entity.FindSubModelEntity;
import com.troy.entity.KeystoneProjectEntity;
import com.troy.entity.KeystoneRoleEntity;
import com.troy.entity.Model;
import com.troy.entity.ModelType;
import com.troy.entity.ProjectRole;
import com.troy.entity.PublicModel;
import com.troy.entity.UserSession;
import com.troy.util.HadoopAPI;
import com.troy.util.KeystoneAPI;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ModelManager {
	ModelDAO modelDAO;
	UserSessionManager userSessionManager;
	HadoopAPI hadoopAPI;
	KeystoneAPI ka;
	String oozie;
	JobStatusManager jobStatusManager;
	PublicModelDAO publicModelDAO= new PublicModelDAO();
	BaseModelDAO baseModelDAO = new BaseModelDAO();
	static Logger logger = Logger.getLogger (ModelManager.class.getName());
	
	public JobStatusManager getJobStatusManager() {
		return jobStatusManager;
	}



	public void setJobStatusManager(JobStatusManager jobStatusManager) {
		this.jobStatusManager = jobStatusManager;
	}



	public String getOozie() {
		return oozie;
	}



	public void setOozie(String oozie) {
		this.oozie = oozie;
	}



	public KeystoneAPI getKa() {
		return ka;
	}



	public void setKa(KeystoneAPI ka) {
		this.ka = ka;
	}



	public ModelDAO getModelDAO() {
		return modelDAO;
	}



	public void setModelDAO(ModelDAO modelDAO) {
		this.modelDAO = modelDAO;
	}



	public HadoopAPI getHadoopAPI() {
		return hadoopAPI;
	}



	public void setHadoopAPI(HadoopAPI hadoopAPI) {
		this.hadoopAPI = hadoopAPI;
	}


	public UserSessionManager getUserSessionManager() {
		return userSessionManager;
	}



	public void setUserSessionManager(UserSessionManager userSessionManager) {
		this.userSessionManager = userSessionManager;
	}

	public FindModelList findModelList(FindModelList list) {
		for (FindSubModelEntity item : list.getList()) {
			Model model = modelDAO.findById(item.getModelId());
			item.setPath(model.getHdfsLocation());
		}
		logger.info("编译程序获取子项目的hdfs地址成功！");
		return list;
	}
	
	public int getOutput(Model request) {
		Model item = modelDAO.findById(request.getModelId());
		item.setOutputDir(request.getOutputDir());
		item.setInputDir(request.getInputDir());
		if (modelDAO.save(item)) {
			logger.info("翻译作业返回模型"+item.getModelName()+"输入输出文件夹地址成功");
			return 201;
		}else{
			logger.info("翻译作业返回模型"+item.getModelName()+"输入输出文件夹地址失败");
			return 400;
		}
	}
	
	public int getModelStatus(String token,String modelId) throws AuthenticationException {
		if (userSessionManager.mapSession.containsKey(token)){
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.getMapMyProjects().containsKey(modelId)) {
				if (((ProjectRole)us.getMapMyProjects().get(modelId)).isOwner()) {
					Model model = modelDAO.findById(modelId);
					return model.getStatus();
				}else{
					throw new AuthenticationException("user donot have Authentication");
				}
			}else{
				throw new AuthenticationException("user do not have this model");
			}
		}else{
			throw new AuthenticationException("the user is offline");
		}
	}
	
	public String getModelNameArg(String token,String modelId) throws DocumentException, IOException{
		
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			Model model = modelDAO.findById(modelId);
			if (us.getMapMyProjects().containsKey(modelId)) {
				
				if (((ProjectRole)us.getMapMyProjects().get(modelId)).isOwner()||((ProjectRole)us.getMapMyProjects().get(modelId)).isExecuter()||((ProjectRole)us.getMapMyProjects().get(modelId)).isWriter()) {
					File file = new File("kjbFolder\\"+modelId+"\\main.kjb");
					if(!file.exists()){
						logger.error("获取命名参数失败：找不到KJB文件");
						throw new IOException("找不到KJB文件");
					}
					SAXReader reader = new SAXReader();
			        Document document = reader.read(file);
			        List<Element> childList = document.getRootElement().element("parameters").elements();
			        JSONArray jsonArray = new JSONArray();
			        for (Element element : childList) {
			        	StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append("{\"name\":\""+element.elementText("name")+"\",");
						stringBuilder.append("\"default_value\":\""+element.elementText("default_value")+"\",");
						stringBuilder.append("\"description\":\""+element.elementText("description")+"\"}");
						jsonArray.add(new JSONObject().fromObject(stringBuilder.toString()));
					 }
			        logger.error("获取命名参数成功");
			        return jsonArray.toString(); 
				
				}else{
					logger.error("获取命名参数失败：权限不足");
					throw new AuthenticationException("获取命名参数失败：权限不足");
				}
			}else if (model.getIsPublic()) {
					File file = new File("kjbFolder\\"+modelId+"\\main.kjb");
					if(!file.exists()){
						logger.error("获取命名参数失败：找不到KJB文件");
						throw new IOException("找不到KJB文件");
					}
					SAXReader reader = new SAXReader();
			        Document document = reader.read(file);
			        List<Element> childList = document.getRootElement().element("parameters").elements();
			        JSONArray jsonArray = new JSONArray();
			        for (Element element : childList) {
			        	StringBuilder stringBuilder = new StringBuilder();
						stringBuilder.append("{\"name\":\""+element.elementText("name")+"\",");
						stringBuilder.append("\"default_value\":\""+element.elementText("default_value")+"\",");
						stringBuilder.append("\"description\":\""+element.elementText("description")+"\"}");
						jsonArray.add(new JSONObject().fromObject(stringBuilder.toString()));
					 }
			        logger.error("获取发布的命名参数成功");
			        return jsonArray.toString(); 
			}else{
				logger.error("获取命名参数失败：权限不足");
				throw new AuthenticationException("获取命名参数失败：权限不足");
			}
		}else{
			logger.error("获取命名参数失败：权限不足");
			throw new AuthenticationException("获取命名参数失败：权限不足");
		}
		
	}
	
	
	
	
	public Model createModel(String token,Model requestModel) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		
		Model model = new Model();
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			
			
			if(us.isIsdeveloper()) {
				KeystoneProjectEntity kpe = new KeystoneProjectEntity();
				kpe.setDescription(requestModel.getModelDescription());
				kpe.setEnable("true");
				kpe.setProjectName(requestModel.getModelName());
				String temp = ka.createProject(kpe, us.getKue().getUserID());
				String hdfsLocation;
				if (hadoopAPI.mkDir("/user/cdhfive/"+us.getKue().getUserID()+"/"+temp+"/")){
					hdfsLocation = "/user/"+hadoopAPI.getHdfsUserName()+"/"+us.getKue().getUserID()+"/"+temp+"/";
				}else{
					throw new IOException();
				}
				
				
				
				model = new Model(temp, requestModel.getExecuteType(), us.getKue().getUserID(), requestModel.getModelName(), requestModel.getModelDescription(), hdfsLocation, "", 0, requestModel.getVersion(), new Date(), new Date(), 0, temp, true, requestModel.getIconpath(), requestModel.getFunction(),"",false,"",requestModel.getType());
				
				if(modelDAO.save(model)){
					ProjectRole pr = new ProjectRole();
					pr.setOwner(true);
					KeystoneProjectEntity kpe2 = new KeystoneProjectEntity();
					kpe2.setProjectID(model.getModelId());
					kpe2.setProjectName(model.getModelName());
					kpe2.setDescription(model.getModelDescription());
					((UserSession)userSessionManager.getMapSession().get(token)).getMapMyProjects().put(kpe2.getProjectID(), pr);
					File file =new File("kjbFolder\\"+model.getModelId());  
					if  (!file .exists()  && !file .isDirectory())      
					{
					    file.mkdir();
					    File kjb =new File("kjbFolder\\"+model.getModelId()+"\\main.kjb");
					    if(kjb.createNewFile()){
					    	String kjbString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<job>\n<name>"+getUTF8String(model.getModelName()).toString()+"</name>\n<modelID>"+model.getModelId()+"</modelID>\n<description>"+getUTF8String(model.getModelDescription()).toString()+"</description>\n<extended_description>\n</extended_description>\n<job_version>1.0</job_version>\n<job_status>"+model.getType()+"</job_status>\n<directory>&#x2f;</directory>\n<created_user>\n</created_user>\n<created_date>\n</created_date>\n<modified_user>\n</modified_user>\n<modified_date>\n</modified_date>\n<parameters>\n</parameters>\n<slaveservers>\n</slaveservers>\n<job-log-table>\n<connection>troy_etl_log</connection>\n<schema/>\n<table>K_LOG_J</table>\n<size_limit_lines/>\n<interval/>\n<timeout_days/>\n<field>\n<id>ID_JOB</id>\n<enabled>Y</enabled>\n<name>ID_JOB</name>\n</field>\n<field>\n<id>CHANNEL_ID</id>\n<enabled>Y</enabled>\n<name>CHANNEL_ID</name>\n</field>\n<field>\n<id>JOBNAME</id>\n<enabled>Y</enabled>\n<name>JOBNAME</name>\n</field>\n<field>\n<id>STATUS</id>\n<enabled>Y</enabled>\n<name>STATUS</name>\n</field>\n<field>\n<id>LINES_READ</id>\n<enabled>Y</enabled>\n<name>LINES_READ</name>\n</field>\n<field>\n<id>LINES_WRITTEN</id>\n<enabled>Y</enabled>\n<name>LINES_WRITTEN</name>\n</field>\n<field>\n<id>LINES_UPDATED</id>\n<enabled>Y</enabled>\n<name>LINES_UPDATED</name>\n</field>\n<field>\n<id>LINES_INPUT</id>\n<enabled>Y</enabled>\n<name>LINES_INPUT</name>\n</field>\n<field>\n<id>LINES_OUTPUT</id>\n<enabled>Y</enabled>\n<name>LINES_OUTPUT</name>\n</field>\n<field>\n<id>LINES_REJECTED</id>\n<enabled>Y</enabled>\n<name>LINES_REJECTED</name>\n</field>\n<field>\n<id>ERRORS</id>\n<enabled>Y</enabled>\n<name>ERRORS</name>\n</field>\n<field>\n<id>STARTDATE</id>\n<enabled>Y</enabled>\n<name>STARTDATE</name>\n</field>\n<field>\n<id>ENDDATE</id>\n<enabled>Y</enabled>\n<name>ENDDATE</name>\n</field>\n<field>\n<id>LOGDATE</id>\n<enabled>Y</enabled>\n<name>LOGDATE</name>\n</field>\n<field>\n<id>DEPDATE</id>\n<enabled>Y</enabled>\n<name>DEPDATE</name>\n</field>\n<field>\n<id>REPLAYDATE</id>\n<enabled>Y</enabled>\n<name>REPLAYDATE</name>\n</field>\n<field>\n<id>LOG_FIELD</id>\n<enabled>Y</enabled>\n<name>LOG_FIELD</name>\n</field>\n<field>\n<id>EXECUTING_SERVER</id>\n<enabled>N</enabled>\n<name>EXECUTING_SERVER</name>\n</field>\n<field>\n<id>EXECUTING_USER</id>\n<enabled>N</enabled>\n<name>EXECUTING_USER</name>\n</field>\n<field>\n<id>START_JOB_ENTRY</id>\n<enabled>N</enabled>\n<name>START_JOB_ENTRY</name>\n</field>\n<field>\n<id>CLIENT</id>\n<enabled>N</enabled>\n<name>CLIENT</name>\n</field>\n</job-log-table>\n<jobentry-log-table>\n<connection>troy_etl_log</connection>\n<schema/>\n<table>K_LOG_J_ENTRY</table>\n<timeout_days/>\n<field>\n<id>ID_BATCH</id>\n<enabled>Y</enabled>\n<name>ID_BATCH</name>\n</field>\n<field>\n<id>CHANNEL_ID</id>\n<enabled>Y</enabled>\n<name>CHANNEL_ID</name>\n</field>\n<field>\n<id>LOG_DATE</id>\n<enabled>Y</enabled>\n<name>LOG_DATE</name>\n</field>\n<field>\n<id>JOBNAME</id>\n<enabled>Y</enabled>\n<name>TRANSNAME</name>\n</field>\n<field>\n<id>JOBENTRYNAME</id>\n<enabled>Y</enabled>\n<name>STEPNAME</name>\n</field>\n<field>\n<id>LINES_READ</id>\n<enabled>Y</enabled>\n<name>LINES_READ</name>\n</field>\n<field>\n<id>LINES_WRITTEN</id>\n<enabled>Y</enabled>\n<name>LINES_WRITTEN</name>\n</field>\n<field>\n<id>LINES_UPDATED</id>\n<enabled>Y</enabled>\n<name>LINES_UPDATED</name>\n</field>\n<field>\n<id>LINES_INPUT</id>\n<enabled>Y</enabled>\n<name>LINES_INPUT</name>\n</field>\n<field>\n<id>LINES_OUTPUT</id>\n<enabled>Y</enabled>\n<name>LINES_OUTPUT</name>\n</field>\n<field>\n<id>LINES_REJECTED</id>\n<enabled>Y</enabled>\n<name>LINES_REJECTED</name>\n</field>\n<field>\n<id>ERRORS</id>\n<enabled>Y</enabled>\n<name>ERRORS</name>\n</field>\n<field>\n<id>RESULT</id>\n<enabled>Y</enabled>\n<name>RESULT</name>\n</field>\n<field>\n<id>NR_RESULT_ROWS</id>\n<enabled>Y</enabled>\n<name>NR_RESULT_ROWS</name>\n</field>\n<field>\n<id>NR_RESULT_FILES</id>\n<enabled>Y</enabled>\n<name>NR_RESULT_FILES</name>\n</field>\n<field>\n<id>LOG_FIELD</id>\n<enabled>N</enabled>\n<name>LOG_FIELD</name>\n</field>\n<field>\n<id>COPY_NR</id>\n<enabled>N</enabled>\n<name>COPY_NR</name>\n</field>\n</jobentry-log-table>\n<channel-log-table>\n<connection>troy_etl_log</connection>\n<schema/>\n<table>K_LOG_J_CHANNEL</table>\n<timeout_days/>\n<field>\n<id>ID_BATCH</id>\n<enabled>Y</enabled>\n<name>ID_BATCH</name>\n</field>\n<field>\n<id>CHANNEL_ID</id>\n<enabled>Y</enabled>\n<name>CHANNEL_ID</name>\n</field>\n<field>\n<id>LOG_DATE</id>\n<enabled>Y</enabled>\n<name>LOG_DATE</name>\n</field>\n<field>\n<id>LOGGING_OBJECT_TYPE</id>\n<enabled>Y</enabled>\n<name>LOGGING_OBJECT_TYPE</name>\n</field>\n<field>\n<id>OBJECT_NAME</id>\n<enabled>Y</enabled>\n<name>OBJECT_NAME</name>\n</field>\n<field>\n<id>OBJECT_COPY</id>\n<enabled>Y</enabled>\n<name>OBJECT_COPY</name>\n</field>\n<field>\n<id>REPOSITORY_DIRECTORY</id>\n<enabled>Y</enabled>\n<name>REPOSITORY_DIRECTORY</name>\n</field>\n<field>\n<id>FILENAME</id>\n<enabled>Y</enabled>\n<name>FILENAME</name>\n</field>\n<field>\n<id>OBJECT_ID</id>\n<enabled>Y</enabled>\n<name>OBJECT_ID</name>\n</field>\n<field>\n<id>OBJECT_REVISION</id>\n<enabled>Y</enabled>\n<name>OBJECT_REVISION</name>\n</field>\n<field>\n<id>PARENT_CHANNEL_ID</id>\n<enabled>Y</enabled>\n<name>PARENT_CHANNEL_ID</name>\n</field>\n<field>\n<id>ROOT_CHANNEL_ID</id>\n<enabled>Y</enabled>\n<name>ROOT_CHANNEL_ID</name>\n</field>\n</channel-log-table>\n<pass_batchid>N</pass_batchid>\n<shared_objects_file/>\n<entries> \n </entries> \n <hops> \n </hops>\n  <notepads>  \n</notepads>\n</job>";
					    	java.nio.file.Files.write(kjb.toPath(), kjbString.getBytes());
					    }else{
					    	throw new IOException("创建空白kjb失败！");
					    }
					}
					logger.info("用户"+us.getKue().getUserName()+"创建项目"+model.getModelName()+"成功");
					return model;
				}else{
					model.setModelId("");
					model.setModelDescription("500");
					logger.error("用户"+us.getKue().getUserName()+"创建项目"+model.getModelName()+"失败");
					return model;
					
				}
				
			} else{
				model.setModelId("");
				model.setModelDescription("401");
				logger.error("用户"+us.getKue().getUserName()+"创建项目"+model.getModelName()+"失败：用户不是developer");
				return model;
			}
		}else{
			model.setModelId("");
			model.setModelDescription("404");
			logger.error("用户创建项目"+model.getModelName()+"失败：用户未登录");
			return model;
			}
	}


	private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
            return true;
        }
        return false;
    }
	private static StringBuffer getUTF8String(String strInput) {
		StringBuffer output = new StringBuffer();
		for (int i = 0; i < strInput.length(); i++)
		{
			char charx = strInput.charAt(i);
			if(isChinese(charx)){
				String char1 = Integer.toString(charx,16);
				output.append("&#x" +char1+";");
			}else{
				output.append(charx);
			}
		}
		return output;
	}
	
	/**
	 * 用于创建基本模型，将数据放入BaseModelTable,已废弃
	 * @param token
	 * @param requestModel
	 * @return BaseModel
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 */
	@Deprecated
	public BaseModel createBaseModel(String token,BaseModel requestModel) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		
		BaseModel baseModel = new BaseModel();
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			
			
			if(us.isIsadmin()) {
				KeystoneProjectEntity kpe = new KeystoneProjectEntity();
				kpe.setDescription(requestModel.getModelDescription());
				kpe.setEnable("true");
				kpe.setProjectName(requestModel.getModelName());
				String temp = ka.createProject(kpe, us.getKue().getUserID());
				String hdfsLocation;
				if (hadoopAPI.mkDir("/user/cdhfive/system/"+temp+"/")){
					hdfsLocation = "/user/cdhfive/system/"+temp+"/";
				}else{
					throw new IOException();
				}
				
				
				
				
				baseModel = new BaseModel(temp, requestModel.getExecuteType(), requestModel.getModelName(), requestModel.getModelDescription(), hdfsLocation, 0, requestModel.getVersion(), new Date(), temp, requestModel.getIconpath(),requestModel.getFunction()	, requestModel.getBaseType());
				
				if(baseModelDAO.save(baseModel)){
					return baseModel;
				}else{
					baseModel.setModelId("");
					baseModel.setModelDescription("500");
					return baseModel;
					
				}
				
			} else{
				baseModel.setModelId("");
				baseModel.setModelDescription("401");
				return baseModel;
			}
		}else{
			baseModel.setModelId("");
			baseModel.setModelDescription("404");
			return baseModel;
			}
	}
	
	
	public int setBaseModel(String token,String modelId){
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if(us.isIsadmin()) {
				Model model = modelDAO.findById(modelId);
				
				if(model!=null){
					if(model.getStatus().equals(3)&&(!model.getIsPublic())){
						BaseModel baseModel = new BaseModel(modelId, model.getExecuteType(), model.getModelName(), model.getModelDescription(), model.getHdfsLocation(), model.getStatus(), model.getVersion(), model.getUpdateDate(), model.getModelId(), model.getIconpath(), model.getFunction(), model.getType());
						model.setIsPublic(true);
						if(baseModelDAO.save(baseModel)&&modelDAO.save(model)){
							logger.info("管理员将项目"+model.getModelName()+"设置为基础项目成功");
							return 200;
						}else{
							logger.info("管理员将项目"+model.getModelName()+"设置为基础项目失败");
							return 500;
						}
					}else{
						logger.info("管理员将项目"+model.getModelName()+"设置为基础项目失败：项目未编译成功");
						return 400;
					}
				}else{
					logger.info("管理员将项目"+model.getModelName()+"设置为基础项目失败："+modelId+"不存在");
					return 400;
				}
			}else{
				logger.info("管理员将项目设置为基础项目失败：用户不是管理员");
				return 403;
			}
		}else{
			logger.info("管理员将项目设置为基础项目失败：用户未登录");
			return 404;
		}
	}
	
	
	
	
	
	public String copyToDir(String token,String projectId,MultipartFile[] file) throws IOException, KeyManagementException, NoSuchAlgorithmException, HttpException, DocumentException, InterruptedException{
		Model model =null;
		String type = "";
		if(userSessionManager.getMapSession().containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if(us.getMapMyProjects().containsKey(projectId)){
				if (((ProjectRole)us.getMapMyProjects().get(projectId)).isOwner()) {
					model = modelDAO.findById(projectId);
					if (hadoopAPI.mkDir(model.getHdfsLocation()+"main/lib/")&&hadoopAPI.mkDir(model.getHdfsLocation()+"subModel/")&&hadoopAPI.mkDir(model.getHdfsLocation()+"data/")){
						for (MultipartFile item : file) {
							type = item.getOriginalFilename().substring(item.getOriginalFilename().length()-3);
							if(type.equals("jar")){
								CommonsMultipartFile cf= (CommonsMultipartFile)item; 
					            DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
					            File file2 = fi.getStoreLocation();
					            //复制到本地
					            int byteread = 0; // 读取的字节数  
					            InputStream in = null;  
					            OutputStream out = null;  
					            File destFile = new File("kjbFolder\\"+model.getModelId()+"\\"+item.getOriginalFilename());
					            try {  
					                in = new FileInputStream(file2);  
					                out = new FileOutputStream(destFile);  
					                byte[] buffer = new byte[1024];  
					      
					                while ((byteread = in.read(buffer)) != -1) {  
					                    out.write(buffer, 0, byteread);  
					                }   
					            } catch (FileNotFoundException e) {  
					            } catch (IOException e) {  
					            } finally {  
					                try {  
					                    if (out != null)  
					                        out.close();  
					                    if (in != null)  
					                        in.close();  
					                } catch (IOException e) {  
					                    e.printStackTrace();  
					                }  
					            }  
								if(!hadoopAPI.mknewFile("/user/cdhfive/"+model.getCreateUserId()+"/"+model.getModelId()+"/main/lib/"+item.getOriginalFilename(),item)){
									logger.error("项目"+model.getModelName()+"上传jar失败");
									throw new RuntimeException("项目"+model.getModelName()+"上传jar失败");
								}
								logger.info("项目"+model.getModelName()+"上传文件"+item.getOriginalFilename()+"完成");
							}else{
								CommonsMultipartFile cf= (CommonsMultipartFile)item; 
					            DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
					            File file2 = fi.getStoreLocation();
					            String subModelId="";
					            if(type.equals("kjb")){
						            SAXReader reader = new SAXReader();
						            Document document = reader.read(file2);
						            Element root = document.getRootElement().element("entries");
						            List<Element> childList = root.elements("entry");
						            
						            for (Element element : childList) {
										subModelId+=element.elementText("baseModelID")+"|";
									}
					            }
					            //复制到本地
					            int byteread = 0; // 读取的字节数  
					            InputStream in = null;  
					            OutputStream out = null;  
					            File destFile = null;
					            if(type.equals("kjb"))
					            	destFile = new File("kjbFolder\\"+model.getModelId()+"\\main.kjb");
					            else
					            	destFile = new File("kjbFolder\\"+model.getModelId()+"\\"+item.getOriginalFilename()+"");
					            try {  
					                in = new FileInputStream(file2);  
					                out = new FileOutputStream(destFile);  
					                byte[] buffer = new byte[1024];  
					      
					                while ((byteread = in.read(buffer)) != -1) {  
					                    out.write(buffer, 0, byteread);  
					                }   
					            } catch (FileNotFoundException e) {  
					            } catch (IOException e) {  
					            } finally {  
					                try {  
					                    if (out != null)  
					                        out.close();  
					                    if (in != null)  
					                        in.close();  
					                } catch (IOException e) {  
					                    e.printStackTrace();  
					                }  
					            }  
					            boolean result = false;
					            if(type.equals("kjb"))
					            	result = hadoopAPI.mknewFile("/user/cdhfive/"+model.getCreateUserId()+"/"+model.getModelId()+"/main/main.kjb",item);
					            else
					            	result = hadoopAPI.mknewFile("/user/cdhfive/"+model.getCreateUserId()+"/"+model.getModelId()+"/main/"+item.getOriginalFilename(),item);
					            
					           if(result){
					        	   
					        	   model.setSubModel(subModelId);
					        	   model.setStatus(1);
					        	   modelDAO.save(model);
					        	   logger.info("项目"+model.getModelName()+"上传"+item.getOriginalFilename()+"成功");
					           }else{
					        	   logger.error("项目"+model.getModelName()+"上传"+item.getOriginalFilename()+"失败");
					        	   throw new RuntimeException("项目"+model.getModelName()+"上传"+item.getOriginalFilename()+"失败");
					           }
							}
						}
						String jobid = "";
						if(type.equals("kjb")){
							jobid = jobStatusManager.startTrans(oozie, model.getModelId(), model.getHdfsLocation(),us.getKue().getUserID());
							model.setStatus(2);
							modelDAO.save(model);
							logger.info("项目"+model.getModelName()+"上传文件完成，项目"+model.getModelName()+"开始编译,编译ID"+jobid);
						}
						if(jobid.equals(""))
							return "201";
						else
							return jobid;
					}else{
						logger.error("创建项目"+model.getModelName()+"文件夹失败！");
						throw new IOException("创建项目"+model.getModelName()+"文件夹失败！");
					}
				}else{
					logger.error("上传项目文件"+model.getModelName()+"失败：不是该项目的拥有者");
					throw new AuthenticationException("上传项目文件"+model.getModelName()+"失败：不是该项目的拥有者");
				}
			}else{
				logger.error("上传项目文件失败：不是找不到该项目"+projectId);
				throw new AuthenticationException("上传项目文件失败：不是找不到该项目"+projectId);
			}	
		}else{
			logger.error("上传项目文件失败：用户"+token+"未登录");
			throw new AuthenticationException("上传项目文件失败：用户"+token+"未登录");
		}
		
		

	}
	
	
	
	
	
	
	/**
	 * 
	 * @param token
	 * @param requestModel
	 * @return Model
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 */
	public Model UploadModel(String token,Model requestModel) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException {
		Model model = new Model();
		if (userSessionManager.mapSession.containsKey(token)){
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.getMapMyProjects().containsKey(requestModel.getModelId())) {
				if (((ProjectRole)us.getMapMyProjects().get(model)).isOwner()) {
					model = modelDAO.findById(requestModel.getModelId());
					if (us.getKue().getUserID().equals(model.getCreateUserId())) {
						model.setStatus(1);
						model.setSubModel(requestModel.getSubModel());
						model.setUpdateDate(new Date());
						String submodel[] = requestModel.getSubModel().split("|");
						
						if (modelDAO.save(model)) {
							return model;
						} else {
							model.setModelId("");
							model.setModelDescription("500");
							return model;
						}

					} else {

						model.setModelId("");
						model.setModelDescription("401");
						return model;

					}
				}else{
					model.setModelId("");
					model.setModelDescription("401");
					return model;
				}
			}else{
				model.setModelId("");
				model.setModelDescription("401");
				return model;
			}
			
			
			
		}else{
			model.setModelId("");
			model.setModelDescription("404");
			return model;
			}
	}
	
	/**
	 * 
	 * @param token
	 * @param request
	 * @return int
	 */
	public int publicModel(String token,PublicModel request){
		
		Model model=new Model();
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.getMapMyProjects().containsKey(request.getModelId())) {
				if (((ProjectRole)us.getMapMyProjects().get(request.getModelId())).isOwner()) {
					model = modelDAO.findById(request.getModelId());
					if(model==null)
						throw new RuntimeException("there is not model "+request.getModelId());
					
					if(!model.getIsPublic()&&model.getStatus()==3){
						model.setIsPublic(true);
						request.setPublishDate(new Date());
						if(modelDAO.save(model)&&publicModelDAO.save(request)){
							logger.info("用户"+us.getKue().getUserName()+"发布模型"+model.getModelName()+"成功！");
							return 201;
						}else{
							logger.info("用户"+us.getKue().getUserName()+"发布模型"+model.getModelName()+"失败：连接数据库错误");
							return 500;
						}
					}else{
						logger.info("用户"+us.getKue().getUserName()+"发布模型"+model.getModelName()+"失败：该模型已经发布或模型编译不成功");
						return 400;
					}
				}else{
					logger.info("用户"+us.getKue().getUserName()+"发布模型"+model.getModelName()+"失败：用户无该模型的owner权限");
					return 403;
				}
			}else{
				logger.info("用户"+us.getKue().getUserName()+"发布模型"+model.getModelName()+"失败：用户无该模型的owner权限");
				return 403;
			}
		}else{
			logger.info("用户发布模型失败：用户未成功登录");
			return 401;
		}
	}
	/**
	 * 
	 * @param token
	 * @param modelId
	 * @return File
	 * @throws IOException 
	 */
	public File findFileByModelId(String token,String modelId) throws IOException{
		
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.getMapMyProjects().containsKey(modelId)) {
				if (((ProjectRole)us.getMapMyProjects().get(modelId)).isOwner()||((ProjectRole)us.getMapMyProjects().get(modelId)).isExecuter()||((ProjectRole)us.getMapMyProjects().get(modelId)).isWriter()) {
					File file = new File("kjbFolder\\"+modelId+"\\main.kjb");
					if(!file.exists())
						throw new IOException("未上传模型的 kjb");
					logger.info("用户"+us.getKue().getUserName()+"寻找模型"+modelId+"的kjb成功！");
					return file;
				}else{
					logger.info("用户"+us.getKue().getUserName()+"下载模型"+modelId+"的kjb失败：该用户不是模型的拥有者");
					throw new AuthenticationException("用户权限不足");
				}
			}else{
				logger.info("用户"+us.getKue().getUserName()+"下载模型"+modelId+"的kjb失败：该用户没有该模型的使用权");
				throw new AuthenticationException("用户权限不足");
			}
		}else{
			logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"下载模型"+modelId+"的kjb失败：用户未登录");
			throw new AuthenticationException("the user is offline");
		}
	}
	public String[] getDirOfModel(String token,String modelId) throws Exception{
		
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.getMapMyProjects().containsKey(modelId)) {
				Model model = modelDAO.findById(modelId);
				String[] temp = hadoopAPI.getFile(model.getOutputDir());
				logger.info("用户"+us.getKue().getUserName()+"从hdfs获取模型"+modelId+"的输出目录下文件名成功");
				return temp;
			}else{
				logger.info("用户"+us.getKue().getUserName()+"获取模型"+modelId+"的输出目录失败：该用户没有该模型的使用权");
				throw new AuthenticationException("用户权限不足");
			}
		}else{
			logger.info("用户获取模型"+modelId+"的输出目录失败：用户未登录");
			throw new AuthenticationException("the user is offline");
		}
	}
	
	/**
	 * 获取指定的输出文件，输入参数为模型拥有者token、模型ID和指定的某个输出文件的绝对地址。返回连接的Response，其中有连接状态以及文件的二进制流
	 * @param token 模型拥有者的token
	 * @param model 输出文件所对应的模型Id
	 * @return HttpResponse
	 * @throws KeyManagementException
	 * @throws NoSuchAlgorithmException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws HttpException
	 */
	public HttpResponse getOutputFileOfModel(String token,Model model) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{
		
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.getMapMyProjects().containsKey(model.getModelId())) {
				logger.info("用户"+us.getKue().getUserName()+"从hdfs获取模型"+model.getModelId()+"的输出文件"+model.getOutputDir()+"成功");
				 return hadoopAPI.readFile(model.getOutputDir());
			}else {
				logger.info("用户"+us.getKue().getUserName()+"从hdfs获取模型"+model.getModelId()+"的输出文件"+model.getOutputDir()+"失败：该用户没有该模型的使用权");
				throw new AuthenticationException("用户权限不足");
			}
		}else{
			logger.info("用户从hdfs获取模型"+model.getModelId()+"的输出文件"+model.getOutputDir()+"失败：用户未登录");
			throw new AuthenticationException("the user is offline");
		}
	}
	
	
	
	
	public ModelType addModelType(String token,ModelType modelType) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException, NamingException{
		
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.isIsadmin()) {
				if(!modelType.getTypeName().equals("")){
					ModelTypeDAO dao = new ModelTypeDAO();
					if(dao.save(modelType)){
						return (ModelType)dao.findByTypeName(modelType.getTypeName()).get(0);
					}else{
						logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"添加模型类型失败：数据库链接问题");
						throw new DataAccessResourceFailureException("the datebase connecter wrong");
					}
				}else{
					logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"添加模型类型失败：类型名为空");
					throw new NamingException("the name of type cannot be enpty");
				}
			}else{
				logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"添加模型类型失败：用户不是admin");
				throw new AuthenticationException("only admin can add model type");
			}
		}else{
			logger.info("用户添加模型类型失败：用户未登录");
			throw new AuthenticationException("the user is offline");
		}
	}
	
public ModelType updateModelType(String token,ModelType modelType) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException, NamingException{
		
		if (userSessionManager.mapSession.containsKey(token)){
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(new Date());
			gc.add(Calendar.HOUR_OF_DAY, 2);
			((UserSession)userSessionManager.mapSession.get(token)).setExpires_at(gc);
			UserSession us = ((UserSession)userSessionManager.mapSession.get(token));
			if (us.isIsadmin()) {
				if(!modelType.getTypeName().equals("")){
					ModelTypeDAO dao = new ModelTypeDAO();
					ModelType  modelType2 = dao.findById(modelType.getTypeId());
					modelType2.setParentTypeId(modelType.getParentTypeId());
					modelType2.setTypeDesc(modelType.getTypeDesc());
					modelType2.setTypeName(modelType.getTypeName());
					if(dao.save(modelType2)){
						logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"修改模型类型成功");
						return modelType2;
					}else{
						logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"修改模型类型失败：数据库链接问题");
						throw new DataAccessResourceFailureException("the datebase connecter wrong");
					}
				}else{
					logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"修改模型类型失败：类型名为空");
					throw new NamingException("the name of type cannot be enpty");
				}
			}else{
				logger.info("用户"+((UserSession)userSessionManager.mapSession.get(token)).getKue().getUserName()+"修改模型类型失败：用户不是admin");
				throw new AuthenticationException("only admin can add model type");
			}
		}else{
			logger.info("用户修改模型类型失败：用户未登录");
			throw new AuthenticationException("the user is offline");
		}
	}
	
	
	
	
	
	
	
	
	
}
