/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code.
 * Программный код написан Грабко Константином Владимировичем и является его интеллектуальной 
 * собственностью.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Предприниматель Грабко Константин Владимирович
 * </p>
 * 
 * @author Konstantin Grabko
 * @version 1.0
 */

package com.cbsinc.cms.publisher.models;

import java.math.BigDecimal;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code. You can not use it and you
 * cannot change it without written permission from Konstantin Grabko Email:
 * konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002-2014
 * </p>
 * <p>
 * Company: CENTER BUSINESS SOLUTIONS INC
 * </p>
 * 
 * @author Konstantin Grabko
 * @version 1.0
 */
@Component
public class AuthorizationPageBean implements java.io.Serializable {

	private static final long serialVersionUID = -7953240488409859679L;

	//private static Logger log = Logger.getLogger(AuthorizationPageBean.class);
	private XLogger log = XLoggerFactory.getXLogger(AuthorizationPageBean.class.getName());

	transient private ResourceBundle localization = null;
	
	@Autowired
	 AuthorizationPageFaced authorizationPageFaced;

	public AuthorizationPageBean() {
		try {
			calendar = java.util.Calendar.getInstance();

			dayfrom_id = calendar.get(java.util.Calendar.DAY_OF_MONTH);
			mountfrom_id = (calendar.get(java.util.Calendar.MONTH) + 1);
			yearfrom_id = calendar.get(java.util.Calendar.YEAR);

			dayto_id = calendar.get(java.util.Calendar.DAY_OF_MONTH);
			mountto_id = (calendar.get(java.util.Calendar.MONTH) + 1);
			yearto_id = calendar.get(java.util.Calendar.YEAR);

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
	}

	java.util.Calendar calendar;

	private String strLogin = "";

	private String strPasswd = "";

	private Long intUserID = Long.valueOf(0);

	private Long intLevelUp = Long.valueOf(0);

	private String strFirstName = "";

	private String strLastName = "";

	private String strMessage = "";

	private String strEMail = "";

	private Integer intLogined = Integer.valueOf(0);

	private String site_id = "2";

	private Integer lang_id = Integer.valueOf(-1);

	private String site_dir = "localhost";

	private Integer rezalt_reg = Integer.valueOf(0);

	private String strCountry = "";

	private String strCity = "";

	private String strCompany = "";

	private String strPhone = "";

	private String strMPhone = "";

	private String strFax = "";

	private String strIcq = "";

	private String strWebsite = "";

	private String strQuestion = "";

	private String strAnswer = "";

	private String strCPasswd = "";

	private String country_id = "1";

	private String city_id = "0";

	private String currency_id = "1";

	private String paysys_shop_cd;

	private String address = "";

	private String subject_site = "";

	private String nick_site = "";

	private String company_name = "";

	private String host = "";

	private String user_site = "-1";

	private String userList = "";

	private String select_site = "";
	private String select_country = "";
	private String select_city = "";
	private String select_currency = "";

	private String catalog_id = "-2";

	private String catalog_parent_id = "0";

	private Long offsetLastPage = Long.valueOf(0);

	private Long lastProductId = Long.valueOf(0);

	private Long currentOrderId = Long.valueOf(0);

	private Long creteria1_id = Long.valueOf(0);

	private Long creteria2_id = Long.valueOf(0);

	private Long creteria3_id = Long.valueOf(0);

	private Long creteria4_id = Long.valueOf(0);

	private Long creteria5_id = Long.valueOf(0);

	private Long creteria6_id = Long.valueOf(0);

	private Long creteria7_id = Long.valueOf(0);

	private Long creteria8_id = Long.valueOf(0);

	private Long creteria9_id = Long.valueOf(0);

	private Long creteria10_id = Long.valueOf(0);

	private Integer dayfrom_id = Integer.valueOf(0);

	private Integer mountfrom_id = Integer.valueOf(0);

	private Integer yearfrom_id = Integer.valueOf(0);

	private Integer dayto_id = Integer.valueOf(0);

	private Integer mountto_id = Integer.valueOf(0);

	private Integer yearto_id = Integer.valueOf(0);

	private Integer numberPostedMessages = Integer.valueOf(0);

	private BigDecimal fromCost = BigDecimal.valueOf(0);
	private BigDecimal toCost = BigDecimal.valueOf(0);

	private String locale = "en";

	private String lastSessionId = "";

	private String balance = "0";

	// private String emailPassword = "" ;

	private String lastVisitedPage = "";

  private String select_menu_catalog;



	public boolean isHttpSession(javax.servlet.http.HttpServletResponse response,
			javax.servlet.http.HttpServletRequest request) {

		javax.servlet.http.HttpSession hsession = request.getSession();
		if (hsession == null) {
			hsession.invalidate();
			setStrMessage("You have auto logout  by timeout, login again");
			try {
				response.sendRedirect("Authorization.jsp");
			} catch (Exception ex) {
				System.err.println(ex);
			}
			return false;
		}

		if (getIntUserID() == 0) {
			setStrMessage("User not login or loguot by timeout , make login in site now");
			try {
				response.sendRedirect("Authorization.jsp");
			} catch (Exception ex) {
				System.err.println(ex);
			}
			return false;
		}

		return true;
	}



	public String getEmailPassword() {
		if (getStrPasswd().length() > 4)
			return getStrPasswd().substring(0, 4).concat(getStrLogin());
		else
			return "";
		// return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		// this.emailPassword = emailPassword;
	}

	public ResourceBundle getLocalization(ServletContext applicationContext) {

			localization = (ResourceBundle) applicationContext.getAttribute("user_locale_" + getLocale());
		return localization;
		// return localization;
	}



	public void setLocale(String locale, ServletContext applicationContext) {
      this.locale = locale;
      if (applicationContext == null)
          return;
     
      if (authorizationPageFaced != null) {
          setLang_id(authorizationPageFaced.getLengId(locale));
      }
  }



	public void setSelect_menu_catalog(String select_menu_catalog) {
      this.select_menu_catalog = select_menu_catalog;
  }
	
	   public void setSite_id(String site_id, AuthorizationPageFaced authorizationPageFaced) {
	        this.site_id = site_id;
	        try {
	            authorizationPageFaced.initSiteDir(this.site_id, this);
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }

	    }
	   public void setIntUserID(long intUserID) {
	        this.intUserID = intUserID;
	    }

	    public long getIntUserID() {
	        return intUserID;
	    }

	    public void setIntLevelUp(long intLevelUp) {
	        this.intLevelUp = intLevelUp;
	    }

	    public long getIntLevelUp() {
	        return intLevelUp;
	    }
	    
	    
	    @Deprecated
	    public void setCatalog_parent_id(String catalog_parent_id) {
	        this.catalog_parent_id = catalog_parent_id;
	    }
	    
	    public void setCatalogParent_id(String catalog_parent_id) {
	        this.catalog_parent_id = catalog_parent_id;
	    }

	    
	    public void setStrToCost(String toCost) {
	        this.toCost = new BigDecimal(toCost);
	    }
	    public void setFromCost(BigDecimal fromCost) {
	        this.fromCost = fromCost;
	    }

	    public void setStrFromCost(String fromCost) {
	        this.fromCost = new BigDecimal(fromCost);
	    }
	    
	    public long getCreteria1_id() {
	        return creteria1_id;
	    }

//	  public String getCreteria1_id() {
//	      return Long.toString(creteria1_id);
//	  }

	    public void setCreteria1_id(long creteria1_id) {
	        this.creteria1_id = creteria1_id;
	    }

	    public void setStrCreteria1_id(String creteria1_id) {
	        this.creteria1_id = Long.parseLong(creteria1_id);
	    }

	    public long getCreteria10_id() {
	        return creteria10_id;
	    }

	    public void setCreteria10_id(long creteria10_id) {
	        this.creteria10_id = creteria10_id;
	    }

	    public void setStrCreteria10_id(String creteria10_id) {
	        this.creteria10_id = Long.parseLong(creteria10_id);
	    }

	    public long getCreteria2_id() {
	        return creteria2_id;
	    }

	    public void setCreteria2_id(long creteria2_id) {
	        this.creteria2_id = creteria2_id;
	    }

	    public void setStrCreteria2_id(String creteria2_id) {
	        this.creteria2_id = Long.parseLong(creteria2_id);
	    }

	    public long getCreteria3_id() {
	        return creteria3_id;
	    }

	    public void setCreteria3_id(long creteria3_id) {
	        this.creteria3_id = creteria3_id;
	    }

	    public void setStrCreteria3_id(String creteria3_id) {
	        this.creteria3_id = Long.parseLong(creteria3_id);
	    }

	    public long getCreteria4_id() {
	        return creteria4_id;
	    }

	    public void setCreteria4_id(long creteria4_id) {
	        this.creteria4_id = creteria4_id;
	    }

	    public void setStrCreteria4_id(String creteria4_id) {
	        this.creteria4_id = Long.parseLong(creteria4_id);
	    }

	    public long getCreteria5_id() {
	        return creteria5_id;
	    }

	    public void setCreteria5_id(long creteria5_id) {
	        this.creteria5_id = creteria5_id;
	    }

	    public void setStrCreteria5_id(String creteria5_id) {
	        this.creteria5_id = Long.parseLong(creteria5_id);
	    }

	    public long getCreteria6_id() {
	        return creteria6_id;
	    }

	    public void setCreteria6_id(long creteria6_id) {
	        this.creteria6_id = creteria6_id;
	    }

	    public void setStrCreteria6_id(String creteria6_id) {
	        this.creteria6_id = Long.parseLong(creteria6_id);
	    }

	    public long getCreteria7_id() {
	        return creteria7_id;
	    }

	    public void setCreteria7_id(long creteria7_id) {
	        this.creteria7_id = creteria7_id;
	    }

	    public void setStrCreteria7_id(String creteria7_id) {
	        this.creteria7_id = Long.parseLong(creteria7_id);
	    }

	    public long getCreteria8_id() {
	        return creteria8_id;
	    }

	    public void setCreteria8_id(long creteria8_id) {
	        this.creteria8_id = creteria8_id;
	    }

	    public void setStrCreteria8_id(String creteria8_id) {
	        this.creteria8_id = Long.parseLong(creteria8_id);
	    }

	    public long getCreteria9_id() {
	        return creteria9_id;
	    }

	    public void setCreteria9_id(long creteria9_id) {
	        this.creteria9_id = creteria9_id;
	    }

	    public void setStrCreteria9_id(String creteria9_id) {
	        this.creteria9_id = Long.parseLong(creteria9_id);
	    }



		public XLogger getLog() {
			return log;
		}



		public void setLog(XLogger log) {
			this.log = log;
		}



		public ResourceBundle getLocalization() {
			return localization;
		}



		public void setLocalization(ResourceBundle localization) {
			this.localization = localization;
		}



		public AuthorizationPageFaced getAuthorizationPageFaced() {
			return authorizationPageFaced;
		}



		public void setAuthorizationPageFaced(AuthorizationPageFaced authorizationPageFaced) {
			this.authorizationPageFaced = authorizationPageFaced;
		}



		public java.util.Calendar getCalendar() {
			return calendar;
		}



		public void setCalendar(java.util.Calendar calendar) {
			this.calendar = calendar;
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



		public String getStrMessage() {
			return strMessage;
		}



		public void setStrMessage(String strMessage) {
			this.strMessage = strMessage;
		}



		public String getStrEMail() {
			return strEMail;
		}



		public void setStrEMail(String strEMail) {
			this.strEMail = strEMail;
		}



		public Integer getIntLogined() {
			return intLogined;
		}



		public void setIntLogined(Integer intLogined) {
			this.intLogined = intLogined;
		}



		public String getSite_id() {
			return site_id;
		}



		public void setSite_id(String site_id) {
			this.site_id = site_id;
		}



		public Integer getLang_id() {
			return lang_id;
		}



		public void setLang_id(Integer lang_id) {
			this.lang_id = lang_id;
		}



		public String getSite_dir() {
			return site_dir;
		}



		public void setSite_dir(String site_dir) {
			this.site_dir = site_dir;
		}



		public Integer getRezalt_reg() {
			return rezalt_reg;
		}



		public void setRezalt_reg(Integer rezalt_reg) {
			this.rezalt_reg = rezalt_reg;
		}



		public String getStrCountry() {
			return strCountry;
		}



		public void setStrCountry(String strCountry) {
			this.strCountry = strCountry;
		}



		public String getStrCity() {
			return strCity;
		}



		public void setStrCity(String strCity) {
			this.strCity = strCity;
		}



		public String getStrCompany() {
			return strCompany;
		}



		public void setStrCompany(String strCompany) {
			this.strCompany = strCompany;
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



		public String getStrQuestion() {
			return strQuestion;
		}



		public void setStrQuestion(String strQuestion) {
			this.strQuestion = strQuestion;
		}



		public String getStrAnswer() {
			return strAnswer;
		}



		public void setStrAnswer(String strAnswer) {
			this.strAnswer = strAnswer;
		}



		public String getStrCPasswd() {
			return strCPasswd;
		}



		public void setStrCPasswd(String strCPasswd) {
			this.strCPasswd = strCPasswd;
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



		public String getPaysys_shop_cd() {
			return paysys_shop_cd;
		}



		public void setPaysys_shop_cd(String paysys_shop_cd) {
			this.paysys_shop_cd = paysys_shop_cd;
		}



		public String getAddress() {
			return address;
		}



		public void setAddress(String address) {
			this.address = address;
		}



		public String getSubject_site() {
			return subject_site;
		}



		public void setSubject_site(String subject_site) {
			this.subject_site = subject_site;
		}



		public String getNick_site() {
			return nick_site;
		}



		public void setNick_site(String nick_site) {
			this.nick_site = nick_site;
		}



		public String getCompany_name() {
			return company_name;
		}



		public void setCompany_name(String company_name) {
			this.company_name = company_name;
		}



		public String getHost() {
			return host;
		}



		public void setHost(String host) {
			this.host = host;
		}



		public String getUser_site() {
			return user_site;
		}



		public void setUser_site(String user_site) {
			this.user_site = user_site;
		}



		public String getUserList() {
			return userList;
		}



		public void setUserList(String userList) {
			this.userList = userList;
		}



		public String getSelect_site() {
			return select_site;
		}



		public void setSelect_site(String select_site) {
			this.select_site = select_site;
		}



		public String getSelect_country() {
			return select_country;
		}



		public void setSelect_country(String select_country) {
			this.select_country = select_country;
		}



		public String getSelect_city() {
			return select_city;
		}



		public void setSelect_city(String select_city) {
			this.select_city = select_city;
		}



		public String getSelect_currency() {
			return select_currency;
		}



		public void setSelect_currency(String select_currency) {
			this.select_currency = select_currency;
		}



		public String getCatalog_id() {
			return catalog_id;
		}



		public void setCatalog_id(String catalog_id) {
			this.catalog_id = catalog_id;
		}



		public Long getOffsetLastPage() {
			return offsetLastPage;
		}



		public void setOffsetLastPage(Long offsetLastPage) {
			this.offsetLastPage = offsetLastPage;
		}



		public Long getLastProductId() {
			return lastProductId;
		}



		public void setLastProductId(Long lastProductId) {
			this.lastProductId = lastProductId;
		}



		public Long getCurrentOrderId() {
			return currentOrderId;
		}



		public void setCurrentOrderId(Long currentOrderId) {
			this.currentOrderId = currentOrderId;
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



		public Integer getNumberPostedMessages() {
			return numberPostedMessages;
		}



		public void setNumberPostedMessages(Integer numberPostedMessages) {
			this.numberPostedMessages = numberPostedMessages;
		}



		public BigDecimal getToCost() {
			return toCost;
		}



		public void setToCost(BigDecimal toCost) {
			this.toCost = toCost;
		}



		public String getLocale() {
			return locale;
		}



		public void setLocale(String locale) {
			this.locale = locale;
		}



		public String getLastSessionId() {
			return lastSessionId;
		}



		public void setLastSessionId(String lastSessionId) {
			this.lastSessionId = lastSessionId;
		}



		public String getBalance() {
			return balance;
		}



		public void setBalance(String balance) {
			this.balance = balance;
		}



		public String getLastVisitedPage() {
			return lastVisitedPage;
		}



		public void setLastVisitedPage(String lastVisitedPage) {
			this.lastVisitedPage = lastVisitedPage;
		}



		public String getCatalog_parent_id() {
			return catalog_parent_id;
		}



		public BigDecimal getFromCost() {
			return fromCost;
		}



		public String getSelect_menu_catalog() {
			return select_menu_catalog;
		}



		public void setIntUserID(Long intUserID) {
			this.intUserID = intUserID;
		}



		public void setIntLevelUp(Long intLevelUp) {
			this.intLevelUp = intLevelUp;
		}



		public void setCreteria1_id(Long creteria1_id) {
			this.creteria1_id = creteria1_id;
		}



		public void setCreteria2_id(Long creteria2_id) {
			this.creteria2_id = creteria2_id;
		}



		public void setCreteria3_id(Long creteria3_id) {
			this.creteria3_id = creteria3_id;
		}



		public void setCreteria4_id(Long creteria4_id) {
			this.creteria4_id = creteria4_id;
		}



		public void setCreteria5_id(Long creteria5_id) {
			this.creteria5_id = creteria5_id;
		}



		public void setCreteria6_id(Long creteria6_id) {
			this.creteria6_id = creteria6_id;
		}



		public void setCreteria7_id(Long creteria7_id) {
			this.creteria7_id = creteria7_id;
		}



		public void setCreteria8_id(Long creteria8_id) {
			this.creteria8_id = creteria8_id;
		}



		public void setCreteria9_id(Long creteria9_id) {
			this.creteria9_id = creteria9_id;
		}



		public void setCreteria10_id(Long creteria10_id) {
			this.creteria10_id = creteria10_id;
		}
	    
	    
}
