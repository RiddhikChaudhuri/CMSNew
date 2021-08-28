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

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.AccountDetalHistoryForm;
import com.cbsinc.cms.dto.pages.response.CMSAccountDetailHistoryModel;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.models.AccountHistoryDetalBean;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;


@RestController
public class AccountDetalHistoryController extends CMSObjects {


  private final XLogger logger = XLoggerFactory.getXLogger(AccountDetalHistoryController.class.getName());


  @Autowired
  OrderFaced orderFaced;
  
  //@Autowired
  public AccountDetalHistoryController() {
    logger.entry();
    logger.exit();
  }
  
  @PostMapping(value = "/doPostAccountDetalHistory")
  public @ResponseBody CMSAccountDetailHistoryModel getAccountHistoryDetal(AccountDetalHistoryForm accountDetalHistoryForm )
      throws Exception {
    logger.entry(accountDetalHistoryForm);
    AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
    if (authorizationPageBeanId == null ) {
      logger.error("Authorization Bean missing in the session");
      return null;
    }
    //requestOpts.setCharacterEncoding("UTF-8");
    AccountHistoryDetalBean accountHistoryDetalBean = orderFaced.getPayment(authorizationPageBeanId.getIntUserID(),authorizationPageBeanId.getIntLevelUp(), accountDetalHistoryForm.getAmountId());
   
    CMSAccountDetailHistoryModel model = new CMSAccountDetailHistoryModel() ;
    model.setActive(accountHistoryDetalBean.getActive());
    model.setAddAmount(accountHistoryDetalBean.getAdd_amount());
    model.setAmountId(accountHistoryDetalBean.getAmount_id());
    model.setComplete(accountHistoryDetalBean.getComplete());
    model.setCurrencyAddLable(accountHistoryDetalBean.getCurrency_add_lable());
    model.setCurrencyAldLable(accountHistoryDetalBean.getCurrency_old_lable());
    model.setCurrencyTotalLable(accountHistoryDetalBean.getCurrency_total_lable());
    model.setDateEnd(accountHistoryDetalBean.getDate_end());
    model.setDateInput(accountHistoryDetalBean.getDate_input());
    model.setDecsription(accountHistoryDetalBean.getDecsription());
    model.setOldAmount(accountHistoryDetalBean.getOld_amount());
    model.setRezultCode(accountHistoryDetalBean.getRezult_cd());
    model.setSysdate(accountHistoryDetalBean.getSysdate());
    model.setTotalAmount(accountHistoryDetalBean.getTotal_amount());
    model.setUserHeader(accountHistoryDetalBean.getUser_header());
    model.setUserIp(accountHistoryDetalBean.getUser_ip());
    logger.exit(model);
    return model ;
  }

}
