package com.cbsinc.cms.dto.pages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OrderListRequestForm {

	@JsonProperty("offset")
	private String offset;

	@JsonProperty("searchquery")
	private String searchquery;

	@JsonProperty("datefrom")
	private String datefrom;

	@JsonProperty("date_format")
	private String date_format;

	@JsonProperty("dateto")
	private String dateto;

	@JsonProperty("order_paystatus")
	private String order_paystatus;

	@JsonProperty("order_status")
	private String order_status;

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getSearchquery() {
		return searchquery;
	}

	public void setSearchquery(String searchquery) {
		this.searchquery = searchquery;
	}

	public String getDatefrom() {
		return datefrom;
	}

	public void setDatefrom(String datefrom) {
		this.datefrom = datefrom;
	}

	public String getDate_format() {
		return date_format;
	}

	public void setDate_format(String date_format) {
		this.date_format = date_format;
	}

	public String getDateto() {
		return dateto;
	}

	public void setDateto(String dateto) {
		this.dateto = dateto;
	}

	public String getOrder_paystatus() {
		return order_paystatus;
	}

	public void setOrder_paystatus(String order_paystatus) {
		this.order_paystatus = order_paystatus;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	@Override
	public String toString() {
		return "OrderListRequestForm [offset=" + offset + ", searchquery=" + searchquery + ", datefrom=" + datefrom
				+ ", date_format=" + date_format + ", dateto=" + dateto + ", order_paystatus=" + order_paystatus
				+ ", order_status=" + order_status + "]";
	}



}
