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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cbsinc.cms.publisher.dao.TransactionSupport;

@Component
public class PayGatewayListBean implements Serializable {

	private static final long serialVersionUID = -1197050877775233920L;

	//static private Logger log = Logger.getLogger(PayGatewayListBean.class);
	private XLogger log = XLoggerFactory.getXLogger(PayGatewayListBean.class.getName());

	private String limit = "10";

	private String select_shop_id = "0";

	private String select_name_gateway = "0";

	private String site_id = "0";

	private LinkedList<PayGatewayBean> listShopSetupBean = new LinkedList<PayGatewayBean>();

	private Integer indx_select = 0;

	private Integer offset = 0;

	private String cururl;

	private String listup;

	private String listdown;

	@Autowired
	TransactionSupport transactionSupport;

	public void mapmingShopBean(String site_id) {
		listShopSetupBean.clear();
		String query = "select shop_id , shop_cd ,  owner_id , login , passwd ,  pay_gateway.pay_gateway_id  , pay_gateway.name_gateway , pay_gateway.site_url  from shop join pay_gateway on pay_gateway.pay_gateway_id = shop.pay_gateway_id  where active = true and  shop.site_id = "
				+ site_id + " limit  " + limit + " offset " + offset;
		
		try {
			List<Map<String,Object>> shopList =  transactionSupport.getJdbcTemplate().queryForList(query);

			for (int i = 0; shopList.size() > i; i++) {
				PayGatewayBean shopSetupBean = new PayGatewayBean();
				Map<String,Object> shopSetUpBean = shopList.get(i);
				shopSetupBean.setShop_id((String)shopSetUpBean.get("shop_id"));
				shopSetupBean.setShop_cd((String) shopSetUpBean.get("shop_cd"));
				shopSetupBean.setOwner_id((String) shopSetUpBean.get("owner_id"));
				shopSetupBean.setLogin((String) shopSetUpBean.get("login"));
				shopSetupBean.setPasswd((String) shopSetUpBean.get("passwd"));
				shopSetupBean.setPay_gateway_id((String) shopSetUpBean.get("pay_gateway.pay_gateway_id"));
				shopSetupBean.setName_gateway((String)  shopSetUpBean.get("pay_gateway.name_gateway"));
				shopSetupBean.setPay_url((String)  shopSetUpBean.get("pay_gateway.site_url"));
				listShopSetupBean.add(shopSetupBean);
			}

		}catch (Exception ex) {

			log.error(ex.getLocalizedMessage());
		} 
	}

	public String getTable(long intLevelUp) {
		cururl = "PayGatewayList.jsp?offset=" + offset;
		listup = "PayGatewayList.jsp?offset=" + (offset + 10);
		if (offset - 10 < 0)
			listdown = "PayGatewayList.jsp?offset=0";
		else
			listdown = "PayGatewayList.jsp?offset=" + (offset - 10); 
		StringBuffer table = new StringBuffer();
		table.append("<table class=\"columns\">\n");
		table.append("<tbody>\n");

		if (intLevelUp == 2) {

			if (listShopSetupBean.size() == 0) {
				table.append("<TR BGCOLOR=\"#8CACBB\" >" + "<TD WIDTH=\"10%\" >ID </TD>"
						+ "<TD WIDTH=\"70%\" >Paymant Gateway </TD>"
						+ "<TD WIDTH=\"20%\" ><a href =\"PayGatewaySetup.jsp\">add</a> </TD>" + "</TR>\n");
			} else {
				table.append("<TR BGCOLOR=\"#808080\" >" + "<TD WIDTH=\"10%\" >ID </TD>"
						+ "<TD WIDTH=\"70%\" >Paymant Gateway </TD>" + "<TD WIDTH=\"20%\" ></TD>" + "</TR>\n");
			}
		} else {
			table.append("<TR BGCOLOR=\"#808080\" >" + "<TD>ID</TD>" + "<TD>Paymant Gateway </TD>" + "<TD></TD>"
					+ "</TR>\n");
		}

		if (listShopSetupBean.size() < 10) {
			// table.append("<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD></TD>" + "</TR>\n");
		} else {
			table.append(
					"<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\"" + listup + "\">up 10</a>  </TD>" + "</TR>\n");
		}

		for (int i = 0; listShopSetupBean.size() > i; i++) {
			table.append("<TR>" + "<TD>" + (i + 1) + "</TD>" + "<TD>"
					+ ((PayGatewayBean) listShopSetupBean.get(i)).getName_gateway() + "</TD>"
					+ "<TD><a href =\"PayGatewaySetup.jsp?row=" + i + "\">edit</a> </TD>" + "</TR>\n");
		}

		table.append(
				"<TR>" + "<TD></TD>" + "<TD></TD>" + "<TD><a href=\"" + listdown + "\">back 10</a>  </TD>" + "</TR>\n");

		table.append("</tbody>\n");
		table.append("</TABLE>\n");

		return table.toString();
	}

	public static void main(String[] args) {
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getSelect_shop_id() {
		return select_shop_id;
	}

	public void setSelect_shop_id(String select_shop_id) {
		this.select_shop_id = select_shop_id;
	}

	public String getSelect_name_gateway() {
		return select_name_gateway;
	}

	public void setSelect_name_gateway(String select_name_gateway) {
		this.select_name_gateway = select_name_gateway;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public int getIndx_select() {
		return indx_select;
	}

	public void setIndx_select(int indx_select) {
		this.indx_select = indx_select;
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

	public String getCururl() {
		return cururl;
	}

	public void setCururl(String cururl) {
		this.cururl = cururl;
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

	public PayGatewayBean getPayGatewayBean(int index) {
		Object payGatewayBean = listShopSetupBean.get(index);
		if (payGatewayBean instanceof PayGatewayBean)
			return (PayGatewayBean) payGatewayBean;
		return null;
	}

}