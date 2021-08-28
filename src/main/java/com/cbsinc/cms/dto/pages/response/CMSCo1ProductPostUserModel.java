package com.cbsinc.cms.dto.pages.response;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.PublisherBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CMSCo1ProductPostUserModel extends PageModel {

  @JsonInclude(Include.NON_NULL)
  String catalog_parent_id;
  
  @JsonInclude(Include.NON_NULL)
  String type_id;
  
  @JsonInclude(Include.NON_NULL)
  int indx_select;
  
  @JsonInclude(Include.NON_NULL)
  int offset;
  
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
  private String soft_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strSoftName;

  @JsonInclude(Include.NON_NULL)
  private String catalog_id;
  
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
  private String image_id;
  
  @JsonInclude(Include.NON_NULL)
  private String portlettype_id;
  
  @JsonInclude(Include.NON_NULL)
  private String filename;
  
  @JsonInclude(Include.NON_NULL)
  private String bigimgname;
  
  @JsonInclude(Include.NON_NULL)
  private String bigimage_id;
  
  @JsonInclude(Include.NON_NULL)
  private String progname_id;
  
  @JsonInclude(Include.NON_NULL)
  private String user_id;
  
  @JsonInclude(Include.NON_NULL)
  private String strMessage;
  
  @JsonInclude(Include.NON_NULL)
  private String nameOfPage;
  
  @JsonInclude(Include.NON_NULL)
  private String action;
  
  @JsonInclude(Include.NON_NULL)
  private String name;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria10_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria1_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria2_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria3_id;

  @JsonInclude(Include.NON_NULL)
  private String select_creteria4_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria5_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria6_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria7_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria8_id;
  
  @JsonInclude(Include.NON_NULL)
  private String select_creteria9_id;

  
  
  public CMSCo1ProductPostUserModel(PublisherBean publisherBeanId, CatalogAddBean catalogAddBeanId,
      CatalogEditBean catalogEditBeanI, CatalogListBean catalogListBeanId,
      AuthorizationPageBean authorizationPageBeanId) {
    this.catalog_parent_id = authorizationPageBeanId.getCatalog_parent_id();
    this.type_id = publisherBeanId.getType_id();
    this.indx_select = catalogListBeanId.getIndx_select();
    this.offset = catalogListBeanId.getOffset();
    this.creteria1_id = publisherBeanId.getCreteria1_id();
    this.creteria2_id = publisherBeanId.getCreteria2_id();
    this.creteria3_id = publisherBeanId.getCreteria3_id();
    this.creteria4_id = publisherBeanId.getCreteria4_id();
    this.creteria5_id = publisherBeanId.getCreteria5_id();
    this.creteria6_id = publisherBeanId.getCreteria6_id();
    this.creteria7_id = publisherBeanId.getCreteria7_id();
    this.creteria8_id = publisherBeanId.getCreteria8_id();
    this.creteria9_id = publisherBeanId.getCreteria9_id();
    this.creteria10_id = publisherBeanId.getCreteria10_id();
    this.soft_id = publisherBeanId.getSoft_id();
    this.strSoftName = publisherBeanId.getStrSoftName();
    this.catalog_id = authorizationPageBeanId.getCatalog_id();
    this.strSoftCost = publisherBeanId.getStrSoftCost();
    this.strCurrency = publisherBeanId.getStrCurrency();
    this.strSoftDescription = publisherBeanId.getStrSoftDescription();
    this.product_fulldescription = publisherBeanId.getProduct_fulldescription();
    this.action = publisherBeanId.getAction();
    if (publisherBeanId.getAction().compareTo("add") == 0) {
      this.name = catalogAddBeanId.getName();
    } else if (publisherBeanId.getAction().compareTo("edit") == 0) {
      this.indx_select = catalogEditBeanI.getIndx_select();
      this.name = catalogEditBeanI.getName();
    }
    this.select_creteria1_id = publisherBeanId.getSelect_creteria1_id();
    this.select_creteria2_id = publisherBeanId.getSelect_creteria2_id();
    this.select_creteria3_id = publisherBeanId.getSelect_creteria3_id();
    this.select_creteria4_id = publisherBeanId.getSelect_creteria4_id();
    this.select_creteria5_id = publisherBeanId.getSelect_creteria5_id();
    this.select_creteria6_id = publisherBeanId.getSelect_creteria6_id();
    this.select_creteria7_id = publisherBeanId.getSelect_creteria7_id();
    this.select_creteria8_id = publisherBeanId.getSelect_creteria8_id();
    this.select_creteria9_id = publisherBeanId.getSelect_creteria9_id();
    this.select_creteria10_id = publisherBeanId.getSelect_creteria10_id();
    this.imgname = publisherBeanId.getImgname();
    this.image_id = publisherBeanId.getImage_id();
    this.portlettype_id = publisherBeanId.getPortlettype_id();
    this.filename = publisherBeanId.getFilename();
    this.bigimgname = publisherBeanId.getBigimgname();
    this.bigimage_id = publisherBeanId.getBigimage_id();
    this.progname_id = publisherBeanId.getProgname_id();
    this.user_id = publisherBeanId.getUser_id();
    this.strMessage = authorizationPageBeanId.getStrMessage();
    this.nameOfPage = publisherBeanId.getNameOfPage();
  }

}
