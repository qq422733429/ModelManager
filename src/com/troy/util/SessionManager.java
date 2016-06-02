package com.troy.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.troy.entity.UserSession;
import com.troy.sessionManager.JobStatusManager;

public class SessionManager extends Thread {
	
	Map mapSessions;
	static Logger logger = Logger.getLogger (SessionManager.class.getName());
	
	/**
	 * 新线程的调用接口类，该类用于清除超过两小时未使用的token
	 * @param mapSession
	 */
	public SessionManager(Map mapSession){
		mapSessions= mapSession;
	}
	
	
	
	
	
	/**
	 * 该方法用于定时清除掉过期的token，每10分钟运行一次，若有token超过两小时为使用，则将其从mapSeession图中删除
	 */
	public void run(){		
		do{
			try {	
				 logger.info("当前在线人数："+mapSessions.size());
				 Set<String> set = mapSessions.keySet();
				 List<String> keys = new ArrayList<String>(set);
				 for (String string : keys) {
					 UserSession Session=(UserSession)mapSessions.get(string);
					 if(Session.getExpires_at().getTime().before(new Date())){
						 	logger.info("清除超时在线用户：："+string);
							mapSessions.remove(string);
					 }
				}
				Thread.sleep(10*60*1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		 }while(true);
	}
}
