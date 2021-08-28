package com.cbsinc.cms.publisher.models;

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
import com.cbsinc.cms.publisher.dao.TransactionSupport;

/**
 * Servlet Class
 * 
 * @web.servlet name="downloadservletbyrowid" display-name="Name for
 *              DownloadServletByRowId" description="Description for
 *              DownloadServletByRowId"
 * @web.servlet-mapping url-pattern="/downloadservletbyrowid"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
@WebServlet(urlPatterns = "/downloadservletbyrowid/*", loadOnStartup = 1)
public class DownloadServletByRowIdRederect extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private XLogger log = XLoggerFactory.getXLogger(DownloadServletByRowIdRederect.class.getName());
	

	String strDevice = "";

	@Autowired
	AuthorizationPageBean AuthorizationPageBeanId;

	String file_id = "1"; // "unknown.zip" ;

	String soft_id = "1"; // "unknown.zip" ;

	int row = 0;

	// LargeObject obj = null ;
	// LargeObjectManager lobj = null ;
	// int oid = 0 ;

	String Qtable = "";

	Vector rows = new Vector();

	String[] columnNames = {};

	// Class[] columnTpyes = {};

	/////// ProductlistBean ProductlistBeanId = null;

	String type_page = "";


	transient ResourceBundle localization = null;
	String basePath = "";
	
	@Autowired
    TransactionSupport trxnSupport;


	public void init() throws ServletException {

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

		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", response.getLocale());
		else if (!localization.getLocale().equals(response.getLocale()))
			localization = PropertyResourceBundle.getBundle("localization", response.getLocale());

		if (request.getParameter("productid") != null)
			soft_id = request.getParameter("productid");
		AuthorizationPageBeanId = (AuthorizationPageBean) request.getSession().getAttribute("authorizationPageBeanId");

		if (strDevice != null) {
			if (soft_id == null || soft_id.length() == 0) {
				AuthorizationPageBeanId.setStrMessage("We are requied autorization on site for download files .");
				response.sendRedirect("Policy.jsp");
				return;
			}
		}

		// FileDownload fileDownload = setFileNameByFile_ID(file_id);
		FileDownload fileDownload = setFileNameByProductId(soft_id);
		response.setHeader("Content-disposition", "attachment;filename=" + fileDownload.getName());
		if (fileDownload.getPath() != null && fileDownload.getPath().length() > 0) {

			String path = request.getContextPath();
			basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path
					+ "/";
			String file_ext = fileDownload.getName().substring(fileDownload.getName().lastIndexOf("."));
			String fileurl = basePath + "//files//" + fileDownload.getFile_id() + file_ext;
			// ((HttpServletRequest) request).getRequestDispatcher(fileurl).forward(
			// request, response) ;
			response.sendRedirect(fileurl);
			AuthorizationPageBeanId.setStrMessage(
					fileDownload.getName() + " " + localization.getString("download_servlet_by_order.has_downloaded"));
			return;
		}

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
          List<Map<String,Object>> resultList =  trxnSupport.getJdbcTemplate().queryForList(query);

			if (resultList.size() != 0) {
				fileDownload.setName((String) resultList.get(0).get("name"));
				fileDownload.setPath((String) resultList.get(0).get("path"));
			}

		}catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return fileDownload;
	}

	public FileDownload setFileNameByProductId(String productId) {
		FileDownload fileDownload = new FileDownload();
		String query = "select file.name , file.path , file.file_id from soft LEFT  JOIN file  ON  soft.file_id = file.file_id   where  soft.soft_id  = "
				+ productId;
		try {
          List<Map<String,Object>> resultList =  trxnSupport.getJdbcTemplate().queryForList(query);

			if (resultList.size() != 0) {
				fileDownload.setName((String)resultList.get(0).get("file.name"));
				fileDownload.setPath((String)resultList.get(0).get("file.path"));
				fileDownload.setFile_id((String)resultList.get(0).get("file.file_id"));
			}

		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return fileDownload;
	}

	public void setPassiveRow(String soft_id) {
      TransactionStatus trxnStatus =  trxnSupport.beginTransaction();
		String query = "UPDATE soft SET active = true  WHERE soft_id = " + soft_id;
		// select 0 AS test ;
		try {
          trxnSupport.getJdbcTemplate().update(query);
          trxnSupport.commitTransaction(trxnStatus);
        } catch (Exception ex) {
          trxnSupport.rollbackTransaction(trxnStatus);
          log.error(ex.getLocalizedMessage());
      } 
        return;
	}

}
