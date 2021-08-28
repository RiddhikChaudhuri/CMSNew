package com.cbsinc.cms.dto.pages.response;
/*
 * 
 * /cmsmanhattan/WebContent/AccountHistoryDetal.jsp
 * 
 * table.append("<payment>\n");

	table.append("<add_amount>" + getStrFormatNumberFloat(accountHistoryDetalBean.getAdd_amount()) + "</add_amount>\n");
	table.append("<old_amount>" + getStrFormatNumberFloat(accountHistoryDetalBean.getOld_amount()) + "</old_amount>\n");
	table.append("<date_input>" + accountHistoryDetalBean.getDate_input() + "</date_input>\n");
	table.append("<date_end>" + accountHistoryDetalBean.getDate_end() + "</date_end>\n");
	table.append("<sysdate>" + accountHistoryDetalBean.getSysdate() + "</sysdate>\n");
	table.append("<complete>" + accountHistoryDetalBean.getComplete() + "</complete>\n");
	table.append("<decsription>" + accountHistoryDetalBean.getDecsription() + "</decsription>\n");
	table.append("<active>" + accountHistoryDetalBean.getActive() + "</active>\n");
	table.append("<amount_id>" + accountHistoryDetalBean.getAmount_id() + "</amount_id>\n");
	table.append("<total_amount>" + getStrFormatNumberFloat(accountHistoryDetalBean.getTotal_amount()) + "</total_amount>\n");
	table.append("<currency_add_lable>" + accountHistoryDetalBean.getCurrency_add_lable()
			+ "</currency_add_lable>\n");
	table.append("<currency_old_lable>" + accountHistoryDetalBean.getCurrency_old_lable()
			+ "</currency_old_lable>\n");
	table.append("<currency_total_lable>" + accountHistoryDetalBean.getCurrency_total_lable()
			+ "</currency_total_lable>\n");
	table.append("<user_ip>" + accountHistoryDetalBean.getUser_ip() + "</user_ip>\n");
	table.append("<user_header>" + accountHistoryDetalBean.getUser_header() + "</user_header>\n");
	table.append("<rezult_cd>" + accountHistoryDetalBean.getRezult_cd() + "</rezult_cd>\n");
	table.append("</payment>\n");
 */

public class CMSAccountDetailHistoryModel {
	
	String addAmount = "" ;
	String oldAmount = "" ;
	String dateInput = "";
	String dateEnd = "";
	String sysdate = "";
	String complete = "";
	String decsription = "";
	String active = "";
	String amountId = "";
	String totalAmount  = "";
	String currencyAddLable = "" ;
	String currencyAldLable = "" ;
	String currencyTotalLable = "" ;
	String userIp = "" ;
	String userHeader = "" ;
	String rezultCode = "" ;
	public String getAddAmount() {
		return addAmount;
	}
	public void setAddAmount(String addAmount) {
		this.addAmount = addAmount;
	}
	public String getOldAmount() {
		return oldAmount;
	}
	public void setOldAmount(String oldAmount) {
		this.oldAmount = oldAmount;
	}
	public String getDateInput() {
		return dateInput;
	}
	public void setDateInput(String dateInput) {
		this.dateInput = dateInput;
	}
	public String getDateEnd() {
		return dateEnd;
	}
	public void setDateEnd(String dateEnd) {
		this.dateEnd = dateEnd;
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
	public String getDecsription() {
		return decsription;
	}
	public void setDecsription(String decsription) {
		this.decsription = decsription;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getAmountId() {
		return amountId;
	}
	public void setAmountId(String amountId) {
		this.amountId = amountId;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getCurrencyAddLable() {
		return currencyAddLable;
	}
	public void setCurrencyAddLable(String currencyAddLable) {
		this.currencyAddLable = currencyAddLable;
	}
	public String getCurrencyAldLable() {
		return currencyAldLable;
	}
	public void setCurrencyAldLable(String currencyAldLable) {
		this.currencyAldLable = currencyAldLable;
	}
	public String getCurrencyTotalLable() {
		return currencyTotalLable;
	}
	public void setCurrencyTotalLable(String currencyTotalLable) {
		this.currencyTotalLable = currencyTotalLable;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public String getUserHeader() {
		return userHeader;
	}
	public void setUserHeader(String userHeader) {
		this.userHeader = userHeader;
	}
	public String getRezultCode() {
		return rezultCode;
	}
	public void setRezultCode(String rezultCode) {
		this.rezultCode = rezultCode;
	}
	
	@Override
	public String toString() {
		return "CMSAccountDetailHistory [addAmount=" + addAmount + ", oldAmount=" + oldAmount + ", dateInput="
				+ dateInput + ", dateEnd=" + dateEnd + ", sysdate=" + sysdate + ", complete=" + complete
				+ ", decsription=" + decsription + ", active=" + active + ", amountId=" + amountId + ", totalAmount="
				+ totalAmount + ", currencyAddLable=" + currencyAddLable + ", currencyAldLable=" + currencyAldLable
				+ ", currencyTotalLable=" + currencyTotalLable + ", userIp=" + userIp + ", userHeader=" + userHeader
				+ ", rezultCode=" + rezultCode + "]";
	}
	
	
	

}
