package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "calendar")
public class Calendar implements java.io.Serializable {

	private static final long serialVersionUID = 6111428897170685507L;

	@Id
	@Column(name = "CALENDAR_ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long calendarId;

	@Column(name = "SOFT_ID")
	private Long softId;
	@Column(name = "HOLDDATE")
	private Integer holddate;
	@Column(name = "FIST_NAME", length = 50)
	private String fistName;
	@Column(name = "LAST_NAME", length = 50)
	private String lastName;
	@Column(name = "FATHER_NAME", length = 50)
	private String fatherName;
	@Column(name = "DOCUMENT_NUMBER", length = 50)
	private String documentNumber;
	@Column(name = "DOCUMENT_TYPE", length = 50)
	private String documentType;
	@Column(name = "AGE", length = 50)
	private String age;
	@Column(name = "ACTIVE")
	private Boolean active;
	@Column(name = "NOTE", length = 500)
	private String note;
	@Column(name = "PARENT_ID")
	private Long parentId;

	@Column(name = "NODE_NAME", length = 500)
	private String nodeName;
	@Column(name = "SITE_ID", nullable = false)
	private long siteId;
	@Column(name = "BASKET_ID")
	private Long basketId;

	public Calendar(long calendarId, long siteId) {
		this.calendarId = calendarId;
		this.siteId = siteId;
	}

	public long getCalendarId() {
		return calendarId;
	}

	public void setCalendarId(long calendarId) {
		this.calendarId = calendarId;
	}

	public Long getSoftId() {
		return softId;
	}

	public void setSoftId(Long softId) {
		this.softId = softId;
	}

	public Integer getHolddate() {
		return holddate;
	}

	public void setHolddate(Integer holddate) {
		this.holddate = holddate;
	}

	public String getFistName() {
		return fistName;
	}

	public void setFistName(String fistName) {
		this.fistName = fistName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFatherName() {
		return fatherName;
	}

	public void setFatherName(String fatherName) {
		this.fatherName = fatherName;
	}

	public String getDocumentNumber() {
		return documentNumber;
	}

	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public long getSiteId() {
		return siteId;
	}

	public void setSiteId(long siteId) {
		this.siteId = siteId;
	}

	public Long getBasketId() {
		return basketId;
	}

	public void setBasketId(Long basketId) {
		this.basketId = basketId;
	}

	@Override
	public String toString() {
		return "Calendar [calendarId=" + calendarId + ", softId=" + softId + ", holddate=" + holddate + ", fistName="
				+ fistName + ", lastName=" + lastName + ", fatherName=" + fatherName + ", documentNumber="
				+ documentNumber + ", documentType=" + documentType + ", age=" + age + ", active=" + active + ", note="
				+ note + ", parentId=" + parentId + ", nodeName=" + nodeName + ", siteId=" + siteId + ", basketId="
				+ basketId + "]";
	}

}
