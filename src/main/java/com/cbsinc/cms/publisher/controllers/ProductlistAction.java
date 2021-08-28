package com.cbsinc.cms.publisher.controllers;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code.
 * You can not use it and you cannot change it without written permission from Konstantin Grabko
 * Email: konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
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

import java.io.File;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.http.HttpSession;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
//import org.apache.tomcat.util.http.mapper.Mapper;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.ProductListWebForm;
import com.cbsinc.cms.dto.pages.response.CMSProductListPageModel;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.PolicyFaced;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.dao.ProductlistFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.ProductlistBean;
import io.swagger.annotations.ApiOperation;

@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class ProductlistAction extends CMSObjects {

    
    @Autowired
	private AuthorizationPageFaced authorizationPageFaced;
	
    @Value("${is_criteria_by_catalog}")
    private String  is_criteria_by_catalog_string;
    
    private XLogger logger = XLoggerFactory.getXLogger(ProductlistAction.class.getName());
    
    @Autowired
    ProductPostAllFaced productPostAllFaced;
    
    @Autowired
    ProductlistFaced productlistFaced;
    
    @Autowired
    PolicyFaced policyFaced;

    @Autowired
    ProductlistBean productlistBeanId;
    
    public ProductlistAction() {
        logger.entry(is_criteria_by_catalog_string);
        logger.exit();
    }
    
    
	
	//https://howtodoinjava.com/spring-boot2/testing/rest-controller-unit-test-example/
	
    @PostMapping(value = "/doPostProductlistAction", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Post For Product List Controller",notes = "Returns Product List based on the Request")
	public @ResponseBody CMSProductListPageModel doPost(@RequestBody ProductListWebForm productListWebForm)
			throws Exception {
		return doGet(productListWebForm);
	}
	
	@GetMapping(value = "/doGetProductlistAction", consumes = "application/json", produces = "application/json")
    @ApiOperation(value = "Get For Product List Controller",notes = "Returns Product List based on the Product ID")
	public @ResponseBody CMSProductListPageModel doGet(ProductListWebForm productListWebForm)throws Exception {
		AuthorizationPageBean authorizationPageBeanId;
		HttpSession session;
		boolean is_criteria_by_catalog = false;
		String notselected = "";
		boolean isInternet = true;
		logger.entry(productlistBeanId,productlistFaced,productPostAllFaced,productListWebForm);
		is_criteria_by_catalog = is_criteria_by_catalog_string.equals("true");
	
		if (getHttpServletRequest().getRemoteAddr().startsWith("192."))
			isInternet = false;
		if (getHttpServletRequest().getRemoteAddr().startsWith("10."))
			isInternet = false;
		session = getHttpSession();
		authorizationPageBeanId = getAuthorizationPageBean().get();

		if (notselected.length() == 0)
			notselected = authorizationPageBeanId.getLocalization(getServletContext()).getString("notselected");
	

		productlistBeanId.isInternet = isInternet;
		authorizationPageBeanId.setBalance(authorizationPageFaced.getStrBalans(authorizationPageBeanId.getIntUserID()));

		if (productListWebForm.getOffset() != null && isNumber(productListWebForm.getOffset())) {
			productlistBeanId.setOffset(productlistBeanId.stringToInt(productListWebForm.getOffset()));
			authorizationPageBeanId.setOffsetLastPage(Long.parseLong(productListWebForm.getOffset()));
		}

		if (productListWebForm.getCatalog_id() != null && isNumber(productListWebForm.getCatalog_id())) {
			if (authorizationPageBeanId.getCatalog_id().compareTo(productListWebForm.getCatalog_id()) != 0) {
				productlistBeanId.setOffset(0);
			}
			authorizationPageBeanId.setCatalog_id(productListWebForm.getCatalog_id());
			authorizationPageBeanId.setCatalogParent_id(productListWebForm.getCatalog_id());

		}
		productlistBeanId.setIntLevelUp(authorizationPageBeanId.getIntLevelUp());

		// Open or closed dialog window
		if (productListWebForm.getDialog() != null)
			productlistBeanId.setDialog(productListWebForm.getDialog());
		if (productListWebForm.getIs_advanced_search_open() != null)
			productlistBeanId.setAdvancedSearchOpen(productListWebForm.getIs_advanced_search_open());
		if (productListWebForm.getIs_forum_open() != null)
			productlistBeanId.setForumOpen(productListWebForm.getIs_forum_open());

		if (productListWebForm.getDayfrom_id() != null && isNumber(productListWebForm.getDayfrom_id()))
			authorizationPageBeanId.setDayfrom_id(Integer.parseInt(productListWebForm.getDayfrom_id()));
		if (productListWebForm.getMountfrom_id() != null && isNumber(productListWebForm.getMountfrom_id()))
			authorizationPageBeanId.setMountfrom_id(Integer.parseInt(productListWebForm.getMountfrom_id()));
		if (productListWebForm.getYearfrom_id() != null && isNumber(productListWebForm.getYearfrom_id()))
			authorizationPageBeanId.setYearfrom_id(Integer.parseInt(productListWebForm.getYearfrom_id()));

		if (productListWebForm.getFromcost() != null && isFloat(productListWebForm.getFromcost()))
			authorizationPageBeanId.setStrFromCost(productListWebForm.getFromcost().trim());
		if (productListWebForm.getTocost() != null && isFloat(productListWebForm.getTocost()))
			authorizationPageBeanId.setStrToCost(productListWebForm.getTocost().trim());

		if (productListWebForm.getDayto_id() != null && isNumber(productListWebForm.getDayto_id()))
			authorizationPageBeanId.setDayto_id(Integer.parseInt(productListWebForm.getDayto_id()));
		if (productListWebForm.getMountto_id() != null && isNumber(productListWebForm.getMountto_id()))
			authorizationPageBeanId.setMountto_id(Integer.parseInt(productListWebForm.getMountto_id()));
		if (productListWebForm.getYearto_id()!= null && isNumber(productListWebForm.getYearto_id()))
			authorizationPageBeanId.setYearto_id(Integer.parseInt(productListWebForm.getYearto_id()));

		if (productListWebForm.getAction() != null) {
			productlistBeanId.setAction(productListWebForm.getAction());
		}
		if (productListWebForm.getCreteria1_id() != null && isNumber(productListWebForm.getCreteria1_id()))
			authorizationPageBeanId.setStrCreteria1_id(productListWebForm.getCreteria1_id());
		if (productListWebForm.getCreteria2_id() != null && isNumber(productListWebForm.getCreteria2_id()))
			authorizationPageBeanId.setStrCreteria2_id(productListWebForm.getCreteria2_id());
		if (productListWebForm.getCreteria3_id() != null && isNumber(productListWebForm.getCreteria3_id()))
			authorizationPageBeanId.setStrCreteria3_id(productListWebForm.getCreteria3_id());
		if (productListWebForm.getCreteria4_id() != null && isNumber(productListWebForm.getCreteria4_id()))
			authorizationPageBeanId.setStrCreteria4_id(productListWebForm.getCreteria4_id());
		if (productListWebForm.getCreteria5_id() != null && isNumber(productListWebForm.getCreteria5_id()))
			authorizationPageBeanId.setStrCreteria5_id(productListWebForm.getCreteria5_id());
		if (productListWebForm.getCreteria6_id() != null && isNumber(productListWebForm.getCreteria6_id()))
			authorizationPageBeanId.setStrCreteria6_id(productListWebForm.getCreteria6_id());
		if (productListWebForm.getCreteria7_id() != null && isNumber(productListWebForm.getCreteria7_id()))
			authorizationPageBeanId.setStrCreteria7_id(productListWebForm.getCreteria7_id());
		if (productListWebForm.getCreteria8_id() != null && isNumber(productListWebForm.getCreteria8_id()))
			authorizationPageBeanId.setStrCreteria8_id(productListWebForm.getCreteria8_id());
		if (productListWebForm.getCreteria9_id() != null && isNumber(productListWebForm.getCreteria9_id()))
			authorizationPageBeanId.setStrCreteria9_id(productListWebForm.getCreteria9_id());
		if (productListWebForm.getCreteria10_id() != null && isNumber(productListWebForm.getCreteria10_id() ))
			authorizationPageBeanId.setStrCreteria10_id(productListWebForm.getCreteria10_id() );
		if (productListWebForm.getSearch_value() != null)
			productlistBeanId.setSearchValueArg(productListWebForm.getSearch_value());
		if (productListWebForm.getSearchquery() != null && isNumber(productListWebForm.getSearchquery()))
			productlistBeanId.setSearchquery(Integer.parseInt(productListWebForm.getSearchquery()));
		else if (productListWebForm.getOffset() == null)
			productlistBeanId.setSearchquery(0);

		String cl_locale = getHttpServletRequest().getLocale().getDisplayName();
		if (cl_locale != null) {

			if (isAllowLocale(cl_locale)) {

				if (authorizationPageBeanId.getLocale().compareTo(cl_locale) != 0) {
					productlistBeanId.setOffset(0);
					authorizationPageBeanId.setCatalog_id("" + SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
				}

				authorizationPageBeanId.setLocale(cl_locale, getServletContext());
			}

		}

		if (productlistBeanId.getSearchquery() == 2 && productListWebForm.getSearch_char() != null
				&& productListWebForm.getSearch_char().length() > 0) {
			// if new char then start with 0
			if (productlistBeanId.getSearchValueArg().compareTo(productListWebForm.getSearch_char() ) != 0) {
				productlistBeanId.setOffset(0);
			}

			productlistBeanId.setSearchValueArg(productListWebForm.getSearch_char());
		} else if (productlistBeanId.getSearchquery() == 1 && productListWebForm.getSearch_value()!= null
				&& productListWebForm.getSearch_value().length() > 0) {
			productlistBeanId.setSearchValueArg(productListWebForm.getSearch_value());
		} else if (productlistBeanId.getSearchquery() == 0) {
			productlistBeanId.setSearchValueArg("");
		}

		if (productlistBeanId.getAction().compareTo("del") == 0) {
			productlistBeanId.setAction("");
			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage("Access denny  for login user , You must be membership");
			}

			if (productListWebForm.getProduct_id() != null && isNumber(productListWebForm.getProduct_id())) {

				if (authorizationPageBeanId.getIntLevelUp() != 2) {
					int user_id = productlistFaced.getWhoseProduct(productListWebForm.getProduct_id());
					if (authorizationPageBeanId.getIntUserID() != user_id) {
						authorizationPageBeanId.setStrMessage(
								"Access denny  for login " + authorizationPageBeanId + " , You must be owner message");
						
					}
				}

				if (authorizationPageBeanId.getSite_id()
						.compareTo(productlistFaced.getSiteByProduct(productListWebForm.getProduct_id())) == 0)
					productlistFaced.deletePosition(productListWebForm.getProduct_id());
				else
					authorizationPageBeanId.setStrMessage("Access denny  for login " + authorizationPageBeanId + " , You must be owner message");
			}
		}

		if (productlistBeanId.getAction().compareTo("up_position") == 0) {
			int user_id = 0;
			productlistBeanId.setAction("");
			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage("Access denny  for login user , You must be membership");
				return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
			}

			if (productListWebForm.getProduct_id() != null) {

				if (authorizationPageBeanId.getIntLevelUp() != 2) {
					user_id = productlistFaced.getWhoseProduct(productListWebForm.getProduct_id());
					if (user_id == -1)
					  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId); // Пользователя для этой записи не сущетсвует
					if (authorizationPageBeanId.getIntUserID() != user_id) {
						authorizationPageBeanId.setStrMessage("Access denny  for login "
								+ authorizationPageBeanId.getStrLogin() + " , You must be owner message");
						return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
					}
				}


				productlistFaced.upPosition(productListWebForm.getProduct_id());
			}
		}

		if (productlistBeanId.getAction().compareTo("set_color") == 0) {
			productlistBeanId.setAction("");
			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage("Access denny  for login user , You must be membership");
				return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
			}

			if (productListWebForm.getProduct_id() != null) {

				if (authorizationPageBeanId.getIntLevelUp() != 2) {
					int user_id = productlistFaced.getWhoseProduct(productListWebForm.getProduct_id());
					if (authorizationPageBeanId.getIntUserID() != user_id) {
						authorizationPageBeanId.setStrMessage("Access denny  for login " + authorizationPageBeanId.getStrFirstName() + " , You must be owner message");
						return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);					}
				}
				productlistFaced.colorPosition(productListWebForm.getProduct_id(), "#FCF8CF");
			}
		}

		if (productlistBeanId.getAction().compareTo("edit") == 0) {

			if (isNumber(productListWebForm.getProduct_id())) {
				productlistBeanId.setAction("");

				// authorizationPageBeanId.setCatalog_id(productlistFaced.getCatalogId(productListWebForm.getProduct_id()));

//		        	  productPostAllFaced.initPage(productListWebForm.getProduct_id(),  SoftPostBeanId , authorizationPageBeanId);
				// Для того чтобы правильно отрывался раздел когда открываеш из другово раздела
				// authorizationPageBeanId.setCatalogParent_id("" +
				// productlistFaced.getCatalogParentId(authorizationPageBeanId));

				if (productListWebForm.getElement().compareTo("forum") == 0) {
					//response.sendRedirect("ProductUserPost.jsp?product_id=" + productListWebForm.getProduct_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("userinfo") == 0) {
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("edit_biz_info") == 0) {
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("edit_transport") == 0) {
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("edit_realty") == 0) {
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("addvideo") == 0) {
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("addmusics") == 0) {
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext1_user") == 0) {
					//response.sendRedirect("Ext1ProductPostUser.jsp?product_id=" + productListWebForm.getProduct_id());
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext2_user") == 0) {
					//response.sendRedirect("Ext2ProductPostUser.jsp?product_id=" + productListWebForm.getProduct_id());
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext_files_user") == 0) {
					//response.sendRedirect(
						//	"ExtFilesProductPostUser.jsp?product_id=" + productListWebForm.getProduct_id());
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext_ofice_files_user") == 0) {
//					response.sendRedirect(
//							"ExtOficeFilesProductPostUser.jsp?product_id=" + productListWebForm.getProduct_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext_music_files_user") == 0) {
					//response.sendRedirect(
						//	"ExtOficeFilesProductPostUser.jsp?product_id=" + productListWebForm.getProduct_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("product") == 0) {
//					response.sendRedirect("ProductPostCre.jsp?product_id=" + productListWebForm.getProduct_id()
//							+ "&parent_id=" + authorizationPageBeanId.getCatalog_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("news") == 0) {
//					response.sendRedirect("NewsBlockPostCre.jsp?product_id=" + productListWebForm.getProduct_id()
//							+ "&parent_id=" + authorizationPageBeanId.getCatalog_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				// if(productListWebForm.getElement().compareTo("news") == 0 )
				// response.sendRedirect("ProductPost.jsp") ;
				if (productListWebForm.getElement().compareTo("co1") == 0) {
//					response.sendRedirect("Co1ProductPost.jsp?product_id=" + productListWebForm.getProduct_id()
//							+ "&parent_id=" + authorizationPageBeanId.getCatalog_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("co2") == 0) {
//					response.sendRedirect("Co2ProductPost.jsp?product_id=" + productListWebForm.getProduct_id()
//							+ "&parent_id=" + authorizationPageBeanId.getCatalog_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("bottom") == 0) {
					//response.sendRedirect("BottomListPost.jsp?product_id=" + productListWebForm.getProduct_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext1") == 0) {
					//response.sendRedirect("Ext1ProductPost.jsp?product_id=" + productListWebForm.getProduct_id());
					logger.exit();
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext2") == 0) {
					logger.exit();
				  //response.sendRedirect("Ext2ProductPost.jsp?product_id=" + productListWebForm.getProduct_id());
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext_files") == 0) {
					//response.sendRedirect("ExtFilesProductPost.jsp?product_id=" + productListWebForm.getProduct_id());
					logger.exit();
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext_tabls") == 0) {
					//response.sendRedirect("ExtTabsProductPost.jsp?product_id=" + productListWebForm.getProduct_id());
					logger.exit();
				  return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("ext_service_page") == 0) {
					logger.exit();
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
				if (productListWebForm.getElement().compareTo("blog") == 0) {
					authorizationPageBeanId.setLastProductId(Long.parseLong(productListWebForm.getProduct_parent_id()));
					logger.exit();
					return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
				}
			}
		}
		if (productlistBeanId.getAction().compareTo("create_site") == 0) {
			productlistBeanId.setAction("");
			authorizationPageFaced.getCreateShopBean().setLogin(authorizationPageBeanId.getStrLogin());
			authorizationPageFaced.getCreateShopBean().setPasswd(authorizationPageBeanId.getStrPasswd());
			authorizationPageFaced.getCreateShopBean().setAddress("no created");

			String domain = authorizationPageBeanId.getSite_dir()
					.substring(authorizationPageBeanId.getSite_dir().indexOf("."));

			authorizationPageFaced.getCreateShopBean().setCompany_name(authorizationPageBeanId.getStrCompany());
			authorizationPageFaced.getCreateShopBean().setSite_dir(authorizationPageBeanId.getStrLogin() + domain);
			authorizationPageFaced.getCreateShopBean().setNick_site(authorizationPageBeanId.getStrLogin() + domain);
			authorizationPageFaced.getCreateShopBean().setSubject_site("Cabinet");
			authorizationPageFaced.getCreateShopBean().setPerson(
					authorizationPageBeanId.getStrFirstName() + " " + authorizationPageBeanId.getStrLastName());
			authorizationPageFaced.getCreateShopBean().setPhone(authorizationPageBeanId.getStrPhone());

			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext())
						.getString("you_can_not_to_create_shop"));
				return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
			}
			authorizationPageFaced.getCreateShopBean().addSite(authorizationPageBeanId.getIntUserID());
			logger.exit();
			return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
		}

		// if( productlistBeanId.getAction().compareTo("create_site2") == 0 )
		if (productListWebForm.getCreate_site_by_id() != null) {
			productlistBeanId.setAction("");
			authorizationPageFaced.getCreateShopBean().setLogin(authorizationPageBeanId.getStrLogin());
			authorizationPageFaced.getCreateShopBean().setPasswd(authorizationPageBeanId.getStrPasswd());
			authorizationPageFaced.getCreateShopBean().setAddress("no created");

			String domain = authorizationPageBeanId.getSite_dir()
					.substring(authorizationPageBeanId.getSite_dir().indexOf("."));

			authorizationPageFaced.getCreateShopBean().setCompany_name(authorizationPageBeanId.getStrCompany());
			authorizationPageFaced.getCreateShopBean().setSite_dir(authorizationPageBeanId.getStrLogin() + domain);
			authorizationPageFaced.getCreateShopBean()
					.setHost("www." + authorizationPageBeanId.getStrLogin() + ".irr.bz");
			authorizationPageFaced.getCreateShopBean().setNick_site(authorizationPageBeanId.getStrLogin() + domain);
			authorizationPageFaced.getCreateShopBean().setSubject_site("internet shop");
			authorizationPageFaced.getCreateShopBean().setPerson(
					authorizationPageBeanId.getStrFirstName() + " " + authorizationPageBeanId.getStrLastName());
			authorizationPageFaced.getCreateShopBean().setPhone(authorizationPageBeanId.getStrPhone());

			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("you_can_not_to_create_shop"));
				return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
		}

			if (authorizationPageBeanId.getLang_id() == 1)
				authorizationPageFaced.getCreateShopBean().addShopWithExtract(authorizationPageBeanId,
				    productListWebForm.getCreate_site_by_id(), getServletContext());
			else
				authorizationPageFaced.getCreateShopBean().addShopWithExtract_en(authorizationPageBeanId,productListWebForm.getCreate_site_by_id(), getServletContext());

			if (authorizationPageBeanId.getUser_site().equals("-1")) {
				Map messageMail = getMessageMail().get();
				String sitePath = (String) getHttpSession().getAttribute("site_path");
				String shop = sitePath + File.separatorChar + "mail" + File.separatorChar + "newshop.txt";
				String policy = sitePath + File.separatorChar + "mail" + File.separatorChar + "Policy.pdf";
				messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName());
				messageMail.put("@LastName", authorizationPageBeanId.getStrLastName());
				messageMail.put("@EmailPassword", authorizationPageBeanId.getEmailPassword());
				messageMail.put("@Password", authorizationPageBeanId.getStrPasswd());
				messageMail.put("@Login", authorizationPageBeanId.getStrLogin());
				MessageSender mqSender = new MessageSender(getHttpSession(), SendMailMessageBean.messageQuery);
				Message message = new Message();
				message.put("to", authorizationPageBeanId.getStrEMail());
				message.put("subject", "My Internet shop ");
				message.put("pathmessage", shop);
				message.put("fields", messageMail);
				mqSender.send(message);
			}

			// authorizationPageBeanId.
			authorizationPageBeanId.setUser_site(authorizationPageFaced.getCreateShopBean().getSite_id());

			// get current tomcat server, engine and context objects
			MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
			ObjectName name = new ObjectName("Catalina", "type", "Server");
			Server server = (Server) mBeanServer.getAttribute(name, "managedResource");
			Service[] services = server.findServices();
			StandardEngine engine = (StandardEngine) services[0].getContainer();
			StandardContext context = (StandardContext) engine.findChild(engine.getDefaultHost())
					.findChild(getServletContext().getContextPath());
			logger.exit();
			return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
		}

		if (productlistBeanId.getAction().compareTo("login_usersite") == 0) {
			productlistBeanId.setAction("");
			authorizationPageBeanId.setCatalog_id("-2");
			if (authorizationPageBeanId.getUser_site().compareTo("-1") != 0) {
				String cokie_session_id = (String) session.getAttribute("cokie_session_id");
				authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
				authorizationPageBeanId.setSite_id(authorizationPageBeanId.getUser_site(), authorizationPageFaced);
				String session_id = authorizationPageFaced.getCokieSessionId(getHttpServletRequest(),getHttpServletRsponse());
				if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
						authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, session_id)
						&& authorizationPageBeanId.getStrLogin().length() != 0) {
					
					productlistBeanId.setOffset(0);
				}
			} else
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(getServletContext()).getString("you_not_have_site"));

		}

///////+++
		if (productListWebForm.getSite() != null && isNumber(productListWebForm.getSite())) {

			productlistBeanId.setAction("");
			String site = productListWebForm.getSite();

			if (authorizationPageBeanId.getSite_id().compareTo(site) != 0) {
				productlistBeanId.setOffset(0);
				authorizationPageBeanId.setCatalog_id("" + SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			}

			String cokie_session_id = (String) session.getAttribute("cokie_session_id");
			authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
			authorizationPageBeanId.setSite_id(site, authorizationPageFaced);
			String session_id = authorizationPageFaced.getCokieSessionId(getHttpServletRequest(),getHttpServletRsponse());
			if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
					authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, session_id)
					&& authorizationPageBeanId.getStrLogin().length() != 0) {;
				productlistBeanId.setOffset(0);
			} else {
				authorizationPageBeanId.setStrPasswd(SiteRole.GUEST);
				authorizationPageBeanId.setStrLogin(SiteRole.GUEST);
				authorizationPageBeanId.setSite_id(site, authorizationPageFaced);
				if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
						authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, "")
						&& authorizationPageBeanId.getStrLogin().length() != 0) {
				
					productlistBeanId.setOffset(0);
				}
			}

		}

///////+++//
		if (productListWebForm.getLogoff_site()!= null) {
			productlistBeanId.setAction("");
			String cokie_session_id = (String) session.getAttribute("cokie_session_id");
			authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
			authorizationPageBeanId.setStrPasswd(SiteRole.GUEST);
			authorizationPageBeanId.setStrLogin(SiteRole.GUEST);
			authorizationPageBeanId.setSite_id(productListWebForm.getLogoff_site(), authorizationPageFaced);
			if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
					authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, "")
					&& authorizationPageBeanId.getStrLogin().length() != 0) {
				productlistBeanId.setOffset(0);
			}
		}

		if (productlistBeanId.getAction().compareTo("logoff") == 0
				|| productlistBeanId.getAction().compareTo("logoff_usersite") == 0) {
			String site_id = authorizationPageBeanId.getSite_id();
			String cokie_session_id = (String) session.getAttribute("cokie_session_id");
			session.invalidate();
			authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
			logger.exit();
			return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
		}


		productlistBeanId.Adp = productlistFaced.getProductlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), Long.parseLong(authorizationPageBeanId.getCatalog_id()),
				productlistBeanId, authorizationPageBeanId);
		productlistFaced.getQuantityProducts(productlistBeanId);
		productlistBeanId.co1Adp = productlistFaced.getCoOneProductlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId.getCatalog_id(), productlistBeanId,
				authorizationPageBeanId);
		productlistBeanId.co2Adp = productlistFaced.getCoTwoProductlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId.getCatalog_id(), productlistBeanId,
				authorizationPageBeanId);
		productlistBeanId.newsAdp = productlistFaced.getNewslist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId);
		productlistBeanId.blogExtAdp = productlistFaced.getBlogTopProductlist(authorizationPageBeanId.getSite_id(),
				productlistBeanId, authorizationPageBeanId);

		productlistBeanId.bottomAdp = productlistFaced.getBottomlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId);

		authorizationPageBeanId.setCatalogParent_id("" + productlistFaced.getCatalogParentId(authorizationPageBeanId));

		productlistBeanId.setCriteria1_label(productlistFaced
				.getOneLabel("select  label   from creteria1   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria2_label(productlistFaced
				.getOneLabel("select  label   from creteria2   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria3_label(productlistFaced
				.getOneLabel("select  label   from creteria3   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria4_label(productlistFaced
				.getOneLabel("select  label   from creteria4   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria5_label(productlistFaced
				.getOneLabel("select  label   from creteria5   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria6_label(productlistFaced
				.getOneLabel("select  label   from creteria6   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria7_label(productlistFaced
				.getOneLabel("select  label   from creteria7   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria8_label(productlistFaced
				.getOneLabel("select  label   from creteria8   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria9_label(productlistFaced
				.getOneLabel("select  label   from creteria9   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setCriteria10_label(productlistFaced
				.getOneLabel("select  label   from creteria10   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));

		productlistBeanId.setSelect_currency_cd(productlistFaced.getXMLDBList("Productlist.jsp?currency_cd",
				"currencies", productlistBeanId.getCurrency_cd(),
				"SELECT currency_cd , currency_desc  FROM currency  WHERE active = true","currency_cd","currency_desc"));
		productlistBeanId.setSelect_tree_catalog(productlistFaced.getTreeXMLDBList("Productlist.jsp?catalog_id",
				"catalog", authorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable   from catalog   where  active = true and lang_id = "
						+ authorizationPageBeanId.getLang_id() + " and site_id = "
						+ authorizationPageBeanId.getSite_id() + " and parent_id = "
						+ authorizationPageBeanId.getCatalog_parent_id(),
				"select catalog_id , lable   from catalog   where  active = true and parent_id = "
						+ authorizationPageBeanId.getCatalog_id()));

		productlistBeanId.setSelect_menu_catalog(productlistFaced.getMenuXMLDBList("Productlist.jsp?catalog_id", "menu",
				authorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable , parent_id  from catalog   where  active = true and parent_id = 0 and site_id = "
						+ authorizationPageBeanId.getSite_id() + " and lang_id = "
						+ authorizationPageBeanId.getLang_id()
						+ " or parent_id in (select catalog_id   from catalog   where  active = true and site_id = "
						+ authorizationPageBeanId.getSite_id() + "  and parent_id = 0 )"));


		productlistBeanId.setSelect_creteria1_id(productlistFaced.getXMLDBCriteriaListLocale(
				"Productlist.jsp?creteria1_id", "creteria1", "" + authorizationPageBeanId.getCreteria1_id(),
				notselected, "select creteria1_id , name   from creteria1   where  active = true " + productlistBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		productlistBeanId.setSelect_creteria2_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria2_id",
						"creteria2", "" + authorizationPageBeanId.getCreteria2_id(), notselected,
						"select creteria2_id , name   from creteria2   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria1_id()
								+ " ) "));
		productlistBeanId.setSelect_creteria3_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria3_id",
						"creteria3", "" + authorizationPageBeanId.getCreteria3_id(), notselected,
						"select creteria3_id , name   from creteria3   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria2_id()
								+ " ) "));
		productlistBeanId
				.setSelect_creteria4_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria4_id",
						"creteria4", "" + authorizationPageBeanId.getCreteria4_id(), notselected,
						"select creteria4_id , name   from creteria4   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria3_id()
								+ " ) "));
		productlistBeanId
				.setSelect_creteria5_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria5_id",
						"creteria5", "" + authorizationPageBeanId.getCreteria5_id(), notselected,
						"select creteria5_id , name   from creteria5   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria4_id()
								+ " ) "));
		productlistBeanId
				.setSelect_creteria6_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria6_id",
						"creteria6", "" + authorizationPageBeanId.getCreteria6_id(), notselected,
						"select creteria6_id , name   from creteria6   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria5_id()
								+ " ) "));
		productlistBeanId
				.setSelect_creteria7_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria7_id",
						"creteria7", "" + authorizationPageBeanId.getCreteria7_id(), notselected,
						"select creteria7_id , name   from creteria7   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria6_id()
								+ " ) "));
		productlistBeanId
				.setSelect_creteria8_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria8_id",
						"creteria8", "" + authorizationPageBeanId.getCreteria8_id(), notselected,
						"select creteria8_id , name   from creteria8   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria7_id()
								+ " ) "));
		productlistBeanId
				.setSelect_creteria9_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria9_id",
						"creteria9", "" + authorizationPageBeanId.getCreteria9_id(), notselected,
						"select creteria9_id , name   from creteria9   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria8_id()
								+ " ) "));
		productlistBeanId.setSelect_creteria10_id(productlistFaced.getXMLDBCriteriaListLocale("Productlist.jsp?creteria10_id",
						"creteria10", "" + authorizationPageBeanId.getCreteria10_id(), notselected,
						"select creteria10_id , name   from creteria10   where  active = true "
								+ productlistBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(),
										is_criteria_by_catalog)
								+ " and ( link_id = 0 or link_id = " + authorizationPageBeanId.getCreteria9_id()
								+ " ) "));

		productlistBeanId.setSelect_dayfrom_id(productlistFaced.getXMLListDateDay("Productlist.jsp?dayfrom_id",
				"dayfrom", "" + authorizationPageBeanId.getDayfrom_id()));
		productlistBeanId.setSelect_mountfrom_id(productlistFaced.getXMLListDateMount("Productlist.jsp?mountfrom_id",
				"mountfrom", "" + authorizationPageBeanId.getMountfrom_id()));
		productlistBeanId.setSelect_yearfrom_id(productlistFaced.getXMLListDateYear("Productlist.jsp?yearfrom_id",
				"yearfrom", "" + authorizationPageBeanId.getYearfrom_id()));

		productlistBeanId.setSelect_dayto_id(productlistFaced.getXMLListDateDay("Productlist.jsp?dayto_id", "dayto",
				"" + authorizationPageBeanId.getDayto_id()));
		productlistBeanId.setSelect_mountto_id(productlistFaced.getXMLListDateMount("Productlist.jsp?mountto_id",
				"mountto", "" + authorizationPageBeanId.getMountto_id()));
		productlistBeanId.setSelect_yearto_id(productlistFaced.getXMLListDateYear("Productlist.jsp?yearto_id", "yearto",
				"" + authorizationPageBeanId.getYearto_id()));
		logger.exit();
		return new CMSProductListPageModel(authorizationPageBeanId,productlistBeanId);
	}

	public boolean isNumber(String tmp) {
		if (tmp == null)
			return false;
		String IntField = "0123456789";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}

	public boolean isFloat(String tmp) {
		if (tmp == null)
			return false;
		String IntField = "0123456789.";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}

	public boolean isAllowLocale(String locale) {
		if (locale == null)
			return false;
		String[] IntField = { "en", "ru" };
		for (int i = 0; i < IntField.length; i++) {
			if (IntField[i].compareTo(locale) == 0) {
				return true;
			}
		}
		return false;
	}

}
