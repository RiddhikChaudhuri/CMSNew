package com.cbsinc.cms.dto.pages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterationRequest {

	@JsonProperty("Login")
	private String strLogin;

	@JsonProperty("Passwd1")
	private String strPasswd;

	@JsonProperty("Passwd2")
	private String strCPasswd;

	@JsonProperty("FName")
	private String strFirstName;

	@JsonProperty("LName")
	private String strLastName;

	@JsonProperty("Company")
	private String strCompany;

	@JsonProperty("EMail")
	private String strEMail;

	@JsonProperty("Phone")
	private String strPhone;

	@JsonProperty("MPhone")
	private String strMPhone;

	@JsonProperty("Fax")
	private String strFax;

	@JsonProperty("Icq")
	private String strIcq;

	@JsonProperty("Website")
	private String strWebsite;

	@JsonProperty("Question")
	private String strQuestion;

	@JsonProperty("Answer")
	private String strAnswer;

	@JsonProperty("country_id")
	private String country_id;

	@JsonProperty("city_id")
	private String city_id;

	@JsonProperty("currency_id")
	private String currency_id;

	@JsonProperty("site_id")
	private String site_id;

	public String getStrLogin() {
		return strLogin;
	}

	public void setStrLogin(String strLogin) {
		this.strLogin = strLogin;
	}

	public String getStrPasswd() {
		return strPasswd;
	}

	public void setStrPasswd(String strPasswd) {
		this.strPasswd = strPasswd;
	}

	public String getStrCPasswd() {
		return strCPasswd;
	}

	public void setStrCPasswd(String strCPasswd) {
		this.strCPasswd = strCPasswd;
	}

	public String getStrFirstName() {
		return strFirstName;
	}

	public void setStrFirstName(String strFirstName) {
		this.strFirstName = strFirstName;
	}

	public String getStrLastName() {
		return strLastName;
	}

	public void setStrLastName(String strLastName) {
		this.strLastName = strLastName;
	}

	public String getStrCompany() {
		return strCompany;
	}

	public void setStrCompany(String strCompany) {
		this.strCompany = strCompany;
	}

	public String getStrEMail() {
		return strEMail;
	}

	public void setStrEMail(String strEMail) {
		this.strEMail = strEMail;
	}

	public String getStrPhone() {
		return strPhone;
	}

	public void setStrPhone(String strPhone) {
		this.strPhone = strPhone;
	}

	public String getStrMPhone() {
		return strMPhone;
	}

	public void setStrMPhone(String strMPhone) {
		this.strMPhone = strMPhone;
	}

	public String getStrFax() {
		return strFax;
	}

	public void setStrFax(String strFax) {
		this.strFax = strFax;
	}

	public String getStrIcq() {
		return strIcq;
	}

	public void setStrIcq(String strIcq) {
		this.strIcq = strIcq;
	}

	public String getStrWebsite() {
		return strWebsite;
	}

	public void setStrWebsite(String strWebsite) {
		this.strWebsite = strWebsite;
	}

	public String getStrQuestion() {
		return strQuestion;
	}

	public void setStrQuestion(String strQuestion) {
		this.strQuestion = strQuestion;
	}

	public String getStrAnswer() {
		return strAnswer;
	}

	public void setStrAnswer(String strAnswer) {
		this.strAnswer = strAnswer;
	}

	public String getCountry_id() {
		return country_id;
	}

	public void setCountry_id(String country_id) {
		this.country_id = country_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	@Override
	public String toString() {
		return "RegisterationRequest [strLogin=" + strLogin + ", strPasswd=" + strPasswd + ", strCPasswd=" + strCPasswd
				+ ", strFirstName=" + strFirstName + ", strLastName=" + strLastName + ", strCompany=" + strCompany
				+ ", strEMail=" + strEMail + ", strPhone=" + strPhone + ", strMPhone=" + strMPhone + ", strFax="
				+ strFax + ", strIcq=" + strIcq + ", strWebsite=" + strWebsite + ", strQuestion=" + strQuestion
				+ ", strAnswer=" + strAnswer + ", country_id=" + country_id + ", city_id=" + city_id + ", currency_id="
				+ currency_id + ", site_id=" + site_id + "]";
	}

}
