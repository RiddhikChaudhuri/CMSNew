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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public interface IQueryManager {

	public Connection getCurrentConnection();

	public SimpleDateFormat getSimpleDateFormat();

	public List rows();

	public void executeQuery(String query) throws SQLException;

	public ResultSet executeQueryResultSet(String query) throws SQLException;

	public int executeInsertWithArgs(String query, Object[] args) throws SQLException;

	public int executeInsertWithArgs(String query, Map args) throws SQLException;

	public int executeInsertWithJavaObject(String query, Map args) throws SQLException;

	public int executeUpdateWithArgs(String query, Map args) throws SQLException;

	public void executeQueryWithArgs(String query, Object[] args) throws SQLException;

	public ResultSet executeQueryResultSet(String query, Object[] args) throws SQLException;

	public void executeQueryWithArgs(String query, Object[] args, int limmit, int offset) throws SQLException;

	public List executeQueryWithArgsList(String query, Object[] args, int limmit, int offset) throws SQLException;

	public List executeQueryList(String query) throws SQLException;

	public List executeQueryList(String query, int limmit, int offset) throws SQLException;

	public void executeUpdate(String query) throws SQLException;

	public int getColumnCount();

	public int getRowCount();

	public String getValueAt(int aRow, int aColumn) throws SQLException;

	public Object getValueObjectAt(int aRow, int aColumn);

	public void close();

	public int string2Integer(String s);

	public double string2Double(String s);

	public void rollback();

	public void commit();

	public void BeginTransaction();

	public ResourceBundle getResources_localization();

	public Map getArgs();

}
