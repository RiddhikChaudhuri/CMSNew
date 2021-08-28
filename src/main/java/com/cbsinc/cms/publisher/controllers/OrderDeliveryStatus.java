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

public enum OrderDeliveryStatus {

	DO_NOT_FORMATION_ORDER(0), BILL_HAS_SENT_TO_CLIENT(1), BILL_HAS_PAID(2), ORDER_HAS_SENT_TO_CLIENT(3),
	ORDER_HAS_DELIVERED_TO_CLIENT(4), REFUSE(5), MONEY_BACK(6);

	private int code;

	private OrderDeliveryStatus(int c) {
		code = c;
	}

	public int getCode() {
		return code;
	}

}
