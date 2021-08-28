package com.cbsinc.cms.publisher.entity;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity(name = "accesslevel")
public class Accesslevel implements java.io.Serializable {

  /**
  * 
  */
  private static final long serialVersionUID = 7063951489224582028L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long accesslevelId;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "DESCRIPTION", length = 500)
  private String description;

  @Column(name = "ACTIVE", nullable = false)
  @Nonnull
  private boolean active;


  public Accesslevel(long accesslevelId, boolean active) {
    this.accesslevelId = accesslevelId;
    this.active = active;
  }


}
