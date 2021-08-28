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
@Table(name = "currency")
public class Currency implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "CURRENCY_ID")
  private long currencyId;

  @Column(name = "RATE", precision = 22, scale = 0)
  private Double rate;

  @Column(name = "CURRENCY_LABLE", length = 10)
  private String currencyLable;

  @Column(name = "CURRENCY_DESC", length = 100)
  private String currencyDesc;

  @Column(name = "ACTIVE")
  private Boolean active;

  @Column(name = "CURRENCY_CD", length = 100)
  private String currencyCd;

  @Column(name = "CURSDATE", length = 100)
  private String cursdate;

  @Column(name = "CHANGEDATE", nullable = false, length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date changedate;

  public Currency() {}

  public Currency(long currencyId, Date changedate) {
    this.currencyId = currencyId;
    this.changedate = changedate;
  }

  public Currency(long currencyId, Double rate, String currencyLable, String currencyDesc,
      Boolean active, String currencyCd, String cursdate, Date changedate) {
    this.currencyId = currencyId;
    this.rate = rate;
    this.currencyLable = currencyLable;
    this.currencyDesc = currencyDesc;
    this.active = active;
    this.currencyCd = currencyCd;
    this.cursdate = cursdate;
    this.changedate = changedate;
  }

  public long getCurrencyId() {
    return this.currencyId;
  }

  public void setCurrencyId(long currencyId) {
    this.currencyId = currencyId;
  }

  public Double getRate() {
    return this.rate;
  }

  public void setRate(Double rate) {
    this.rate = rate;
  }

  public String getCurrencyLable() {
    return this.currencyLable;
  }

  public void setCurrencyLable(String currencyLable) {
    this.currencyLable = currencyLable;
  }

  public String getCurrencyDesc() {
    return this.currencyDesc;
  }

  public void setCurrencyDesc(String currencyDesc) {
    this.currencyDesc = currencyDesc;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public String getCurrencyCd() {
    return this.currencyCd;
  }

  public void setCurrencyCd(String currencyCd) {
    this.currencyCd = currencyCd;
  }

  public String getCursdate() {
    return this.cursdate;
  }

  public void setCursdate(String cursdate) {
    this.cursdate = cursdate;
  }

  public Date getChangedate() {
    return this.changedate;
  }

  public void setChangedate(Date changedate) {
    this.changedate = changedate;
  }

}
