package com.cbsinc.cms.publisher.controllers;

import java.util.Enumeration;

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
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.response.CMSPayBeanModel;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.OperationAmountBean;
import com.cbsinc.cms.publisher.models.OrderBean;
import com.cbsinc.cms.publisher.models.PayBean;

@RestController
public class FDPayAction extends CMSObjects {
  private XLogger logger = XLoggerFactory.getXLogger(FDPayAction.class.getName());
    
    @Autowired
    public FDPayAction() {
      logger.entry();
      logger.exit();
    }
	public boolean isInternet = true;
	
	@Autowired
	AuthorizationPageBean authorizationPageBean;
	
	
	@Autowired
	OperationAmountBean operationAmountBean;
	
	@Autowired
	OrderFaced orderFaced;
    
	@GetMapping(value="/doGetFDPay", consumes = "application/json", produces = "application/json")
	public CMSPayBeanModel getFdPayAction() throws Exception {


		OrderBean orderBean = getOrderBean().get();
        PayBean payBean = getPayBean().get();
		ResourceBundle	resources = PropertyResourceBundle.getBundle("localization");
		logger.entry();
		if ((authorizationPageBean != null) || (payBean != null))
			return null;
		
	
		payBean.setDescription(resources.getString("Purchase"));

		String user_os = "";
		String header_name = "";
		String header_value = "";
		Enumeration<String> en = getHttpServletRequest().getHeaderNames();
		while (en.hasMoreElements()) {
			header_name = (String) en.nextElement();
			header_value = getHttpServletRequest().getHeader(header_name);
			user_os = user_os.concat(header_name + "=" + header_value + "\n");
		}
		
		payBean.setAccount_hist_id(operationAmountBean.addMoneyStart("Purchase",
				Double.parseDouble(orderBean.getorder_amount()), orderBean.getOrder_currency_id(),
				authorizationPageBean.getIntUserID(), getHttpServletRequest().getRemoteAddr(), user_os, orderBean.getOrder_id()));
		payBean.setStatusInrocess(orderBean.getOrder_id());
		logger.exit();
      return new CMSPayBeanModel(payBean);
	}

}
