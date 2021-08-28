package com.cbsinc.cms.publisher.controllers;

import javax.servlet.http.HttpServletRequest;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;

@RestController(value="/doPostSetDomain")
public class SetDomainAction extends CMSObjects  {

	public boolean isInternet = true;
	
	@Autowired
	AuthorizationPageFaced authorizationPageFaced;
	
	  private XLogger logger = XLoggerFactory.getXLogger(SetDomainAction.class.getName());


      public SetDomainAction() {
        logger.entry();
        logger.exit();
      }


	@PostMapping
	public String action(String domain) throws Exception {
	    logger.entry(domain);
		AuthorizationPageBean authorizationPageBean = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
		HttpServletRequest request  = getHttpServletRequest() ;
		
		if (authorizationPageBean == null) return "";
		request.setCharacterEncoding("UTF-8");
		String host = request.getParameter("domain");
		if (host != null) {
			authorizationPageBean.setHost(host);
			authorizationPageFaced.saveNewDomain(authorizationPageBean.getHost(), authorizationPageBean.getSite_id());
			request.setAttribute("message", "host name changed successfully.");
		}
		
		return authorizationPageBean.getHost();
		
	}

}
