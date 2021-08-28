package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CMSInstallResponseModel {
  
  @JsonInclude(Include.NON_NULL)
  private long intUserID;
  
  @JsonInclude(Include.NON_NULL)
  private long intLevelUp;
  
  @JsonInclude(Include.NON_NULL)
  private String strPasswd;
  
  @JsonInclude(Include.NON_NULL)
  private String strLogin;
  
  @JsonInclude(Include.NON_NULL)
  private String strMessage;
  
  @JsonInclude(Include.NON_NULL)
  private String user_site;
  
  @JsonInclude(Include.NON_NULL)
  private String currency_id;
  
  @JsonInclude(Include.NON_NULL)
  private String city_id;
  
  @JsonInclude(Include.NON_NULL)
  private String country_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strAnswer;
  
  @JsonInclude(Include.NON_NULL)
  private String strQuestion;
  
  @JsonInclude(Include.NON_NULL)
  private String strWebsite;
  
  @JsonInclude(Include.NON_NULL)
  private String address;
  
  @JsonInclude(Include.NON_NULL)
  private String strFax;
  
  @JsonInclude(Include.NON_NULL)
  private String strMPhone;
  
  @JsonInclude(Include.NON_NULL)
  private String strPhone;
  
  @JsonInclude(Include.NON_NULL)
  private String strEMail;
  
  @JsonInclude(Include.NON_NULL)
  private String strCompany;
  
  @JsonInclude(Include.NON_NULL)
  private String strLastName;
  
  @JsonInclude(Include.NON_NULL)
  private String strFirstName;
  
  @JsonInclude(Include.NON_NULL)
  private String strCPasswd;

  @JsonInclude(Include.NON_NULL)
  private String select_country;
  
  @JsonInclude(Include.NON_NULL)
  private String select_city;
  
  public CMSInstallResponseModel(AuthorizationPageBean authorizationPageBeanId) {
    this.intUserID = authorizationPageBeanId.getIntUserID();
    this.intLevelUp = authorizationPageBeanId.getIntLevelUp();
    this.strPasswd = authorizationPageBeanId.getStrPasswd();
    this.strLogin = authorizationPageBeanId.getStrLogin();
    this.strMessage = authorizationPageBeanId.getStrMessage();
    this.user_site = authorizationPageBeanId.getUser_site();
    this.currency_id = authorizationPageBeanId.getCurrency_id();
    this.city_id = authorizationPageBeanId.getCity_id();
    this.country_id = authorizationPageBeanId.getCountry_id();
    this.strAnswer = authorizationPageBeanId.getStrAnswer();
    this.strQuestion = authorizationPageBeanId.getStrQuestion();
    this.strWebsite = authorizationPageBeanId.getStrWebsite();
    this.address = authorizationPageBeanId.getAddress();
    this.strFax = authorizationPageBeanId.getStrFax();
    this.strMPhone = authorizationPageBeanId.getStrMPhone();
    this.strPhone = authorizationPageBeanId.getStrPhone();
    this.strEMail = authorizationPageBeanId.getStrEMail();
    this.strCompany = authorizationPageBeanId.getStrCompany();
    this.strLastName = authorizationPageBeanId.getStrLastName();
    this.strFirstName = authorizationPageBeanId.getStrFirstName();
    this.strCPasswd = authorizationPageBeanId.getStrCPasswd();
    this.select_country=authorizationPageBeanId.getSelect_country();
    this.select_city=authorizationPageBeanId.getSelect_city();
  }

  
  
  
}
