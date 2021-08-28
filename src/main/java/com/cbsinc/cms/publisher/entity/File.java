package com.cbsinc.cms.publisher.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "file")
public class File implements java.io.Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "FILE_ID")
  private long fileId;

  @Column(name = "NAME", length = 50)
  private String name;

  @Column(name = "FILEDATA")
  private byte[] filedata;

  @Column(name = "USER_ID")
  private Long userId;

  @Column(name = "SIZE", length = 15)
  private String size;

  @Column(name = "PATH", length = 500)
  private String path;

  public File() {}

  public File(long fileId) {
    this.fileId = fileId;
  }

  public File(long fileId, String name, byte[] filedata, Long userId, String size, String path) {
    this.fileId = fileId;
    this.name = name;
    this.filedata = filedata;
    this.userId = userId;
    this.size = size;
    this.path = path;
  }

  public long getFileId() {
    return this.fileId;
  }

  public void setFileId(long fileId) {
    this.fileId = fileId;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public byte[] getFiledata() {
    return this.filedata;
  }

  public void setFiledata(byte[] filedata) {
    this.filedata = filedata;
  }

  public Long getUserId() {
    return this.userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getSize() {
    return this.size;
  }

  public void setSize(String size) {
    this.size = size;
  }

  public String getPath() {
    return this.path;
  }

  public void setPath(String path) {
    this.path = path;
  }

}
