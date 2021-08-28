package com.cbsinc.cms.publisher.entity;

// Generated 29.03.2014 21:04:05 by Hibernate Tools 3.4.0.CR1

import java.util.Arrays;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class StoreSessionId implements java.io.Serializable {

  @Column(name = "IDSESSION_HASH1")
  private Long idsessionHash1;

  @Column(name = "IDSESSION_HASH2")
  private Long idsessionHash2;

  @Column(name = "IDSESSION_HASH3")
  private Long idsessionHash3;

  @Column(name = "IDSESSION_HASH4")
  private Long idsessionHash4;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "TYPE", length = 200)
  private String type;

  @Column(name = "LASTURL", length = 500)
  private String lasturl;

  @Column(name = "CLASSBODY", length = 250)
  private String classbody;

  @Column(name = "BCLASSBODY")
  private byte[] bclassbody;

  @Column(name = "ACTIVE")
  private boolean active;

  @Column(name = "CDATE", length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date cdate;

  public StoreSessionId() {}

  public StoreSessionId(boolean active, Date cdate) {
    this.active = active;
    this.cdate = cdate;
  }

  public StoreSessionId(Long idsessionHash1, Long idsessionHash2, Long idsessionHash3,
      Long idsessionHash4, Long userId, String type, String lasturl, String classbody,
      byte[] bclassbody, boolean active, Date cdate) {
    this.idsessionHash1 = idsessionHash1;
    this.idsessionHash2 = idsessionHash2;
    this.idsessionHash3 = idsessionHash3;
    this.idsessionHash4 = idsessionHash4;
    this.userId = userId;
    this.type = type;
    this.lasturl = lasturl;
    this.classbody = classbody;
    this.bclassbody = bclassbody;
    this.active = active;
    this.cdate = cdate;
  }

  public Long getIdsessionHash1() {
    return this.idsessionHash1;
  }

  public void setIdsessionHash1(Long idsessionHash1) {
    this.idsessionHash1 = idsessionHash1;
  }

  public Long getIdsessionHash2() {
    return this.idsessionHash2;
  }

  public void setIdsessionHash2(Long idsessionHash2) {
    this.idsessionHash2 = idsessionHash2;
  }

  public Long getIdsessionHash3() {
    return this.idsessionHash3;
  }

  public void setIdsessionHash3(Long idsessionHash3) {
    this.idsessionHash3 = idsessionHash3;
  }

  public Long getIdsessionHash4() {
    return this.idsessionHash4;
  }

  public void setIdsessionHash4(Long idsessionHash4) {
    this.idsessionHash4 = idsessionHash4;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getType() {
    return this.type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getLasturl() {
    return this.lasturl;
  }

  public void setLasturl(String lasturl) {
    this.lasturl = lasturl;
  }

  public String getClassbody() {
    return this.classbody;
  }

  public void setClassbody(String classbody) {
    this.classbody = classbody;
  }

  public byte[] getBclassbody() {
    return this.bclassbody;
  }

  public void setBclassbody(byte[] bclassbody) {
    this.bclassbody = bclassbody;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Date getCdate() {
    return this.cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof StoreSessionId))
      return false;
    StoreSessionId castOther = (StoreSessionId) other;

    return ((this.getIdsessionHash1() == castOther.getIdsessionHash1())
        || (this.getIdsessionHash1() != null && castOther.getIdsessionHash1() != null
            && this.getIdsessionHash1().equals(castOther.getIdsessionHash1())))
        && ((this.getIdsessionHash2() == castOther.getIdsessionHash2())
            || (this.getIdsessionHash2() != null && castOther.getIdsessionHash2() != null
                && this.getIdsessionHash2().equals(castOther.getIdsessionHash2())))
        && ((this.getIdsessionHash3() == castOther.getIdsessionHash3())
            || (this.getIdsessionHash3() != null && castOther.getIdsessionHash3() != null
                && this.getIdsessionHash3().equals(castOther.getIdsessionHash3())))
        && ((this.getIdsessionHash4() == castOther.getIdsessionHash4())
            || (this.getIdsessionHash4() != null && castOther.getIdsessionHash4() != null
                && this.getIdsessionHash4().equals(castOther.getIdsessionHash4())))
        && ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
            && castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())))
        && ((this.getType() == castOther.getType()) || (this.getType() != null
            && castOther.getType() != null && this.getType().equals(castOther.getType())))
        && ((this.getLasturl() == castOther.getLasturl()) || (this.getLasturl() != null
            && castOther.getLasturl() != null && this.getLasturl().equals(castOther.getLasturl())))
        && ((this.getClassbody() == castOther.getClassbody())
            || (this.getClassbody() != null && castOther.getClassbody() != null
                && this.getClassbody().equals(castOther.getClassbody())))
        && ((this.getBclassbody() == castOther.getBclassbody())
            || (this.getBclassbody() != null && castOther.getBclassbody() != null
                && Arrays.equals(this.getBclassbody(), castOther.getBclassbody())))
        && (this.isActive() == castOther.isActive())
        && ((this.getCdate() == castOther.getCdate()) || (this.getCdate() != null
            && castOther.getCdate() != null && this.getCdate().equals(castOther.getCdate())));
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (getIdsessionHash1() == null ? 0 : this.getIdsessionHash1().hashCode());
    result = 37 * result + (getIdsessionHash2() == null ? 0 : this.getIdsessionHash2().hashCode());
    result = 37 * result + (getIdsessionHash3() == null ? 0 : this.getIdsessionHash3().hashCode());
    result = 37 * result + (getIdsessionHash4() == null ? 0 : this.getIdsessionHash4().hashCode());
    result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());
    result = 37 * result + (getType() == null ? 0 : this.getType().hashCode());
    result = 37 * result + (getLasturl() == null ? 0 : this.getLasturl().hashCode());
    result = 37 * result + (getClassbody() == null ? 0 : this.getClassbody().hashCode());
    int bclassbodyHashcode = 0;
    byte[] bclassbodyProperty = this.getBclassbody();
    if (bclassbodyProperty != null) {
      bclassbodyHashcode = 1;
      for (int i = 0; i < bclassbodyProperty.length; i++) {
        bclassbodyHashcode = 37 * bclassbodyHashcode + bclassbodyProperty[i];
      }
    }

    result = 37 * result + bclassbodyHashcode;

    result = 37 * result + (this.isActive() ? 1 : 0);
    result = 37 * result + (getCdate() == null ? 0 : this.getCdate().hashCode());
    return result;
  }

}
