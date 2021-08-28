package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "catalog_images")
public class CatalogImages implements java.io.Serializable {

	private static final long serialVersionUID = -7798979904613845622L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "CATALOG_IMAGES_ID")
	private long catalogImagesId;

	@Column(name = "IMGNAME", length = 100)
	private String imgname;

	@Column(name = "IMG_URL", length = 100)
	private String imgUrl;

	@Column(name = "USER_ID")
	private Long userId;

	public CatalogImages(long catalogImagesId) {
		this.catalogImagesId = catalogImagesId;
	}

	public long getCatalogImagesId() {
		return catalogImagesId;
	}

	public void setCatalogImagesId(long catalogImagesId) {
		this.catalogImagesId = catalogImagesId;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CatalogImages [catalogImagesId=" + catalogImagesId + ", imgname=" + imgname + ", imgUrl=" + imgUrl
				+ ", userId=" + userId + "]";
	}

}
