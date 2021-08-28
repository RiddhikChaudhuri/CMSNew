package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paystatus")
public class Paystatus implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "PAYSTATUS_ID")
  private long paystatusId;

  @Column(name = "LABLE", length = 50)
  private String lable;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "LANG")
  private String lang;

  public Paystatus() {}

  public Paystatus(long paystatusId, boolean active) {
    this.paystatusId = paystatusId;
    this.active = active;
  }

  public Paystatus(long paystatusId, String lable, String description, boolean active,
      String lang) {
    this.paystatusId = paystatusId;
    this.lable = lable;
    this.description = description;
    this.active = active;
    this.lang = lang;
  }

  public long getPaystatusId() {
    return this.paystatusId;
  }

  public void setPaystatusId(long paystatusId) {
    this.paystatusId = paystatusId;
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

  public String getLang() {
    return this.lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

}
