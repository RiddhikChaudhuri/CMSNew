package com.cbsinc.cms.publisher.controllers;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.response.CMSAccountHistoryModel;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.models.AccountHistoryBean;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;

@RestController
public class AccountHistoryController extends CMSObjects {


  private final XLogger logger =
      XLoggerFactory.getXLogger(AccountHistoryController.class.getName());


  @Autowired
  OrderFaced orderFaced;

  @Autowired
  public AccountHistoryController() {
    logger.entry();
    logger.exit();
  }


  @GetMapping(value = "/doGetAccountHistory")
  public @ResponseBody List<CMSAccountHistoryModel> getAccountHistory(
      AccountHistoryBean accountHistoryBeanId) throws Exception {

    logger.entry(accountHistoryBeanId);

    AuthorizationPageBean authorizationPageBeanId =
        (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");

    List<CMSAccountHistoryModel> cmsAccountHistoryList = new ArrayList<>();

    if (accountHistoryBeanId.getSearchquery().equals("2")) {
      orderFaced.getPaymentlistByDate(authorizationPageBeanId.getIntUserID(),
          authorizationPageBeanId.getIntLevelUp(), accountHistoryBeanId).stream().forEach(n -> {
            CMSAccountHistoryModel e = new CMSAccountHistoryModel();
            e.setAddAmount(n.getAdd_amount());
            e.setAmountId(n.getAmount_id());
            e.setComplete(n.getComplete());
            e.setCurrencyAddLable(n.getCurrency_add_lable());
            e.setRezultCode(n.getRezult_cd());
            e.setSysdate(n.getSysdate());
            cmsAccountHistoryList.add(e);
          });

    } else {
      orderFaced.getPaymentlist(authorizationPageBeanId.getIntUserID(),
          authorizationPageBeanId.getIntLevelUp(), accountHistoryBeanId).stream().forEach(n -> {
            CMSAccountHistoryModel e = new CMSAccountHistoryModel();
            e.setAddAmount(n.getAdd_amount());
            e.setAmountId(n.getAmount_id());
            e.setComplete(n.getComplete());
            e.setCurrencyAddLable(n.getCurrency_add_lable());
            e.setRezultCode(n.getRezult_cd());
            e.setSysdate(n.getSysdate());
            cmsAccountHistoryList.add(e);
          });

    }
    logger.exit(cmsAccountHistoryList);

    return cmsAccountHistoryList;
  }



}
