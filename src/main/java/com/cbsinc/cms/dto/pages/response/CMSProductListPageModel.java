package com.cbsinc.cms.dto.pages.response;

import java.math.BigDecimal;
import java.util.List;

import com.cbsinc.cms.publisher.dao.RowMappers.XmlDBCriteriaLocale;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.ProductlistBean;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class CMSProductListPageModel {

	@JsonInclude(Include.NON_ABSENT)
	private String balance;

	@JsonInclude(Include.NON_ABSENT)
	private long offsetLastPage;

	@JsonInclude(Include.NON_ABSENT)
	private String catalog_id;

	@JsonInclude(Include.NON_ABSENT)
	private String catalog_parent_id;

	@JsonInclude(Include.NON_ABSENT)
	private Integer dayfrom_id;

	@JsonInclude(Include.NON_ABSENT)
	private Integer mountfrom_id;

	@JsonInclude(Include.NON_ABSENT)
	private Integer yearfrom_id;

	@JsonInclude(Include.NON_ABSENT)
	private BigDecimal fromCost;

	@JsonInclude(Include.NON_ABSENT)
	private BigDecimal toCost;

	@JsonInclude(Include.NON_ABSENT)
	private Integer dayto_id;

	@JsonInclude(Include.NON_ABSENT)
	private Integer mountto_id;

	@JsonInclude(Include.NON_ABSENT)
	private Integer yearto_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria1_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria2_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria3_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria4_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria5_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria6_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria7_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria8_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria9_id;

	@JsonInclude(Include.NON_ABSENT)
	private long creteria10_id;

	@JsonInclude(Include.NON_ABSENT)
	private String locale;

	@JsonInclude(Include.NON_ABSENT)
	private String strMessage;

	@JsonInclude(Include.NON_ABSENT)
	private String user_site;

	@JsonInclude(Include.NON_ABSENT)
	private String strLogin;

	@JsonInclude(Include.NON_ABSENT)
	private String strPasswd;

	@JsonInclude(Include.NON_ABSENT)
	private String site_id;

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
	private List Adp;

	@JsonInclude(Include.NON_ABSENT)
	private List co1Adp;

	@JsonInclude(Include.NON_ABSENT)
	private List co2Adp;

	@JsonInclude(Include.NON_ABSENT)
	private List newsAdp;

	@JsonInclude(Include.NON_ABSENT)
	private List blogExtAdp;

	@JsonInclude(Include.NON_ABSENT)
	private List bottomAdp;

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
	private String select_currency_cd;

	@JsonInclude(Include.NON_ABSENT)
	private String select_tree_catalog;

	@JsonInclude(Include.NON_ABSENT)
	private String select_menu_catalog;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria1_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria2_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria3_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria4_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria5_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria6_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria7_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria8_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria9_id;

	@JsonInclude(Include.NON_ABSENT)
	private List<XmlDBCriteriaLocale> select_creteria10_id;

	@JsonInclude(Include.NON_ABSENT)
	private String select_dayfrom_id;

	@JsonInclude(Include.NON_ABSENT)
	private String select_mountfrom_id;

	@JsonInclude(Include.NON_ABSENT)
	private String select_yearfrom_id;

	@JsonInclude(Include.NON_ABSENT)
	private String select_dayto_id;

	@JsonInclude(Include.NON_ABSENT)
	private String select_mountto_id;

	@JsonInclude(Include.NON_ABSENT)
	private String select_yearto_id;

	public CMSProductListPageModel(AuthorizationPageBean authorizationPageBeanId, ProductlistBean productlistBeanId) {
		this.balance = authorizationPageBeanId.getBalance();
		this.offsetLastPage = authorizationPageBeanId.getOffsetLastPage();
		this.catalog_id = authorizationPageBeanId.getCatalog_id();
		this.catalog_parent_id = authorizationPageBeanId.getCatalog_parent_id();
		this.dayfrom_id = authorizationPageBeanId.getDayfrom_id();
		this.mountfrom_id = authorizationPageBeanId.getMountfrom_id();
		this.yearfrom_id = authorizationPageBeanId.getYearfrom_id();
		this.fromCost = authorizationPageBeanId.getFromCost();
		this.toCost = authorizationPageBeanId.getToCost();
		this.dayto_id = authorizationPageBeanId.getDayto_id();
		this.mountto_id = authorizationPageBeanId.getMountto_id();
		this.yearto_id = authorizationPageBeanId.getYearto_id();
		this.creteria1_id = authorizationPageBeanId.getCreteria1_id();
		this.creteria2_id = authorizationPageBeanId.getCreteria2_id();
		this.creteria3_id = authorizationPageBeanId.getCreteria3_id();
		this.creteria4_id = authorizationPageBeanId.getCreteria4_id();
		this.creteria5_id = authorizationPageBeanId.getCreteria5_id();
		this.creteria6_id = authorizationPageBeanId.getCreteria6_id();
		this.creteria7_id = authorizationPageBeanId.getCreteria7_id();
		this.creteria8_id = authorizationPageBeanId.getCreteria8_id();
		this.creteria9_id = authorizationPageBeanId.getCreteria9_id();
		this.creteria10_id = authorizationPageBeanId.getCreteria10_id();
		this.locale = authorizationPageBeanId.getLocale();
		this.strMessage = authorizationPageBeanId.getStrMessage();
		this.user_site = authorizationPageBeanId.getUser_site();
		this.strLogin = authorizationPageBeanId.getStrLogin();
		this.strPasswd = authorizationPageBeanId.getStrPasswd();
		this.site_id = authorizationPageBeanId.getSite_id();

		this.dialog = productlistBeanId.getDialog();
		this.advancedSearchOpen = productlistBeanId.getAdvancedSearchOpen();
		this.forumOpen = productlistBeanId.getForumOpen();
		this.isInternet = productlistBeanId.isInternet;
		this.offset = productlistBeanId.getOffset();
		this.intLevelUp = productlistBeanId.onggetIntLevelUp();
		this.action = productlistBeanId.getAction();
		this.searchValueArg = productlistBeanId.getSearchValueArg();
		this.searchquery = productlistBeanId.getSearchquery();
		this.Adp = productlistBeanId.Adp;
		this.blogExtAdp = productlistBeanId.blogExtAdp;
		this.bottomAdp = productlistBeanId.bottomAdp;
		this.co1Adp = productlistBeanId.co1Adp;
		this.co2Adp = productlistBeanId.co2Adp;
		this.newsAdp = productlistBeanId.newsAdp;
		this.criteria1_label = productlistBeanId.getCriteria1_label();
		this.criteria2_label = productlistBeanId.getCriteria2_label();
		this.criteria3_label = productlistBeanId.getCriteria3_label();
		this.criteria4_label = productlistBeanId.getCriteria4_label();
		this.criteria5_label = productlistBeanId.getCriteria5_label();
		this.criteria6_label = productlistBeanId.getCriteria6_label();
		this.criteria7_label = productlistBeanId.getCriteria7_label();
		this.criteria8_label = productlistBeanId.getCriteria8_label();
		this.criteria9_label = productlistBeanId.getCriteria9_label();
		this.criteria10_label = productlistBeanId.getCriteria10_label();
		this.select_currency_cd = productlistBeanId.getSelect_currency_cd();
		this.select_tree_catalog = productlistBeanId.getSelect_tree_catalog();
		this.select_menu_catalog = productlistBeanId.getSelect_menu_catalog();
		this.select_creteria1_id = productlistBeanId.getSelect_creteria1_id();
		this.select_creteria2_id = productlistBeanId.getSelect_creteria2_id();
		this.select_creteria3_id = productlistBeanId.getSelect_creteria3_id();
		this.select_creteria4_id = productlistBeanId.getSelect_creteria4_id();
		this.select_creteria5_id = productlistBeanId.getSelect_creteria5_id();
		this.select_creteria6_id = productlistBeanId.getSelect_creteria6_id();
		this.select_creteria7_id = productlistBeanId.getSelect_creteria7_id();
		this.select_creteria8_id = productlistBeanId.getSelect_creteria8_id();
		this.select_creteria9_id = productlistBeanId.getSelect_creteria9_id();
		this.select_creteria10_id = productlistBeanId.getSelect_creteria10_id();
		this.select_dayfrom_id = productlistBeanId.getSelect_dayfrom_id();
		this.select_dayto_id = productlistBeanId.getSelect_dayto_id();
		this.select_mountfrom_id = productlistBeanId.getSelect_mountfrom_id();
		this.select_mountto_id = productlistBeanId.getSelect_mountto_id();
		this.select_yearfrom_id = productlistBeanId.getSelect_yearfrom_id();
		this.select_yearto_id = productlistBeanId.getSelect_yearto_id();
	}

	public String getBalance() {
		return balance;
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public long getOffsetLastPage() {
		return offsetLastPage;
	}

	public void setOffsetLastPage(long offsetLastPage) {
		this.offsetLastPage = offsetLastPage;
	}

	public String getCatalog_id() {
		return catalog_id;
	}

	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}

	public String getCatalog_parent_id() {
		return catalog_parent_id;
	}

	public void setCatalog_parent_id(String catalog_parent_id) {
		this.catalog_parent_id = catalog_parent_id;
	}

	public Integer getDayfrom_id() {
		return dayfrom_id;
	}

	public void setDayfrom_id(Integer dayfrom_id) {
		this.dayfrom_id = dayfrom_id;
	}

	public Integer getMountfrom_id() {
		return mountfrom_id;
	}

	public void setMountfrom_id(Integer mountfrom_id) {
		this.mountfrom_id = mountfrom_id;
	}

	public Integer getYearfrom_id() {
		return yearfrom_id;
	}

	public void setYearfrom_id(Integer yearfrom_id) {
		this.yearfrom_id = yearfrom_id;
	}

	public BigDecimal getFromCost() {
		return fromCost;
	}

	public void setFromCost(BigDecimal fromCost) {
		this.fromCost = fromCost;
	}

	public BigDecimal getToCost() {
		return toCost;
	}

	public void setToCost(BigDecimal toCost) {
		this.toCost = toCost;
	}

	public Integer getDayto_id() {
		return dayto_id;
	}

	public void setDayto_id(Integer dayto_id) {
		this.dayto_id = dayto_id;
	}

	public Integer getMountto_id() {
		return mountto_id;
	}

	public void setMountto_id(Integer mountto_id) {
		this.mountto_id = mountto_id;
	}

	public Integer getYearto_id() {
		return yearto_id;
	}

	public void setYearto_id(Integer yearto_id) {
		this.yearto_id = yearto_id;
	}

	public long getCreteria1_id() {
		return creteria1_id;
	}

	public void setCreteria1_id(long creteria1_id) {
		this.creteria1_id = creteria1_id;
	}

	public long getCreteria2_id() {
		return creteria2_id;
	}

	public void setCreteria2_id(long creteria2_id) {
		this.creteria2_id = creteria2_id;
	}

	public long getCreteria3_id() {
		return creteria3_id;
	}

	public void setCreteria3_id(long creteria3_id) {
		this.creteria3_id = creteria3_id;
	}

	public long getCreteria4_id() {
		return creteria4_id;
	}

	public void setCreteria4_id(long creteria4_id) {
		this.creteria4_id = creteria4_id;
	}

	public long getCreteria5_id() {
		return creteria5_id;
	}

	public void setCreteria5_id(long creteria5_id) {
		this.creteria5_id = creteria5_id;
	}

	public long getCreteria6_id() {
		return creteria6_id;
	}

	public void setCreteria6_id(long creteria6_id) {
		this.creteria6_id = creteria6_id;
	}

	public long getCreteria7_id() {
		return creteria7_id;
	}

	public void setCreteria7_id(long creteria7_id) {
		this.creteria7_id = creteria7_id;
	}

	public long getCreteria8_id() {
		return creteria8_id;
	}

	public void setCreteria8_id(long creteria8_id) {
		this.creteria8_id = creteria8_id;
	}

	public long getCreteria9_id() {
		return creteria9_id;
	}

	public void setCreteria9_id(long creteria9_id) {
		this.creteria9_id = creteria9_id;
	}

	public long getCreteria10_id() {
		return creteria10_id;
	}

	public void setCreteria10_id(long creteria10_id) {
		this.creteria10_id = creteria10_id;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public String getStrMessage() {
		return strMessage;
	}

	public void setStrMessage(String strMessage) {
		this.strMessage = strMessage;
	}

	public String getUser_site() {
		return user_site;
	}

	public void setUser_site(String user_site) {
		this.user_site = user_site;
	}

	public String getStrLogin() {
		return strLogin;
	}

	public void setStrLogin(String strLogin) {
		this.strLogin = strLogin;
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

	public Boolean getIsInternet() {
		return isInternet;
	}

	public void setIsInternet(Boolean isInternet) {
		this.isInternet = isInternet;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public long getIntLevelUp() {
		return intLevelUp;
	}

	public void setIntLevelUp(long intLevelUp) {
		this.intLevelUp = intLevelUp;
	}

	public String getDialog() {
		return dialog;
	}

	public void setDialog(String dialog) {
		this.dialog = dialog;
	}

	public String getAdvancedSearchOpen() {
		return advancedSearchOpen;
	}

	public void setAdvancedSearchOpen(String advancedSearchOpen) {
		this.advancedSearchOpen = advancedSearchOpen;
	}

	public String getForumOpen() {
		return forumOpen;
	}

	public void setForumOpen(String forumOpen) {
		this.forumOpen = forumOpen;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getSearchValueArg() {
		return searchValueArg;
	}

	public void setSearchValueArg(String searchValueArg) {
		this.searchValueArg = searchValueArg;
	}

	public int getSearchquery() {
		return searchquery;
	}

	public void setSearchquery(int searchquery) {
		this.searchquery = searchquery;
	}

	public List getAdp() {
		return Adp;
	}

	public void setAdp(List adp) {
		Adp = adp;
	}

	public List getCo1Adp() {
		return co1Adp;
	}

	public void setCo1Adp(List co1Adp) {
		this.co1Adp = co1Adp;
	}

	public List getCo2Adp() {
		return co2Adp;
	}

	public void setCo2Adp(List co2Adp) {
		this.co2Adp = co2Adp;
	}

	public List getNewsAdp() {
		return newsAdp;
	}

	public void setNewsAdp(List newsAdp) {
		this.newsAdp = newsAdp;
	}

	public List getBlogExtAdp() {
		return blogExtAdp;
	}

	public void setBlogExtAdp(List blogExtAdp) {
		this.blogExtAdp = blogExtAdp;
	}

	public List getBottomAdp() {
		return bottomAdp;
	}

	public void setBottomAdp(List bottomAdp) {
		this.bottomAdp = bottomAdp;
	}

	public String getCriteria1_label() {
		return criteria1_label;
	}

	public void setCriteria1_label(String criteria1_label) {
		this.criteria1_label = criteria1_label;
	}

	public String getCriteria2_label() {
		return criteria2_label;
	}

	public void setCriteria2_label(String criteria2_label) {
		this.criteria2_label = criteria2_label;
	}

	public String getCriteria3_label() {
		return criteria3_label;
	}

	public void setCriteria3_label(String criteria3_label) {
		this.criteria3_label = criteria3_label;
	}

	public String getCriteria4_label() {
		return criteria4_label;
	}

	public void setCriteria4_label(String criteria4_label) {
		this.criteria4_label = criteria4_label;
	}

	public String getCriteria5_label() {
		return criteria5_label;
	}

	public void setCriteria5_label(String criteria5_label) {
		this.criteria5_label = criteria5_label;
	}

	public String getCriteria6_label() {
		return criteria6_label;
	}

	public void setCriteria6_label(String criteria6_label) {
		this.criteria6_label = criteria6_label;
	}

	public String getCriteria7_label() {
		return criteria7_label;
	}

	public void setCriteria7_label(String criteria7_label) {
		this.criteria7_label = criteria7_label;
	}

	public String getCriteria8_label() {
		return criteria8_label;
	}

	public void setCriteria8_label(String criteria8_label) {
		this.criteria8_label = criteria8_label;
	}

	public String getCriteria9_label() {
		return criteria9_label;
	}

	public void setCriteria9_label(String criteria9_label) {
		this.criteria9_label = criteria9_label;
	}

	public String getCriteria10_label() {
		return criteria10_label;
	}

	public void setCriteria10_label(String criteria10_label) {
		this.criteria10_label = criteria10_label;
	}

	public String getSelect_currency_cd() {
		return select_currency_cd;
	}

	public void setSelect_currency_cd(String select_currency_cd) {
		this.select_currency_cd = select_currency_cd;
	}

	public String getSelect_tree_catalog() {
		return select_tree_catalog;
	}

	public void setSelect_tree_catalog(String select_tree_catalog) {
		this.select_tree_catalog = select_tree_catalog;
	}

	public String getSelect_menu_catalog() {
		return select_menu_catalog;
	}

	public void setSelect_menu_catalog(String select_menu_catalog) {
		this.select_menu_catalog = select_menu_catalog;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria1_id() {
		return select_creteria1_id;
	}

	public void setSelect_creteria1_id(List<XmlDBCriteriaLocale> select_creteria1_id) {
		this.select_creteria1_id = select_creteria1_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria2_id() {
		return select_creteria2_id;
	}

	public void setSelect_creteria2_id(List<XmlDBCriteriaLocale> select_creteria2_id) {
		this.select_creteria2_id = select_creteria2_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria3_id() {
		return select_creteria3_id;
	}

	public void setSelect_creteria3_id(List<XmlDBCriteriaLocale> select_creteria3_id) {
		this.select_creteria3_id = select_creteria3_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria4_id() {
		return select_creteria4_id;
	}

	public void setSelect_creteria4_id(List<XmlDBCriteriaLocale> select_creteria4_id) {
		this.select_creteria4_id = select_creteria4_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria5_id() {
		return select_creteria5_id;
	}

	public void setSelect_creteria5_id(List<XmlDBCriteriaLocale> select_creteria5_id) {
		this.select_creteria5_id = select_creteria5_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria6_id() {
		return select_creteria6_id;
	}

	public void setSelect_creteria6_id(List<XmlDBCriteriaLocale> select_creteria6_id) {
		this.select_creteria6_id = select_creteria6_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria7_id() {
		return select_creteria7_id;
	}

	public void setSelect_creteria7_id(List<XmlDBCriteriaLocale> select_creteria7_id) {
		this.select_creteria7_id = select_creteria7_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria8_id() {
		return select_creteria8_id;
	}

	public void setSelect_creteria8_id(List<XmlDBCriteriaLocale> select_creteria8_id) {
		this.select_creteria8_id = select_creteria8_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria9_id() {
		return select_creteria9_id;
	}

	public void setSelect_creteria9_id(List<XmlDBCriteriaLocale> select_creteria9_id) {
		this.select_creteria9_id = select_creteria9_id;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria10_id() {
		return select_creteria10_id;
	}

	public void setSelect_creteria10_id(List<XmlDBCriteriaLocale> select_creteria10_id) {
		this.select_creteria10_id = select_creteria10_id;
	}

	public String getSelect_dayfrom_id() {
		return select_dayfrom_id;
	}

	public void setSelect_dayfrom_id(String select_dayfrom_id) {
		this.select_dayfrom_id = select_dayfrom_id;
	}

	public String getSelect_mountfrom_id() {
		return select_mountfrom_id;
	}

	public void setSelect_mountfrom_id(String select_mountfrom_id) {
		this.select_mountfrom_id = select_mountfrom_id;
	}

	public String getSelect_yearfrom_id() {
		return select_yearfrom_id;
	}

	public void setSelect_yearfrom_id(String select_yearfrom_id) {
		this.select_yearfrom_id = select_yearfrom_id;
	}

	public String getSelect_dayto_id() {
		return select_dayto_id;
	}

	public void setSelect_dayto_id(String select_dayto_id) {
		this.select_dayto_id = select_dayto_id;
	}

	public String getSelect_mountto_id() {
		return select_mountto_id;
	}

	public void setSelect_mountto_id(String select_mountto_id) {
		this.select_mountto_id = select_mountto_id;
	}

	public String getSelect_yearto_id() {
		return select_yearto_id;
	}

	public void setSelect_yearto_id(String select_yearto_id) {
		this.select_yearto_id = select_yearto_id;
	}

	@Override
	public String toString() {
		return "CMSProductListPageModel [balance=" + balance + ", offsetLastPage=" + offsetLastPage + ", catalog_id="
				+ catalog_id + ", catalog_parent_id=" + catalog_parent_id + ", dayfrom_id=" + dayfrom_id
				+ ", mountfrom_id=" + mountfrom_id + ", yearfrom_id=" + yearfrom_id + ", fromCost=" + fromCost
				+ ", toCost=" + toCost + ", dayto_id=" + dayto_id + ", mountto_id=" + mountto_id + ", yearto_id="
				+ yearto_id + ", creteria1_id=" + creteria1_id + ", creteria2_id=" + creteria2_id + ", creteria3_id="
				+ creteria3_id + ", creteria4_id=" + creteria4_id + ", creteria5_id=" + creteria5_id + ", creteria6_id="
				+ creteria6_id + ", creteria7_id=" + creteria7_id + ", creteria8_id=" + creteria8_id + ", creteria9_id="
				+ creteria9_id + ", creteria10_id=" + creteria10_id + ", locale=" + locale + ", strMessage="
				+ strMessage + ", user_site=" + user_site + ", strLogin=" + strLogin + ", strPasswd=" + strPasswd
				+ ", site_id=" + site_id + ", isInternet=" + isInternet + ", offset=" + offset + ", intLevelUp="
				+ intLevelUp + ", dialog=" + dialog + ", advancedSearchOpen=" + advancedSearchOpen + ", forumOpen="
				+ forumOpen + ", action=" + action + ", searchValueArg=" + searchValueArg + ", searchquery="
				+ searchquery + ", Adp=" + Adp + ", co1Adp=" + co1Adp + ", co2Adp=" + co2Adp + ", newsAdp=" + newsAdp
				+ ", blogExtAdp=" + blogExtAdp + ", bottomAdp=" + bottomAdp + ", criteria1_label=" + criteria1_label
				+ ", criteria2_label=" + criteria2_label + ", criteria3_label=" + criteria3_label + ", criteria4_label="
				+ criteria4_label + ", criteria5_label=" + criteria5_label + ", criteria6_label=" + criteria6_label
				+ ", criteria7_label=" + criteria7_label + ", criteria8_label=" + criteria8_label + ", criteria9_label="
				+ criteria9_label + ", criteria10_label=" + criteria10_label + ", select_currency_cd="
				+ select_currency_cd + ", select_tree_catalog=" + select_tree_catalog + ", select_menu_catalog="
				+ select_menu_catalog + ", select_creteria1_id=" + select_creteria1_id + ", select_creteria2_id="
				+ select_creteria2_id + ", select_creteria3_id=" + select_creteria3_id + ", select_creteria4_id="
				+ select_creteria4_id + ", select_creteria5_id=" + select_creteria5_id + ", select_creteria6_id="
				+ select_creteria6_id + ", select_creteria7_id=" + select_creteria7_id + ", select_creteria8_id="
				+ select_creteria8_id + ", select_creteria9_id=" + select_creteria9_id + ", select_creteria10_id="
				+ select_creteria10_id + ", select_dayfrom_id=" + select_dayfrom_id + ", select_mountfrom_id="
				+ select_mountfrom_id + ", select_yearfrom_id=" + select_yearfrom_id + ", select_dayto_id="
				+ select_dayto_id + ", select_mountto_id=" + select_mountto_id + ", select_yearto_id="
				+ select_yearto_id + "]";
	}

}
