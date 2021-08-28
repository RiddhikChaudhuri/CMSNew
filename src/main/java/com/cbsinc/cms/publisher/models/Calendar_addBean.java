package com.cbsinc.cms.publisher.models;

import java.io.Serializable;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;

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
@Component
public class Calendar_addBean implements Serializable {

	
	 private static final long serialVersionUID = 4879601106156013341L;

	 //static private Logger log = Logger.getLogger(Calendar_addBean.class);
	 private XLogger log = XLoggerFactory.getXLogger(Calendar_addBean.class.getName());

	private String query;

	private String holddate = "0";

	private String site_id = "0";

	private String calendar_id = "0";

	private Integer indx_select = 0;

	private String day_id = "0";

	private String mount_id = "0";

	private String year_id = "0";

	java.util.Calendar calendar;

	private String product_id = "";
	
	private String first_name = ""  ;
	private String last_name = ""  ;
	private String father_name = ""  ;
	private String document_number = ""  ;
	private String document_type = ""  ;
	private String age = ""  ;
	private String note = ""  ;
	

	transient ResourceBundle sequences_rs = null ;
	
	 @Autowired
	 TransactionSupport transactionSupport;
	  
	 
	 @Value("${calendar}")
	 private String calendar_string;
	 
	public Calendar_addBean() {
		calendar = java.util.Calendar.getInstance();
		day_id = "" + calendar.get(java.util.Calendar.DAY_OF_MONTH);
		mount_id = "" + (calendar.get(java.util.Calendar.MONTH) + 1);
		year_id = "" + calendar.get(java.util.Calendar.YEAR);
		if( sequences_rs == null )  sequences_rs = PropertyResourceBundle.getBundle("sequence");
	}

	public void addCalendar() {

		if (product_id == null || product_id.length() == 0)	return;
		calendar.set(Integer.parseInt(year_id), Integer.parseInt(mount_id) - 1,	Integer.parseInt(day_id));
		holddate = "" + calendar.getTimeInMillis();
		TransactionStatus transactionStatus = transactionSupport.beginTransaction();
		String query = "";
		query = calendar_string;
		try 
		{
		calendar_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
		query = "insert into calendar (calendar_id , soft_id , site_id , holddate  , active  ) "
				+ " values (? , ? , ? , ? , ?)"; 
		
		transactionSupport.getJdbcTemplate().update(query, calendar_id, product_id, site_id, holddate, true);

		transactionSupport.commitTransaction(transactionStatus);
		}
		catch (Exception ex) 
		{
			log.error(ex.getLocalizedMessage());
		}
		finally
        {
		  transactionSupport.rollbackTransaction(transactionStatus); 	
        }  
	}

	
	
	
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public int getIndx_select() {
		return indx_select;
	}

	public void setIndx_select(int indx_select) {
		this.indx_select = indx_select;
	}

	public String getCalendar_id() {
		return calendar_id;
	}

	public void setCalendar_id(String calendar_id) {
		this.calendar_id = calendar_id;
	}

	public String getHolddate() {
		return holddate;
	}

	public void setHolddate(String holddate) {
		this.holddate = holddate;
	}

	public String getDay_id() {
		return day_id;
	}

	public void setDay_id(String day_id) {
		this.day_id = day_id;
	}

	public String getMount_id() {
		return mount_id;
	}

	public void setMount_id(String mount_id) {
		this.mount_id = mount_id;
	}

	public String getYear_id() {
		return year_id;
	}

	public void setYear_id(String year_id) {
		this.year_id = year_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getDocument_number() {
		return document_number;
	}

	public void setDocument_number(String document_number) {
		this.document_number = document_number;
	}

	public String getDocument_type() {
		return document_type;
	}

	public void setDocument_type(String document_type) {
		this.document_type = document_type;
	}

	public String getFather_name() {
		return father_name;
	}

	public void setFather_name(String father_name) {
		this.father_name = father_name;
	}

	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

}
