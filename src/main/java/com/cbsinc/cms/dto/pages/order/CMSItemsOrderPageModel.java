package com.cbsinc.cms.dto.pages.order;

import java.util.List;

import com.cbsinc.cms.dto.Admin;
import com.cbsinc.cms.dto.CityItem;
import com.cbsinc.cms.dto.CountryItem;
import com.cbsinc.cms.dto.MenuItem;

public class CMSItemsOrderPageModel {

	String version;
	String name;
	String role_id;
	String title;
	Admin admin;

	String subject_site;
	String site_name;
	String host;
	String login;
	String passwdord;
	String message;
	String shoping_url;
	String balans;
	String shipment_phone;
	String contact_person;
	String shipment_email;

	String firstname;
	String lastname;
	String company;
	String email;
	String phone;
	String mphone;
	String fax;
	String icq;
	String website;
	String question;
	String answer;
	String countryId;
	String cityId;
	String site;

	List<ProductInCart> list;

	String empty_page;
	String empty_basket;
	String quantity_product;
	String offset;

	String next;
	String prev;

	String action;
	String imgname;
	String image_id;
	String img_url;
	String city_id;
	String country_id;
	String order_end_amount;
	String order_amount;
	String order_tax;
	String order_id;
	String order_currency_id;
	String order_paystatus;
	String order_status;
	String order_status_lable;
	String delivery_amoun;
	String delivery_timeend;
	String delivery_long;
	String delivery_start;
	String cards_name;
	String city_fullname;
	String country_fullname;
	String currency_lable;

	String shipment_address;
	String shipment_fax;
	String shipment_description;
	String city_name;
	String country_name;

	String country_telcode;
	String currency_rate;
	String city_telcode;
	String cdate;
	String paystatus_lable;

	String internet;

	List<MenuItem> menu;

	List<CountryItem> country;

	List<CityItem> city;

	List<PaystatusItem> paystatus;

	List<DeliverystatusItem> deliverystatus;

}
