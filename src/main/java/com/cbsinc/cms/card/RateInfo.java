package com.cbsinc.cms.card;

public class RateInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String currency;

	private String date;

	private String rate;

	public String getcurrency() {
		return currency;
	}

	public void setcurrency(String value) {
		currency = value;
	}

	public String getdate() {
		return date;
	}

	public void setdate(String value) {
		date = value;
	}

	public String getrate() {
		return rate;
	}

	public void setrate(String value) {
		rate = value;
	}

}
