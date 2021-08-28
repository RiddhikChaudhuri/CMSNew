package com.cbsinc.cms.publisher.models;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class SearchBean implements java.io.Serializable {

	 private static final long serialVersionUID = -7111281278620430928L;
	//transient static private Logger log = Logger.getLogger(SearchBean.class);
	private XLogger log = XLoggerFactory.getXLogger(SearchBean.class.getName());

	private String searchValueArg = "";

	public String[][] rows = new String[10][2];

	// public String[][] news_rows = new String[10][2] ;
	public String[][] co1_rows = new String[10][2];

	public String[][] co2_rows = new String[10][2];

	// public String[][] allquery_rows = new String[10][2];

	@Autowired
	GetValueTool tool;

	public List allqueryAdp = new LinkedList();

	public List Adp = new LinkedList();

	public List co1Adp = new LinkedList();

	public List co2Adp = new LinkedList();

	private List tmpAdp = new LinkedList();

	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;

	private String type1_id = "1";

	private String type2_id = "1";

	// private int type_id = 1 ;
	private Long intLevelUp = new Long(0);

	private String phonetype_id = "-1";

	private String progname_id = "-1";

	private String phonemodel_id = "-1";

	private String licence_id = "-1";

	private String imgname;

	private String image_id;

	private String img_url;

	private String img_url2;

	private String user1_id;

	private String user2_id;

	private String cururl;

	// private String catalog_id = "-2";

	private String catalog_parent_id = "0";

	// private String site_id = "0";

	private String post_manager = "";

	private String currency_id = "";

	private String currency_id2 = "";

	private String currency_cd = "";

	private String currency_desc = "";

	private String currency_desc2 = "";

	private String product_name = "";

	private String product_name2 = "";

	private String product_url = "";

	private String product_url2 = "";

	private String product_imgurl = "";

	private String product_iconurl = "";

	private String product_iconurl2 = "";

	private String product_description = "";

	private String product_description2 = "";

	private String product_fulldescription = "";

	private String product_version = "";

	private String product_cost = "";

	private String product_cost2 = "";

	private String product_currency = "";

	private String product_owner = "";

	private String product_id = "";

	private String action = "";

	private Integer searchquery = 0;

	private Integer pagecount = 0;

	private String portlettype_id = "0";

	private Integer pagecount_blog = 0;

	private Integer pagecount_co1 = 0;

	private Integer pagecount_co2 = 0;

	private String query_productlist = "";

	private String select_currency_cd = "";
	private String select_path = "";

	private String color = "";

	private String allFoundProducts = "";

	String dialog = "true";
	String advancedSearchOpen = "true";
	String forumOpen = "true";

	public Boolean isInternet = true;

	public SearchBean() {

	}

	public String getProductsIdlist(int count) {
		String productsId = "";
		try {
			rows = new String[tool.getRowCount(Adp)][2];
			if (rows.length < count)
				return "";
			for (int i = 0; rows.length > i; i++) {
				productsId = productsId + "_" + rows[i][0];
				if (i == (count - 1))
					break;
			}
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		return productsId.substring(1);
	}

	public String getQuantityProducts() {
		return "" + tool.getRowCount(allqueryAdp);
	}

	public String getProductlist(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		rows = new String[tool.getRowCount(Adp)][2];

		StringBuffer table = new StringBuffer();
		table.append("<list>\n");
		cururl = "Productlist.jsp?offset=" + offset;
		listup = "Productlist.jsp?offset=" + (offset + 10);
		if (offset - 10 < 0)
			listdown = "Productlist.jsp?offset=0";
		else
			listdown = "Productlist.jsp?offset=" + (offset - 10);

		try {
			if (intLevelUp == 2)
				setPost_manager("PostManager.jsp");
			else
				setPost_manager("");
			pagecount = tool.getRowCount(Adp);

			for (int i = 0; tool.getRowCount(Adp) > i; i = i + 2) {
				rows[i][0] = (String) tool.getValueAt(Adp, i, 0);
				rows[i][1] = tool.getValueAt(Adp, i, 7) == null ? "" : tool.getValueAt(Adp, i, 7);
				// rows[i][1] = Adp.getValueAt(i, 7) ; //== null
				// ?"":Adp.getValueAt(i, 7) ;

				product_name = (String) tool.getValueAt(Adp, i, 1);
				// strSoftURL = "downloadservlet?row=" + i + "&dev=html" ;;
				// strSoftURL = "downloadservlet?row=" + i ;
				/////////////// product_url = "Policy.jsp?row=" + i;
				product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(Adp, i, 0);
				// licy_byproductid

				img_url = (String) tool.getValueAt(Adp, i, 13);
				if (img_url != null)
					product_iconurl = img_url;
				else
					product_iconurl = "images/Folder.jpg";
				product_description = (String) tool.getValueAt(Adp, i, 2);
				product_version = (String) tool.getValueAt(Adp, i, 3);
				product_cost = (String) tool.getValueAt(Adp, i, 4);
				currency_id = (String) tool.getValueAt(Adp, i, 5);

				CurrencyHash currencyHash = CurrencyHash.getInstance();

				currency_desc = currencyHash.getCurrency_decs(currency_id);
				// Currency curr = CurrencyHash.getCurrency(currency_id);
				// if(curr == null) throw new
				// java.lang.UnsupportedOperationException("Currency curr == null
				// ");
				// currency_cd = curr.getCode();
				// currency_cd = curr.getCode();

				// phonetype_id = (String)tool.getValueAt(Adp,i,10) ;
				// phonetype_id2 = (String)tool.getValueAt(Adp,i,10) ;
				// progname_id = (String)tool.getValueAt(Adp,i,11) ;
				image_id = (String) tool.getValueAt(Adp, i, 12);
				product_fulldescription = (String) tool.getValueAt(Adp, i, 14);
				// product_bigimgurl = (String)tool.getValueAt(Adp,i,15) ;
				user1_id = (String) tool.getValueAt(Adp, i, 16);
				// cdate = (Date)Adp.getValueObjectAt(i,17) ;
				// statistic = (String)tool.getValueAt(Adp,i,18) ;
				// user1_id = (String)tool.getValueAt(Adp,i,16) ;
				// private String statistic = "0" ;
				// private Date cdate =null ;

				type1_id = (String) tool.getValueAt(Adp, i, 8);

				color = (String) tool.getValueAt(Adp, i, 40);

				table.append("<product>\n");

				table.append("<rigth>\n");
				table.append("<product_id>" + rows[i][0] + "</product_id>\n");
				table.append("<row_id>" + i + "</row_id>\n");
				table.append("<name>" + product_name + "</name>\n");
				table.append("<type_id>" + type1_id + "</type_id>\n");
				table.append("<icon>" + product_iconurl + "</icon>\n");
				table.append("<image></image>\n");
				table.append("<user_id>" + user1_id + "</user_id>\n");
				// Referece to pruduct
				table.append("<policy_url>" + product_url + "</policy_url>\n");
				table.append("<description>" + product_description + "</description>\n");
				// table.append("<fulldescription>" + product_fulldescription +
				// "</fulldescription>\n") ;
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("<color>" + color + "</color>\n");
				table.append("</rigth>\n");

				if (tool.getRowCount(Adp) > (i + 1)) {
					/// product_url2 = "Policy.jsp?row=" + (i + 1);
					product_url2 = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(Adp, i + 1, 0);

					rows[i + 1][0] = (String) tool.getValueAt(Adp, i + 1, 0);
					rows[i + 1][1] = tool.getValueAt(Adp, i + 1, 7) == null ? "" : tool.getValueAt(Adp, i + 1, 7);
					// rows[i+1][1] = (String)tool.getValueAt(Adp,i+1,7) ;
					product_name2 = (String) tool.getValueAt(Adp, i + 1, 1);
					img_url2 = (String) tool.getValueAt(Adp, i + 1, 13);
					if (img_url2 != null)
						product_iconurl2 = img_url2;
					else
						product_iconurl2 = "images/Folder.jpg";
					product_description2 = (String) tool.getValueAt(Adp, i + 1, 2);
					product_cost2 = (String) tool.getValueAt(Adp, i + 1, 4);
					currency_id2 = (String) tool.getValueAt(Adp, i + 1, 5);
					// CurrencyHash currencyHash = CurrencyHash.getInstance() ;
					currency_desc2 = currencyHash.getCurrency_decs(currency_id2);
					user2_id = (String) tool.getValueAt(Adp, i + 1, 16);
					type2_id = (String) tool.getValueAt(Adp, i + 1, 8);
					color = (String) tool.getValueAt(Adp, i + 1, 40);

					table.append("<left>\n");
					table.append("<product_id>" + rows[i + 1][0] + "</product_id>\n");
					table.append("<row_id>" + (i + 1) + "</row_id>\n");
					table.append("<name>" + product_name2 + "</name>\n");
					table.append("<type_id>" + type2_id + "</type_id>\n");
					table.append("<icon>" + product_iconurl2 + "</icon>\n");
					table.append("<image></image>\n");
					table.append("<user_id>" + user2_id + "</user_id>\n");
					// Referece to pruduct
					table.append("<policy_url>" + product_url2 + "</policy_url>\n");
					table.append("<description>" + product_description2 + "</description>\n");
					// table.append("<fulldescription>" + product_fulldescription +
					// "</fulldescription>\n") ;
					table.append("<amount>" + product_cost2 + "</amount>\n");
					table.append("<currency>\n");
					table.append("<code>" + currency_cd + "</code>\n");
					table.append("<description>dollar us</description>\n");
					table.append("</currency>\n");
					table.append("<version>" + currency_desc2 + "</version>\n");
					table.append("<color>" + color + "</color>\n");
					table.append("</left>\n");
				}

				table.append("</product>\n");
			}
			table.append("</list>\n");
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return table.toString();
	}

	public String getProductSimpleList(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		rows = new String[tool.getRowCount(Adp)][2];

		StringBuffer table = new StringBuffer();
		table.append("<product_list>\n");
		cururl = "Productlist.jsp?offset=" + offset;
		listup = "Productlist.jsp?offset=" + (offset + 10);
		if (offset - 10 < 0)
			listdown = "Productlist.jsp?offset=0";
		else
			listdown = "Productlist.jsp?offset=" + (offset - 10);

		try {
			if (intLevelUp == 2)
				setPost_manager("PostManager.jsp");
			else
				setPost_manager("");
			pagecount = tool.getRowCount(Adp);

			for (int i = 0; tool.getRowCount(Adp) > i; i++) {
				rows[i][0] = (String) tool.getValueAt(Adp, i, 0);
				rows[i][1] = tool.getValueAt(Adp, i, 7) == null ? "" : tool.getValueAt(Adp, i, 7);
				// rows[i][1] = tool.getValueAt(Adp,i, 7) ; //== null
				// ?"":Adp.getValueAt(i, 7) ;

				product_name = (String) tool.getValueAt(Adp, i, 1);
				// strSoftURL = "downloadservlet?row=" + i + "&dev=html" ;;
				// strSoftURL = "downloadservlet?row=" + i ;
				///// product_url = "Policy.jsp?row=" + i;
				product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(Adp, i, 0);

				img_url = (String) tool.getValueAt(Adp, i, 13);
				if (img_url != null)
					product_iconurl = img_url;
				else
					product_iconurl = "images/Folder.jpg";
				product_description = (String) tool.getValueAt(Adp, i, 2);
				product_version = (String) tool.getValueAt(Adp, i, 3);
				product_cost = (String) tool.getValueAt(Adp, i, 4);
				currency_id = (String) tool.getValueAt(Adp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				// Currency curr = CurrencyHash.getCurrency(currency_id);
				// if(curr == null) throw new
				// java.lang.UnsupportedOperationException("Currency curr == null
				// ");
				// currency_cd = curr.getCode();
				// currency_cd = curr.getCode();

				// phonetype_id = (String)tool.getValueAt(Adp,i,10) ;
				// phonetype_id2 = (String)tool.getValueAt(Adp,i,10) ;
				// progname_id = (String)tool.getValueAt(Adp,i,11) ;
				image_id = (String) tool.getValueAt(Adp, i, 12);
				product_fulldescription = (String) tool.getValueAt(Adp, i, 14);
				// product_bigimgurl = (String)tool.getValueAt(Adp,i,15) ;
				user1_id = (String) tool.getValueAt(Adp, i, 16);
				// cdate = (Date)Adp.getValueObjectAt(i,17) ;
				// statistic = (String)tool.getValueAt(Adp,i,18) ;
				// user1_id = (String)tool.getValueAt(Adp,i,16) ;
				// private String statistic = "0" ;
				// private Date cdate =null ;
				color = (String) tool.getValueAt(Adp, i, 40);

				type1_id = (String) tool.getValueAt(Adp, i, 8);

				table.append("<product>\n");
				table.append("<product_id>" + rows[i][0] + "</product_id>\n");
				table.append("<row_id>" + i + "</row_id>\n");
				table.append("<name>" + product_name + "</name>\n");
				table.append("<type_id>" + type1_id + "</type_id>\n");
				table.append("<icon>" + product_iconurl + "</icon>\n");
				table.append("<image></image>\n");
				table.append("<user_id>" + user1_id + "</user_id>\n");
				// Referece to pruduct
				table.append("<policy_url>" + product_url + "</policy_url>\n");
				table.append("<description>" + product_description + "</description>\n");
				// table.append("<fulldescription>" + product_fulldescription +
				// "</fulldescription>\n") ;
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("<color>" + color + "</color>\n");
				table.append("</product>\n");
			}
			table.append("</product_list>\n");
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return table.toString();
	}

	String getEmptyProductList() {
		StringBuffer table = new StringBuffer();
		table.append("<list>\n");
		table.append("</list>\n");
		return table.toString();
	}

	public String getProduct(String _soft_id) throws SQLException {

		StringBuffer table = new StringBuffer();
		table.append("<list>\n");
		cururl = "Productlist.jsp?offset=" + offset; // + "&catalog_id=" +
														// catalog_id +
														// "&phonetype_id=" +
														// phonetype_id +
														// "&licence_id=" +
														// licence_id ;
		listup = "Productlist.jsp?offset=" + (offset + 10); // + "&catalog_id="
															// + catalog_id +
															// "&phonetype_id="
															// + phonetype_id +
															// "&licence_id=" +
															// licence_id ;
		if (offset - 10 < 0)
			listdown = "Productlist.jsp?offset=0"; // &catalog_id=" +
													// catalog_id +
													// "&phonetype_id=" +
													// phonetype_id +
													// "&licence_id=" +
													// licence_id ;
		else
			listdown = "Productlist.jsp?offset=" + (offset - 10); // +
																	// "&catalog_id="
																	// +
																	// catalog_id
																	// +
																	// "&phonetype_id="
																	// +
																	// phonetype_id
																	// +
																	// "&licence_id="
																	// +
																	// licence_id
																	// ;

		try {
			if (intLevelUp == 2)
				setPost_manager("PostManager.jsp");
			else
				setPost_manager("");
			pagecount = tool.getRowCount(tmpAdp);

			for (int i = 0; tool.getRowCount(tmpAdp) > i; i++) {
				rows[i][0] = (String) tool.getValueAt(tmpAdp, i, 0);
				rows[i][1] = tool.getValueAt(tmpAdp, i, 7) == null ? "" : tool.getValueAt(tmpAdp, i, 7);
				product_name = (String) tool.getValueAt(tmpAdp, i, 1);
				product_url = "Policy.jsp?row=" + i;

				img_url = (String) tool.getValueAt(tmpAdp, i, 13);
				if (img_url != null)
					product_iconurl = img_url;
				else
					product_iconurl = "images/Folder.jpg";
				product_description = (String) tool.getValueAt(tmpAdp, i, 2);
				product_version = (String) tool.getValueAt(tmpAdp, i, 3);
				product_cost = (String) tool.getValueAt(tmpAdp, i, 4);
				currency_id = (String) tool.getValueAt(tmpAdp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				image_id = (String) tool.getValueAt(tmpAdp, i, 12);
				product_fulldescription = (String) tool.getValueAt(tmpAdp, i, 14);
			}
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		return table.toString();

	}

	/*
	 * Здесь наверное ошибка public String getProduct(String _soft_id) throws
	 * SQLException {
	 * 
	 * StringBuffer table = new StringBuffer(); table.append("<list>\n"); cururl =
	 * "Productlist.jsp?offset=" + offset; // + "&catalog_id=" + // catalog_id + //
	 * "&phonetype_id=" + // phonetype_id + // "&licence_id=" + // licence_id ;
	 * listup = "Productlist.jsp?offset=" + (offset + 10); // + "&catalog_id=" // +
	 * catalog_id + // "&phonetype_id=" // + phonetype_id + // "&licence_id=" + //
	 * licence_id ; if (offset - 10 < 0) listdown = "Productlist.jsp?offset=0"; //
	 * &catalog_id=" + // catalog_id + // "&phonetype_id=" + // phonetype_id + //
	 * "&licence_id=" + // licence_id ; else listdown = "Productlist.jsp?offset=" +
	 * (offset - 10); // + // "&catalog_id=" // + // catalog_id // + //
	 * "&phonetype_id=" // + // phonetype_id // + // "&licence_id=" // + //
	 * licence_id // ;
	 * 
	 * 
	 * try { if (intLevelUp == 2) setPost_manager("PostManager.jsp"); else
	 * setPost_manager(""); pagecount = tool.getRowCount(tmpAdp);
	 * 
	 * for (int i = 0; tool.getRowCount(tmpAdp) > i; i++) { ext1_rows[i][0] =
	 * (String) tool.getValueAt( tmpAdp,i, 0); ext1_rows[i][1] = tool.getValueAt(
	 * tmpAdp,i, 7) == null ? "" : tool.getValueAt( tmpAdp,i, 7); product_name =
	 * (String) tool.getValueAt( tmpAdp,i, 1); product_url = "Policy.jsp?co1_row=" +
	 * i;
	 * 
	 * img_url = (String) tool.getValueAt( tmpAdp,i, 13); if (img_url != null)
	 * product_iconurl = img_url; else product_iconurl = "images/Folder.jpg";
	 * product_description = (String) tool.getValueAt( tmpAdp,i, 2); product_version
	 * = (String) tool.getValueAt( tmpAdp,i, 3); product_cost = (String)
	 * tool.getValueAt( tmpAdp,i, 4); currency_id = (String) tool.getValueAt(
	 * tmpAdp,i, 5); currency_desc = CurrencyHash.getCurrency_decs(currency_id);
	 * image_id = (String) tool.getValueAt( tmpAdp,i, 12); product_fulldescription =
	 * (String) tool.getValueAt( tmpAdp,i, 14); } } catch (Exception ex) {
	 * log.error(ex); } return table.toString();
	 * 
	 * }
	 * 
	 */

	public String getCoOneProductlist(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		co1_rows = new String[tool.getRowCount(co1Adp)][2];

		StringBuffer table = new StringBuffer();
		table.append("<coproductlist1>\n");
		cururl = "Productlist.jsp?offset=" + offset; // + "&catalog_id=" +
														// catalog_id +
														// "&phonetype_id=" +
														// phonetype_id +
														// "&licence_id=" +
														// licence_id ;
		listup = "Productlist.jsp?offset=" + (offset + 10); // + "&catalog_id="
															// + catalog_id +
															// "&phonetype_id="
															// + phonetype_id +
															// "&licence_id=" +
															// licence_id ;
		if (offset - 10 < 0)
			listdown = "Productlist.jsp?offset=0"; // &catalog_id=" +
													// catalog_id +
													// "&phonetype_id=" +
													// phonetype_id +
													// "&licence_id=" +
													// licence_id ;
		else
			listdown = "Productlist.jsp?offset=" + (offset - 10); // +
																	// "&catalog_id="
																	// +
																	// catalog_id
																	// +
																	// "&phonetype_id="
																	// +
																	// phonetype_id
																	// +
																	// "&licence_id="
																	// +
																	// licence_id
																	// ;

		try {
			if (intLevelUp == 2)
				setPost_manager("PostManager.jsp");
			else
				setPost_manager("");
			pagecount_co1 = tool.getRowCount(co1Adp);

			for (int i = 0; tool.getRowCount(co1Adp) > i; i++) {
				co1_rows[i][0] = (String) tool.getValueAt(co1Adp, i, 0);
				co1_rows[i][1] = tool.getValueAt(co1Adp, i, 7) == null ? "" : tool.getValueAt(co1Adp, i, 7);
				product_name = (String) tool.getValueAt(co1Adp, i, 1);
				String attache_file = "downloadservletbyrowid?productid=" + (String) tool.getValueAt(co1Adp, i, 0);
				// product_url = "Policy.jsp?co1_row=" + i;
				product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(co1Adp, i, 0);

				product_iconurl = (String) tool.getValueAt(co1Adp, i, 13);
				if (product_iconurl == null)
					product_iconurl = "images/Folder.jpg";

				img_url = (String) tool.getValueAt(co1Adp, i, 15);
				if (img_url == null)
					img_url = "images/Folder.jpg";
				String file_exist1 = "";
				if (co1_rows[i][1].length() > 0)
					file_exist1 = "true";

				product_description = (String) tool.getValueAt(co1Adp, i, 2);
				product_version = (String) tool.getValueAt(co1Adp, i, 3);
				product_cost = (String) tool.getValueAt(co1Adp, i, 4);
				currency_id = (String) tool.getValueAt(co1Adp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				image_id = (String) tool.getValueAt(co1Adp, i, 12);
				product_fulldescription = (String) tool.getValueAt(co1Adp, i, 14);
				user1_id = (String) tool.getValueAt(co1Adp, i, 16);

				table.append("<coproduct1>\n");
				table.append("<product_id>" + co1_rows[i][0] + "</product_id>\n");
				table.append("<row_id>" + i + "</row_id>\n");
				table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
				table.append("<name>" + product_name + "</name>\n");
				table.append("<icon>" + product_iconurl + "</icon>\n");
				table.append("<image>" + img_url + "</image>\n");
				table.append("<big_image_type>" + img_url.substring(img_url.indexOf(".") + 1) + "</big_image_type>\n");
				table.append(
						"<icon_type>" + product_iconurl.substring(product_iconurl.indexOf(".") + 1) + "</icon_type>\n");
				table.append("<user_id>" + user1_id + "</user_id>\n");
				// Referece to pruduct
				table.append("<policy_url>" + product_url + "</policy_url>\n");
				table.append("<product_url>" + attache_file + "</product_url>\n");
				table.append("<description>" + product_description + "</description>\n");
				// table.append("<fulldescription>" + product_fulldescription +
				// "</fulldescription>\n") ;
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("</coproduct1>\n");
			}
			table.append("</coproductlist1>\n");
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		return table.toString();
	}

	public String getCoTwoProductlist(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		co2_rows = new String[tool.getRowCount(co2Adp)][2];

		StringBuffer table = new StringBuffer();
		table.append("<coproductlist2>\n");
		cururl = "Productlist.jsp?offset=" + offset; // + "&catalog_id=" +
														// catalog_id +
														// "&phonetype_id=" +
														// phonetype_id +
														// "&licence_id=" +
														// licence_id ;
		listup = "Productlist.jsp?offset=" + (offset + 10); // + "&catalog_id="
															// + catalog_id +
															// "&phonetype_id="
															// + phonetype_id +
															// "&licence_id=" +
															// licence_id ;
		if (offset - 10 < 0)
			listdown = "Productlist.jsp?offset=0"; // &catalog_id=" +
													// catalog_id +
													// "&phonetype_id=" +
													// phonetype_id +
													// "&licence_id=" +
													// licence_id ;
		else
			listdown = "Productlist.jsp?offset=" + (offset - 10); // +
																	// "&catalog_id="
																	// +
																	// catalog_id
																	// +
																	// "&phonetype_id="
																	// +
																	// phonetype_id
																	// +
																	// "&licence_id="
																	// +
																	// licence_id
																	// ;

		try {
			if (intLevelUp == 2)
				setPost_manager("PostManager.jsp");
			else
				setPost_manager("");
			pagecount_co2 = tool.getRowCount(co2Adp);

			for (int i = 0; tool.getRowCount(co2Adp) > i; i++) {
				co2_rows[i][0] = (String) tool.getValueAt(co2Adp, i, 0);
				co2_rows[i][1] = tool.getValueAt(co2Adp, i, 7) == null ? "" : tool.getValueAt(co2Adp, i, 7);
				product_name = (String) tool.getValueAt(co2Adp, i, 1);
				String attache_file = "downloadservletbyrowid?productid=" + (String) tool.getValueAt(co2Adp, i, 0);
				// product_url = "Policy.jsp?co2_row=" + i;
				product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(co2Adp, i, 0);

				product_iconurl = (String) tool.getValueAt(co2Adp, i, 13);
				if (product_iconurl == null)
					product_iconurl = "images/Folder.jpg";

				img_url = (String) tool.getValueAt(co2Adp, i, 15);
				if (img_url == null)
					img_url = "images/Folder.jpg";

				String file_exist1 = "";
				if (co2_rows[i][1].length() > 0)
					file_exist1 = "true";

				product_description = (String) tool.getValueAt(co2Adp, i, 2);
				product_version = (String) tool.getValueAt(co2Adp, i, 3);
				product_cost = (String) tool.getValueAt(co2Adp, i, 4);
				currency_id = (String) tool.getValueAt(co2Adp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				image_id = (String) tool.getValueAt(co2Adp, i, 12);
				product_fulldescription = (String) tool.getValueAt(co2Adp, i, 14);
				user1_id = (String) tool.getValueAt(co2Adp, i, 16);
				table.append("<coproduct2>\n");
				table.append("<product_id>" + co2_rows[i][0] + "</product_id>\n");
				table.append("<row_id>" + i + "</row_id>\n");
				table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
				table.append("<name>" + product_name + "</name>\n");
				table.append("<icon>" + product_iconurl + "</icon>\n");
				table.append("<image>" + img_url + "</image>\n");
				table.append("<big_image_type>" + img_url.substring(img_url.indexOf(".") + 1) + "</big_image_type>\n");
				table.append(
						"<icon_type>" + product_iconurl.substring(product_iconurl.indexOf(".") + 1) + "</icon_type>\n");
				table.append("<user_id>" + user1_id + "</user_id>\n");
				// Referece to pruduct
				table.append("<policy_url>" + product_url + "</policy_url>\n");
				table.append("<product_url>" + attache_file + "</product_url>\n");
				table.append("<description>" + product_description + "</description>\n");
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("</coproduct2>\n");
			}
			table.append("</coproductlist2>\n");
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}
		return table.toString();
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getOffset() {
		return offset;
	}

	public int stringToInt(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = 0;
			log.error(ex.getLocalizedMessage());
		}
		return i;
	}

	public void setIntLevelUp(long intLevelUp) {
		this.intLevelUp = intLevelUp;
	}

	public long getIntLevelUp() {
		return intLevelUp;
	}

	public void setPhonetype_id(String phonetype_id) {
		this.phonetype_id = phonetype_id;
	}

	public String getPhonetype_id() {
		return phonetype_id;
	}

	public void setProgname_id(String progname_id) {
		this.progname_id = progname_id;
	}

	public String getProgname_id() {
		return progname_id;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getImgname() {
		return imgname;
	}

	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}

	public String getImage_id() {
		return image_id;
	}

	public String getCururl() {
		return cururl;
	}

	public void setCururl(String cururl) {
		this.cururl = cururl;
	}

	public String getPhonemodel_id() {
		return phonemodel_id;
	}

	public void setPhonemodel_id(String phonemodel_id) {
		this.phonemodel_id = phonemodel_id;
	}

	public String getLicence_id() {
		return licence_id;
	}

	public void setLicence_id(String licence_id) {
		this.licence_id = licence_id;
	}

//	public String getCatalog_id() {
//		return catalog_id;
//	}
//
//	public void setCatalog_id(String catalog_id) {
//		this.catalog_id = catalog_id;
//	}

//	public String getSite_id() {
//		return site_id;
//	}
//
//	public void setSite_id(String site_id) {
//		this.site_id = site_id;
//	}

	public String getPost_manager() {
		return post_manager;
	}

	public void setPost_manager(String post_manager) {
		this.post_manager = post_manager;
	}

	public String getListup() {
		return listup;
	}

	public void setListup(String listup) {
		this.listup = listup;
	}

	public String getListdown() {
		return listdown;
	}

	public void setListdown(String listdown) {
		this.listdown = listdown;
	}

	public String getCurrency_id() {
		return currency_id;
	}

	public void setCurrency_id(String currency_id) {
		this.currency_id = currency_id;
	}

	public String getCurrency_cd() {
		return currency_cd;
	}

	public void setCurrency_cd(String currency_cd) {
		this.currency_cd = currency_cd;
	}

	public String getCurrency_desc() {
		return currency_desc;
	}

	public void setCurrency_desc(String currency_desc) {
		this.currency_desc = currency_desc;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getProduct_url() {
		return product_url;
	}

	public void setProduct_url(String product_url) {
		this.product_url = product_url;
	}

	public String getProduct_imgurl() {
		return product_imgurl;
	}

	public void setProduct_imgurl(String product_imgurl) {
		this.product_imgurl = product_imgurl;
	}

	public String getProduct_iconurl() {
		return product_iconurl;
	}

	public void setProduct_iconurl(String product_iconurl) {
		this.product_iconurl = product_iconurl;
	}

	public String getProduct_description() {
		return product_description;
	}

	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}

	public String getProduct_version() {
		return product_version;
	}

	public void setProduct_version(String product_version) {
		this.product_version = product_version;
	}

	public String getProduct_cost() {
		return product_cost;
	}

	public void setProduct_cost(String product_cost) {
		this.product_cost = product_cost;
	}

	public String getProduct_currency() {
		return product_currency;
	}

	public void setProduct_currency(String product_currency) {
		this.product_currency = product_currency;
	}

	public String getProduct_owner() {
		return product_owner;
	}

	public void setProduct_owner(String product_owner) {
		this.product_owner = product_owner;
	}

	public String getTrueValue(String tmp1, String tmp2, boolean b) {
		if (b)
			return tmp1;
		else
			return tmp2;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getProduct_fulldescription() {
		return product_fulldescription;
	}

	public void setProduct_fulldescription(String product_fulldescription) {
		this.product_fulldescription = product_fulldescription;
	}

	public int getSearchquery() {
		return searchquery;
	}

	public void setSearchquery(int searchquery) {
		this.searchquery = searchquery;
	}

	public String getSearchValueArg() {
		return searchValueArg;
	}

	public void setSearchValueArg(String searchValueArg) {
		this.searchValueArg = searchValueArg;
	}

	public int getPagecount() {
		return pagecount;
	}

	public void setPagecount(int pagecount) {
		this.pagecount = pagecount;
	}

	public String getPortlettype_id() {
		return portlettype_id;
	}

	public void setPortlettype_id(String portlettype_id) {
		this.portlettype_id = portlettype_id;
	}

	public int getPagecount_co1() {
		return pagecount_co1;
	}

	public void setPagecount_co1(int pagecount_co1) {
		this.pagecount_co1 = pagecount_co1;
	}

	public int getPagecount_co2() {
		return pagecount_co2;
	}

	public void setPagecount_co2(int pagecount_co2) {
		this.pagecount_co2 = pagecount_co2;
	}

//	public String getCreteria1_id() {
//		return creteria1_id;
//	}
//
//	public void setCreteria1_id(String creteria1_id) {
//		this.creteria1_id = creteria1_id;
//	}
//
//	public String getCreteria10_id() {
//		return creteria10_id;
//	}
//
//	public void setCreteria10_id(String creteria10_id) {
//		this.creteria10_id = creteria10_id;
//	}
//
//	public String getCreteria2_id() {
//		return creteria2_id;
//	}
//
//	public void setCreteria2_id(String creteria2_id) {
//		this.creteria2_id = creteria2_id;
//	}
//
//	public String getCreteria3_id() {
//		return creteria3_id;
//	}
//
//	public void setCreteria3_id(String creteria3_id) {
//		this.creteria3_id = creteria3_id;
//	}
//
//	public String getCreteria4_id() {
//		return creteria4_id;
//	}
//
//	public void setCreteria4_id(String creteria4_id) {
//		this.creteria4_id = creteria4_id;
//	}
//
//	public String getCreteria5_id() {
//		return creteria5_id;
//	}
//
//	public void setCreteria5_id(String creteria5_id) {
//		this.creteria5_id = creteria5_id;
//	}
//
//	public String getCreteria6_id() {
//		return creteria6_id;
//	}
//
//	public void setCreteria6_id(String creteria6_id) {
//		this.creteria6_id = creteria6_id;
//	}
//
//	public String getCreteria7_id() {
//		return creteria7_id;
//	}
//
//	public void setCreteria7_id(String creteria7_id) {
//		this.creteria7_id = creteria7_id;
//	}
//
//	public String getCreteria8_id() {
//		return creteria8_id;
//	}
//
//	public void setCreteria8_id(String creteria8_id) {
//		this.creteria8_id = creteria8_id;
//	}
//
//	public String getCreteria9_id() {
//		return creteria9_id;
//	}
//
//	public void setCreteria9_id(String creteria9_id) {
//		this.creteria9_id = creteria9_id;
//	}
//
//	public String getDayfrom_id() {
//		return dayfrom_id;
//	}
//
//	public void setDayfrom_id(String dayfrom_id) {
//		this.dayfrom_id = dayfrom_id;
//	}
//
//	public String getDayto_id() {
//		return dayto_id;
//	}
//
//	public void setDayto_id(String dayto_id) {
//		this.dayto_id = dayto_id;
//	}
//
//	public String getMountfrom_id() {
//		return mountfrom_id;
//	}
//
//	public void setMountfrom_id(String mountfrom_id) {
//		this.mountfrom_id = mountfrom_id;
//	}
//
//	public String getMountto_id() {
//		return mountto_id;
//	}
//
//	public void setMountto_id(String mountto_id) {
//		this.mountto_id = mountto_id;
//	}
//
//	public String getYearfrom_id() {
//		return yearfrom_id;
//	}
//
//	public void setYearfrom_id(String yearfrom_id) {
//		this.yearfrom_id = yearfrom_id;
//	}
//
//	public String getYearto_id() {
//		return yearto_id;
//	}
//
//	public void setYearto_id(String yearto_id) {
//		this.yearto_id = yearto_id;
//	}

	public String getUser1_id() {
		return user1_id;
	}

	public void setUser1_id(String user1_id) {
		this.user1_id = user1_id;
	}

	public String getUser2_id() {
		return user2_id;
	}

	public void setUser2_id(String user2_id) {
		this.user2_id = user2_id;
	}

	public String getCatalog_parent_id() {
		return catalog_parent_id;
	}

	public void setCatalog_parent_id(String catalog_parent_id) {
		this.catalog_parent_id = catalog_parent_id;
	}

	public String getPartCriteria(String _criteriaId, boolean isSpace) {

		try {
			if (Long.parseLong(_criteriaId) < 0)
				_criteriaId = "0";
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return !isSpace ? "" : " and catalog_id = " + _criteriaId;
	}

	public String getType1_id() {
		return type1_id;
	}

	public void setType1_id(String type1_id) {
		this.type1_id = type1_id;
	}

	public String getType2_id() {
		return type2_id;
	}

	public void setType2_id(String type2_id) {
		this.type2_id = type2_id;
	}

//	public String getFromCost() {
//		return fromCost;
//	}
//
//	public void setFromCost(String fromCost) {
//		this.fromCost = fromCost;
//	}
//
//	public String getToCost() {
//		return toCost;
//	}
//
//	public void setToCost(String toCost) {
//		this.toCost = toCost;
//	}
//
//	public java.util.Calendar getCalendar() {
//		return calendar;
//	}
//
//	public void setCalendar(java.util.Calendar calendar) {
//		this.calendar = calendar;
//	}

	public String getQuery_productlist() {
		return query_productlist;
	}

	public void setQuery_productlist(String query_productlist) {
		this.query_productlist = query_productlist;
	}

	public String getSelect_currency_cd() {
		return select_currency_cd;
	}

	public void setSelect_currency_cd(String select_currency_cd) {
		this.select_currency_cd = select_currency_cd;
	}

	public String getSelect_path() {
		return select_path;
	}

	public void setSelect_path(String select_path) {
		this.select_path = select_path;
	}

	public String getAllFoundProducts() {
		return allFoundProducts;
	}

	public void setAllFoundProducts(String allFoundProducts) {
		this.allFoundProducts = allFoundProducts;
	}

	public int getPagecount_blog() {
		return pagecount_blog;
	}

	public void setPagecount_blog(int pagecount_blog) {
		this.pagecount_blog = pagecount_blog;
	}

	public String getDialog() {
		return dialog;
	}

	public void setDialog(String dialog) {
		this.dialog = dialog;
	}

	public String getAdvancedSearchOpen() {
		return advancedSearchOpen;
	}

	public void setAdvancedSearchOpen(String advancedSearchOpen) {
		this.advancedSearchOpen = advancedSearchOpen;
	}

	public String getForumOpen() {
		return forumOpen;
	}

	public void setForumOpen(String forumOpen) {
		this.forumOpen = forumOpen;
	}
}
