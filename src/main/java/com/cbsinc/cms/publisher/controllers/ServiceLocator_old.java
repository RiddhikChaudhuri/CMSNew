package com.cbsinc.cms.publisher.controllers;

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
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.OrderFaced;
import com.cbsinc.cms.publisher.dao.PolicyFaced;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.dao.ProductlistFaced;

public class ServiceLocator_old {

	private static ServiceLocator_old me;
	InitialContext context = null;

	private ServiceLocator_old() throws Exception {
		try {
			context = new InitialContext();
			context.rebind("authorizationPageFaced", new AuthorizationPageFaced());
			context.rebind("orderFaced", new OrderFaced());
			context.rebind("policyFaced", new PolicyFaced());
			context.rebind("productlistFaced", new ProductlistFaced());
			context.rebind("productPostAllFaced", new ProductPostAllFaced());
		} catch (NamingException ne) {
			throw new Exception(ne);
		}
	}

	// Returns the instance of ServiceLocator class
	public static ServiceLocator_old getInstance() throws Exception {
		if (me == null) {
			me = new ServiceLocator_old();
		}
		return me;
	}

	// Converts the serialized string into EJBHandle
	// then to EJBObject.
	public AuthorizationPageFaced getAuthorizationPageFaced() throws Exception {
		AuthorizationPageFaced authorizationPageFaced = null;
		try {
			authorizationPageFaced = (AuthorizationPageFaced) context.lookup("authorizationPageFaced");
		} catch (Exception ex) {
			throw new Exception(ex);
		}
		return authorizationPageFaced;
	}

	public OrderFaced getOrderFaced() throws Exception {
		OrderFaced orderFaced = null;
		try {
			orderFaced = (OrderFaced) context.lookup("orderFaced");
		} catch (Exception ex) {
			throw new Exception(ex);
		}
		return orderFaced;
	}

	public PolicyFaced getPolicyFaced() throws Exception {
		PolicyFaced policyFaced = null;
		try {
			policyFaced = (PolicyFaced) context.lookup("policyFaced");
		} catch (Exception ex) {
			throw new Exception(ex);
		}
		return policyFaced;
	}

	public ProductlistFaced getProductlistFaced() throws Exception {
		ProductlistFaced productlistFaced = null;
		try {
			productlistFaced = (ProductlistFaced) context.lookup("productlistFaced");
		} catch (Exception ex) {
			throw new Exception(ex);
		}
		return productlistFaced;
	}

	public ProductPostAllFaced getProductPostAllFaced() throws Exception {
		ProductPostAllFaced productPostAllFaced = null;
		try {
			productPostAllFaced = (ProductPostAllFaced) context.lookup("productPostAllFaced");
		} catch (Exception ex) {
			throw new Exception(ex);
		}
		return productPostAllFaced;
	}

}
