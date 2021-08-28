package com.cbsinc.cms.publisher.models;

import java.io.*;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.stereotype.Component;
import com.cbsinc.cms.publisher.dao.WebControls;

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
@Component
public class SoftPostBean  extends WebControls implements
		java.io.Serializable {


	private static final long serialVersionUID = -4418477184811109279L;

	//static private Logger log = Logger.getLogger(SoftPostBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(SoftPostBean.class.getName());
	
	
	

	private String sample = "Start value";

	private String strSoftName = "";

	private String strSoftURL = "";

	public String strSoftDescription = "";

	private String strSoftVersion = "";

	private String strSoftCost = "0";

	private String strCurrency = "0";

	private String strOwner = "";

	private String serial_nubmer = "";

	private String file_id = "-1";

	private String type_id = "-1";

	private String filename = "-1";

	private String phonetype_id = "-1";

	private String progname_id = "-1";

	private String image_id = "-1";

	private String bigimage_id = "-1";

	private String imgname = "";

	private String bigimgname = "";

	private String user_id = "-1";

	private String phonemodel_id = "-1";

	private String licence_id = "-1";

	//private String catalog_id = "-1";

	private String site_id = "0";

	private String salelogic_id = "0";

	private String card_number = "0";

	private String card_code = "0";

	private String type_card_id = "0";

	private String product_code_id = "0";

	public String product_fulldescription = "";

	private String portlettype_id = "0";

	private String parent_portlettype_id = "0";
	
	public String getParent_portlettype_id() {
		return parent_portlettype_id;
	}

	public void setParent_portlettype_id(String parent_portlettype_id) {
		this.parent_portlettype_id = parent_portlettype_id;
	}

	private String soft_id = "-1";

	private String creteria1_id = "0";

	private String creteria2_id = "0";

	private String creteria3_id = "0";

	private String creteria4_id = "0";

	private String creteria5_id = "0";

	private String creteria6_id = "0";

	private String creteria7_id = "0";

	private String creteria8_id = "0";

	private String creteria9_id = "0";

	private String creteria10_id = "0";

	private String day_id = "0";

	private String mount_id = "0";

	private String year_id = "0";

	private String save = "false";
	
	private String action = "" ;
	
	// CHECKED
	private String strShow_forum = "false" ;
	//private String show_forum_checked = "CHECKED" ;
	private String show_forum_checked = "" ;
	
	private String strShow_ratimg1 = "false" ;
	//private String strShow_ratimg1_checked = "CHECKED" ;
	private String strShow_ratimg1_checked = "" ;
	
	
	private String strShow_ratimg2 = "false" ;
	//private String strShow_ratimg2_checked = "CHECKED" ;
	private String strShow_ratimg2_checked = "" ;
	
	private String strShow_ratimg3 = "false" ;
	private String strShow_ratimg3_checked = "" ;
	//private String strShow_ratimg3_checked = "CHECKED" ;
	
	
	private String strSoftName2 = "";
	
	private String amount1 = "0" ;
	private String amount2 = "0" ;
	private String amount3 = "0" ;

	private String jsp_url = "" ;
	
	
	private String strSearch2 = "" ;
	
	
	private String select_big_images = "" ;
	private String select_small_images = "" ;
	
	private String select_big_image_url = "" ;
	private String select_small_image_url = "" ;
	private String select_files = "" ;
	
	private String criteria1_label = "" ;
	private String criteria2_label = "" ;
	private String criteria3_label = "" ;
	private String criteria4_label = "" ;
	private String criteria5_label = "" ;
	private String criteria6_label = "" ;
	private String criteria7_label = "" ;
	private String criteria8_label = "" ;
	private String criteria9_label = "" ;
	private String criteria10_label = "" ;
	
	
	private String creteria1_name = "" ;
	private String creteria2_name = "" ;
	private String creteria3_name = "" ;
	private String creteria4_name = "" ;
	private String creteria5_name = "" ;
	private String creteria6_name = "" ;
	private String creteria7_name = "" ;
	private String creteria8_name = "" ;
	private String creteria9_name = "" ;
	private String creteria10_name = "" ;
	
	private String select_currency_cd = "" ;
	private String select_tree_catalog = "" ;
	private String select_creteria1_id = "" ;
	private String select_creteria2_id = "" ;
	private String select_creteria3_id = "" ;
	private String select_creteria4_id = "" ;
	private String select_creteria5_id = "" ;
	private String select_creteria6_id = "" ;
	private String select_creteria7_id = "" ;
	private String select_creteria8_id = "" ;
	private String select_creteria9_id = "" ;
	private String select_creteria10_id = "" ;

	
	private String select_dayfrom_id = "" ;
	private String select_mountfrom_id = "" ;
	private String select_yearfrom_id = "" ;
	private String select_dayto_id = "" ;
	private String select_mountto_id = "" ;
	private String select_yearto_id = "" ;
	
	private String select_path= "" ;
	
	private String nameOfPage= "" ;
	
	public String getNameOfPage() {
		return nameOfPage;
	}

	public void setNameOfPage(String nameOfPage) {
		this.nameOfPage = nameOfPage;
	}

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

	public void setStrSoftName(String strSoftName)
			throws UnsupportedEncodingException {
		// this.strSoftName = norm(strSoftName) ;
		this.strSoftName = strSoftName;
	}

	public String getStrSoftName() {
		return strSoftName;
	}

	public void setStrSoftURL(String strSoftURL) {
		this.strSoftURL = strSoftURL;
	}

	public String getStrSoftURL() {
		return strSoftURL;
	}

	public void setStrSoftDescription(String strSoftDescription) {
		try
		{
			if (strSoftDescription.indexOf("\n") != -1) {
			strSoftDescription = "\n<r>"
					+ strSoftDescription.replaceAll("\n", "</r><r>");
			if (strSoftDescription.substring(strSoftDescription.length() - 3,
					strSoftDescription.length()).equals("<r>")) {
				strSoftDescription = strSoftDescription.substring(0,
						strSoftDescription.length() - 3);
			} else
				strSoftDescription = strSoftDescription + "</r>";
			} else {
				strSoftDescription = "\n<r>" + strSoftDescription + "</r>";
			}
			strSoftDescription = strSoftDescription.replaceAll("&", "and");
			strSoftDescription = strSoftDescription.replaceAll("'", "`");
			this.strSoftDescription = strSoftDescription;
		
		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage());
		}

	}

	public String getStrSoftDescription() {
		// BASE64Encoder encoder = new BASE64Encoder();
		// return encoder.encode(strSoftDescription.getBytes()) ;
		try
		{
		strSoftDescription = strSoftDescription.replaceAll("</r><r>", "\n");
		strSoftDescription = strSoftDescription.replaceAll("<r>", "");
		strSoftDescription = strSoftDescription.replaceAll("</r>", "");
		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage());
		}
		return this.strSoftDescription.trim();
	}

	public void setStrSoftVersion(String strSoftVersion) {
		this.strSoftVersion = strSoftVersion;
	}

	public String getStrSoftVersion() {
		return strSoftVersion;
	}

	public void setStrSoftCost(String strSoftCost) {
		this.strSoftCost = strSoftCost;
	}

	public String getStrSoftCost() {
		return strSoftCost;
	}

	public void setStrCurrency(String strCurrency) {
		this.strCurrency = strCurrency;
	}

	public String getStrCurrency() {
		return strCurrency;
	}

	public void setStrOwner(String strOwner) {
		this.strOwner = strOwner;
	}

	public String getStrOwner() {
		return strOwner;
	}

	
	
	
		
	
	
	public void setSerial_nubmer(String serial_nubmer) {
		this.serial_nubmer = serial_nubmer;
	}

	public String getSerial_nubmer() {
		return serial_nubmer;
	}

	public void setFile_id(String file_id) {
		if( file_id.equals(""))  file_id = "-1" ;
		this.file_id = file_id;
	}

	public String getFile_id() {
		return file_id;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_id() {
		return type_id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getFilename() {
		return filename;
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

	public void setImage_id(String image_id) {
		if( image_id.equals(""))  image_id = "-1" ;
		this.image_id = image_id;
	}

	public String getImage_id() {
		return image_id;
	}

	public void setImgname(String imgname) {
		this.imgname = imgname;
	}

	public String getImgname() {
		return imgname;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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


	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getSalelogic_id() {
		return salelogic_id;
	}

	public void setSalelogic_id(String salelogic_id) {
		if(salelogic_id.equals(""))salelogic_id = "0" ;
		this.salelogic_id = salelogic_id;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_code() {
		return card_code;
	}

	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}

	public String getType_card_id() {
		return type_card_id;
	}

	public void setType_card_id(String type_card_id) {
		this.type_card_id = type_card_id;
	}

	public String getProduct_code_id() {
		return product_code_id;
	}

	public void setProduct_code_id(String product_code_id) {
		if(product_code_id.equals(""))product_code_id = "0" ;
		this.product_code_id = product_code_id;
	}

	public String getProduct_fulldescription() {

		try
		{
		product_fulldescription = product_fulldescription.replaceAll("</r><r>","\n");
		product_fulldescription = product_fulldescription.replaceAll("<r>", "");
		product_fulldescription = product_fulldescription.replaceAll("</r>", "");
		}
		catch (Exception ex) 
		{
		 logger.error(ex.getLocalizedMessage());
		}

		return product_fulldescription.trim();
	}

	public void setProduct_fulldescription(String product_fulldescription) {

	 try
	 {
		if (strSoftDescription.indexOf("\n") != -1) {
			product_fulldescription = "\n<r>"
					+ product_fulldescription.replaceAll("\n", "</r><r>");
			if (product_fulldescription.substring(
					product_fulldescription.length() - 3,
					product_fulldescription.length()).equals("<r>")) {
				product_fulldescription = product_fulldescription.substring(0,
						product_fulldescription.length() - 3);
			} else
				product_fulldescription = product_fulldescription + "</r>";
		} else {
			product_fulldescription = "\n<r>" + product_fulldescription
					+ "</r>";
		}
		product_fulldescription = product_fulldescription
				.replaceAll("&", "and");
		product_fulldescription = product_fulldescription.replaceAll("'", "`");
		this.product_fulldescription = product_fulldescription;

	 }
	 catch (Exception ex) 
	 {
	  logger.error(ex.getLocalizedMessage());
	 }
	}

	public String getBigimage_id() {
		return bigimage_id;
	}

	public void setBigimage_id(String bigimage_id) {
		if( bigimage_id.equals(""))  bigimage_id = "-1" ;
		this.bigimage_id = bigimage_id;
	}

	public String getBigimgname() {
		return bigimgname;
	}

	public void setBigimgname(String bigimgname) {
		this.bigimgname = bigimgname;
	}

	public String getPortlettype_id() {
		return portlettype_id;
	}

	public void setPortlettype_id(String portlettype_id) {
		this.portlettype_id = portlettype_id;
	}

	public String getSoft_id() {
		return soft_id;
	}

	public void setSoft_id(String soft_id) {
		this.soft_id = soft_id;
	}

	public String getCreteria10_id() {
		return creteria10_id;
	}

	public void setCreteria10_id(String creteria10_id) {
		if( creteria10_id.equals(""))  creteria10_id = "0" ;
		this.creteria10_id = creteria10_id;
	}

	public String getCreteria1_id() {
		return creteria1_id;
	}

	public void setCreteria1_id(String creteria1_id) {
		if( creteria1_id.equals(""))  creteria1_id = "0" ;
		this.creteria1_id = creteria1_id;
	}

	public String getCreteria2_id() {
		return creteria2_id;
	}

	public void setCreteria2_id(String creteria2_id) {
		if( creteria2_id.equals(""))  creteria2_id = "0" ;
		this.creteria2_id = creteria2_id;
	}

	public String getCreteria3_id() {
		return creteria3_id;
	}

	public void setCreteria3_id(String creteria3_id) {
		if( creteria3_id.equals(""))  creteria3_id = "0" ;
		this.creteria3_id = creteria3_id;
	}

	public String getCreteria4_id() {
		return creteria4_id;
	}

	public void setCreteria4_id(String creteria4_id) {
		if( creteria4_id.equals(""))  creteria4_id = "0" ;
		this.creteria4_id = creteria4_id;
	}

	public String getCreteria5_id() {
		return creteria5_id;
	}

	public void setCreteria5_id(String creteria5_id) {
		if( creteria5_id.equals(""))  creteria5_id = "0" ;
		this.creteria5_id = creteria5_id;
	}

	public String getCreteria6_id() {
		return creteria6_id;
	}

	public void setCreteria6_id(String creteria6_id) {
		if( creteria6_id.equals(""))  creteria6_id = "0" ;
		this.creteria6_id = creteria6_id;
	}

	public String getCreteria7_id() {
		return creteria7_id;
	}

	public void setCreteria7_id(String creteria7_id) {
		if( creteria7_id.equals(""))  creteria7_id = "0" ;
		this.creteria7_id = creteria7_id;
	}

	public String getCreteria8_id() {
		return creteria8_id;
	}

	public void setCreteria8_id(String creteria8_id) {
		if( creteria8_id.equals(""))  creteria8_id = "0" ;
		this.creteria8_id = creteria8_id;
	}

	public String getCreteria9_id() {
		return creteria9_id;
	}

	public void setCreteria9_id(String creteria9_id) {
		if( creteria9_id.equals(""))  creteria9_id = "0" ;
		this.creteria9_id = creteria9_id;
	}

	public String getDay_id() {
		return day_id;
	}

	public void setDay_id(String day_id) {
		this.day_id = day_id;
	}

	public String getMount_id() {
		return mount_id;
	}

	public void setMount_id(String mount_id) {
		this.mount_id = mount_id;
	}

	public String getYear_id() {
		return year_id;
	}

	public void setYear_id(String year_id) {
		this.year_id = year_id;
	}

	public String getSave() {
		return save;
	}

	public void setSave(String save) {
		this.save = save;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	public String getPartCriteria(String _criteriaId , boolean  isSpace )
	{
		
	 try
	 {
		if( Long.parseLong(_criteriaId)  < 0 )   _criteriaId = "0" ;
	 }
	 catch (Exception ex) 
	 {
	  logger.error(ex.getLocalizedMessage());
	 }
		return !isSpace?"":" and catalog_id = " + _criteriaId ;	
	}

	public String getStrShow_forum() {

		if(strShow_forum.equals("true")) show_forum_checked= "CHECKED" ;
		if(strShow_forum.equals("false")) show_forum_checked= "" ;

		return strShow_forum;
	}

	public void setStrShow_forum(String strShow_forum) 
	{
		this.strShow_forum = strShow_forum;
	}

	

	public String getStrSoftName2() {
		return strSoftName2;
	}

	public void setStrSoftName2(String strSoftName2) {
		this.strSoftName2 = strSoftName2;
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

	public String getShow_forum_checked() 
	{
		if( strShow_forum.equals("true")) { setShow_forum_checked("CHECKED") ; } 
		return show_forum_checked;
	}

	public void setShow_forum_checked(String show_forum_checked) {
		this.show_forum_checked = show_forum_checked;
	}

	public String getStrShow_ratimg1_checked() {
		
		if( strShow_ratimg1.equals("true")) { setStrShow_ratimg1_checked("CHECKED") ; } 
		return strShow_ratimg1_checked;
	}

	public void setStrShow_ratimg1_checked(String strShow_ratimg1_checked) {
		this.strShow_ratimg1_checked = strShow_ratimg1_checked;
	}

	public String getStrShow_ratimg2_checked() {
		if( strShow_ratimg2.equals("true")) { setStrShow_ratimg2_checked("CHECKED") ; } 
		return strShow_ratimg2_checked;
	}

	public void setStrShow_ratimg2_checked(String strShow_ratimg2_checked) {
		this.strShow_ratimg2_checked = strShow_ratimg2_checked;
	}

	public String getStrShow_ratimg3_checked() {
		if( strShow_ratimg3.equals("true")) { setStrShow_ratimg3_checked("CHECKED") ; } 
		return strShow_ratimg3_checked;
	}

	public void setStrShow_ratimg3_checked(String strShow_ratimg3_checked) {
		this.strShow_ratimg3_checked = strShow_ratimg3_checked;
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

	public String getCreteria1_name() {
		return creteria1_name;
	}

	public void setCreteria1_name(String creteria1_name) {
		this.creteria1_name = creteria1_name;
	}

	public String getCreteria10_name() {
		return creteria10_name;
	}

	public void setCreteria10_name(String creteria10_name) {
		this.creteria10_name = creteria10_name;
	}

	public String getCreteria2_name() {
		return creteria2_name;
	}

	public void setCreteria2_name(String creteria2_name) {
		this.creteria2_name = creteria2_name;
	}

	public String getCreteria3_name() {
		return creteria3_name;
	}

	public void setCreteria3_name(String creteria3_name) {
		this.creteria3_name = creteria3_name;
	}

	public String getCreteria4_name() {
		return creteria4_name;
	}

	public void setCreteria4_name(String creteria4_name) {
		this.creteria4_name = creteria4_name;
	}

	public String getCreteria5_name() {
		return creteria5_name;
	}

	public void setCreteria5_name(String creteria5_name) {
		this.creteria5_name = creteria5_name;
	}

	public String getCreteria6_name() {
		return creteria6_name;
	}

	public void setCreteria6_name(String creteria6_name) {
		this.creteria6_name = creteria6_name;
	}

	public String getCreteria7_name() {
		return creteria7_name;
	}

	public void setCreteria7_name(String creteria7_name) {
		this.creteria7_name = creteria7_name;
	}

	public String getCreteria8_name() {
		return creteria8_name;
	}

	public void setCreteria8_name(String creteria8_name) {
		this.creteria8_name = creteria8_name;
	}

	public String getCreteria9_name() {
		return creteria9_name;
	}

	public void setCreteria9_name(String creteria9_name) {
		this.creteria9_name = creteria9_name;
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

	public String getSelect_big_images() {
		return select_big_images;
	}

	public void setSelect_big_images(String select_big_images) {
		this.select_big_images = select_big_images;
	}

	public String getSelect_creteria1_id() {
		return select_creteria1_id;
	}

	public void setSelect_creteria1_id(String select_creteria1_id) {
		this.select_creteria1_id = select_creteria1_id;
	}

	public String getSelect_creteria10_id() {
		return select_creteria10_id;
	}

	public void setSelect_creteria10_id(String select_creteria10_id) {
		this.select_creteria10_id = select_creteria10_id;
	}

	public String getSelect_creteria2_id() {
		return select_creteria2_id;
	}

	public void setSelect_creteria2_id(String select_creteria2_id) {
		this.select_creteria2_id = select_creteria2_id;
	}

	public String getSelect_creteria3_id() {
		return select_creteria3_id;
	}

	public void setSelect_creteria3_id(String select_creteria3_id) {
		this.select_creteria3_id = select_creteria3_id;
	}

	public String getSelect_creteria4_id() {
		return select_creteria4_id;
	}

	public void setSelect_creteria4_id(String select_creteria4_id) {
		this.select_creteria4_id = select_creteria4_id;
	}

	public String getSelect_creteria5_id() {
		return select_creteria5_id;
	}

	public void setSelect_creteria5_id(String select_creteria5_id) {
		this.select_creteria5_id = select_creteria5_id;
	}

	public String getSelect_creteria6_id() {
		return select_creteria6_id;
	}

	public void setSelect_creteria6_id(String select_creteria6_id) {
		this.select_creteria6_id = select_creteria6_id;
	}

	public String getSelect_creteria7_id() {
		return select_creteria7_id;
	}

	public void setSelect_creteria7_id(String select_creteria7_id) {
		this.select_creteria7_id = select_creteria7_id;
	}

	public String getSelect_creteria8_id() {
		return select_creteria8_id;
	}

	public void setSelect_creteria8_id(String select_creteria8_id) {
		this.select_creteria8_id = select_creteria8_id;
	}

	public String getSelect_creteria9_id() {
		return select_creteria9_id;
	}

	public void setSelect_creteria9_id(String select_creteria9_id) {
		this.select_creteria9_id = select_creteria9_id;
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

	public String getSelect_small_images() {
		return select_small_images;
	}

	public void setSelect_small_images(String select_small_images) {
		this.select_small_images = select_small_images;
	}

	public String getSelect_tree_catalog() {
		return select_tree_catalog;
	}

	public void setSelect_tree_catalog(String select_tree_catalog) {
		this.select_tree_catalog = select_tree_catalog;
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

	public String getSelect_big_image_url() {
		return select_big_image_url;
	}

	public void setSelect_big_image_url(String select_big_image_url) {
		this.select_big_image_url = select_big_image_url;
	}

	public String getSelect_files() {
		return select_files;
	}

	public void setSelect_files(String select_files) {
		this.select_files = select_files;
	}

	public String getSelect_small_image_url() {
		return select_small_image_url;
	}

	public void setSelect_small_image_url(String select_small_image_url) {
		this.select_small_image_url = select_small_image_url;
	}

	

	
	
	
}
