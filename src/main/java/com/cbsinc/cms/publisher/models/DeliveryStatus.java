package com.cbsinc.cms.publisher.models;

import java.io.Serializable;

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

public interface DeliveryStatus extends Serializable {
	final static long FILLING_BASKET = 0;
	final static long INPPROCESS_DELIVERY = 1;
	final static long ORDER_DELIVERED = 2;
	final static long ORDER_NOT_DELIVERED = 3;

}
