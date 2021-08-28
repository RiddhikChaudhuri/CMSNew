package com.cbsinc.cms.publisher.models;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ListDataGet extends LinkedList<Object> {

	transient private static final long serialVersionUID = -2800195444725820081L;

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

	public void addList(List<Map<String,Object>> list) {
		super.clear();
		super.addAll(list);
	}

	public String getValueAt(int aRow, int aColumn) {
		String[] row = (String[]) get(aRow);
		return row[aColumn];
	}

	public int getRowCount() {
		return super.size();
	}

}
