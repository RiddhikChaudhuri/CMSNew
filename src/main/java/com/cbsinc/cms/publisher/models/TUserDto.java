package com.cbsinc.cms.publisher.models;

import java.io.Serializable;
import java.util.Date;

public class TUserDto implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 6167434965665565989L;
  
  private long userId;
  private String login;
  private String passwd;
  private String firstName;
  private String lastName;
  private String EMail;
  private String phone;
  private String mobilPhone;
  private Date regdate;

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getPasswd() {
    return passwd;
  }

  public void setPasswd(String passwd) {
    this.passwd = passwd;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEMail() {
    return EMail;
  }

  public void setEMail(String eMail) {
    EMail = eMail;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getMobilPhone() {
    return mobilPhone;
  }

  public void setMobilPhone(String mobilPhone) {
    this.mobilPhone = mobilPhone;
  }

 

  public Date getRegdate() {
    return regdate;
  }

  public void setRegdate(Date regdate) {
    this.regdate = regdate;
  }

  public TUserDto(long userId, String login, String passwd, String firstName, String lastName,
      String eMail, String phone, String mobilPhone, Date regdate) {
    super();
    this.userId = userId;
    this.login = login;
    this.passwd = passwd;
    this.firstName = firstName;
    this.lastName = lastName;
    EMail = eMail;
    this.phone = phone;
    this.mobilPhone = mobilPhone;
    this.regdate = regdate;
  }


}
