package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "portlettype")
public class Portlettype implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "PORTLETTYPE_ID")
  private long portlettypeId;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "LANG_ID")
  private Long langId;

  public Portlettype() {}

  public Portlettype(long portlettypeId) {
    this.portlettypeId = portlettypeId;
  }

  public Portlettype(long portlettypeId, String name, Long langId) {
    this.portlettypeId = portlettypeId;
    this.name = name;
    this.langId = langId;
  }

  public long getPortlettypeId() {
    return this.portlettypeId;
  }

  public void setPortlettypeId(long portlettypeId) {
    this.portlettypeId = portlettypeId;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getLangId() {
    return this.langId;
  }

  public void setLangId(Long langId) {
    this.langId = langId;
  }

}
