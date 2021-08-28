package com.cbsinc.cms.dto.pages.response;

import java.util.List;
import com.cbsinc.cms.publisher.models.ItemDescriptionBean;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CMSPolicyPageModel extends PageModel {
  @JsonInclude(Include.NON_NULL)
  private String product_id;
  
  @JsonInclude(Include.NON_NULL)
  private  Boolean internet;
  @JsonInclude(Include.NON_NULL)
  private String file_exist;
  @JsonInclude(Include.NON_NULL)
  private Long portlettype_id;
  
  @JsonInclude(Include.NON_NULL)
  private String  parent_product_id;
  
  @JsonInclude(Include.NON_NULL)
  private String productName;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_forum;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_ratimg1;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_ratimg2;
  
  @JsonInclude(Include.NON_NULL)
  private String strShow_ratimg3;
  
  @JsonInclude(Include.NON_NULL)
  private String jsp_url;
  
  @JsonInclude(Include.NON_NULL)
  private String productURL;
  
  @JsonInclude(Include.NON_NULL)
  private String img_url;
  
  @JsonInclude(Include.NON_NULL)
  private String productDescription;
  
  @JsonInclude(Include.NON_NULL)
  private String bigimgURL;
  
  @JsonInclude(Include.NON_NULL)
  private String productVersion;
  
  @JsonInclude(Include.NON_NULL)
  private String productCost;
  
  @JsonInclude(Include.NON_NULL)
  private String strCDate;
  
  @JsonInclude(Include.NON_NULL)
  private String statistic;
  
  @JsonInclude(Include.NON_NULL)
  private String creator_info_user_id;
  
  @JsonInclude(Include.NON_NULL)
  private String currency_cd;
  
  @JsonInclude(Include.NON_NULL)
  private String  currency_desc;
  
  @JsonInclude(Include.NON_NULL)
  private String back_url;
  
  @JsonInclude(Include.NON_NULL)
  private String type_page;
  
  @JsonInclude(Include.NON_NULL)
  private long intUserID;
  
  @JsonInclude(Include.NON_NULL)
  private String rating1_xml;
  
  @JsonInclude(Include.NON_NULL)
  private String balans;
  
  @JsonInclude(Include.NON_NULL)
  private boolean fistOpen;
  
  @JsonInclude(Include.NON_NULL)
  private String catalogXMLUrlPath;
  
  @JsonInclude(Include.NON_NULL)
  private List ext1Adp;
  
  @JsonInclude(Include.NON_NULL)
  private List ext2Adp;
  
  @JsonInclude(Include.NON_NULL)
  private String select_currencies;
  
  @JsonInclude(Include.NON_NULL)
  private String select_tree_catalog;
  
  @JsonInclude(Include.NON_NULL)
  private List extFilesAdp;
  
  @JsonInclude(Include.NON_NULL)
  private List extTabsAdp ;
  
  @JsonInclude(Include.NON_NULL)
  private  List blogExtAdp;
  
  @JsonInclude(Include.NON_NULL)
  private List newsAdp;
  
  @JsonInclude(Include.NON_NULL)
  private List bottomAdp;
  
  @JsonInclude(Include.NON_NULL)
  private String select_menu_catalog;
 

  @JsonCreator
  public CMSPolicyPageModel(ItemDescriptionBean itemDescriptionBeanId) {
    this.product_id = itemDescriptionBeanId.getProduct_id();
    this.internet=itemDescriptionBeanId.internet;
    this.file_exist=itemDescriptionBeanId.getFile_exist();
    this.portlettype_id=itemDescriptionBeanId.getPortlettype_id();
    this.parent_product_id=itemDescriptionBeanId.getParent_product_id();
    this.strShow_forum=itemDescriptionBeanId.getStrShow_forum();
    this.strShow_ratimg1=itemDescriptionBeanId.getStrShow_ratimg1();
    this.strShow_ratimg2=itemDescriptionBeanId.getStrShow_ratimg2();
    this.strShow_ratimg3=itemDescriptionBeanId.getStrShow_ratimg3();
    this.jsp_url=itemDescriptionBeanId.getJsp_url();
    this.productURL=itemDescriptionBeanId.getProductURL();
    this.img_url=itemDescriptionBeanId.getImg_url();
    this.productDescription=itemDescriptionBeanId.getProductDescription();
    this.bigimgURL=itemDescriptionBeanId.getBigimgURL();
    this.productVersion=itemDescriptionBeanId.getProductVersion();
    this.productCost=itemDescriptionBeanId.getProductCost();
    this.strCDate=itemDescriptionBeanId.getStrCDate();
    this.statistic=itemDescriptionBeanId.getStatistic();
    this.creator_info_user_id=itemDescriptionBeanId.getCreator_info_user_id();
    this.currency_cd=itemDescriptionBeanId.getCurrency_cd();
    this.currency_desc=itemDescriptionBeanId.getCurrency_desc();
    this.back_url=itemDescriptionBeanId.getBack_url();
    this.type_page=itemDescriptionBeanId.getType_page();
    this.intUserID=itemDescriptionBeanId.getIntUserID();
    this.rating1_xml=itemDescriptionBeanId.getRating1_xml();
    this.balans=itemDescriptionBeanId.getBalans();
    this.fistOpen=itemDescriptionBeanId.fistOpen;
    this.catalogXMLUrlPath=itemDescriptionBeanId.getSelectCatalogXMLUrlPath();
    this.ext1Adp=itemDescriptionBeanId.ext1Adp;
    this.ext2Adp=itemDescriptionBeanId.ext2Adp;
    this.select_currencies=itemDescriptionBeanId.getSelect_currencies();
    this.select_tree_catalog=itemDescriptionBeanId.getSelect_tree_catalog();
    this.extFilesAdp=itemDescriptionBeanId.extFilesAdp;
    this.extTabsAdp=itemDescriptionBeanId.extTabsAdp;
    this.blogExtAdp=itemDescriptionBeanId.blogExtAdp;
    this.newsAdp=itemDescriptionBeanId.newsAdp;
    this.bottomAdp=itemDescriptionBeanId.bottomAdp;
    this.select_menu_catalog=itemDescriptionBeanId.getSelect_menu_catalog();
  }
}

