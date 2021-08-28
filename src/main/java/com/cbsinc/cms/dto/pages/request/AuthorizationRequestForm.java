package com.cbsinc.cms.dto.pages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorizationRequestForm {

	@JsonProperty("Login")
	private String login;

	@JsonProperty("Passwd1")
	private String password;

	@JsonProperty("Message")
	private String message;

	@JsonProperty("site_id")
	private String siteId;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	@Override
	public String toString() {
		return "AuthorizationRequestForm [login=" + login + ", password=" + password + ", message=" + message
				+ ", siteId=" + siteId + "]";
	}
	
	

}
