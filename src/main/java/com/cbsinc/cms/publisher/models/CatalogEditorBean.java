package com.cbsinc.cms.publisher.models;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbsinc.cms.publisher.dao.TransactionSupport;
import com.cbsinc.cms.publisher.dao.WebControls;

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
public class CatalogEditorBean extends WebControls {

	private static final long serialVersionUID = 7657808971053499899L;

	public String[][] rows = new String[10][2];

	private String listup = "";

	private String listdown = "";

	private Integer offset = 0;

	private String type_id = "1";

	private Integer intLevelUp = 0;

	private String phonetype_id = "-1";

	private String progname_id = "-1";

	private String phonemodel_id = "-1";

	private String licence_id = "-1";

	private String imgname;

	private String image_id;

	private String img_url;

	private String cururl;

	private String catalog_id = "-1";

	private String site_id = "0";

	private XLogger log = XLoggerFactory.getXLogger(CatalogEditorBean.class.getName());
	  
	@Autowired
	private TransactionSupport transactionSupport;
	
	
	public String getTable(String strUser_id) {
		if (strUser_id == null || strUser_id.length() == 0)
			strUser_id = "0";

		cururl = "CatalogEditor.jsp?offset=" + offset + "&catalog_id=" + catalog_id + "&phonetype_id=" + phonetype_id
				+ "&licence_id=" + licence_id;

		listup = "CatalogEditor.jsp?offset=" + (offset + 10); 
		if (offset - 10 < 0)
			listdown = "Softlisting.jsp?offset=0"; 
		else
			listdown = "CatalogEditor.jsp?offset=" + (offset - 10); 

		String strSoftName = "";
		String strSoftURL = "";
		String strImgURL = "";
		String strSoftDescription = "";
		String strSoftVersion = "";
		String strSoftCost = "";
		String strCurrency = "";
		StringBuffer table = new StringBuffer();
		

		String query = "";

		query = "SELECT  \"soft\".\"soft_id\", \"soft\".\"name\",\"soft\".\"description\", \"soft\".\"version\", \"soft\".\"cost\", \"soft\".\"currency\", \"soft\".\"serial_nubmer\", \"soft\".\"file_id\", \"soft\".\"type_id\", \"soft\".\"active\" , \"soft\".\"phonetype_id\" , \"soft\".\"progname_id\"  , \"soft\".\"image_id\" , \"images\".\"img_url\" FROM \"soft\" LEFT  JOIN \"images\" ON \"soft\".\"image_id\" = \"images\".\"image_id\" WHERE \"soft\".\"catalog_id\" = "
				+ catalog_id + " and \"soft\".\"phonetype_id\" = " + phonetype_id + " and  \"soft\".\"licence_id\" = "
				+ licence_id + " and  \"soft\".\"phonemodel_id\" = " + phonemodel_id + " limit 10 offset " + offset;

		try {
			List<Map<String,Object>> rowsList = transactionSupport.getJdbcTemplate().queryForList(query);

			table.append(
					"<TABLE ALIGN=\"CENTER\" WIDTH=\"100%\"   border=\"1\"  CELLSPACING=\"0\" CELLPADDING=\"2\">\n");

			if (intLevelUp == 2) {
				table.append("<TR BGCOLOR=\"#808080\" >" + "<TD>Soft name </TD>" + "<TD>Description  </TD>"
						+ "<TD>Version </TD>" + "<TD>Cost </TD>" + "<TD>Currency </TD>"
						+ "<TD><A HREF=\"PostManager.jsp\" ><img SRC=\"images/post.jpg\" border=\"0\" alt=\"Post\" ></A></TD>"
						+ "</TR>\n");
			} else {
				table.append("<TR BGCOLOR=\"#808080\" >" + "<TD height=\"20%\" > Soft name </TD>"
						+ "<TD height=\"40%\" > Description </TD>" + "<TD height=\"5%\" > Version </TD>"
						+ "<TD height=\"5%\" > Cost </TD>" + "<TD height=\"20%\" > Currency </TD>"
						+ "<TD height=\"10%\" > </TD>" + "</TR>\n");
			}
			if (rowsList.size() < 10) {
				table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>"
						+ "</TR>\n");
			} else {
				table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>"
						+ "<TD><a href=\"" + listup + "\">Next 10</a>  </TD>" + "</TR>\n");
			}
			for (int i = 0; rowsList.size() > i; i++) {
				rows[i][0] = (String) rowsList.get(i).get("soft.soft_id");
				rows[i][1] = (String) rowsList.get(i).get("soft.file_id");
				strSoftName = (String)  rowsList.get(i).get("soft.name");;
				strSoftURL = "Policy.jsp?row=" + i;
				img_url = (String)  rowsList.get(i).get("images.img_url");
				if (img_url != null)
					strImgURL = img_url;
				else
					strImgURL = "images/Folder.jpg";
				strSoftDescription = (String) rowsList.get(i).get("soft.description");
				strSoftVersion = (String) rowsList.get(i).get("soft.version");
				strSoftCost = (String) rowsList.get(i).get("soft.cost");
				strCurrency = (String) rowsList.get(i).get("soft.currency");
				phonetype_id = (String) rowsList.get(i).get("soft.phonetype_id");
				progname_id = (String)  rowsList.get(i).get("soft.progname_id");
				image_id = (String)rowsList.get(i).get("soft.image_id");
				table.append("<TR>" + "<TD><A HREF=\"" + strSoftURL + "\"><IMG height=23 alt=\"\" src=\"" + strImgURL
						+ "\" width=25 border=0>" + strSoftName + "</TD>" + "<TD>" + strSoftDescription + "</TD>"
						+ "<TD>" + strSoftVersion + "</TD>" + "<TD>" + strSoftCost + "</TD>" + "<TD>" + strCurrency
						+ "</TD>" + "<TD></TD>" + "</TR>\n");
			}
			table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\""
					+ listdown + "\">Back 10</a>  </TD>" + "</TR>\n");
			table.append("</TABLE>\n");
		}  catch (Exception ex) {
			log.error(ex.getLocalizedMessage(),ex);
		} finally {
			log.exit();
		}
		return table.toString();
	}



	public int stringToInt(String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = 0;
		}
		return i;
	}



	public String[][] getRows() {
		return rows;
	}



	public void setRows(String[][] rows) {
		this.rows = rows;
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



	public Integer getOffset() {
		return offset;
	}



	public void setOffset(Integer offset) {
		this.offset = offset;
	}



	public String getType_id() {
		return type_id;
	}



	public void setType_id(String type_id) {
		this.type_id = type_id;
	}



	public Integer getIntLevelUp() {
		return intLevelUp;
	}



	public void setIntLevelUp(Integer intLevelUp) {
		this.intLevelUp = intLevelUp;
	}



	public String getPhonetype_id() {
		return phonetype_id;
	}



	public void setPhonetype_id(String phonetype_id) {
		this.phonetype_id = phonetype_id;
	}



	public String getProgname_id() {
		return progname_id;
	}



	public void setProgname_id(String progname_id) {
		this.progname_id = progname_id;
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



	public String getImgname() {
		return imgname;
	}



	public void setImgname(String imgname) {
		this.imgname = imgname;
	}



	public String getImage_id() {
		return image_id;
	}



	public void setImage_id(String image_id) {
		this.image_id = image_id;
	}



	public String getImg_url() {
		return img_url;
	}



	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}



	public String getCururl() {
		return cururl;
	}



	public void setCururl(String cururl) {
		this.cururl = cururl;
	}



	public String getCatalog_id() {
		return catalog_id;
	}



	public void setCatalog_id(String catalog_id) {
		this.catalog_id = catalog_id;
	}



	public String getSite_id() {
		return site_id;
	}



	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}



	public XLogger getLog() {
		return log;
	}



	public void setLog(XLogger log) {
		this.log = log;
	}



	public TransactionSupport getTransactionSupport() {
		return transactionSupport;
	}



	public void setTransactionSupport(TransactionSupport transactionSupport) {
		this.transactionSupport = transactionSupport;
	}



	@Override
	public String toString() {
		return "CatalogEditorBean [rows=" + Arrays.toString(rows) + ", listup=" + listup + ", listdown=" + listdown
				+ ", offset=" + offset + ", type_id=" + type_id + ", intLevelUp=" + intLevelUp + ", phonetype_id="
				+ phonetype_id + ", progname_id=" + progname_id + ", phonemodel_id=" + phonemodel_id + ", licence_id="
				+ licence_id + ", imgname=" + imgname + ", image_id=" + image_id + ", img_url=" + img_url + ", cururl="
				+ cururl + ", catalog_id=" + catalog_id + ", site_id=" + site_id + ", log=" + log
				+ ", transactionSupport=" + transactionSupport + "]";
	}

	

}
