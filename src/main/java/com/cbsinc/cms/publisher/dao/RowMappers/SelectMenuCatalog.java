package com.cbsinc.cms.publisher.dao.RowMappers;

public class SelectMenuCatalog {

	private String catalog_id;
	private String lable;
	private String parent_id;

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

	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	@Override
	public String toString() {
		return "SelectMenuCatalog [catalog_id=" + catalog_id + ", lable=" + lable + ", parent_id=" + parent_id + "]";
	}

}
