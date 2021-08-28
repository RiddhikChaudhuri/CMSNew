package com.cbsinc.cms.exceptions;

public class LocalException extends java.lang.Exception {

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

	private static final long serialVersionUID = 1L;

	public LocalException(Throwable cause) {
		super(cause);
	}

	public LocalException(String message, Throwable cause) {
		super(message, cause);
	}

	public LocalException() {
	}

}