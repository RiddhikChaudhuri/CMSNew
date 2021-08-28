package com.cbsinc.cms.publisher.models;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code.
 * You can not use it and you cannot change it without written permission from Konstantin Grabko
 * Email: konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002-2014
 * </p>
 * <p>
 * Company: CENTER BUSINESS SOLUTIONS INC 
 * </p>
 * 
 * @author Konstantin Grabko
 * @version 1.0
 */

import java.util.Enumeration;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;

import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageListener;

/*
 *   <listener>
 *   <listener-class>com.cbsinc.cms.FrontMessageContoller</listener-class>
 *   </listener>
 *
 */

public class FrontMessageContoller extends HttpServlet implements HttpSessionAttributeListener {

	//transient private Logger log = Logger.getLogger(ServletSiteEvent.class);
	private XLogger log = XLoggerFactory.getXLogger(FrontMessageContoller.class.getName());
	
	transient ResourceBundle jms_beans_resources = null;
	HashMap controllerMap = new HashMap();

	public FrontMessageContoller() {
		jms_beans_resources = PropertyResourceBundle.getBundle("jms_beans");
	}

	public void init() throws ServletException {
		// if( jms_beans_resources == null ) jms_beans_resources =
		// PropertyResourceBundle.getBundle("jms_beans");

	}

	public void attributeAdded(HttpSessionBindingEvent se) {

		String message_query = "";

		if (se.getName() instanceof String)
			message_query = se.getName();
		else
			return;

		if (isExistKey(jms_beans_resources.getKeys(), message_query)) {
			se.getSession().removeAttribute(se.getName());
			Message message = null;
			if (se.getValue() instanceof Message)
				message = (Message) se.getValue();
			else
				message = (Message) new HashMap();

			Object obj = null;
			if (!controllerMap.containsKey(message_query)) {
				String className = jms_beans_resources.getString(message_query).trim();
				obj = createObject(className);
				// if(obj == null) throw new Exception("Class " + className + " is not found."
				// );
				controllerMap.put(message_query, obj);
			} else {
				obj = controllerMap.get(message_query);
			}

			if (obj != null) {
				if (obj instanceof MessageListener) {
					((MessageListener) obj).onMessage(message, se.getSession().getServletContext(), se.getSession());
				}
			}

			// se.getSession().removeAttribute(se.getName());
		}

	}

	public void attributeRemoved(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub

	}

	public void attributeReplaced(HttpSessionBindingEvent se) {
		// TODO Auto-generated method stub

	}

	public Object createObject(String className) {
		Object obj = null;
		try {
			Class cls = Class.forName(className);
			obj = cls.newInstance();
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return obj;
	}

	boolean isExistKey(Enumeration en, String key) {
		while (en.hasMoreElements()) {
			String _key = (String) en.nextElement();
			if (_key.equals(key))
				return true;
		}

		return false;

	}
}
