package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "catalog")
public class Catalog implements java.io.Serializable {

	private static final long serialVersionUID = -8263254104526436784L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "CATALOG_ID", nullable = false)
	private long catalogId;

	@Column(name = "USER_ID")
	private Long userId;

	@Column(name = "LABLE", length = 50)
	private String lable;

	@Column(name = "ACTIVE", nullable = false)
	private boolean active;

	@Column(name = "TAX", precision = 22, scale = 0)
	private Double tax;

	@Column(name = "DESCRIPTION", length = 500)
	private String description;

	@Column(name = "SITE_ID")
	private Long siteId;

	@Column(name = "LANG_ID")
	private Long langId;

	@Column(name = "PARENT_ID")
	private Long parentId;

	@Column(name = "CATALOG_IMAGE_ID")
	private Long catalogImageId;

	public Catalog(long catalogId, boolean active) {
		this.catalogId = catalogId;
		this.active = active;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getCatalogId() {
		return catalogId;
	}

	public void setCatalogId(long catalogId) {
		this.catalogId = catalogId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getLable() {
		return lable;
	}

	public void setLable(String lable) {
		this.lable = lable;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getSiteId() {
		return siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Long getLangId() {
		return langId;
	}

	public void setLangId(Long langId) {
		this.langId = langId;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getCatalogImageId() {
		return catalogImageId;
	}

	public void setCatalogImageId(Long catalogImageId) {
		this.catalogImageId = catalogImageId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "Catalog [id=" + id + ", catalogId=" + catalogId + ", userId=" + userId + ", lable=" + lable
				+ ", active=" + active + ", tax=" + tax + ", description=" + description + ", siteId=" + siteId
				+ ", langId=" + langId + ", parentId=" + parentId + ", catalogImageId=" + catalogImageId + "]";
	}

}
