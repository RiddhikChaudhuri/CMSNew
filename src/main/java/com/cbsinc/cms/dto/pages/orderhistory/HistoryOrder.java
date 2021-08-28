package com.cbsinc.cms.dto.pages.orderhistory;

public class HistoryOrder {

  String order_id;
  String end_amount;
  String cdate;
  String paystatus_id;
  String paystatus_lable;
  String currency_lable;

  public String getOrder_id() {
    return order_id;
  }

  public void setOrder_id(String order_id) {
    this.order_id = order_id;
  }

  public String getEnd_amount() {
    return end_amount;
  }

  public void setEnd_amount(String end_amount) {
    this.end_amount = end_amount;
  }

  public String getCdate() {
    return cdate;
  }

  public void setCdate(String cdate) {
    this.cdate = cdate;
  }

  public String getPaystatus_id() {
    return paystatus_id;
  }

  public void setPaystatus_id(String paystatus_id) {
    this.paystatus_id = paystatus_id;
  }

  public String getPaystatus_lable() {
    return paystatus_lable;
  }

  public void setPaystatus_lable(String paystatus_lable) {
    this.paystatus_lable = paystatus_lable;
  }

  public String getCurrency_lable() {
    return currency_lable;
  }

  public void setCurrency_lable(String currency_lable) {
    this.currency_lable = currency_lable;
  }

}
