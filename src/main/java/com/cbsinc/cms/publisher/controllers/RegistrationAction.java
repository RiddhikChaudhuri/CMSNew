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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.RegisterationRequest;
import com.cbsinc.cms.dto.pages.response.CMSAuthorizationPageModel;
import com.cbsinc.cms.jms.controllers.AddUserToMailMessageBean;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;

@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class RegistrationAction extends CMSObjects{

	HttpSession session;
	
	@Autowired
	AuthorizationPageFaced authorizationPageFaced;
	
	
	
	String create_cabinet = "";
	String gen_code = "";

    @Value("${james_register}")
    private String james_register;
    
    @PostMapping(value = "/doPostRegisteration",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSAuthorizationPageModel doPost(@RequestBody RegisterationRequest registerationRequest)
			throws Exception {
        AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
		action(registerationRequest,authorizationPageBeanId);
		ServletContext servletContext = getServletContext();
		HttpServletRequest httpRequest =  getHttpServletRequest();
		HttpServletResponse httpResponse = getHttpServletRsponse();
		
		String session_id = authorizationPageFaced.getCokieSessionId(httpRequest,httpResponse);
		int rezalt_reg = authorizationPageFaced.isRegCorrect(authorizationPageBeanId.getStrLogin(),
				authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId.getStrCPasswd(),
				authorizationPageBeanId.getStrFirstName(), authorizationPageBeanId.getStrLastName(),
				authorizationPageBeanId.getStrCompany(), authorizationPageBeanId.getStrEMail(),
				authorizationPageBeanId.getStrPhone(), authorizationPageBeanId.getStrMPhone(),
				authorizationPageBeanId.getStrFax(), authorizationPageBeanId.getStrIcq(),
				authorizationPageBeanId.getStrWebsite(), authorizationPageBeanId.getStrQuestion(),
				authorizationPageBeanId.getStrAnswer(), null, authorizationPageBeanId.getCountry_id(),
				authorizationPageBeanId.getCity_id(), authorizationPageBeanId.getCurrency_id(), session_id,
				authorizationPageBeanId);
		authorizationPageBeanId.setRezalt_reg(rezalt_reg);
		if (rezalt_reg == 0) {

			if (james_register.contains("true")) {
				MessageSender mqSender = new MessageSender(httpRequest.getSession(), AddUserToMailMessageBean.messageQuery);
				Message message = new Message();
				message.put("user_login", authorizationPageBeanId.getStrLogin());
				message.put("user_password", authorizationPageBeanId.getStrPasswd());
				mqSender.send(message);
			}

			authorizationPageFaced.initUserSite(authorizationPageBeanId.getIntUserID(), authorizationPageBeanId);
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("welcom_after_registration"));

			if (create_cabinet.equals("true"))
				servletContext.getRequestDispatcher("Productlist.jsp?action=create_site").forward(httpRequest, httpResponse);


			return new CMSAuthorizationPageModel(authorizationPageBeanId);
		} else {

			switch (rezalt_reg) {
			case 1:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.login.empty"));
				break;
			case 2:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.password1.empty"));
				break;
			case 12:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.password1.short"));
				break;
			case 10:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.password1.empty"));
				break;
			case 13:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.login.not_english"));
				break;
			case 11:
				authorizationPageBeanId.setStrMessage("select city");
				break;
			case 3:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.firstname.empty"));
				break;
			case 4:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.lastname.empty"));
				break;
			case 5:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.email.empty"));
				break;
			case 6:
				authorizationPageBeanId.setStrMessage("Field Question  is empty");
				break;
			case 7:
				authorizationPageBeanId.setStrMessage("Field Answer  is empty");
				break;
			case 8:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.email.wrong"));
				break;
			case 9:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.password.wrong"));
				break;
			case -1:
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("reg.login1.text")
								+ authorizationPageBeanId.getStrLogin()
								+ authorizationPageBeanId.getLocalization(servletContext).getString("reg.login2.text"));
				break;

			}

		}

		authorizationPageBeanId.setSelect_city(authorizationPageFaced.getXMLDBList("Authorization.jsp?city_id", "city",
				authorizationPageBeanId.getCity_id(),
				"select  city_id , name  from  city where country_id =" + authorizationPageBeanId.getCountry_id()
						+ " and locale = '" + authorizationPageBeanId.getLocale() + "' ","city_id","name"));
		authorizationPageBeanId.setSelect_country(authorizationPageFaced.getXMLDBList("Authorization.jsp?country_id",
				"country", authorizationPageBeanId.getCountry_id(),
				"select country_id ,name from country  where locale = '" + authorizationPageBeanId.getLocale() + "' ","country_id","name"));

		authorizationPageBeanId.setSelect_currency(authorizationPageFaced.getXMLDBList("Authorization.jsp?currency_id",
				"currency", "3", "SELECT currency_id , currency_desc FROM currency  WHERE active = true","currency_id","currency_desc"));
		authorizationPageBeanId.setSelect_site(authorizationPageFaced.getXMLDBList("Authorization.jsp?site_id", "site",
				authorizationPageBeanId.getSite_id(), "SELECT  site_id, host FROM  site WHERE  active = true","site_id","host"));

		authorizationPageBeanId.setIntUserID(1);
		authorizationPageBeanId.setIntLevelUp(1);
		authorizationPageBeanId.setStrPasswd("");
		authorizationPageBeanId.setStrLogin("");
        return new CMSAuthorizationPageModel(authorizationPageBeanId); 

	}

    @GetMapping(value = "/doGetRegisteration",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSAuthorizationPageModel doGet(@RequestBody RegisterationRequest registerationRequest)
			throws Exception {
      AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
      return new CMSAuthorizationPageModel(action(registerationRequest,authorizationPageBeanId));
	}

	public AuthorizationPageBean action(RegisterationRequest registerationRequest,AuthorizationPageBean authorizationPageBeanId)throws Exception {

		session = getHttpSession();
		gen_code = (String) getHttpSession().getAttribute("gen_number");


		if (registerationRequest.getStrLogin() != null)
			authorizationPageBeanId.setStrLogin(registerationRequest.getStrLogin());
		if (registerationRequest.getStrPasswd() != null)
			authorizationPageBeanId.setStrPasswd(registerationRequest.getStrPasswd());
		if (registerationRequest.getStrCPasswd() != null)
			authorizationPageBeanId.setStrCPasswd(registerationRequest.getStrCPasswd());
		if (registerationRequest.getStrFirstName() != null)
			authorizationPageBeanId.setStrFirstName(registerationRequest.getStrFirstName());
		if (registerationRequest.getStrLastName() != null)
			authorizationPageBeanId.setStrLastName(registerationRequest.getStrLastName());
		if (registerationRequest.getStrCompany() != null)
			authorizationPageBeanId.setStrCompany(registerationRequest.getStrCompany());
		if (registerationRequest.getStrEMail() != null)
			authorizationPageBeanId.setStrEMail(registerationRequest.getStrEMail());
		if (registerationRequest.getStrPhone() != null)
			authorizationPageBeanId.setStrPhone(registerationRequest.getStrPhone());
		if (registerationRequest.getStrMPhone() != null)
			authorizationPageBeanId.setStrMPhone(registerationRequest.getStrMPhone());
		if (registerationRequest.getStrFax() != null)
			authorizationPageBeanId.setStrFax(registerationRequest.getStrFax());
		if (registerationRequest.getStrIcq() != null)
			authorizationPageBeanId.setStrIcq(registerationRequest.getStrIcq());
		if (registerationRequest.getStrWebsite() != null)
			authorizationPageBeanId.setStrWebsite(registerationRequest.getStrWebsite());
		if (registerationRequest.getStrQuestion() != null)
			authorizationPageBeanId.setStrQuestion(registerationRequest.getStrQuestion());
		if (registerationRequest.getStrAnswer() != null)
			authorizationPageBeanId.setStrAnswer(registerationRequest.getStrAnswer());
		if (registerationRequest.getCountry_id() != null)
			authorizationPageBeanId.setCountry_id(registerationRequest.getCountry_id());
		if (registerationRequest.getCity_id() != null)
			authorizationPageBeanId.setCity_id(registerationRequest.getCity_id());
		if (registerationRequest.getCurrency_id() != null)
			authorizationPageBeanId.setCurrency_id(registerationRequest.getCurrency_id());
		if (registerationRequest.getSite_id() != null) {
			authorizationPageBeanId.setSite_id(registerationRequest.getSite_id(), authorizationPageFaced);
		}
		
		return authorizationPageBeanId;

	}

}
