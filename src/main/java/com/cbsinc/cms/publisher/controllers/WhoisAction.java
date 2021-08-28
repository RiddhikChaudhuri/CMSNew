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

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.WhoIsRequestBody;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.services.net.FingerClient;
import com.cbsinc.cms.services.whois.DomainState;



@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class WhoisAction extends CMSObjects{

	String[] hight_domains = { ".org.uk", ".net", ".us", ".tel", ".biz", ".org", ".de", ".net.uk", ".or.at", ".info",
			".lc", ".eu", ".plc.uk", ".mobi", ".bz", ".name", ".at", ".ag", ".mn", ".hn", ".vc", ".sc", ".me", ".asia",
			".me.uk", ".cn", ".co.at", ".co.uk", ".com", ".ltd.uk" };
	@Autowired
	AuthorizationPageFaced authorizationPageFaced;
	
	@Value("${support_mail}")
	private String support_mail;

	public WhoisAction() {

	}

    @PostMapping(value="/doPostWhois",produces = "application/json",consumes = "application/json")
	public void doPost(@RequestBody WhoIsRequestBody whoISRequestRequest)
			throws Exception {
		action(whoISRequestRequest);
	}

    @GetMapping(value="/doGetWhois",produces = "application/json",consumes = "application/json")
	public void doGet(@RequestBody WhoIsRequestBody whoISRequestRequest)throws Exception {
		HttpSession session;
		List<DomainState> domainList = null;
		AuthorizationPageBean authorizationPageBeanId = null;
		domainList = (List<DomainState>) getHttpSession().getAttribute("domainList");
		authorizationPageBeanId = getAuthorizationPageBean().get();
		
	}

	private void action(WhoIsRequestBody whoISRequestRequest)throws Exception {

		HttpSession session;
		java.util.LinkedList<DomainState> domainList = null;
		AuthorizationPageBean authorizationPageBeanId = null;
		String handle = "domain:";
		String domain = "";
		String regdomain = "";

		domainList = whoISRequestRequest.getDomainList();
		if (domainList == null) {
			domainList = new java.util.LinkedList();
			whoISRequestRequest.setDomainList(domainList);
		}
		authorizationPageBeanId = getAuthorizationPageBean().get();

	
		regdomain = whoISRequestRequest.getRegdomain();
		if (regdomain != null) {
		    //todo
			//MessageSender mqSender = new MessageSender(request.getSession(), SendMailMessageBean.messageQuery);
			Message message = new Message();
			message.put("to", support_mail);
			message.put("mailfrom", authorizationPageBeanId.getStrEMail());
			message.put("subject", "Inform about buy domain");
			message.put("body", regdomain + "\r\n Phone: " + authorizationPageBeanId.getStrPhone() 
			+ "\r\n Loing: "+ authorizationPageBeanId.getStrLogin());
			message.put("fromperson",authorizationPageBeanId.getStrFirstName() + "" + authorizationPageBeanId.getStrLastName());
			//mqSender.send(message);
			whoISRequestRequest.setDomain(regdomain);
			whoISRequestRequest.setMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("your_application_has_send"));
			return;
		}

		domain = whoISRequestRequest.getDomain();
		String[] domain_parts = domain.split("\\.");
		if (domain_parts.length > 1) {
			int level = getWhatLevelDomain(domain);
			switch (level) {
			case 0: {
			  whoISRequestRequest.setMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("unknown_zone"));
	            whoISRequestRequest.setDomain(domain);
				return;
			}
			case 1:
				domain = domain_parts[domain_parts.length - 2].concat(".")
						.concat(domain_parts[domain_parts.length - 1]);
				break;
			case 2:
				domain = domain_parts[domain_parts.length - 3].concat(".").concat(domain_parts[domain_parts.length - 2])
						.concat(".").concat(domain_parts[domain_parts.length - 1]);
				break;
			}

		} else {
		  whoISRequestRequest.setMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("unknown_zone"));
          whoISRequestRequest.setDomain(domain);
			return;
		}

        whoISRequestRequest.setDomain(domain);

		if (domainList.size() > 20)
			domainList.removeFirst();

		FingerClient finger = new FingerClient();
		// We want to timeout if a response takes longer than 60 seconds
		finger.setDefaultTimeout(60000);
		try {
			InetAddress address = InetAddress.getByName("whois.joker.com");
			finger.connect(address, 4343);
			String[] status = finger.query(false, handle + domain).split(" ");
			DomainState domainState = new DomainState();
			domainState.setDomain(domain);
			domainState.setStatus(authorizationPageBeanId.getLocalization(getServletContext()).getString(status[1].trim()));
			domainState.setFree(status[1].trim().startsWith("free"));
			domainList.add(domainState);
		} catch (IOException e) {
			authorizationPageBeanId.setStrMessage(e.getLocalizedMessage());
			System.err.println("Error I/O exception: " + e.getMessage());

		} finally {
			try {
				finger.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	int getWhatLevelDomain(String domain) {
		int level = 0;

		for (int i = 0; hight_domains.length > i; i++) {
			if (domain.endsWith(hight_domains[i])) {
				if (domain.endsWith(".uk"))
					return hight_domains[i].split(".").length;
				level = level + 1;
			}
		}

		return level;
	}

}
