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

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;
import org.postgresql.PGConnection;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;

/**
 * Servlet Class
 * 
 * @web.servlet name="download" display-name="Name for Download"
 *              description="Description for Download"
 * @web.servlet-mapping url-pattern="/download"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
@WebServlet(urlPatterns = "/download/*", loadOnStartup = 1)
public class DownloadServlet extends HttpServlet {

	//static private Logger log = Logger.getLogger(DownloadServlet.class);
	private XLogger log = XLoggerFactory.getXLogger(DownloadServlet.class.getName());

	private static final long serialVersionUID = 1L;

	String strDevice = "";

	ProductlistBean ProductlistBeanId = null;

	AuthorizationPageBean AuthorizationPageBeanId = null;

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

	Vector rows = new Vector();

	String[] columnNames = {};
	ResourceBundle localization = null;

	public static ResourceBundle resources_ds;

	@Autowired
	TransactionSupport trxnSupport;
	// Class[] columnTpyes = {};

	// private static final String CONTENT_TYPE = "text/html;
	// charset=windows-1251";
	// private static final String CONTENT_TYPE = "application/x-zip-compressed;
	// ";
	// private static final String CONTENT_TYPE = "application/zip";
	// application/zip
	// application/x-zip-compressed
	// Initialize global variables
	public void init() throws ServletException {
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());
		else if (!localization.getLocale().getLanguage().equals(request.getLocale().getLanguage()))
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());

		if (request.getSession().getAttribute("productlistBeanId") instanceof ProductlistBean) {
			strDevice = "xml";
			ProductlistBeanId = (ProductlistBean) request.getSession().getAttribute("productlistBeanId");
		}
		AuthorizationPageBeanId = (AuthorizationPageBean) request.getSession().getAttribute("authorizationPageBeanId");
		String strRow = request.getParameter("row");
		if (strRow == null)
			row = 0;
		else
			row = Integer.parseInt(strRow);
		if (strDevice != null) {
			if ("xml".compareTo(strDevice) == 0) {
				soft_id = ProductlistBeanId.rows[row][0];
				file_id = ProductlistBeanId.rows[row][1];
			}
			if (file_id.length() == 0) {
				AuthorizationPageBeanId.setStrMessage("?\uFFFD???\uFFFD ?\uFFFD????????.");
				response.sendRedirect("Policy.jsp");
				return;
			}
			if (soft_id.length() == 0) {
				AuthorizationPageBeanId
						.setStrMessage("?\uFFFD???\uFFFD ?\uFFFD?????????? ???\uFFFD?????\uFFFD???\uFFFD??.");
				response.sendRedirect("Policy.jsp");
				return;
			}
		}
		filename = setFileNameByFile_ID(file_id);
		OpenConnection("jdbc:postgresql://127.0.0.1:5432/mobilsoftutf8");
		String CONTENT_TYPE = "application/octet-stream";
		String ext = "";
		ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
		ext = ext.toLowerCase();
		if (ext.compareTo("jar") == 0)
			CONTENT_TYPE = "application/java-archive";
		if (ext.compareTo("jad") == 0)
			CONTENT_TYPE = "text/vnd.sun.j2me.app-descriptor";
		if (ext.compareTo("wml") == 0)
			CONTENT_TYPE = "text/vnd.wap.wml";
		if (ext.compareTo("mid") == 0)
			CONTENT_TYPE = "audio/x-midi";
		if (ext.compareTo("midi") == 0)
			CONTENT_TYPE = "audio/x-midi";
		if (ext.compareTo("wbmp") == 0)
			CONTENT_TYPE = "image/vnd.wap.wbmp";
		if (ext.compareTo("wml") == 0)
			CONTENT_TYPE = "text/vnd.wap.wml";
		if (ext.compareTo("wmlc") == 0)
			CONTENT_TYPE = "application/vnd.wap.wmlc";
		if (ext.compareTo("wmlscriptc") == 0)
			CONTENT_TYPE = "application/vnd.wap.wmlscriptc";
		if (ext.compareTo("jpe") == 0)
			CONTENT_TYPE = "image/jpeg";
		if (ext.compareTo("jpeg") == 0)
			CONTENT_TYPE = "image/jpeg";
		if (ext.compareTo("jpg") == 0)
			CONTENT_TYPE = "image/jpeg";
		if (ext.compareTo("gif") == 0)
			CONTENT_TYPE = "image/gif";
		if (ext.compareTo("mov") == 0)
			CONTENT_TYPE = "video/quicktime";
		if (ext.compareTo("movie") == 0)
			CONTENT_TYPE = "video/x-sgi-movie";
		if (ext.compareTo("mp1") == 0)
			CONTENT_TYPE = "audio/x-mpeg";
		if (ext.compareTo("mp2") == 0)
			CONTENT_TYPE = "audio/x-mpeg";
		if (ext.compareTo("mp3") == 0)
			CONTENT_TYPE = "audio/x-mpeg";
		if (ext.compareTo("mp4") == 0)
			CONTENT_TYPE = "audio/x-mpeg";
		if (ext.compareTo("mpa") == 0)
			CONTENT_TYPE = "audio/x-mpeg";
		if (ext.compareTo("mpe") == 0)
			CONTENT_TYPE = "video/mpeg";
		if (ext.compareTo("mpeg") == 0)
			CONTENT_TYPE = "video/mpeg";
		if (ext.compareTo("mpega") == 0)
			CONTENT_TYPE = "audio/x-mpeg";
		if (ext.compareTo("mpg") == 0)
			CONTENT_TYPE = "video/mpeg";
		if (ext.compareTo("mpv2") == 0)
			CONTENT_TYPE = "video/mpeg2";
		if (ext.compareTo("divx") == 0)
			CONTENT_TYPE = "video/mpeg4";
		response.setContentType(CONTENT_TYPE);
		response.setHeader("Content-disposition",
				(new StringBuilder("attachment;filename=")).append(filename).toString());
		ServletOutputStream servletoutputstream = response.getOutputStream();
		if (ext.compareTo("jad") == 0) {
			String jad = getBObj(servletoutputstream, false);
			int sjad = jad.indexOf("MIDlet-Jar-URL:");
			String jad1 = jad.substring(0, sjad + "MIDlet-Jar-URL:".length());
			int ejad = jad.indexOf("MIDlet-Name:");
			String jad2 = jad.substring(ejad);
			String makejar = (new StringBuilder(String.valueOf(filename.substring(0, filename.length() - 1))))
					.append("r").toString();
			String midl_jar_url = (new StringBuilder(" midlets/")).append(makejar).append("\n").toString();
			jad = (new StringBuilder(String.valueOf(jad1))).append(midl_jar_url).append(jad2).toString();
			servletoutputstream.write(jad.getBytes());
		} else {
			getBObj(servletoutputstream, true);
		}
		servletoutputstream.flush();
		servletoutputstream.close();
		close();
		setPassiveRow(soft_id);
	}

	public void destroy() {
	}

	public String getBObj(OutputStream out, boolean check) {
		StringBuffer rez = new StringBuffer();
		try {
			ByteArrayInputStream bs = null;
			conn.setAutoCommit(false);
			LargeObjectManager lobj = ((PGConnection) conn).getLargeObjectAPI();
			PreparedStatement ps = conn.prepareStatement("SELECT filedata FROM file  WHERE  file_id=?");
			ps.setString(1, file_id);
			ResultSet rs = ps.executeQuery();
			if (rs != null) {
				LargeObject obj;
				for (; rs.next(); obj.close()) {
					int oid = rs.getInt(1);
					obj = lobj.open(oid, 0x40000);
					byte buf[] = new byte[obj.size()];
					obj.read(buf, 0, obj.size());
					bs = new ByteArrayInputStream(buf);
				}

				rs.close();
			}
			ps.close();
			conn.commit();
			if (check) {
				byte buf[] = new byte[2048];
				int tl = 0;
				if (bs == null)
					return null;
				int s;
				while ((s = bs.read(buf, 0, 2048)) > 0) {
					out.write(buf, 0, s);
					tl += s;
				}
			} else {
				if (bs == null)
					return null;
				for (int b = 0; (b = bs.read()) != -1;)
					rez.append(b);

			}
			bs.close();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		return rez.substring(0);
	}

	public void OpenConnection_old(String url) {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(url, "postgres", "postgres");
			stat = conn.createStatement();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			System.err.println(e);
			System.err.println((new StringBuilder()).append(getClass()).toString());
			System.err.println("Method OpenConnection()");
		}
	}

	public void OpenConnection(String url) {
		try {
			Class.forName(resources_ds.getString("driver").trim());
			Properties connProp = new Properties();
			connProp.put("user", resources_ds.getString("user").trim());
			connProp.put("password", resources_ds.getString("password").trim());
			conn = DriverManager.getConnection(resources_ds.getString("url").trim(), connProp);
			stat = conn.createStatement();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			System.err.println((new StringBuilder("E: ")).append(e).toString());
			close();
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
			log.error(ex.getLocalizedMessage());
			System.err.println(ex);
			System.err.println((new StringBuilder()).append(getClass()).toString());
			System.err.println("Method close()");
		}
	}

	public String getServletInfo() {
		return "Short description";
	}

	public String setFileNameByFile_ID(String File_ID) {
		String filename = "";
		String query = (new StringBuilder("select name  from file  where  file_id  = ")).append(File_ID).toString();
		try {
		  List<Map<String,Object>> fileList = trxnSupport.getJdbcTemplate().queryForList(query);
			if (fileList.size() != 0)
				filename = (String)fileList.get(0).get("name");
			log.error(query);

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());

		} 

		return filename;
	}

	public void setPassiveRow(String soft_id) {
		TransactionStatus trxnStatus =  trxnSupport.beginTransaction();
		String query = (new StringBuilder("UPDATE soft SET active = true  WHERE soft_id = ")).append(soft_id)
				.toString();
		try {
		  trxnSupport.getJdbcTemplate().update(query);
		  trxnSupport.commitTransaction(trxnStatus);
		}  catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			trxnSupport.rollbackTransaction(trxnStatus);
		} 

	}

}
