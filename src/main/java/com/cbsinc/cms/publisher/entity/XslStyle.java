package com.cbsinc.cms.publisher.entity;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "xsl_style")
public class XslStyle implements java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "XSL_STYLE_ID")
  private int xslStyleId;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "PRODUCER_ID", nullable = false)
  private int producerId;

  @Column(name = "OWNER_ID", nullable = false)
  private long ownerId;

  @Column(name = "COST", precision = 22, scale = 0, nullable = false)
  private double cost;

  @Column(name = "CURRENCY_ID", nullable = false)
  private long currencyId;

  @Column(name = "SYS_DATE", length = 19, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date sysDate;

  @Column(name = "SITE_ID", nullable = false)
  private long siteId;

  @Column(name = "DIRNAME", length = 200)
  private String dirname;

  @Column(name = "XSL_SUBJ_ID")
  private Integer xslSubjId;

  public XslStyle() {}

  public XslStyle(int xslStyleId, boolean active, int producerId, long ownerId, double cost,
      long currencyId, Date sysDate, long siteId) {
    this.xslStyleId = xslStyleId;
    this.active = active;
    this.producerId = producerId;
    this.ownerId = ownerId;
    this.cost = cost;
    this.currencyId = currencyId;
    this.sysDate = sysDate;
    this.siteId = siteId;
  }

  public XslStyle(int xslStyleId, String name, String description, boolean active, int producerId,
      long ownerId, double cost, long currencyId, Date sysDate, long siteId, String dirname,
      Integer xslSubjId) {
    this.xslStyleId = xslStyleId;
    this.name = name;
    this.description = description;
    this.active = active;
    this.producerId = producerId;
    this.ownerId = ownerId;
    this.cost = cost;
    this.currencyId = currencyId;
    this.sysDate = sysDate;
    this.siteId = siteId;
    this.dirname = dirname;
    this.xslSubjId = xslSubjId;
  }

  public int getXslStyleId() {
    return this.xslStyleId;
  }

  public void setXslStyleId(int xslStyleId) {
    this.xslStyleId = xslStyleId;
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

  public int getProducerId() {
    return this.producerId;
  }

  public void setProducerId(int producerId) {
    this.producerId = producerId;
  }

  public long getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(long ownerId) {
    this.ownerId = ownerId;
  }

  public double getCost() {
    return this.cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }

  public long getCurrencyId() {
    return this.currencyId;
  }

  public void setCurrencyId(long currencyId) {
    this.currencyId = currencyId;
  }

  public Date getSysDate() {
    return this.sysDate;
  }

  public void setSysDate(Date sysDate) {
    this.sysDate = sysDate;
  }

  public long getSiteId() {
    return this.siteId;
  }

  public void setSiteId(long siteId) {
    this.siteId = siteId;
  }

  public String getDirname() {
    return this.dirname;
  }

  public void setDirname(String dirname) {
    this.dirname = dirname;
  }

  public Integer getXslSubjId() {
    return this.xslSubjId;
  }

  public void setXslSubjId(Integer xslSubjId) {
    this.xslSubjId = xslSubjId;
  }

}
