package com.cbsinc.cms.jms.controllers;

import javax.servlet.http.HttpSession;

public class MessageSender 
{

	HttpSession httpSession  = null ;
	String messageQuery = null ;
	
	public MessageSender(HttpSession httpSession , String messageQuery ) throws Exception 
	{
		this.httpSession = httpSession ;
		if( messageQuery == null ) new Exception("You must define varible ( String messageQuery ) in your XXXMessageBean  a class") ;
		this.messageQuery = messageQuery ;
	}

	private MessageSender()
	{
		
	}

	
	public void send( Message message)
	{
		httpSession.setAttribute(messageQuery , message ) ;
	}

}
