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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;



@Component
@PropertySources(value = {
    @PropertySource(value="classpath:sequence.properties")
  })
public class Catalog_addBean implements java.io.Serializable  { 

	private static final long serialVersionUID = -7221967667229682339L;

	//static private Logger log = Logger.getLogger(Catalog_addBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(Catalog_addBean.class.getName());

	private String name = "0";

	private Integer indx_select = 0;

	private String holddate = "0";
	
	transient ResourceBundle localization = null ;
	
    @Value("${catalog}")
    private String sequence_catalog;
    
    @Autowired
    private TransactionSupport transactionSupport;
    
    public Catalog_addBean() {
      if (localization == null)
        localization = PropertyResourceBundle.getBundle("localization");
      logger.entry();
      logger.exit();

    }

    public Catalog_addBean(Locale locale) {
      if (localization == null)
        localization = PropertyResourceBundle.getBundle("localization", locale);
      logger.entry(locale);
      logger.exit();
    }
	
    public void addCatalog(AuthorizationPageBean authorizationPageBeanId) {
      logger.entry(authorizationPageBeanId);
      String query = "";
      query = sequence_catalog;
      TransactionStatus transactionStatus = transactionSupport.beginTransaction();

      try {
        String catalog_id =
            transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
        query =
            "insert into catalog (catalog_id , parent_id , site_id , tax , lable , lang_id ,active ) "
                + " values ( ? , ? , ? , ? , ? , ? , ? ) ";
        transactionSupport.getJdbcTemplate().update(query, Long.valueOf(catalog_id),
            Long.valueOf(authorizationPageBeanId.getCatalog_parent_id()),
            Long.valueOf(authorizationPageBeanId.getSite_id()), 1, name,
            authorizationPageBeanId.getLang_id(), true);
        transactionSupport.commitTransaction(transactionStatus);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        transactionSupport.rollbackTransaction(transactionStatus);
      } finally {
        logger.exit();
      }

      return;
    }

	
	public String  getAddForm( String _name  )
	{
		
		//localization.getString("add_catalog") 
		 
		StringBuffer buff = new StringBuffer();
		buff.append("<form method=\"post\"   name=\"catalog_add\"  ACTION=\"ProductPostCre.jsp\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"add\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " +  _name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		return buff.toString();
	}	
	

	public String  getAddFormWhere( String title , ResourceBundle localization )
	{
		
		StringBuffer buff = new StringBuffer();
		
		buff.append("<h1>"+localization.getString("add_catalog") +" "+title+"</h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_add\"  ACTION=\"ProductPostCre.jsp\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"add\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		return buff.toString();
	}	
	
	
	public String  getAddUserCatalog( String title )
	{
		
		
		 
		StringBuffer buff = new StringBuffer();
		
		buff.append("<h1>"+localization.getString("add_catalog") +" "+title+"</h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_add\"  ACTION=\"ProductUserPost.jsp\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"add\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		return buff.toString();
	}	
	
	
	
	public String  getAddForm()
	{
		
		
		 
		StringBuffer buff = new StringBuffer();
		
		buff.append("<h1>"+localization.getString("add_catalog") +" </h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_add\"  ACTION=\"ProductPostCre.jsp\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"add\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("save") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		return buff.toString();
	}	
	
	public String  getAddFormWithJsp(String jspPage)
	{
		StringBuffer buff = new StringBuffer();
		buff.append("<form method=\"post\"   name=\"catalog_add\"  ACTION=\""+jspPage+"\" >\n");
		buff.append("<table>\n");	
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"add\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""+localization.getString("add_catalog") +"\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		return buff.toString();
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
