package com.cbsinc.cms.publisher.entity;

// Generated 29.03.2014 21:04:05 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "shop")
public class Shop implements java.io.Serializable {

  private static final long serialVersionUID = -6046469557007702983L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "SHOP_ID")
  private Long shopId;

  @Column(name = "SHOP_CD", nullable = false)
  private int shopCd;

  @Column(name = "OWNER_ID", nullable = false)
  private long ownerId;

  @Column(name = "LOGIN", length = 50)
  private String login;

  @Column(name = "SITE_ID")
  private Long siteId;

  @Column(name = "PAY_GATEWAY_ID")
  private Long payGatewayId;

  @Column(name = "PASSWD", length = 50)
  private String passwd;

  @Column(name = "CDATE", length = 19, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date cdate;

  public Shop() {}

  public Shop(int shopCd, long ownerId, Date cdate) {
    this.shopCd = shopCd;
    this.ownerId = ownerId;
    this.cdate = cdate;
  }

  public Shop(int shopCd, long ownerId, String login, Long siteId, Long payGatewayId, String passwd,
      Date cdate) {
    this.shopCd = shopCd;
    this.ownerId = ownerId;
    this.login = login;
    this.siteId = siteId;
    this.payGatewayId = payGatewayId;
    this.passwd = passwd;
    this.cdate = cdate;
  }

  public Long getShopId() {
    return this.shopId;
  }

  public void setShopId(Long shopId) {
    this.shopId = shopId;
  }

  public int getShopCd() {
    return this.shopCd;
  }

  public void setShopCd(int shopCd) {
    this.shopCd = shopCd;
  }

  public long getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
  }

  public String getLogin() {
    return this.login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public Long getSiteId() {
    return this.siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public Long getPayGatewayId() {
    return this.payGatewayId;
  }

  public void setPayGatewayId(Long payGatewayId) {
    this.payGatewayId = payGatewayId;
  }

  public String getPasswd() {
    return this.passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public Date getCdate() {
    return this.cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

}
