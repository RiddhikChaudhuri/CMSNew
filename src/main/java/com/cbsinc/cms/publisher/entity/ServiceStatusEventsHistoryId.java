package com.cbsinc.cms.publisher.entity;

// Generated 29.03.2014 21:04:05 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class ServiceStatusEventsHistoryId implements java.io.Serializable {
    @Column(name="SITE_ID")
	private Long siteId;
    
    @Column(name="DATE_SEND",length=19)
    @Temporal(TemporalType.TIMESTAMP)
	private Date dateSend;
    
    @Column(name="SERVICE_STATUS")
	private Integer serviceStatus;
    
    @Column(name="CLASSBODY",length=250)
	private String classbody;
    
    @Column(name="ISNOTIFY")
	private boolean isnotify;
    
    @Column(name="ACTIVE")
	private boolean active;

	public ServiceStatusEventsHistoryId() {
	}

	public ServiceStatusEventsHistoryId(Date dateSend, boolean isnotify,
			boolean active) {
		this.dateSend = dateSend;
		this.isnotify = isnotify;
		this.active = active;
	}

	public ServiceStatusEventsHistoryId(Long siteId, Date dateSend,
			Integer serviceStatus, String classbody, boolean isnotify,
			boolean active) {
		this.siteId = siteId;
		this.dateSend = dateSend;
		this.serviceStatus = serviceStatus;
		this.classbody = classbody;
		this.isnotify = isnotify;
		this.active = active;
	}

	public Long getSiteId() {
		return this.siteId;
	}

	public void setSiteId(Long siteId) {
		this.siteId = siteId;
	}

	public Date getDateSend() {
		return this.dateSend;
	}

	public void setDateSend(Date dateSend) {
		this.dateSend = dateSend;
	}

	public Integer getServiceStatus() {
		return this.serviceStatus;
	}

	public void setServiceStatus(Integer serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getClassbody() {
		return this.classbody;
	}

	public void setClassbody(String classbody) {
		this.classbody = classbody;
	}

	public boolean isIsnotify() {
		return this.isnotify;
	}

	public void setIsnotify(boolean isnotify) {
		this.isnotify = isnotify;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof ServiceStatusEventsHistoryId))
			return false;
		ServiceStatusEventsHistoryId castOther = (ServiceStatusEventsHistoryId) other;

		return ((this.getSiteId() == castOther.getSiteId()) || (this
				.getSiteId() != null && castOther.getSiteId() != null && this
				.getSiteId().equals(castOther.getSiteId())))
				&& ((this.getDateSend() == castOther.getDateSend()) || (this
						.getDateSend() != null
						&& castOther.getDateSend() != null && this
						.getDateSend().equals(castOther.getDateSend())))
				&& ((this.getServiceStatus() == castOther.getServiceStatus()) || (this
						.getServiceStatus() != null
						&& castOther.getServiceStatus() != null && this
						.getServiceStatus()
						.equals(castOther.getServiceStatus())))
				&& ((this.getClassbody() == castOther.getClassbody()) || (this
						.getClassbody() != null
						&& castOther.getClassbody() != null && this
						.getClassbody().equals(castOther.getClassbody())))
				&& (this.isIsnotify() == castOther.isIsnotify())
				&& (this.isActive() == castOther.isActive());
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSiteId() == null ? 0 : this.getSiteId().hashCode());
		result = 37 * result
				+ (getDateSend() == null ? 0 : this.getDateSend().hashCode());
		result = 37
				* result
				+ (getServiceStatus() == null ? 0 : this.getServiceStatus()
						.hashCode());
		result = 37 * result
				+ (getClassbody() == null ? 0 : this.getClassbody().hashCode());
		result = 37 * result + (this.isIsnotify() ? 1 : 0);
		result = 37 * result + (this.isActive() ? 1 : 0);
		return result;
	}

}
