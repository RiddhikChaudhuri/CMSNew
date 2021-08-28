package com.cbsinc.cms.publisher.models;

public class Currency implements java.io.Serializable {

	private static final long serialVersionUID = -5089556736903083959L;

	/**
	 * <p>
	 * Title: Content Manager System
	 * </p>
	 * <p>
	 * Description: System building web application develop by Konstantin Grabko.
	 * Konstantin Grabko is Owner and author this code. You can not use it and you
	 * cannot change it without written permission from Konstantin Grabko Email:
	 * konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
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

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public String getCurrency_labe() {
		return currency_labe;
	}

	public void setCurrency_labe(String currency_labe) {
		this.currency_labe = currency_labe;
	}

	public String getCurrency_desc() {
		return currency_desc;
	}

	public void setCurrency_desc(String currency_desc) {
		this.currency_desc = currency_desc;
	}

	private String currency_id = "";

	private String currency_labe = "";

	private String currency_desc = "";

	private Float rate = Float.valueOf(0);

	private String code = "";

	private String date = "";

	public Currency() {
	}

	public Currency(String currency_id, String currency_labe, String currency_desc, float rate, String code,
			String date) {
		this.rate = rate;
		this.code = code;
		this.date = date;
		this.currency_id = currency_id;
		this.currency_labe = currency_labe;
		this.currency_desc = currency_desc;
	}

	public Currency(float rate, String code, String date) {
		this.rate = rate;
		this.code = code;
		this.date = date;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
