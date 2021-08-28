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

import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletRequest;
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
import com.cbsinc.cms.dto.pages.request.OrderListRequestForm;
import com.cbsinc.cms.dto.pages.response.CMSOrderListResponseModel;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.OrderListBean;


@RestController
public class OrderListAction extends CMSObjects{

    private final XLogger logger = XLoggerFactory.getXLogger(OrderListAction.class.getName());

    @Autowired
	OrderFaced orderFaced;
    
	SimpleDateFormat formatter;

	public boolean isInternet = true;

	public OrderListAction() {
	  logger.entry();
	  logger.exit();
	}
	
	@PostMapping(value = "/doPostOrderList",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSOrderListResponseModel doPost(@RequestBody OrderListRequestForm orderListRequestForm)
			throws Exception {
	    logger.entry(orderListRequestForm);
	    logger.exit();
		return doGet(orderListRequestForm);

	}

	@GetMapping(value = "/doGetOrderList",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSOrderListResponseModel doGet(@RequestBody OrderListRequestForm orderListRequestForm)	throws Exception {
	    logger.entry(orderListRequestForm);
		AuthorizationPageBean authorizationPageBeanId;
		HttpSession session =  getHttpSession();
		HttpServletRequest httpRequest = getHttpServletRequest();
		OrderListBean orderListBean = getOrderListBean().get();
		authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
		if (authorizationPageBeanId == null || orderListBean == null || orderFaced == null)
			return null;


		if (orderListRequestForm.getOffset() != null && isNumber(orderListRequestForm.getOffset())) {
			orderListBean.setOffset(Integer.parseInt(orderListRequestForm.getOffset()));
		}
		if (orderListRequestForm.getSearchquery() != null && isNumber(orderListRequestForm.getSearchquery())) {
			if (!orderListBean.getSearchquery().equals(orderListRequestForm.getSearchquery()))
				orderListBean.setOffset(0);
			orderListBean.setSearchquery(orderListRequestForm.getSearchquery());
		}


		if (orderListBean.getSearchquery().equals("2")) {

			if (orderListRequestForm.getDatefrom() != null) {
				if (isNumber(orderListRequestForm.getDatefrom()))
					orderListBean.setDateFrom(Long.parseLong(orderListRequestForm.getDatefrom()));
				else if (isDatePattern(orderListRequestForm.getDate_format()))
					orderListBean.setDateFrom(orderListRequestForm.getDatefrom(), orderListRequestForm.getDate_format(),httpRequest.getLocale());
			}
			if (orderListRequestForm.getDateto()!= null) {
				if (isNumber(orderListRequestForm.getDateto()))
					orderListBean.setDateTo(Long.parseLong(orderListRequestForm.getDateto()));
				else if (isDatePattern(orderListRequestForm.getDate_format()))
					orderListBean.setDateTo(orderListRequestForm.getDateto(),orderListRequestForm.getDate_format(),httpRequest.getLocale());
			}
			orderListBean.setSelectOrderlistXML(orderFaced.getOrderlistByDate(authorizationPageBeanId.getIntUserID(),
					orderListBean, httpRequest.getLocale(), authorizationPageBeanId.getIntLevelUp(),
					authorizationPageBeanId.getSite_id()));
			orderListBean.setSearchquery("0");
			return new CMSOrderListResponseModel(orderListBean);
		}

		if (orderListBean.getSearchquery().equals("3")) {

			if (orderListRequestForm.getOrder_paystatus() != null) {
				orderListBean.setOrder_paystatus_id(orderListRequestForm.getOrder_paystatus());
			}
			if (orderListRequestForm.getOrder_status() != null) {
				orderListBean.setDeliverystatus_id(orderListRequestForm.getOrder_status());
			}

			orderListBean.setSelectOrderlistXML(orderFaced.getOrderlistByStatus(authorizationPageBeanId.getSite_id(),
					orderListBean, httpRequest.getLocale()));
			orderListBean.setSelect_paystatus(orderFaced.getXMLDBList("OrderList.jsp?order_paystatus", "paystatus",
					orderListBean.getOrder_paystatus_id(),
					"select  paystatus_id , lable  from  paystatus  where  active  = true","paystatus_id","lable"));
			orderListBean.setSelect_deliverystatus(orderFaced.getXMLDBList("OrderList.jsp?order_deliverystatus",
					"deliverystatus", orderListBean.getDeliverystatus_id(),
					"select  deliverystatus_id , lable  from  deliverystatus  where  active  = true and lang = '"
							+ authorizationPageBeanId.getLocale() + "' ","deliverystatus_id","lable"));
			orderListBean.setSearchquery("0");
			return new CMSOrderListResponseModel(orderListBean,authorizationPageBeanId);
	}

		orderListBean.setSelectOrderlistXML(
				orderFaced.getOrderlist(authorizationPageBeanId.getIntUserID(), orderListBean, httpRequest.getLocale()));
		orderListBean.setSelect_paystatus(orderFaced.getXMLDBList("OrderList.jsp?order_paystatus", "paystatus",
				orderListBean.getOrder_paystatus_id(),
				"select  paystatus_id , lable  from  paystatus  where  active  = true","paystatus_id","lable"));
		orderListBean.setSelect_deliverystatus(orderFaced.getXMLDBList("OrderList.jsp?order_deliverystatus",
				"deliverystatus", orderListBean.getDeliverystatus_id(),
				"select  deliverystatus_id , lable  from  deliverystatus  where  active  = true and lang = '"
						+ authorizationPageBeanId.getLocale() + "' ","deliverystatus_id","lable"));

		orderListBean.setSelect_menu_catalog(orderFaced.getMenuXMLDBList("Productlist.jsp?catalog_id", "menu",
				authorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable , parent_id  from catalog   where  active = true and parent_id = 0 and site_id = "
						+ authorizationPageBeanId.getSite_id() + " and lang_id = "
						+ authorizationPageBeanId.getLang_id()
						+ " or parent_id in (select catalog_id   from catalog   where  active = true and site_id = "
						+ authorizationPageBeanId.getSite_id() + "  and parent_id = 0 )"));
		
		  logger.exit();
		return new CMSOrderListResponseModel(orderListBean,authorizationPageBeanId);
	}

	private boolean isNumber(String tmp) {
		if (tmp == null || tmp.length() == 0)
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

	private boolean isDatePattern(String tmp) {
		if (tmp == null || tmp.length() == 0)
			return false;
		String IntField = "dm/yMDY:.";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}
}
