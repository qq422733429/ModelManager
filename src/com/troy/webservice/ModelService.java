package com.troy.webservice;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.troy.dao.ModelDAO;
import com.troy.entity.BaseModel;
import com.troy.entity.FindModelList;
import com.troy.entity.Model;
import com.troy.entity.ModelType;
import com.troy.entity.PublicModel;
import com.troy.entity.StringArray;
import com.troy.sessionManager.ModelManager;

@Controller
@RequestMapping(value = "/model")
public class ModelService {
	/*
	 * 模型管理对象，由spring加载
	 */
	private ModelManager modelManager;
	static Logger logger = Logger.getLogger (ModelService.class.getName());

	public ModelManager getModelManager() {
		return modelManager;
	}

	public void setModelManager(ModelManager modelManager) {
		this.modelManager = modelManager;
	}

	/**
	 * 该接口用于创建模型，输入参数由Model类给出，model创建成功返回模型相关数据，创建项目必须是developer权限，模型创建成功status为0
	 * @param response
	 * @param token
	 * @param requestModel
	 * @return Model s
	 */
	@RequestMapping(value = "/createModel/{token}",method = RequestMethod.POST)
	public @ResponseBody Model createModel(HttpServletResponse response,@PathVariable("token") String token,@RequestBody Model requestModel) {
		
		response.setContentType("application/json;charset=utf-8");
		try {
			Model temp =modelManager.createModel(token, requestModel);
			if (temp.getModelDescription().equals("500")){
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
				return null;
			}else if (temp.getModelDescription().equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
				return null;
			}else if (temp.getModelDescription().equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
				return null;
			}else{
				return temp;
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
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	
	/**
	 * 该接口用于创建基本模型，输入参数由BaseModel类给出，BaseModel创建成功返回模型相关数据，创建项目必须是admin权限，模型创建成功status为0,已废弃
	 * @param response
	 * @param token
	 * @param requestModel
	 * @return Model 
	 */
	@Deprecated
	@RequestMapping(value = "/createBaseModel/{token}",method = RequestMethod.POST)
	public @ResponseBody BaseModel createBaseModel(HttpServletResponse response,@PathVariable("token") String token,@RequestBody BaseModel requestModel) {
		response.setContentType("application/json;charset=utf-8");
		try {
			BaseModel temp =modelManager.createBaseModel(token, requestModel);
			if (temp.getModelDescription().equals("500")){
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
				return null;
			}else if (temp.getModelDescription().equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
				return null;
			}else if (temp.getModelDescription().equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
				return null;
			}else{
				return temp;
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
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	}
	
	
	/**
	 * 该接口用于将模型设置为基本模型，token必须是admin权限,model必须是管理员创建的且已经编译成功的模型
	 * @param response
	 * @param token
	 * @param modelId
	 * @return Model 
	 */
	@RequestMapping(value = "/setBaseModel/{token}/{modelId}",method = RequestMethod.PUT)
	public void setBaseModel(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("modelId") String modelId) {
		response.setContentType("application/json;charset=utf-8");
		try {
			switch(modelManager.setBaseModel(token, modelId)){
				case 200:
					response.getWriter().write("{\"status\":\"success\"}");
					break;
				case 500:
					response.sendError(response.SC_INTERNAL_SERVER_ERROR, "Internal error");
					break;
				case 400:
					response.sendError(response.SC_BAD_REQUEST, "model is not prepered or modelid is worry");
					break;
				case 403:
					response.sendError(response.SC_UNAUTHORIZED, "token is not admin");
					break;
				case 404:
					response.sendError(response.SC_NOT_FOUND, "token is not found");
					break;
				
			}
		} catch ( IOException e) {
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
	 * 上传模型的运行文件，已废弃
	 * @param response
	 * @param token 模型拥有者权限
	 * @param requestModel
	 * @return Model
	 * @throws IOException
	 */
	@Deprecated
	@RequestMapping(value = "/UploadModel/{token}",method = RequestMethod.POST)
	public @ResponseBody Model UploadModel(HttpServletResponse response,@PathVariable("token") String token,@RequestBody Model requestModel) throws IOException {

		response.setContentType("application/json;charset=utf-8");
		try {
			Model temp =modelManager.UploadModel(token, requestModel);
			if (temp.getModelDescription().equals("500")){
				response.sendError(response.SC_BAD_REQUEST, "Internal error");
				return null;
			}else if (temp.getModelDescription().equals("401")){
				response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
				return null;
			}else if (temp.getModelDescription().equals("404")){
				response.sendError(response.SC_NOT_FOUND, "User is offline");
				return null;
			}else{
				return temp;
			}
		} catch (KeyManagementException | NoSuchAlgorithmException
				 | IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			
				response.sendError(response.SC_BAD_REQUEST, e.getMessage());
				return null;
			
		}
	}
	
	
	
	/**
	 * 该接口用于对指定模型上传jar、xml和kjb文件，每个文件分别调用该接口，上传成功后status为1，kjb上传完毕后会自动调用解释程序进行解释，开始解释status为2
	 * @param response
	 * @param token
	 * @param projectId
	 * @param file
	 */
	 @RequestMapping(value = "/filesUpload/{token}/{projectId}",method = RequestMethod.POST)
	 public void filesUpload(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("projectId") String projectId,@RequestParam("myfiles") MultipartFile[] file) {
		 logger.info("用户开始上传kjb、jar、xml、hql");
			try {
				String result = modelManager.copyToDir(token, projectId, file);
				switch (result) {
				case "201":
					response.getWriter().write("{\"status\":\"success\"}");
					break;
				case "401":
					response.sendError(response.SC_FORBIDDEN, "User is Unauthorized");
					break;
				case "404":
					response.sendError(response.SC_NOT_FOUND, "User is offline");
					break;
				default:
					response.sendError(response.SC_BAD_REQUEST, "upload file fail");
					break;
				}
			} catch (KeyManagementException | NoSuchAlgorithmException
					| IOException | HttpException | DocumentException
					| InterruptedException e) {
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
	 * 该接口用于解释作业过程中，解释程序需要复制子模型时查找所有子模型的绝对地址，并统一返回给解释程序
	 * @param response
	 * @param request
	 * @return FindModelList
	 */
	 @RequestMapping(value = "/findSubModelPath",method = RequestMethod.POST)
	 public @ResponseBody FindModelList findSubModelPath(HttpServletResponse response,@RequestBody FindModelList request) {
		try {
			return  modelManager.findModelList(request);
		}catch (Exception e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, e.getMessage());
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
				
			}
		}
		
	 }	 
	 /**
	  * 在解释程序解释完毕以后会返回输入和输出目录地址，存入数据库中，以便以后执行后查看输出目录
	  * @param response
	  * @param request 
	  */
	 @RequestMapping(value = "/outputAndInputDir",method = RequestMethod.POST)
	 public void outputAndInputDir(HttpServletResponse response,@RequestBody Model request) {
		 try {
			switch (modelManager.getOutput(request)) {
			case 201:
				response.getWriter().write("{\"status\":\"success\"}");
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
				response.sendError(response.SC_INTERNAL_SERVER_ERROR, "there is modelid is "+request.getModelId());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	 }	 
	 /**
	  * 该接口用于用户发布自己创建的且成功编译的文件，发布以后其他人可以从AIManager查看该模型介绍，并可以申请使用该模型试用权
	  * @param response
	  * @param token
	  * @param request
	  */
	 @RequestMapping(value = "/publicModel/{token}",method = RequestMethod.POST)
	 public void publicModel(HttpServletResponse response,@PathVariable("token") String token,@RequestBody PublicModel request) {
		 try {
			switch (modelManager.publicModel(token, request)) {
			case 201:
				response.getWriter().write("{\"status\":\"success\"}");
				break;
			case 400:
				response.sendError(response.SC_BAD_REQUEST, "the model has publiced");
				break;
			case 401:
				response.sendError(response.SC_NOT_FOUND,"the user is offline");
				break;
			case 403:
				response.sendError(response.SC_FORBIDDEN, "the user does not have this model");
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
	  * 获取某个指定模型的kjb文件，用于修改模型。返回kjb文件的二进制流
	  * @param response
	  * @param token
	  * @param modelId
	  */
	 @RequestMapping(value = "/downPhotoById/{token}/{modelId}")  
	 public void downPhotoByStudentId(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("modelId") String modelId){  
		 OutputStream outputStream=null;
	     FileInputStream fis=null;
		 try {
			File file  = modelManager.findFileByModelId(token, modelId);
			fis = new FileInputStream(file);  
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);  
            byte[] b = new byte[1000];  
            int n;  
            ModelDAO modeldao = new ModelDAO();
            Model model = modeldao.findById(modelId);
			 String fileName = model.getModelName()+".kjb";
			 fileName = URLEncoder.encode(fileName, "UTF-8");  
			 response.reset();  
			 response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
			 response.addHeader("Content-Length", "" + file.length());  
			 response.setContentType("application/octet-stream;charset=UTF-8");  
			 outputStream = new BufferedOutputStream(response.getOutputStream()); 
			 while ((n = fis.read(b)) != -1) {  
	                bos.write(b, 0, n);  
	           }
			 bos.writeTo(outputStream);
			 outputStream.flush();
			 logger.info("用户"+token+"下载模型"+modelId+"的kjb成功！");
		} catch (IOException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
		}  finally{
	     try {
	    	fis.close();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
		}
	 } 
	 /**
	  * 用户获得查看获得输出文件共分为两步
	  * 第一步获取指定model的输出目录下的所有文件列表。第二步选择指定文件获得文件内容流
	  * 该接口为第一步，必须确定为已成功编译的模型，若调度执行过才会有结果
	  * @param response
	  * @param token
	  * @param modelId
	  * @return StringArray
	  */
	 @RequestMapping(value = "/getDirOfModel/{token}/{modelId}",method = RequestMethod.GET)  
	 public @ResponseBody StringArray getDirOfModel(HttpServletResponse response,@PathVariable("token") String token,@PathVariable("modelId") String modelId){  
		 response.setContentType("application/json;charset=utf-8");
		 try {
			 String[] result = modelManager.getDirOfModel(token,modelId);
				if(result!=null){
					StringArray sa = new StringArray();
					sa.setOutput(result);
					logger.info("token"+token+"获取模型"+modelId+"的输出目录成功");
					return sa;
				}else{
					logger.info("token"+token+"获取模型"+modelId+"的输出目录失败");
					return null;
				}
			}catch (Exception e) {
				StringBuffer stringBuffer = new StringBuffer();
				for (StackTraceElement element : e.getStackTrace()) {
					stringBuffer.append("\t"+element+"\n");
				}
				logger.error(e.getMessage()+"\n"+stringBuffer.toString());
				try {
					response.sendError(response.SC_NOT_FOUND, "there is no output file!");
					return null;
				} catch (IOException e1) {
					e1.printStackTrace();
					
				}
			}
		 return null;
		 
	 } 
	 
	 /**
	  * 该接口为查看输出文件第二步，输入参数为modelid和指定的某个输出文件，文件将以文件流的形式返回
	  * @param response
	  * @param token
	  * @param model
	  */
	 @RequestMapping(value = "/getOutputFileOfModel/{token}",method = RequestMethod.POST)  
	 public void getOutputFileOfModel(HttpServletResponse response,@PathVariable("token") String token,@RequestBody Model model){  
		 
		 OutputStream outputStream=null;
	     InputStream fis=null;
	     ByteArrayOutputStream bos=null;
	     try {
			HttpResponse responseFile = modelManager.getOutputFileOfModel(token,model);
			
			 if(responseFile!=null){
				fis = responseFile.getEntity().getContent(); 
				long lenth= responseFile.getEntity().getContentLength();
				EntityUtils.consume(responseFile.getEntity());
				bos = new ByteArrayOutputStream(1000);  
			    byte[] b = new byte[1000];  
			    int n;  
				 String fileName = "result";
				 fileName = URLEncoder.encode(fileName, "UTF-8");  
				 response.reset();  
				 response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");  
				 response.addHeader("Content-Length", "" + lenth);  
				 response.setContentType("application/octet-stream;charset=UTF-8");  
				 outputStream = new BufferedOutputStream(response.getOutputStream()); 
				 while ((n = fis.read(b)) != -1) {  
			            bos.write(b, 0, n);  
			       }
				 bos.writeTo(outputStream);
				 outputStream.flush();
				 logger.info("token"+token+"获取模型"+model.getModelId()+"的输出文件"+model.getOutputDir()+"成功");
			 }
		} catch (KeyManagementException | NoSuchAlgorithmException
				| UnsupportedOperationException
				| IOException | HttpException e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
		}finally{
			try {
				bos.close();
				fis.close();
				outputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	 } 
	 
	
	 @RequestMapping(value = "/addModelType/{token}",method = RequestMethod.POST)  
	 public @ResponseBody ModelType addModelType(HttpServletResponse response,@PathVariable("token") String token,@RequestBody ModelType modelType){  
		 
	   try {
		   return modelManager.addModelType(token, modelType);
	   } catch (Exception e) {
			StringBuffer stringBuffer = new StringBuffer();
			for (StackTraceElement element : e.getStackTrace()) {
				stringBuffer.append("\t"+element+"\n");
			}
			logger.error(e.getMessage()+"\n"+stringBuffer.toString());
			try {
				response.sendError(response.SC_BAD_REQUEST, e.getMessage()+stringBuffer.toString());
				return null;
			} catch (IOException e1) {
				e1.printStackTrace();
				return null;
			}
		}
	 }
	   
	   
	   @RequestMapping(value = "/updateModelType/{token}",method = RequestMethod.POST)  
		 public @ResponseBody ModelType updateModelType(HttpServletResponse response,@PathVariable("token") String token,@RequestBody ModelType modelType){  
			 
		   try {
			   return modelManager.updateModelType(token, modelType);
		   } catch (Exception e) {
				StringBuffer stringBuffer = new StringBuffer();
				for (StackTraceElement element : e.getStackTrace()) {
					stringBuffer.append("\t"+element+"\n");
				}
				logger.error(e.getMessage()+"\n"+stringBuffer.toString());
				try {
					response.sendError(response.SC_BAD_REQUEST, e.getMessage()+stringBuffer.toString());
					return null;
				} catch (IOException e1) {
					e1.printStackTrace();
					return null;
				}
			}
			
		
		
	 }
	 
	 
}
