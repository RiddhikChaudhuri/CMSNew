package com.cbsinc.cms.publisher.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import com.cbsinc.cms.publisher.dao.RowMappers.XmlDBCriteriaLocale;

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
public class ProductlistBean implements java.io.Serializable {

	private static final long serialVersionUID = -7111281778620430929L;

	//static private Logger log = Logger.getLogger(ProductlistBean.class);
	private XLogger log = XLoggerFactory.getXLogger(ProductlistBean.class.getName());

	private String searchValueArg = "";

	public String[][] rows = new String[10][2];

	public String[][] bottom_rows = new String[10][2];

	public String[][] co1_rows = new String[10][2];

	public String[][] co2_rows = new String[10][2];

	public String[][] blog_rows = new String[20][2];

	public String[][] newsrows = new String[10][2];

	private transient GetValueTool tool = new GetValueTool();

	public List allqueryAdp = new LinkedList();

	public List Adp = new LinkedList();

	public List newsAdp = new LinkedList();

	public List co1Adp = new LinkedList();

	public List co2Adp = new LinkedList();

	public List bottomAdp = new LinkedList();

	private List tmpAdp = new LinkedList();

	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;

	private String type1_id = "1";

	private String type2_id = "1";

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

	private String catalog_parent_id = "0";

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

	private String criteria1_label = "";
	private String criteria2_label = "";
	private String criteria3_label = "";
	private String criteria4_label = "";
	private String criteria5_label = "";
	private String criteria6_label = "";
	private String criteria7_label = "";
	private String criteria8_label = "";
	private String criteria9_label = "";
	private String criteria10_label = "";

	private String creteria1_name = "";
	private String creteria2_name = "";
	private String creteria3_name = "";
	private String creteria4_name = "";
	private String creteria5_name = "";
	private String creteria6_name = "";
	private String creteria7_name = "";
	private String creteria8_name = "";
	private String creteria9_name = "";
	private String creteria10_name = "";

	private String select_currency_cd = "";
	private String select_tree_catalog = "";
	private String select_menu_catalog = "";

	private List<XmlDBCriteriaLocale> select_creteria1_id= new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria2_id = new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria3_id = new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria4_id= new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria5_id = new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria6_id= new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria7_id= new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria8_id = new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria9_id = new ArrayList<XmlDBCriteriaLocale>();
	private List<XmlDBCriteriaLocale> select_creteria10_id = new ArrayList<XmlDBCriteriaLocale>();

	private String select_dayfrom_id = "";
	private String select_mountfrom_id = "";
	private String select_yearfrom_id = "";
	private String select_dayto_id = "";
	private String select_mountto_id = "";
	private String select_yearto_id = "";

	private String select_path = "";

	private String color = "";

	private String allFoundProducts = "";

	String dialog = "true";
	String advancedSearchOpen = "true";
	String forumOpen = "true";

	public Boolean isInternet = true;

	public List blogExtAdp = new LinkedList();

	public ProductlistBean() {

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

				product_name = (String) tool.getValueAt(Adp, i, 1);
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
				image_id = (String) tool.getValueAt(Adp, i, 12);
				product_fulldescription = (String) tool.getValueAt(Adp, i, 14);
				user1_id = (String) tool.getValueAt(Adp, i, 16);

				type1_id = (String) tool.getValueAt(Adp, i, 8);

				creteria1_name = (String) tool.getValueAt(Adp, i, 30);
				creteria2_name = (String) tool.getValueAt(Adp, i, 31);
				creteria3_name = (String) tool.getValueAt(Adp, i, 32);
				creteria4_name = (String) tool.getValueAt(Adp, i, 33);
				creteria5_name = (String) tool.getValueAt(Adp, i, 34);
				creteria6_name = (String) tool.getValueAt(Adp, i, 35);
				creteria7_name = (String) tool.getValueAt(Adp, i, 36);
				creteria8_name = (String) tool.getValueAt(Adp, i, 37);
				creteria9_name = (String) tool.getValueAt(Adp, i, 38);
				creteria10_name = (String) tool.getValueAt(Adp, i, 39);
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
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("<creteria1>" + creteria1_name + "</creteria1>\n");
				table.append("<creteria2>" + creteria2_name + "</creteria2>\n");
				table.append("<creteria3>" + creteria3_name + "</creteria3>\n");
				table.append("<creteria4>" + creteria4_name + "</creteria4>\n");
				table.append("<creteria5>" + creteria5_name + "</creteria5>\n");
				table.append("<creteria6>" + creteria6_name + "</creteria6>\n");
				table.append("<creteria7>" + creteria7_name + "</creteria7>\n");
				table.append("<creteria8>" + creteria8_name + "</creteria8>\n");
				table.append("<creteria9>" + creteria9_name + "</creteria9>\n");
				table.append("<creteria10>" + creteria10_name + "</creteria10>\n");
				table.append("<color>" + color + "</color>\n");
				table.append("</rigth>\n");

				if (tool.getRowCount(Adp) > (i + 1)) {
					product_url2 = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(Adp, i + 1, 0);

					rows[i + 1][0] = (String) tool.getValueAt(Adp, i + 1, 0);
					rows[i + 1][1] = tool.getValueAt(Adp, i + 1, 7) == null ? "" : tool.getValueAt(Adp, i + 1, 7);
					product_name2 = (String) tool.getValueAt(Adp, i + 1, 1);
					img_url2 = (String) tool.getValueAt(Adp, i + 1, 13);
					if (img_url2 != null)
						product_iconurl2 = img_url2;
					else
						product_iconurl2 = "images/Folder.jpg";
					product_description2 = (String) tool.getValueAt(Adp, i + 1, 2);
					product_cost2 = (String) tool.getValueAt(Adp, i + 1, 4);
					currency_id2 = (String) tool.getValueAt(Adp, i + 1, 5);
					currency_desc2 = currencyHash.getCurrency_decs(currency_id2);
					user2_id = (String) tool.getValueAt(Adp, i + 1, 16);
					type2_id = (String) tool.getValueAt(Adp, i + 1, 8);
					creteria1_name = (String) tool.getValueAt(Adp, i + 1, 30);
					creteria2_name = (String) tool.getValueAt(Adp, i + 1, 31);
					creteria3_name = (String) tool.getValueAt(Adp, i + 1, 32);
					creteria4_name = (String) tool.getValueAt(Adp, i + 1, 33);
					creteria5_name = (String) tool.getValueAt(Adp, i + 1, 34);
					creteria6_name = (String) tool.getValueAt(Adp, i + 1, 35);
					creteria7_name = (String) tool.getValueAt(Adp, i + 1, 36);
					creteria8_name = (String) tool.getValueAt(Adp, i + 1, 37);
					creteria9_name = (String) tool.getValueAt(Adp, i + 1, 38);
					creteria10_name = (String) tool.getValueAt(Adp, i + 1, 39);
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
					table.append("<amount>" + product_cost2 + "</amount>\n");
					table.append("<currency>\n");
					table.append("<code>" + currency_cd + "</code>\n");
					table.append("<description>dollar us</description>\n");
					table.append("</currency>\n");
					table.append("<version>" + currency_desc2 + "</version>\n");
					table.append("<creteria1>" + creteria1_name + "</creteria1>\n");
					table.append("<creteria2>" + creteria2_name + "</creteria2>\n");
					table.append("<creteria3>" + creteria3_name + "</creteria3>\n");
					table.append("<creteria4>" + creteria4_name + "</creteria4>\n");
					table.append("<creteria5>" + creteria5_name + "</creteria5>\n");
					table.append("<creteria6>" + creteria6_name + "</creteria6>\n");
					table.append("<creteria7>" + creteria7_name + "</creteria7>\n");
					table.append("<creteria8>" + creteria8_name + "</creteria8>\n");
					table.append("<creteria9>" + creteria9_name + "</creteria9>\n");
					table.append("<creteria10>" + creteria10_name + "</creteria10>\n");
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

				product_name = (String) tool.getValueAt(Adp, i, 1);
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
				image_id = (String) tool.getValueAt(Adp, i, 12);
				product_fulldescription = (String) tool.getValueAt(Adp, i, 14);
				user1_id = (String) tool.getValueAt(Adp, i, 16);
				creteria1_name = (String) tool.getValueAt(Adp, i, 30);
				creteria2_name = (String) tool.getValueAt(Adp, i, 31);
				creteria3_name = (String) tool.getValueAt(Adp, i, 32);
				creteria4_name = (String) tool.getValueAt(Adp, i, 33);
				creteria5_name = (String) tool.getValueAt(Adp, i, 34);
				creteria6_name = (String) tool.getValueAt(Adp, i, 35);
				creteria7_name = (String) tool.getValueAt(Adp, i, 36);
				creteria8_name = (String) tool.getValueAt(Adp, i, 37);
				creteria9_name = (String) tool.getValueAt(Adp, i, 38);
				creteria10_name = (String) tool.getValueAt(Adp, i, 39);
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
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("<creteria1>" + creteria1_name + "</creteria1>\n");
				table.append("<creteria2>" + creteria2_name + "</creteria2>\n");
				table.append("<creteria3>" + creteria3_name + "</creteria3>\n");
				table.append("<creteria4>" + creteria4_name + "</creteria4>\n");
				table.append("<creteria5>" + creteria5_name + "</creteria5>\n");
				table.append("<creteria6>" + creteria6_name + "</creteria6>\n");
				table.append("<creteria7>" + creteria7_name + "</creteria7>\n");
				table.append("<creteria8>" + creteria8_name + "</creteria8>\n");
				table.append("<creteria9>" + creteria9_name + "</creteria9>\n");
				table.append("<creteria10>" + creteria10_name + "</creteria10>\n");
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

	public String getProduct(String _soft_id) {

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

	public String getBottomList(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		bottom_rows = new String[tool.getRowCount(bottomAdp)][2];
		StringBuffer table = new StringBuffer();
		table.append("<bottomlist>\n");

		try {
			if (intLevelUp == 2)
				setPost_manager("PostManager.jsp");
			else
				setPost_manager("");

			for (int i = 0; tool.getRowCount(bottomAdp) > i; i++) {
				bottom_rows[i][0] = (String) tool.getValueAt(bottomAdp, i, 0);
				bottom_rows[i][1] = tool.getValueAt(bottomAdp, i, 7) == null ? "" : tool.getValueAt(bottomAdp, i, 7);
				product_name = (String) tool.getValueAt(bottomAdp, i, 1);
				String attache_file = "downloadservletbyrowid?productid=" + (String) tool.getValueAt(bottomAdp, i, 0);
				product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(bottomAdp, i, 0);

				product_iconurl = (String) tool.getValueAt(bottomAdp, i, 13);
				if (product_iconurl == null)
					product_iconurl = "images/Folder.jpg";

				img_url = (String) tool.getValueAt(bottomAdp, i, 15);
				if (img_url == null)
					img_url = "images/Folder.jpg";
				String file_exist1 = "";
				if (bottom_rows[i][1].length() > 0)
					file_exist1 = "true";

				product_description = (String) tool.getValueAt(bottomAdp, i, 2);
				product_version = (String) tool.getValueAt(bottomAdp, i, 3);
				product_cost = (String) tool.getValueAt(bottomAdp, i, 4);
				currency_id = (String) tool.getValueAt(bottomAdp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				image_id = (String) tool.getValueAt(bottomAdp, i, 12);
				product_fulldescription = (String) tool.getValueAt(bottomAdp, i, 14);
				user1_id = (String) tool.getValueAt(bottomAdp, i, 16);

				table.append("<bottom>\n");
				table.append("<product_id>" + bottom_rows[i][0] + "</product_id>\n");
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
				table.append("</bottom>\n");
			}
			table.append("</bottomlist>\n");
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

	/**
	 * Extention info 2 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id
	 * @return
	 */

	public String getBlogTopProductlist(String site_id) {

		blog_rows = new String[tool.getRowCount(blogExtAdp)][2];
		StringBuilder table = new StringBuilder();
		table.append("<product_blog_list>\n");
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

			pagecount_blog = tool.getRowCount(blogExtAdp);
			for (int i = 0; tool.getRowCount(blogExtAdp) > i; i++) {
				blog_rows[i][0] = (String) tool.getValueAt(blogExtAdp, i, 0);
				blog_rows[i][1] = tool.getValueAt(blogExtAdp, i, 7) == null ? "" : tool.getValueAt(blogExtAdp, i, 7);
				product_name = (String) tool.getValueAt(blogExtAdp, i, 1);
				String parent = (String) tool.getValueAt(blogExtAdp, i, 22);
				String parent_title = (String) tool.getValueAt(blogExtAdp, i, 23);
				product_url = "Policy.jsp?policy_byproductid=" + parent;

				img_url = (String) tool.getValueAt(blogExtAdp, i, 13);
				if (img_url != null)
					product_iconurl = img_url;
				else
					product_iconurl = "images/Folder.jpg";

				String file_exist1 = "";
				if (blog_rows[i][1].length() > 0)
					file_exist1 = "true";

				product_description = (String) tool.getValueAt(blogExtAdp, i, 2);
				product_version = (String) tool.getValueAt(blogExtAdp, i, 3);
				product_cost = (String) tool.getValueAt(blogExtAdp, i, 4);
				currency_id = (String) tool.getValueAt(blogExtAdp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				image_id = (String) tool.getValueAt(blogExtAdp, i, 12);
				product_fulldescription = (String) tool.getValueAt(blogExtAdp, i, 14);
				user1_id = (String) tool.getValueAt(blogExtAdp, i, 16);

				String strCDate = (String) tool.getValueAt(blogExtAdp, i, 17);
				String statistic = (String) tool.getValueAt(blogExtAdp, i, 18);
				String firstName = (String) tool.getValueAt(blogExtAdp, i, 19);
				String lastName = (String) tool.getValueAt(blogExtAdp, i, 20);
				String company = (String) tool.getValueAt(blogExtAdp, i, 21);

				table.append("<product_blog>\n");
				table.append("<product_id>" + blog_rows[i][0] + "</product_id>\n");
				table.append("<row_id>" + i + "</row_id>\n");
				table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
				table.append("<name>" + product_name + "</name>\n");
				table.append("<parent_title>" + parent_title + "</parent_title>\n");
				table.append("<product_parent_id>" + parent + "</product_parent_id>\n");
				table.append("<icon>" + product_iconurl + "</icon>\n");
				table.append("<image></image>\n");
				table.append("<user_id>" + user1_id + "</user_id>\n");
				table.append("<author>" + firstName + " " + lastName + "</author>\n");
				table.append("<company>" + company + "</company>\n");
				table.append("<policy_url>" + product_url + "</policy_url>\n");
				table.append("<description>" + product_description + "</description>\n");
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("<statistic>" + statistic + "</statistic>\n");
				table.append("<cdate>" + strCDate + "</cdate>\n");
				table.append("</product_blog>\n");
			}
			table.append("</product_blog_list>\n");
		} catch (Exception ex) {
			log.error(ex.getLocalizedMessage());
		}

		return table.toString();
	}

	public String getNewslist(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";
		// this.site_id = site_id;
		newsrows = new String[tool.getRowCount(newsAdp)][2];
		StringBuffer table = new StringBuffer();
		table.append("<newslist>\n");

		try {
			for (int i = 0; tool.getRowCount(newsAdp) > i; i++) {
				newsrows[i][0] = (String) tool.getValueAt(newsAdp, i, 0);
				newsrows[i][1] = tool.getValueAt(newsAdp, i, 7) == null ? "" : tool.getValueAt(newsAdp, i, 7);

				product_name = (String) tool.getValueAt(newsAdp, i, 1);

				product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt(newsAdp, i, 0);

				product_iconurl = (String) tool.getValueAt(newsAdp, i, 13);
				if (product_iconurl == null)
					product_iconurl = "images/Folder.jpg";

				img_url = (String) tool.getValueAt(newsAdp, i, 15);
				if (img_url == null)
					img_url = "images/Folder.jpg";

				String file_exist1 = "";
				if (newsrows[i][1].length() > 0)
					file_exist1 = "true";

				product_description = (String) tool.getValueAt(newsAdp, i, 2);
				product_version = (String) tool.getValueAt(newsAdp, i, 3);
				product_cost = (String) tool.getValueAt(newsAdp, i, 4);
				currency_id = (String) tool.getValueAt(newsAdp, i, 5);
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				currency_desc = currencyHash.getCurrency_decs(currency_id);
				image_id = (String) tool.getValueAt(newsAdp, i, 12);
				product_fulldescription = (String) tool.getValueAt(newsAdp, i, 14);
				user1_id = (String) tool.getValueAt(newsAdp, i, 16);

				table.append("<news>\n");
				table.append("<product_id>" + newsrows[i][0] + "</product_id>\n");
				table.append("<row_id>" + i + "</row_id>\n");
				table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
				table.append("<name>" + product_name + "</name>\n");
				table.append("<icon>" + product_iconurl + "</icon>\n");
				table.append("<image>" + img_url + "</image>\n");
				table.append("<big_image_type>" + img_url.substring(img_url.indexOf(".") + 1) + "</big_image_type>\n");
				table.append(
						"<icon_type>" + product_iconurl.substring(product_iconurl.indexOf(".") + 1) + "</icon_type>\n");
				table.append("<user_id>" + user1_id + "</user_id>\n");
				table.append("<policy_url>" + product_url + "</policy_url>\n");
				table.append("<description>" + product_description + "</description>\n");
				table.append("<amount>" + product_cost + "</amount>\n");
				table.append("<currency>\n");
				table.append("<code>" + currency_cd + "</code>\n");
				table.append("<description>dollar us</description>\n");
				table.append("</currency>\n");
				table.append("<version>" + currency_desc + "</version>\n");
				table.append("</news>\n");
			}
			table.append("</newslist>\n");
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

	public long onggetIntLevelUp() {
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

	public String getQuery_productlist() {
		return query_productlist;
	}

	public void setQuery_productlist(String query_productlist) {
		this.query_productlist = query_productlist;
	}

	public String getCriteria1_label() {
		return criteria1_label;
	}

	public void setCriteria1_label(String criteria1_label) {
		this.criteria1_label = criteria1_label;
	}

	public String getCriteria10_label() {
		return criteria10_label;
	}

	public void setCriteria10_label(String criteria10_label) {
		this.criteria10_label = criteria10_label;
	}

	public String getCriteria2_label() {
		return criteria2_label;
	}

	public void setCriteria2_label(String criteria2_label) {
		this.criteria2_label = criteria2_label;
	}

	public String getCriteria3_label() {
		return criteria3_label;
	}

	public void setCriteria3_label(String criteria3_label) {
		this.criteria3_label = criteria3_label;
	}

	public String getCriteria4_label() {
		return criteria4_label;
	}

	public void setCriteria4_label(String criteria4_label) {
		this.criteria4_label = criteria4_label;
	}

	public String getCriteria5_label() {
		return criteria5_label;
	}

	public void setCriteria5_label(String criteria5_label) {
		this.criteria5_label = criteria5_label;
	}

	public String getCriteria6_label() {
		return criteria6_label;
	}

	public void setCriteria6_label(String criteria6_label) {
		this.criteria6_label = criteria6_label;
	}

	public String getCriteria7_label() {
		return criteria7_label;
	}

	public void setCriteria7_label(String criteria7_label) {
		this.criteria7_label = criteria7_label;
	}

	public String getCriteria8_label() {
		return criteria8_label;
	}

	public void setCriteria8_label(String criteria8_label) {
		this.criteria8_label = criteria8_label;
	}

	public String getCriteria9_label() {
		return criteria9_label;
	}

	public void setCriteria9_label(String criteria9_label) {
		this.criteria9_label = criteria9_label;
	}

	public List<XmlDBCriteriaLocale> getSelect_creteria1_id() {
		return select_creteria1_id;
	}
	
    public List<XmlDBCriteriaLocale> getSelect_creteria2_id() {
      return select_creteria2_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria3_id() {
      return select_creteria3_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria4_id() {
      return select_creteria4_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria5_id() {
      return select_creteria5_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria6_id() {
      return select_creteria6_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria7_id() {
      return select_creteria7_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria8_id() {
      return select_creteria8_id;
    }

    public List<XmlDBCriteriaLocale> getSelect_creteria9_id() {
      return select_creteria9_id;
    }
  
    public List<XmlDBCriteriaLocale> getSelect_creteria10_id() {
      return select_creteria10_id;
    }

    public void setSelect_creteria1_id(List<XmlDBCriteriaLocale> select_creteria1_id) {
      this.select_creteria1_id = select_creteria1_id;
    }

    public void setSelect_creteria2_id(List<XmlDBCriteriaLocale> select_creteria2_id) {
      this.select_creteria2_id = select_creteria2_id;
    }

    public void setSelect_creteria3_id(List<XmlDBCriteriaLocale> select_creteria3_id) {
      this.select_creteria3_id = select_creteria3_id;
    }

    public void setSelect_creteria4_id(List<XmlDBCriteriaLocale> select_creteria4_id) {
      this.select_creteria4_id = select_creteria4_id;
    }

    public void setSelect_creteria5_id(List<XmlDBCriteriaLocale> select_creteria5_id) {
      this.select_creteria5_id = select_creteria5_id;
    }

    public void setSelect_creteria6_id(List<XmlDBCriteriaLocale> select_creteria6_id) {
      this.select_creteria6_id = select_creteria6_id;
    }

    public void setSelect_creteria7_id(List<XmlDBCriteriaLocale> select_creteria7_id) {
      this.select_creteria7_id = select_creteria7_id;
    }

    public void setSelect_creteria8_id(List<XmlDBCriteriaLocale> select_creteria8_id) {
      this.select_creteria8_id = select_creteria8_id;
    }

    public void setSelect_creteria9_id(List<XmlDBCriteriaLocale> select_creteria9_id) {
      this.select_creteria9_id = select_creteria9_id;
    }

    public void setSelect_creteria10_id(List<XmlDBCriteriaLocale> select_creteria10_id) {
      this.select_creteria10_id = select_creteria10_id;
    }

	

	public String getSelect_currency_cd() {
		return select_currency_cd;
	}

	public void setSelect_currency_cd(String select_currency_cd) {
		this.select_currency_cd = select_currency_cd;
	}

	public String getSelect_dayfrom_id() {
		return select_dayfrom_id;
	}

	public void setSelect_dayfrom_id(String select_dayfrom_id) {
		this.select_dayfrom_id = select_dayfrom_id;
	}

	public String getSelect_dayto_id() {
		return select_dayto_id;
	}

	public void setSelect_dayto_id(String select_dayto_id) {
		this.select_dayto_id = select_dayto_id;
	}

	public String getSelect_mountfrom_id() {
		return select_mountfrom_id;
	}

	public void setSelect_mountfrom_id(String select_mountfrom_id) {
		this.select_mountfrom_id = select_mountfrom_id;
	}

	public String getSelect_mountto_id() {
		return select_mountto_id;
	}

	public void setSelect_mountto_id(String select_mountto_id) {
		this.select_mountto_id = select_mountto_id;
	}

	public String getSelect_path() {
		return select_path;
	}

	public void setSelect_path(String select_path) {
		this.select_path = select_path;
	}

	public String getSelect_tree_catalog() {
		return select_tree_catalog;
	}

	public void setSelect_tree_catalog(String select_tree_catalog) {
		this.select_tree_catalog = select_tree_catalog;
	}

	public String getSelect_menu_catalog() {
		return select_menu_catalog;
	}

	public void setSelect_menu_catalog(String select_menu_catalog) {
		this.select_menu_catalog = select_menu_catalog;
	}

	public String getSelect_yearfrom_id() {
		return select_yearfrom_id;
	}

	public void setSelect_yearfrom_id(String select_yearfrom_id) {
		this.select_yearfrom_id = select_yearfrom_id;
	}

	public String getSelect_yearto_id() {
		return select_yearto_id;
	}

	public void setSelect_yearto_id(String select_yearto_id) {
		this.select_yearto_id = select_yearto_id;
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
