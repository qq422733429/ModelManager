package com.troy.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;















import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;













import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import net.sf.json.JSONObject;


public class HadoopAPI {
	String hdfsUserName;
	HttpClientUtil hcu = new HttpClientUtil();
	
	public String getHdfsUserName() {
		return hdfsUserName;
	}

	public void setHdfsUserName(String hdfsUserName) {
		this.hdfsUserName = hdfsUserName;
	}

	private String LocalHost ="";
	DataOutputStream ds;
	
	public HadoopAPI(String HadoopHostUrl,String hdfsUserName){ 
		this.hdfsUserName=hdfsUserName;
		LocalHost="http://"+HadoopHostUrl;
	}
	
	public boolean mkDir(String dirName) throws IOException{
		
		URL url = new URL(LocalHost+dirName+"?op=MKDIRS&user.name="+hdfsUserName);
	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
	    connet.setRequestMethod("PUT"); 
	    if(connet.getResponseCode() != 200){
	        throw new IOException(connet.getResponseMessage());
	    }
	    BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream()));
	    StringBuilder  sb  = new StringBuilder();
	    String line;
	    while((line = brd.readLine()) != null){
	        sb.append(line);
	    }
	    brd.close();
	    connet.disconnect();
	    JSONObject jsonObj =new JSONObject().fromObject(sb.toString());
		if(jsonObj.get("boolean").toString().equals("true")){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean deleteDir(String dirName) throws Exception {
		URL url = new URL(LocalHost+dirName+"?op=DELETE&user.name="+hdfsUserName);
	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
	    connet.setRequestMethod("DELETE"); 
	    if(connet.getResponseCode() != 200){
	        throw new IOException(connet.getResponseMessage());
	    }
	    BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream()));
	    StringBuilder  sb  = new StringBuilder();
	    String line;
	    while((line = brd.readLine()) != null){
	        sb.append(line);
	    }
	    brd.close();
	    connet.disconnect();
	    JSONObject jsonObj =new JSONObject().fromObject(sb.toString());
		if(jsonObj.get("boolean").toString().equals("true")){
			return true;
		}else{
			return false;
		}
		
		
	}
	
//	public boolean mknewFile(String fileName) throws Exception {
//		URL url = new URL(LocalHost+fileName+"?op=CREATE&user.name="+hdfsUserName);
//	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
//	    connet.setRequestMethod("PUT"); 
//	    if(connet.getResponseCode() != 307){
//	        throw new IOException(connet.getResponseMessage());
//	    }
//	    BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream()));
//	    StringBuilder  sb  = new StringBuilder();
//	    String line;
//	    while((line = brd.readLine()) != null){
//	        sb.append(line);
//	    }
//	    brd.close();
//	    connet.disconnect();
//	    JSONObject jsonObj =new JSONObject().fromObject(sb.toString());
//		if(jsonObj.get("boolean").toString().equals("true")){
//			return true;
//		}else{
//			return false;
//		}
//		
//		
//	}
	
	
	public boolean mknewFile(String fileName,MultipartFile file) throws ClientProtocolException, IOException, KeyManagementException, NoSuchAlgorithmException, HttpException, InterruptedException{
		String BOUNDARY = "--";
		URL url = new URL(LocalHost+fileName+"?op=CREATE&user.name="+hdfsUserName);
		System.out.println(url.toString());
		HttpResponse hrp =hcu.doPut(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 307){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
	    Header aa[] = hrp.getHeaders("Location");
	    String temp = aa[0].getValue();
	   
	    	temp=temp.replaceAll("datanode1", "192.168.121.35");
	    	temp=temp.replaceAll("master", "192.168.121.34");
	    	temp=temp.replaceAll("datanode2", "192.168.121.36");
	    	temp=temp.replaceAll("datanode3", "192.168.121.37");
	    	temp=temp.replaceAll("false", "true");
	    	Thread.sleep(1000);
	    	CommonsMultipartFile cf= (CommonsMultipartFile)file; 
	        DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
	        File file2 = fi.getStoreLocation(); 
	        
	        HttpClient httpclient = new DefaultHttpClient();  
			FileEntity fileEntity = new FileEntity(file2, "application/octet-stream");
			HttpPut put2 = new HttpPut(temp);
			put2.addHeader("Content-Type", "application/octet-stream");
			put2.setEntity(fileEntity);
			HttpResponse response2 = httpclient.execute(put2);
			if(201==response2.getStatusLine().getStatusCode()){    
                
               return true;
                
            }else{
            	return false;
            }
	        
	        
	        
	        
	        
	        
//	    	
//	    	FileWriter fw=new FileWriter("D:\\curl\\"+file.getOriginalFilename()+".bat");
//	    	String aaa = "cd d:\\curl\ncurl.exe\ncurl -i -X PUT -T "+file2.getPath()+" \""+temp+"\"";
//	    	fw.write(aaa);
//	    	fw.close();
//	    	String cmd = "cmd /c start D:\\curl\\"+file.getOriginalFilename()+".bat";// pass
//			
//			
//			Process ps = Runtime.getRuntime().exec(cmd);
//	    	
//			Thread.sleep(1000);
//	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
	    	
//	    	
//	    	HttpClient httpclient = new DefaultHttpClient();  
//            HttpPut put = new HttpPut(temp);  
//            CommonsMultipartFile cf= (CommonsMultipartFile)file; 
//            DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
//            File file2 = fi.getStoreLocation(); 
//              
//            
//            
//            FileBody fileBody = new FileBody(file2);  
//            
//            
//            
//            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,BOUNDARY,Charset.forName("UTF-8"));  
//            entity.addPart("myfiles", fileBody);   
//            System.out.println(entity.getContentType());
//            
//            put.setEntity(entity);  
//            
//            HttpResponse response = httpclient.execute(put);  
//            if(HttpStatus.SC_OK==response.getStatusLine().getStatusCode()){    
//                  
//                HttpEntity entitys = response.getEntity();  
//                if (entity != null) {  
//                    System.out.println(entity.getContentLength());  
//                    System.out.println(EntityUtils.toString(entitys));  
//                }  
//            }  
//            httpclient.getConnectionManager().shutdown();  
	    	
	    	
	    	
            
	    	
	    	
//	    	
//	    	HttpClient httpclient = new DefaultHttpClient();  
//            HttpPut put = new HttpPut(temp);  
//            CommonsMultipartFile cf= (CommonsMultipartFile)file; //���myfile��MultipartFile��
//            DiskFileItem fi = (DiskFileItem)cf.getFileItem(); 
//            File file2 = fi.getStoreLocation(); 
////            HadoopUploadFile.upload(temp, file2);  
//            FileBody fileBody = new FileBody(file2);
//            System.out.println(fileBody.getContentLength());
//            MultipartEntity entity = new MultipartEntity();  
//            entity.addPart("myfiles", fileBody);
//            HttpResponse response = httpclient.execute(put); 
//	    	
//		    int statusCode = response.getStatusLine().getStatusCode();
//		    
//		    if(statusCode!=201){
//		    	throw new HttpException();
//		    }
//	    httpclient.getConnectionManager().shutdown();
	  
	
	}
	
	public HttpResponse readFile(String fileName) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException, HttpException{

		URL url = new URL(LocalHost+fileName+"?op=OPEN&user.name="+hdfsUserName);
		HttpResponse hrp =hcu.doGetWithoutRedirect(url.toString());
		    if((hrp.getStatusLine().getStatusCode()) != 307){
	        	throw new HttpException(hrp.getStatusLine().getStatusCode()+"\t"+hrp.getStatusLine().getReasonPhrase());
				
		    }
	    Header aa[] = hrp.getHeaders("Location");
	    String temp = aa[0].getValue();
	   
	    	temp=temp.replaceAll("datanode1", "192.168.121.35");
	    	temp=temp.replaceAll("master", "192.168.121.34");
	    	temp=temp.replaceAll("datanode2", "192.168.121.36");
	    	temp=temp.replaceAll("datanode3", "192.168.121.37");
	    	temp=temp.replaceAll("false", "true");
	    	
	        HttpClient httpclient = new DefaultHttpClient();  
//			FileEntity fileEntity = new FileEntity(file2, "application/octet-stream");
			HttpGet get2 = new HttpGet(temp);
			get2.addHeader("Accept", "application/octet-stream");
//			put2.setEntity(fileEntity);
			HttpResponse response2 = httpclient.execute(get2);
			if(200==response2.getStatusLine().getStatusCode()){   
                return response2;
            }else{
            	return null;
            }
	}
	
	
//	
//	public String[] getAllExistFile() throws Exception {
//		Queue<String> dirQueue = new LinkedList<String>();
//		dirQueue.add("");
//		List<String> allFile = new ArrayList<String>();
//		String current;
//		String dir[];
//		String files[];
//		while(!dirQueue.isEmpty()){
//			current = dirQueue.poll();
//			dir=getDir(current);
//			for (String string : dir) {
//				dirQueue.add(current+string+"/");
//			}
//			files=getFile(current);
//			for (String string : files) {
//				allFile.add(current+string);
//			}
//			
//		}
//		String aa[] = new String[allFile.size()];
//	    for (int i=0;i<allFile.size();i++) {
//			aa[i] = allFile.get(i);
//		}
//	    
//	    
//	    return aa;
//		
//	}
//	
////	public boolean mkNewFile2(String dirName,String fileName) throws Exception {
////		URL url = new URL("http://192.168.121.35:50070/webhdfs/v1/user/cdhfive/?user.name=cdhfive&op=LISTSTATUS");
////		
////	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
////	    connet.setRequestMethod("GET");
////	    int aa= connet.getResponseCode();
////	    System.out.println(connet.getResponseCode());
////	    return true;
////	}
//	
//	
//	public String[] getDir(String dir) throws Exception{
//		URL url = new URL(LocalHost+dir+"?op=LISTSTATUS&user.name="+hdfsUserName);
//	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
//	    if(connet.getResponseCode() != 200){
//	        throw new IOException(connet.getResponseMessage());
//	    }
//	    BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream()));
//	    StringBuilder  sb  = new StringBuilder();
//	    String line;
//	    while((line = brd.readLine()) != null){
//	        sb.append(line);
//	    }
//	    brd.close();
//	    connet.disconnect();
//	    JSONObject jsonObj =new JSONObject().fromObject(sb.toString());
//	    JSONObject jsonObj2=new JSONObject().fromObject(jsonObj.get("FileStatuses").toString());
//	    java.util.List<Map<String, String>> ll= (java.util.List<Map<String,String>>)jsonObj2.get("FileStatus");
//	    List<String> temp = new ArrayList<String>();
//	    for(int i=0;i<ll.size();i++) {
//			Map<String, String> item = (Map<String, String>) ll.get(i);
//			if(item.get("type").equals("DIRECTORY"))
//				temp.add(item.get("pathSuffix"));
//		}
//	    String aa[] = new String[temp.size()];
//	    for (int i=0;i<temp.size();i++) {
//			aa[i] = temp.get(i);
//		}
//	    
//	    
//	    return aa;
//	}
//	
	public String[] getFile(String dir) throws Exception{
		URL url = new URL(LocalHost+dir+"?op=LISTSTATUS&user.name="+hdfsUserName);
	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
	    if(connet.getResponseCode() != 200){
	        throw new IOException(connet.getResponseMessage());
	    }
	    BufferedReader brd = new BufferedReader(new InputStreamReader(connet.getInputStream()));
	    StringBuilder  sb  = new StringBuilder();
	    String line;
	    while((line = brd.readLine()) != null){
	        sb.append(line);
	    }
	    brd.close();
	    connet.disconnect();
	    JSONObject jsonObj =new JSONObject().fromObject(sb.toString());
	    JSONObject jsonObj2=new JSONObject().fromObject(jsonObj.get("FileStatuses").toString());
	    java.util.List<Map<String, String>> ll= (java.util.List<Map<String,String>>)jsonObj2.get("FileStatus");
	    
	    List<String> temp = new ArrayList<String>();
	    for(int i=0;i<ll.size();i++) {
			Map<String, String> item = (Map<String, String>) ll.get(i);
			if(item.get("type").equals("FILE"))
				temp.add(item.get("pathSuffix"));
		}
	    String aa[] = new String[temp.size()];
	    for (int i=0;i<temp.size();i++) {
			aa[i] = dir+"/"+temp.get(i);
		}
	    
	    
	    return aa;
	}
//
//	public boolean mkNewFile(String dirName,String fileName) throws Exception {
//		URL url = new URL("http://192.168.121.82:50075/webhdfs/v1/"+dirName+"/"+fileName+"?op=CREATE&user.name="+hdfsUserName+"&namenoderpcaddress=192.168.121.81:9000");
//	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
//	    connet.setRequestMethod("PUT"); 
//	    if(connet.getResponseCode() != 201){
//	        throw new IOException(connet.getResponseMessage());
//	    }else{
//			return true;
//		}
//	}
//
//	public boolean appendToFile(String fileName,String appendString) throws Exception {
//		URL url = new URL("http://192.168.121.82:50075/webhdfs/v1/"+fileName+"?op=APPEND&data=true&user.name=="+hdfsUserName+"&namenoderpcaddress=192.168.121.81:9000");
//	    HttpURLConnection connet = (HttpURLConnection) url.openConnection();
//	    connet.setDoOutput(true);
//	    connet.setUseCaches(false);
//	    connet.setConnectTimeout(10000); //���ӳ�ʱΪ10��
//	    connet.setRequestMethod("POST");
//	    connet.setRequestProperty("Content-Type",
//				"multipart/form-data;");
//	    ds = new DataOutputStream(connet.getOutputStream()); 
//	    ds.writeBytes(appendString + "\r\n");
//	    try {
//	    	connet.connect();
//		} catch (SocketTimeoutException e) {
//			// something
//			throw new RuntimeException();
//		}
//	    if(connet.getResponseCode() != 200){
//	        throw new IOException(connet.getResponseMessage());
//	    }else{
//			return true;
//		}
//	    
//	}

}


