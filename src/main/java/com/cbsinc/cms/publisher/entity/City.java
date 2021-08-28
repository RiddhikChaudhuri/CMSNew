package com.cbsinc.cms.publisher.entity;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "city")
public class City implements java.io.Serializable {


  private static final long serialVersionUID = -8200360472533587305L;
  
  @EmbeddedId
  private CityId id;


}
