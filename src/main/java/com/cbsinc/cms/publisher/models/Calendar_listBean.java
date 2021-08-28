package com.cbsinc.cms.publisher.models;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;

import com.cbsinc.cms.publisher.dao.TransactionSupport;
import com.cbsinc.cms.publisher.dao.WebControls;

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
public class Calendar_listBean extends WebControls implements
		java.io.Serializable {

	

	 private static final long serialVersionUID = -3430180944717439685L;

	 //private static  Logger log = Logger.getLogger(Calendar_listBean.class);
	 private XLogger log = XLoggerFactory.getXLogger(Calendar_listBean.class.getName());

	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;


	private String cururl;

	private String calendar_id = "-1";

	private String site_id = "2";

	private String row_id = "0";

	private Integer indx_select = 0;

	// Access sample property
	// Access sample property

	private String mount_id = "0";

	private String year_id = "0";

	transient java.util.Calendar calendar;

	String holddate_from = "0";

	String holddate_to = "0";

	private String product_id = "";

	private String descrition = "";
	
	transient ServletContext applicationContext ;
	
	@Autowired
	TransactionSupport transactionSupport;
	
	public Calendar_listBean() {
		calendar = java.util.Calendar.getInstance();
		mount_id = "" + (calendar.get(java.util.Calendar.MONTH) + 1);
		year_id = "" + calendar.get(java.util.Calendar.YEAR);
	}

	public void init_calendar(String holddate) {
		calendar.setTimeInMillis(Long.parseLong(holddate));
	}

	public String calendar_day(String holddate) {
		calendar.setTimeInMillis(Long.parseLong(holddate));
		return "" + calendar.get(java.util.Calendar.DAY_OF_MONTH);
		
	}

	public String calendar_date(String holddate) {
		calendar.setTimeInMillis(Long.parseLong(holddate));
		return "" + calendar.getTime();
	}

	public void calendar_change() {

		calendar.set(Integer.parseInt(year_id), Integer.parseInt(mount_id) - 1,
				1);
		holddate_from = "" + calendar.getTimeInMillis();
		System.out.println(calendar.getTime());
		if (Integer.parseInt(mount_id) > 11)
			calendar.set(Integer.parseInt(year_id) + 1, 1, 1);
		else
			calendar.set(Integer.parseInt(year_id), Integer.parseInt(mount_id),
					1);
		holddate_to = "" + calendar.getTimeInMillis();
		System.out.println(calendar.getTime());
	}

	public String getTable(int intLevelUp) {

		cururl = "calendar_list.jsp?offset=" + offset;

		listup = "calendar_list.jsp?offset=" + (offset + 10); 
		if (offset - 10 < 0)
			listdown = "calendar_list.jsp?offset=0"; 
		else
			listdown = "calendar_list.jsp?offset=" + (offset - 10);

		StringBuffer table = new StringBuffer();
		

		String query = "";
		query = "select calendar_id ,  holddate   FROM calendar WHERE active = true and holddate > "
				+ holddate_from
				+ " and holddate < "
				+ holddate_to
				+ "  and soft_id = "
				+ product_id
				+ " limit 10 offset "
				+ offset;

		try 
		{
  			List<Map<String,Object>> calendarList = transactionSupport.getJdbcTemplate().queryForList(query);
  			String calendar_id;
  			String holddate;
  			String holday;
  			String holCalendarDate;

		table.append("<table class=\"columns\">\n");
		table.append("<tbody>\n");
		if (intLevelUp == 2) {
			table
					.append("<TR BGCOLOR=\"#8CACBB\" >"
							+ "<TD WIDTH=\"10%\" >№ </TD>"
							+ "<TD WIDTH=\"70%\" >Число  </TD>"
							+ "<TD WIDTH=\"20%\" ><a href =\"calendar_add.jsp\">добавить</a> </TD>"
							+ "</TR>\n");

			// "<TD height=\"*\" border=\"0\" >" + this.getComboBox("type_id",
			// "" + type_id ,"select type_id , type_lable from typesoft where
			// active = true") + "<input type=\"submit\" name=\"Submit\"
			// value=\"OK\"></TD>" +
			// postManager
		} else {
			table.append("<TR BGCOLOR=\"#8CACBB\" >"
					+ "<TD WIDTH=\"10%\" >№ </TD>"
					+ "<TD WIDTH=\"70%\" >Число  </TD>"
					+ "<TD WIDTH=\"20%\" ></TD>" + "</TR>\n");
		}

		if (calendarList.size() < 10) {
			table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>"
					+ "</TR>\n");
		} else {
			table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\""
					+ listup + "\">след 10</a>  </TD>" + "</TR>\n");
		}
		for (int i = 0; calendarList.size() > i; i++) {
		    calendar_id = (String) calendarList.get(i).get("calendar_id");
		    holddate =  (String) calendarList.get(i).get("holddate");
		    holday = calendar_day(holddate);
		    holCalendarDate = calendar_date(holddate);

			if (intLevelUp == 2) {
				table
						.append("<TR>"
								+ "<TD  >"
								+ calendar_id
								+ "</TD>"
								+ "<TD  >"
								+ holday
								+ " ( "
								+ holCalendarDate
								+ " ) </TD>"
								+ "<TD algin=\"rigth\" ><a href =\"calendar_edit.jsp?row="
								+ i
								+ "\">редактировать</a> </TD>"
								+ "<TD algin=\"rigth\" ><a href =\"calendar_list.jsp?del="
								+ i + "\">удалить</a> </TD>" + "</TR>\n");
			} else {
				table.append("<TR>" + "<TD>" + calendar_id + "</TD>" + "<TD>"
						+ holday + " ( " + holCalendarDate + " ) </TD>"
						+ "<TD algin=\"rigth\" ></TD>"
						+ "<TD algin=\"rigth\" ></TD>" + "</TR>\n");
			}

		}

		table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\""
				+ listdown + "\">назад 10</a>  </TD>" + "</TR>\n");
		table.append("</tbody>\n");
		table.append("</TABLE>\n");
		}
		catch (Exception ex) 
		{
			log.error(ex.getLocalizedMessage());
		}
		
		return table.toString();
	}

	public void delete(String calendar_id) {
		TransactionStatus trxnStatus = transactionSupport.beginTransaction();
		String query = "";
		query = "delete FROM calendar WHERE site_id = " + site_id + " and calendar_id = " + calendar_id;
		try 
		{
		  transactionSupport.getJdbcTemplate().update(query);
		  transactionSupport.commitTransaction(trxnStatus);
		}
		catch (Exception ex) 
		{
			log.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
			
		} 
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public int stringToInt(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = 0;
		}
		return i;
	}

	public boolean setSelectedDemand() {
		return true;
	}

	public boolean setPassiveDemand() {
		return true;
	}

}
