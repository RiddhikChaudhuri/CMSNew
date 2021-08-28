package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "images")
public class Images implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name="IMAGE_ID")
  private long imageId;

  @Column(name = "IMGNAME", length = 50)
  private String imgname;

  @Column(name = "IMG_URL", length = 500)
  private String imgUrl;

  @Column(name = "USER_ID")
  private Long userId;

  public Images() {}

  public Images(long imageId) {
    this.imageId = imageId;
  }

  public Images(long imageId, String imgname, String imgUrl, Long userId) {
    this.imageId = imageId;
    this.imgname = imgname;
    this.imgUrl = imgUrl;
    this.userId = userId;
  }

  public long getImageId() {
    return this.imageId;
  }

  public void setImageId(long imageId) {
    this.imageId = imageId;
  }

  public String getImgname() {
    return this.imgname;
  }

  public void setImgname(String imgname) {
    this.imgname = imgname;
  }

  public String getImgUrl() {
    return this.imgUrl;
  }

  public void setImgUrl(String imgUrl) {
    this.imgUrl = imgUrl;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

}
