package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "big_images")
public class BigImages implements java.io.Serializable {


  private static final long serialVersionUID = -1521709510542539149L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "BIG_IMAGES_ID")
  private long bigImagesId;

  @Column(name = "IMGNAME", length = 100)
  private String imgname;

  @Column(name = "IMG_URL", length = 200)
  private String imgUrl;

  @Column(name = "USER_ID")
  private Long userId;



  public BigImages(long bigImagesId) {
    this.bigImagesId = bigImagesId;
  }



}
