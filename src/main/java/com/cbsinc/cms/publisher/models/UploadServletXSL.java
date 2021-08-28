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
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;


/**
 * Servlet Class
 * 
 * @web.servlet name="uploadservletxsl" display-name="Name for UploadServletXSL"
 *              description="Description for UploadServletXSL"
 * @web.servlet-mapping url-pattern="/uploadservletxsl"
 * @web.servlet-init-param name="A parameter" value="A value"
 */

@WebServlet(urlPatterns = "/uploadservletXSL/*", loadOnStartup = 1)
public class UploadServletXSL extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4325358301026757709L;
	/**
	 * 
	 */
	AuthorizationPageBean AuthorizationPageBeanId = null;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	transient ResourceBundle resources = null;
	ServletContext servletContext = null;

	//static private Logger log = Logger.getLogger(UploadServletXSL.class);
	private XLogger log = XLoggerFactory.getXLogger(UploadServletXSL.class.getName());
	
	public String filename = "";
	// public int intID = 0;
	FileOutputStream bs = null;
	// File roolfiles = null ;
	File workdir = null;
	private final static int BUFFER_SIZE = 2048;
	String basePath = "";
	String siteDir = "www.bdg-server20.com";
	String message = "";

	float limmit_file_size = 1570000;
	transient ResourceBundle setup_resources = null;
	transient ResourceBundle localization = null;

	/**
	 * Constructor of the object.
	 */
	public UploadServletXSL() {
		super();
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		// TODO Auto-generated method stub
		if (setup_resources == null)
			setup_resources = PropertyResourceBundle.getBundle("SetupApplicationResources");
		if (!setup_resources.getString("limmit_xsl_jar_size").equals(""))
			limmit_file_size = Integer.parseInt(setup_resources.getString("limmit_xsl_jar_size"));
		servletContext = config.getServletContext();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());
		else if (!localization.getLocale().getLanguage().equals(request.getLocale().getLanguage()))
			localization = PropertyResourceBundle.getBundle("localization", request.getLocale());
		message = localization.getString("to_setup_file_click_here");

		if (request.getParameter("install") != null) {
			String installfile = request.getParameter("install");
			unzipFile(installfile);
		}

		if (workdir == null)
			createWorkDir(request);
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("   <HEAD>");
		out.println("    <TITLE>" + localization.getString("design_list") + "</TITLE>");
		out.println("	  <META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
		out.println("	</HEAD>");
		out.println("  <BODY>");
		out.println("  <h3>" + this.message + "</h3>");
		if (request.getParameter("install") != null) {
			out.println("  <h3>_____________________________________________________________</h3>");
			out.print(getReportURLList());
			out.println("</br>");
			out.println("<a href='Productlist.jsp' >" + localization.getString("see_new_design") + "</a>");
			out.println("  <h3>_____________________________________________________________</h3>");

		}
		out.print(getDesignURLList());
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request  the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException      if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (localization == null)
			localization = PropertyResourceBundle.getBundle("localization", response.getLocale());
		message = localization.getString("to_setup_file_click_here");
		if (workdir == null)
			createWorkDir(request);
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 */
	public String getServletInfo() {
		return "Servlet for upload report";
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */

	void createWorkDir(HttpServletRequest request) {

		AuthorizationPageBean authorizationPageBeanId = null;
		HttpSession hsession = ((HttpServletRequest) request).getSession(false);
		if (hsession.getAttribute("authorizationPageBeanId") instanceof AuthorizationPageBean) {
			authorizationPageBeanId = ((AuthorizationPageBean) hsession.getAttribute("authorizationPageBeanId"));
			siteDir = authorizationPageBeanId.getSite_dir();
		}

		String path = this.getClass().getResource("").getPath();
		path = path.substring(0, path.indexOf("/WEB-INF/"));
		workdir = new File(path + File.separator + "xsl" + File.separator + siteDir);
		if (!workdir.getParentFile().exists())
			workdir.getParentFile().mkdir();
		if (!workdir.exists())
			workdir.mkdir();
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
		try 
		{
		AuthorizationPageBeanId = (AuthorizationPageBean) req.getSession().getAttribute("authorizationPageBeanId");

		if (resources == null)
			resources = PropertyResourceBundle.getBundle("localization", res.getLocale());
		
		long start = System.currentTimeMillis();
		String contentType = req.getContentType();
		// res.setContentType("text/html;charset=cp1251");
		res.setContentType("text/html;charset=UTF8");
		out = res.getWriter();

		if (productPostAllFaced.isLimmitPostedMessages(AuthorizationPageBeanId, true)) {
			printResult(out, resources.getString("global_has_limmit_forsite"));
			out.close();
			return;
		}

		String path = req.getContextPath();
		basePath = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + path + "/";

		if (contentType == null) {
			System.out.println("content type is null");
			return;
		}

		int ind = contentType.indexOf("boundary=");
		if (ind == -1) {
			System.out.println("IND is less than 0");
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

						if (limmit_file_size < size) {
							printResult(out, localization.getString("has_reached_limmit_file_size") + " "
									+ (limmit_file_size - 70000) / 1000000 + " Mb.");
							out.close();
							return;
						}

						if (isFirst) { // ignore all proceeding \r\n
							if (result == 2 && buffer[0] == '\r' && buffer[1] == '\n') {
								continue;
							}
						}

						if (bytesStartsWith(buffer, 0, result, boundaryString)) {
							if (!isFirst) {
								size += tmpbufferlen - 2;
								bs.write(tmpbuffer, 0, tmpbufferlen - 2);
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
					log.error(ie.getLocalizedMessage());
					System.out.println("IO Error while write to file : " + ie.toString());
				} finally {
					System.out.println("finally Upload : size = " + size);
					appendValue(map, name, filename, fileContentType, size);
					saveFile();
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
		saveFile();
		printResult(out, map);
		}
		catch (Exception ex) {
			log.error(ex.getLocalizedMessage(),ex);
		}finally {
			if(out != null )out.close();
			
			
		}
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

	public void saveFile() {
		try {
			if (bs == null)
				return;
			// unzipStream(bs) ;
			bs.flush();
			bs.close();

			unzipFile(filename);
			return;
		} catch (java.lang.Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
	}

	void appendValue(HashMap map, String name, String value, String contentType, int size) {
		ItemData data = new ItemData(name, value, contentType, size, true);
		map.put(name, data);
	}

	void appendValue(HashMap map, String name, String value) {
		ItemData data = new ItemData(name, value, null, 0, false);
		map.put(name, data);
	}

	String getValue(HashMap map, String name) {
		ItemData data = (ItemData) map.get(name);
		if (data == null) {
			return null;
		}
		return data.value;
	}

	final static int NORMAL = 0;

	final static int AMPERSAND = 1;

	final static int AMPERSHARP = 2;

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

	public FileOutputStream cretareFileStream(String FileName) {
		FileOutputStream fis = null;
		try {
			if (FileName == null)
				return null;
			if (FileName.length() == 0)
				return null;
			String path = this.getClass().getResource("").getPath();
			path = path.substring(0, path.indexOf("/WEB-INF/"));
			File uploadfile = new File(path + File.separator + "xsl" + File.separator + siteDir, FileName);
			if (!uploadfile.getParentFile().exists())
				uploadfile.getParentFile().mkdir();
			uploadfile.createNewFile();
			fis = new FileOutputStream(uploadfile);
			// workdir = uploadfile.getParentFile() ;
		} catch (java.lang.Exception e) {
			log.error(e.getLocalizedMessage());
			System.out.println(e.toString());
		}
		return fis;
	}

	void printResult(PrintWriter out, Map map) throws IOException {
		Iterator itr = map.values().iterator();
		out.println("<HTML><HEAD>");
		out.println("<META http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<TITLE>" + localization.getString("programm_for_loading_desing") + "</TITLE>");

		out.println("</HEAD><BODY  >");
		out.println("<H1>" + localization.getString("programm_for_loading_desing") + " </H1>");
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

		out.println("<b>" + localization.getString("list_uploaded_files") + "</b>");
		out.println("  <h3>_____________________________________________________________</h3>");
		out.println(getReportURLList());
		out.println("<a href='Productlist.jsp' >" + localization.getString("see_new_design") + "</a>");
		out.println("  <h3>_____________________________________________________________</h3>");

		out.println("</BODY></HTML>");
	}

	void printResult(PrintWriter out, String mess) throws IOException {
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<META http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
		out.println("<TITLE>" + localization.getString("programm_for_loading_desing") + " </TITLE>");
		out.println("<script language=\"JavaScript\">");
		out.println("<!--");
		out.println("function setClose(){");
		out.println("document.location.href='XSLControl.jsp';");
		out.println("return true ;");
		out.println("}");
		out.println("//-->");
		out.println("</script>");
		out.println("</HEAD>");
		out.println("<BODY>");
		out.println("<H1>" + localization.getString("error_in_during_upload_new_desing") + "</H1>");
		out.println("<b>" + mess + "</b>");
		out.println("<FORM  ><input type=\"button\" value=\"" + localization.getString("close_window")
				+ "\" onClick=\"return setClose()\" ></FORM>");
		out.println("</BODY>");
		out.println("</HTML>");
	}

	String getReportURLList() {
		File[] list = workdir.listFiles();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			buff.append(getHRef(list[i]));
		}
		return buff.toString();
	}

	String getDesignURLList() {
		String[] list = workdir.list();
		StringBuffer buff = new StringBuffer();
		for (int i = 0; i < list.length; i++) {
			if (list[i].endsWith(".zip"))
				buff.append(getHRefDesignList(list[i]));
		}
		return buff.toString();
	}

	String getHRef(File file) {

		// File file = new File(workdir.getPath() + File.separatorChar + filename);
		// String shortname = file.s
		String shortname = file.getPath().substring(file.getPath().indexOf(siteDir) + siteDir.length());
		// shortname = shortname.replaceAll("" + File.pathSeparatorChar, "/");
		shortname = shortname.replace(File.separatorChar, (char) 47);
		String filename = basePath + "xsl/" + siteDir + shortname;
		StringBuffer buff = new StringBuffer();

		if (file.isDirectory()) {
			File[] list = file.listFiles();
			for (int i = 0; i < list.length; i++) {
				buff.append(getHRef(list[i]));
			}
		} else {
			buff.append("<br/>");
			buff.append("<a href ='" + filename + "'>");
			buff.append(filename);
			buff.append("</a>");
			buff.append("<br/>");

		}

		return buff.toString();
		// return "< a href =\""+filename+" \">"+filename+"</a><br/>\n" ;
	}

	String getHRefDesignList(String filename) {
		// filename = basePath + "xsl/" + siteDir + "/" + filename ;

		StringBuffer buff = new StringBuffer();
		buff.append("<br/>");
		buff.append("<a href ='/uploadservletxsl?install=" + filename + "' >");
		buff.append(localization.getString("setup_design"));
		buff.append(filename);
		buff.append("</a>");
		buff.append("<br/>");
		return buff.toString();
		// return "< a href =\""+filename+" \">"+filename+"</a><br/>\n" ;
	}

//hsession.setAttribute("site_path",sitePath);
	public void unzipFile(String uploadfilename) {
		final int BUFFER = 2048;
		try {

			if (!uploadfilename.endsWith(".zip") && !uploadfilename.endsWith(".jar")) {
				message = localization.getString("file_wrong_of_format_1") + " " + uploadfilename
						+ localization.getString("file_wrong_of_format_2");
				return;
			}
			String zipfilename = workdir.getPath() + File.separator + uploadfilename;
			FileInputStream fis = new FileInputStream(zipfilename);
			BufferedOutputStream dest = null;
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				// String filename =
				// entry.getName().substring(entry.getName().indexOf((char)47)+1) ;
				String filename = entry.getName();
				if (filename.equals("") || filename.endsWith("/"))
					continue;
				File file = new File(workdir.getPath() + File.separator + filename);
				createDirForFile(file);
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}

				dest.flush();
				dest.close();
			}
			zis.close();
			message = localization.getString("setup_design_good_1") + " " + uploadfilename + " "
					+ localization.getString("setup_design_good_2");
			// getServletContext().getAttribute("ITransformationService") ;
			if (getServletContext().getAttribute("ITransformationService") instanceof ITransformationService) {
				ITransformationService iTransformationService = (ITransformationService) getServletContext()
						.getAttribute("ITransformationService");
				iTransformationService.clearAllXSLTemplates();
			}

			// servletContext.setAttribute("ITransformationService",(ITransformationService)this)

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void createDirForFile(File file) {
		File childfile = file;
		while (!childfile.getParentFile().exists()) {
			if (childfile.getParentFile().isFile())
				continue;
			childfile.getParentFile().mkdir();
			childfile = childfile.getParentFile();
		}
	}

	public void unzipFile_old(String uploadfilename) {
		final int BUFFER = 2048;
		try {

			// PipedOutputStream pos = new PipedOutputStream();
			// os = new BufferedOutputStream(pos);
			// InputStream fis = new PipedInputStream(pos);
			String zipfilename = workdir.getPath() + File.separator + uploadfilename;
			FileInputStream fis = new FileInputStream(zipfilename);
			BufferedOutputStream dest = null;
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				File file = new File(
						workdir.getPath() + File.separator + entry.getName().replace((char) 47, File.separatorChar));
				// File file = new File(workdir.getPath() + "\\" +
				// entry.getName().substring(entry.getName().indexOf((char)47))) ;
				file.getParentFile().mkdir();
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}

				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void unzipStream(java.io.OutputStream os) {
		final int BUFFER = 2048;
		try {

			PipedOutputStream pos = new PipedOutputStream();
			os = new BufferedOutputStream(pos);
			InputStream fis = new PipedInputStream(pos);
			BufferedOutputStream dest = null;
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				FileOutputStream fos = new FileOutputStream(entry.getName());
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}

				dest.flush();
				dest.close();
			}
			zis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
