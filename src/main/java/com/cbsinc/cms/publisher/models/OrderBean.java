package com.cbsinc.cms.publisher.models;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

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
public class OrderBean implements java.io.Serializable {

	transient private static final long serialVersionUID = 2520644735459719809L;

	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;

	private String type_id = "1";

	private Integer intLevelUp;

	private String phonetype_id = "1";

	private String progname_id = "1";

	private String imgname;

	private String image_id;

	private String img_url;

	private String action;

	private String order_id = "";

	private String user_id;

	private String city_id = "0";

	private String country_id = "0";

	private String order_currency_id = "0";

	private String delivery_amoun = "0";

	private String delivery_timeend = "";

	private String end_amount = "0";

	private String order_amount = "0";

	private String order_tax = "0";

	private String order_delivery_long = "0";

	private String order_paystatus = "0";

	private String product_name = "";

	private String product_description = "";

	private String cards_name = "";

	private String city_fullname = "";

	private String country_fullname = "";

	private String currency_lable = "";

	private String shipment_address = "";

	private String shipment_phone = "";

	private String contact_person = "";

	private String shipment_email = "";

	private String shipment_fax = "";

	private String shipment_description = "";

	private String city_name = "";

	private String country_name = "";

	private String country_telcode = "0";

	private String currency_rate = "0";

	private String product_cost = "0";

	private String product_weight = "0";

	private String product_count = "0";

	private String shipment_zip = "0";

	private String order_city_telcode = "";

	private String delivery_start = "";

	private String cdate = "";

	private String product_currency_cd = "";

	private String product_currency_lable = "";

	private String basket_id = "";

	private String paystatus_lable = "";

	private String account_history_id = "";

	private String imei = "0";

	private String phonemodel_id = "";

	private Integer pagecount = 0;

	private Float balans = Float.valueOf(0);

	private String strBalans = "0";
	private List<OrderBean> productList = new ArrayList<>();
	private String select_country = "";
	private String select_city = "";
	private String select_paystatus = "";

	private Boolean empty_basket = false;

	private Integer quantity_product = 0;

	public Boolean isInternet = true;

	private String deliverystatus_id = "0";

	private String select_deliverystatus = "";

	private String order_status = "";

	private String postOwnerId = "0";

	NumberFormat nf;

	public OrderBean() {
		nf = NumberFormat.getInstance();
		nf.setGroupingUsed(true);
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_id() {
		return type_id;
	}

	public void setIntLevelUp(int intLevelUp) {
		this.intLevelUp = intLevelUp;
	}

	public int getIntLevelUp() {
		return intLevelUp;
	}

	public void setPhonetype_id(String phonetype_id) {
		this.phonetype_id = phonetype_id;
	}

	public String getPhonetype_id() {
		return phonetype_id;
	}

	public void setProgname_id(String progname_id) {
		this.progname_id = progname_id;
	}

	public String getProgname_id() {
		return progname_id;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public String getImage_id() {
		return image_id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public void setUser_ID(String strUserID) {
		this.user_id = strUserID;
	}

	public String getUser_ID() {
		return user_id;
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

	public String getOrder_currency_id() {
		return order_currency_id;
	}

	public void setOrder_currency_id(String order_currency_id) {
		this.order_currency_id = order_currency_id;
	}

	public String getDelivery_amoun() {
		return delivery_amoun;
	}

	public void setDelivery_amoun(String delivery_amoun) {
		this.delivery_amoun = delivery_amoun;
	}

	public String getdelivery_timeend() {
		return delivery_timeend;
	}

	public void setdelivery_timeend(String delivery_timeend) {
		this.delivery_timeend = delivery_timeend;
	}

	public String getend_amount() {
		// if( end_amount.length() == 0 ) end_amount = "0" ;
		return end_amount;
	}

	public void setend_amount(String end_amount) {
		this.end_amount = end_amount;
	}

	public String getorder_amount() {
		// if( order_amount.length() == 0 ) order_amount = "0" ;
		return order_amount;
	}

	public void setorder_amount(String order_amount) {
		this.order_amount = order_amount;
	}

	public String getorder_tax() {
		// if( order_tax.length() == 0 ) order_tax = "0" ;
		return order_tax;
	}

	public void setorder_tax(String order_tax) {
		this.order_tax = order_tax;
	}

	public String getorder_delivery_long() {
		// if( order_delivery_long.length() == 0 ) order_delivery_long = "0" ;
		return order_delivery_long;
	}

	public void setorder_delivery_long(String order_delivery_long) {
		this.order_delivery_long = order_delivery_long;
	}

	public String getorder_paystatus() {
		return order_paystatus;
	}

	public void setorder_paystatus(String order_paystatus) {
		this.order_paystatus = order_paystatus;
	}

	/*
	 * public String getshipping_lable() { return shipping_lable; } public void
	 * setshipping_lable(String shipping_lable) { this.shipping_lable =
	 * shipping_lable; } public String getshipping_description() { return
	 * shipping_description; } public void setshipping_description(String
	 * shipping_description) { this.shipping_description = shipping_description; }
	 */
	public String getproduct_name() {
		return product_name;
	}

	public void setproduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getproduct_description() {
		return product_description;
	}

	public void setproduct_description(String product_description) {
		this.product_description = product_description;
	}

	public String getcards_name() {
		return cards_name;
	}

	public void setcards_name(String cards_name) {
		this.cards_name = cards_name;
	}

	public String getcity_fullname() {
		return city_fullname;
	}

	public void setcity_fullname(String city_fullname) {
		this.city_fullname = city_fullname;
	}

	public String getcountry_fullname() {
		return country_fullname;
	}

	public void setcountry_fullname(String country_fullname) {
		this.country_fullname = country_fullname;
	}

	public String getcurrency_lable() {
		return currency_lable;
	}

	public void setcurrency_lable(String currency_lable) {
		this.currency_lable = currency_lable;
	}

	public String getimg_url() {
		return img_url;
	}

	public void setimg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getshipment_address() {
		return shipment_address;
	}

	public void setshipment_address(String shipment_address) {
		this.shipment_address = shipment_address;
	}

	public String getshipment_phone() {
		return shipment_phone;
	}

	public void setshipment_phone(String shipment_phone) {
		this.shipment_phone = shipment_phone;
	}

	public String getContact_person() {
		return contact_person;
	}

	public void setContact_person(String contact_person) {
		this.contact_person = contact_person;
	}

	public String getshipment_email() {
		return shipment_email;
	}

	public void setshipment_email(String shipment_email) {
		this.shipment_email = shipment_email;
	}

	public String getshipment_fax() {
		return shipment_fax;
	}

	public void setshipment_fax(String shipment_fax) {
		this.shipment_fax = shipment_fax;
	}

	public String getshipment_description() {
		return shipment_description;
	}

	public void setshipment_description(String shipment_description) {
		this.shipment_description = shipment_description;
	}

	public String getcity_name() {
		return city_name;
	}

	public void setcity_name(String city_name) {
		this.city_name = city_name;
	}

	public String getcountry_name() {
		return country_name;
	}

	public void setcountry_name(String country_name) {
		this.country_name = country_name;
	}

	public String getcountry_telcode() {
		return country_telcode;
	}

	public void setcountry_telcode(String country_telcode) {
		this.country_telcode = country_telcode;
	}

	public String getcurrency_rate() {
		return currency_rate;
	}

	public void setcurrency_rate(String currency_rate) {
		this.currency_rate = currency_rate;
	}

	public String getproduct_cost() {
		return product_cost;
	}

	public void setproduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}

	public String getproduct_weight() {
		return product_weight;
	}

	public void setproduct_weight(String product_weight) {
		this.product_weight = product_weight;
	}

	public String getproduct_count() {
		return product_count;
	}

	public void setproduct_count(String product_count) {
		this.product_count = product_count;
	}

	public String getshipment_zip() {
		return shipment_zip;
	}

	public void setshipment_zip(String shipment_zip) {
		this.shipment_zip = shipment_zip;
	}

	public String getorder_city_telcode() {
		return order_city_telcode;
	}

	public void setOrder_city_telcode(String order_city_telcode) {
		this.order_city_telcode = order_city_telcode;
	}

	public String getdelivery_start() {
		return delivery_start;
	}

	public void setdelivery_start(String delivery_start) {
		this.delivery_start = delivery_start;
	}

	public String getcdate() {
		return cdate;
	}

	public void setcdate(String cdate) {
		this.cdate = cdate;
	}

	public String getProduct_currency_cd() {
		return product_currency_cd;
	}

	public void setProduct_currency_cd(String product_currency_cd) {
		this.product_currency_cd = product_currency_cd;
	}

	public String getProduct_currency_lable() {
		return product_currency_lable;
	}

	public void setProduct_currency_lable(String product_currency_lable) {
		this.product_currency_lable = product_currency_lable;
	}

	public String getListup() {
		return listup;
	}

	public void setListup(String listup) {
		this.listup = listup;
	}

	public String getListdown() {
		return listdown;
	}

	public void setListdown(String listdown) {
		this.listdown = listdown;

	}

	public String getPaystatus_lable() {
		return paystatus_lable;
	}

	public void setPaystatus_lable(String paystatus_lable) {
		this.paystatus_lable = paystatus_lable;
	}

	public String getTrueValue(String tmp1, String tmp2, boolean b) {
		if (b)
			return tmp1;
		else
			return tmp2;
	}

	public String getAccount_history_id() {
		return account_history_id;
	}

	public void setAccount_history_id(String account_history_id) {
		this.account_history_id = account_history_id;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getPhonemodel_id() {
		return phonemodel_id;
	}

	public void setPhonemodel_id(String phonemodel_id) {
		this.phonemodel_id = phonemodel_id;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public float getBalans() {
		return balans;
	}

	public void setBalans(float balans) {
		this.balans = balans;
	}

	public String getSelect_city() {
		return select_city;
	}

	public void setSelect_city(String select_city) {
		this.select_city = select_city;
	}

	public String getSelect_country() {
		return select_country;
	}

	public void setSelect_country(String select_country) {
		this.select_country = select_country;
	}

	public String getSelect_paystatus() {
		return select_paystatus;
	}

	public void setSelect_paystatus(String select_paystatus) {
		this.select_paystatus = select_paystatus;
	}

	public List<OrderBean> getProductList() {
		return productList;
	}

	public void setProductList(List<OrderBean> productList) {
		this.productList = productList;
	}

	public int getQuantity_product() {
		return quantity_product;
	}

	public void setQuantity_product(int quantity_product) {
		this.quantity_product = quantity_product;
	}

	public boolean isEmpty_basket() {
		return empty_basket;
	}

	public void setEmpty_basket(boolean empty_basket) {
		this.empty_basket = empty_basket;
	}

	public String getBasket_id() {
		return basket_id;
	}

	public void setBasket_id(String basket_id) {
		this.basket_id = basket_id;
	}

	public String getStrBalans() {
		strBalans = nf.format(balans);
		return strBalans;
	}

	public void setStrBalans(String strBalans) {
		balans = Float.parseFloat(strBalans);
		this.strBalans = strBalans;
	}

	public String getDeliverystatus_id() {
		return deliverystatus_id;
	}

	public void setDeliverystatus_id(String deliverystatus_id) {
		this.deliverystatus_id = deliverystatus_id;
	}

	public String getSelect_deliverystatus() {
		return select_deliverystatus;
	}

	public void setSelect_deliverystatus(String select_deliverystatus) {
		this.select_deliverystatus = select_deliverystatus;
	}

	public String getOrder_status() {
		return order_status;
	}

	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}

	public String getPostOwnerId() {
		return postOwnerId;
	}

	public void setPostOwnerId(String postOwnerId) {
		this.postOwnerId = postOwnerId;
	}

	private String select_menu_catalog;

	public String getSelect_menu_catalog() {
		return select_menu_catalog;
	}

	public void setSelect_menu_catalog(String select_menu_catalog) {
		this.select_menu_catalog = select_menu_catalog;
	}

}
