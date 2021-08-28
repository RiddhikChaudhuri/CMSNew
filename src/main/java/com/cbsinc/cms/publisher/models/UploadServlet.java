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
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.postgresql.largeobject.LargeObject;
import org.postgresql.largeobject.LargeObjectManager;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;


/**
 * Servlet Class
 * 
 * @web.servlet name="uploadservlet" display-name="Name for Upload"
 *              description="Description for Upload"
 * @web.servlet-mapping url-pattern="/uploadservlet"
 * @web.servlet-init-param name="A parameter" value="A value"
 */
@WebServlet(urlPatterns = "/uploadservlet/*", loadOnStartup = 1)
public class UploadServlet extends HttpServlet {

	/**
	 * 
	 */

	//static private Logger log = Logger.getLogger(UploadServlet.class);
	private XLogger log = XLoggerFactory.getXLogger(UploadServlet.class.getName());
	

	private static final long serialVersionUID = 1L;

	PublisherBean publisherBeanId = null;

	AuthorizationPageBean AuthorizationPageBeanId = null;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	transient ResourceBundle resources = null;
	ServletContext servletContext = null;

	public String filename = "";

	public String fullpath_filename = "";

	public int intID = 0;

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

	transient ResourceBundle localization = null;
	transient ResourceBundle resources_ds = null;

	// Class[] columnTpyes = {};
	// DataSource ds = null;

	// private final static int BUFFER_SIZE = 2048;
	private final static int BUFFER_SIZE = 20480;

	/**
	 * Initializes the servlet.
	 */

	ResourceBundle sequences_rs = null;

	public UploadServlet() {
		if (sequences_rs == null)
			sequences_rs = PropertyResourceBundle.getBundle("sequence");
		if (resources_ds == null)
			resources_ds = PropertyResourceBundle.getBundle("application");
	}

	public void init(ServletConfig config) throws ServletException {
		servletContext = config.getServletContext();
		super.init(config);
	}

	public void OpenConnection_old(String url) {
		try {
			Class.forName("org.postgresql.Driver"); // PG7.0
			// conn = DriverManager.getConnection(url,"postgres","");
			conn = DriverManager.getConnection(url, "postgres", "postgres");
			stat = conn.createStatement();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			System.err.println("E: " + e);
			close();
		}
	}

	public void OpenConnection(String url) {
		try {
			Class.forName(resources_ds.getString("driver").trim()); // PG7.0
			Properties connProp = new Properties();
			connProp.put("user", resources_ds.getString("user").trim());
			connProp.put("password", resources_ds.getString("password").trim());
			conn = DriverManager.getConnection(resources_ds.getString("url").trim(), connProp);
			stat = conn.createStatement();

		} catch (Exception e) {
			log.error(e.getLocalizedMessage());
			System.err.println("E: " + e);
			close();
		}
	}

	public void OpenLObj() {
		try {
			// OpenConnection("jdbc:postgresql://192.168.0.1:5432/mobilsoft") ;
			// All LargeObject API calls must be within a transaction
			conn.setAutoCommit(false);
			// Get the Large Object Manager to perform operations with
			lobj = ((org.postgresql.PGConnection) conn).getLargeObjectAPI();
			// create a new large object
			oid = lobj.create(LargeObjectManager.READ | LargeObjectManager.WRITE);
			// open the large object for write
			obj = lobj.open(oid, LargeObjectManager.WRITE);
		} catch (java.lang.Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
	}

	public void writeLObj(byte[] buf, int off, int len) {
		try {
			// if( obj == null) return ;
			obj.write(buf, off, len);
		} catch (java.lang.Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}

	}

	public void saveLObj(String FileName) {
		try {
			if (FileName == null)
				return;
			if (FileName.length() == 0)
				return;
			obj.close();
			// int intID = 0 ;
			// String query = "SELECT NEXT VALUE FOR file_id_seq AS ID FROM ONE_SEQUENCES";
			String query = sequences_rs.getString("file");
			executeQuery(query);
			if (rows.size() != 0)
				intID = string2Integer("" + getValueAt(0, 0));
			// PreparedStatement ps = conn.prepareStatement("INSERT INTO file
			// VALUES (?, ? , ?)");
			PreparedStatement ps = conn.prepareStatement("INSERT INTO  file  VALUES (?, ? , ? , ?)");
			ps.setInt(1, intID);
			ps.setString(2, FileName);
			ps.setInt(3, oid);
			// +++
			ps.setLong(4, AuthorizationPageBeanId.getIntUserID());
			// ps.setInt(4,1);
			publisherBeanId.setFile_id("" + intID);
			// +++
			ps.executeUpdate();
			conn.commit();
			ps.close();
			// publisherBeanId = null
			return;
		} catch (java.lang.Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		// try{ conn.rollback(); } catch( java.sql.SQLException e){
		// System.out.println( e.toString() ); }
	}

	public void saveFileURL(String FileName, String path) {
		try {
			if (FileName == null)
				return;
			if (FileName.length() == 0)
				return;
			// String query = "SELECT NEXT VALUE FOR file_id_seq AS ID FROM ONE_SEQUENCES";
			String query = sequences_rs.getString("file");
			executeQuery(query);
			if (rows.size() != 0)
				intID = string2Integer("" + getValueAt(0, 0));
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO file (file_id, name, user_id, path) VALUES (?, ? , ? , ?)");
			ps.setInt(1, intID);
			ps.setString(2, FileName);
			ps.setLong(3, AuthorizationPageBeanId.getIntUserID());
			ps.setString(4, path);
			publisherBeanId.setFile_id("" + intID);
			ps.executeUpdate();
			conn.commit();
			ps.close();
			return;
		} catch (java.lang.Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
	}

	/**
	 * Destroys the servlet.
	 */
	public void destroy() {

	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request  servlet request
	 * @param response servlet response
	 */
	protected void processRequest(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, java.io.IOException {
		
		PrintWriter out = null ;
		
		try {
		
		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", req.getLocale());
		else if (!localization.getLocale().getLanguage().equals(req.getLocale().getLanguage()))
			localization = PropertyResourceBundle.getBundle("localization", req.getLocale());

		// OpenConnection("jdbc:postgresql://192.168.0.1:5432/mobilsoft") ;
		// localhost/mobilsoftutf8
		OpenConnection("jdbc:postgresql://127.0.0.1:5432/mobilsoftutf8");
		// // req.getParameter("")
		// req.
		// SessionBean sessionBean = (SessionBean)
		// request.getSession().getAttribute("sessionBean");
		publisherBeanId = (PublisherBean) req.getSession().getAttribute("publisherBeanId");
		AuthorizationPageBeanId = (AuthorizationPageBean) req.getSession().getAttribute("authorizationPageBeanId");

		if (resources == null)
			resources = PropertyResourceBundle.getBundle("localization", res.getLocale());
		

		long start = System.currentTimeMillis();
		// RFC 1867
		String contentType = req.getContentType();

		res.setContentType("text/html;charset=UTF8");
		// // res.addHeader("Expires","Mon, 26 Jul 1997 05:00:00 GMT");
		// res.addDateHeader("Last-Modified", new java.util.Date().getTime());
		// res.addHeader("Cache-Control","no-cache, must-revalidate");
		// res.addHeader("Pragma","no-cache");

		 out = res.getWriter();
		if (AuthorizationPageBeanId == null) {
			printResult(out, resources.getString("user_not_autorization"));
			return;
		}
		if (AuthorizationPageBeanId.getIntUserID() == 0) {
			printResult(out, resources.getString("user_not_autorization"));
			out.close();
			return;
		}
		if (AuthorizationPageBeanId.getIntLevelUp() != 2) {
			printResult(out, resources.getString("user_have_low_access"));
			out.close();
			return;
		}

		if (productPostAllFaced.isLimmitPostedMessages(AuthorizationPageBeanId, true)) {
			printResult(out, resources.getString("global_has_limmit_forsite"));
			out.close();
			return;
		}

		if (req.getParameter("filepath") != null) {
			fullpath_filename = req.getParameter("filepath");
			// String fileName =
			// fullpath_filename.substring(fullpath_filename.indexOf("\\"),
			// fullpath_filename.length() ) ;
			// int fnStart = 0 ;
			// int fnEnd = 0;
			int lastindex = -1;
			// filename = fullpath_filename.substring(fnStart+10, fnEnd);
			if ((lastindex = fullpath_filename.lastIndexOf('/')) < 0) {
				lastindex = fullpath_filename.lastIndexOf('\\');
			}
			if (lastindex >= 0) {
				filename = fullpath_filename.substring(lastindex + 1);
			}
			filename = processEscape(filename);

			saveFileURL(filename, fullpath_filename);
			printResultSaveFileURl(out, "URL: " + fullpath_filename);
			out.close();
			return;
		}

		if (contentType == null) {
			System.out.println("content type is null");
			return;
		}

		int ind = contentType.indexOf("boundary=");
		if (ind == -1) {
			System.out.println("IND is less than 0");
			// logger.info("Upload : IND is less than 0");
			return;
		}
		String boundary = contentType.substring(ind + 9);

		if (boundary == null) {
			System.out.println("boundary is null");
			// logger.info("Upload : boundary is null");
			return;
		}

		String boundaryString = "--" + boundary;
		ServletInputStream in = req.getInputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		HashMap map = new HashMap();
		int result = in.readLine(buffer, 0, BUFFER_SIZE);

		outer: while (true) {
			if (result <= 0) {
				System.out.println("Error. Stream truncated");
				// logger.info("Upload : stream truncated error.");
				break;
			}

			String line = new String(buffer, 0, result);

			if (!line.startsWith(boundaryString)) {
				System.out.println("Error. multipart boundary missing.");
				// logger.info("Upload : error. multipart boundary missing.");
				break;
			}

			// boundary end tag
			if (line.substring(boundaryString.length()).startsWith("--")) {
				// System.out.println("End of multipart");
				break;
			}

			result = in.readLine(buffer, 0, BUFFER_SIZE);
			if (result <= 0) {
				System.out.println("Upload : may be end boundary which has no contents");
				break;
			}

			line = new String(buffer, 0, result);
			// System.out.println("content disposition line = " + line);
			StringTokenizer tokenizer = new StringTokenizer(line, ";\r\n");
			String token = tokenizer.nextToken();
			String upperToken = token.toUpperCase();
			if (!upperToken.startsWith("CONTENT-DISPOSITION")) {
				System.out.println("Format error. Content-Disposition expected.");
				break;
			}
			String disposition = upperToken.substring(21);
			if (!disposition.equals("FORM-DATA")) {
				System.out.println("Sorry, I don't know how to handle [" + disposition + "] disposition.");
				break;
			}
			if (tokenizer.hasMoreElements()) {
				token = tokenizer.nextToken();
			} else {
				System.out.println("Format error. NAME expected.");
				break;
			}
			int nameStart = token.indexOf("name=\"");
			int nameEnd = token.indexOf("\"", nameStart + 7);
			if (nameStart < 0 || nameEnd < 0) {
				System.out.println("Format error. NAME expected.");
				break;
			}
			String name = token.substring(nameStart + 6, nameEnd);

			if (tokenizer.hasMoreElements()) {
				filename = null;
				int fnStart, fnEnd;
				// File file = null;
				String fileContentType = null;
				// FileOutputStream fout = null;
				int size = 0;

				fnStart = line.indexOf("filename=\"");
				if (fnStart < 0) { // filename term missing
					System.out.println("NO FILENAME given.");
					result = in.readLine(buffer, 0, BUFFER_SIZE);
					continue;
				}

				fnEnd = line.indexOf("\"", fnStart + 11);
				if (fnEnd < 0) {
					System.out.println("FILENAME is null.");
				} else {
					filename = line.substring(fnStart + 10, fnEnd);

					int lastindex = -1;
					if ((lastindex = filename.lastIndexOf('/')) < 0) {
						lastindex = filename.lastIndexOf('\\');
					}
					if (lastindex >= 0) {
						filename = filename.substring(lastindex + 1);
					}
					filename = processEscape(filename);
				}

				// System.out.println("receiving file named " + filename);
				/*
				 * if (filename != null) { String path = getValue(map, PATH_KEY);
				 * 
				 * file = new File(path); if (path != null && file.exists() &&
				 * file.isDirectory()) { file = new File(path, filename); } }
				 */

				result = in.readLine(buffer, 0, BUFFER_SIZE);
				if (result <= 0) {
					System.out.println("Error. Stream truncated");
					break;
				}
				fileContentType = new String(buffer, 0, result);
				if (fileContentType.toUpperCase().startsWith("CONTENT-TYPE:")) {
					fileContentType = fileContentType.substring(13).trim();
				} else {
					System.out.println("what should I read here ??? - result = " + result + ", and read ["
							+ new String(buffer, 0, result) + "]");
				}

				try {
					byte[] tmpbuffer1 = buffer;
					byte[] tmpbuffer2 = new byte[BUFFER_SIZE];
					byte[] tmpbuffer = tmpbuffer2;
					int tmpbufferlen = 0;
					boolean isFirst = true;
					boolean odd = true;
					inner: while ((result = in.readLine(buffer, 0, BUFFER_SIZE)) > 0) {
						if (isFirst) { // ignore all proceeding \r\n
							if (result == 2 && buffer[0] == '\r' && buffer[1] == '\n') {
								continue;
							}

							if (conn != null) {
								// ?�?�?�?�???????? ???????�?�???? ?????�?????�
								OpenLObj();
							}
						}

						if (bytesStartsWith(buffer, 0, result, boundaryString)) {
							if (!isFirst) {
								size += tmpbufferlen - 2;
								if (obj != null) {
									writeLObj(tmpbuffer, 0, tmpbufferlen - 2);
								}

							}
							continue outer;
						} else {
							if (!isFirst) {
								size += tmpbufferlen;
								if (obj != null) {
									writeLObj(tmpbuffer, 0, tmpbufferlen);
								}
							}
						}

						if (odd) {
							buffer = tmpbuffer2;
							tmpbuffer = tmpbuffer1;
						} else {
							buffer = tmpbuffer1;
							tmpbuffer = tmpbuffer2;
						}
						odd = !odd;

						tmpbufferlen = result;
						isFirst = false;
					}
				} catch (IOException ie) {
					log.error(ie.getLocalizedMessage());
					System.out.println("IO Error while write to file : " + ie.toString());
				} finally {
					System.out.println("Upload : size = " + size);
					if (obj != null) {
						// fout.close();
						saveLObj(filename.trim());
					}
					if (size > 0) {
						appendValue(map, name, filename, fileContentType, size);
					}
				}
				result = in.readLine(buffer, 0, BUFFER_SIZE);
				System.out.println("what should I read here? - result = " + result + ", and read ["
						+ new String(buffer, 0, result) + "]");
			} else { // no more elements
				result = in.readLine(buffer, 0, BUFFER_SIZE);
				if (result <= 0) {
					System.out.println("Error. Stream truncated");
					break;
				}
				result = in.readLine(buffer, 0, BUFFER_SIZE);
				if (result <= 0) {
					System.out.println("Error. Stream truncated");
					break;
				}
				String value = new String(buffer, 0, result - 2); // exclude
																	// \r\n
				appendValue(map, name, value);
			}

			result = in.readLine(buffer, 0, BUFFER_SIZE);
		} // end of while

		long end = System.currentTimeMillis();
		System.out.println("Good! it took " + (end - start) + " (ms)");

		printResult(out, map);
		//out.close();
		
		// response.setContentType("text/html");
		// java.io.PrintWriter out = response.getWriter();
		/*
		 * output your page here out.println("<html>"); out.println("<head>");
		 * out.println("<title>Servlet</title>"); out.println("</head>");
		 * out.println("<body>");
		 * 
		 * out.println("</body>"); out.println("</html>");
		 */
		}
		catch (Exception ex) {
			log.error(ex.getLocalizedMessage(),ex);
		}finally {
			if(out != null )out.close();
			close();
			
		}
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request  servlet request
	 * @param response servlet response
	 */
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

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Short description";
	}

	boolean bytesStartsWith(byte[] bytes, int offset, int length, String toCompare) {
		boolean result = true;
		if (toCompare.length() > length) {
			return false;
		}

		for (int i = toCompare.length() - 1; i >= 0; i--) {
			if (toCompare.charAt(i) != bytes[offset + i]) {
				result = false;
				break;
			}
		}

		return result;
	}

	void appendValue(HashMap map, String name, String value, String contentType, int size) {
		UploadData data = new UploadData(name, value, contentType, size, true);
		map.put(name, data);
	}

	void appendValue(HashMap map, String name, String value) {
		UploadData data = new UploadData(name, value, null, 0, false);
		map.put(name, data);
	}

	String getValue(HashMap map, String name) {
		UploadData data = (UploadData) map.get(name);
		if (data == null) {
			return null;
		}
		return data.value;
	}

	final static int NORMAL = 0;

	final static int AMPERSAND = 1;

	final static int AMPERSHARP = 2;

	/**
	 * process html escape characters (&#NNNN;)
	 */
	String processEscape(String string) {
		StringBuffer buffer = new StringBuffer(string.length());
		char[] chars = string.toCharArray();
		StringBuffer escaped = new StringBuffer(6);
		int status = NORMAL;

		for (int i = 0; i < string.length(); i++) {
			switch (status) {
			case NORMAL:
				if (chars[i] == '&') {
					status = AMPERSAND;
				} else {
					buffer.append(chars[i]);
				}
				break;

			case AMPERSAND:
				if (chars[i] == '#') {
					status = AMPERSHARP;
				} else {
					status = NORMAL;
					buffer.append('&');
				}
				break;

			case AMPERSHARP:
				if (chars[i] == ';') {
					try {
						buffer.append((char) Integer.parseInt(escaped.toString()));
					} catch (NumberFormatException nfe) {
						// I don't handle other Entities
						buffer.append(escaped);
						buffer.append(';');
					}
					escaped.setLength(0);
					status = NORMAL;
				} else {
					escaped.append(chars[i]);
				}
				break;
			}
		}

		if (escaped.length() > 0) {
			buffer.append(escaped);
		}

		return buffer.toString();
	}

	void printResult(PrintWriter out, Map map) throws IOException {
		Iterator itr = map.values().iterator();
		out.println("<HTML><HEAD>");
		out.println("<META http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<TITLE>" + localization.getString("result_upload") + "</TITLE>");
		out.println("<script language=\"JavaScript\">");
		out.println("<!--");
		out.println("function setData(){");
		out.println("opener.parent.postsoftform.filename.value = '" + filename + "' ;");
		out.println("opener.parent.postsoftform.file_id.value = " + intID + " ;");
		// out.println("top.close();");
		out.println("return true ;");
		out.println("}");
		out.println("function setClose(){");
		out.println("top.close() ;");
		out.println("return true ;");
		out.println("}");
		out.println("//-->");
		out.println("</script>");
		out.println("</HEAD><BODY onLoad=\"return  setData()\" >");
		out.println("<H1>" + localization.getString("result_upload") + "</H1>");
		out.println("<TABLE>");
		out.println(localization.getString("header_result_loading"));
		while (itr.hasNext()) {
			ItemData data = (ItemData) itr.next();
			out.println("<TR>");
			// out.println("<TD>" + (data.name == null ? "" : data.name) + "</TD>");
			out.println("<TD>" + (data.value == null ? "" : data.value) + "</TD>");
			out.println("<TD>" + (data.contentType == null ? "" : data.contentType) + "</TD>");
			out.println("<TD>" + (data.isFile ? String.valueOf(data.size) : "") + "</TD>");
			// out.println("<TD>" + (data.isFile ? "file" : "") + "</TD>");
			out.println("</TR>");
		}
		out.println("</TABLE>");
		out.println("<FORM><input type=\"button\" value=\"" + localization.getString("close_window")
				+ "\" onClick=\"return setClose()\" ></FORM>");
		out.println("</BODY></HTML>");
	}

	void printResult(PrintWriter out, String mess) throws IOException {
		out.println("<HTML><HEAD>");
		out.println("<META http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<TITLE>" + localization.getString("result_upload") + "</TITLE>");
		out.println("<script language=\"JavaScript\">");
		out.println("<!--");
		out.println("function setData(){");
		out.println("top.postsoftform.filename.value = '" + localization.getString("file_not_uploaded") + "' ;");
		// out.println("opener.parent.postsoftform.filename.value = 'file not
		// uploaded' ;" );
		out.println("top.postsoftform.file_id.value = -1 ;");
		// out.println("top.close();");
		out.println("return true ;");
		out.println("}");
		out.println("function setClose(){");
		out.println("top.dwindow('SelectImage.jsp');");
		out.println("return true ;");
		out.println("}");
		out.println("//-->");
		out.println("</script>");
		out.println("</HEAD><BODY onLoad=\"return  setData()\" >");
		out.println("<H1>" + localization.getString("result_upload") + "</H1>");
		out.println("<TABLE>");
		out.println(localization.getString("header_result_loading"));
		out.println("<TR>");
		out.println("<TD>" + localization.getString("file_not_uploaded") + "</TD>");
		out.println("<TD>value is null </TD>");
		out.println("<TD>content is null</TD>");
		out.println("<TD>size is null</TD>");
		out.println("<TD>" + mess + "</TD>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("<FORM><input type=\"button\" value=\"" + localization.getString("close_window")
				+ "\" onClick=\"return setClose()\" ></FORM>");
		out.println("</BODY></HTML>");
	}

	void printResultSaveFileURl(PrintWriter out, String mess) throws IOException {
		out.println("<HTML><HEAD>");
		out.println("<META http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<TITLE>Upload Result</TITLE>");
		out.println("<script language=\"JavaScript\">");
		out.println("<!--");
		out.println("function setData(){");
		out.println("top.postsoftform.filename.value = '" + localization.getString("result_upload") + "' ;");
		// out.println("opener.parent.postsoftform.filename.value = 'file not
		// uploaded' ;" );
		out.println("top.postsoftform.file_id.value = -1 ;");
		// out.println("top.close();");
		out.println("return true ;");
		out.println("}");
		out.println("function setClose(){");
		out.println("top.dwindow('SelectImage.jsp');");
		out.println("return true ;");
		out.println("}");
		out.println("//-->");
		out.println("</script>");
		out.println("</HEAD><BODY onLoad=\"return  setData()\" >");
		out.println("<H1>" + localization.getString("result_upload") + "</H1>");
		out.println("<TABLE>");
		out.println("<TR><TH> " + localization.getString("file_uploaded") + " </TH></TR>");
		out.println("<TR>");
		out.println("<TD>" + mess + "</TD>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("<FORM><input type=\"button\" value=\"" + localization.getString("close_window")
				+ "\" onClick=\"return setClose()\" ></FORM>");
		out.println("</BODY></HTML>");
	}

	/*
	 * public String getServletInfo() { return "A servlet that uploads files"; }
	 */

	public void close() {

		try {
			// ds.
			if (rs != null)
				rs.close();
			if (stat != null)
				stat.close();
			if (conn != null)
				conn.close();
		} catch (SQLException ex) {
			log.error(ex.getLocalizedMessage());
			System.err.println("Close DataBase :" + ex);
			// close() ;
		}

	}

	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	public void executeQuery(String query) {
		Qtable = query;
		if (conn == null || stat == null) {
			System.err.println("There is no database to execute the query.");
			return;
		}
		try {
			rs = stat.executeQuery(query);
			metaData = rs.getMetaData();

			int numberOfColumns = metaData.getColumnCount();
			columnNames = new String[numberOfColumns];
			for (int column = 0; column < numberOfColumns; column++) {
				columnNames[column] = metaData.getColumnLabel(column + 1);
			}

			// Get all rows.
			rows = new Vector();

			while (rs.next()) {
				String[] Doc = new String[getColumnCount()];

				for (int i = 1; i <= getColumnCount(); i++) {
					Doc[i - 1] = rs.getString(i);
					System.out.println(rs.getString(i)); // ++
				}
				rows.addElement(Doc);

			}

		} catch (SQLException ex) {
			log.error(ex.getLocalizedMessage());
			System.err.println(ex);
			close();
		}
	}

	public void executeUpdate(String query) {

		if (conn == null || stat == null) {
			System.err.println("There is no database to execute the query.");
			return;
		}
		try {
			stat.executeUpdate(query);
		} catch (SQLException ex) {
			log.error(ex.getLocalizedMessage());
			System.err.println(ex);
			close();
		}
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return rows.size();
	}

	public String getValueAt(int aRow, int aColumn) {
		String[] row = (String[]) rows.elementAt(aRow);
		return row[aColumn];
	}

	public int string2Integer(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = -1;
		}
		return i;
	}

}

class UploadData {
	String name;

	String value;

	String contentType;

	int size;

	boolean isFile;

	UploadData(String name, String value, String contentType, int size, boolean isFile) {
		this.name = name;
		this.value = value;
		this.contentType = contentType;
		this.size = size;
		this.isFile = isFile;
	}

}
