package com.cbsinc.cms.publisher.controllers;

import java.util.Map;

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

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.AuthorizationRequestForm;
import com.cbsinc.cms.dto.pages.response.CMSAuthorizationPageModel;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;


@RestController
public class AuthorizationAction extends CMSObjects{

  
  private XLogger logger = XLoggerFactory.getXLogger(AuthorizationAction.class.getName());
  
  
  @Autowired
  AuthorizationPageFaced authorizationPageFaced;
  
  @Autowired
  public AuthorizationAction() {
    logger.entry();
    logger.exit();
  }
  

  @PostMapping(value="/doPostAuthorization",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
  public CMSAuthorizationPageModel postAuthorization(@RequestBody AuthorizationRequestForm authorizationRequestForm) throws Exception {
        logger.entry(authorizationRequestForm);

		HttpServletResponse response = getHttpServletRsponse();
		HttpServletRequest request  = getHttpServletRequest();
		Map<String,String> messageMail = new HashedMap();
		ResourceBundle	locale_resource = PropertyResourceBundle.getBundle("localization", response.getLocale());
		AuthorizationPageBean authorizationPageBeanId =((AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId"));


        authorizationPageBeanId.setStrLogin(authorizationRequestForm.getLogin());
        authorizationPageBeanId.setStrPasswd(authorizationRequestForm.getPassword());
        authorizationPageBeanId.setStrMessage("" + authorizationRequestForm.getMessage());
        authorizationPageBeanId.setSite_id(authorizationRequestForm.getSiteId(),
            authorizationPageFaced);

		messageMail.clear();
		messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName());
		messageMail.put("@LastName", authorizationPageBeanId.getStrLastName());
		messageMail.put("@Balans", "" + authorizationPageFaced.getBalans(authorizationPageBeanId.getIntUserID()));
		messageMail.put("@Phone", authorizationPageBeanId.getStrPhone());
		messageMail.put("@City", authorizationPageBeanId.getStrCity());
		messageMail.put("@Contry", authorizationPageBeanId.getStrCountry());
		messageMail.put("@CustomerEmail", authorizationPageBeanId.getStrEMail());
		messageMail.put("@CustomerFax", authorizationPageBeanId.getStrFax());

		String session_id = authorizationPageFaced.getCokieSessionId((HttpServletRequest) request,
				(HttpServletResponse) response);

		if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
				authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, session_id)
				&& authorizationPageBeanId.getStrLogin().length() != 0) {

			authorizationPageBeanId.setStrMessage(locale_resource.getString("reg.login1.text"));
			authorizationPageFaced.initUserSite(authorizationPageBeanId.getIntUserID(), authorizationPageBeanId);

			return new CMSAuthorizationPageModel(authorizationPageBeanId);

		}


		if (authorizationPageBeanId.getIntLogined() == 2 && authorizationPageBeanId.getRezalt_reg() == 0
				&& !authorizationPageBeanId.getStrPasswd().equals(SiteRole.GUEST_PASSWORD)) {
			authorizationPageBeanId.setStrMessage(locale_resource.getString("reg.login1.text") + " "
					+ authorizationPageBeanId.getStrLogin() + " " + locale_resource.getString("reg.login3.wrong"));
			authorizationPageBeanId.setIntUserID(Long.valueOf(SiteRole.GUEST_ID));
			authorizationPageBeanId.setIntLevelUp(Long.valueOf(SiteRole.GUEST_ROLE_ID));
			authorizationPageBeanId.setStrPasswd(SiteRole.GUEST_PASSWORD);
			authorizationPageBeanId.setStrLogin(SiteRole.GUEST);
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
		authorizationPageBeanId.setSelect_menu_catalog(authorizationPageFaced.getMenuXMLDBList(
				"Productlist.jsp?catalog_id", "menu", authorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable , parent_id  from catalog   where  active = true and parent_id = 0 and site_id = "
						+ authorizationPageBeanId.getSite_id() + " and lang_id = "
						+ authorizationPageBeanId.getLang_id()
						+ " or parent_id in (select catalog_id   from catalog   where  active = true and site_id = "
						+ authorizationPageBeanId.getSite_id() + "  and parent_id = 0 )"));
		return new CMSAuthorizationPageModel(authorizationPageBeanId);

	}

}
