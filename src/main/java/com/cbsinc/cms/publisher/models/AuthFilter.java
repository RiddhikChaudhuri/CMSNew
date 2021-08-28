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

package com.cbsinc.cms.publisher.models;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class AuthFilter implements Filter {
	/**
	 * 
	 */
	static final long serialVersionUID = 1L;

	private FilterConfig filterConfig;

	//static private Logger log = Logger.getLogger(AuthFilter.class);
	private XLogger logger = XLoggerFactory.getXLogger(AuthFilter.class.getName());

	public void init(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		try {
		  
		  String path = ((HttpServletRequest) request).getRequestURI();

          int index = path.lastIndexOf("/") + 1;
          path = path.substring(index);
			HttpSession hsession = ((HttpServletRequest) request).getSession(false);
			if (hsession != null) {
				AuthorizationPageBean authorizationPageBeanId;
				if (hsession.getAttribute("authorizationPageBeanId") instanceof AuthorizationPageBean) {
					authorizationPageBeanId = ((AuthorizationPageBean) hsession
							.getAttribute("authorizationPageBeanId"));
					if (authorizationPageBeanId.getStrLogin().length() == 0) {
						return;
					}

				} else {
					return;
				}

			} else {
				return;
			}

			filterChain.doFilter(request, response);
		} catch (ServletException sx) {
			logger.error(sx.getLocalizedMessage());
			filterConfig.getServletContext().log(sx.getMessage());
		} catch (IOException iox) {
			logger.error(iox.getLocalizedMessage());
			filterConfig.getServletContext().log(iox.getMessage());
		}
	}

	public void destroy() {
	}
	
	
}