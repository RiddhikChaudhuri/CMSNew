package com.cbsinc.cms.publisher.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "creteria5")
public class Creteria5 implements java.io.Serializable {

  @Id
  @Column(name = "CRETERIA5_ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long creteria5Id;

  @Column(name = "NAME", length = 100)
  private String name;

  @Column(name = "ACTIVE")
  private Boolean active;

  @Column(name = "LANG_ID")
  private Long langId;

  @Column(name = "LINK_ID", nullable = false)
  private long linkId;

  @Column(name = "CATALOG_ID", nullable = false)
  private long catalogId;

  @Column(name = "LABEL", length = 100)
  private String label;

  public Creteria5() {}

  public Creteria5(long creteria5Id, long linkId, long catalogId) {
    this.creteria5Id = creteria5Id;
    this.linkId = linkId;
    this.catalogId = catalogId;
  }

  public Creteria5(long creteria5Id, String name, Boolean active, Long langId, long linkId,
      long catalogId, String label) {
    this.creteria5Id = creteria5Id;
    this.name = name;
    this.active = active;
    this.langId = langId;
    this.linkId = linkId;
    this.catalogId = catalogId;
    this.label = label;
  }

  public long getCreteria5Id() {
    return this.creteria5Id;
  }

  public void setCreteria5Id(long creteria5Id) {
    this.creteria5Id = creteria5Id;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Long getLangId() {
    return this.langId;
  }

  public void setLangId(Long langId) {
    this.langId = langId;
  }

  public long getLinkId() {
    return this.linkId;
  }

  public void setLinkId(long linkId) {
    this.linkId = linkId;
  }

  public long getCatalogId() {
    return this.catalogId;
  }

  public void setCatalogId(long catalogId) {
    this.catalogId = catalogId;
  }

  public String getLabel() {
    return this.label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

}
