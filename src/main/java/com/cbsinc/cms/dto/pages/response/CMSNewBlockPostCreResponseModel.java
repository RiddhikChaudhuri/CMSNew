package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.PublisherBean;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_ABSENT)
public class CMSNewBlockPostCreResponseModel extends PageModel {

  @JsonInclude(Include.NON_ABSENT)
  private String strShow_ratimg1_checked;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strShow_ratimg1;
  
  @JsonInclude(Include.NON_ABSENT)
  private String show_forum_checked;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strShow_forum;
  
  @JsonInclude(Include.NON_ABSENT)
  private String site_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria1_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria2_id; 
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria3_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria4_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria5_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria6_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria7_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria8_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria9_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String creteria10_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strSoftName;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strSoftName2;
  
  @JsonInclude(Include.NON_ABSENT)
  private String jsp_url;
  
  @JsonInclude(Include.NON_ABSENT)
  private String type_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strSoftCost;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strCurrency;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strSoftDescription;
  
  @JsonInclude(Include.NON_ABSENT)
  private String product_fulldescription;

  @JsonInclude(Include.NON_ABSENT)
  private String imgname;
  
  @JsonInclude(Include.NON_ABSENT)
  private String image_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String portlettype_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String file_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String filename;
  
  @JsonInclude(Include.NON_ABSENT)
  private String bigimgname;
  
  @JsonInclude(Include.NON_ABSENT)
  private String bigimage_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String progname_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String user_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria1_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria2_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria3_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria4_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria5_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria6_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria7_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria8_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria9_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String criteria10_label;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria1_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria2_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria3_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria4_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria5_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria6_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria7_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria8_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria9_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String select_creteria10_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String action;
  
  @JsonInclude(Include.NON_ABSENT)
  private String nameOfPage;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strMessage;
  
  @JsonInclude(Include.NON_ABSENT)
  private String catalog_parent_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String catalog_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String name;
  
  @JsonInclude(Include.NON_ABSENT)
  private int indx_select;
  
  public @JsonCreator CMSNewBlockPostCreResponseModel(PublisherBean publisherBeanId,AuthorizationPageBean authorizationPageBeanId,CatalogEditBean catalogEditBean,CatalogAddBean catalogAddBean) {
    this.strShow_ratimg1_checked=publisherBeanId.getStrShow_ratimg1_checked();
    this.strShow_ratimg1=publisherBeanId.getStrShow_ratimg1();
    this.show_forum_checked=publisherBeanId.getShow_forum_checked();
    this.strShow_forum=publisherBeanId.getStrShow_forum();
    this.site_id=publisherBeanId.getSite_id();
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
    this.jsp_url=publisherBeanId.getJsp_url();
    this.strSoftName=publisherBeanId.getStrSoftName();
    this.strSoftName2=publisherBeanId.getStrSoftName2();
    this.type_id=publisherBeanId.getType_id();
    this.strSoftCost=publisherBeanId.getStrSoftCost();
    this.strCurrency=publisherBeanId.getStrCurrency();
    this.strSoftDescription=publisherBeanId.getStrSoftDescription();
    this.product_fulldescription=publisherBeanId.getProduct_fulldescription();
    this.bigimgname=publisherBeanId.getBigimgname();
    this.imgname=publisherBeanId.getImgname();
    this.image_id=publisherBeanId.getImage_id();
    this.portlettype_id=publisherBeanId.getPortlettype_id();
    this.file_id=publisherBeanId.getFile_id();
    this.filename=publisherBeanId.getFilename();
    this.bigimage_id=publisherBeanId.getBigimage_id();
    this.progname_id=publisherBeanId.getProgname_id();
    this.user_id=publisherBeanId.getUser_id();
    this.criteria1_label=publisherBeanId.getCriteria1_label();
    this.criteria2_label=publisherBeanId.getCriteria2_label();
    this.criteria3_label=publisherBeanId.getCriteria3_label();
    this.criteria4_label=publisherBeanId.getCriteria4_label();
    this.criteria5_label=publisherBeanId.getCriteria5_label();
    this.criteria6_label=publisherBeanId.getCriteria6_label();
    this.criteria7_label=publisherBeanId.getCriteria7_label();
    this.criteria8_label=publisherBeanId.getCriteria8_label();
    this.criteria9_label=publisherBeanId.getCriteria9_label();
    this.criteria10_label=publisherBeanId.getCriteria10_label();
    this.select_creteria1_id=publisherBeanId.getSelect_creteria1_id();
    this.select_creteria2_id=publisherBeanId.getSelect_creteria2_id();
    this.select_creteria3_id=publisherBeanId.getSelect_creteria3_id();
    this.select_creteria4_id=publisherBeanId.getSelect_creteria4_id();
    this.select_creteria5_id=publisherBeanId.getSelect_creteria5_id();
    this.select_creteria6_id=publisherBeanId.getSelect_creteria6_id();
    this.select_creteria7_id=publisherBeanId.getSelect_creteria7_id();
    this.select_creteria8_id=publisherBeanId.getSelect_creteria8_id();
    this.select_creteria9_id=publisherBeanId.getSelect_creteria9_id();
    this.select_creteria10_id=publisherBeanId.getSelect_creteria10_id();
    this.action=publisherBeanId.getAction();
    this.nameOfPage=publisherBeanId.getNameOfPage();
    this.strMessage=authorizationPageBeanId.getStrMessage();
    this.catalog_parent_id=authorizationPageBeanId.getCatalog_parent_id();
    this.catalog_id=authorizationPageBeanId.getCatalog_id();
    
    if (publisherBeanId.getAction().compareTo("add") == 0) {
      this.name = catalogAddBean.getName();
    }else if (publisherBeanId.getAction().compareTo("edit") == 0) {
      this.name = catalogEditBean.getName();
      this.indx_select=catalogEditBean.getIndx_select();
    }
  }
  
  
  

}
