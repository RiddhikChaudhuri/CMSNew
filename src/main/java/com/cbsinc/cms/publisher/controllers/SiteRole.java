package com.cbsinc.cms.publisher.controllers;

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

public interface SiteRole {

	final static String GUEST = "user";
	final static String GUEST_PASSWORD = "user";
	final static int GUEST_ID = 1;
	final static int GUEST_ROLE_ID = 0;
	final static int MEMBER_ROLE_ID = 1;
	final static String ADMINISTRATOR = "admin";
	final static int ADMINISTRATOR_ID = 2;
	final static int ADMINISTRATOR_ROLE_ID = 2;
	final static String CONTENT_MANAGER = "manager";
	final static int CONTENT_MANAGER_ID = 3;
	final static int CUSTOMER_ID = 4;
	final static String CUSTOMER = "customer";

}
