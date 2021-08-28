package com.cbsinc.cms.publisher.dao;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.cbsinc.cms.dto.CatalogName;
import com.cbsinc.cms.dto.CatalogNameItem;
import com.cbsinc.cms.dto.CatalogSubNameitem;
import com.cbsinc.cms.dto.DayFromJsonList;
import com.cbsinc.cms.dto.DayFromName;
import com.cbsinc.cms.dto.MountFormName;
import com.cbsinc.cms.dto.MountFromJsonList;
import com.cbsinc.cms.dto.NameItem;
import com.cbsinc.cms.dto.SubNameItem;
import com.cbsinc.cms.dto.YearFromJsonList;
import com.cbsinc.cms.dto.YearFromName;
import com.cbsinc.cms.dto.pages.request.SubXMLDB;
import com.cbsinc.cms.dto.pages.request.SubnameItemDto;
import com.cbsinc.cms.publisher.dao.RowMappers.SelectCatalog;
import com.cbsinc.cms.publisher.dao.RowMappers.SelectMenuCatalog;
import com.cbsinc.cms.publisher.dao.RowMappers.XMLDbCriteriaListRowMapper;
import com.cbsinc.cms.publisher.dao.RowMappers.XmlDBCriteriaLocale;
import com.cbsinc.cms.publisher.util.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Repository
public class WebControls implements Serializable {

	private static final long serialVersionUID = 1583778363930770359L;
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

	private static XLogger logger = XLoggerFactory.getXLogger(WebControls.class.getName());
	NumberFormat nf;
	// transient DecimalFormat df ;

	public WebControls() {
		// df= new DecimalFormat("0.##");
		nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setGroupingUsed(true);
	}
	
	@Autowired
	private TransactionSupport transactionSupport;


	
	public String getOneLabel(String query) {
		String name = "";
		try {
			List<String> labellist = transactionSupport.getJdbcTemplate().queryForList(query, String.class);
			if (labellist.size() != 0)
				name = labellist.get(0);
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		} 
		return name;
	}



	public String getXMLListDateDay(String pagejsp, String name, String selected_cd) throws JsonProcessingException {
		String[] arrDay = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17",
				"18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" };
		String strCD = "1";
		String strLable = "1";
		
		
        List<NameItem> nameItemList = new ArrayList<NameItem>();
        NameItem defaultNameItemObject = new NameItem(selected_cd, null, strCD, pagejsp+"="+strCD);
        nameItemList.add(defaultNameItemObject);
        for (int i = 0; arrDay.length > i; i++) {
          strCD = arrDay[i];
          strLable = arrDay[i];
          NameItem nameItemObject = new NameItem(selected_cd, strLable, strCD, pagejsp);
          nameItemList.add(nameItemObject);
        }
        DayFromName nameObject = new DayFromName(nameItemList);
        DayFromJsonList   jsonList = new DayFromJsonList(nameObject);


		ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(jsonList);
	}

	public String getXMLListDateMount(String pagejsp, String name, String selected_cd) throws JsonProcessingException {
		String[] arrDay = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		String strCD = "1";
		String strLable = "1";
		//JsonList   jsonList = new JsonList();
        
        List<NameItem> nameItemList = new ArrayList<NameItem>();
        NameItem defaultNameItemObject = new NameItem(selected_cd, null, strCD, pagejsp+"="+strCD);
        nameItemList.add(defaultNameItemObject);
		for (int i = 0; arrDay.length > i; i++) {
		  strCD = arrDay[i];
          strLable = arrDay[i];
          NameItem nameItemObject = new NameItem(selected_cd, strLable, strCD, pagejsp+"="+strCD);
          nameItemList.add(nameItemObject);
		}
		 MountFormName nameObject = new MountFormName(nameItemList);
	     MountFromJsonList   jsonList = new MountFromJsonList(nameObject);

		ObjectMapper mapper = new ObjectMapper();

		return mapper.writeValueAsString(jsonList);
	}

	public String getXMLListDateYear(String pagejsp, String name, String selected_cd) throws JsonProcessingException {
		String[] arrDay = { "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015","2016","2017","2018","2019","2020","2021" };
		String strCD = "1";
		String strLable = "1";
	    //  JsonList   jsonList = new JsonList();
		List<NameItem> nameItemList = new ArrayList<NameItem>();
        NameItem defaultNameItemObject = new NameItem(selected_cd, null, strCD, pagejsp+"="+strCD);
        nameItemList.add(defaultNameItemObject);

		for (int i = 0; arrDay.length > i; i++) {
		  strCD = arrDay[i];
          strLable = arrDay[i];
          NameItem nameItemObject = new NameItem(selected_cd, strLable, strCD, pagejsp+"="+strCD);
          nameItemList.add(nameItemObject);
			
		}
		YearFromName nameObject = new YearFromName(nameItemList);
		 YearFromJsonList   jsonList = new YearFromJsonList(nameObject);

		ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(jsonList);
	}


    public String getXMLDBList(String pagejsp, String name, String selected_cd, String query,
        String param1, String param2) throws JsonProcessingException {
      // System.out.println(query) ;
      String strCD = "0";
      String strLable = "Other";

      List<Map<String, Object>> xmlDBList =transactionSupport.getJdbcTemplate().queryForList(query);

      List<NameItem> nameItemList = new ArrayList<NameItem>();
      NameItem defaultNameItemObject =
          new NameItem(selected_cd, null, strCD, pagejsp + "=" + strCD);
      nameItemList.add(defaultNameItemObject);

      for (int i = 0; xmlDBList.size() > i; i++) {
        strCD = String.valueOf(xmlDBList.get(i).get(param1));
        strLable = String.valueOf(xmlDBList.get(i).get(param2));

        NameItem nameItemObject = new NameItem(selected_cd, strLable, strCD, pagejsp + "=" + strCD);
        nameItemList.add(nameItemObject);
      }
      JsonUtil jsonUtil = new JsonUtil();
      jsonUtil.beginDoc(name);
      jsonUtil.addArray(name + "-item", nameItemList.stream().toArray(NameItem[]::new));
      jsonUtil.endDoc();
      return jsonUtil.toJSON();
    }

	public List<XmlDBCriteriaLocale> getXMLDBCriteriaListLocale(String pagejsp, String name, String selected_cd, String defaultString,
			String query) {

		String strCD = "0";
		String strLable = "Other";
		List<XmlDBCriteriaLocale> xmlDBCriteriaLocaleList =  new ArrayList<>();
		try {
		    List<XMLDbCriteriaListRowMapper> xmlCriteriaObject = transactionSupport.getJdbcTemplate().queryForList(query, XMLDbCriteriaListRowMapper.class);

			for (int i = 0; xmlCriteriaObject.size() > i; i++) {
			  
			  String url =pagejsp+"="+strCD;
			  XMLDbCriteriaListRowMapper xml = xmlCriteriaObject.get(i);
			  strCD =  xml.getCriteriaId();
              strLable =  xml.getName();
              if (strCD.equals("0"))
                  strLable = defaultString;
			  
			  XmlDBCriteriaLocale xmlDBCriteriaLocale =  new XmlDBCriteriaLocale(name, selected_cd, strLable, strCD, url);
			  xmlDBCriteriaLocaleList.add(xmlDBCriteriaLocale);
			}
		}catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}
		return xmlDBCriteriaLocaleList;
	}

	
    public String getTreeXMLDBList(String pagejsp, String name, String selected_cd, String query,String subquery) throws JsonProcessingException {

      String strCD = "0";
      String strLable = "Other";

      List<SelectCatalog> selectCategoryList =
          transactionSupport.getJdbcTemplate().queryForList(query, SelectCatalog.class);
      CatalogName catalogName = new CatalogName();
      List<CatalogNameItem> nameItemList = new ArrayList<CatalogNameItem>();
      CatalogNameItem catalogNameDefaultItem = new CatalogNameItem();
      catalogNameDefaultItem.setCode("-1");
      catalogNameDefaultItem.setSelected(selected_cd);
      catalogNameDefaultItem.setItem(null);
      catalogNameDefaultItem.setUrl(pagejsp + "=" + strCD);
      CatalogSubNameitem cataLogSubNameDefaultitem = new CatalogSubNameitem();
      catalogNameDefaultItem.setSubnameitem(cataLogSubNameDefaultitem);
      nameItemList.add(catalogNameDefaultItem);

      for (int i = 0; selectCategoryList.size() > i; i++) {
        strCD = selectCategoryList.get(i).getCatalog_id();
        strLable = selectCategoryList.get(i).getLable();
        CatalogNameItem catalogNameItem = new CatalogNameItem();
        catalogNameItem.setSelected(selected_cd);
        catalogNameItem.setItem(strLable);
        catalogNameItem.setCode(strCD);
        catalogNameItem.setUrl(pagejsp + "=" + strCD);
        CatalogSubNameitem cataLogSubNameItem = new CatalogSubNameitem();
        catalogNameItem.setSubnameitem(cataLogSubNameItem);
        if (strCD.equals(selected_cd)) {
          catalogNameItem
              .setSubXMLDBList(getSubXMLDBList(pagejsp, name, selected_cd, strLable, subquery));
        }

      }
      catalogName.setNameItem(nameItemList);
      ObjectMapper mapper = new ObjectMapper();

      return mapper.writeValueAsString(catalogName);

    }

	public String getMenuXMLDBList(String pagejsp, String name, String selected_cd, String query) throws JsonProcessingException {

		String strCD = "0";
		String strLable = "Other";
		String strParent = "0";
			List<SelectMenuCatalog> listForSelectMenuCatalog =  transactionSupport.getJdbcTemplate().queryForList(query,SelectMenuCatalog.class);

			//table.append("<" + name + ">\n");
			//table.append("<" + name + "-item>");
			//table.append("</" + name + "-item>\n");
			List<NameItem> nameItemList = new ArrayList<NameItem>();
		      NameItem defaultNameItemObject =
		          new NameItem(selected_cd, null, "-1", pagejsp + "=" + strCD);
		      nameItemList.add(defaultNameItemObject);
			for (int i = 0; listForSelectMenuCatalog.size() > i; i++) {

				strParent = (String) listForSelectMenuCatalog.get(i).getParent_id();
				if (strParent.compareTo("0") == 0) {
					strCD = (String) listForSelectMenuCatalog.get(i).getCatalog_id();
					strLable = (String) listForSelectMenuCatalog.get(i).getLable();
					//table.append("<" + name + "-item>");
					NameItem nameItemObject = new NameItem();
					nameItemObject.setSelected(selected_cd);
					nameItemObject.setItem(strLable);
					nameItemObject.setCode(strCD);
					nameItemObject.setUrl(pagejsp+"="+strCD);
					getSubMenuXMLDBList(pagejsp, name, selected_cd, strLable,  strCD,listForSelectMenuCatalog.get(i),nameItemObject);
					nameItemList.add(nameItemObject);
				}
			}
			
            JsonUtil jsonUtil = new JsonUtil();
            jsonUtil.beginDoc(name);
            jsonUtil.addArray(name + "-item", nameItemList.stream().toArray(NameItem[]::new));
            jsonUtil.endDoc();
            return jsonUtil.toJSON();

			
	}

    private void getSubMenuXMLDBList(String pagejsp, String name, String selected_cd,
        String strLable_arg, String parent, SelectMenuCatalog selectMenuCatalog,
        NameItem nameItemObject) {

      String strCD = "0";
      String strLable = "Other";
      String strParent = "0";

        strCD = (String) selectMenuCatalog.getCatalog_id();
        strLable = (String) selectMenuCatalog.getLable();
        strParent = (String) selectMenuCatalog.getParent_id();
        if (strParent.compareTo(parent) == 0) {
          SubNameItem subNameItem =
              new SubNameItem(selected_cd, strLable, strCD, pagejsp + "=" + strCD, name);

          nameItemObject.setSubnameItem(subNameItem);
          // return mapper.writeValueAsString(subNameItem);

        }
  
      // return subTable.toString();
    }

	private List<SubXMLDB> getSubXMLDBList(String pagejsp, String name, String selected_cd, String strLable_arg, String query) {

		String strCD = "0";
		String strLable = "Other";
		List<SubXMLDB> subXmlDbList = new ArrayList<>();
		try {
			List<SelectCatalog> selectCatalogList = transactionSupport.getJdbcTemplate().queryForList(query, SelectCatalog.class);

			for (int i = 0; selectCatalogList.size() > i; i++) {
				strCD = selectCatalogList.get(i).getCatalog_id();
				strLable = selectCatalogList.get(i).getLable();
				SubXMLDB subXmlDb = new SubXMLDB();
				SubnameItemDto subnameItem = new SubnameItemDto(selected_cd, strLable, strCD, pagejsp + "=" + strCD);
				subXmlDb.setSubnameItem(subnameItem);
				subXmlDbList.add(subXmlDb);
			}

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		} 
		return subXmlDbList;
	}


	public boolean isNumber(String tmp) {
		if (tmp == null)
			return false;
		String IntField = "0123456789.";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}

	public float getBalans(int intUserID) {
		String field = "0";
		float balans = 0;
		String query = "SELECT  account.amount FROM account LEFT OUTER JOIN currency ON account.currency_id = currency.currency_id WHERE account.user_id = "
				+ intUserID;

		try {
		  List<String> balanceList = transactionSupport.getJdbcTemplate().queryForList(query, String.class);
          if (balanceList.size() != 0)
              field = balanceList.get(0); // + " " + // + " " +
			balans = Float.parseFloat(field);

		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		} 
		return balans;
	}

	public float getBalans(long intUserID) {
		String field = "0";
		float balans = 0;
		String query = "SELECT  account.amount FROM account LEFT OUTER JOIN currency ON account.currency_id = currency.currency_id WHERE account.user_id = "
				+ intUserID;

		try {
		  List<String> balanceList = transactionSupport.getJdbcTemplate().queryForList(query, String.class);
			if (balanceList.size() != 0)
				field = balanceList.get(0); // + " " +
			balans = Float.parseFloat(field);
			;
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		} 

		return balans;
	}

	public String getStrBalans(long intUserID) {
		String field = "0";
		String falans = "0";
		String query = "SELECT  account.amount FROM account LEFT OUTER JOIN currency ON account.currency_id = currency.currency_id WHERE account.user_id = "
				+ intUserID;
		try {
			List<String> balanceList = transactionSupport.getJdbcTemplate().queryForList(query, String.class);
			if (balanceList.size() != 0)
				field = balanceList.get(0); // + " " +
			    falans = nf.format(Float.parseFloat(field));
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}

		return falans;
	}

	public String getStrFormatNumber(float amount) {
		String falans = "0";
		try {
			falans = nf.format(amount);
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}
		return falans;
	}

	public String getStrFormatNumber(double amount) {
		String falans = "0";
		try {
			falans = nf.format(amount);
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}

		return falans;
	}

	public String getStrFormatNumberFloat(String floatAmount) {
		String falans = "0";
		try {
			falans = nf.format(Float.parseFloat(floatAmount));
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}

		return falans;
	}

	public String getStrFormatNumberDouble(String doubleAmount) {
		String falans = "0";
		try {
			falans = nf.format(Double.parseDouble(doubleAmount));
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
		}

		return falans;
	}

}
