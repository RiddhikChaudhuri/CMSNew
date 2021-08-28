package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "pay_gateway")
public class PayGateway implements java.io.Serializable {

  @EmbeddedId
  private PayGatewayId id;

  public PayGateway() {}

  public PayGateway(PayGatewayId id) {
    this.id = id;
  }

  public PayGatewayId getId() {
    return this.id;
  }

  public void setId(PayGatewayId id) {
    this.id = id;
  }

}
