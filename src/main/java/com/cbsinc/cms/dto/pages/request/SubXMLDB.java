package com.cbsinc.cms.dto.pages.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubXMLDB {

	public List<String> selected;
	public List<String> item;
	public List<String> code;
	public List<String> url;
	@JsonProperty("subcatalog-item")
	public SubnameItemDto subnameItem;

	public List<String> getSelected() {
		return selected;
	}

	public void setSelected(List<String> selected) {
		this.selected = selected;
	}

	public List<String> getItem() {
		return item;
	}

	public void setItem(List<String> item) {
		this.item = item;
	}

	public List<String> getCode() {
		return code;
	}

	public void setCode(List<String> code) {
		this.code = code;
	}

	public List<String> getUrl() {
		return url;
	}

	public void setUrl(List<String> url) {
		this.url = url;
	}

	public SubnameItemDto getSubnameItem() {
		return subnameItem;
	}

	public void setSubnameItem(SubnameItemDto subnameItem) {
		this.subnameItem = subnameItem;
	}

	@Override
	public String toString() {
		return "SubXMLDB [selected=" + selected + ", item=" + item + ", code=" + code + ", url=" + url
				+ ", subnameItem=" + subnameItem + "]";
	}

}
