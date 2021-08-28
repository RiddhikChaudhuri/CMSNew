package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity(name = "store_session")
public class StoreSession implements java.io.Serializable {

  @EmbeddedId
  private StoreSessionId id;

  public StoreSession() {}

  public StoreSession(StoreSessionId id) {
    this.id = id;
  }

  public StoreSessionId getId() {
    return this.id;
  }

  public void setId(StoreSessionId id) {
    this.id = id;
  }

}
