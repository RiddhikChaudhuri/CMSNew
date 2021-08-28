package com.cbsinc.cms.publisher.models;

import java.util.Calendar;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderBank implements java.io.Serializable {

	/**
	 * 
	 */
	transient private static final long serialVersionUID = -3147862933505181532L;

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

	//transient static private Logger log = Logger.getLogger(OrderBank.class);
	private XLogger logger = XLoggerFactory.getXLogger(OrderBank.class.getName());

	// Calendar startCalendar ;
	transient Calendar endCalendar;

	Integer intDayPeriod = 1;

	private String order_ID;

	private String beginData;

	private String endData;
	
	@Autowired
	public OrderBank() {
		endCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"), new Locale("ru"));
		logger.entry(endCalendar,endData);
		logger.exit();
		
		// new Locale("ru"));
	}

	public String getOrder_ID() {
		return order_ID;
	}

	public void setOrder_ID(String order_ID) {
		this.order_ID = order_ID;
	}

	public String getBeginData() {
		return beginData;
	}

	public String getBeginData_Day() {
		return tokenize(beginData, "-")[2];
	}

	public int getBeginData_intDay() {
		int intSDay = 0;
		String strSDay = (tokenize(beginData, "-")[2]).trim();
		if (isNumber(strSDay)) {
			intSDay = Integer.parseInt(strSDay);
		}
		return intSDay;
	}

	/*
	 * int intSDay = 0 ; String strSDay = (tokenize(beginData,"-")[2]).trim() ;
	 * if(isNumber(strSDay)){ intSDay = new Integer(strSDay).intValue() ;
	 */

	public String getEndData_Day() {
		int intEDay = 0;
		String strEDay = "0";
		// endCalendar.set( getBeginData_intYear(), getBeginData_intMonth(),
		// getBeginData_intDay() ) ;
		// endCalendar.add(endCalendar.DATE , intDayPeriod);
		try {
			intEDay = endCalendar.get(Calendar.DATE);
			strEDay = "" + intEDay;
			if (strEDay.length() == 1)
				strEDay = "0" + strEDay;
			// if(strEDay.length() = 1)strEDay = "0" + strEDay
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return strEDay;
	}

	public String getEndData_Month() {
		int intEMonth = 0;
		String strEMonth = "0";
		// endCalendar.set( getBeginData_intYear(), getBeginData_intMonth(),
		// getBeginData_intDay() ) ;
		try {
			int intEDay = endCalendar.get(Calendar.DATE);
			if (intEDay > getBeginData_intDay())
				endCalendar.add(Calendar.MONTH, 1);
			intEMonth = endCalendar.get(Calendar.MONTH);
			strEMonth = "" + intEMonth;
			if (strEMonth.length() == 1)
				strEMonth = "0" + strEMonth;
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return strEMonth;
		// return getBeginData_Month() ;
	}

	public String getEndData_Year() {
		/*
		 * int intEMonth = endCalendar.get(endCalendar.MONTH) ; if( intEMonth >
		 * getBeginData_intMonth() ) endCalendar.add(endCalendar.YEAR , 1); int intEYear
		 * = endCalendar.get(endCalendar.YEAR) ; return "" + intEYear ;
		 */
		return getBeginData_Year();
	}

	public String getBeginData_Month() {
		// return "" + startCalendar.get(startCalendar.MONTH) ;
		// return tokenize(beginData,"-")[1] ;
		return "" + getBeginData_intMonth();
	}

	public int getBeginData_intMonth() {
		int intSMonth = 0;

		try {
			String strSMonth = (tokenize(beginData, "-")[1]).trim();
			if (isNumber(strSMonth)) {
				intSMonth = Integer.parseInt(strSMonth);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return intSMonth - 1;
	}

	public String getBeginData_Year() {
		// return "" + startCalendar.get(startCalendar.YEAR) ;
		return tokenize(beginData, "-")[0];
	}

	public int getBeginData_intYear() {
		int intSYear = 0;
		String strSYear = "0";
		try {
			strSYear = (tokenize(beginData, "-")[0]).trim();
			if (isNumber(strSYear)) {
				intSYear = Integer.parseInt(strSYear);
			}
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		return intSYear;
	}

	public void setBeginData(String beginData) {
		this.beginData = beginData.substring(0, 10);

		try {
			endCalendar.set(getBeginData_intYear(), getBeginData_intMonth(), getBeginData_intDay());
			endCalendar.add(Calendar.DATE, intDayPeriod);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	public String getEndData() {
		return endData;
	}

	public void setEndData(String endData) {
		this.endData = endData.substring(0, 10);

	}

	public String[] tokenize(String s, String d) {
		String[] as = null;
		try {
			Vector vector = new Vector();
			for (StringTokenizer stringtokenizer = new StringTokenizer(s, d); stringtokenizer.hasMoreTokens(); vector
					.addElement(stringtokenizer.nextToken()))
				;

			as = new String[vector.size()];
			for (int i = 0; i < as.length; i++)
				as[i] = (String) vector.elementAt(i);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}

		return as;
	}

	public boolean isNumber(String tmp) {
		if (tmp == null)
			return false;
		String IntField = "0123456789.";
		for (int i = 0; i < tmp.length(); i++) {
			if (IntField.indexOf(tmp.charAt(i)) == -1)
				return false;
		}
		return true;
	}

}
