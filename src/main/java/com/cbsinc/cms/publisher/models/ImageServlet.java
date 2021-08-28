package com.cbsinc.cms.publisher.models;

import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 * Servlet Class
 * 
 * @web.servlet name="imageservlet" display-name="Name for Image"
 *              description="Description for Image"
 * @web.servlet-mapping url-pattern="/imageservlet"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
@WebServlet(urlPatterns = "/imageServlet/*", loadOnStartup = 1)
public class ImageServlet extends HttpServlet {

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

    @Autowired
    TransactionSupport transactionSupport;
  
	//static private Logger log = Logger.getLogger(ImageServlet.class);
	private XLogger logger = XLoggerFactory.getXLogger(ImageServlet.class.getName());

	private static final long serialVersionUID = 1L;

	ProductlistBean productlistBeanId = null;

	String file_id = "1"; // "unknown.zip" ;

	String soft_id = "1"; // "unknown.zip" ;

	String filename = "unknown.zip";

	int row = 0;

	LargeObject obj = null;

	LargeObjectManager lobj = null;

	int oid = 0;

	Connection conn = null;

	Statement stat = null;

	ResultSet rs = null;

	ResultSetMetaData metaData = null;

	String Qtable = "";
	String[] columnNames = {};
	transient ResourceBundle localization = null;

	// DataSource ds = null;

	public void init() throws ServletException {
	  logger.entry();
	  logger.exit();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request  servlet request
	 * @param response servlet response
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		processRequest(request, response);
	}

	// Process the HTTP Get request
	// public void doGet(HttpServletRequest request, HttpServletResponse
	// response) throws ServletException, IOException {
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
	    logger.entry(request,response);
		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());
		else if (!localization.getLocale().getLanguage().equals(request.getLocale().getLanguage()))
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());

		productlistBeanId = (ProductlistBean) request.getSession().getAttribute("productlistBeanId");
		String strRow = request.getParameter("row");
		if (strRow == null) {
			row = 0;
		} else {
			row = Integer.parseInt(strRow);
		}

		soft_id = productlistBeanId.rows[row][0];
		file_id = productlistBeanId.rows[row][1];
		filename = setFileNameByFile_ID(file_id);

		OpenConnection("jdbc:postgresql://127.0.0.1:5432/mobilsoft");

		response.setContentType("image/jpeg");
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		javax.servlet.ServletOutputStream servletoutputstream = response.getOutputStream();
		getBObj(servletoutputstream);
		servletoutputstream.flush();
		servletoutputstream.close();
		close();
		logger.exit();
	}

	public void destroy() {
	}

	public void getBObj(OutputStream out) {
		try {
			PreparedStatement ps = conn.prepareStatement("SELECT img FROM images WHERE image_id=?");
			ps.setString(1, file_id);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					byte[] imgBytes = rs.getBytes(1);
					out.write(imgBytes);
				}
				rs.close();
			}
			ps.close();
			// bs.close();
		} catch (java.lang.Exception e) {
			logger.error(e.getLocalizedMessage(),e);
			System.out.println(e.toString());
		}
	}

	public void OpenConnection(String url) {
		try {
			Class.forName("org.postgresql.Driver"); // PG7.0
			// conn = DriverManager.getConnection(url,"postgres","");
			conn = DriverManager.getConnection(url, "Grabko", "");
			stat = conn.createStatement();

		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			System.err.println("E: " + e);
		}
	}

	public void close() {

		try {
			if (rs != null)
				rs.close();
			if (stat != null)
				stat.close();
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {
			logger.error(ex.getLocalizedMessage());
			System.err.println("Close DataBase :" + ex);
		}

	}

	// protected void finalize() throws Throwable {
	// close();
	// super.finalize();
	// }

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}

    public String setFileNameByFile_ID(String File_ID) {
      String filename = "";

      String query = "select name  from file  where  file_id  = " + File_ID;
      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        filename = (String) rows.get(0).get("name");
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage());
      } finally {
        logger.exit();
      }

      return filename;
    }

    public void setPassiveRow(String soft_id) {
      logger.entry(soft_id);
      // String strID ;
      String query = "UPDATE soft SET active = true  WHERE soft_id = " + soft_id;
      TransactionStatus transactionStatus = transactionSupport.beginTransaction();
      try {
        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(transactionStatus);
      } catch (Exception ex) {
        transactionSupport.rollbackTransaction(transactionStatus);
        logger.error(ex.getLocalizedMessage());
      } finally {
        logger.exit();
      }

      return;
    }

}
