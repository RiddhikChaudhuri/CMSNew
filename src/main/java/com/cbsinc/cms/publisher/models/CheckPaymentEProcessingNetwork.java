package com.cbsinc.cms.publisher.models;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.Vector;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;

@Component
@PropertySource(value="classpath:SetupApplicationResources.properties")
public class CheckPaymentEProcessingNetwork implements java.io.Serializable {

	private static final long serialVersionUID = 8160271389486247546L;

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

	//static private Logger log = Logger.getLogger(CheckPaymentResult.class);
	private XLogger logger = XLoggerFactory.getXLogger(CheckPaymentEProcessingNetwork.class.getName());
	
	@Autowired
	private TransactionSupport transactionSupport;
	
	private final String checkpay_template;

	private URL url1;
	transient long delay = 60000;
	String strUrlServer = "https://www.eProcessingNetwork.Com/cgi-bin/tdbe/transact.pl";
	
	@Autowired
	public CheckPaymentEProcessingNetwork(@Value("${checkpay_template}")String checkpay_template) {
	  logger.entry();
	  this.checkpay_template = checkpay_template;
		try {
		    delay = Long.parseLong(checkpay_template.trim());
			url1 = new URL(strUrlServer);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		java.util.Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(t, 0, delay);
		logger.exit();
	}

	transient java.util.TimerTask t = new java.util.TimerTask() {
		public void run() {
			Optional<OrderBank> objOrderBank = Optional.ofNullable(getOrder());
			if (objOrderBank.isPresent() && objOrderBank.get().getOrder_ID() != null) {
				logger.info("Order : " + objOrderBank.get().getOrder_ID());
				logger.info("Send  request to " + strUrlServer + " pay gateway: "+ new Date(scheduledExecutionTime()).toString());
				CheckOrder(objOrderBank);
			}
		}
	};

    public OrderBank getOrder() {
      logger.entry();
      String query = "";
      OrderBank objOrderBank = null;
      List<Map<String, Object>> rows;
      try {
        query =
            "SELECT id, date_input FROM account_hist WHERE complete = false and active = true ORDER BY id ASC LIMIT 1  OFFSET 0";
        rows = transactionSupport.getJdbcTemplate().queryForList(query);
      } catch (Exception ex) {
        logger.error(query, ex);
        logger.error(ex.getLocalizedMessage(), ex);
        return null;
      } 

      if (rows.size() > 0) {
        try {
          objOrderBank = new OrderBank();
          objOrderBank.setOrder_ID((String) rows.get(0).get("id"));
          objOrderBank.setBeginData((String) rows.get(1).get("date_input"));
        } catch (Exception ex) {
          logger.error(ex.getLocalizedMessage(), ex);
          return null;
        }
      }

      return objOrderBank;
    }

	public void CheckOrder(Optional<OrderBank> objOrderBank) {

		try {
			// URL url1 = new URL ("http://pgate.grabko.com:88");
		  if(objOrderBank.isPresent()) {
			InputStreamReader inputstreamreader = new InputStreamReader(url1.openStream());
			String request = "ShopOrderNumber=" + objOrderBank.get().getOrder_ID()
					+ "&Shop_ID=84473&login=gvidon&password=231003&SUCCESS=2&STARTDAY="
					+ objOrderBank.get().getBeginData_Day() + "&STARTMONTH=" + objOrderBank.get().getBeginData_Month()
					+ "&STARTYEAR=" + objOrderBank.get().getBeginData_Year() + "&ENDDAY=" + objOrderBank.get().getEndData_Day()
					+ "&ENDMONTH=" + objOrderBank.get().getEndData_Month() + "&ENDYEAR=" + objOrderBank.get().getEndData_Year()
					+ "&MEANTYPE=0&PAYMENTTYPE=0&FORMAT=1&ZIPFLAG=0&ENGLISH=0&HEADER1=0&Delimiter=;&RowDelimiter=13,10&S_FIELDS=ORDERNUMBER;RESPONSE_CODE;RECOMMENDATION;DATE;TOTAL";
			BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
			bufferedreader = postData(strUrlServer, request);
			String nextLine = "";
			while ((nextLine = bufferedreader.readLine()) != null) {
				String tmp = java.net.URLDecoder.decode(nextLine, "UTF-8");
				String[] result = tmp.split(";");
				System.err.println(tmp);
				if (tmp.indexOf(";") != -1) {
					parserRequest(result[0], result[1], result[2]);
				}
			}
		  }
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}

		System.out.println("Answer from pay gateway: " + new Date(System.currentTimeMillis()).toString());
	}

	public void parserRequest(String i_strNumerOrder, String i_strRezult, String i_strDecsription) {
	  logger.info("Order: " + i_strNumerOrder + " Rezalt: " + i_strRezult + " " + i_strDecsription);
	    TransactionStatus status = transactionSupport.beginTransaction();
		String query = "";
		try {
			if (Long.parseLong(i_strRezult) == PayStatus.SUCCESS) {
				end_addmoney(status, i_strNumerOrder, i_strRezult, i_strRezult);
			} else if (Long.parseLong(i_strRezult) == PayStatus.UNSUCCESS) {
				query = "UPDATE account_hist SET complete = ? , active = ? , rezult_cd = ? , decsription =  ?  WHERE  id =  "
						+ i_strNumerOrder + "";
				transactionSupport.getJdbcTemplate().update(query,false,false,i_strRezult,i_strDecsription);
			}
			transactionSupport.commitTransaction(status);
		}  catch (Exception ex) {
		  logger.error(query, ex);
			logger.error(ex.getLocalizedMessage(),ex);
			transactionSupport.rollbackTransaction(status);
			return;
		} finally {
			logger.exit();
		}

	}

	public String[] tokenize(String s, String d) {
		Vector<String> vector = new Vector<String>();
		for (StringTokenizer stringtokenizer = new StringTokenizer(s, d); stringtokenizer.hasMoreTokens(); vector
				.addElement(stringtokenizer.nextToken()))
			;

		String as[] = new String[vector.size()];
		for (int i = 0; i < as.length; i++)
			as[i] = (String) vector.elementAt(i);
		return as;
	}

	public BufferedReader postData(String s, String s1) {
		BufferedReader bufferedreader = null;
		try {
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			try {
				Class<?> clsFactory = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
				if ((null != clsFactory) && (null == Security.getProvider("SunJSSE")))
					Security.addProvider((Provider) clsFactory.newInstance());
			} catch (ClassNotFoundException cfe) {
				logger.error(cfe.getLocalizedMessage());
				throw new Exception("Unable to load the JSSE SSL stream handler.  Check classpath." + cfe.toString());
			}

			URL url1 = new URL(s);
			java.net.HttpURLConnection urlconnection = (java.net.HttpURLConnection) url1.openConnection();
			urlconnection.setUseCaches(false);
			urlconnection.setDoOutput(true);
			ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream(s1.length() * 2);
			PrintWriter printwriter = new PrintWriter(bytearrayoutputstream, true);
			printwriter.print(s1);
			printwriter.flush();
			urlconnection.setRequestProperty("Content-Length", String.valueOf(bytearrayoutputstream.size()));
			urlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			bytearrayoutputstream.writeTo(urlconnection.getOutputStream());
			bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			e.printStackTrace();
			return null;
		}
		return bufferedreader;
	}

	public static void main(String[] args) {

	}

    void end_addmoney(TransactionStatus status, String i_strAccountHistory_id, String i_strRezult,
        String i_strDecsription) throws Exception {

      double add_amount = 0;
      double old_amount = 0;
      double total_amount = 0;
      // String date_input = "" ;
      double amount = 0;
      double rate = 0;
      String user_id = "";
      String query = "";
      String order_id = "";

      try {
        query =
            "select add_amount , old_amount ,  date_input , rate , date_end , user_id , order_id from  account_hist where  id =  "
                + i_strAccountHistory_id;
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        if (rows.size() > 0) {
          add_amount = new Double((String) rows.get(0).get("add_amount")).doubleValue();
          old_amount = new Double((String) rows.get(0).get("old_amount")).doubleValue();
          rate = new Double((String) rows.get(0).get("rate")).doubleValue();
          user_id = (String) rows.get(0).get("user_id");
          order_id = (String) rows.get(0).get("order_id");
          total_amount = old_amount + add_amount;
        }

        query = "UPDATE account_hist SET complete = ? , active = ? , rezult_cd = ?  WHERE  id =  "
            + i_strAccountHistory_id + "";

        transactionSupport.getJdbcTemplate().update(query, true, false, i_strRezult);

        total_amount = amount + (add_amount * rate);
        query =
            "UPDATE account SET amount = ? , curr = ? , date_input = ? WHERE  user_id = " + user_id;
        transactionSupport.getJdbcTemplate().update(query, Double.valueOf(total_amount),
            (long) rate, new java.util.Date());

        if (total_amount >= 0) {
          query = "update orders  set paystatus_id = " + PayStatus.SUCCESS + " where order_id = "
              + order_id;
          transactionSupport.getJdbcTemplate().update(query);
        }
        transactionSupport.commitTransaction(status);
      } catch (Exception ex) {
        logger.error(query, ex);
        logger.error(ex.getLocalizedMessage(), ex);
        transactionSupport.rollbackTransaction(status);
        return;
      } finally {
        logger.exit();
      }
    }

}
