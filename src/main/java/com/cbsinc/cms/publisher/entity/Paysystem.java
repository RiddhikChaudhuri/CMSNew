package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "paysystem")
public class Paysystem implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "PAYSYSTEM_ID")
  private long paysystemId;

  @Column(name = "PAYSYSTEM_CD", length = 50)
  private String paysystemCd;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "IMG_URL", length = 500)
  private String imgUrl;

  @Column(name = "PAY_GATEWAY_ID")
  private Long payGatewayId;

  public Paysystem() {}

  public Paysystem(long paysystemId, boolean active) {
    this.paysystemId = paysystemId;
    this.active = active;
  }

  public Paysystem(long paysystemId, String paysystemCd, String name, String description,
      boolean active, String imgUrl, Long payGatewayId) {
    this.paysystemId = paysystemId;
    this.paysystemCd = paysystemCd;
    this.name = name;
    this.description = description;
    this.active = active;
    this.imgUrl = imgUrl;
    this.payGatewayId = payGatewayId;
  }

  public long getPaysystemId() {
    return this.paysystemId;
  }

  public void setPaysystemId(long paysystemId) {
    this.paysystemId = paysystemId;
  }

  public String getPaysystemCd() {
    return this.paysystemCd;
  }

  public void setPaysystemCd(String paysystemCd) {
    this.paysystemCd = paysystemCd;
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

  public String getImgUrl() {
    return this.imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public Long getPayGatewayId() {
    return this.payGatewayId;
  }

  public void setPayGatewayId(Long payGatewayId) {
    this.payGatewayId = payGatewayId;
  }

}
