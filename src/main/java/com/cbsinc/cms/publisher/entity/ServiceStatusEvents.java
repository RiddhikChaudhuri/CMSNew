package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name="service_status_events")
public class ServiceStatusEvents implements java.io.Serializable {
    @EmbeddedId
	private ServiceStatusEventsId id;

	public ServiceStatusEvents() {
	}

	public ServiceStatusEvents(ServiceStatusEventsId id) {
		this.id = id;
	}

	public ServiceStatusEventsId getId() {
		return this.id;
	}

	public void setId(ServiceStatusEventsId id) {
		this.id = id;
	}

}
