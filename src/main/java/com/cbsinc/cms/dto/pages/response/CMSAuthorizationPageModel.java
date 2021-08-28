package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CMSAuthorizationPageModel extends PageModel {
  
  @JsonInclude(Include.NON_NULL)
  String login;
  
  @JsonInclude(Include.NON_NULL)
  String strMessage;
  
  @JsonInclude(Include.NON_NULL)
  String User_site;
  
  @JsonInclude(Include.NON_NULL)
  String strPasswd;
  
  @JsonInclude(Include.NON_NULL)
  String site_id;
  
  @JsonInclude(Include.NON_NULL)
  Long intUserID;
  
  @JsonInclude(Include.NON_NULL)
  long intLevelUp;
  
  @JsonInclude(Include.NON_NULL)
  String strLogin;
  
  @JsonInclude(Include.NON_NULL)
  String select_city;
  
  @JsonInclude(Include.NON_NULL)
  String select_country;
  
  @JsonInclude(Include.NON_NULL)
  String select_currency;
  
  @JsonInclude(Include.NON_NULL)
  String select_site;
  
  @JsonInclude(Include.NON_NULL)
  String select_menu_catalog;

  @JsonInclude(Include.NON_NULL)
  String strFirstName;
  
  @JsonInclude(Include.NON_NULL)
  String strLastName;

  @JsonInclude(Include.NON_NULL)
  String strEMail;
  
  @JsonInclude(Include.NON_NULL)
  String strPhone;
  
  @JsonInclude(Include.NON_NULL)
  String strMPhone;
  
  @JsonInclude(Include.NON_NULL)
  String strFax;
  
  @JsonInclude(Include.NON_NULL)
  String strIcq;
  
  @JsonInclude(Include.NON_NULL)
  String strWebsite;
  
  @JsonInclude(Include.NON_NULL)
  String strCompany;
  
  @JsonInclude(Include.NON_NULL)
  String country_id;
  
  @JsonInclude(Include.NON_NULL)
  String city_id;
  
  @JsonInclude(Include.NON_NULL)
  String currency_id;
  

  
  
  @JsonCreator
  public CMSAuthorizationPageModel(AuthorizationPageBean authorizationPageBeanId) {
    this.login = authorizationPageBeanId.getStrLogin();
    this.strMessage = authorizationPageBeanId.getStrMessage();
    this.User_site = authorizationPageBeanId.getUser_site();
    this.strPasswd = authorizationPageBeanId.getStrPasswd();
    this.site_id = authorizationPageBeanId.getSite_id();
    this.strLogin = authorizationPageBeanId.getStrLogin();
    this.intUserID = authorizationPageBeanId.getIntUserID();
    this.intLevelUp = authorizationPageBeanId.getIntLevelUp();
    this.select_city = authorizationPageBeanId.getSelect_city();
    this.select_country = authorizationPageBeanId.getSelect_country();
    this.select_currency = authorizationPageBeanId.getSelect_currency();
    this.select_site = authorizationPageBeanId.getSelect_site();
    this.select_menu_catalog = authorizationPageBeanId.getSelect_menu_catalog();
    this.strFirstName=authorizationPageBeanId.getStrFirstName();
    this.strLastName=authorizationPageBeanId.getStrLastName();
    this.strEMail=authorizationPageBeanId.getStrEMail();
    this.strPhone=authorizationPageBeanId.getStrPhone();
    this.strMPhone=authorizationPageBeanId.getStrMPhone();
    this.strFax=authorizationPageBeanId.getStrFax();
    this.strIcq=authorizationPageBeanId.getStrIcq();
    this.strWebsite=authorizationPageBeanId.getStrWebsite();
    this.strCompany=authorizationPageBeanId.getStrCompany();
    this.country_id=authorizationPageBeanId.getCountry_id();
    this.city_id=authorizationPageBeanId.getCity_id();
    this.currency_id=authorizationPageBeanId.getCurrency_id();
  }




  public String getLogin() {
    return login;
  }




  public void setLogin(String login) {
    this.login = login;
  }




  public String getStrMessage() {
    return strMessage;
  }




  public void setStrMessage(String strMessage) {
    this.strMessage = strMessage;
  }




  public String getUser_site() {
    return User_site;
  }




  public void setUser_site(String user_site) {
    User_site = user_site;
  }




  public String getStrPasswd() {
    return strPasswd;
  }




  public void setStrPasswd(String strPasswd) {
    this.strPasswd = strPasswd;
  }




  public String getSite_id() {
    return site_id;
  }




  public void setSite_id(String site_id) {
    this.site_id = site_id;
  }




  public Long getIntUserID() {
    return intUserID;
  }




  public void setIntUserID(Long intUserID) {
    this.intUserID = intUserID;
  }




  public long getIntLevelUp() {
    return intLevelUp;
  }




  public void setIntLevelUp(long intLevelUp) {
    this.intLevelUp = intLevelUp;
  }




  public String getStrLogin() {
    return strLogin;
  }




  public void setStrLogin(String strLogin) {
    this.strLogin = strLogin;
  }




  public String getSelect_city() {
    return select_city;
  }




  public void setSelect_city(String select_city) {
    this.select_city = select_city;
  }




  public String getSelect_country() {
    return select_country;
  }




  public void setSelect_country(String select_country) {
    this.select_country = select_country;
  }




  public String getSelect_currency() {
    return select_currency;
  }




  public void setSelect_currency(String select_currency) {
    this.select_currency = select_currency;
  }




  public String getSelect_site() {
    return select_site;
  }




  public void setSelect_site(String select_site) {
    this.select_site = select_site;
  }




  public String getSelect_menu_catalog() {
    return select_menu_catalog;
  }




  public void setSelect_menu_catalog(String select_menu_catalog) {
    this.select_menu_catalog = select_menu_catalog;
  }




  public String getStrFirstName() {
    return strFirstName;
  }




  public void setStrFirstName(String strFirstName) {
    this.strFirstName = strFirstName;
  }




  public String getStrLastName() {
    return strLastName;
  }




  public void setStrLastName(String strLastName) {
    this.strLastName = strLastName;
  }




  public String getStrEMail() {
    return strEMail;
  }




  public void setStrEMail(String strEMail) {
    this.strEMail = strEMail;
  }




  public String getStrPhone() {
    return strPhone;
  }




  public void setStrPhone(String strPhone) {
    this.strPhone = strPhone;
  }




  public String getStrMPhone() {
    return strMPhone;
  }




  public void setStrMPhone(String strMPhone) {
    this.strMPhone = strMPhone;
  }




  public String getStrFax() {
    return strFax;
  }




  public void setStrFax(String strFax) {
    this.strFax = strFax;
  }




  public String getStrIcq() {
    return strIcq;
  }




  public void setStrIcq(String strIcq) {
    this.strIcq = strIcq;
  }




  public String getStrWebsite() {
    return strWebsite;
  }




  public void setStrWebsite(String strWebsite) {
    this.strWebsite = strWebsite;
  }




  public String getStrCompany() {
    return strCompany;
  }




  public void setStrCompany(String strCompany) {
    this.strCompany = strCompany;
  }




  public String getCountry_id() {
    return country_id;
  }




  public void setCountry_id(String country_id) {
    this.country_id = country_id;
  }




  public String getCity_id() {
    return city_id;
  }




  public void setCity_id(String city_id) {
    this.city_id = city_id;
  }




  public String getCurrency_id() {
    return currency_id;
  }




  public void setCurrency_id(String currency_id) {
    this.currency_id = currency_id;
  }

  
  
}
