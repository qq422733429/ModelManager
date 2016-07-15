package com.troy.sessionManager;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.troy.entity.Frequence;
import com.troy.util.HttpClientUtil;
import com.troy.webservice.AuthorityService;
      



public class JobStatusManager {
	HttpClientUtil hcu =new HttpClientUtil();
	static Logger logger = Logger.getLogger (JobStatusManager.class.getName());
	
	
	
	public String startTrans(String Oozieurl,String modelId,String appPath,String userId) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		
		
		URL url = new URL("http://"+Oozieurl+"/schedule/compile");
		   
        String content = "{\"userId\":\""+userId+"\",\"appPath\":\"hdfs://datanode1:8020/user/cdhfive/Yun/ModelExplain\",\"kjbHdfsPath\":\"hdfs://datanode1:8020"+appPath+"main/main.kjb\",\"modelId\":\""+modelId+"\"}";
        HttpResponse hrp =hcu.doPost(url.toString(), content);
        
        
	    if((hrp.getStatusLine().getStatusCode()) == 200){
		    String jsonObj =EntityUtils.toString(hrp.getEntity()); 
		    EntityUtils.consume(hrp.getEntity());
		    return jsonObj;
		   
	    }else{
	    	EntityUtils.consume(hrp.getEntity());
        	throw new HttpException("开始翻译作业出错，"+hrp.getStatusLine().getReasonPhrase());
		}
		
		
		
		  
	}
	
	
	public String startJob(String Oozieurl,String userID,String appPath,String modelID,String modelName) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		
		
		URL url = new URL("http://"+Oozieurl+"/schedule/submit");
		   
        String content = "{\"userId\":\""+userID+"\",\"modelName\":\""+modelName+"\",\"appPath\":\""+"hdfs://datanode1:8020"+appPath+"main"+"\",\"modelId\":\""+modelID+"\",\"modelId\":\""+modelID+"\"}";
        HttpResponse hrp =hcu.doPost(url.toString(), content);
        
        
	    if((hrp.getStatusLine().getStatusCode()) == 200){
		    String jsonObj =EntityUtils.toString(hrp.getEntity());
		    EntityUtils.consume(hrp.getEntity());
		    return jsonObj;
		   
	    }else if((hrp.getStatusLine().getStatusCode()) == 403){
        	throw new HttpException("开始作业失败:模型正在运行");
		}else{
			throw new HttpException("开始作业失败:参数不合法");
		}
	}
	
	public int killJob(String Oozieurl,String jobId) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		URL url = new URL("http://"+Oozieurl+"/schedule/killJob");
		String content = jobId;
        HttpResponse hrp = hcu.doPost(url.toString(), content);
        if((hrp.getStatusLine().getStatusCode()) == 200){
        	EntityUtils.consume(hrp.getEntity());
		    return 200;
	    }else{
	    	EntityUtils.consume(hrp.getEntity());
        	throw new HttpException("停止作业失败，"+hrp.getStatusLine().getReasonPhrase());
		}
	}
	
	public int suspendJob(String Oozieurl,String jobId) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		URL url = new URL("http://"+Oozieurl+"/schedule/suspendJob");
		String content = jobId;
        HttpResponse hrp = hcu.doPost(url.toString(), content);
        if((hrp.getStatusLine().getStatusCode()) == 200){
        	EntityUtils.consume(hrp.getEntity());
		    return 200;
	    }else{
	    	EntityUtils.consume(hrp.getEntity());
        	throw new HttpException("暂停作业失败，"+hrp.getStatusLine().getReasonPhrase());
		}
	}
	public int resumeJob(String Oozieurl,String jobId) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		URL url = new URL("http://"+Oozieurl+"/schedule/resumeJob");
		String content = jobId;
        HttpResponse hrp = hcu.doPost(url.toString(), content);
        if((hrp.getStatusLine().getStatusCode()) == 200){
        	EntityUtils.consume(hrp.getEntity());
		    return 200;
	    }else{
	    	EntityUtils.consume(hrp.getEntity());
        	throw new HttpException("回复作业失败，"+hrp.getStatusLine().getReasonPhrase());
		}
	}
	
	
	public String startSetTimeJob(String Oozieurl,String userID,String appPath,String time,String modelId,String modelName) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		URL url = new URL("http://"+Oozieurl+"/schedule/coordinator");
        String content = "{\"userId\":\""+userID+"\",\"modelId\":\""+modelId+"\",\"modelName\":\""+modelName+"\",\"appPath\":\""+"hdfs://datanode1:8020"+appPath+"main"+"\",\"frequency\":\"\",\"start\":\""+time+"\",\"end\":\"\"}";
        HttpResponse hrp =hcu.doPost(url.toString(), content);
        
        
	    if((hrp.getStatusLine().getStatusCode()) == 200){
		    String jsonObj =EntityUtils.toString(hrp.getEntity());
		    EntityUtils.consume(hrp.getEntity());
		    return jsonObj;
	    }else if((hrp.getStatusLine().getStatusCode()) == 403){
        	throw new HttpException("开始定时作业失败:模型正在运行");
		}else{
			throw new HttpException("开始定时作业失败:参数不合法");
		}
	}
	
	
	public String startFrequenceJob(String Oozieurl,String userID,String appPath,String modelName,String modelId,Frequence frequence) throws HttpException, IOException, KeyManagementException, NoSuchAlgorithmException{
		
		
		URL url = new URL("http://"+Oozieurl+"/schedule/coordinatorfreq");
		   
        String content = "{\"userId\":\""+userID+"\",\"modelId\":\""+modelId+"\",\"modelName\":\""+modelName+"\",\"appPath\":\""+"hdfs://datanode1:8020"+appPath+"main"+"\",\"frequency\":\""+frequence.getFrequency()+"\",\"start\":\""+frequence.getStart()+"\",\"end\":\""+frequence.getEnd()+"\"}";
        HttpResponse hrp =hcu.doPost(url.toString(), content);
        
        
	    if((hrp.getStatusLine().getStatusCode()) == 200){
		    String jsonObj =EntityUtils.toString(hrp.getEntity());
		    EntityUtils.consume(hrp.getEntity());
		    return jsonObj;
	    }else if((hrp.getStatusLine().getStatusCode()) == 403){
        	throw new HttpException("开始周期作业失败:模型正在运行");
		}else{
			throw new HttpException("开始周期作业失败:参数不合法");
		}
	}
	
	

}
