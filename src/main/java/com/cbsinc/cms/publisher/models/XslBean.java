package com.cbsinc.cms.publisher.models;

import java.io.File;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
public class XslBean extends WebControls implements
		java.io.Serializable {
	
  @Autowired
  TransactionSupport transactionSupport;

	private static final long serialVersionUID = 1148781987253439919L;

	//static private Logger log = Logger.getLogger(XslBean.class);
	
	private XLogger log = XLoggerFactory.getXLogger(XslBean.class.getName());
	
	private String xsl_subj_id = "";

	private String xsl_style_id = "";

	private String dirname = "";

	public String getUserDir(HttpServletRequest req) {
		File file = null;
		StringBuffer table = new StringBuffer();

		String path = this.getClass().getResource("").getPath();
		path = path.substring(0, path.indexOf("/WEB-INF/"));

		file = new File(path + "//xsl//" + req.getServerName());

		if (!file.exists())
			file.mkdir();
		File[] filesName = file.listFiles();

		for (int i = 0; i < filesName.length; i++) {
			table.append("<BR><STRONG><U><FONT color=\"#000099\">"
					+ filesName[i].getName()
					+ "....................................."
					+ new java.util.Date(filesName[i].lastModified())
					+ "</FONT></U></STRONG>\n");
		}

		return table.toString();
	}

	public String getXsl_subj_id() {
		return xsl_subj_id;
	}

	public void setXsl_subj_id(String xsl_subj_id) {
		this.xsl_subj_id = xsl_subj_id;
	}

	public String getXsl_style_id() {
		return xsl_style_id;
	}

	public void setXsl_style_id(String xsl_style_id) {
		this.xsl_style_id = xsl_style_id;
	}

	public String getDirname() {
		return dirname;
	}

	public void setDirname(String dirname) {
		this.dirname = dirname;
		// validate dirname
		if (!isXsl_subj_idChange(xsl_style_id))
			this.dirname = getFirstDirnameByXsl_subj_id(xsl_subj_id);
		// setDirname(xslBeanId.getDirnameBy( xslBeanId.getXsl_style_id() )) ;
	}

	public String getDir(HttpServletRequest req, String folder) {
		// / if(!isXsl_subj_idChange(xsl_style_id))return "";
		// SELECT public.xsl_style.xsl_style_id, public.xsl_style.name FROM
		// public.xsl_style WHERE public.xsl_style.active = true AND
		// public.xsl_style.xsl_subj_id = "+ xslBeanId.getXsl_subj_id()
		// if(!isXsl_subj_idChange(xsl_style_id)) folder =
		// getFirstDirnameByXsl_subj_id (xsl_subj_id) ;

		File file = null;
		StringBuffer table = new StringBuffer();

		String path = this.getClass().getResource("").getPath();
		path = path.substring(0, path.indexOf("/WEB-INF/"));

		file = new File(path + "//xsl//" + folder);

		if (!file.exists())
			file.mkdir();
		File[] filesName = file.listFiles();

		for (int i = 0; i < filesName.length; i++) {
			table.append("<BR><STRONG><U><FONT color=\"#000099\">"
					+ filesName[i].getName()
					+ "....................................."
					+ new java.util.Date(filesName[i].lastModified())
					+ "</FONT></U></STRONG>\n");
		}

		return table.toString();
	}

	public boolean isXsl_subj_idChange(String xsl_style_id) {
		boolean b = true;
		String subj_id = "-1";
		// /String query = "SELECT public.xsl_style.dirname FROM
		// public.xsl_style WHERE public.xsl_style.xsl_style_id = " +
		// xsl_style_id ;
		String query = "SELECT  public.xsl_style.xsl_subj_id FROM public.xsl_style WHERE   public.xsl_style.xsl_style_id = "
				+ xsl_style_id;

		// Adp.BeginTransaction();
		try {
			List<Map<String,Object>> xslList = transactionSupport.getJdbcTemplate().queryForList(query);		

		if (xslList.size() != 0)
			subj_id = (String) xslList.get(0).get("public.xsl_style.xsl_subj_id");

		if (xsl_subj_id.compareTo(subj_id) != 0)
			b = false;
		}
		catch (Exception ex) 
		{
			log.error(ex.getLocalizedMessage());
		}
		return b;
	}

	public String getFirstDirnameByXsl_subj_id(String subj_id) {
		String folder = "";
		String query = "SELECT  public.xsl_style.dirname   FROM  public.xsl_style WHERE   public.xsl_style.active = true  AND public.xsl_style.xsl_subj_id = "
				+ subj_id;
		// String query = "SELECT public.xsl_style.xsl_subj_id FROM
		// public.xsl_style WHERE public.xsl_style.xsl_style_id = "+
		// xsl_style_id ;
		// Adp.BeginTransaction();
		try {
		  List<Map<String,Object>> xslList = transactionSupport.getJdbcTemplate().queryForList(query); 
		

		if (xslList.size() != 0)
			folder = (String) xslList.get(0).get("public.xsl_style.dirname");

	}
	catch (Exception ex) 
	{
		log.error(ex.getLocalizedMessage());
	}
	

		return folder;
	}

	public String getDirnameBy(String xsl_style_id) {
		String dirname = "default";
		String query = "SELECT  public.xsl_style.dirname FROM  public.xsl_style  WHERE  public.xsl_style.xsl_style_id = "
				+ xsl_style_id;

		// Adp.BeginTransaction();
		try {
		  List<Map<String,Object>> xslList = transactionSupport.getJdbcTemplate().queryForList(query); 
		

		  if (xslList.size() != 0)
		    dirname = (String) xslList.get(0).get("public.xsl_style.dirname");

	} 
	catch (Exception ex) 
	{
		log.error(ex.getLocalizedMessage());
	}
		// this.dirname = dirname;
		return dirname;

	}

	public void initFields(String site_id) {
		if (xsl_subj_id.length() == 0 && xsl_style_id.length() == 0
				&& dirname.length() == 0) {
			String query = "SELECT  public.xsl_style.xsl_style_id, public.xsl_style.dirname, public.xsl_style.xsl_subj_id, public.xsl_style.site_id FROM public.xsl_style WHERE public.xsl_style.site_id = "
					+ site_id;
		
			// Adp.BeginTransaction();
			try {
	          List<Map<String,Object>> xslList = transactionSupport.getJdbcTemplate().queryForList(query); 
			

			if (xslList.size() != 0) {
				xsl_style_id =(String) xslList.get(0).get("public.xsl_style.xsl_style_id");
				dirname =(String) xslList.get(0).get("public.xsl_style.dirname");
				xsl_subj_id = (String) xslList.get(0).get("public.xsl_style.xsl_subj_id");
			}
			}
		catch (Exception ex) 
		{
			log.error(ex.getLocalizedMessage());
		}

		}
	}

	public void addDir(String site_id) {
		if (xsl_subj_id.length() == 0 && xsl_style_id.length() == 0
				&& dirname.length() == 0) {
			String query = "SELECT  public.xsl_style.xsl_style_id, public.xsl_style.dirname, public.xsl_style.xsl_subj_id, public.xsl_style.site_id FROM public.xsl_style WHERE public.xsl_style.site_id = "
					+ site_id;
		
			// Adp.BeginTransaction();
			try 
			{
			  List<Map<String,Object>> xslList = transactionSupport.getJdbcTemplate().queryForList(query); 
			

				if (xslList.size() != 0) 
				{
				  xsl_style_id =(String) xslList.get(0).get("public.xsl_style.xsl_style_id");
	                dirname =(String) xslList.get(0).get("public.xsl_style.dirname");
	                xsl_subj_id = (String) xslList.get(0).get("public.xsl_style.xsl_subj_id");
				}
			}
			catch (Exception ex) 
			{
				log.error(ex.getLocalizedMessage());
			}
			

		

		}
	}

}
