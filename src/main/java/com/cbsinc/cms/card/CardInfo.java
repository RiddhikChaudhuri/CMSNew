package com.cbsinc.cms.card;

public class CardInfo implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String country;

	private String cardnumber;

	private String bankname;

	private String bin;

	private String cardtype;

	private String cardsubtype;

	public String getcountry() {
		return country;
	}

	public void setcountry(String value) {
		country = value;
	}

	public String getcardnumber() {
		return cardnumber;
	}

	public void setcardnumber(String value) {
		cardnumber = value;
	}

	public String getbankname() {
		return bankname;
	}

	public void setbankname(String value) {
		bankname = value;
	}

	public String getbin() {
		return bin;
	}

	public void setbin(String value) {
		bin = value;
	}

	public String getcardtype() {
		return cardtype;
	}

	public void setcardtype(String value) {
		cardtype = value;
	}

	public String getcardsubtype() {
		return cardsubtype;
	}

	public void setcardsubtype(String value) {
		cardsubtype = value;
	}

}
