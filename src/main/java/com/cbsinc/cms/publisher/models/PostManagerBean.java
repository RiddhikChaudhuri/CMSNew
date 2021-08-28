package com.cbsinc.cms.publisher.models;

import org.springframework.stereotype.Component;

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
@Component
public class PostManagerBean implements java.io.Serializable {

	private static final long serialVersionUID = -2367956439277209520L;

	private String sample = "Start value";

	// Access sample property
	public String getSample() {
		return sample;
	}

	// Access sample property
	public void setSample(String newValue) {
		if (newValue != null) {
			sample = newValue;
		}
	}
}
