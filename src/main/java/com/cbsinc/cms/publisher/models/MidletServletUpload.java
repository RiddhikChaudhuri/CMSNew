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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.dao.TransactionSupport;

/**
 * Servlet Class
 * 
 * @web.servlet name="midletservletupload" display-name="Name for
 *              MidletServletUpload" description="Description for
 *              MidletServletUpload"
 * @web.servlet-mapping url-pattern="/midletservletupload"
 * @web.servlet-init-param name="A parameter" value="A value"
 */

@WebServlet(urlPatterns = "/midletServlet/*", loadOnStartup = 1)
public class MidletServletUpload extends HttpServlet {
	/**
	 * 
	 */

	//static private Logger log = Logger.getLogger(MidletServletUpload.class);
	private XLogger log = XLoggerFactory.getXLogger(MidletServletUpload.class.getName());

	private static final long serialVersionUID = 1L;

	PublisherBean publisherBeanId = null;

	AuthorizationPageBean AuthorizationPageBeanId = null;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	transient ResourceBundle resources = null;
	ServletContext servletContext = null;

	public String filename = "";

	public int intID = 0;

	// java.io.PipedOutputStream bs = null ;
	// java.io.PipedInputStream pin = null ;

	FileOutputStream bs = null;

	// java.io.ByteArrayInputStream dd = new java.io.ByteArrayInputStream( ;
	// java.io.

	// java.io.PipedOutputStream outs = null ;

	private final static int BUFFER_SIZE = 2048;

	/**
	 * Initializes the servlet.
	 */

	transient ResourceBundle sequences_rs = null;
	
	@Value("${images}")
	private String images_sequence_string;

	@Autowired
	TransactionSupport trsxnSupport;
	
	public void init(ServletConfig config) throws ServletException {
		if (sequences_rs == null)
			sequences_rs = PropertyResourceBundle.getBundle("sequence");
		servletContext = config.getServletContext();
		super.init(config);
	}

	public void saveFile() {
		try {
			if (bs == null)
				return;
			bs.flush();
			bs.close();
			return;
		} catch (java.lang.Exception e) {
			System.out.println(e.toString());
		}
	}

	public FileOutputStream cretareFileStream(String FileName) {
		FileOutputStream fis = null;
		// File file = null ;
		try {
			if (FileName == null)
				return null;
			if (FileName.length() == 0)
				return null;
			String path = this.getClass().getResource("").getPath();
			path = path.substring(0, path.indexOf("/WEB-INF/"));
			File file = new File(path + "//midlets", FileName);
			fis = new FileOutputStream(file);
		} catch (java.lang.Exception e) {
			System.out.println(e.toString());
		}
		return fis;
	}

	public int saveImgURL(String FileName, int user_id) {
		String strID = "-1";
		TransactionStatus trxnStatus =  trsxnSupport.beginTransaction();
		String query ="";

		try {

			strID =trsxnSupport.getJdbcTemplate().queryForObject(images_sequence_string, String.class);
			query = "insert into images " + "(" + " image_id , " + " imgname , " + " img_url , " + " user_id " + ")"
					+ " VALUES " + "( " + strID + ", '" + FileName + "', '" + "imgpositions/" + strID
					+ FileName.substring(FileName.lastIndexOf(".")) + "' , " + user_id + " )";

			trsxnSupport.getJdbcTemplate().update(query);
			trsxnSupport.commitTransaction(trxnStatus);
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			trsxnSupport.rollbackTransaction(trxnStatus);
		}
		return Integer.parseInt(strID);
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

		// mobilesoft.MidletServletUpload.

		// req.
		// OpenConnection("jdbc:postgresql://192.168.0.1:5432/mobilsoft") ;
		// bs = new java.io.PipedOutputStream() ;
		// pin = new java.io.PipedInputStream(bs);
		// File file = null;
		// FileOutputStream bs = new null;

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
			printResult(out, "User not autorization");
			return;
		}
		if (AuthorizationPageBeanId.getIntUserID() == 0) {
			printResult(out, "User not autorization");
			out.close();
			return;
		}
		if (AuthorizationPageBeanId.getIntLevelUp() != 2) {
			printResult(out, "User have low  access");
			out.close();
			return;
		}

		if (productPostAllFaced.isLimmitPostedMessages(AuthorizationPageBeanId, true)) {
			printResult(out, resources.getString("global_has_limmit_forsite"));
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
			return;
		}

		String boundaryString = "--" + boundary;
		ServletInputStream in = req.getInputStream();
		byte[] buffer = new byte[BUFFER_SIZE];
		HashMap map = new HashMap();
		int result = in.readLine(buffer, 0, BUFFER_SIZE);
		int size = 0;
		outer: while (true) {
			if (result <= 0) {
				System.out.println("Error. Stream truncated");
				break;
			}

			String line = new String(buffer, 0, result);

			if (!line.startsWith(boundaryString)) {
				System.out.println("Error. multipart boundary missing.");
				break;
			}

			// boundary end tag
			if (line.substring(boundaryString.length()).startsWith("--")) {
				break;
			}

			result = in.readLine(buffer, 0, BUFFER_SIZE);
			if (result <= 0) {
				System.out.println("Upload : may be end boundary which has no contents");
				break;
			}

			line = new String(buffer, 0, result);
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
				String fileContentType = null;

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

				bs = cretareFileStream(filename);
				if (bs == null) {
					System.out.println("Error. FileOutputStream = null");
					break;
				}
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

						}

						if (bytesStartsWith(buffer, 0, result, boundaryString)) {
							if (!isFirst) {
								size += tmpbufferlen - 2;
								bs.write(tmpbuffer, 0, tmpbufferlen - 2);
								// fw.
							}
							continue outer;
						} else {
							if (!isFirst) {
								size += tmpbufferlen;
								bs.write(tmpbuffer, 0, tmpbufferlen);
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
					System.out.println("IO Error while write to file : " + ie.toString());
				} finally {
					System.out.println("finally Upload : size = " + size);
					saveFile();
					// saveLObj(filename.trim() , size ) ;
					// if (size > 0) {
					// appendValue(map, name, filename, fileContentType, size);
					// }
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
		// saveLObj(filename.trim() , bs) ;
		// saveLObj(filename.trim() , size ) ;
		saveFile();
		// saveLObj(filename.trim() , size ) ;
		printResult(out, map);
		
		}
		catch (Exception ex) {
			log.error(ex.getLocalizedMessage(),ex);
		}finally {
			if(out != null )out.close();
			
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
		MidletUploadData data = new MidletUploadData(name, value, contentType, size, true);
		map.put(name, data);
	}

	void appendValue(HashMap map, String name, String value) {
		MidletUploadData data = new MidletUploadData(name, value, null, 0, false);
		map.put(name, data);
	}

	String getValue(HashMap map, String name) {
		MidletUploadData data = (MidletUploadData) map.get(name);
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
		out.println("<TITLE>Upload Result</TITLE>");
		out.println("<script language=\"JavaScript\">");
		out.println("<!--");
		out.println("function setData(){");

		out.println("opener.parent.postsoftform.midletname.value = '" + filename + "' ;");
		// out.println("opener.parent.postsoftform.imagename.value =
		// 'imgpositions/" + filename + "' ;" );
		// out.println("opener.parent.postsoftform.image_id.value = " + intID +
		// " ;" );
		out.println("opener.parent.postsoftform.midlet_uploaded.value = true  ;");
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
		out.println("<H1>Upload Result</H1>");
		out.println("<TABLE>");
		out.println("<TR><TH>NAME</TH><TH>VALUE</TH><TH>CONTENT TYPE</TH><TH>SIZE</TH><TH>FILE</TH></TR>");
		while (itr.hasNext()) {
			MidletUploadData data = (MidletUploadData) itr.next();
			out.println("<TR>");
			out.println("<TD>" + (data.name == null ? "" : data.name) + "</TD>");
			out.println("<TD>" + (data.value == null ? "" : data.value) + "</TD>");
			out.println("<TD>" + (data.contentType == null ? "" : data.contentType) + "</TD>");
			out.println("<TD>" + (data.isFile ? String.valueOf(data.size) : "") + "</TD>");
			out.println("<TD>" + (data.isFile ? "file" : "") + "</TD>");
			out.println("</TR>");
		}
		out.println("</TABLE>");
		out.println("<FORM><input type=\"button\" value=\"Close\" onClick=\"return setClose()\" ></FORM>");
		out.println("</BODY></HTML>");
	}

	void printResult(PrintWriter out, String mess) throws IOException {
		out.println("<HTML><HEAD>");
		out.println("<TITLE>Upload Result</TITLE>");
		out.println("<script language=\"JavaScript\">");
		out.println("<!--");
		out.println("function setData(){");
		out.println("return true ;");
		out.println("}");
		out.println("function setClose(){");
		out.println("top.close() ;");
		out.println("return true ;");
		out.println("}");
		out.println("//-->");
		out.println("</script>");
		out.println("</HEAD><BODY onLoad=\"return  setData()\" >");
		out.println("<H1>Upload Result</H1>");
		out.println("<TABLE>");
		out.println("<TR><TH>NAME</TH><TH>VALUE</TH><TH>CONTENT TYPE</TH><TH>SIZE</TH><TH>FILE</TH></TR>");
		out.println("<TR>");
		out.println("<TD>file not uploaded</TD>");
		out.println("<TD>value is null </TD>");
		out.println("<TD>content is null</TD>");
		out.println("<TD>size is null</TD>");
		out.println("<TD>" + mess + "</TD>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("<FORM><input type=\"button\" value=\"Close\" onClick=\"return setClose()\" ></FORM>");
		out.println("</BODY></HTML>");
	}

	/*
	 * public String getServletInfo() { return "A servlet that uploads files"; }
	 */

	public int string2Integer(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			log.error(ex.getLocalizedMessage());
			i = -1;
		}
		return i;
	}

}

class MidletUploadData {
	String name;

	String value;

	String contentType;

	int size;

	boolean isFile;

	MidletUploadData(String name, String value, String contentType, int size, boolean isFile) {
		this.name = name;
		this.value = value;
		this.contentType = contentType;
		this.size = size;
		this.isFile = isFile;
	}

}
