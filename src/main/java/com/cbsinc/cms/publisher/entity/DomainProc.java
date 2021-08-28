package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "domain_proc")
public class DomainProc implements java.io.Serializable {
  @EmbeddedId
  private DomainProcId id;

  public DomainProc() {}

  public DomainProc(DomainProcId id) {
    this.id = id;
  }

  public DomainProcId getId() {
    return this.id;
  }

  public void setId(DomainProcId id) {
    this.id = id;
  }

}
