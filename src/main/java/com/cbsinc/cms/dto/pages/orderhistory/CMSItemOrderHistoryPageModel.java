package com.cbsinc.cms.dto.pages.orderhistory;

import java.util.List;

import com.cbsinc.cms.dto.MenuItem;

public class CMSItemOrderHistoryPageModel {
	
	   String version; 
	   String name ;

	   String title ;
	   String subject_site; 
	   String site_name;
	   String host;
	   String login;
	   String role_id ;
	   String passwdord ;
	   String shoping_url; 
	   String message;
	   String balans;
	   String datefrom_formated;
	   String dateto_formated;
	   String datefrom;
	   String dateto;
	   String date_format;
	   
	   List<HistoryOrder> list ;

	   String  next;
	   String prev;

	   List<MenuItem> menu;

}
