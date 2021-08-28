package com.cbsinc.cms.services.whois;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class DomainState 
{

	private String domain = "" ;
	private boolean isFree = true ; 
	private String status = "" ;
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public boolean isFree() {
		return isFree;
	}
	public void setFree(boolean isFree) {
		this.isFree = isFree;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
}
