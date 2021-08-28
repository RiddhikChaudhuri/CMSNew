package com.cbsinc.cms.publisher.controllers;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.InstallForm;
import com.cbsinc.cms.dto.pages.response.CMSInstallResponseModel;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CreateShopBean;


@RestController
public class InstallAction extends CMSObjects{
    
  private XLogger logger = XLoggerFactory.getXLogger(InstallAction.class.getName());
  
  @Autowired
  CreateShopBean createShopBean;
  
  
  @Autowired
  AuthorizationPageFaced authorizationPageFaced;
  
  public InstallAction() {
    logger.entry();
    logger.exit();
  }
  
	private HttpSession session;
	
	
	private Map messageMail;

	
    @PostMapping(value="/doPostInstall", consumes = "application/json", produces = "application/json")
	public CMSInstallResponseModel doPost(@RequestBody InstallForm installForm)
			throws Exception {
        logger.entry(installForm);
        AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
		action(installForm,authorizationPageBeanId);
		logger.exit();
		return new CMSInstallResponseModel(authorizationPageBeanId);
	}
    
    @GetMapping(value="/doGetInstall", consumes = "application/json", produces = "application/json")
	public CMSInstallResponseModel doGet()throws Exception {
		session = getHttpSession();
		messageMail = (Map) session.getAttribute("messageMail");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
		logger.entry(messageMail,session,authorizationPageBeanId);
		authorizationPageBeanId.setSelect_city(authorizationPageFaced.getXMLDBList("Authorization.jsp?city_id", "city",
				authorizationPageBeanId.getCity_id(),
				"select  city_id , name  from  city where country_id =" + authorizationPageBeanId.getCountry_id(),"city_id","name"));
		authorizationPageBeanId.setSelect_country(authorizationPageFaced.getXMLDBList("Authorization.jsp?country_id",
				"country", authorizationPageBeanId.getCountry_id(), "select country_id ,name from country","country_id","name"));
		logger.exit();
		return new CMSInstallResponseModel(authorizationPageBeanId);
	}

	public void action(InstallForm installForm,AuthorizationPageBean authorizationPageBeanId)	throws Exception {
	    logger.entry(installForm);
		session = getHttpSession();
		messageMail = getMessageMail().get();

		
		if (installForm.getStrLogin() != null)
			authorizationPageBeanId.setStrLogin(installForm.getStrLogin());
		if (installForm.getStrPasswd() != null)
			authorizationPageBeanId.setStrPasswd(installForm.getStrPasswd());
		if (installForm.getStrCPasswd() != null)
			authorizationPageBeanId.setStrCPasswd(installForm.getStrCPasswd());
		if (installForm.getStrFirstName() != null)
			authorizationPageBeanId.setStrFirstName(installForm.getStrFirstName());
		if (installForm.getStrLastName() != null)
			authorizationPageBeanId.setStrLastName(installForm.getStrLastName());
		if (installForm.getStrCompany() != null)
			authorizationPageBeanId.setStrCompany(installForm.getStrCompany());
		if (installForm.getStrEMail() != null)
			authorizationPageBeanId.setStrEMail(installForm.getStrEMail());
		if (installForm.getStrPhone() != null)
			authorizationPageBeanId.setStrPhone(installForm.getStrPhone());
		if (installForm.getStrMPhone() != null)
			authorizationPageBeanId.setStrMPhone(installForm.getStrMPhone());
		if (installForm.getStrFax() != null)
			authorizationPageBeanId.setStrFax(installForm.getStrFax());
		if (installForm.getAddress() != null)
			authorizationPageBeanId.setAddress(installForm.getAddress());
		if (installForm.getStrWebsite() != null)
			authorizationPageBeanId.setStrWebsite(installForm.getStrWebsite());
		if (installForm.getStrQuestion() != null)
			authorizationPageBeanId.setStrQuestion(installForm.getStrQuestion());
		if (installForm.getStrAnswer() != null)
			authorizationPageBeanId.setStrAnswer(installForm.getStrAnswer());
		if (installForm.getCountry_id() != null)
			authorizationPageBeanId.setCountry_id(installForm.getCountry_id());
		if (installForm.getCity_id() != null)
			authorizationPageBeanId.setCity_id(installForm.getCity_id());
		if (installForm.getCurrency_id() != null)
			authorizationPageBeanId.setCurrency_id(installForm.getCurrency_id());
		

		createShopBean.addSite(authorizationPageBeanId);

		messageMail.clear();
		messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName());
		messageMail.put("@LastName", authorizationPageBeanId.getStrLastName());
		messageMail.put("@Balans", "" + authorizationPageFaced.getBalans(authorizationPageBeanId.getIntUserID()));
		messageMail.put("@Phone", authorizationPageBeanId.getStrPhone());
//		    messageMail.put("@Address", orderBeanId.getshipment_address() ) ;
		messageMail.put("@City", authorizationPageBeanId.getStrCity());
		messageMail.put("@Contry", authorizationPageBeanId.getStrCountry());
		messageMail.put("@CustomerEmail", authorizationPageBeanId.getStrEMail());
		messageMail.put("@CustomerFax", authorizationPageBeanId.getStrFax());
		String session_id = authorizationPageFaced.getCokieSessionId(getHttpServletRequest(),getHttpServletRsponse());

		if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
				authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, session_id)
				&& authorizationPageBeanId.getStrLogin().length() != 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(getServletContext()).getString("reg.login1.text"));
			authorizationPageFaced.initUserSite(authorizationPageBeanId.getIntUserID(), authorizationPageBeanId);
		}

		if (authorizationPageBeanId.getIntLogined() == 2 && authorizationPageBeanId.getRezalt_reg() == 0
				&& !authorizationPageBeanId.getStrPasswd().equals(SiteRole.GUEST_PASSWORD)) {
			authorizationPageBeanId
					.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("reg.login1.text")
							+ " " + authorizationPageBeanId.getStrLogin() + " "
							+ authorizationPageBeanId.getLocalization(getServletContext()).getString("reg.login3.wrong"));
			authorizationPageBeanId.setIntUserID(SiteRole.GUEST_ID);
			authorizationPageBeanId.setIntLevelUp(SiteRole.GUEST_ROLE_ID);
			authorizationPageBeanId.setStrPasswd(SiteRole.GUEST_PASSWORD);
			authorizationPageBeanId.setStrLogin(SiteRole.GUEST);
		}
		logger.exit();
	}

}
