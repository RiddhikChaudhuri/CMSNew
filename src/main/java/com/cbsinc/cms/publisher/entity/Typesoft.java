package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "typesoft")
public class Typesoft implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "TYPE_ID")
  private long typeId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "TYPE_LABLE", length = 50)
  private String typeLable;

  @Column(name = "TYPE_DESC", length = 100)
  private String typeDesc;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "TAX", precision = 22, scale = 0, nullable = false)
  private double tax;

  public Typesoft() {}

  public Typesoft(long typeId, boolean active, double tax) {
    this.typeId = typeId;
    this.active = active;
    this.tax = tax;
  }

  public Typesoft(long typeId, Long userId, String typeLable, String typeDesc, boolean active,
      double tax) {
    this.typeId = typeId;
    this.userId = userId;
    this.typeLable = typeLable;
    this.typeDesc = typeDesc;
    this.active = active;
    this.tax = tax;
  }

  public long getTypeId() {
    return this.typeId;
  }

  public void setTypeId(long typeId) {
    this.typeId = typeId;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getTypeLable() {
    return this.typeLable;
  }

  public void setTypeLable(String typeLable) {
    this.typeLable = typeLable;
  }

  public String getTypeDesc() {
    return this.typeDesc;
  }

  public void setTypeDesc(String typeDesc) {
    this.typeDesc = typeDesc;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public double getTax() {
    return this.tax;
  }

  public void setTax(double tax) {
    this.tax = tax;
  }

}
