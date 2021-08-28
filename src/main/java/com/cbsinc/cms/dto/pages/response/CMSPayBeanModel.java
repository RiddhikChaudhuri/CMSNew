package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.PayBean;

public class CMSPayBeanModel extends PageModel {

  private String account_hist_id;
  
  private String description;

  public CMSPayBeanModel(PayBean paybean) {
    super();
    this.account_hist_id =paybean.getAccount_hist_id();
    this.description = paybean.getDescription();
  }
  
  
}
