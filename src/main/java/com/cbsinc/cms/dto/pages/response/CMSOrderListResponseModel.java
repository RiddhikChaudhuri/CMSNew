package com.cbsinc.cms.dto.pages.response;

import java.util.List;
import com.cbsinc.cms.dto.pages.orderhistory.HistoryOrder;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.OrderListBean;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonRootName(value = "orderList")
public class CMSOrderListResponseModel {

  @JsonInclude(Include.NON_NULL)
  private int offset;
  
  @JsonInclude(Include.NON_NULL)
  private String searchquery;
  
  @JsonInclude(Include.NON_NULL)
  private long dateFrom;
  
  @JsonInclude(Include.NON_NULL)
  private long setDateTo;
  
  @JsonInclude(Include.NON_NULL)
  private List<HistoryOrder> selectOrderlistXML;
  
  @JsonInclude(Include.NON_NULL)
  private String order_paystatus_id;
  
  
  @JsonInclude(Include.NON_NULL)
  private String deliverystatus_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_paystatus;
  
  
  @JsonInclude(Include.NON_NULL)
  private String select_deliverystatus;
  
  @JsonInclude(Include.NON_NULL)
  private String select_menu_catalog;
  
  
  public CMSOrderListResponseModel(OrderListBean orderListBean,
      AuthorizationPageBean authorizationPageBeanId) {
    
  }

  @JsonCreator
  public CMSOrderListResponseModel(OrderListBean orderListBean) {
    this.offset = orderListBean.getOffset();
    this.searchquery = orderListBean.getSearchquery();
    this.dateFrom = orderListBean.getDateFrom();
    this.setDateTo = orderListBean.getDateTo();
    this.selectOrderlistXML = orderListBean.getSelectOrderlistXML();
    this.order_paystatus_id = orderListBean.getOrder_paystatus_id();
    this.deliverystatus_id = orderListBean.getDeliverystatus_id();
    this.select_paystatus = orderListBean.getSelect_paystatus();
    this.select_deliverystatus = orderListBean.getSelect_deliverystatus();
    this.select_menu_catalog = orderListBean.getSelect_menu_catalog();
  }

}
