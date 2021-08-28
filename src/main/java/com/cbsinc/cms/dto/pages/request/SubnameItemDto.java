package com.cbsinc.cms.dto.pages.request;

public class SubnameItemDto {
	
	

	public String subselected;
	public String subitem;
	public String subcode;
	public String suburl;
	
	
	public SubnameItemDto() {
	}

	public SubnameItemDto(String subselected, String subitem, String subcode, String suburl) {
		super();
		this.subselected = subselected;
		this.subitem = subitem;
		this.subcode = subcode;
		this.suburl = suburl;
	}

	public String getSubselected() {
		return subselected;
	}

	public void setSubselected(String subselected) {
		this.subselected = subselected;
	}

	public String getSubitem() {
		return subitem;
	}

	public void setSubitem(String subitem) {
		this.subitem = subitem;
	}

	public String getSubcode() {
		return subcode;
	}

	public void setSubcode(String subcode) {
		this.subcode = subcode;
	}

	public String getSuburl() {
		return suburl;
	}

	public void setSuburl(String suburl) {
		this.suburl = suburl;
	}

	
	
	@Override
	public String toString() {
		return "SubnameItemDto [subselected=" + subselected + ", subitem=" + subitem + ", subcode=" + subcode
				+ ", suburl=" + suburl + "]";
	}

}