package com.cbsinc.cms.publisher.controllers;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.OrderBean;
import com.cbsinc.cms.publisher.models.PrePayBean;
import io.swagger.annotations.ApiOperation;


@RestController
public class PrePayAction extends CMSObjects{
  
  private XLogger logger = XLoggerFactory.getXLogger(PrePayAction.class.getName());
	  
    @Autowired
    AuthorizationPageFaced authorizationPageFaced;
    
	
	public boolean isInternet = true ;
	
	public PrePayAction() {
	  logger.entry();
	  logger.exit();
	}

	@GetMapping("/doGetPrePay")
	@ApiOperation(value = "Get For Pre Payment Controller",notes = "Returns Pre Payment Result in Json Format based on Request")
	public @ResponseBody PrePayBean action() throws Exception {
		OrderBean orderBean =getOrderBean().get();
	    PrePayBean prePayBean = getPrePayBean().get();
		prePayBean.setSelectCurrencyListXML(authorizationPageFaced.getXMLDBList("Pay.jsp?currency_id","currency", orderBean.getOrder_currency_id()  ,"SELECT currency_id , currency_desc FROM currency  WHERE active = true","currency_id","currency_desc")); 
        prePayBean.setSelectPaysystemListXML(authorizationPageFaced.getXMLDBList(
            "Pay.jsp?paysystem_id", "paysystem", "1",
            "SELECT paysystem.paysystem_id , paysystem.description FROM  paysystem WHERE paysystem.active = true","paysystem.paysystem_id","paysystem.description"));
        logger.exit();
        return null;
      }

}
