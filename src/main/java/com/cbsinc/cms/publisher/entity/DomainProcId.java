package com.cbsinc.cms.publisher.entity;



import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Embeddable
public class DomainProcId implements java.io.Serializable {

  @Column(name = "DOMAIN_PROC_ID")
  private long domainProcId;

  @Column(name = "LAST_DATE", length = 19)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastDate;

  public DomainProcId() {}

  public DomainProcId(long domainProcId, Date lastDate) {
    this.domainProcId = domainProcId;
    this.lastDate = lastDate;
  }

  public long getDomainProcId() {
    return this.domainProcId;
  }

  public void setDomainProcId(long domainProcId) {
    this.domainProcId = domainProcId;
  }

  public Date getLastDate() {
    return this.lastDate;
  }

  public void setLastDate(Date lastDate) {
    this.lastDate = lastDate;
  }

  public boolean equals(Object other) {
    if ((this == other))
      return true;
    if ((other == null))
      return false;
    if (!(other instanceof DomainProcId))
      return false;
    DomainProcId castOther = (DomainProcId) other;

    return (this.getDomainProcId() == castOther.getDomainProcId())
        && ((this.getLastDate() == castOther.getLastDate())
            || (this.getLastDate() != null && castOther.getLastDate() != null
                && this.getLastDate().equals(castOther.getLastDate())));
  }

  public int hashCode() {
    int result = 17;

    result = 37 * result + (int) this.getDomainProcId();
    result = 37 * result + (getLastDate() == null ? 0 : this.getLastDate().hashCode());
    return result;
  }

}
