package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "licence")
public class Licence implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "LICENCE_ID")
  private long licenceId;

  @Column(name = "LABLE", length = 50)
  private String lable;

  @Column(name = "DESCRIPTION", length = 1000)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "SITE_ID")
  private Long siteId;

  @Column(name = "LANG_ID")
  private Long langId;

  public Licence() {}

  public Licence(long licenceId, boolean active) {
    this.licenceId = licenceId;
    this.active = active;
  }

  public Licence(long licenceId, String lable, String description, boolean active, Long siteId,
      Long langId) {
    this.licenceId = licenceId;
    this.lable = lable;
    this.description = description;
    this.active = active;
    this.siteId = siteId;
    this.langId = langId;
  }

  public long getLicenceId() {
    return this.licenceId;
  }

  public void setLicenceId(long licenceId) {
    this.licenceId = licenceId;
  }

  public String getLable() {
    return this.lable;
  }

  public void setLable(String lable) {
    this.lable = lable;
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

  public Long getSiteId() {
    return this.siteId;
  }

  public void setSiteId(Long siteId) {
    this.siteId = siteId;
  }

  public Long getLangId() {
    return this.langId;
  }

  public void setLangId(Long langId) {
    this.langId = langId;
  }

}
