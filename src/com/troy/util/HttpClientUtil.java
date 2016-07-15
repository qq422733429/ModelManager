package com.troy.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;

public class HttpClientUtil {

	HttpClient httpClient = null;
	String X_TOKEN="";
	
	public void init(String token) throws KeyManagementException, NoSuchAlgorithmException, InterruptedException{
		httpClient = new SSLClient();
		X_TOKEN = token;
	}
	
	public HttpResponse doGet(String url) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{
		
			HttpGet httpGET = new HttpGet(url);
			httpClient = new SSLClient();
			httpGET.releaseConnection();
			httpGET.setHeader("X-Auth-Token",X_TOKEN);
			httpGET.setHeader("Accept","application/json");
			httpGET.setHeader("Content-Type","application/json");
			HttpResponse httpResponse = httpClient.execute(httpGET);
			return  httpResponse;
	}
	public HttpResponse doPost(String url,String bodys) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{  
          
        	HttpPost httpPost = new HttpPost(url); 
            httpClient = new SSLClient();
            httpPost.releaseConnection();
            httpPost.setHeader("X-Auth-Token",X_TOKEN);
            httpPost.setHeader("Accept","application/json");
            httpPost.setHeader("Content-Type","application/json; charset=utf-8");
            StringEntity se = new StringEntity(bodys, "UTF-8");
            httpPost.setEntity(se);
            HttpResponse httpResponse =httpClient.execute(httpPost);
            
            return httpResponse;
    } 
	public HttpResponse doPut(String url) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{  
        
        	HttpPut httpPut = new HttpPut(url);
            httpClient = new SSLClient();
            httpPut.releaseConnection();
            httpPut.setHeader("X-Auth-Token",X_TOKEN);
            httpPut.setHeader("Accept","application/json");
            httpPut.setHeader("Content-Type","application/json");
            HttpResponse httpResponse =httpClient.execute(httpPut);
            return httpResponse;
    }
	public HttpResponse doDelete(String url) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{  
        
        	HttpDelete httpDelete = new HttpDelete(url);
            httpClient = new SSLClient();  
            httpDelete.releaseConnection();
            httpDelete.setHeader("X-Auth-Token",X_TOKEN);
            httpDelete.setHeader("Accept","application/json");
            httpDelete.setHeader("Content-Type","application/json");
            return httpClient.execute(httpDelete);
    }
	
	public HttpResponse doPatch(String url,String bodys) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{  
        
			HttpPatch HttpPatch = null;
            httpClient = new SSLClient();  
            HttpPatch = new HttpPatch(url);
            HttpPatch.setHeader("X-Auth-Token",X_TOKEN);
            HttpPatch.setHeader("Accept","application/json");
            HttpPatch.setHeader("Content-Type","application/json");
            StringEntity se = new StringEntity(bodys);
            HttpPatch.setEntity(se);
            return httpClient.execute(HttpPatch);
    }
	
	public HttpResponse doGetWithoutRedirect(String url) throws KeyManagementException, NoSuchAlgorithmException, ClientProtocolException, IOException{
		
		HttpGet httpGET = null;
		httpClient = new SSLClient();
		httpGET = new HttpGet(url);
		httpGET.setHeader("Content-Type","application/json");
		httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		return httpClient.execute(httpGET);
	
}
	
	
	
	
	
}