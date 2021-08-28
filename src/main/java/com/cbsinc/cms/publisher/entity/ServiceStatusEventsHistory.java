package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "service_status_events_history")
public class ServiceStatusEventsHistory implements java.io.Serializable {
  @EmbeddedId
  private ServiceStatusEventsHistoryId id;

  public ServiceStatusEventsHistory() {}

  public ServiceStatusEventsHistory(ServiceStatusEventsHistoryId id) {
    this.id = id;
  }

  public ServiceStatusEventsHistoryId getId() {
    return this.id;
  }

  public void setId(ServiceStatusEventsHistoryId id) {
    this.id = id;
  }

}
