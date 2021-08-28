package com.cbsinc.cms.dto.pages.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchRequestForm {

	String search_value = "";
	String search_char = "";
	String searchquery = "";
	String offset = "";

	String catalog_id = "";
	String product_id = "";

	String dialog = "";
	String is_advanced_search_open = "";
	String is_forum_open = "";
	String dayfrom_id = "";
	String mountfrom_id = "";
	String yearfrom_id = "";
	String fromcost = "";
	String tocost = "";
	String dayto_id = "";
	String mountto_id = "";
	String yearto_id = "";
	String action = "";

	@JsonProperty("creteria1_id")
	private String creteria1_id = "";

	@JsonProperty("creteria2_id")
	private String creteria2_id = "";

	@JsonProperty("creteria3_id")
	private String creteria3_id = "";

	@JsonProperty("creteria4_id")
	private String creteria4_id = "";

	@JsonProperty("creteria5_id")
	private String creteria5_id = "";

	@JsonProperty("creteria6_id")
	private String creteria6_id = "";

	@JsonProperty("creteria7_id")
	private String creteria7_id = "";

	@JsonProperty("creteria8_id")
	private String creteria8_id = "";

	@JsonProperty("creteria9_id")
	private String creteria9_id = "";

	@JsonProperty("creteria10_id")
	private String creteria10_id = "";

	String element = "";
	String product_parent_id = "";
	String create_site_by_id = "";
	String site = "";
	String logoff_site = "";

	public String getSearch_value() {
		return search_value;
	}

	public void setSearch_value(String search_value) {
		this.search_value = search_value;
	}

	public String getSearch_char() {
		return search_char;
	}

	public void setSearch_char(String search_char) {
		this.search_char = search_char;
	}

	public String getSearchquery() {
		return searchquery;
	}

	public void setSearchquery(String searchquery) {
		this.searchquery = searchquery;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getCatalog_id() {
		return catalog_id;
	}

	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getDialog() {
		return dialog;
	}

	public void setDialog(String dialog) {
		this.dialog = dialog;
	}

	public String getIs_advanced_search_open() {
		return is_advanced_search_open;
	}

	public void setIs_advanced_search_open(String is_advanced_search_open) {
		this.is_advanced_search_open = is_advanced_search_open;
	}

	public String getIs_forum_open() {
		return is_forum_open;
	}

	public void setIs_forum_open(String is_forum_open) {
		this.is_forum_open = is_forum_open;
	}

	public String getDayfrom_id() {
		return dayfrom_id;
	}

	public void setDayfrom_id(String dayfrom_id) {
		this.dayfrom_id = dayfrom_id;
	}

	public String getMountfrom_id() {
		return mountfrom_id;
	}

	public void setMountfrom_id(String mountfrom_id) {
		this.mountfrom_id = mountfrom_id;
	}

	public String getYearfrom_id() {
		return yearfrom_id;
	}

	public void setYearfrom_id(String yearfrom_id) {
		this.yearfrom_id = yearfrom_id;
	}

	public String getFromcost() {
		return fromcost;
	}

	public void setFromcost(String fromcost) {
		this.fromcost = fromcost;
	}

	public String getTocost() {
		return tocost;
	}

	public void setTocost(String tocost) {
		this.tocost = tocost;
	}

	public String getDayto_id() {
		return dayto_id;
	}

	public void setDayto_id(String dayto_id) {
		this.dayto_id = dayto_id;
	}

	public String getMountto_id() {
		return mountto_id;
	}

	public void setMountto_id(String mountto_id) {
		this.mountto_id = mountto_id;
	}

	public String getYearto_id() {
		return yearto_id;
	}

	public void setYearto_id(String yearto_id) {
		this.yearto_id = yearto_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getCreteria1_id() {
		return creteria1_id;
	}

	public void setCreteria1_id(String creteria1_id) {
		this.creteria1_id = creteria1_id;
	}

	public String getCreteria2_id() {
		return creteria2_id;
	}

	public void setCreteria2_id(String creteria2_id) {
		this.creteria2_id = creteria2_id;
	}

	public String getCreteria3_id() {
		return creteria3_id;
	}

	public void setCreteria3_id(String creteria3_id) {
		this.creteria3_id = creteria3_id;
	}

	public String getCreteria4_id() {
		return creteria4_id;
	}

	public void setCreteria4_id(String creteria4_id) {
		this.creteria4_id = creteria4_id;
	}

	public String getCreteria5_id() {
		return creteria5_id;
	}

	public void setCreteria5_id(String creteria5_id) {
		this.creteria5_id = creteria5_id;
	}

	public String getCreteria6_id() {
		return creteria6_id;
	}

	public void setCreteria6_id(String creteria6_id) {
		this.creteria6_id = creteria6_id;
	}

	public String getCreteria7_id() {
		return creteria7_id;
	}

	public void setCreteria7_id(String creteria7_id) {
		this.creteria7_id = creteria7_id;
	}

	public String getCreteria8_id() {
		return creteria8_id;
	}

	public void setCreteria8_id(String creteria8_id) {
		this.creteria8_id = creteria8_id;
	}

	public String getCreteria9_id() {
		return creteria9_id;
	}

	public void setCreteria9_id(String creteria9_id) {
		this.creteria9_id = creteria9_id;
	}

	public String getCreteria10_id() {
		return creteria10_id;
	}

	public void setCreteria10_id(String creteria10_id) {
		this.creteria10_id = creteria10_id;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getProduct_parent_id() {
		return product_parent_id;
	}

	public void setProduct_parent_id(String product_parent_id) {
		this.product_parent_id = product_parent_id;
	}

	public String getCreate_site_by_id() {
		return create_site_by_id;
	}

	public void setCreate_site_by_id(String create_site_by_id) {
		this.create_site_by_id = create_site_by_id;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLogoff_site() {
		return logoff_site;
	}

	public void setLogoff_site(String logoff_site) {
		this.logoff_site = logoff_site;
	}

	@Override
	public String toString() {
		return "SearchRequestForm [search_value=" + search_value + ", search_char=" + search_char + ", searchquery="
				+ searchquery + ", offset=" + offset + ", catalog_id=" + catalog_id + ", product_id=" + product_id
				+ ", dialog=" + dialog + ", is_advanced_search_open=" + is_advanced_search_open + ", is_forum_open="
				+ is_forum_open + ", dayfrom_id=" + dayfrom_id + ", mountfrom_id=" + mountfrom_id + ", yearfrom_id="
				+ yearfrom_id + ", fromcost=" + fromcost + ", tocost=" + tocost + ", dayto_id=" + dayto_id
				+ ", mountto_id=" + mountto_id + ", yearto_id=" + yearto_id + ", action=" + action + ", creteria1_id="
				+ creteria1_id + ", creteria2_id=" + creteria2_id + ", creteria3_id=" + creteria3_id + ", creteria4_id="
				+ creteria4_id + ", creteria5_id=" + creteria5_id + ", creteria6_id=" + creteria6_id + ", creteria7_id="
				+ creteria7_id + ", creteria8_id=" + creteria8_id + ", creteria9_id=" + creteria9_id
				+ ", creteria10_id=" + creteria10_id + ", element=" + element + ", product_parent_id="
				+ product_parent_id + ", create_site_by_id=" + create_site_by_id + ", site=" + site + ", logoff_site="
				+ logoff_site + "]";
	}

}
