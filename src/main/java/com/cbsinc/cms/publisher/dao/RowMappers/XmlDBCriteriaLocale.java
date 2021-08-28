package com.cbsinc.cms.publisher.dao.RowMappers;

import com.fasterxml.jackson.annotation.JsonProperty;

public class XmlDBCriteriaLocale {

	public XmlDBCriteriaLocale(String name, String selected_cd, String strLable, String strCD, String url) {
		super();
		this.name = name;
		this.selected_cd = selected_cd;
		this.strLable = strLable;
		this.strCD = strCD;
		this.url = url;
	}

	private String name;

	@JsonProperty(value = "selected")
	private String selected_cd;

	@JsonProperty(value = "item")
	private String strLable;

	@JsonProperty(value = "code")
	private String strCD;

	@JsonProperty(value = "url")
	private String url;

	@Override
	public String toString() {
		return "XmlDBCriteriaLocale [name=" + name + ", selected_cd=" + selected_cd + ", strLable=" + strLable
				+ ", strCD=" + strCD + ", url=" + url + "]";
	}

}
