package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lang")
public class Lang implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "LANG_ID")
  private long langId;

  @Column(name = "LABLE", length = 50)
  private String lable;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  public Lang() {}

  public Lang(long langId, boolean active) {
    this.langId = langId;
    this.active = active;
  }

  public Lang(long langId, String lable, String description, boolean active) {
    this.langId = langId;
    this.lable = lable;
    this.description = description;
    this.active = active;
  }

  public long getLangId() {
    return this.langId;
  }

  public void setLangId(long langId) {
    this.langId = langId;
  }

  public String getLable() {
    return this.lable;
  }

  public void setLable(String lable) {
    this.lable = lable;
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
