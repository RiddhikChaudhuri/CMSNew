package com.cbsinc.cms.publisher.controllers;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko. Konstantin Grabko is
 * Owner and author this code. You can not use it and you cannot change it without written
 * permission from Konstantin Grabko Email: konstantin.grabko@yahoo.com or
 * konstantin.grabko@gmail.com
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

import java.io.File;
import java.util.Map;
import java.util.Optional;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.PayRequestBody;
import com.cbsinc.cms.dto.pages.response.CMSPayActionResponseDTO;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.OperationAmountBean;
import com.cbsinc.cms.publisher.models.OrderBean;
import com.cbsinc.cms.publisher.models.PayBean;


@RestController
public abstract class PayAction extends CMSObjects{



  public boolean isInternet = true;

  @Autowired
  AuthorizationPageFaced authorizationPageFaced;

  @Autowired
  OrderFaced orderFaced;
  
  private XLogger logger = XLoggerFactory.getXLogger(PayAction.class.getName());

  public PayAction() {
    logger.entry();
    logger.exit();
  }

  
  @GetMapping(name = "/pay", consumes = "application/json", produces = "application/json")
  public CMSPayActionResponseDTO pay(@RequestBody PayRequestBody payRequestBody)throws Exception {
    AuthorizationPageBean authorizationPageBean = getAuthorizationPageBean().get();
    PayBean payBeanId = getPayBean().get();
    OrderBean orderBeanId = getOrderBean().get();
    Optional<OperationAmountBean> operationAmountBean = Optional.empty();
    Map messageMail = getMessageMail().get();


    if (authorizationPageBean!=null || payBeanId!=null || orderFaced!=null)
      return null;

    ResourceBundle resources = null;
    if (resources == null)
      resources = PropertyResourceBundle.getBundle("localization", getHttpServletRsponse().getLocale());

    String Amount = payRequestBody.getAmount();

    if (Amount != null) {
      payBeanId.setAmount(Amount);
    } else {
      payBeanId.setAmount("0");
    }
    Amount = null;

    String currency_id = payRequestBody.getCurrency_id();
    if (currency_id != null) {
      payBeanId.setCurrency_id(currency_id);
      payBeanId.setCurrency_cd(payBeanId.getCurrency_code(currency_id));
    } else {
      payBeanId.setCurrency_id("0");
    }
    currency_id = null;

    String paysystem_id = payRequestBody.getPaysystem_id();
    if (paysystem_id != null) {
      payBeanId.setPaysystem_id(paysystem_id);
      payBeanId.setPaysystem_cd(payBeanId.getPaySystem_code(paysystem_id));
    } else {
      payBeanId.setAmount("0");
    }
    paysystem_id = null;

    authorizationPageFaced.initPaySys_Shop_cd(authorizationPageBean.getSite_id(),authorizationPageBean);
    payBeanId.setDescription(resources.getString("input_money"));
    String user_os = "";
    String header_name = "";
    String header_value = "";
    java.util.Enumeration en = getHttpServletRequest().getHeaderNames();
    while (en.hasMoreElements()) {
      header_name = (String) en.nextElement();
      header_value = getHttpServletRequest().getHeader(header_name);
      user_os = user_os.concat(header_name + "=" + header_value + "\n");
    }
    payBeanId.setAccount_hist_id(operationAmountBean.get().addMoneyStart(payBeanId.getDescription(),
        new Double(payBeanId.getAmount()), payBeanId.getCurrency_id(),
        authorizationPageBean.getIntUserID(), getHttpServletRequest().getRemoteAddr(), user_os,
        orderBeanId.getOrder_id()));


    messageMail.clear();
    messageMail.put("@FirstName", authorizationPageBean.getStrFirstName());
    messageMail.put("@LastName", authorizationPageBean.getStrLastName());
    messageMail.put("@NumberOfOrder", orderBeanId.getOrder_id());
    messageMail.put("@ContactPerson", orderBeanId.getContact_person());
    messageMail.put("@Balans",
        "" + orderFaced.getBalans(authorizationPageBean.getIntUserID()));
    messageMail.put("@Phone", orderBeanId.getshipment_phone());
    messageMail.put("@Address", orderBeanId.getshipment_address());
    messageMail.put("@City", orderBeanId.getcity_fullname());
    messageMail.put("@Contry", orderBeanId.getcountry_fullname());
    messageMail.put("@CustomerEmail", orderBeanId.getshipment_email());
    messageMail.put("@CustomerFax", orderBeanId.getshipment_fax());
    messageMail.put("@CustomerCommentariy", orderBeanId.getshipment_description());
    messageMail.put("@ProductCount",
        "" + orderFaced.getProductsListSize(getHttpServletRequest(), orderBeanId));
    messageMail.put("@IPAddress", "" + getHttpServletRequest().getRemoteAddr());
    messageMail.put("@HTTPHEAD", "" + user_os);

    System.out.println("code pay: " + payBeanId.getPaysystem_cd());
    System.out.println("code pay1: " + resources.getString(payBeanId.getPaysystem_cd()));
    messageMail.put("@PaySystem", "" + resources.getString(payBeanId.getPaysystem_cd()));
    messageMail.put("@Amount", "" + payBeanId.getAmount());
    messageMail.put("@Currency", "" + payBeanId.getCurrency_lable(payBeanId.getCurrency_id()));

    String sitePath = (String) getHttpSession().getAttribute("site_path");
    String shopOrder =
        sitePath + File.separatorChar + "mail" + File.separatorChar + "ShopPayment.txt";
    String clientOrder =
        sitePath + File.separatorChar + "mail" + File.separatorChar + "ClientPayment.txt";
    String attachFile = sitePath + File.separatorChar + "mail" + File.separatorChar + "info.txt";


    payBeanId.setStatusInrocess(orderBeanId.getOrder_id());
    return new CMSPayActionResponseDTO(authorizationPageBean, payBeanId);
  }

  public boolean isNumber(String tmp) {
    if (tmp == null)
      return false;
    String IntField = "0123456789";
    for (int i = 0; i < tmp.length(); i++) {

      if (IntField.indexOf(tmp.charAt(i)) == -1) {
        if (tmp.charAt(i) != '-' && i != 0)
          return false;
      }
    }
    return true;
  }
}
