package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class PayGatewayId implements java.io.Serializable {

  private static final long serialVersionUID = -1476454881033632685L;

  @Column(name = "PAY_GATEWAY_ID")
  private long payGatewayId;

  @Column(name = "NAME_GATEWAY", length = 50)
  private String nameGateway;

  @Column(name = "FULLNAME_GATEWAY", length = 500)
  private String fullnameGateway;

  @Column(name = "PHONE", length = 15)
  private String phone;

  @Column(name = "FAX", length = 15)
  private String fax;

  @Column(name = "SITE_URL", length = 500)
  private String siteUrl;

  @Column(name = "ACTIVE")
  private boolean active;

  public PayGatewayId() {}

  public PayGatewayId(long payGatewayId, boolean active) {
    this.payGatewayId = payGatewayId;
    this.active = active;
  }

  public PayGatewayId(long payGatewayId, String nameGateway, String fullnameGateway, String phone,
      String fax, String siteUrl, boolean active) {
    this.payGatewayId = payGatewayId;
    this.nameGateway = nameGateway;
    this.fullnameGateway = fullnameGateway;
    this.phone = phone;
    this.fax = fax;
    this.siteUrl = siteUrl;
    this.active = active;
  }

  public long getPayGatewayId() {
    return this.payGatewayId;
  }

  public void setPayGatewayId(long payGatewayId) {
    this.payGatewayId = payGatewayId;
  }

  public String getNameGateway() {
    return this.nameGateway;
  }

  public void setNameGateway(String nameGateway) {
    this.nameGateway = nameGateway;
  }

  public String getFullnameGateway() {
    return this.fullnameGateway;
  }

  public void setFullnameGateway(String fullnameGateway) {
    this.fullnameGateway = fullnameGateway;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFax() {
    return this.fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getSiteUrl() {
    return this.siteUrl;
  }

  public void setSiteUrl(String siteUrl) {
    this.siteUrl = siteUrl;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof PayGatewayId))
      return false;
    PayGatewayId castOther = (PayGatewayId) other;

    return (this.getPayGatewayId() == castOther.getPayGatewayId())
        && ((this.getNameGateway() == castOther.getNameGateway())
            || (this.getNameGateway() != null && castOther.getNameGateway() != null
                && this.getNameGateway().equals(castOther.getNameGateway())))
        && ((this.getFullnameGateway() == castOther.getFullnameGateway())
            || (this.getFullnameGateway() != null && castOther.getFullnameGateway() != null
                && this.getFullnameGateway().equals(castOther.getFullnameGateway())))
        && ((this.getPhone() == castOther.getPhone()) || (this.getPhone() != null
            && castOther.getPhone() != null && this.getPhone().equals(castOther.getPhone())))
        && ((this.getFax() == castOther.getFax()) || (this.getFax() != null
            && castOther.getFax() != null && this.getFax().equals(castOther.getFax())))
        && ((this.getSiteUrl() == castOther.getSiteUrl()) || (this.getSiteUrl() != null
            && castOther.getSiteUrl() != null && this.getSiteUrl().equals(castOther.getSiteUrl())))
        && (this.isActive() == castOther.isActive());
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (int) this.getPayGatewayId();
    result = 37 * result + (getNameGateway() == null ? 0 : this.getNameGateway().hashCode());
    result =
        37 * result + (getFullnameGateway() == null ? 0 : this.getFullnameGateway().hashCode());
    result = 37 * result + (getPhone() == null ? 0 : this.getPhone().hashCode());
    result = 37 * result + (getFax() == null ? 0 : this.getFax().hashCode());
    result = 37 * result + (getSiteUrl() == null ? 0 : this.getSiteUrl().hashCode());
    result = 37 * result + (this.isActive() ? 1 : 0);
    return result;
  }

}
