package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CMSExtProductPageModel extends PageModel {

  @JsonInclude(Include.NON_NULL)
  private String bigimage_id;
  
  @JsonInclude(Include.NON_NULL)
  private String image_id;
  
  @JsonInclude(Include.NON_NULL)
  private String site_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strMessage;
  
  @JsonInclude(Include.NON_NULL)
  private String policy_byproductid;
  
  @JsonInclude(Include.NON_NULL)
  private String strSoftName;
  
  @JsonInclude(Include.NON_NULL)
  private String type_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strSoftCost;
  
  @JsonInclude(Include.NON_NULL)
  private String strCurrency;
  
  @JsonInclude(Include.NON_NULL)
  private String strSoftDescription;
  
  @JsonInclude(Include.NON_NULL)
  private String product_fulldescription;
  
  @JsonInclude(Include.NON_NULL)
  private String imgname;
  
  @JsonInclude(Include.NON_NULL)
  private String portlettype_id;
  
  @JsonInclude(Include.NON_NULL)
  private String newValue;
  
  @JsonInclude(Include.NON_NULL)
  private String bigimgname;
  
  @JsonInclude(Include.NON_NULL)
  private String progname_id;
  
  @JsonInclude(Include.NON_NULL)
  private String user_id;
  
  @JsonInclude(Include.NON_NULL)
  private String nameOfPage; 
  
  @JsonInclude(Include.NON_NULL)
  private String creteria1_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria2_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria3_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria4_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria5_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria6_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria7_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria8_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria9_id;
  
  @JsonInclude(Include.NON_NULL)
  private String creteria10_id;
  
  @JsonInclude(Include.NON_NULL)
  private String catalog_parent_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strSearch2;
  
  @JsonInclude(Include.NON_NULL)
  private String strSoftName2;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_ratimg1;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_ratimg2;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_ratimg3;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_forum;
  
  @JsonInclude(Include.NON_NULL)
  private String jsp_url;
  
  @JsonInclude(Include.NON_NULL)
  private String filename;
  
  @JsonInclude(Include.NON_NULL)
  private String parent_portlettype_id;

  
  @JsonInclude(Include.NON_NULL)
  private String strSoftVersion;
  
  @JsonInclude(Include.NON_NULL)
  private String serial_nubmer;
  
  @JsonInclude(Include.NON_NULL)
  private String file_id;
 
  
  @JsonInclude(Include.NON_NULL)
  private String salelogic_id;
  
  @JsonInclude(Include.NON_NULL)
  private String product_code_id;
  
  @JsonInclude(Include.NON_NULL)
  private String amount1;
  
  @JsonInclude(Include.NON_NULL)
  private String amount2;
  
  @JsonInclude(Include.NON_NULL)
  private String amount3;

  public String getBigimage_id() {
    return bigimage_id;
  }


  public void setBigimage_id(String bigimage_id) {
    this.bigimage_id = bigimage_id;
  }


  public String getImage_id() {
    return image_id;
  }


  public void setImage_id(String image_id) {
    this.image_id = image_id;
  }


  public String getSite_id() {
    return site_id;
  }


  public void setSite_id(String site_id) {
    this.site_id = site_id;
  }


  public String getStrMessage() {
    return strMessage;
  }


  public void setStrMessage(String strMessage) {
    this.strMessage = strMessage;
  }


  public String getPolicy_byproductid() {
    return policy_byproductid;
  }


  public void setPolicy_byproductid(String policy_byproductid) {
    this.policy_byproductid = policy_byproductid;
  }


  public String getStrSoftName() {
    return strSoftName;
  }


  public void setStrSoftName(String strSoftName) {
    this.strSoftName = strSoftName;
  }


  public String getType_id() {
    return type_id;
  }


  public void setType_id(String type_id) {
    this.type_id = type_id;
  }


  public String getStrSoftCost() {
    return strSoftCost;
  }


  public void setStrSoftCost(String strSoftCost) {
    this.strSoftCost = strSoftCost;
  }


  public String getStrCurrency() {
    return strCurrency;
  }


  public void setStrCurrency(String strCurrency) {
    this.strCurrency = strCurrency;
  }


  public String getStrSoftDescription() {
    return strSoftDescription;
  }


  public void setStrSoftDescription(String strSoftDescription) {
    this.strSoftDescription = strSoftDescription;
  }


  public String getProduct_fulldescription() {
    return product_fulldescription;
  }


  public void setProduct_fulldescription(String product_fulldescription) {
    this.product_fulldescription = product_fulldescription;
  }


  public String getImgname() {
    return imgname;
  }


  public void setImgname(String imgname) {
    this.imgname = imgname;
  }


  public String getPortlettype_id() {
    return portlettype_id;
  }


  public void setPortlettype_id(String portlettype_id) {
    this.portlettype_id = portlettype_id;
  }


  public String getNewValue() {
    return newValue;
  }


  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }


  public String getBigimgname() {
    return bigimgname;
  }


  public void setBigimgname(String bigimgname) {
    this.bigimgname = bigimgname;
  }


  public String getProgname_id() {
    return progname_id;
  }


  public void setProgname_id(String progname_id) {
    this.progname_id = progname_id;
  }


  public String getUser_id() {
    return user_id;
  }


  public void setUser_id(String user_id) {
    this.user_id = user_id;
  }


  public String getNameOfPage() {
    return nameOfPage;
  }


  public void setNameOfPage(String nameOfPage) {
    this.nameOfPage = nameOfPage;
  }
  
  public CMSExtProductPageModel(PublisherBean publisherBeanId,AuthorizationPageBean authorizationPageBeanId) {
    this.bigimage_id = publisherBeanId.getBigimage_id();
    this.image_id = publisherBeanId.getImage_id();
    this.site_id = publisherBeanId.getSite_id();
    this.strMessage = authorizationPageBeanId.getStrMessage();
    this.policy_byproductid = String.valueOf(authorizationPageBeanId.getLastProductId());
    this.strSoftName = publisherBeanId.getStrSoftName();
    this.type_id = publisherBeanId.getType_id();
    this.strSoftCost = publisherBeanId.getStrSoftCost();
    this.strCurrency = publisherBeanId.getStrCurrency();
    this.strSoftDescription = publisherBeanId.getStrSoftDescription();
    this.product_fulldescription = publisherBeanId.getProduct_fulldescription();
    this.imgname = publisherBeanId.getImgname();
    this.portlettype_id = publisherBeanId.getPortlettype_id();
    this.newValue = publisherBeanId.getSample();
    this.bigimgname = publisherBeanId.getBigimgname();
    this.progname_id = publisherBeanId.getProgname_id();
    this.user_id = publisherBeanId.getUser_id();
    this.nameOfPage = publisherBeanId.getNameOfPage();
    this.creteria1_id=publisherBeanId.getCreteria1_id();
    this.creteria2_id=publisherBeanId.getCreteria2_id();
    this.creteria3_id=publisherBeanId.getCreteria3_id();
    this.creteria4_id=publisherBeanId.getCreteria4_id();
    this.creteria5_id=publisherBeanId.getCreteria5_id();
    this.creteria6_id=publisherBeanId.getCreteria6_id();
    this.creteria7_id=publisherBeanId.getCreteria7_id();
    this.creteria8_id=publisherBeanId.getCreteria8_id();
    this.creteria9_id=publisherBeanId.getCreteria9_id();
    this.creteria10_id=publisherBeanId.getCreteria10_id();
    this.strSearch2=publisherBeanId.getStrSearch2();
    this.strShow_ratimg1=publisherBeanId.getStrShow_ratimg1();
    this.strShow_ratimg2=publisherBeanId.getStrShow_ratimg2();
    this.strShow_ratimg3=publisherBeanId.getStrShow_ratimg3();
    this.strShow_forum=publisherBeanId.getStrShow_forum();
    this.jsp_url=publisherBeanId.getJsp_url();
    this.filename = publisherBeanId.getFilename();
    this.parent_portlettype_id= publisherBeanId.getParent_portlettype_id();
  }
  
  
  

}
