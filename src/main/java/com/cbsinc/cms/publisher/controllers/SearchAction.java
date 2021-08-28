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
import java.util.HashMap;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.SearchRequestForm;
import com.cbsinc.cms.dto.pages.response.CMSSearchPageModel;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.PolicyFaced;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.dao.ProductlistFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.SearchBean;
import io.swagger.annotations.ApiOperation;

@RestController
public class SearchAction extends CMSObjects{
  
    private final XLogger logger = XLoggerFactory.getXLogger(SearchAction.class.getName());
    
    @Autowired
	ProductPostAllFaced productPostAllFaced;
	
    @Autowired
    AuthorizationPageFaced authorizationPageFaced;

    @Autowired
    ProductlistFaced productlistFaced ;
    
    @Autowired
    PolicyFaced policyFaced;
    
    SearchBean searchBeanId;

    
	public SearchAction() {
	  logger.entry();
	  logger.exit();
	}

	@ApiOperation(value = "Post For Search",
	    notes = "Returns Search Result in Json Format based on the Search Request")
	@PostMapping(value = "/doPostSearch",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSSearchPageModel doPost(@RequestBody SearchRequestForm searchRequestBody)
			throws Exception {
		return doGet(searchRequestBody);
	}

	@ApiOperation(value = "Get For Search",
        notes = "Returns Search Result in Json Format based on the Search Request")
	@GetMapping(value = "/doGetSearch",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSSearchPageModel doGet(@RequestBody SearchRequestForm searchRequestBody)
			throws Exception {
		AuthorizationPageBean authorizationPageBeanId;
		HttpSession session;
		String notselected = "";
		boolean isInternet = true;

		if (getHttpServletRequest().getRemoteAddr().startsWith("192."))
			isInternet = false;
		if (getHttpServletRequest().getRemoteAddr().startsWith("10."))
			isInternet = false;
		session = getHttpSession();
        authorizationPageBeanId = getAuthorizationPageBean().get();

		if (notselected.length() == 0)
			notselected = authorizationPageBeanId.getLocalization(getServletContext()).getString("notselected");

		if (policyFaced == null || searchBeanId == null || authorizationPageBeanId == null
				|| authorizationPageFaced == null)
			return null;


		searchBeanId.isInternet = isInternet;
		authorizationPageBeanId.setBalance(authorizationPageFaced.getStrBalans(authorizationPageBeanId.getIntUserID()));

		if (searchRequestBody.getOffset() != null && isNumber(searchRequestBody.getOffset())) {
			searchBeanId.setOffset(searchBeanId.stringToInt(searchRequestBody.getOffset()));
			authorizationPageBeanId.setOffsetLastPage(Long.parseLong(searchRequestBody.getOffset()));
		}

		if (searchRequestBody.getCatalog_id() != null && isNumber(searchRequestBody.getCatalog_id())) {
			if (authorizationPageBeanId.getCatalog_id().compareTo(searchRequestBody.getCatalog_id()) != 0) {
				searchBeanId.setOffset(0);
			}
			authorizationPageBeanId.setCatalog_id(searchRequestBody.getCatalog_id());

		}
		searchBeanId.setIntLevelUp(authorizationPageBeanId.getIntLevelUp());

		// Open or closed dialog window
		if (searchRequestBody.getDialog() != null)
			searchBeanId.setDialog(searchRequestBody.getDialog());
		if (searchRequestBody.getIs_advanced_search_open() != null)
			searchBeanId.setAdvancedSearchOpen(searchRequestBody.getIs_advanced_search_open());
		if (searchRequestBody.getIs_forum_open() != null)
			searchBeanId.setForumOpen(searchRequestBody.getIs_forum_open());

		if (searchRequestBody.getFromcost()!= null && isFloat(searchRequestBody.getFromcost()))
			authorizationPageBeanId.setStrFromCost(searchRequestBody.getFromcost().trim());
		if (searchRequestBody.getTocost() != null && isFloat(searchRequestBody.getTocost()))
			authorizationPageBeanId.setStrToCost(searchRequestBody.getTocost().trim());

		if (searchRequestBody.getAction()!= null) {
			searchBeanId.setAction(searchRequestBody.getAction());
		}
		if (searchRequestBody.getSearch_value() != null)
			searchBeanId.setSearchValueArg(searchRequestBody.getSearch_value());
		if (searchRequestBody.getSearchquery() != null && isNumber(searchRequestBody.getSearchquery() ))
			searchBeanId.setSearchquery(Integer.parseInt(searchRequestBody.getSearchquery() ));
		else if (searchRequestBody.getOffset() == null)
			searchBeanId.setSearchquery(0);

		String cl_locale = getHttpServletRequest().getLocale().getDisplayName();
		if (cl_locale != null) {

			if (isAllowLocale(cl_locale)) {

				if (authorizationPageBeanId.getLocale().compareTo(cl_locale) != 0) {
					searchBeanId.setOffset(0);
					authorizationPageBeanId.setCatalog_id("" + SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
				}

				authorizationPageBeanId.setLocale(cl_locale, getServletContext());
			}

		}

		if (searchBeanId.getSearchquery() == 2 && searchRequestBody.getSearch_char() != null
				&& searchRequestBody.getSearch_char().length() > 0) {
			// if new char then start with 0
			if (searchBeanId.getSearchValueArg().compareTo(searchRequestBody.getSearch_char()) != 0) {
				searchBeanId.setOffset(0);
			}

			searchBeanId.setSearchValueArg(searchRequestBody.getSearch_value());
		} else if (searchBeanId.getSearchquery() == 1 && searchRequestBody.getSearch_value() != null
				&& searchRequestBody.getSearch_value().length() > 0) {
			searchBeanId.setSearchValueArg(searchRequestBody.getSearch_value());
		} else if (searchBeanId.getSearchquery() == 0) {
			searchBeanId.setSearchValueArg("");
		}

		if (searchBeanId.getAction().compareTo("del") == 0) {
			searchBeanId.setAction("");
			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage("Access denny  for login user , You must be membership");
				//servletContext.getRequestDispatcher("/Authorization.jsp").forward(request, response);
			}

			if (searchRequestBody.getProduct_id() != null && isNumber(searchRequestBody.getProduct_id())) {

				if (authorizationPageBeanId.getIntLevelUp() != 2) {
					int user_id = productlistFaced.getWhoseProduct(searchRequestBody.getProduct_id());
					if (authorizationPageBeanId.getIntUserID() != user_id) {
						authorizationPageBeanId.setStrMessage(
								"Access denny  for login " + authorizationPageBeanId + " , You must be owner message");
						//servletContext.getRequestDispatcher("/Authorization.jsp").forward(request, response);
					}
				}

				if (authorizationPageBeanId.getSite_id()
						.compareTo(productlistFaced.getSiteByProduct(searchRequestBody.getProduct_id())) == 0)
					productlistFaced.deletePosition(searchRequestBody.getProduct_id());
				else
					authorizationPageBeanId.setStrMessage(
							"Access denny  for login " + authorizationPageBeanId + " , You must be owner message");
			}
		}

		if (searchBeanId.getAction().compareTo("up_position") == 0) {
			int user_id = 0;
			searchBeanId.setAction("");
			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage("Access denny  for login user , You must be membership");
				//servletContext.getRequestDispatcher("/Authorization.jsp").forward(request, response);
				return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
			}

			if (searchRequestBody.getProduct_id()!= null) {

				if (authorizationPageBeanId.getIntLevelUp() != 2) {
					user_id = productlistFaced.getWhoseProduct(searchRequestBody.getProduct_id());
					if (user_id == -1)
					    return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
					if (authorizationPageBeanId.getIntUserID() != user_id) {
						authorizationPageBeanId.setStrMessage("Access denny  for login "
								+ authorizationPageBeanId.getStrLogin() + " , You must be owner message");
						//servletContext.getRequestDispatcher("/Authorization.jsp").forward(request, response);
						return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
					}
				}


				productlistFaced.upPosition(searchRequestBody.getProduct_id());
			}

		}

		if (searchBeanId.getAction().compareTo("set_color") == 0) {
			searchBeanId.setAction("");
			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage("Access denny  for login user , You must be membership");
				//servletContext.getRequestDispatcher("/Authorization.jsp").forward(request, response);
				return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
			}

			if (searchRequestBody.getProduct_id()!= null) {

				if (authorizationPageBeanId.getIntLevelUp() != 2) {
					int user_id = productlistFaced.getWhoseProduct(searchRequestBody.getProduct_id());
					if (authorizationPageBeanId.getIntUserID() != user_id) {
						authorizationPageBeanId.setStrMessage(
								"Access denny  for login " + authorizationPageBeanId + " , You must be owner message");
						//servletContext.getRequestDispatcher("/Authorization.jsp").forward(request, response);
						return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
					}
				}
				productlistFaced.colorPosition(searchRequestBody.getProduct_id(), "#FCF8CF");
			}
		}

		if (searchBeanId.getAction().compareTo("edit") == 0) {

			if (isNumber(searchRequestBody.getProduct_id())) {
				searchBeanId.setAction("");



			}
		}

		if (searchBeanId.getAction().compareTo("create_site") == 0) {
			searchBeanId.setAction("");
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
				authorizationPageBeanId.setStrMessage("не регистрированный пользователь  не может создовать магазин");
				//servletContext.getRequestDispatcher("/Authorization.jsp?Login=newuser").forward(request, response);
				return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
			}
			authorizationPageFaced.getCreateShopBean().addSite(authorizationPageBeanId.getIntUserID());
//			servletContext
//					.getRequestDispatcher(
//							"/Authorization.jsp?site_id=" + authorizationPageFaced.getCreateShopBean().getSite_id()
//									+ "&Login=" + authorizationPageFaced.getCreateShopBean().getLogin() + "&Passwd1="
//									+ authorizationPageFaced.getCreateShopBean().getPasswd())
//					.forward(request, response);
			return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);

		}

		if (searchRequestBody.getCreate_site_by_id() != null) {
			searchBeanId.setAction("");
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
			authorizationPageFaced.getCreateShopBean().setSubject_site("Cabinet");
			authorizationPageFaced.getCreateShopBean().setPerson(
					authorizationPageBeanId.getStrFirstName() + " " + authorizationPageBeanId.getStrLastName());
			authorizationPageFaced.getCreateShopBean().setPhone(authorizationPageBeanId.getStrPhone());

			if (authorizationPageBeanId.getIntLevelUp() == 0) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext())
						.getString("you_can_not_to_create_shop"));
				//servletContext.getRequestDispatcher("/Authorization.jsp?Login=").forward(request, response);
				return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
			}

			if (authorizationPageBeanId.getLang_id() == 1)
				authorizationPageFaced.getCreateShopBean().addShopWithExtract(authorizationPageBeanId,
				    searchRequestBody.getCreate_site_by_id(), getServletContext());
			else
				authorizationPageFaced.getCreateShopBean().addShopWithExtract_en(authorizationPageBeanId,
				    searchRequestBody.getCreate_site_by_id(), getServletContext());

			// authorizationPageFaced.getCreateShopBean().addShopWithExtract_allLang(authorizationPageBeanId,request.getParameter("create_site_by_id"),servletContext);

			if (authorizationPageBeanId.getUser_site().equals("-1")) {
				HashMap messageMail = (HashMap) session.getAttribute("messageMail");
				String sitePath = (String) getHttpSession().getAttribute("site_path");
				String shop = sitePath + File.separatorChar + "mail" + File.separatorChar + "newshop.txt";
				String policy = sitePath + File.separatorChar + "mail" + File.separatorChar + "Policy.pdf";
				messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName());
				messageMail.put("@LastName", authorizationPageBeanId.getStrLastName());
				messageMail.put("@EmailPassword", authorizationPageBeanId.getEmailPassword());
				messageMail.put("@Password", authorizationPageBeanId.getStrPasswd());
				messageMail.put("@Login", authorizationPageBeanId.getStrLogin());
				messageMail.put("@Shop", "http://www.siteoneclick.com/Productlist.jsp?site="
						+ authorizationPageFaced.getCreateShopBean().getSite_id());
				messageMail.put("@Policy", "http://www.siteoneclick.com/Policy.pdf");

				MessageSender mqSender = new MessageSender(getHttpSession(), SendMailMessageBean.messageQuery);
				Message message = new Message();
				message.put("to", authorizationPageBeanId.getStrEMail());
				message.put("subject", "My Internet shop ");
				message.put("pathmessage", shop);
				message.put("fields", messageMail);
				// message.put("@Policy", "http://www.siteoneclick.com/Policy.pdf" ) ;
				// message.put("attachFile", policy ) ;
				mqSender.send(message);
			}

			authorizationPageBeanId.setUser_site(authorizationPageFaced.getCreateShopBean().getSite_id());

			//response.sendRedirect("Productlist.jsp?action=login_usersite");
			return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
		}

		if (searchBeanId.getAction().compareTo("login_usersite") == 0) {
			searchBeanId.setAction("");
			authorizationPageBeanId.setCatalog_id("-2");
			if (authorizationPageBeanId.getUser_site().compareTo("-1") != 0) {
				String cokie_session_id = (String) session.getAttribute("cokie_session_id");
				authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
				authorizationPageBeanId.setSite_id(authorizationPageBeanId.getUser_site(), authorizationPageFaced);
				String session_id = authorizationPageFaced.getCokieSessionId(getHttpServletRequest(),getHttpServletRsponse());
				if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
						authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, session_id)
						&& authorizationPageBeanId.getStrLogin().length() != 0) {
					searchBeanId.setOffset(0);
				}
			} else
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(getServletContext()).getString("you_not_have_site"));

		}

///////+++
		if (searchRequestBody.getSite() != null && isNumber(searchRequestBody.getSite())) {

			searchBeanId.setAction("");
			String site = searchRequestBody.getSite();

			if (authorizationPageBeanId.getSite_id().compareTo(site) != 0) {
				searchBeanId.setOffset(0);
				authorizationPageBeanId.setCatalog_id("" + SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			}

			String cokie_session_id = (String) session.getAttribute("cokie_session_id");
			authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
			authorizationPageBeanId.setSite_id(site, authorizationPageFaced);
			String session_id = authorizationPageFaced.getCokieSessionId(getHttpServletRequest(),getHttpServletRsponse());
			if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
					authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, session_id)
					&& authorizationPageBeanId.getStrLogin().length() != 0) {
				searchBeanId.setOffset(0);
			} else {
				authorizationPageBeanId.setStrPasswd(SiteRole.GUEST);
				authorizationPageBeanId.setStrLogin(SiteRole.GUEST);
				authorizationPageBeanId.setSite_id(site, authorizationPageFaced);
				if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
						authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, "")
						&& authorizationPageBeanId.getStrLogin().length() != 0) {
					searchBeanId.setOffset(0);
				}
			}

		}

		if (searchRequestBody.getLogoff_site() != null) {
			searchBeanId.setAction("");
			String cokie_session_id = (String) session.getAttribute("cokie_session_id");
			authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
			authorizationPageBeanId.setStrPasswd(SiteRole.GUEST);
			authorizationPageBeanId.setStrLogin(SiteRole.GUEST);
			authorizationPageBeanId.setSite_id(searchRequestBody.getLogoff_site(), authorizationPageFaced);
			if (authorizationPageFaced.isLoginCorrect(authorizationPageBeanId.getStrLogin(),
					authorizationPageBeanId.getStrPasswd(), authorizationPageBeanId, "")
					&& authorizationPageBeanId.getStrLogin().length() != 0) {

				searchBeanId.setOffset(0);
			}
		}

		if (searchBeanId.getAction().compareTo("logoff") == 0
				|| searchBeanId.getAction().compareTo("logoff_usersite") == 0) {
			String site_id = authorizationPageBeanId.getSite_id();
			String cokie_session_id = (String) session.getAttribute("cokie_session_id");
			session.invalidate();
			authorizationPageFaced.clearCookieFromBD(authorizationPageBeanId, cokie_session_id);
			//servletContext.getRequestDispatcher("/Productlist.jsp?site=" + site_id).forward(request, response);
			return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);
		}

		searchBeanId.Adp = productlistFaced.getSearchList("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), Long.parseLong(authorizationPageBeanId.getCatalog_id()),
				searchBeanId, authorizationPageBeanId);
		productlistFaced.getQuantitySearch(searchBeanId);
		searchBeanId.co1Adp = productlistFaced.getCoOneSearchDirect("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId.getCatalog_id(), searchBeanId,
				authorizationPageBeanId);
		searchBeanId.co2Adp = productlistFaced.getCoTwoSearchDirect("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId.getCatalog_id(), searchBeanId,
				authorizationPageBeanId);

		authorizationPageBeanId.setCatalogParent_id("" + productlistFaced.getCatalogParentId(authorizationPageBeanId));

		searchBeanId.setSelect_currency_cd(productlistFaced.getXMLDBList("Productlist.jsp?currency_cd",
				"currencies", searchBeanId.getCurrency_cd(),
				"SELECT currency_cd , currency_desc  FROM currency  WHERE active = true","currency_cd","currency_desc"));
     return new CMSSearchPageModel(searchBeanId, authorizationPageBeanId);

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
