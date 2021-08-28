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
@Table(name = "orders")
public class Orders implements java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ORDER_ID")
  private long orderId;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "DELIVERY_TIMEEND", nullable = false, length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date deliveryTimeend;

  @Column(name = "AMOUNT", precision = 22, scale = 0, nullable = false)
  private double amount;

  @Column(name = "TAX", precision = 22, scale = 0)
  private Double tax;

  @Column(name = "END_AMOUNT", precision = 22, scale = 0)
  private Double endAmount;

  @Column(name = "DELIVERY_AMOUNT", precision = 22, scale = 0)
  private Double deliveryAmount;

  @Column(name = "DELIVERY_LONG")
  private Integer deliveryLong;

  @Column(name = "PAYSTATUS_ID")
  private Long paystatusId;

  @Column(name = "DELIVERY_START", length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date deliveryStart;

  @Column(name = "CDATE", length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date cdate;

  @Column(name = "CURRENCY_ID")
  private Long currencyId;

  @Column(name = "COUNTRY_ID")
  private Long countryId;

  @Column(name = "CITY_ID")
  private Long cityId;

  @Column(name = "ADDRESS", length = 200)
  private String address;

  @Column(name = "PHONE", length = 50)
  private String phone;

  @Column(name = "CONTACT_PERSON", length = 200)
  private String contactPerson;

  @Column(name = "EMAIL", length = 100)
  private String email;

  @Column(name = "FAX", length = 50)
  private String fax;

  @Column(name = "DESCRIPTION", length = 1000)
  private String description;

  @Column(name = "ZIP")
  private Integer zip;

  @Column(name = "TREE_ID")
  private Integer treeId;

  @Column(name = "IMEI")
  private Integer imei;

  @Column(name = "PHONEMODEL_ID")
  private Integer phonemodelId;

  @Column(name = "DELIVERYSTATUS_ID")
  private Long deliverystatusId;

  public Orders() {}

  public Orders(long orderId, Date deliveryTimeend, double amount) {
    this.orderId = orderId;
    this.deliveryTimeend = deliveryTimeend;
    this.amount = amount;
  }

  public Orders(long orderId, Long userId, Date deliveryTimeend, double amount, Double tax,
      Double endAmount, Double deliveryAmount, Integer deliveryLong, Long paystatusId,
      Date deliveryStart, Date cdate, Long currencyId, Long countryId, Long cityId, String address,
      String phone, String contactPerson, String email, String fax, String description, Integer zip,
      Integer treeId, Integer imei, Integer phonemodelId, Long deliverystatusId) {
    this.orderId = orderId;
    this.userId = userId;
    this.deliveryTimeend = deliveryTimeend;
    this.amount = amount;
    this.tax = tax;
    this.endAmount = endAmount;
    this.deliveryAmount = deliveryAmount;
    this.deliveryLong = deliveryLong;
    this.paystatusId = paystatusId;
    this.deliveryStart = deliveryStart;
    this.cdate = cdate;
    this.currencyId = currencyId;
    this.countryId = countryId;
    this.cityId = cityId;
    this.address = address;
    this.phone = phone;
    this.contactPerson = contactPerson;
    this.email = email;
    this.fax = fax;
    this.description = description;
    this.zip = zip;
    this.treeId = treeId;
    this.imei = imei;
    this.phonemodelId = phonemodelId;
    this.deliverystatusId = deliverystatusId;
  }

  public long getOrderId() {
    return this.orderId;
  }

  public void setOrderId(long orderId) {
    this.orderId = orderId;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Date getDeliveryTimeend() {
    return this.deliveryTimeend;
  }

  public void setDeliveryTimeend(Date deliveryTimeend) {
    this.deliveryTimeend = deliveryTimeend;
  }

  public double getAmount() {
    return this.amount;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public Double getTax() {
    return this.tax;
  }

  public void setTax(Double tax) {
    this.tax = tax;
  }

  public Double getEndAmount() {
    return this.endAmount;
  }

  public void setEndAmount(Double endAmount) {
    this.endAmount = endAmount;
  }

  public Double getDeliveryAmount() {
    return this.deliveryAmount;
  }

  public void setDeliveryAmount(Double deliveryAmount) {
    this.deliveryAmount = deliveryAmount;
  }

  public Integer getDeliveryLong() {
    return this.deliveryLong;
  }

  public void setDeliveryLong(Integer deliveryLong) {
    this.deliveryLong = deliveryLong;
  }

  public Long getPaystatusId() {
    return this.paystatusId;
  }

  public void setPaystatusId(Long paystatusId) {
    this.paystatusId = paystatusId;
  }

  public Date getDeliveryStart() {
    return this.deliveryStart;
  }

  public void setDeliveryStart(Date deliveryStart) {
    this.deliveryStart = deliveryStart;
  }

  public Date getCdate() {
    return this.cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

  public Long getCurrencyId() {
    return this.currencyId;
  }

  public void setCurrencyId(Long currencyId) {
    this.currencyId = currencyId;
  }

  public Long getCountryId() {
    return this.countryId;
  }

  public void setCountryId(Long countryId) {
    this.countryId = countryId;
  }

  public Long getCityId() {
    return this.cityId;
  }

  public void setCityId(Long cityId) {
    this.cityId = cityId;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getContactPerson() {
    return this.contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFax() {
    return this.fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Integer getZip() {
    return this.zip;
  }

  public void setZip(Integer zip) {
    this.zip = zip;
  }

  public Integer getTreeId() {
    return this.treeId;
  }

  public void setTreeId(Integer treeId) {
    this.treeId = treeId;
  }

  public Integer getImei() {
    return this.imei;
  }

  public void setImei(Integer imei) {
    this.imei = imei;
  }

  public Integer getPhonemodelId() {
    return this.phonemodelId;
  }

  public void setPhonemodelId(Integer phonemodelId) {
    this.phonemodelId = phonemodelId;
  }

  public Long getDeliverystatusId() {
    return this.deliverystatusId;
  }

  public void setDeliverystatusId(Long deliverystatusId) {
    this.deliverystatusId = deliverystatusId;
  }

}
