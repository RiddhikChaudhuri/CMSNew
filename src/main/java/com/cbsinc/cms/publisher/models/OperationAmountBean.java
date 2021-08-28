package com.cbsinc.cms.publisher.models;

import java.util.List;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
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
@PropertySources(value = {
    @PropertySource(value="classpath:sequence.properties")
  })
public class OperationAmountBean implements java.io.Serializable {

	/**
	 * 
	 */
	transient private static final long serialVersionUID = -5629328315307393690L;

	/**
	 * 
	 */

	//transient static private Logger log = Logger.getLogger(OperationAmountBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(OperationAmountBean.class.getName());

    @Autowired
    private TransactionSupport transactionSupport;
       
    @Value("${account_hist}")
    private String sequence_account_hist;

	public OperationAmountBean() {
		logger.entry();
		logger.exit();
	}
//
//    public float getConvCurrency(String strCurrency_IDFrom, String strCurrency_IDTo) {
//      logger.entry(strCurrency_IDFrom, strCurrency_IDTo);
//      float floRate = 0;
//      String strRezult = "";
//      String query = "SELECT " + strCurrency_IDTo + " FROM currency_converter WHERE currency_id = "
//          + strCurrency_IDFrom;
//
//      try {
//        List<String> rows = transactionSupport.getJdbcTemplate().queryForList(query, String.class);
//
//        if (rows.size() != 0) {
//          strRezult = (String) rows.get(0);
//        } else {
//          strRezult = "0";
//        }
//        floRate = new Float(strRezult).floatValue();
//
//      } catch (Exception ex) {
//
//        logger.error(ex.getLocalizedMessage(), ex);
//      } finally {
//        logger.exit();
//      }
//
//      return floRate;
//    }

	public String setBuy(String soft_id, int intUserID, String order_id) {
	    logger.entry(intUserID,order_id,soft_id);
	    TransactionStatus status = transactionSupport.beginTransaction();
		float floRate = 1; // Curs
		String strID;
		String old_amount = "0";
		String strCurrency_IDAccount = "0";

		String strProductName = "";
		String strDescriptionProduct = "";
		String strVersionProduct = "";
		String strCostProduct = "";
		String strCurrency_IDProduct = "0";
		String strTaxProduct = "0";
		float floRezult_amount = 0;
		float floWithtax_rezult_amount = 0;

		// String query = "SELECT NEXT VALUE FOR account_hist_id_seq AS ID FROM
		// ONE_SEQUENCES";
		String query = sequence_account_hist;
		try {
			strID  = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "SELECT  account.amount, account.currency_id , currency.currency_lable, currency.currency_desc FROM account LEFT OUTER JOIN currency ON account.currency_id = currency.currency_id WHERE account.user_id = "
					+ intUserID;

			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			old_amount = (String) rows.get(0).get("account.amount");
			strCurrency_IDAccount = (String) rows.get(0).get("account.currency_id");
			query = "SELECT  soft.name, soft.description , soft.version, soft.cost , soft.currency, soft.serial_nubmer , catalog.lable , catalog.tax  FROM soft LEFT  JOIN catalog  ON soft.catalog_id = catalog.catalog_id WHERE soft.soft_id = "
					+ soft_id;
			rows.clear();
			rows = transactionSupport.getJdbcTemplate().queryForList(query);

			strProductName = (String) rows.get(0).get("soft.name");
			strDescriptionProduct = (String) rows.get(0).get("soft.description");
			strVersionProduct = (String) rows.get(0).get("soft.version");
			strCostProduct = (String)rows.get(0).get("soft.cost");
			strCurrency_IDProduct = (String) rows.get(0).get("soft.currency");
			strTaxProduct = (String) rows.get(0).get("catalog.tax");
			floRezult_amount = Float.parseFloat(old_amount) - Float.parseFloat(strCostProduct);
			floWithtax_rezult_amount = floRezult_amount - Float.parseFloat(strTaxProduct);

			query = "insert into account_hist " + "(" + " id  , " + " user_id , " + " order_id , " + " add_amount , "
					+ " old_amount  , " + " date_input , " + " complete   , " + " decsription  , "
					+ " currency_id_add  , " + " currency_id_old  , " + " currency_id_total  , " + " active  , "
					+ " rezult_cd , " + " date_end , " + "account_hist.sysdate , " + " total_amount  , " + " tax  , "
					+ " withtax_total_amount  " + ")" + " VALUES " + "( " + strID + ", " + intUserID + ", " + order_id
					+ ", " + strCostProduct + ", " + old_amount + ", " + "now()" + ", " + "true" + ", '"
					+ "Bought product " + strProductName + ", " + strDescriptionProduct + ", version "
					+ strVersionProduct + "', " + strCurrency_IDProduct + ", " + strCurrency_IDAccount + ", "
					+ strCurrency_IDAccount + ", " + "false  , '" + "bought" + "', " + "now()" + ", " + "now()" + ", "
					+ floRezult_amount + ", " + strTaxProduct + ", " + floWithtax_rezult_amount + " " + ")";

			transactionSupport.getJdbcTemplate().update(query);

			query = "UPDATE account SET amount = " + floRezult_amount + " WHERE account.user_id = " + intUserID;

			transactionSupport.getJdbcTemplate().update(query);
			transactionSupport.commitTransaction(status);

		} catch (Exception ex) {
			logger.error(query, ex);
			logger.error(ex.getLocalizedMessage(),ex);
			transactionSupport.rollbackTransaction(status);
		}

		finally {
			logger.exit();
		}

		return "";
	}

	/*
	 * return Account ID
	 */

	// public String addMoneyStart( String strDescriptionProduct , String
	// strAmountDebit , String strCurrency_IDAmountDebit , String currency_id ,
	// int intUserID ) {
	public String addMoneyStart(String strDescriptionProduct, double amountDebit, String strCurrency_IDAmountDebit,
			long intUserID, String user_ip, String user_header, String order_id) {
		String query = "";
		String strID = "";
		logger.entry(intUserID,order_id,strDescriptionProduct,amountDebit,strCurrency_IDAmountDebit,user_header);
        TransactionStatus status = transactionSupport.beginTransaction();
		try {
			double old_amount = 0;
			String strCurrency_IDAccount = "0";
			CurrencyHash currencyHash = CurrencyHash.getInstance();
			Currency curr = currencyHash.getCurrency(strCurrency_IDAmountDebit);
			// if( curr == null ) return "" ;
			// float floRate = curr.getRate();
			// float floRate = CurrencyHash.getCurrency()

			double taxProduct = 0;
			double rezult_amount = 0;
			double withtax_rezult_amount = 0;

			// query = "SELECT NEXT VALUE FOR account_hist_id_seq AS ID FROM ONE_SEQUENCES";
			query = sequence_account_hist;
			strID = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "SELECT  account.amount, account.currency_id , currency.currency_lable, currency.currency_desc FROM account LEFT OUTER JOIN currency ON account.currency_id = currency.currency_id WHERE account.user_id = "
					+ intUserID;
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);

			old_amount = new Double((String) rows.get(0).get("account.amount")).doubleValue();

			strCurrency_IDAccount = (String) rows.get(0).get("account.currency_id");
			rezult_amount = old_amount + amountDebit;
			withtax_rezult_amount = rezult_amount - taxProduct;

			query = "insert into account_hist " + "(" + " id  , " + " user_id , " + " order_id , " + " add_amount , "
					+ " old_amount  , " + " date_input , " + " complete   , " + " decsription  , "
					+ " currency_id_add  , " + " currency_id_old  , " + " currency_id_total  , " + " active  , "
					+ " account_hist.sysdate , " + " total_amount , " + " tax  , " + " withtax_total_amount ,"
					+ " user_ip ," + " user_header ," + " rate " + ")" + " VALUES " + "( " + strID + ", " + intUserID
					+ ", " + order_id + ", " + amountDebit + ", " + old_amount + ", " + "now()" + ", " + "false" + ", '"
					+ strDescriptionProduct + "', " + strCurrency_IDAmountDebit + ", " + strCurrency_IDAccount + ", "
					+ strCurrency_IDAccount + ", " + "true  , " + "now()" + ", " + rezult_amount + ", " + taxProduct
					+ ", " + withtax_rezult_amount + ", '" + user_ip + "', '" + user_header + "', " + 0 + " " + ")";

			transactionSupport.getJdbcTemplate().update(query);
			transactionSupport.commitTransaction(status);
		} catch (Exception ex) {
			logger.error(query, ex);
			transactionSupport.rollbackTransaction(status);
		}

		finally {
			logger.exit();
		}



		return strID;
	}


}
