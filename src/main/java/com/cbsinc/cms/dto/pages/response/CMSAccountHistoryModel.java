package com.cbsinc.cms.dto.pages.response;

public class CMSAccountHistoryModel {
  
  String amountId = "";
  String currencyAddLable = "" ;
  String addAmount = "" ;
  String sysdate = "";
  String complete = "";
  String rezultCode = "" ;
  
  
  public String getAmountId() {
    return amountId;
  }
  public void setAmountId(String amountId) {
    this.amountId = amountId;
  }
  public String getCurrencyAddLable() {
    return currencyAddLable;
  }
  public void setCurrencyAddLable(String currencyAddLable) {
    this.currencyAddLable = currencyAddLable;
  }
  public String getAddAmount() {
    return addAmount;
  }
  public void setAddAmount(String addAmount) {
    this.addAmount = addAmount;
  }
  public String getSysdate() {
    return sysdate;
  }
  public void setSysdate(String sysdate) {
    this.sysdate = sysdate;
  }
  public String getComplete() {
    return complete;
  }
  public void setComplete(String complete) {
    this.complete = complete;
  }
  public String getRezultCode() {
    return rezultCode;
  }
  public void setRezultCode(String rezultCode) {
    this.rezultCode = rezultCode;
  }
  @Override
  public String toString() {
    return "CMSAccountHistoryModel [amountId=" + amountId + ", currencyAddLable=" + currencyAddLable
        + ", addAmount=" + addAmount + ", sysdate=" + sysdate + ", complete=" + complete
        + ", rezultCode=" + rezultCode + "]";
  }

  
  
  
  
}
