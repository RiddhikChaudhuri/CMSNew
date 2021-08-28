package com.cbsinc.cms.dto.pages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InstallForm {

	@JsonProperty("Login")
	String strLogin;

	@JsonProperty("Passwd1")
	String strPasswd;

	@JsonProperty("Passwd2")
	String strCPasswd;

	@JsonProperty("FName")
	String strFirstName;

	@JsonProperty("LName")
	String strLastName;

	@JsonProperty("Company")
	String strCompany;

	@JsonProperty("EMail")
	String strEMail;

	@JsonProperty("Phone")
	String strPhone;

	@JsonProperty("MPhone")
	String strMPhone;

	@JsonProperty("Fax")
	String strFax;

	@JsonProperty("Address")
	String address;

	@JsonProperty("Website")
	String strWebsite;

	@JsonProperty("Question")
	String strQuestion;

	@JsonProperty("Answer")
	String strAnswer;

	@JsonProperty("country_id")
	String country_id;

	@JsonProperty("city_id")
	String city_id;

	@JsonProperty("currency_id")
	String currency_id;

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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@Override
	public String toString() {
		return "InstallForm [strLogin=" + strLogin + ", strPasswd=" + strPasswd + ", strCPasswd=" + strCPasswd
				+ ", strFirstName=" + strFirstName + ", strLastName=" + strLastName + ", strCompany=" + strCompany
				+ ", strEMail=" + strEMail + ", strPhone=" + strPhone + ", strMPhone=" + strMPhone + ", strFax="
				+ strFax + ", address=" + address + ", strWebsite=" + strWebsite + ", strQuestion=" + strQuestion
				+ ", strAnswer=" + strAnswer + ", country_id=" + country_id + ", city_id=" + city_id + ", currency_id="
				+ currency_id + "]";
	}

}
