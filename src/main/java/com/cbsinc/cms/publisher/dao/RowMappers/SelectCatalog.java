package com.cbsinc.cms.publisher.dao.RowMappers;

public class SelectCatalog {

	private String catalog_id;
	private String lable;

	public SelectCatalog() {
	}

	public SelectCatalog(String catalog_id, String lable) {
		super();
		this.catalog_id = catalog_id;
		this.lable = lable;
	}

	public String getCatalog_id() {
		return catalog_id;
	}

	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	@Override
	public String toString() {
		return "SelectCatalog [catalog_id=" + catalog_id + ", lable=" + lable + "]";
	}

}
