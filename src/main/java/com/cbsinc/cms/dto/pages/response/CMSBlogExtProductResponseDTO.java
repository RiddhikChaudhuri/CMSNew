package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class CMSBlogExtProductResponseDTO extends PageModel {

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
  private String catalog_id;
  
  @JsonInclude(Include.NON_NULL)
  private String currency_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strSoftVersion;
  
  @JsonInclude(Include.NON_NULL)
  private String serial_nubmer;
  
  @JsonInclude(Include.NON_NULL)
  private String file_id;
  
  @JsonInclude(Include.NON_NULL)
  private String catalog_parent_id;
  
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
  private String select_small_image_url;
  
  
  
  public CMSBlogExtProductResponseDTO(PublisherBean publisherBeanId,AuthorizationPageBean authorizationPageBeanId) {
    this.bigimage_id = publisherBeanId.getBigimage_id();
    this.image_id = publisherBeanId.getImage_id();
    this.site_id = publisherBeanId.getSite_id();
    this.strMessage = authorizationPageBeanId.getStrMessage();
    this.policy_byproductid = String.valueOf(authorizationPageBeanId.getLastProductId());
    this.strSoftName = publisherBeanId.getStrSoftName();
    this.catalog_id=authorizationPageBeanId.getCatalog_id();
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
    this.strSoftVersion= publisherBeanId.getStrSoftVersion();
    this.catalog_parent_id=authorizationPageBeanId.getCatalog_parent_id();
    this.serial_nubmer=publisherBeanId.getSerial_nubmer();
    this.file_id=publisherBeanId.getFile_id();
    this.salelogic_id=publisherBeanId.getSalelogic_id();
    this.product_code_id=publisherBeanId.getProduct_code_id();
    this.amount1=publisherBeanId.getAmount1();
    this.amount2=publisherBeanId.getAmount2();
    this.amount3=publisherBeanId.getAmount3();
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
    this.select_small_image_url=publisherBeanId.getSelect_small_image_url();
    
  }
  
}
