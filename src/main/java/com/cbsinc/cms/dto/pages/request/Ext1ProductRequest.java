package com.cbsinc.cms.dto.pages.request;

public class Ext1ProductRequest {

	private String bigimage_id;
	private String image_id;
	private String product_id;

	private String softname;
	private String type_id;
	private String softcost;
	private String currency_id;
	private String description;
	private String fulldescription;
	private String imagename;
	private String portlettype_id;
	private String filename;
	private String bigimagename;
	private String salelogic_id;

	public String getBigimage_id() {
		return bigimage_id;
	}

	public void setBigimage_id(String bigimage_id) {
		this.bigimage_id = bigimage_id;
	}

	public String getImage_id() {
		return image_id;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getSoftname() {
		return softname;
	}

	public void setSoftname(String softname) {
		this.softname = softname;
	}

	public String getType_id() {
		return type_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getSoftcost() {
		return softcost;
	}

	public void setSoftcost(String softcost) {
		this.softcost = softcost;
	}

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFulldescription() {
		return fulldescription;
	}

	public void setFulldescription(String fulldescription) {
		this.fulldescription = fulldescription;
	}

	public String getImagename() {
		return imagename;
	}

	public void setImagename(String imagename) {
		this.imagename = imagename;
	}

	public String getPortlettype_id() {
		return portlettype_id;
	}

	public void setPortlettype_id(String portlettype_id) {
		this.portlettype_id = portlettype_id;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getBigimagename() {
		return bigimagename;
	}

	public void setBigimagename(String bigimagename) {
		this.bigimagename = bigimagename;
	}

	public String getSalelogic_id() {
		return salelogic_id;
	}

	public void setSalelogic_id(String salelogic_id) {
		this.salelogic_id = salelogic_id;
	}

	@Override
	public String toString() {
		return "Ext1ProductRequest [bigimage_id=" + bigimage_id + ", image_id=" + image_id + ", product_id="
				+ product_id + ", softname=" + softname + ", type_id=" + type_id + ", softcost=" + softcost
				+ ", currency_id=" + currency_id + ", description=" + description + ", fulldescription="
				+ fulldescription + ", imagename=" + imagename + ", portlettype_id=" + portlettype_id + ", filename="
				+ filename + ", bigimagename=" + bigimagename + ", salelogic_id=" + salelogic_id + "]";
	}

}
