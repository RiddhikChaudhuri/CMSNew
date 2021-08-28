package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "site")
public class Site implements java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "SITE_ID")
  private long siteId;

  @Column(name = "HOST", length = 100)
  private String host;

  @Column(name = "OWNER")
  private Long owner;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "HOME_DIR", length = 100)
  private String homeDir;

  @Column(name = "SITE_DIR", length = 100)
  private String siteDir;

  @Column(name = "PERSON", length = 200)
  private String person;

  @Column(name = "PHONE", length = 50)
  private String phone;

  @Column(name = "ADDRESS", length = 300)
  private String address;

  @Column(name = "SUBJECT_SITE", length = 300)
  private String subjectSite;

  @Column(name = "NICK_SITE", length = 200)
  private String nickSite;

  @Column(name = "COMPANY_NAME", length = 100)
  private String companyName;

  public Site() {}

  public Site(long siteId, boolean active) {
    this.siteId = siteId;
    this.active = active;
  }

  public Site(long siteId, String host, Long owner, boolean active, String homeDir, String siteDir,
      String person, String phone, String address, String subjectSite, String nickSite,
      String companyName) {
    this.siteId = siteId;
    this.host = host;
    this.owner = owner;
    this.active = active;
    this.homeDir = homeDir;
    this.siteDir = siteDir;
    this.person = person;
    this.phone = phone;
    this.address = address;
    this.subjectSite = subjectSite;
    this.nickSite = nickSite;
    this.companyName = companyName;
  }

  public long getSiteId() {
    return this.siteId;
  }

  public void setSiteId(long siteId) {
    this.siteId = siteId;
  }

  public String getHost() {
    return this.host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public Long getOwner() {
    return this.owner;
  }

  public void setOwner(Long owner) {
    this.owner = owner;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getHomeDir() {
    return this.homeDir;
  }

  public void setHomeDir(String homeDir) {
    this.homeDir = homeDir;
  }

  public String getSiteDir() {
    return this.siteDir;
  }

  public void setSiteDir(String siteDir) {
    this.siteDir = siteDir;
  }

  public String getPerson() {
    return this.person;
  }

  public void setPerson(String person) {
    this.person = person;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getAddress() {
    return this.address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getSubjectSite() {
    return this.subjectSite;
  }

  public void setSubjectSite(String subjectSite) {
    this.subjectSite = subjectSite;
  }

  public String getNickSite() {
    return this.nickSite;
  }

  public void setNickSite(String nickSite) {
    this.nickSite = nickSite;
  }

  public String getCompanyName() {
    return this.companyName;
  }

  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }

}
