package com.cbsinc.cms.dto.pages.request;

import java.util.LinkedList;

import com.cbsinc.cms.services.whois.DomainState;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WhoIsRequestBody {

	@JsonProperty("domainList")
	LinkedList<DomainState> domainList;

	@JsonProperty("regdomain")
	String regdomain = "";

	@JsonProperty("domain")
	String domain = "";

	@JsonProperty("message")
	String message = "";

	public LinkedList<DomainState> getDomainList() {
		return domainList;
	}

	public void setDomainList(LinkedList<DomainState> domainList) {
		this.domainList = domainList;
	}

	public String getRegdomain() {
		return regdomain;
	}

	public void setRegdomain(String regdomain) {
		this.regdomain = regdomain;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "WhoIsRequestBody [domainList=" + domainList + ", regdomain=" + regdomain + ", domain=" + domain
				+ ", message=" + message + "]";
	}

}
