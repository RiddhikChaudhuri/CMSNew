package com.cbsinc.cms.publisher.models;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PolicyBean  implements	java.io.Serializable {

	private static final long serialVersionUID = -4538742603892275854L;

	//static private Logger log = Logger.getLogger(PolicyBean.class);
	private XLogger log = XLoggerFactory.getXLogger(PolicyBean.class.getName());

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

	
	private Long portlettype_id = new Long(0);
	
	private Long intUserID = new Long(0);

	private Integer intLevelUp = 0;

	private String imgname;

	private String image_id = "0";

	private String img_url;

	private String cururl;

	private String strLogin = "";

	private String curency_name = "$";

	private Integer intRow_id = 0;

	private String catalog_id = "1";

	private String licence_id = "1";

	private String site_id = "0";

	private String productName;

	private String productURL;

	private String imgURL;

	private String bigimgURL;

	private String productDescription;

	private String productVersion;

	private String productCost = "0";

	private String currency_cd = "";

	private String owner;

	private String product_id = "0";

	private String currency_desc = "";

	private String file_exist = "";
	
	private String type_page = "";

	private String statistic = "0" ;
	
	private String strCDate =null ;
	
	private String select_currencies = "" ;
	
	private String balans = "0" ;

	
	private String parent_product_id = "0";
	
	private String rating1_xml = "" ;
	
	private String back_url = "" ;
	
	public Boolean internet = true ;
	
	
//	 CHECKED
	private String strShow_forum = "false" ;
	private String strShow_ratimg1 = "false" ;
	private String strShow_ratimg2 = "false" ;
	private String strShow_ratimg3 = "false" ;
	private String strSoftName2 = "";
	private String amount1 = "0" ;
	private String amount2 = "0" ;
	private String amount3 = "0" ;
	private String jsp_url = "" ;
	private String strSearch2 = "" ;
	
	private String  creator_info_user_id = "0" ;
	
	public Boolean  fistOpen = true ;
	
	public List ext1Adp = new LinkedList();
	
	public List extFilesAdp = new LinkedList();
	public List extTabsAdp = new LinkedList();
	public List ext2Adp = new LinkedList();
	public List blogExtAdp = new LinkedList();
	public List newsAdp  = new LinkedList();
	public List bottomAdp = new LinkedList();
	
	public String[][] bottom_rows = new String[10][2];
	
	public String[][] ext1_rows = new String[20][2];

	public String[][] ext2_rows = new String[20][2];
	
	public String[][] ext_files_rows = new String[20][2];
	
	public String[][] ext_tabs_rows = new String[20][2];
	
	public String[][] blog_rows = new String[20][2];
	
	public String[][] newsrows = new String[10][2];
	
	private Integer pagecount_ext1 = 0;

	private Integer pagecount_ext2 = 0;
	
	private Integer pagecount_ext_files  = 0;
	
	private Integer pagecount_ext_tabs = 0;
	
	private Integer pagecount_blog = 0;
	
	private  GetValueTool tool = new GetValueTool();
	
	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;
	
	public PolicyBean() 
	{
		//calendarCDate = java.util.Calendar.getInstance();
	}
	
	public Long getPortlettype_id() {
		return portlettype_id;
	}

	public void setPortlettype_id(Long portlettype_id) {
		this.portlettype_id = portlettype_id;
	}
	
	// --------- Business logic functionality start -----

	public int stringToInt(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = 0;
		}
		return i;
	}

	// --------- Business logic functionality end -----
	
	

	public void setIntLevelUp(int intLevelUp) {
		this.intLevelUp = intLevelUp;
	}

	public int getIntLevelUp() {
		return intLevelUp;
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

	public String getStrLogin() {
		return strLogin;
	}

	public void setStrLogin(String strLogin) {
		this.strLogin = strLogin;
	}

	public String getCurency_name() {
		return curency_name;
	}

	public void setCurency_name(String curency_name) {
		this.curency_name = curency_name;
	}

	public void setIntUserID(long intUserID) {
		this.intUserID = intUserID;
	}

	public long getIntUserID() {
		return intUserID;
	}

	public void setRow_id(int intRow_id) {
		this.intRow_id = intRow_id;
	}

	public int getRow_id() {
		return intRow_id;
	}

	public String getCatalog_id() {
		return catalog_id;
	}

	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}

	public String getLicence_id() {
		return licence_id;
	}

	public void setLicence_id(String licence_id) {
		this.licence_id = licence_id;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductURL() {
		return productURL;
	}

	public void setProductURL(String productURL) {
		this.productURL = productURL;
	}

	public String getImgURL() {
		return imgURL;
	}

	public void setImgURL(String imgURL) {
		this.imgURL = imgURL;
	}

	public String getProductDescription() {
		return productDescription;
	}

	public void setProductDescription(String productDescription) {
		this.productDescription = productDescription;
	}

	public String getProductVersion() {
		return productVersion;
	}

	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}

	public String getProductCost() {
		return productCost;
	}

	public void setProductCost(String productCost) {
		this.productCost = productCost;
	}

	public String getCurrency_cd() {
		return currency_cd;
	}

	public void setCurrency_cd(String currency_cd) {
		this.currency_cd = currency_cd;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getCurrency_desc() {
		return currency_desc;
	}

	public void setCurrency_desc(String currency_desc) {
		this.currency_desc = currency_desc;
	}

	public void setIntRow_id(int intRow_id) {
		this.intRow_id = intRow_id;
	}

	public int getIntRow_id() {
		return intRow_id;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getImg_url() {
		return img_url;
	}

	public String getBigimgURL() {
		return bigimgURL;
	}

	public void setBigimgURL(String bigimgURL) {
		this.bigimgURL = bigimgURL;
	}

	public String getFile_exist() {
		return file_exist;
	}

	public void setFile_exist(String file_exist) {
		this.file_exist = file_exist;
	}

	public String getType_page() {
		return type_page;
	}

	public void setType_page(String type_page) {
		this.type_page = type_page;
	}

	public String getStatistic() {
		return statistic;
	}

	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}

	public String getStrCDate() {
		return strCDate;
	}

	public void setStrCDate(String strCDate) {
		this.strCDate = strCDate;
	}

	public String getSelect_currencies() {
		return select_currencies;
	}

	public void setSelect_currencies(String select_currencies) {
		this.select_currencies = select_currencies;
	}




	public String getBalans() {
		return balans;
	}




	public void setBalans(String balans) {
		this.balans = balans;
	}




	public String getParent_product_id() {
		return parent_product_id;
	}




	public void setParent_product_id(String parent_product_id) {
		this.parent_product_id = parent_product_id;
	}




	public String getBack_url() {
		return back_url;
	}




	public void setBack_url(String back_url) {
		this.back_url = back_url;
	}




	public String getRating1_xml() {
		return rating1_xml;
	}




	public void setRating1_xml(String rating1_xml) {
		this.rating1_xml = rating1_xml;
	}




	public String getAmount1() {
		return amount1;
	}




	public void setAmount1(String amount1) {
		this.amount1 = amount1;
	}




	public String getAmount2() {
		return amount2;
	}




	public void setAmount2(String amount2) {
		this.amount2 = amount2;
	}




	public String getAmount3() {
		return amount3;
	}




	public void setAmount3(String amount3) {
		this.amount3 = amount3;
	}




	public boolean isInternet() {
		return internet;
	}




	public void setInternet(boolean isInternet) {
		this.internet = isInternet;
	}




	public String getJsp_url() {
		return jsp_url;
	}




	public void setJsp_url(String jsp_url) {
		this.jsp_url = jsp_url;
	}









	public String getStrSearch2() {
		return strSearch2;
	}




	public void setStrSearch2(String strSearch2) {
		this.strSearch2 = strSearch2;
	}




	public String getStrShow_forum() {
		return strShow_forum;
	}




	public void setStrShow_forum(String strShow_forum) {
		this.strShow_forum = strShow_forum;
	}




	public String getStrShow_ratimg1() {
		return strShow_ratimg1;
	}




	public void setStrShow_ratimg1(String strShow_ratimg1) {
		this.strShow_ratimg1 = strShow_ratimg1;
	}



	public String getStrShow_ratimg2() {
		return strShow_ratimg2;
	}

	public void setStrShow_ratimg2(String strShow_ratimg2) {
		this.strShow_ratimg2 = strShow_ratimg2;
	}



	public String getStrShow_ratimg3() {
		return strShow_ratimg3;
	}


	public void setStrShow_ratimg3(String strShow_ratimg3) {
		this.strShow_ratimg3 = strShow_ratimg3;
	}


	public String getStrSoftName2() {
		return strSoftName2;
	}




	public void setStrSoftName2(String strSoftName2) {
		this.strSoftName2 = strSoftName2;
	}




	public String getCreator_info_user_id() {
		return creator_info_user_id;
	}




	public void setCreator_info_user_id(String creator_info_user_id) {
		this.creator_info_user_id = creator_info_user_id;
	}

	public boolean isFistOpen() {
		return fistOpen;
	}

	public void setFistOpen(boolean fistOpen) {
		this.fistOpen = fistOpen;
	}

	/**
	 * Extention info 2 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id
	 * @return
	 */
	
	public String getExtPolicyFilesProductlist(String strUser_id, String site_id , String tree_id) 
	{
		if (strUser_id == null || strUser_id.length() == 0)	strUser_id = "0";
		ext_files_rows = new String[tool.getRowCount(extFilesAdp)][2];
		StringBuffer table = new StringBuffer();
		table.append("<extpolicy_file_list>\n");
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
		
	
		try
		{
		if (intLevelUp == 2)	setPost_manager("PostManager.jsp");
		else setPost_manager("");
	
		pagecount_ext_files = tool.getRowCount(extFilesAdp);
		for (int i = 0; tool.getRowCount(extFilesAdp) > i; i++) 
		{
			ext_files_rows[i][0] = (String) tool.getValueAt(extFilesAdp,i, 0);
			ext_files_rows[i][1] = tool.getValueAt( extFilesAdp,i, 7) == null ? "" : tool.getValueAt( extFilesAdp,i, 7);
			String  product_name = (String) tool.getValueAt( extFilesAdp,i, 1);
			String attache_file = "downloadservletbyrowid?productid=" + (String) tool.getValueAt( extFilesAdp,i, 0);
			//product_url = "Policy.jsp?ext_files_row=" + i;
			String product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt( extFilesAdp,i, 0);
			
			String product_iconurl = (String) tool.getValueAt( extFilesAdp,i, 13);
			if (product_iconurl == null ) product_iconurl = "images/Folder.jpg";
			
			img_url = (String) tool.getValueAt( extFilesAdp,i, 15);
			if (img_url == null ) img_url = "images/Folder.jpg";
			String file_exist1 = "" ;
			if (ext_files_rows[i][1].length() > 0) file_exist1 = "true" ;
			
			
			String product_description = (String) tool.getValueAt( extFilesAdp,i, 2);
			String product_version = (String) tool.getValueAt( extFilesAdp,i, 3);
			String product_cost = (String) tool.getValueAt( extFilesAdp,i, 4);
			String currency_id = (String) tool.getValueAt( extFilesAdp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			image_id = (String) tool.getValueAt( extFilesAdp,i, 12);
			String product_fulldescription = (String) tool.getValueAt( extFilesAdp,i, 14);
			String user1_id = (String)tool.getValueAt( extFilesAdp,i,16) ;
			String image_type = img_url.substring(img_url.indexOf(".") + 1 ) ;
			
			table.append("<extpolicy_file>\n");
			table.append("<product_id>" + ext_files_rows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image>" + img_url + "</image>\n");
			table.append("<image_type>" + image_type + "</image_type>\n");
			table.append("<big_image_type>"+  img_url.substring(img_url.indexOf(".") + 1 ) + "</big_image_type>\n");
			table.append("<icon_type>"+  product_iconurl.substring(product_iconurl.indexOf(".") + 1 ) + "</icon_type>\n");
			table.append("<user_id>" + user1_id + "</user_id>\n");
			// Referece to pruduct
			table.append("<policy_url>" + product_url + "</policy_url>\n");
			table.append("<product_url>" + attache_file + "</product_url>\n");
			table.append("<description>" + product_description	+ "</description>\n");
			// table.append("<fulldescription>" + product_fulldescription +
			// "</fulldescription>\n") ;
			table.append("<amount>" + product_cost + "</amount>\n");
			table.append("<currency>\n");
			table.append("<code>" + currency_cd + "</code>\n");
			table.append("<description>dollar us</description>\n");
			table.append("</currency>\n");
			table.append("<version>" + currency_desc + "</version>\n");
			table.append("</extpolicy_file>\n");
			}
		table.append("</extpolicy_file_list>\n");
		}
		catch (Exception ex) 
		{
		log.error(ex.getLocalizedMessage());
		}
		
		return table.toString();
	}

	/**
	 * Extention info 1 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id
	 * @return
	 */
	
	public String getExtPolicyOneProductlist(String strUser_id, String site_id,
			String tree_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";
	
		
		ext1_rows = new String[tool.getRowCount(ext1Adp)][2];
		
		StringBuffer table = new StringBuffer();
		table.append("<extpolicy_productlist1>\n");
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
		
	
		try
		{
		if (intLevelUp == 2)
			setPost_manager("PostManager.jsp");
		else
			setPost_manager("");
	
		pagecount_ext1 = tool.getRowCount(ext1Adp);
		for (int i = 0; tool.getRowCount(ext1Adp) > i; i++) {
			ext1_rows[i][0] = (String)  tool.getValueAt(ext1Adp,i, 0);
			ext1_rows[i][1] =  tool.getValueAt(ext1Adp,i, 7) == null ? "" : tool.getValueAt(ext1Adp,i, 7);
			
			String file_exist1 = "" ;
			if (ext1_rows[i][1].length() > 0) file_exist1 = "true" ;
			// rows[i][1] = tool.getValueAt(Adp,i, 7) ; //== null
			// ?"":Adp.getValueAt(i, 7) ;
	
			String product_name = (String)  tool.getValueAt(ext1Adp,i, 1);
			//strSoftURL = "downloadservlet?row=" + i + "&dev=html" ;;
			String attache_file = "downloadservletbyrowid?productid=" + (String)  tool.getValueAt(ext1Adp,i, 0);
			//// strSoftURL = "downloadservlet?row=" + i ;
			//product_url = "Policy.jsp?ext1_row=" + i;
			String product_url = "Policy.jsp?policy_byproductid=" + (String)  tool.getValueAt(ext1Adp,i, 0);
	
			String product_iconurl = (String)  tool.getValueAt(ext1Adp,i, 13);
			if (product_iconurl == null ) product_iconurl = "images/Folder.jpg";
			
			img_url = (String)  tool.getValueAt(ext1Adp,i, 15);
			if (img_url == null ) img_url = "images/Folder.jpg";
		
			
			String product_description = (String)  tool.getValueAt(ext1Adp,i, 2);
			String product_version = (String)  tool.getValueAt(ext1Adp,i, 3);
			String product_cost = (String)  tool.getValueAt(ext1Adp,i, 4);
			String currency_id = (String)  tool.getValueAt(ext1Adp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			// Currency curr = CurrencyHash.getCurrency(currency_id);
			// if(curr == null) throw new
			// java.lang.UnsupportedOperationException("Currency curr == null
			// ");
			// currency_cd = curr.getCode();
			// currency_cd = curr.getCode();
			image_id = (String)  tool.getValueAt(ext1Adp,i, 12);
			String product_fulldescription = (String)  tool.getValueAt(ext1Adp,i, 14);
			String user1_id = (String)tool.getValueAt( ext1Adp,i,16) ;
			table.append("<extpolicy_product1>\n");
			table.append("<product_id>" + ext1_rows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image>" + img_url + "</image>\n");
			table.append("<big_image_type>"+  img_url.substring(img_url.indexOf(".") + 1 ) + "</big_image_type>\n");
			table.append("<icon_type>"+  product_iconurl.substring(product_iconurl.indexOf(".") + 1 ) + "</icon_type>\n");
			table.append("<user_id>" + user1_id + "</user_id>\n");
			// Referece to pruduct
			table.append("<policy_url>" + product_url + "</policy_url>\n");
			table.append("<product_url>" + attache_file + "</product_url>\n");
			table.append("<description>" + product_description	+ "</description>\n");
			// table.append("<fulldescription>" + product_fulldescription +
			// "</fulldescription>\n") ;
			table.append("<amount>" + product_cost + "</amount>\n");
			table.append("<currency>\n");
			table.append("<code>" + currency_cd + "</code>\n");
			table.append("<description>dollar us</description>\n");
			table.append("</currency>\n");
			table.append("<version>" + currency_desc + "</version>\n");
			table.append("</extpolicy_product1>\n");
		   }
		  table.append("</extpolicy_productlist1>\n");
		}
		catch (Exception ex) 
		{
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
	
	public String getExtPolicyTabsProductlist(String strUser_id, String site_id , String tree_id) 
	{
	
		if (strUser_id == null || strUser_id.length() == 0)	strUser_id = "0";
		ext_tabs_rows = new String[tool.getRowCount(extTabsAdp)][2];
		StringBuffer table = new StringBuffer();
		table.append("<extpolicy_list_tabs>\n");
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
		
	
		try
		{
		if (intLevelUp == 2)	setPost_manager("PostManager.jsp");
		else setPost_manager("");
	
		pagecount_ext_tabs = tool.getRowCount(extTabsAdp);
		for (int i = 0; tool.getRowCount(extTabsAdp) > i; i++) 
		{
			ext_tabs_rows[i][0] = (String) tool.getValueAt( extTabsAdp,i, 0);
			ext_tabs_rows[i][1] = tool.getValueAt( extTabsAdp,i, 7) == null ? "" : tool.getValueAt( extTabsAdp,i, 7);
			String product_name = (String) tool.getValueAt( extTabsAdp,i, 1);
			String attache_file = "downloadservletbyrowid?productid=" +  (String) tool.getValueAt( extTabsAdp,i, 0);
			//product_url = "Policy.jsp?ext_tabls_row=" + i;
			String product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt( extTabsAdp,i, 0);
	
			
			String product_iconurl = (String) tool.getValueAt( extFilesAdp,i, 13);
			if (product_iconurl == null ) product_iconurl = "images/Folder.jpg";
			
			img_url = (String) tool.getValueAt( extTabsAdp,i, 15);
			if (img_url == null ) img_url = "images/Folder.jpg";
			String file_exist1 = "" ;
			if (ext_tabs_rows[i][1].length() > 0) file_exist1 = "true" ;
			
			String product_description = (String) tool.getValueAt( extTabsAdp,i, 2);
			String product_version = (String) tool.getValueAt( extTabsAdp,i, 3);
			String product_cost = (String) tool.getValueAt( extTabsAdp,i, 4);
			String currency_id = (String) tool.getValueAt( extTabsAdp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			image_id = (String) tool.getValueAt( extTabsAdp,i, 12);
			String product_fulldescription = (String) tool.getValueAt( extTabsAdp,i, 14);
			String user1_id = (String)tool.getValueAt( extTabsAdp,i,16) ;
			table.append("<extpolicy_tab>\n");
			table.append("<product_id>" + ext_tabs_rows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image>" + img_url + "</image>\n");
			table.append("<big_image_type>"+  img_url.substring(img_url.indexOf(".") + 1 ) + "</big_image_type>\n");
			table.append("<icon_type>"+  product_iconurl.substring(product_iconurl.indexOf(".") + 1 ) + "</icon_type>\n");
	
			table.append("<user_id>" + user1_id + "</user_id>\n");
			// Referece to pruduct
			table.append("<policy_url>" + product_url + "</policy_url>\n");
			table.append("<product_url>" + attache_file + "</product_url>\n");
			table.append("<description>" + product_description	+ "</description>\n");
			// table.append("<fulldescription>" + product_fulldescription +
			// "</fulldescription>\n") ;
			table.append("<amount>" + product_cost + "</amount>\n");
			table.append("<currency>\n");
			table.append("<code>" + currency_cd + "</code>\n");
			table.append("<description>dollar us</description>\n");
			table.append("</currency>\n");
			table.append("<version>" + currency_desc + "</version>\n");
			table.append("</extpolicy_tab>\n");
			}
		table.append("</extpolicy_list_tabs>\n");
		}
		catch (Exception ex) 
		{
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
	
	public String getExtPolicyTwoProductlist(String strUser_id, String site_id,
			String tree_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";
	
		
		ext2_rows = new String[tool.getRowCount(ext2Adp)][2];
		
		StringBuffer table = new StringBuffer();
		table.append("<extpolicy_productlist2>\n");
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
		
	
		try
		{
		if (intLevelUp == 2)	setPost_manager("PostManager.jsp");
		else setPost_manager("");
	
		pagecount_ext2 = tool.getRowCount(ext2Adp);
		for (int i = 0; tool.getRowCount(ext2Adp) > i; i++) 
		{
			ext2_rows[i][0] = (String)  tool.getValueAt(ext2Adp,i, 0);
			ext2_rows[i][1] =  tool.getValueAt(ext2Adp,i, 7) == null ? "" :  tool.getValueAt(ext2Adp,i, 7);
			String file_exist2 = "" ;
			if (ext2_rows[i][1].length() > 0) file_exist2 = "true" ;
			
			String product_name = (String)  tool.getValueAt(ext2Adp,i, 1);
			String attache_file = "downloadservletbyrowid?productid=" + (String)  tool.getValueAt(ext2Adp,i, 0);
			//product_url = "Policy.jsp?ext2_row=" + i;
			String product_url = "Policy.jsp?policy_byproductid=" + (String)  tool.getValueAt(ext2Adp,i, 0);
	
			
			String product_iconurl = (String)  tool.getValueAt(ext2Adp,i, 13);
			if (product_iconurl == null ) product_iconurl = "images/Folder.jpg";
			
			img_url = (String)  tool.getValueAt(ext2Adp,i, 15);
			if (img_url == null ) img_url = "images/Folder.jpg";
	
			
			String product_description = (String)  tool.getValueAt(ext2Adp,i, 2);
			String product_version = (String)  tool.getValueAt(ext2Adp,i, 3);
			String product_cost = (String)  tool.getValueAt(ext2Adp,i, 4);
			String currency_id = (String)  tool.getValueAt(ext2Adp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			image_id = (String)  tool.getValueAt(ext2Adp,i, 12);
			String product_fulldescription = (String)  tool.getValueAt(ext2Adp,i, 14);
			String user1_id = (String)tool.getValueAt( ext2Adp,i,16) ;
			table.append("<extpolicy_product2>\n");
			table.append("<product_id>" + ext2_rows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<file_exist>" + file_exist2 + "</file_exist>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image>" + img_url + "</image>\n");
			table.append("<big_image_type>"+  img_url.substring(img_url.indexOf(".") + 1 ) + "</big_image_type>\n");
			table.append("<icon_type>"+  product_iconurl.substring(product_iconurl.indexOf(".") + 1 ) + "</icon_type>\n");
			table.append("<user_id>" + user1_id + "</user_id>\n");
			// Referece to pruduct
			table.append("<policy_url>" + product_url + "</policy_url>\n");
			table.append("<product_url>" + attache_file + "</product_url>\n");
			table.append("<description>" + product_description
					+ "</description>\n");
			// table.append("<fulldescription>" + product_fulldescription +
			// "</fulldescription>\n") ;
			table.append("<amount>" + product_cost + "</amount>\n");
			table.append("<currency>\n");
			table.append("<code>" + currency_cd + "</code>\n");
			table.append("<description>dollar us</description>\n");
			table.append("</currency>\n");
			table.append("<version>" + currency_desc + "</version>\n");
			table.append("</extpolicy_product2>\n");
			}
		table.append("</extpolicy_productlist2>\n");
		}
		catch (Exception ex) 
		{
		log.error(ex.getLocalizedMessage());
		}
		
		return table.toString();
	}

	public String[][] getExt_files_rows() {
		return ext_files_rows;
	}

	public void setExt_files_rows(String[][] ext_files_rows) {
		this.ext_files_rows = ext_files_rows;
	}

	public String[][] getExt_tabs_rows() {
		return ext_tabs_rows;
	}

	public void setExt_tabs_rows(String[][] ext_tabs_rows) {
		this.ext_tabs_rows = ext_tabs_rows;
	}

	
	public int getPagecount_ext1() {
		return pagecount_ext1;
	}

	public void setPagecount_ext1(int pagecount_ext1) {
		this.pagecount_ext1 = pagecount_ext1;
	}

	public int getPagecount_ext2() {
		return pagecount_ext2;
	}

	public void setPagecount_ext2(int pagecount_ext2) {
		this.pagecount_ext2 = pagecount_ext2;
	}
	
	
	public int getPagecount_ext_files() {
		return pagecount_ext_files;
	}

	public void setPagecount_ext_files(int pagecount_ext_files) {
		this.pagecount_ext_files = pagecount_ext_files;
	}

	public int getPagecount_ext_tabs() {
		return pagecount_ext_tabs;
	}

	public void setPagecount_ext_tabs(int pagecount_ext_tabs) {
		this.pagecount_ext_tabs = pagecount_ext_tabs;
	}

	public String getPost_manager() {
		return post_manager;
	}

	public void setPost_manager(String post_manager) {
		this.post_manager = post_manager;
	}
	
	public int getPagecount_blog() {
		return pagecount_blog;
	}

	public void setPagecount_blog(int pagecount_blog) {
		this.pagecount_blog = pagecount_blog;
	}
	/**
	 * Extention info 2 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id
	 * @return
	 */
	
	public String getBlogExtPolicyProductlist(String strUser_id, String site_id,
			String tree_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";
	
		
		blog_rows = new String[tool.getRowCount(blogExtAdp)][2];
		StringBuffer table = new StringBuffer();
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
		
	
		try
		{
		if (intLevelUp == 2)	setPost_manager("PostManager.jsp");
		else setPost_manager("");
	
		pagecount_blog = tool.getRowCount(blogExtAdp);
		for (int i = 0; tool.getRowCount(blogExtAdp) > i; i++) 
		{
			blog_rows[i][0] = (String) tool.getValueAt( blogExtAdp,i, 0);
			blog_rows[i][1] = tool.getValueAt( blogExtAdp,i, 7) == null ? "" : tool.getValueAt( blogExtAdp,i, 7);
			String product_name = (String) tool.getValueAt( blogExtAdp,i, 1);
			String product_url = "Policy.jsp?blog_row=" + i;
	
			img_url = (String) tool.getValueAt( blogExtAdp,i, 13);
			String product_iconurl = "" ;
			if (img_url != null) product_iconurl = img_url;
			else product_iconurl = "images/Folder.jpg";
			
			String product_description = (String) tool.getValueAt( blogExtAdp,i, 2);
			String product_version = (String) tool.getValueAt( blogExtAdp,i, 3);
			String product_cost = (String) tool.getValueAt( blogExtAdp,i, 4);
			String currency_id = (String) tool.getValueAt( blogExtAdp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			image_id = (String) tool.getValueAt( blogExtAdp,i, 12);
			String product_fulldescription = (String) tool.getValueAt( blogExtAdp,i, 14);
			String user1_id = (String)tool.getValueAt( blogExtAdp,i,16) ;
			
			String strCDate = (String) tool.getValueAt( blogExtAdp,i, 17);
			//if(strCDate.length() > 10) strCDate = strCDate.substring(0,10) ;
			String statistic = (String) tool.getValueAt( blogExtAdp,i, 18);
			String firstName = (String) tool.getValueAt( blogExtAdp,i, 19);
			String lastName = (String) tool.getValueAt( blogExtAdp,i, 20);
			String company = (String) tool.getValueAt( blogExtAdp,i, 21);
			
			
			table.append("<product_blog>\n");
			table.append("<product_id>" + blog_rows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image></image>\n");
			table.append("<user_id>" + user1_id + "</user_id>\n");
			table.append("<author>" + firstName + " " + lastName + "</author>\n");
			table.append("<company>" + company + "</company>\n");
			// Referece to pruduct
			table.append("<policy_url>" + product_url + "</policy_url>\n");
			table.append("<description>" + product_description
					+ "</description>\n");
			// table.append("<fulldescription>" + product_fulldescription +
			// "</fulldescription>\n") ;
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
		}
		catch (Exception ex) 
		{
		log.error(ex.getLocalizedMessage());
		}
		
		return table.toString();
	}

	
	public String getNewslist(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)	strUser_id = "0";
		this.site_id = site_id;
		newsrows = new String[tool.getRowCount(newsAdp)][2];
		StringBuffer table = new StringBuffer();
		table.append("<newslist>\n");

		try
		{
		for (int i = 0; tool.getRowCount( newsAdp) > i; i++) {
			newsrows[i][0] = (String)  tool.getValueAt(newsAdp,i, 0);
			newsrows[i][1] =  tool.getValueAt(newsAdp,i, 7) == null ? "" : tool.getValueAt(newsAdp,i, 7);

			String product_name = (String)  tool.getValueAt(newsAdp,i, 1);
			///product_url = "Policy.jsp?news=" + i;
			String product_url = "Policy.jsp?policy_byproductid=" + (String)  tool.getValueAt(newsAdp,i, 0);
			
			String  product_iconurl = (String)  tool.getValueAt(newsAdp,i, 13);
			if (product_iconurl == null ) product_iconurl = "images/Folder.jpg";
			
			img_url = (String)  tool.getValueAt(newsAdp,i, 15);
			if (img_url == null ) img_url = "images/Folder.jpg";
			
			String file_exist1 = "" ;
			if (newsrows[i][1].length() > 0) file_exist1 = "true" ;
			
			String product_description = (String)  tool.getValueAt(newsAdp,i, 2);
			String product_version = (String)  tool.getValueAt(newsAdp,i, 3);
			String product_cost = (String)  tool.getValueAt(newsAdp,i, 4);
			String currency_id = (String)  tool.getValueAt(newsAdp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			image_id = (String)  tool.getValueAt(newsAdp,i, 12);
			String product_fulldescription = (String)  tool.getValueAt(newsAdp,i, 14);
			String user1_id = (String)tool.getValueAt(newsAdp,i,16) ;

			table.append("<news>\n");
			table.append("<product_id>" + newsrows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image>" + img_url + "</image>\n");
			table.append("<big_image_type>"+  img_url.substring(img_url.indexOf(".") + 1 ) + "</big_image_type>\n");
			table.append("<icon_type>"+  product_iconurl.substring(product_iconurl.indexOf(".") + 1 ) + "</icon_type>\n");
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
		}
		catch (Exception ex) 
		{
		log.error(ex.getLocalizedMessage());
		}
		return table.toString();
	}
	
	
	public String getBottomList(String strUser_id, String site_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		
		bottom_rows = new String[tool.getRowCount(bottomAdp)][2];
		String product_name ;
		int pagecount_bottom ;
		String product_url ;
		String product_iconurl ;
		String product_description ;
		String product_version ;
		String product_cost ;
		String currency_id ;
		String product_fulldescription ;
		String user1_id ;
		StringBuffer table = new StringBuffer();
		
		table.append("<bottomlist>\n");
		
		
		try
		{
		if (intLevelUp == 2) setPost_manager("PostManager.jsp");
		else setPost_manager("");
		pagecount_bottom = tool.getRowCount(bottomAdp);

		for (int i = 0; tool.getRowCount(bottomAdp) > i; i++) {
			bottom_rows[i][0] = (String) tool.getValueAt( bottomAdp,i, 0);
			bottom_rows[i][1] = tool.getValueAt( bottomAdp,i, 7) == null ? "" : tool.getValueAt( bottomAdp,i, 7);
			product_name = (String) tool.getValueAt( bottomAdp,i, 1);
			String attache_file = "downloadservletbyrowid?productid=" + (String) tool.getValueAt( bottomAdp,i, 0);
			//product_url = "Policy.jsp?co1_row=" + i;
			product_url = "Policy.jsp?policy_byproductid=" + (String) tool.getValueAt( bottomAdp,i, 0);
			
			product_iconurl = (String) tool.getValueAt( bottomAdp,i, 13);
			if (product_iconurl == null ) product_iconurl = "images/Folder.jpg";
			
			img_url = (String) tool.getValueAt( bottomAdp,i, 15);
			if (img_url == null ) img_url = "images/Folder.jpg";
			String file_exist1 = "" ;
			if (bottom_rows[i][1].length() > 0) file_exist1 = "true" ;
			
			product_description = (String) tool.getValueAt( bottomAdp,i, 2);
			product_version = (String) tool.getValueAt( bottomAdp,i, 3);
			product_cost = (String) tool.getValueAt( bottomAdp,i, 4);
			currency_id = (String) tool.getValueAt( bottomAdp,i, 5);
			CurrencyHash currencyHash = CurrencyHash.getInstance() ;
			currency_desc = currencyHash.getCurrency_decs(currency_id);
			image_id = (String) tool.getValueAt( bottomAdp,i, 12);
			product_fulldescription = (String) tool.getValueAt( bottomAdp,i, 14);
			user1_id = (String)tool.getValueAt( bottomAdp,i,16) ;
			
			table.append("<bottom>\n");
			table.append("<product_id>" + bottom_rows[i][0] + "</product_id>\n");
			table.append("<row_id>" + i + "</row_id>\n");
			table.append("<file_exist>" + file_exist1 + "</file_exist>\n");
			table.append("<name>" + product_name + "</name>\n");
			table.append("<icon>" + product_iconurl + "</icon>\n");
			table.append("<image>" + img_url + "</image>\n");
			table.append("<big_image_type>"+  img_url.substring(img_url.indexOf(".") + 1 ) + "</big_image_type>\n");
			table.append("<icon_type>"+  product_iconurl.substring(product_iconurl.indexOf(".") + 1 ) + "</icon_type>\n");
			table.append("<user_id>" + user1_id + "</user_id>\n");
			// Referece to pruduct
			table.append("<policy_url>" + product_url + "</policy_url>\n");
			table.append("<product_url>" + attache_file + "</product_url>\n");
			table.append("<description>" + product_description
					+ "</description>\n");
			// table.append("<fulldescription>" + product_fulldescription +
			// "</fulldescription>\n") ;
			table.append("<amount>" + product_cost + "</amount>\n");
			table.append("<currency>\n");
			table.append("<code>" + currency_cd + "</code>\n");
			table.append("<description>dollar us</description>\n");
			table.append("</currency>\n");
			table.append("<version>" + currency_desc + "</version>\n");
			table.append("</bottom>\n");
		  }
		table.append("</bottomlist>\n");
		}
		catch (Exception ex) 
		{
		log.error(ex.getLocalizedMessage());
		}
		return table.toString();
	}
	
	private String post_manager = "";
	private String select_tree_catalog = "" ;
	private String select_catalogXMLUrlPath = "" ;
	
	public String getSelect_tree_catalog() {
		return select_tree_catalog;
	}

	public void setSelect_tree_catalog(String select_tree_catalog) {
		this.select_tree_catalog = select_tree_catalog;
	}

	public String getSelectCatalogXMLUrlPath() {
		return select_catalogXMLUrlPath;
	}

	public void setSelectCatalogXMLUrlPath(String catalogXMLUrlPath) {
		this.select_catalogXMLUrlPath = catalogXMLUrlPath;
	}


	public String getTrueValue(String tmp1, String tmp2, boolean b) {
		if (b)
			return tmp1;
		else
			return tmp2;
	}
	
	private String select_menu_catalog ;
	
	public String getSelect_menu_catalog() {
		return select_menu_catalog;
	}

	public void setSelect_menu_catalog(String select_menu_catalog) {
		this.select_menu_catalog = select_menu_catalog;
	}

}
