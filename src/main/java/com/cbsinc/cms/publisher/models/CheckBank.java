package com.cbsinc.cms.publisher.models;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.security.Provider;
import java.security.Security;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;

@Component
public class CheckBank implements java.io.Serializable {

	private static final long serialVersionUID = -3492879607551630598L;

	//transient static private Logger log = Logger.getLogger(CheckBank.class);
	private XLogger logger = XLoggerFactory.getXLogger(CheckBank.class.getName());

	
	@Autowired
	private TransactionSupport transactionSupport;
	
	
	public CheckBank() {
		java.util.Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(t, 0, 60000);
		logger.entry();
		logger.exit();
	}

    transient java.util.TimerTask t = new java.util.TimerTask() {
      public void run() {
        OrderBank objOrderBank = getOrder();
        if (objOrderBank != null) {
          logger.info("Order : " + objOrderBank.getOrder_ID());
          logger.info("Work Time: " + scheduledExecutionTime());
          logger.info("System Time: " + System.currentTimeMillis());
          logger.info("Rezalt: " + CheckOrder(objOrderBank));
        }
      }
    };

    public OrderBank getOrder() {
      String query = "";
      OrderBank objOrderBank = new OrderBank();
      List<Map<String, Object>> orderList = new ArrayList<>();
      try {
        query =
            "SELECT id, date_input FROM account_hist WHERE (complete = false and active = true) ORDER BY id ASC LIMIT 1  OFFSET 0";
        orderList = transactionSupport.getJdbcTemplate().queryForList(query);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      }

      if (orderList.size() > 0) {
        try {
          objOrderBank.setOrder_ID((String) orderList.get(0).get("id"));
          objOrderBank.setBeginData((String) orderList.get(0).get("date_input"));
        } catch (Exception ex) {
          logger.error(ex.getLocalizedMessage(), ex);
          return null;
        } finally {
          logger.exit();
        }
      }

      logger.exit();
      return objOrderBank;
    }

    public String CheckOrder(OrderBank objOrderBank) {
      logger.entry(objOrderBank);
      try {
        if (objOrderBank != null) {
          URL url1 = new URL("http://www.assist.ru");
          InputStreamReader inputstreamreader = new InputStreamReader(url1.openStream());
          String request = "ShopOrderNumber=" + objOrderBank.getOrder_ID()
              + "&Shop_ID=84473&login=gvidon&password=231003&SUCCESS=2&STARTDAY="
              + objOrderBank.getBeginData_Day() + "&STARTMONTH=" + objOrderBank.getBeginData_Month()
              + "&STARTYEAR=" + objOrderBank.getBeginData_Year() + "&ENDDAY="
              + objOrderBank.getEndData_Day() + "&ENDMONTH=" + objOrderBank.getEndData_Month()
              + "&ENDYEAR=" + objOrderBank.getEndData_Year()
              + "&MEANTYPE=0&PAYMENTTYPE=0&FORMAT=1&ZIPFLAG=0&ENGLISH=0&HEADER1=0&Delimiter=;&RowDelimiter=13,10&S_FIELDS=ORDERNUMBER;RESPONSE_CODE;RECOMMENDATION;DATE;TOTAL";
          BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
          bufferedreader = postData("http://secure.assist.ru/results/results_long.cfm", request);
          String nextLine = "";
          while ((nextLine = bufferedreader.readLine()) != null) {
            String tmp = java.net.URLDecoder.decode(nextLine, "UTF-8");
            logger.error(tmp);
            if (tmp.indexOf(";") != -1) {
              parserRequest(objOrderBank.getOrder_ID(), tokenize(tmp, ";")[1],
                  tokenize(tmp, ";")[2]);
              return tokenize(tmp, ";")[1];
            }
          }
        }
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        System.out.println(ex.toString());
      }
      return null;
    }

    public String CheckOrder1(OrderBank objOrderBank) {
      logger.entry(objOrderBank);
      try {
        URL url1 = new URL("http://www.assist.ru");
        InputStreamReader inputstreamreader = new InputStreamReader(url1.openStream());
        String request =
            "ShopOrderNumber=%&Shop_ID=84473&login=gvidon&password=231003&SUCCESS=2&STARTDAY="
                + objOrderBank.getBeginData_Day() + "&STARTMONTH="
                + objOrderBank.getBeginData_Month() + "&STARTYEAR="
                + objOrderBank.getBeginData_Year() + "&ENDDAY=" + objOrderBank.getEndData_Day()
                + "&ENDMONTH=" + objOrderBank.getEndData_Month() + "&ENDYEAR="
                + objOrderBank.getEndData_Year()
                + "&MEANTYPE=0&PAYMENTTYPE=0&FORMAT=1&ZIPFLAG=0&ENGLISH=1&HEADER1=0&Delimiter=;&RowDelimiter=13,10&S_FIELDS=ORDERNUMBER;RESPONSE_CODE;RECOMMENDATION;DATE;TOTAL";
        BufferedReader bufferedreader = new BufferedReader(inputstreamreader);
        bufferedreader = postData("http://secure.assist.ru/results/results_long.cfm", request);
        LinkedList<String> linkedList = new LinkedList<String>();
        String nextLine = "";
        while ((nextLine = bufferedreader.readLine()) != null) {
          String tmp = java.net.URLDecoder.decode(nextLine, "UTF-8");
          if (tmp.indexOf("ERROR") != -1)
            return null;
          logger.error(tmp);
          if (tmp.length() > 0)
            linkedList.add(tmp);
        }

        for (int i = 0; linkedList.size() > i; i++) {
          String tmp = (String) linkedList.get(i);
          if (tmp.indexOf(";") != -1) {
            parserRequest(objOrderBank.getOrder_ID(), tokenize(tmp, ";")[1], tokenize(tmp, ";")[2]);
            return tokenize(tmp, ";")[1];
          }
        }

      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      }
      logger.exit();
      return null;
    }

	synchronized public void parserRequest(String i_strNumerOrder, String i_strRezult, String i_strDecsription) {
		logger.entry("Order: " + i_strNumerOrder + " Rezalt: " + i_strRezult + " " + i_strDecsription);
		TransactionStatus trxnstatus = transactionSupport.beginTransaction();
		String query = "";
		try {
			if (i_strRezult.compareTo("AS000") == 0) {
				end_addmoney(trxnstatus, i_strNumerOrder, i_strRezult, i_strRezult);
			}

			if (i_strRezult.compareTo("AS001") == 0) {
				end_addmoney(trxnstatus, i_strNumerOrder, i_strRezult, i_strRezult);
			}
			if (i_strRezult.compareTo("AS000") != 0 && i_strRezult.compareTo("AS001") != 0
					&& i_strRezult.compareTo("AS300") != 0) {
				query = "UPDATE account_hist SET complete = ? , active = ? , rezult_cd = ? , decsription =  ?  WHERE  id =  "
						+ i_strNumerOrder + "";
				transactionSupport.getJdbcTemplate().update(query, false,false,i_strRezult,i_strDecsription);
				transactionSupport.commitTransaction(trxnstatus);
			}
		}  catch (Exception ex) {
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnstatus);
			return;
		} finally {
			logger.exit();
		}

		return;
	}

	public String[] tokenize(String s, String d) {
	    logger.entry(s,d);
		Vector<String> vector = new Vector<String>();
		for (StringTokenizer stringtokenizer = new StringTokenizer(s, d); stringtokenizer.hasMoreTokens(); vector
				.addElement(stringtokenizer.nextToken()))
			;

		String as[] = new String[vector.size()];
		for (int i = 0; i < as.length; i++)
			as[i] = (String) vector.elementAt(i);
		logger.exit();
		return as;
	}

	public BufferedReader postData(String s, String s1) {
	    logger.entry(s,s1);
		BufferedReader bufferedreader = null;
		try {
			System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
			try {
				Class<?> clsFactory = Class.forName("com.sun.net.ssl.internal.ssl.Provider");
				if ((null != clsFactory) && (null == Security.getProvider("SunJSSE")))
					Security.addProvider((Provider) clsFactory.newInstance());
			} catch (ClassNotFoundException cfe) {
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
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.error(exception.getLocalizedMessage(),exception);
			return null;
		}
		logger.exit();
		return bufferedreader;
	}

	public static void main(String[] args) {

	}

    void end_addmoney(TransactionStatus transactionStatus, String i_strAccountHistory_id,
        String i_strRezult, String i_strDecsription) throws Exception {
      logger.entry(transactionStatus,i_strAccountHistory_id,i_strDecsription,i_strRezult);
      double add_amount = 0;
      double old_amount = 0;
      double total_amount = 0;
      // String date_input = "" ;
      double amount = 0;
      double rate = 0;
      String date_input = null;
      String user_id = "";
      String query = "";

      query =
          "select add_amount , old_amount ,  date_input , rate , date_end , user_id  from  account_hist where  id =  "
              + i_strAccountHistory_id;
      List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
      if (rows.size() > 0) {
        add_amount = new Double((String) rows.get(0).get("add_amount")).doubleValue();
        old_amount = new Double((String) rows.get(0).get("old_amount")).doubleValue();
        date_input = (String) rows.get(0).get("date_input");
        rate = new Double((String) rows.get(0).get("rate")).doubleValue();
        user_id = (String) rows.get(0).get("user_id");
        total_amount = old_amount + add_amount;
      }

      query = "UPDATE account_hist SET complete = true  , active = false , rezult_cd = '"
          + i_strRezult + "'  WHERE  id = " + i_strAccountHistory_id;
      transactionSupport.getJdbcTemplate().update(query);

      total_amount = amount + (add_amount * rate);
      query = "UPDATE account SET amount = " + total_amount + " , curr = " + rate
          + " , date_input = '" + date_input + "' WHERE  user_id = " + user_id;
      transactionSupport.getJdbcTemplate().update(query);
      transactionSupport.commitTransaction(transactionStatus);
    }

}
