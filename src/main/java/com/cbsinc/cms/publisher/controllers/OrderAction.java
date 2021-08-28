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
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.models.AccountHistoryBean;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.OrderBean;



@RestController
public class OrderAction extends CMSObjects{
    
    @Autowired
	AuthorizationPageFaced authorizationPageFaced;

	@Autowired
	OrderFaced orderFaced ;

	public boolean isInternet = true;

	public OrderAction() {
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {
		request.getQueryString();
		Cookie cookie = new Cookie("payment_page", ((HttpServletRequest) request).getRequestURI());
		cookie.setMaxAge(60 * 60); // 1 hour
		response.addCookie(cookie);
		doGet(request, response, servletContext);
	}

	public void mappingForm(HttpServletRequest request, OrderBean orderBeanId) throws Exception {
		if (request.getParameter("city_id") != null)
			orderBeanId.setCity_id(request.getParameter("city_id"));
		if (request.getParameter("country_id") != null)
			orderBeanId.setCountry_id(request.getParameter("country_id"));
		if (request.getParameter("order_currency_id") != null)
			orderBeanId.setOrder_currency_id(request.getParameter("order_currency_id"));
		if (request.getParameter("delivery_amoun") != null)
			orderBeanId.setDelivery_amoun(request.getParameter("delivery_amoun"));
		if (request.getParameter("delivery_timeend") != null)
			orderBeanId.setdelivery_timeend(request.getParameter("delivery_timeend"));
		if (request.getParameter("end_amount") != null)
			orderBeanId.setend_amount(request.getParameter("end_amount"));
		if (request.getParameter("order_amount") != null)
			orderBeanId.setorder_amount(request.getParameter("order_amount"));
		if (request.getParameter("order_tax") != null)
			orderBeanId.setorder_tax(request.getParameter("order_tax"));
		if (request.getParameter("order_delivery_long") != null)
			orderBeanId.setorder_delivery_long(request.getParameter("order_delivery_long"));
		if (request.getParameter("order_paystatus") != null)
			orderBeanId.setorder_paystatus(request.getParameter("order_paystatus"));
		if (request.getParameter("order_status") != null)
			orderBeanId.setDeliverystatus_id(request.getParameter("order_status"));

		if (request.getParameter("cards_name") != null)
			orderBeanId.setcards_name(request.getParameter("cards_name"));
		if (request.getParameter("city_fullname") != null)
			orderBeanId.setcity_fullname(request.getParameter("city_fullname"));

		if (request.getParameter("currency_lable") != null)
			orderBeanId.setcurrency_lable(request.getParameter("currency_lable"));
		if (request.getParameter("shipment_address") != null)
			orderBeanId.setshipment_address(request.getParameter("shipment_address"));

		if (request.getParameter("contact_person") != null)
			orderBeanId.setContact_person(request.getParameter("contact_person"));

		if (request.getParameter("shipment_phone") != null)
			orderBeanId.setshipment_phone(request.getParameter("shipment_phone"));

		if (request.getParameter("shipment_email") != null)
			orderBeanId.setshipment_email(request.getParameter("shipment_email"));

		if (request.getParameter("shipment_fax") != null)
			orderBeanId.setshipment_fax(request.getParameter("shipment_fax"));
		if (request.getParameter("shipment_description") != null)
			orderBeanId.setshipment_description(request.getParameter("shipment_description"));
		if (request.getParameter("city_name") != null)
			orderBeanId.setcity_name(request.getParameter("city_name"));
		if (request.getParameter("country_name") != null)
			orderBeanId.setcountry_name(request.getParameter("country_name"));
		if (request.getParameter("country_telcode") != null)
			orderBeanId.setcountry_telcode(request.getParameter("country_telcode"));
		if (request.getParameter("currency_rate") != null)
			orderBeanId.setcurrency_rate(request.getParameter("currency_rate"));
		if (request.getParameter("order_city_telcode") != null)
			orderBeanId.setOrder_city_telcode(request.getParameter("order_city_telcode"));
		if (request.getParameter("delivery_start") != null)
			orderBeanId.setdelivery_start(request.getParameter("delivery_start"));
		if (request.getParameter("country_telcode") != null)
			orderBeanId.setcountry_telcode(request.getParameter("country_telcode"));
		if (request.getParameter("currency_rate") != null)
			orderBeanId.setcountry_telcode(request.getParameter("currency_rate"));
		if (request.getParameter("order_city_telcode") != null)
			orderBeanId.setOrder_city_telcode(request.getParameter("order_city_telcode"));
		if (request.getParameter("delivery_start") != null)
			orderBeanId.setdelivery_start(request.getParameter("delivery_start"));
		if (request.getParameter("cdate") != null)
			orderBeanId.setcdate(request.getParameter("cdate"));
		if (request.getParameter("imei") != null)
			orderBeanId.setImei(request.getParameter("imei"));
		
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {

		OrderBean orderBeanId;
		Map messageMail;
		AuthorizationPageBean AuthorizationPageBeanId;
		AccountHistoryBean accountHistoryBeanId;
		HttpSession session;

		if (request.getRemoteAddr().startsWith("192."))
			isInternet = false;
		if (request.getRemoteAddr().startsWith("10."))
			isInternet = false;

		session = request.getSession();
		
		
		AuthorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
		accountHistoryBeanId = (AccountHistoryBean) session.getAttribute("accountHistoryBeanId");
		orderBeanId = new OrderBean();
		request.setAttribute("orderBeanId", orderBeanId);
		orderBeanId = (OrderBean) request.getAttribute("orderBeanId");
		messageMail = (Map) session.getAttribute("messageMail");
		if (AuthorizationPageBeanId == null || accountHistoryBeanId == null || orderBeanId == null)
			return;

		request.setCharacterEncoding("UTF-8");

		orderBeanId.isInternet = isInternet;
		orderBeanId.setBalans(orderFaced.getBalans(AuthorizationPageBeanId.getIntUserID()));
		orderBeanId.setUser_ID("" + AuthorizationPageBeanId.getIntUserID());

		if (request.getParameter("offset") != null)
			orderBeanId.setOffset(orderFaced.stringToInt(request.getParameter("offset")));

		if (request.getParameter("create_order") != null) {
			String create_order = request.getParameter("create_order");
			if (create_order.compareTo("true") == 0)
				orderFaced.createOrder(AuthorizationPageBeanId.getCurrency_id(), orderBeanId);
			// orderFaced.createOrder("1",orderBeanId );
		}

//		InitOrder
		if (request.getParameter("order_id") != null) {
			if (!isNumber(request.getParameter("order_id")))
				return;
			orderBeanId.setOrder_id(request.getParameter("order_id"));
			AuthorizationPageBeanId.setCurrentOrderId(Long.parseLong(orderBeanId.getOrder_id()));
		} else if (request.getParameter("account_history_id") != null) {
			String account_history_id = request.getParameter("account_history_id");
			if (!isNumber(account_history_id))
				return;
			String order_id = orderFaced.getOrderByAccount(account_history_id);
			orderBeanId.setOrder_id(order_id);
			AuthorizationPageBeanId.setCurrentOrderId(Long.parseLong(orderBeanId.getOrder_id()));
		} else if (orderBeanId.getOrder_id().length() == 0) {

			if (AuthorizationPageBeanId.getCurrentOrderId() > 0) {
				orderBeanId.setOrder_id("" + AuthorizationPageBeanId.getCurrentOrderId());
				orderFaced.getProducts(request, orderBeanId);
			} else {
				orderFaced.createOrder(AuthorizationPageBeanId.getCurrency_id(), orderBeanId);
				AuthorizationPageBeanId.setCurrentOrderId(Long.parseLong(orderBeanId.getOrder_id()));

			}
		}

		mappingForm(request, orderBeanId);

		if (request.getParameter("action") != null) {
			orderBeanId.setAction(request.getParameter("action"));

			if (orderBeanId.getAction().compareTo("add") == 0) {

				if (request.getParameter("position") != null) {

					if (AuthorizationPageBeanId.getIntLevelUp() != 2
							&& orderBeanId.getDeliverystatus_id().equals("1")) {
						AuthorizationPageBeanId
								.setStrMessage("To make a new order you have to login again and use button exit.");
					} else {
						int quantity = 1;
						if (isNumber(request.getParameter("quantity")))
							quantity = Integer.parseInt(request.getParameter("quantity"));
						String rezult = orderFaced.addPosition(request.getParameter("position"), quantity, orderBeanId);
						if (rezult.compareTo("-1") == 0)
							AuthorizationPageBeanId.setStrMessage(AuthorizationPageBeanId
									.getLocalization(servletContext).getString("order.badcurrency.text"));
					}
				}
			}

			if (orderBeanId.getAction().compareTo("del") == 0)
				if (request.getParameter("position") != null) {

					if (AuthorizationPageBeanId.getIntLevelUp() != 2
							&& orderBeanId.getDeliverystatus_id().equals("1")) {
						AuthorizationPageBeanId.setStrMessage(
								"You can not dot remove item because the order is in process stage delivery.");
					} else
						orderFaced.deleteOrder(request.getParameter("position"), orderBeanId);
				}

			// for change payment status
			if (orderBeanId.getAction().compareTo("status") == 0) {
				float balans = orderBeanId.getBalans();
				float order_end_amount = Float.parseFloat(orderBeanId.getend_amount());
				long level_up = AuthorizationPageBeanId.getIntLevelUp();
				if (level_up != 2 && 0 > balans - order_end_amount) {
					AuthorizationPageBeanId.setStrMessage("You have less money for Payment of order  N "
							+ orderBeanId.getOrder_id() + " , Please click link \"Pay\" that payment order .");
					orderBeanId.setorder_paystatus("1");
				} else {
					int result = orderFaced.setSave(orderBeanId);
					switch (result) {
					case 5:
						AuthorizationPageBeanId.setStrMessage("Please enter phone number.");
						break;
					case 6:
						AuthorizationPageBeanId.setStrMessage("Please enter contact person.");
						break;
					}
				}
			}

			if ((orderBeanId.getAction().compareTo("save") == 0
					&& AuthorizationPageBeanId.getStrLogin().compareTo("user") != 0
					&& orderBeanId.getDeliverystatus_id().equals("0"))
					|| (orderBeanId.getAction().compareTo("save") == 0
							&& AuthorizationPageBeanId.getIntLevelUp() == 2)) {

				if (AuthorizationPageBeanId.getIntLevelUp() == 1)
					orderBeanId.setDeliverystatus_id("" + OrderDeliveryStatus.BILL_HAS_SENT_TO_CLIENT.getCode());

				String user_os = "";
				String header_name = "";
				String header_value = "";
				Enumeration en = request.getHeaderNames();
				while (en.hasMoreElements()) {
					header_name = (String) en.nextElement();
					header_value = request.getHeader(header_name);
					user_os = user_os.concat(header_name + "=" + header_value + "\n");
				}

				messageMail.clear();
				messageMail.put("@FirstName", AuthorizationPageBeanId.getStrFirstName());
				messageMail.put("@LastName", AuthorizationPageBeanId.getStrLastName());
				messageMail.put("@NumberOfOrder", orderBeanId.getOrder_id());
				messageMail.put("@ContactPerson", orderBeanId.getContact_person());
				messageMail.put("@Balans", "" + orderBeanId.getBalans());
				messageMail.put("@Phone", orderBeanId.getshipment_phone());
				messageMail.put("@Address", orderBeanId.getshipment_address());
				messageMail.put("@City", orderBeanId.getcity_fullname());
				messageMail.put("@Contry", orderBeanId.getcountry_fullname());
				messageMail.put("@CustomerEmail", orderBeanId.getshipment_email());
				messageMail.put("@CustomerFax", orderBeanId.getshipment_fax());
				messageMail.put("@CustomerCommentariy", orderBeanId.getshipment_description());
				messageMail.put("@ProductCount", "" + orderFaced.getProductsListSize(request, orderBeanId));
				messageMail.put("@Amount", "" + orderBeanId.getend_amount());
				messageMail.put("@Currency", "" + orderBeanId.getcurrency_lable());
				messageMail.put("@IPAddress", "" + request.getRemoteAddr());
				messageMail.put("@HTTPHEAD", "" + user_os);
				messageMail.put("@SiteId", AuthorizationPageBeanId.getSite_id());

				String sitePath = (String) request.getSession().getAttribute("site_path");
				String shopOrder = sitePath + File.separatorChar + "mail" + File.separatorChar + "ShopOrder.txt";
				String clientOrder = sitePath + File.separatorChar + "mail" + File.separatorChar + "ClientOrder.txt";
				String attachFile = sitePath + File.separatorChar + "mail" + File.separatorChar + "info.txt";

				System.out.println("path: " + shopOrder);

				AuthorizationPageBean ownerShop = authorizationPageFaced
						.getAuthorizationBeanOfRoleAdmin(AuthorizationPageBeanId.getSite_id());
				messageMail.put("@ShopEmail", ownerShop.getStrEMail());

				if (AuthorizationPageBeanId.getSite_id().compareTo("2") == 0) {
					orderFaced.setSaveWithOutDeductMoney(orderBeanId);
				} else {
					orderFaced.setSave(orderBeanId);
				}

				MessageSender mqSender = new MessageSender(request.getSession(), SendMailMessageBean.messageQuery);
				Message message = new Message();
				message.put("to", ownerShop.getStrEMail());
				message.put("subject",
						AuthorizationPageBeanId.getLocalization(servletContext).getString("order.order_number.text"));
				message.put("pathmessage", shopOrder);
				message.put("attachFile", attachFile);
				message.put("fields", messageMail);
				mqSender.send(message);

				message = new Message();
				message.put("to", AuthorizationPageBeanId.getStrEMail());
				message.put("mailfrom", ownerShop.getStrEMail());
				message.put("subject",
						AuthorizationPageBeanId.getLocalization(servletContext).getString("order.order_number.text"));
				message.put("pathmessage", clientOrder);
				message.put("attachFile", attachFile);
				message.put("fields", messageMail);
				mqSender.send(message);
				AuthorizationPageBeanId.setStrMessage("Order was not paid yet.");

				RequestDispatcher dispatcher = request.getRequestDispatcher("fdencrypting.jsp");
				dispatcher.forward(request, response);
			} else if (orderBeanId.getAction().compareTo("save") == 0
					&& AuthorizationPageBeanId.getStrLogin().compareTo("user") == 0) {
				AuthorizationPageBeanId.setStrMessage(
						AuthorizationPageBeanId.getLocalization(servletContext).getString("order.forpay.text"));
			}

		}

		orderBeanId.setProductList(orderFaced.getProducts(request, orderBeanId));
		orderBeanId.setEmpty_basket(orderFaced.getProductsListSize(request, orderBeanId) == 0);
		orderBeanId.setQuantity_product(orderFaced.getProductsListSize(request, orderBeanId));
		orderBeanId.setSelect_country(orderFaced.getXMLDBList("Order.jsp?country_id", "country",
				orderBeanId.getCountry_id(), "select country_id ,name from country   where  locale = '"
						+ AuthorizationPageBeanId.getLocale() + "' ","country_id","name"));
		orderBeanId.setSelect_city(orderFaced.getXMLDBList("Order.jsp?city_id", "city", orderBeanId.getCity_id(),
				"select  city_id , name  from  city where country_id =" + orderBeanId.getCountry_id()
						+ " and locale = '" + AuthorizationPageBeanId.getLocale() + "' ","city_id","name"));
		orderBeanId.setSelect_paystatus(
				orderFaced.getXMLDBList("Order.jsp?order_paystatus", "paystatus", orderBeanId.getorder_paystatus(),
						"select  paystatus_id , lable  from  paystatus  where  active  = true","paystatus_id","lable"));
		orderBeanId.setSelect_deliverystatus(orderFaced.getXMLDBList("Order.jsp?order_deliverystatus", "deliverystatus",
				orderBeanId.getDeliverystatus_id(),
				"select  deliverystatus_id , lable  from  deliverystatus  where  active  = true and lang = '"
						+ AuthorizationPageBeanId.getLocale() + "' ","deliverystatus_id","lable"));

		if (orderBeanId.getCity_id().equals("0"))
			orderBeanId.setCity_id(AuthorizationPageBeanId.getCity_id());
		if (orderBeanId.getCountry_id().equals("0"))
			orderBeanId.setCountry_id(AuthorizationPageBeanId.getCountry_id());
		if (orderBeanId.getContact_person().length() == 0)
			orderBeanId.setContact_person(
					AuthorizationPageBeanId.getStrFirstName() + " " + AuthorizationPageBeanId.getStrLastName());
		if (orderBeanId.getshipment_email().length() == 0)
			orderBeanId.setshipment_email(AuthorizationPageBeanId.getStrEMail());
		if (orderBeanId.getshipment_phone().length() == 0)
			orderBeanId.setshipment_phone(AuthorizationPageBeanId.getStrPhone());

		orderBeanId.setSelect_menu_catalog(orderFaced.getMenuXMLDBList("Productlist.jsp?catalog_id", "menu",
				AuthorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable , parent_id  from catalog   where  active = true and parent_id = 0 and site_id = "
						+ AuthorizationPageBeanId.getSite_id() + " and lang_id = "
						+ AuthorizationPageBeanId.getLang_id()
						+ " or parent_id in (select catalog_id   from catalog   where  active = true and site_id = "
						+ AuthorizationPageBeanId.getSite_id() + "  and parent_id = 0 )"));

	}

	public boolean isNumber(String tmp) {
		if (tmp == null)
			return false;
		if (tmp.length() == 0)
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
}
