package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "deliverystatus")
public class Deliverystatus implements java.io.Serializable {


  private static final long serialVersionUID = 9146057023132894522L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "DELIVERYSTATUS_ID")
  private long deliverystatusId;

  @Column(name = "LABLE", length = 50)
  private String lable;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "LANG", length = 2)
  private String lang;

  public Deliverystatus() {}

  public Deliverystatus(long deliverystatusId, boolean active) {
    this.deliverystatusId = deliverystatusId;
    this.active = active;
  }

  public Deliverystatus(long deliverystatusId, String lable, String description, boolean active,
      String lang) {
    this.deliverystatusId = deliverystatusId;
    this.lable = lable;
    this.description = description;
    this.active = active;
    this.lang = lang;
  }

  public long getDeliverystatusId() {
    return this.deliverystatusId;
  }

  public void setDeliverystatusId(long deliverystatusId) {
    this.deliverystatusId = deliverystatusId;
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
