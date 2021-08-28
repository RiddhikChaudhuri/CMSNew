package com.cbsinc.cms.publisher.models;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.TransactionSupport;

/**
 * Servlet Class
 * 
 * @web.servlet name="downloadservletbyodrder" display-name="Name for
 *              DownloadServletByOdrder" description="Description for
 *              DownloadServletByOdrder"
 * @web.servlet-mapping url-pattern="/downloadservletbyodrder"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
@WebServlet(urlPatterns = "/downloadservletbyodrder/*", loadOnStartup = 1)
public class DownloadServletByOdrder extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// static private Logger log = Logger.getLogger(DownloadServletByOdrder.class);
	private XLogger log = XLoggerFactory.getXLogger(DownloadServletByOdrder.class.getName());

	String strDevice = "";

	OperationAmountBean OperationAmountBeanId = null;

	AuthorizationPageBean authorizationPageBeanId = null;

	String file_id = "1"; // "unknown.zip" ;

	String soft_id = "1"; // "unknown.zip" ;

	String order_id = "0"; // "unknown.zip" ;

	String filename = "unknown.zip";

	int row = 0;

	/// Connection conn = null;

	// Statement stat = null;

	// ResultSet rs = null;

	// ResultSetMetaData metaData = null;

	String Qtable = "";

	Vector rows = new Vector();

	String[] columnNames = {};

	// Class[] columnTpyes = {};
	// DataSource ds = null;

	transient ResourceBundle resources = null;

	long limmit = 0; // ?�???????�???? ?????????? ?�?????�???�?�

	long downloadzise = 0; // ?�???????�???? ?�?????�????????

	transient ResourceBundle setup_resources = null;

	@Autowired
	AuthorizationPageFaced authorizationPageFaced;
	transient ResourceBundle localization = null;

	@Autowired
	TransactionSupport trxnSupport;

	public void init() throws ServletException {
		if (resources == null)
			resources = PropertyResourceBundle.getBundle("download");

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

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {
		TransactionStatus trxnStatus = trxnSupport.beginTransaction();
		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());
		else if (!localization.getLocale().getLanguage().equals(request.getLocale().getLanguage()))
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());

		// ////////limmit = getLimmit( request) ;
		if (setup_resources == null)
			setup_resources = PropertyResourceBundle.getBundle("ApplicationResources", response.getLocale());
		else if (!setup_resources.getLocale().getLanguage().equals(response.getLocale().getLanguage()))
			setup_resources = PropertyResourceBundle.getBundle("ApplicationResources", request.getLocale());

		if (request.getSession().getAttribute("productlistBeanId") instanceof ProductlistBean) {
			strDevice = "xml";
		}

		OperationAmountBeanId = (OperationAmountBean) request.getSession().getAttribute("operationAmountBeanId");
		authorizationPageBeanId = (AuthorizationPageBean) request.getSession().getAttribute("authorizationPageBeanId");

		if (authorizationPageFaced.getBalans(authorizationPageBeanId.getIntUserID()) < 0) {
			authorizationPageBeanId.setStrMessage(setup_resources.getString("download_servlet_by_order.low_money"));
			response.sendRedirect("Order.jsp");
			return;
		}

		String basket_id = request.getParameter("basket_id");
		if (basket_id == null) {
			row = 0;
		} else {
			row = Integer.parseInt(basket_id);
		}

		if (strDevice != null) {
			if ("xml".compareTo(strDevice) == 0) {
				String query = "select  product_id  , order_id from  basket where basket_id = " + basket_id;
				try {
					List<Map<String, Object>> baskList = trxnSupport.getJdbcTemplate().queryForList(query);
					if (baskList.size() != 0) {
						soft_id = (String) baskList.get(0).get("product_id"); // + " " +
						order_id = (String) baskList.get(0).get("order_id"); // + " " +
					} else
						throw new NullPointerException("select  product_id   from  basket where basket_id = "
								+ basket_id + " : Exception product_id = null ");
					query = "select  file_id   from  soft where soft_id = " + soft_id;
					List<Map<String, Object>> fileList = trxnSupport.getJdbcTemplate().queryForList(query);
					if (fileList.size() != 0)
						file_id = (String) fileList.get(0).get("file_id"); // + " " +
					else
						throw new NullPointerException("select  file_id   from  soft where soft_id = " + soft_id
								+ " : Exception file_id = null ");
				} catch (Exception ex) {
					log.error(ex.getLocalizedMessage());
				}
			}

		}
		FileDownload fileDownload = setFileNameByFile_ID(file_id);
		filename = fileDownload.getName();
		// OpenConnection();
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
		response.setHeader("Content-disposition", "attachment;filename=" + filename);
		java.nio.channels.WritableByteChannel strout = java.nio.channels.Channels
				.newChannel(response.getOutputStream());
		java.io.FileInputStream fInStreem = null;
		java.nio.channels.FileChannel infileChannel = null;
		java.nio.ByteBuffer buff = null;
		if (fileDownload.getPath() != null && fileDownload.getPath().length() > 0) {

			try {
				fInStreem = new java.io.FileInputStream(fileDownload.getPath());
				infileChannel = fInStreem.getChannel();
				buff = java.nio.ByteBuffer.allocate(2048);
				long count = infileChannel.size();
				while (count > 0) {
					// ///////if(limmit < downloadzise) break ;
					count = count - 2048;
					downloadzise = downloadzise + 2048;
					infileChannel.read(buff);
					buff.rewind();
					strout.write(buff);
					buff.rewind();
				}

				/// setPassiveRow(soft_id);
				authorizationPageBeanId.setStrMessage(
						filename + " " + setup_resources.getString("download_servlet_by_order.has_downloaded"));
				setDeliveryStatus(DeliveryStatus.ORDER_DELIVERED, order_id);
			} catch (Exception e) {
				setDeliveryStatus(DeliveryStatus.ORDER_NOT_DELIVERED, order_id);
				log.error(e.getLocalizedMessage());
			} finally {
				if (infileChannel != null)
					infileChannel.close();
				if (fInStreem != null)
					fInStreem.close();
				if (strout != null)
					strout.close();
				if (buff != null)
					buff.clear();
			}

			return;
		}

		/*
		 * if (ext.compareTo("jad") == 0) { String jad = getBObj(strout, false); //
		 * servletoutputstream = response.getOutputStream(); int sjad =
		 * jad.indexOf("MIDlet-Jar-URL:"); String jad1 = jad.substring(0, sjad +
		 * "MIDlet-Jar-URL:".length()); int ejad = jad.indexOf("MIDlet-Name:"); String
		 * jad2 = jad.substring(ejad); String makejar = filename.substring(0,
		 * filename.length() - 1) + "r"; String midl_jar_url = " midlets/" + makejar +
		 * "\n"; jad = jad1 + midl_jar_url + jad2; java.nio.ByteBuffer buff1 =
		 * java.nio.ByteBuffer.wrap(jad.getBytes()); strout.write(buff1); } else {
		 * getBObj(strout, true);
		 * 
		 * } close();
		 */
		// setPassiveRow(soft_id);
		/// authorizationPageBeanId.setStrMessage(filename + " " +
		// setup_resources.getString("download_servlet_by_order.has_downloaded"));
	}

	// Clean up resources
	public void destroy() {

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

	public FileDownload setFileNameByFile_ID(String File_ID) {
		FileDownload fileDownload = new FileDownload();
		fileDownload.setFile_id(File_ID);
		String query = "select name , path  from file  where  file_id  = " + File_ID;
		try {
			List<Map<String, Object>> list = trxnSupport.getJdbcTemplate().queryForList(query);

			if (list.size() != 0) {
				fileDownload.setName((String) list.get(0).get("name"));
				fileDownload.setPath((String) list.get(0).get("path"));
			}

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		return fileDownload;
	}

	public void setPassiveRow(String soft_id) {
		TransactionStatus trxnStatus = trxnSupport.beginTransaction();
		String query = "UPDATE soft SET active = true  WHERE soft_id = " + soft_id;
		try {
			trxnSupport.getJdbcTemplate().update(query);
			trxnSupport.commitTransaction(trxnStatus);
		} catch (Exception ex) {
			trxnSupport.rollbackTransaction(trxnStatus);
			log.error(ex.getLocalizedMessage());
		}

		return;
	}

	public void setDeliveryStatus(long deliverystatus_id, String order_id) {
		TransactionStatus trxnStatus = trxnSupport.beginTransaction();

		String query = "UPDATE ORDERS SET deliverystatus_id = " + deliverystatus_id + "  WHERE ORDER_ID = " + order_id;

		try {
			trxnSupport.getJdbcTemplate().update(query);
			trxnSupport.commitTransaction(trxnStatus);
		} catch (Exception ex) {
			trxnSupport.rollbackTransaction(trxnStatus);
			log.error(ex.getLocalizedMessage());
		}
		return;
	}

	long getLimmit(HttpServletRequest request) {

		long size = 0;
		String key = "*";
		Enumeration enumeration;
		try {
			enumeration = resources.getKeys();
			while (enumeration.hasMoreElements()) {
				key = (String) enumeration.nextElement();
				if (request.getRemoteHost().compareTo(key) == 0) {
					size = Long.parseLong(resources.getString(key));
					System.out.println("download mask for remote host " + request.getRemoteHost());
					System.out.println("user from remote host " + key);
					System.out.println("limmit size for remote host " + size);
					return size;
				}
			}

			enumeration = resources.getKeys();
			while (enumeration.hasMoreElements()) {
				key = (String) enumeration.nextElement();
				if (request.getRemoteHost().startsWith(key)) {
					size = Long.parseLong(resources.getString(key));
					System.out.println("download mask for remote host " + request.getRemoteHost());
					System.out.println("user from remote host " + key);
					System.out.println("limmit size for remote host " + size);
					return size;
				}
			}

			size = Long.parseLong(resources.getString(key));
			System.out.println("download mask for remote host " + request.getRemoteHost());
			System.out.println("user from remote host " + key);
			System.out.println("limmit size for remote host " + size);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			ex.printStackTrace();
		}

		return size;
	}

}
