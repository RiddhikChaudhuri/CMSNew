package com.cbsinc.cms.publisher.models;

import java.util.*;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.cbsinc.cms.card.Rate;
import com.cbsinc.cms.card.RateInfo;


@Component
public class CkeckCurrency implements java.io.Serializable {

	transient private static final long serialVersionUID = 8904811168576411593L;

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

	//static private Logger log = Logger.getLogger(CkeckCurrency.class);
	private XLogger log = XLoggerFactory.getXLogger(CkeckCurrency.class.getName());

	final String const_strSHOP_ID = "84473";

	final String const_strLOGIN = "gvidon";

	final String const_strPASSWORD = "231003";

	public CkeckCurrency() {
		java.util.Timer timer = new java.util.Timer();
		//////////////////// timer.scheduleAtFixedRate(t, 0, 600000);
	}

	/*
	 * public void initCurrencyHash() { CurrencyHash.set("USD",
	 * getCurrency(getStrFullDate(), "USD")); CurrencyHash.set("EURO",
	 * getCurrency(getStrFullDate(), "EURO")); }
	 * 
	 * java.util.TimerTask t = new java.util.TimerTask() { public void run() { try {
	 * CurrencyHash.set("USD", getCurrency(getStrFullDate(), "USD"));
	 * CurrencyHash.set("EURO", getCurrency(getStrFullDate(), "EURO")); } catch
	 * (Exception ex1) { System.err.println("qq"); log.error(ex1); log.debug(ex1); }
	 * 
	 * } };
	 */
	java.util.Date getShortDate() {
		Calendar objCalendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"), new Locale("ru"));
		int intDay = objCalendar1.get(Calendar.DATE);
		int intEMonth = objCalendar1.get(Calendar.MONTH);
		int intYear = objCalendar1.get(Calendar.YEAR);
		objCalendar1.set(intYear, intEMonth, intDay, 0, 0);
		return objCalendar1.getTime();
	}

	String getStrFullDate() {
		Calendar objCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+3:00"), new Locale("ru"));
		int intDay = objCalendar.get(Calendar.DATE);
		String strDay = "" + intDay;
		if (strDay.length() == 1)
			strDay = "0" + strDay;

		int intMonth = objCalendar.get(Calendar.MONTH);
		String strMonth = "" + (intMonth + 1);
		if (strMonth.length() == 1)
			strMonth = "0" + strMonth;

		int intYear = objCalendar.get(Calendar.YEAR);
		String strYear = "" + intYear;

		return strDay + "." + strMonth + "." + strYear;
	}

	// Rate
	public Currency getCurrency(String strDATE, String strCURRENCY) {
		Currency objCur = new Currency();
		Rate rate = new Rate();
		RateInfo rateInfo;
		try {
			rateInfo = rate.getRateInfo(const_strSHOP_ID, const_strLOGIN, const_strPASSWORD, strCURRENCY, strDATE);
			if (rateInfo == null)
				return objCur;
			objCur.setCode(strCURRENCY);
			objCur.setDate(rateInfo.getdate());
			objCur.setRate(new Float(rateInfo.getrate()).floatValue());

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return objCur;
	}

}
