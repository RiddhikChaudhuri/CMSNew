package com.cbsinc.cms.publisher.entity;

// Generated 29.03.2014 21:04:05 by Hibernate Tools 3.4.0.CR1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "tree")
public class Tree implements java.io.Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "ITEM_ID")
  private long itemId;

  @Column(name = "NODE_ID")
  private Long nodeId;

  @Column(name = "FOLDER_NAME", length = 50)
  private String folderName;

  @Column(name = "OWNER_ID")
  private Long ownerId;

  @Column(name = "GROUP_ID")
  private Integer groupId;

  @Column(name = "ACTIVE", nullable = false)
  private boolean active;

  @Column(name = "DOC_ID")
  private Integer docId;

  @Column(name = "CDATE", length = 19, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date cdate;

  @Column(name = "LAST_TOUCH_ID")
  private Integer lastTouchId;

  @Column(name = "LAST_TIME", length = 19, nullable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date lastTime;

  public Tree() {}

  public Tree(long itemId, boolean active, Date cdate, Date lastTime) {
    this.itemId = itemId;
    this.active = active;
    this.cdate = cdate;
    this.lastTime = lastTime;
  }

  public Tree(long itemId, Long nodeId, String folderName, Long ownerId, Integer groupId,
      boolean active, Integer docId, Date cdate, Integer lastTouchId, Date lastTime) {
    this.itemId = itemId;
    this.nodeId = nodeId;
    this.folderName = folderName;
    this.ownerId = ownerId;
    this.groupId = groupId;
    this.active = active;
    this.docId = docId;
    this.cdate = cdate;
    this.lastTouchId = lastTouchId;
    this.lastTime = lastTime;
  }

  public long getItemId() {
    return this.itemId;
  }

  public void setItemId(long itemId) {
    this.itemId = itemId;
  }

  public Long getNodeId() {
    return this.nodeId;
  }

  public void setNodeId(Long nodeId) {
    this.nodeId = nodeId;
  }

  public String getFolderName() {
    return this.folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public Long getOwnerId() {
    return this.ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  public Integer getGroupId() {
    return this.groupId;
  }

  public void setGroupId(Integer groupId) {
    this.groupId = groupId;
  }

  public boolean isActive() {
    return this.active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Integer getDocId() {
    return this.docId;
  }

  public void setDocId(Integer docId) {
    this.docId = docId;
  }

  public Date getCdate() {
    return this.cdate;
  }

  public void setCdate(Date cdate) {
    this.cdate = cdate;
  }

  public Integer getLastTouchId() {
    return this.lastTouchId;
  }

  public void setLastTouchId(Integer lastTouchId) {
    this.lastTouchId = lastTouchId;
  }

  public Date getLastTime() {
    return this.lastTime;
  }

  public void setLastTime(Date lastTime) {
    this.lastTime = lastTime;
  }

}
