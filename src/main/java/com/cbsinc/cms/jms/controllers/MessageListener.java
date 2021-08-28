package com.cbsinc.cms.jms.controllers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public interface MessageListener 
{
	
	public static String messageQuery = null ;
	
	//public  String getMessageQuery ();
	
	public void  onMessage(Message message , ServletContext  applicationContext , HttpSession httpSession );

}
