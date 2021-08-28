package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currency_rate")
public class CurrencyRate implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "CURRENCY_RATE_ID")
  private long currencyRateId;

  public CurrencyRate() {}

  public CurrencyRate(long currencyRateId) {
    this.currencyRateId = currencyRateId;
  }

  public long getCurrencyRateId() {
    return this.currencyRateId;
  }

  public void setCurrencyRateId(long currencyRateId) {
    this.currencyRateId = currencyRateId;
  }

}
