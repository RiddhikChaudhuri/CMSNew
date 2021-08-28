package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "creteria1")
public class Creteria1 implements java.io.Serializable {


  private static final long serialVersionUID = 1038616620043663165L;

  @Id
  @Column(name="CRETERIA1_ID")
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long creteria1Id;
  
  @Column(name="NAME",length=100)
  private String name;
  
  @Column(name="ACTIVE")
  private Boolean active;
  
  @Column(name="LANG_ID")
  private Long langId;
  
  @Column(name="LINK_ID",nullable = false)
  private long linkId;
  
  @Column(name="CATALOG_ID",nullable=false)
  private long catalogId;
  
  @Column(name="LABEL",length=100)
  private String label;

  public Creteria1() {}

  public Creteria1(long creteria1Id, long linkId, long catalogId) {
    this.creteria1Id = creteria1Id;
    this.linkId = linkId;
    this.catalogId = catalogId;
  }

  public Creteria1(long creteria1Id, String name, Boolean active, Long langId, long linkId,
      long catalogId, String label) {
    this.creteria1Id = creteria1Id;
    this.name = name;
    this.active = active;
    this.langId = langId;
    this.linkId = linkId;
    this.catalogId = catalogId;
    this.label = label;
  }

  public long getCreteria1Id() {
    return this.creteria1Id;
  }

  public void setCreteria1Id(long creteria1Id) {
    this.creteria1Id = creteria1Id;
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
