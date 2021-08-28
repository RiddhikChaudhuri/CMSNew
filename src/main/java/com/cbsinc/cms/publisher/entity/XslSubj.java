package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "xsl_subj")
public class XslSubj implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "XSL_SUBJ_ID")
  private long xslSubjId;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  public XslSubj() {}

  public XslSubj(long xslSubjId, boolean active) {
    this.xslSubjId = xslSubjId;
    this.active = active;
  }

  public XslSubj(long xslSubjId, String name, String description, boolean active) {
    this.xslSubjId = xslSubjId;
    this.name = name;
    this.description = description;
    this.active = active;
  }

  public long getXslSubjId() {
    return this.xslSubjId;
  }

  public void setXslSubjId(long xslSubjId) {
    this.xslSubjId = xslSubjId;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
