package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dinamic_fields")
public class DinamicFields implements java.io.Serializable {

  private static final long serialVersionUID = 983212117315586823L;

  @Id
  @Column(name = "DINAMIC_FIELD_IDs")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long dinamicFieldId;

  @Column(name = "PRODUCT_ID")
  private Long productId;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  public DinamicFields() {}

  public DinamicFields(long dinamicFieldId, boolean active) {
    this.dinamicFieldId = dinamicFieldId;
    this.active = active;
  }

  public DinamicFields(long dinamicFieldId, Long productId, boolean active) {
    this.dinamicFieldId = dinamicFieldId;
    this.productId = productId;
    this.active = active;
  }

  public long getDinamicFieldId() {
    return this.dinamicFieldId;
  }

  public void setDinamicFieldId(long dinamicFieldId) {
    this.dinamicFieldId = dinamicFieldId;
  }

  public Long getProductId() {
    return this.productId;
  }

  public void setProductId(Long productId) {
    this.productId = productId;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

}
