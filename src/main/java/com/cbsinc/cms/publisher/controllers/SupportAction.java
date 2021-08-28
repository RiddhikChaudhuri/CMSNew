package com.cbsinc.cms.publisher.controllers;

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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.SupportRequest;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;

@RestController
@PropertySources(value = {
    @PropertySource("classpath:sequence.properties"),
    @PropertySource("classpath:SetupApplicationResources.properties")
  })
public class SupportAction extends CMSObjects{

    @Autowired
	AuthorizationPageFaced authorizationPageFaced;
        
    @Value("${support_mail}")
    private String support_mail;
	
	private XLogger logger = XLoggerFactory.getXLogger(FDResponceAction.class.getName());

	public SupportAction() {
	  logger.entry();
	  logger.exit();
	}

	@PostMapping(value="/doPostSupport")
	public void doPost(@RequestBody SupportRequest supportRequest)
			throws Exception {
		action(supportRequest);
	}

	@GetMapping(value="/doGetSupport")
	public void doGet(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {
		

	}

	private void action(SupportRequest supportRequest)throws Exception {

		HttpSession session;
		AuthorizationPageBean authorizationPageBeanId = null;

		String email = "";
		String problem = "";
		String person = "";
		String mailto = support_mail;

		session = getHttpSession();
		authorizationPageBeanId = getAuthorizationPageBean().get();

		email = supportRequest.getEmail();
		supportRequest.setEmail(email);
		problem =supportRequest.getProblem();
		supportRequest.setProblem(problem);
		person = supportRequest.getPerson();
		supportRequest.setPerson(person);
		supportRequest.setMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("message_send_well"));

		MessageSender mqSender = new MessageSender(session, SendMailMessageBean.messageQuery);
		Message message = new Message();
		message.put("to", mailto);
		message.put("mailfrom", email);
		message.put("subject", "Tech support");
		message.put("body", problem);
		message.put("fromperson", person);
		mqSender.send(message);

	}

}
