package com.cbsinc.cms.publisher.models;
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

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;





@Component
public class Catalog_editBean implements java.io.Serializable { 
	
	transient private static final long serialVersionUID = -6130230014231390789L;
	private XLogger logger = XLoggerFactory.getXLogger(Catalog_editBean.class.getName());

	private String query;

	private String name = "0";

	private Integer indx_select = 0;

	private String holddate = "0";
	
	transient ResourceBundle localization = null ;
	
	@Autowired
    private TransactionSupport transactionSupport;
	
	public Catalog_editBean(Locale locale) {
			if( localization == null )  localization = PropertyResourceBundle.getBundle("localization", locale);
	}

	public Catalog_editBean() {
		if( localization == null )  localization = PropertyResourceBundle.getBundle("localization");

	}
	
    public void editCatalog(AuthorizationPageBean authorizationPageBeanId) {
      logger.entry(authorizationPageBeanId);
      TransactionStatus status = transactionSupport.beginTransaction();
      String query = "";
      query = "update catalog set  lable = ? , lang_id = ?  where catalog_id = "
          + authorizationPageBeanId.getCatalog_id() + " and site_id = "
          + authorizationPageBeanId.getSite_id();

      try {
        transactionSupport.getJdbcTemplate().update(query, name,
            authorizationPageBeanId.getLang_id());
        transactionSupport.commitTransaction(status);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        transactionSupport.rollbackTransaction(status);
      } finally {
        logger.exit();
      }


      return;
    }

	
	
//edit_catalog
	//"+localization.getString("edit_catalog") +"
	public String  getEditForm( String catalog_id  , String name , ResourceBundle localization  )
	{
		

		StringBuffer buff = new StringBuffer();
		buff.append("<h1>"+localization.getString("edit_catalog") +" "+ name +"</h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_edit\"  ACTION=\"ProductPostCre.jsp\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"catalog_id\"  value = " +  catalog_id + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " +  name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"edit\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");

		return buff.toString();
	}	
   
	
	public String  getEditUserCatalog( String catalog_id  , String name  )
	{
		
		
		
		StringBuffer buff = new StringBuffer();
		buff.append("<h1>"+localization.getString("edit_catalog") +" "+ name +"</h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_edit\"  ACTION=\"ProductUserPost.jsp\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"catalog_id\"  value = " +  catalog_id + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " +  name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"edit\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");

		return buff.toString();
	}	
	
	
	public String  getEditForm( String catalog_id  , String name , String jspPage )
	{
		
		
		
		StringBuffer buff = new StringBuffer();
		buff.append("<form method=\"post\"   name=\"catalog_edit\"  ACTION=\""+jspPage+"\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"catalog_id\"  value = " +  catalog_id + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " +  name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"edit\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		return buff.toString();
	}	
	
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public int getIndx_select() {
		return indx_select;
	}

	public void setIndx_select(int indx_select) {
		this.indx_select = indx_select;
	}

	public String getHolddate() {
		return holddate;
	}

	public void setHolddate(String holddate) {
		this.holddate = holddate;
	}



	

}
