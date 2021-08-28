package com.cbsinc.cms.dto.pages.response;

import java.math.BigDecimal;
import java.util.List;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.SearchBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CMSSearchPageModel extends PageModel {

  @JsonInclude(Include.NON_ABSENT)
  private Boolean isInternet;
  
  @JsonInclude(Include.NON_ABSENT)
  private int offset;

  @JsonInclude(Include.NON_ABSENT)
  private long intLevelUp;

  @JsonInclude(Include.NON_ABSENT)
  private String dialog;

  @JsonInclude(Include.NON_ABSENT)
  private String advancedSearchOpen;

  @JsonInclude(Include.NON_ABSENT)
  private String forumOpen;

  @JsonInclude(Include.NON_ABSENT)
  private String action;

  @JsonInclude(Include.NON_ABSENT)
  private String searchValueArg;

  @JsonInclude(Include.NON_ABSENT)
  private int searchquery;

  @JsonInclude(Include.NON_ABSENT)
  private String query_productlist;

  @JsonInclude(Include.NON_ABSENT)
  private String allFoundProducts;

  @JsonInclude(Include.NON_ABSENT)
  private  List Adp;

  @JsonInclude(Include.NON_ABSENT)
  private List co1Adp;

  @JsonInclude(Include.NON_ABSENT)
  private List co2Adp;

  @JsonInclude(Include.NON_ABSENT)
  private String select_currency_cd;
  @JsonInclude(Include.NON_ABSENT)
  private long offsetLastPage;
  
  @JsonInclude(Include.NON_ABSENT)
  private String balance;
  
  @JsonInclude(Include.NON_ABSENT)
  private String catalog_id;

  
  @JsonInclude(Include.NON_ABSENT)
  private BigDecimal fromCost;

  
  @JsonInclude(Include.NON_ABSENT)
  private BigDecimal toCost;

  
  @JsonInclude(Include.NON_ABSENT)
  private String locale;

  
  @JsonInclude(Include.NON_ABSENT)
  private String strMessage;

  
  @JsonInclude(Include.NON_ABSENT)
  private String site_id;
  
  @JsonInclude(Include.NON_ABSENT)
  private String strPasswd;
  

  
  @JsonInclude(Include.NON_ABSENT)
  private String strLogin;
  
  public CMSSearchPageModel(SearchBean searchBeanId,AuthorizationPageBean authorizationPageBeanId) {
    this.isInternet=searchBeanId.isInternet;
    this.offset=searchBeanId.getOffset();
    this.intLevelUp=searchBeanId.getIntLevelUp();
    this.dialog=searchBeanId.getDialog();
    this.advancedSearchOpen=searchBeanId.getAdvancedSearchOpen();
    this.forumOpen=searchBeanId.getForumOpen();
    this.action=searchBeanId.getAction();
    this.searchValueArg=searchBeanId.getSearchValueArg();
    this.searchquery=searchBeanId.getSearchquery();
    this.query_productlist=searchBeanId.getQuery_productlist();
    this.allFoundProducts=searchBeanId.getAllFoundProducts();
    this.Adp=searchBeanId.Adp;
    this.co1Adp=searchBeanId.co1Adp;
    this.co2Adp=searchBeanId.co2Adp;
    this.select_currency_cd=searchBeanId.getSelect_currency_cd();
    this.offsetLastPage=authorizationPageBeanId.getOffsetLastPage();
    this.balance=authorizationPageBeanId.getBalance();
    this.catalog_id=authorizationPageBeanId.getCatalog_id();
    this.fromCost=authorizationPageBeanId.getFromCost();
    this.toCost=authorizationPageBeanId.getToCost();
    this.locale=authorizationPageBeanId.getLocale();
    this.strMessage=authorizationPageBeanId.getStrMessage();
    this.site_id=authorizationPageBeanId.getSite_id();
    this.strPasswd=authorizationPageBeanId.getStrPasswd();
    this.strLogin=authorizationPageBeanId.getStrLogin();
  }
}
