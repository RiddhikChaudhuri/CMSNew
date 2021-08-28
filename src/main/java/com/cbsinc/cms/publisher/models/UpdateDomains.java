package com.cbsinc.cms.publisher.models;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code. You can not use it and you
 * cannot change it without written permission from Konstantin Grabko Email:
 * konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
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
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
@Component
public class UpdateDomains implements java.io.Serializable {

	private static final long serialVersionUID = 8160271689486247546L;
	
	@Value("${createdomains.delay}")
    private String createdomains_delay;

	//static private Logger log = Logger.getLogger(UpdateDomains.class);
	private XLogger logger = XLoggerFactory.getXLogger(UpdateDomains.class.getName());


	long delay = 60000;

//	public UpdateDomains() {
//		createDomainsInServerXmlFile();
//	}
//	
	@Autowired
	AuthorizationPageFaced authorizationPageFaced ;

//    transient java.util.TimerTask t = new java.util.TimerTask() {
//      public void run() {
//       authorizationPageFaced;
//      }
//    };
//
//	public static void main(String[] args) {
//
//	}
//
//	public void createDomainsInServerXmlFile() {
//		delay = Long.parseLong(createdomains_delay.trim());
//		java.util.Timer timer = new java.util.Timer();
//		timer.scheduleAtFixedRate(t, 0, delay);
//	}

}
