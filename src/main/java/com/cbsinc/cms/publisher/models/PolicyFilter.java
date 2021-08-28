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

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;



/**
 * Filter class
 * 
 * @web.filter name="Policy" display-name="Name for Policy"
 *             description="Description for Policy"
 * @web.filter-mapping url-pattern="/Policy.jsp"
 * @web.filter-init-param name="A parameter" value="A value"
 */
//@Component
//@Order(1)
public class PolicyFilter implements Filter {
	private FilterConfig filterConfig;
	
	//static private Logger log = Logger.getLogger(PolicyFilter.class);
	private XLogger log = XLoggerFactory.getXLogger(PolicyFilter.class.getName());
	
	private String xsltFileName;

	/**
	 * This method is called once when the filter is first loaded.
	 */
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpSession hsession = ((HttpServletRequest) request).getSession(false);
		AuthorizationPageBean authorizationPageBeanId;
		if (hsession != null) {
			if (hsession.getAttribute("authorizationPageBeanId") instanceof AuthorizationPageBean) {
				authorizationPageBeanId = ((AuthorizationPageBean) hsession.getAttribute("authorizationPageBeanId"));
				if (authorizationPageBeanId.getStrLogin().length() == 0) {
					return;
				}

			} else {
				return;
			}

		} else {
			return;
		}

		StringBuffer buffString = new StringBuffer("/xsl/");
		buffString.append(authorizationPageBeanId.getSite_dir().trim());
		buffString.append("/policy.xsl");
		this.xsltFileName = filterConfig.getServletContext().getRealPath(buffString.toString());
		if (this.xsltFileName == null || !new File(this.xsltFileName).exists()) {
			throw new UnavailableException("Unable to locate stylesheet: " + this.xsltFileName, 30);
		}

		Source styleSource = new StreamSource(new File(xsltFileName));
		byte[] htmlBytes;
		String htmlData = "";
		ServletOutputStream out = response.getOutputStream();
		CharResponseWrapper responseWrapper = new CharResponseWrapper((HttpServletResponse) response);
		chain.doFilter(request, responseWrapper);
		// Get response from servlet
		StringReader sr = new StringReader(new String(responseWrapper.getData()).trim());
		Source xmlSource = new StreamSource((Reader) sr);
		CharArrayWriter caw = new CharArrayWriter();
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer(styleSource);
			StreamResult result = new StreamResult(caw);
			transformer.transform(xmlSource, result);
			htmlData = caw.toString();
			htmlData = htmlData.replaceAll("&lt;", "//<");
			htmlData = htmlData.replaceAll("&gt;", "//>");
			htmlBytes = htmlData.getBytes("UTF-8");
			response.setContentLength(htmlBytes.length);
			out.write(htmlBytes);
			out.flush();
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
			out.println(ex.toString());
		} finally {
			caw.close();
			out.flush();
			out.close();
			responseWrapper.close();
		}

	}

	/**
	 * The counterpart to the init( ) method.
	 */
	public void destroy() {
		this.filterConfig = null;
	}
}
