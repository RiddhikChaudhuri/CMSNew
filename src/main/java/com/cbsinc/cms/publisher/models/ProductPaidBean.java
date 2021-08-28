package com.cbsinc.cms.publisher.models;

import java.util.List;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.cbsinc.cms.publisher.dao.TransactionSupport;

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
public class ProductPaidBean implements java.io.Serializable {

	private static final long serialVersionUID = -361863527175082390L;

	//static private Logger log = Logger.getLogger(ItemDeProductPaidBeanscriptionBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(ProductPaidBean.class.getName());
	  
	@Autowired
	private TransactionSupport transactionSupport;

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

	private String cururl;

	private String catalog_id = "-1";

	private String site_id = "0";

	private String product_id = "0";

	private String row_id = "0";

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	// Access sample property
	// Access sample property

    public String getProductPaid(String strUser_id, String basket_id) {
      logger.entry(strUser_id, basket_id);
      String description;
      String name;


      cururl = "multilangdescription_list.jsp?offset=" + offset + "&catalog_id=" + catalog_id
          + "&phonetype_id=" + phonetype_id + "&licence_id=" + licence_id;

      listup = "multilangdescription_list.jsp?offset=" + (offset + 10); // +
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
      if (offset - 10 < 0)
        listdown = "multilangdescription_list.jsp?offset=0"; // &catalog_id="
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
      else
        listdown = "multilangdescription_list.jsp?offset=" + (offset - 10); // +
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

      StringBuffer table = new StringBuffer();


      String query = "";

      query = "SELECT  " + "  public.multilangdescription.multilangdescription_id, "
          + "  public.multilangdescription.soft_cd, " + "  public.multilangdescription.name, "
          + "  public.multilangdescription.description, " + "  public.multilangdescription.ative "
          + " FROM " + "  public.multilangdescription " + " WHERE "
          + "  public.multilangdescription.ative = true and "
          + "  public.multilangdescription.soft_cd = " + getProduct_id() + "" + "  ORDER BY "
          + "  public.multilangdescription.multilangdescription_id ASC ";


      try {
        List<Map<String, Object>> queryList =
            transactionSupport.getJdbcTemplate().queryForList(query);

        table.append(
            "<TABLE ALIGN=\"CENTER\" WIDTH=\"100%\"   border=\"1\"  CELLSPACING=\"0\" CELLPADDING=\"2\">\n");

        if (intLevelUp == 2) {
          table.append("<TR BGCOLOR=\"#808080\" >" + "<TD>id </TD>" + "<TD>soft_id  </TD>"
              + "<TD>name </TD>" + "<TD>description </TD>"
              + "<TD><a href =\"multilangdescription_add.jsp\">add</a> </TD>" + "</TR>\n");
          // "<TD height=\"*\" border=\"0\" >" + this.getComboBox("type_id",
          // "" + type_id ,"select type_id , type_lable from typesoft where
          // active = true") + "<input type=\"submit\" name=\"Submit\"
          // value=\"OK\"></TD>" +
          // postManager
        } else {
          table.append("<TR BGCOLOR=\"#808080\" >" + "<TD height=\"20%\" > id </TD>"
              + "<TD height=\"40%\" > soft_id </TD>" + "<TD height=\"5%\" > name </TD>"
              + "<TD height=\"5%\" > description </TD>"
              + "<TD><a href =\"multilangdescription_add.jsp\">add</a> </TD>" + "</TR>\n");
          // "<TD height=\"*\" border=\"0\" >" + this.getComboBox("type_id",
          // "" + type_id ,"select type_id , type_lable from typesoft where
          // active = true") + "<input type=\"submit\" name=\"Submit\"
          // value=\"OK\"></TD>" +
        }
        // "<TD><A HREF=\"/JspPostPrediction.jsp?offset=" + offset +
        // "&sport_cd=" + sport_cd + "\" ><img SRC=\"images/post.gif\"
        // border=\"0\" alt=\"Post\" ></A></TD>" +

        if (queryList.size() < 10) {
          table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "</TR>\n");
        } else {
          table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\"" + listup
              + "\">Next 10</a>  </TD>" + "</TR>\n");
        }
        for (int i = 0; queryList.size() > i; i++) {
          rows[i][0] =
              (String) queryList.get(i).get("public.multilangdescription.multilangdescription_id");
          rows[i][1] = (String) queryList.get(i).get("public.multilangdescription.soft_cd");
          product_id = (String) queryList.get(i).get("public.multilangdescription.soft_cd");;
          name = (String) queryList.get(i).get("public.multilangdescription.name");
          description = (String) queryList.get(i).get("public.multilangdescription.description");
          table.append("<TR>" + "<TD>" + rows[i][0] + "</TD>" + "<TD>" + rows[i][1] + "</TD>"
              + "<TD>" + name + "</TD>" + "<TD>" + description + "</TD>"
              + "<TD><a href =\"multilangdescription_edit.jsp?row=" + i + "&name=" + name
              + "&description=" + description + "&product_cd=" + product_id + "\">edit</a> </TD>"
              + "</TR>\n");
        }

        // listup = Adp.rows.
        /*
         * if( Adp.rows().size() < 10 ) { table.append("<TR>" + "<TD></TD>" + "<TD>
         * </TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></
         * TD>" + "</TR>\n"); } else {
         */
        table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>"
            + "<TD><a href=\"" + listdown + "\">Back 10</a>  </TD>" + "</TR>\n");
        // }

        table.append("</TABLE>\n");

      } catch (Exception ex) {

        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
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
		}
		return i;
	}

	public boolean setSelectedDemand() {
		return true;
	}

	public boolean setPassiveDemand() {
		return true;
	}

	public void setType_id(String type_id) {
		this.type_id = type_id;
	}

	public String getType_id() {
		return type_id;
	}

	public void setIntLevelUp(int intLevelUp) {
		this.intLevelUp = intLevelUp;
	}

	public int getIntLevelUp() {
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

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

}
