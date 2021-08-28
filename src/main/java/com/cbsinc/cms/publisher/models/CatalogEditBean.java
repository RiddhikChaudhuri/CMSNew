package com.cbsinc.cms.publisher.models;

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
public class CatalogEditBean implements java.io.Serializable {

	transient private static final long serialVersionUID = -6130230014231390789L;
	
	private XLogger logger = XLoggerFactory.getXLogger(CatalogEditBean.class.getName());


	private String query;

	private String name = "0";

	private Integer indx_select = 0;

	private String holddate = "0";
	transient ResourceBundle localization = null;
	
	@Autowired
	private TransactionSupport transactionSupport;

	public CatalogEditBean(Locale locale) {
		logger.entry(locale);
	      if (localization == null)
            localization = PropertyResourceBundle.getBundle("localization", locale);
	      logger.exit();
	}

	public CatalogEditBean() {
		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization");
		logger.exit();

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
	// "+localization.getString("edit_catalog") +"
	public String getEditForm(String catalog_id, String name, ResourceBundle localization) {

		StringBuffer buff = new StringBuffer();
		buff.append("<h1>" + localization.getString("edit_catalog") + " " + name + "</h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_edit\"  ACTION=\"ProductPostCre.jsp\" >\n");
		buff.append("<table>\n");
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"catalog_id\"  value = " + catalog_id + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " + name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"edit\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""
				+ localization.getString("save") + "\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");

		return buff.toString();
	}

	public String getEditUserCatalog(String catalog_id, String name) {

		StringBuffer buff = new StringBuffer();
		buff.append("<h1>" + localization.getString("edit_catalog") + " " + name + "</h1><br/> \n");
		buff.append("<div class='box'>\n");
		buff.append("<div class='body'>\n");
		buff.append("<div>\n");
		buff.append("<form method=\"post\"   name=\"catalog_edit\"  ACTION=\"ProductUserPost.jsp\" >\n");
		buff.append("<table>\n");
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"catalog_id\"  value = " + catalog_id + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " + name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"edit\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""
				+ localization.getString("save") + "\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");
		buff.append("</div>\n");

		return buff.toString();
	}

	public String getEditForm(String catalog_id, String name, String jspPage) {

		StringBuffer buff = new StringBuffer();
		buff.append("<form method=\"post\"   name=\"catalog_edit\"  ACTION=\"" + jspPage + "\" >\n");
		buff.append("<table>\n");
		buff.append("<tbody>\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"catalog_id\"  value = " + catalog_id + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"text\" name=\"name\"  value = " + name + " />\n");
		buff.append("<TR><TD></TD><TD><input type=\"hidden\" name=\"action\"  value = \"edit\"  />\n");
		buff.append("<TR><TD></TD><TD><input type=\"submit\" name=\"submit\"  value = \""
				+ localization.getString("save") + "\" />\n");
		buff.append("</tbody>\n");
		buff.append("</table>\n");
		buff.append("</form>\n");
		return buff.toString();
	}

	public XLogger getLogger() {
		return logger;
	}

	public void setLogger(XLogger logger) {
		this.logger = logger;
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

	public Integer getIndx_select() {
		return indx_select;
	}

	public void setIndx_select(Integer indx_select) {
		this.indx_select = indx_select;
	}

	public String getHolddate() {
		return holddate;
	}

	public void setHolddate(String holddate) {
		this.holddate = holddate;
	}

	public ResourceBundle getLocalization() {
		return localization;
	}

	public void setLocalization(ResourceBundle localization) {
		this.localization = localization;
	}

	public TransactionSupport getTransactionSupport() {
		return transactionSupport;
	}

	public void setTransactionSupport(TransactionSupport transactionSupport) {
		this.transactionSupport = transactionSupport;
	}

	

}
