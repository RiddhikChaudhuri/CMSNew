package com.cbsinc.cms.publisher.entity;


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
@Table(name = "orders_hist")
public class OrdersHist implements java.io.Serializable {

  private static final long serialVersionUID = -5713646572184585177L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ORDER_HIST_ID")
  private long orderHistId;

  @Column(name = "ORDER_HIST_CD")
  private Long orderHistCd;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "POSITION_ID")
  private Integer positionId;

  @Column(name = "DELIVERY_TIMEEND", nullable = false, length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date deliveryTimeend;

  @Column(name = "AMOUNT", precision = 22, scale = 0)
  private Double amount;

  @Column(name = "TAX", precision = 22, scale = 0)
  private Double tax;

  @Column(name = "END_AMOUNT", precision = 22, scale = 0)
  private Double endAmount;

  @Column(name = "DELIVERY_AMOUNT", precision = 22, scale = 0)
  private Double deliveryAmount;

  @Column(name = "CARD_ID")
  private Integer cardId;

  @Column(name = "DELIVERY_LONG")
  private Integer deliveryLong;

  @Column(name = "SHIPMENT_ADDRESS_ID")
  private Integer shipmentAddressId;

  @Column(name = "SERIAL_NUMBER", length = 50)
  private String serialNumber;

  @Column(name = "PAYSTATUS_ID")
  private Long paystatusId;

  @Column(name = "DELIVERYSTATUS_ID")
  private Long deliverystatusId;

  public OrdersHist() {}

  public OrdersHist(long orderHistId, Date deliveryTimeend) {
    this.orderHistId = orderHistId;
    this.deliveryTimeend = deliveryTimeend;
  }

  public OrdersHist(long orderHistId, Long orderHistCd, Long userId, Integer positionId,
      Date deliveryTimeend, Double amount, Double tax, Double endAmount, Double deliveryAmount,
      Integer cardId, Integer deliveryLong, Integer shipmentAddressId, String serialNumber,
      Long paystatusId, Long deliverystatusId) {
    this.orderHistId = orderHistId;
    this.orderHistCd = orderHistCd;
    this.userId = userId;
    this.positionId = positionId;
    this.deliveryTimeend = deliveryTimeend;
    this.amount = amount;
    this.tax = tax;
    this.endAmount = endAmount;
    this.deliveryAmount = deliveryAmount;
    this.cardId = cardId;
    this.deliveryLong = deliveryLong;
    this.shipmentAddressId = shipmentAddressId;
    this.serialNumber = serialNumber;
    this.paystatusId = paystatusId;
    this.deliverystatusId = deliverystatusId;
  }

  public long getOrderHistId() {
    return this.orderHistId;
  }

  public void setOrderHistId(long orderHistId) {
    this.orderHistId = orderHistId;
  }

  public Long getOrderHistCd() {
    return this.orderHistCd;
  }

  public void setOrderHistCd(Long orderHistCd) {
    this.orderHistCd = orderHistCd;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Integer getPositionId() {
    return this.positionId;
  }

  public void setPositionId(Integer positionId) {
    this.positionId = positionId;
  }

  public Date getDeliveryTimeend() {
    return this.deliveryTimeend;
  }

  public void setDeliveryTimeend(Date deliveryTimeend) {
    this.deliveryTimeend = deliveryTimeend;
  }

  public Double getAmount() {
    return this.amount;
  }

  public void setAmount(Double amount) {
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

  public Integer getCardId() {
    return this.cardId;
  }

  public void setCardId(Integer cardId) {
    this.cardId = cardId;
  }

  public Integer getDeliveryLong() {
    return this.deliveryLong;
  }

  public void setDeliveryLong(Integer deliveryLong) {
    this.deliveryLong = deliveryLong;
  }

  public Integer getShipmentAddressId() {
    return this.shipmentAddressId;
  }

  public void setShipmentAddressId(Integer shipmentAddressId) {
    this.shipmentAddressId = shipmentAddressId;
  }

  public String getSerialNumber() {
    return this.serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public Long getPaystatusId() {
    return this.paystatusId;
  }

  public void setPaystatusId(Long paystatusId) {
    this.paystatusId = paystatusId;
  }

  public Long getDeliverystatusId() {
    return this.deliverystatusId;
  }

  public void setDeliverystatusId(Long deliverystatusId) {
    this.deliverystatusId = deliverystatusId;
  }

}
