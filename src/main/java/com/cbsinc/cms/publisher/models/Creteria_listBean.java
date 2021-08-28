package com.cbsinc.cms.publisher.models;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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

public class Creteria_listBean implements java.io.Serializable  { 
	
	
	
	private static final long serialVersionUID = -9098863923171610711L;

	public String[][] rows = new String[10][2];

	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;

	private Integer intLevelUp = 0;

	private String cururl;
	

    @Autowired
    private TransactionSupport transSupport;

	//static private Logger log = Logger.getLogger(Creteria_addBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(Creteria_addBean.class.getName());
	

	private String row_id = "0";

	private Integer indx_select = 0;

	
	private String table_name =  "creteria1 " ;
	
	private Integer link_id  = 0 ; 
	
	
	private String title =  "" ;
	
	transient ListDataGet listDataGet = new ListDataGet();
	
	transient ResourceBundle localization = null ;
	
	public Creteria_listBean()
	{
	    logger.entry();
	    logger.exit();
	}
	
    public void initPage(String addstring) {
      logger.entry(addstring);
      List<Map<String, Object>> list = null;
      String query = "";
      query = "select " + table_name + "_id , name , label  FROM " + table_name
          + " WHERE active = true " + addstring + "  and ( link_id = 0 or link_id = " + link_id
          + " ) limit 10 offset " + offset;
      try {
        list = transSupport.getJdbcTemplate().queryForList(query);
        listDataGet.addList(list);

        if (listDataGet.getRowCount() > 0)
          title = listDataGet.getValueAt(0, 2);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }

    }
	
    public void setTitle(String label, String addstring) {
      logger.entry(label, addstring);
      TransactionStatus status = transSupport.beginTransaction();
      String query = "";
      // query = "select " + table_name + "_id , name , label FROM " + table_name + " WHERE active =
      // true " + addstring + " and ( link_id = 0 or link_id = " + link_id + " ) limit 10 offset " +
      // offset;
      query = "update " + table_name + " set label = '" + label + "' WHERE 0 = 0 " + addstring;
      this.title = title;
      try {
        transSupport.getJdbcTemplate().update(query);
        transSupport.commitTransaction(status);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        transSupport.rollbackTransaction(status);
      } finally {
        logger.exit();
      }
    }

    public String getPartCriteria(String _criteriaId, boolean isSpace) {
      logger.entry(_criteriaId, isSpace);
      try {
        if (Long.parseLong(_criteriaId) < 0)
          _criteriaId = "0";
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      }
      logger.exit();
      return !isSpace ? "" : " and catalog_id = " + _criteriaId;
    }

	public String getTable(Locale locale ) 
	{
		
		if( localization == null )  localization = PropertyResourceBundle.getBundle("localization", locale);
		
		cururl = "Creteria.jsp?offset=" + offset;
		listup = "Creteria.jsp?offset=" + (offset + 10);
		if (offset - 10 < 0)
			listdown = "Creteria.jsp?offset=0"; 
		else
			listdown = "Creteria.jsp?offset=" + (offset - 10); 

		StringBuffer table = new StringBuffer();
		table.append("<table class=\"columns\">\n");
		table.append("<tbody>\n");
		if (intLevelUp == 2) 
		{
			table.append("<TR BGCOLOR=\"#8CACBB\" >"
							+ "<TD WIDTH=\"10%\" >ID</TD>"
							+ "<TD WIDTH=\"70%\" >" + localization.getString("list_means_of_keywords")   + "  </TD>"
							+ "<TD WIDTH=\"20%\" ><a href =\"Creteria_add.jsp?link_id="+link_id+"\">" + localization.getString("add_creteria")   + " </a> </TD>"
							+ "</TR>\n");

	    } 
		else 
		{
			table.append("<TR BGCOLOR=\"#8CACBB\" >"
							+ "<TD WIDTH=\"10%\" >ID</TD>"
							+ "<TD WIDTH=\"70%\" >" + localization.getString("list_means_of_creteria")   + "  </TD>"
							+ "<TD WIDTH=\"20%\" ><a href =\"Creteria_add.jsp?link_id="+link_id+"\">" + localization.getString("add_creteria")   + " </a> </TD>"
							+ "</TR>\n");
		}

		if (listDataGet.getRowCount() < 10) 
		{
			table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>"
					+ "</TR>\n");
		} 
		else 
		{
			table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\""
					+ listup + "\">" + localization.getString("next_creteria")   + " 10</a>  </TD>" + "</TR>\n");
		}
		
		if (listDataGet.getRowCount()  > 0){ title = (String) listDataGet.getValueAt(0, 2); }
		
		for (int i = 0; listDataGet.getRowCount()  > i; i++) 
		{
			rows[i][0] = (String) listDataGet.getValueAt(i, 0);
			rows[i][1] = (String) listDataGet.getValueAt(i, 1);
			
			//urlParent = "<a href=\"Creteria.jsp?parent_id="+ rows[i][0] +"\" >"+rows[i][1]+"</a>";
			
			table.append("<TR>" + "<TD>" + rows[i][0] + "</TD>" + "<TD>"+ rows[i][1] + "</TD>"
					+ "<TD algin=\"rigth\" ><a href =\"Creteria_edit.jsp?row=" + i + "\">" + localization.getString("edit_creteria")   + "</a> </TD>"
					+ "<TD algin=\"rigth\" ><a href =\"Creteria.jsp?del=" + i + "\">" + localization.getString("del_creteria")   + "</a> </TD>" + "</TR>\n");
		}

		table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\""	+ listdown + "\">" + localization.getString("back_creteria")   + " 10</a>  </TD>" + "</TR>\n");
		table.append("</tbody>\n");
		table.append("</TABLE>\n");

		return table.toString();
	}


	
	
	 String getPageNumber() 
	 {
          if(offset == 0  )  return "1" ; 
          if(offset == 10  )  return "2" ;
          if(offset == 20  )  return "3" ;
          if(offset == 30  )  return "4" ;
          if(offset == 40  )  return "5" ;
          if(offset == 50  )  return "6" ;
          if(offset == 60  )  return "7" ;
          if(offset == 70  )  return "8" ;
          if(offset == 80  )  return "9" ;
          if(offset == 90  )  return "10" ;
          if(offset == 100  )  return "11" ;
          if(offset == 110  )  return "12" ;
          if(offset == 120  )  return "13" ;
          if(offset == 130  )  return "14" ;
          if(offset == 140  )  return "15" ;
          if(offset == 150  )  return "16" ;
          if(offset == 160  )  return "17" ;
          if(offset == 170  )  return "18" ;
          if(offset == 180  )  return "19" ;
          if(offset == 190  )  return "20" ;
          if(offset == 200  )  return "21" ;
		 return "0" ;
	 }

		
	
     public void delete(String catalog_id) {
       logger.entry(catalog_id);
       TransactionStatus status = transSupport.beginTransaction();
       if (catalog_id.startsWith("-") || catalog_id.equals("0"))
         return;

       String query = "";
       query = "delete FROM " + table_name + " WHERE  " + table_name + "_id = " + catalog_id;
       try {
         transSupport.getJdbcTemplate().update(query);
         transSupport.commitTransaction(status);
       } catch (Exception ex) {
         transSupport.rollbackTransaction(status);
         logger.error(ex.getLocalizedMessage(), ex);
       } finally {
         logger.exit();
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

	public void setIntLevelUp(int intLevelUp) {
		this.intLevelUp = intLevelUp;
	}

	public int getIntLevelUp() {
		return intLevelUp;
	}

	public String getCururl() {
		return cururl;
	}

	public void setCururl(String cururl) {
		this.cururl = cururl;
	}

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public int getIndx_select() {
		return indx_select;
	}

	public void setIndx_select(int indx_select) {
		this.indx_select = indx_select;
	}

	public String getTable_name() 
	{
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}



	public int getLink_id() {
		return link_id;
	}



	public void setLink_id(int link_id) {
		this.link_id = link_id;
	}



	public String getTitle() {
			
		return title;
	}



	public void setTitle(String title) {
		this.title = title;
	}

	
	

}
