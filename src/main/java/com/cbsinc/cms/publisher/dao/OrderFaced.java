package com.cbsinc.cms.publisher.dao;
/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code.
 * You can not use it and you cannot change without written permission from Konstantin Grabko
 * Email: konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * Company: CENTER BUSINESS SOLUTIONS INC 
 * </p>
 * 
 * @author Konstantin Grabko
 * @version 1.0
 */
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.dto.pages.orderhistory.HistoryOrder;
import com.cbsinc.cms.publisher.dao.RowMappers.AccountHistoryDetalBeanRowMapper;
import com.cbsinc.cms.publisher.dao.RowMappers.HistoryOrderBeanMapper;
import com.cbsinc.cms.publisher.dao.RowMappers.OrderProductBeanRowMapper;
import com.cbsinc.cms.publisher.models.AccountHistoryBean;
import com.cbsinc.cms.publisher.models.AccountHistoryDetalBean;
import com.cbsinc.cms.publisher.models.CurrencyHash;
import com.cbsinc.cms.publisher.models.DeliveryStatus;
import com.cbsinc.cms.publisher.models.OrderBean;
import com.cbsinc.cms.publisher.models.OrderListBean;
import com.cbsinc.cms.publisher.models.PayStatus;
//public class OrderFaced extends TransactionSupport  WebControls implements java.io.Serializable 
@Repository
@PropertySource("classpath:sequence.properties")
public class OrderFaced extends WebControls  implements java.io.Serializable 
{

    @Autowired
    TransactionSupport transactionSupport;

	private static final long serialVersionUID = 8216914184792733202L;

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
	 * Copyright: Copyright (c) 2014
	 * </p>
	 * <p>
	 * Company: CENTER BUSINESS SOLUTIONS INC 
	 * </p>
	 * 
	 * @author Konstantin Grabko
	 * @version 1.0
	 */
	
	private static XLogger logger = XLoggerFactory.getXLogger(OrderFaced.class.getName());
	
    @Value("${orders}")
    private String order_sequence_rs;
    
    @Value("${basket}")
    private String basket_sequence_rs;
    
    @Value("${account_hist}")
    private String account_hist_sequence_rs;

   

    public String createOrder(final String user_currency_id, final OrderBean orderBean)
        throws Exception {
      logger.entry(user_currency_id, orderBean);

      TransactionStatus status = transactionSupport.beginTransaction();
      try {
      String query = order_sequence_rs;
      orderBean.setOrder_id(transactionSupport.getJdbcTemplate().queryForObject(query, String.class));
      query =
          "insert into orders ( order_id ,  cdate ,  end_amount ,  amount ,  tax ,  user_id ,  delivery_amount ,  paystatus_id , deliverystatus_id , currency_id ) "
              + " VALUES ( ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? ,  ? , ? , ? )";

      transactionSupport.getJdbcTemplate().update(query, Long.valueOf(orderBean.getOrder_id()), new java.util.Date(), 0, 0,
          0, Long.valueOf(orderBean.getUser_ID()), 0, PayStatus.CREATE_PAYMENT,
          DeliveryStatus.FILLING_BASKET, Long.valueOf(user_currency_id));
      transactionSupport.commitTransaction(status);
      }
      catch (Exception e) {
		transactionSupport.rollbackTransaction(status);
	}
      
      
      logger.exit();
      return orderBean.getOrder_id();
    }

	
	
	
	
	
    public String deleteOrder(final String basket_id, final OrderBean orderBean) throws Exception {
      logger.entry(basket_id, orderBean);
      if (basket_id == null || basket_id.length() == 0)
        return "";
      if (orderBean.getorder_paystatus().compareTo("0") != 0)
        return orderBean.getOrder_id();

      String query = "";
      float fltProduct_amount = 0;
      float fltOrder_amount = 0;
      float fltEnd_order_amount = 0;
      String strProduct_id = "";
      TransactionStatus status = transactionSupport.beginTransaction();
      query = "select  product_id   from  basket where basket_id = " + basket_id;
      Long productId = transactionSupport.getJdbcTemplate().queryForObject(query, Long.class);
      if (productId != null)
        strProduct_id = productId.toString();
      else {
        return orderBean.getOrder_id();
      }

      query = "select cost  from soft  where soft_id = " + strProduct_id;
      Double cost = transactionSupport.getJdbcTemplate().queryForObject(query, Double.class);
      if (cost != null)
        fltProduct_amount = (new Float(cost)).floatValue();

      query = "select amount , end_amount from orders  where order_id = " + orderBean.getOrder_id();
      Map<String, Object> result = transactionSupport.getJdbcTemplate().queryForMap(query);
      if (result.size() != 0) {
        fltOrder_amount = (new Float((String) result.get("amount"))).floatValue(); // + " " +
                                                                                   // (String)Adp.getValueAt(0,1)
                                                                                   // ;
        fltEnd_order_amount = (new Float((String) result.get("end_amount"))).floatValue(); // + " "
                                                                                           // +
                                                                                           // (String)Adp.getValueAt(0,1)
                                                                                           // ;
      }

      int quantity = 1;
      query = "select quantity  from  basket where basket_id = " + basket_id;
      Integer value = transactionSupport.getJdbcTemplate().queryForObject(query, Integer.class);
      if (value != 0) {
        if (value != null)
          quantity = value;
      }

      try {

        query = "update orders  set end_amount =  ? , amount = ?  where order_id = "
            + orderBean.getOrder_id();
        // Adp.executeUpdate(query);
        Map<String, Double> args = new HashMap<String, Double>();
        args.put("end_amount", (double) (fltEnd_order_amount - (fltProduct_amount * quantity)));
        args.put("amount", (double) (fltOrder_amount - (fltProduct_amount * quantity)));
        transactionSupport.getJdbcTemplate().update(query, args);


        query = "delete  from  basket where basket_id = " + basket_id;
        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(status);
        query = "select  product_id   from  basket where basket_id = " + basket_id;
        Long product_id = transactionSupport.getJdbcTemplate().queryForObject(query, Long.class);

        if (product_id == null) {
          orderBean.setend_amount("0");
          orderBean.setorder_amount("0");
        }
      } catch (Exception e) {
        transactionSupport.rollbackTransaction(status);
      }
      logger.exit();
      return orderBean.getOrder_id();
    }

    


	
	

	/*
	 * если возврацается значение -1 то не
	 * совпадение валют
	 */

	final public String addGroupPosition(final String strPositions_id ,  final int quantity , final OrderBean orderBean) throws Exception {
		String[] positionsArray_id = strPositions_id.split("_");
		String rezult = "";
		for (int i = 0; positionsArray_id.length > i; i++) {
			rezult = addPosition(positionsArray_id[i], quantity, orderBean);
			if (rezult.compareTo("-1") == 0) {
				break;
			}
		}

		return rezult;
	}
	

	
	/*
	 * если возврацается значение -1 то не
	 * совпадение валют
	 */

    final public String addPosition(final String strPosition_id, final int quantity,
        final OrderBean orderBean) throws Exception {
      logger.entry(strPosition_id,quantity,orderBean);
      if (orderBean.getorder_paystatus().compareTo("0") != 0)
        return strPosition_id;
      if (quantity == 0)
        return strPosition_id;
      String basket_id = "";
      String query = "";
  
      float fltProduct_amount = 0;
      float fltOrder_amount = 0;
      float fltEnd_order_amount = 0;
      String strProduct_currency = "0";
      String strOrder_currency = "0";

      
      TransactionStatus status = transactionSupport.beginTransaction();
      try {
      query = "select cost ,  currency from soft  where soft_id = " + strPosition_id;
      Map<String, Object> queryForPosition = transactionSupport.getJdbcTemplate().queryForMap(query);
      if (queryForPosition.size() != 0) {
        fltProduct_amount = (new Float((String) queryForPosition.get("cost"))).floatValue();
        strProduct_currency = (String) queryForPosition.get("currency");
      }

      query = "select currency_id  from orders   where order_id = " + orderBean.getOrder_id();
      strOrder_currency = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

      if (strProduct_currency.compareTo(strOrder_currency) != 0)
        return "-1";
      query = "select amount , end_amount , currency_id   from orders  where order_id = "
          + orderBean.getOrder_id();
      Map<String, Object> queryForOrder = transactionSupport.getJdbcTemplate().queryForMap(query);
      if (queryForOrder.size() != 0) {
        fltOrder_amount = (new Float((String) queryForOrder.get("amount"))).floatValue();
        fltEnd_order_amount = (new Float((String) queryForOrder.get("end_amount"))).floatValue();
      }

      query = "update orders  set end_amount =  ?, amount = ? where order_id = "
          + orderBean.getOrder_id();
      /// Adp.executeUpdate(query);

      Double end_amount = (double) (fltEnd_order_amount + (fltProduct_amount * quantity));
      Double amount = (double) (fltOrder_amount + (fltProduct_amount * quantity));
      Object[] params = {end_amount, amount};
      int[] types = {Types.DOUBLE, Types.DOUBLE};
      transactionSupport.getJdbcTemplate().update(query, params, types);

      basket_id = transactionSupport.getJdbcTemplate().queryForObject(basket_sequence_rs, String.class);


      query =
          "insert into basket ( basket_id ,  product_id , order_id , quantity ) VALUES ( ?, ?, ? , ? )";



      Object[] basketParams = {Long.valueOf(basket_id), Long.valueOf(strPosition_id),
          Long.valueOf(orderBean.getOrder_id()), quantity};
      int[] basketTypes = {Types.BIGINT, Types.BIGINT, Types.BIGINT, Types.INTEGER};
      transactionSupport.getJdbcTemplate().update(query, basketParams, basketTypes);
      transactionSupport.commitTransaction(status);

      }catch (Exception e) {
        transactionSupport.rollbackTransaction(status);
      }
      return strPosition_id;
    }




	
    final public String deletePosition(final String strPosition_id, final OrderBean orderBean) {
      logger.entry(strPosition_id, orderBean);
      TransactionStatus status = transactionSupport.beginTransaction();
      try {
        if (orderBean.getorder_paystatus().compareTo("0") != 0)
          return strPosition_id;
        if (strPosition_id == null || strPosition_id.length() == 0)
          return strPosition_id;

        String query = "";
        query = "update soft set order_id = null where soft_id = " + strPosition_id;
        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(status);
      } catch (Exception e) {
        transactionSupport.rollbackTransaction(status);
      }
      logger.exit();
      return strPosition_id;
    }


    final public int getProductsListSize(final javax.servlet.http.HttpServletRequest request,
        final OrderBean orderBean) throws Exception {

      logger.entry(request, orderBean);
      String query = "";
      query = "SELECT count( orders.order_id ) as size FROM orders "
          // + "RIGHT JOIN basket ON orders.order_id = basket.order_id "
          + " JOIN basket  ON orders.order_id = basket.order_id "
          + "LEFT  JOIN soft  ON soft.soft_id = basket.product_id "
          + "LEFT  JOIN file  ON soft.file_id = file.file_id  "
          + "LEFT  JOIN images ON soft.image_id = images.image_id "
          + "LEFT OUTER   JOIN currency  currency_order  ON orders.currency_id = currency_order.currency_id "
          + "LEFT OUTER   JOIN currency  currency_product ON soft.currency = currency_product.currency_id "
          + "LEFT OUTER   JOIN paystatus  ON orders.paystatus_id = paystatus.paystatus_id  "
          + "WHERE orders.order_id = " + orderBean.getOrder_id();

      Integer size = transactionSupport.getJdbcTemplate().queryForObject(query, Integer.class);


      logger.exit();
      return size;
    }

	
	
    final public String getOrderByAccount(final String account_history_id) throws Exception {


      String query = "";
      query = "SELECT order_id  FROM account_hist WHERE account_hist.id = " + account_history_id;

      Long order_id = transactionSupport.getJdbcTemplate().queryForObject(query, Long.class);

      return Long.toString(order_id);
    }

	final public int stringToInt(final String s) 
	{
		int i;
		try 
		{
			i = Integer.parseInt(s);
		}
		catch (NumberFormatException ex) 
		{
			i = 0;
		}
		return i;
	}

	final public boolean setSelectedDemand() 
	{
		return true;
	}

	final public boolean setPassiveDemand() 
	{
		return true;
	}


	
	

	
    final public int setSave(final OrderBean orderBean) throws Exception {
      String query = "";
      TransactionStatus trxnStatus = transactionSupport.beginTransaction();
      // if(shipment_address == null || shipment_address.length() == 0)
      // return 1 ;
      if (orderBean.getCountry_id() == null || orderBean.getCountry_id().length() == 0)
        return 3;
      if (orderBean.getCity_id() == null || orderBean.getCity_id().length() == 0)
        return 4;
      if (orderBean.getshipment_phone() == null || orderBean.getshipment_phone().length() == 0)
        return 5;
      if (orderBean.getContact_person() == null || orderBean.getContact_person().length() == 0)
        return 6;

      query = "update orders  set user_id =  ? , amount = ? , tax = ? , end_amount = ? , "
          + " delivery_amount = ? , paystatus_id = ? , deliverystatus_id = ? , "
          + " delivery_start = ? , currency_id = ? , country_id = ? , city_id = ? , "
          + " address = ? , phone = ? , contact_person = ? , email = ?  , fax = ? , description = ? "
          + " where order_id = " + orderBean.getOrder_id();

      Map<String, Object> args = new HashMap<String, Object>();
      args.put("user_id", Long.valueOf(orderBean.getUser_ID()));
      args.put("amount", Double.valueOf(orderBean.getorder_amount()));
      args.put("tax", Double.valueOf(orderBean.getorder_tax()));
      args.put("end_amount", Double.valueOf(orderBean.getend_amount()));
      args.put("delivery_amount", Double.valueOf(orderBean.getDelivery_amoun()));
      args.put("paystatus_id", Long.valueOf(orderBean.getorder_paystatus()));
      args.put("deliverystatus_id", Long.valueOf(orderBean.getDeliverystatus_id()));
      args.put("delivery_start", new java.util.Date());
      args.put("currency_id", Long.valueOf(orderBean.getOrder_currency_id()));
      args.put("country_id", Long.valueOf(orderBean.getCountry_id()));
      args.put("city_id", Long.valueOf(orderBean.getCity_id()));
      args.put("address", orderBean.getshipment_address());
      args.put("phone", orderBean.getshipment_phone());
      args.put("contact_person", orderBean.getContact_person());
      args.put("email", orderBean.getshipment_email());
      args.put("fax", orderBean.getshipment_fax());
      args.put("description", orderBean.getshipment_description());
      try {


        if (orderBean.getAccount_history_id().length() == 0) {
          long intUser_id = Long.parseLong(orderBean.getUser_ID());
          float Balans = orderBean.getBalans();
          float fltEnd_amount = (new Float(orderBean.getend_amount())).floatValue();
          float fltTotal_amount = (Balans - fltEnd_amount);
          float fltOrder_tax = (new Float(orderBean.getorder_tax())).floatValue();
          float fltWithtaxTotal_amount = fltTotal_amount - fltOrder_tax;
          CurrencyHash currencyHash = CurrencyHash.getInstance();
          com.cbsinc.cms.publisher.models.Currency curr =
              currencyHash.getCurrency(orderBean.getOrder_currency_id());
          // query = "SELECT NEXT VALUE FOR account_hist_id_seq AS ID FROM ONE_SEQUENCES";


          orderBean.setAccount_history_id(
              transactionSupport.getJdbcTemplate().queryForObject(account_hist_sequence_rs, String.class));
          String strAccountCurrency_id = "-1";
          query = "SELECT currency_id from account WHERE  user_id = " + orderBean.getUser_ID();
          strAccountCurrency_id =
              String.valueOf(transactionSupport.getJdbcTemplate().queryForObject(query, Long.class));

          query = "insert into account_hist (id  ,  user_id ,  order_id ,  add_amount , "
              + " old_amount  ,  date_input ,  date_end , complete   ,  decsription  ,  currency_id_add  , "
              + " currency_id_old  , "
              + " currency_id_total  ,  active  ,  account_hist.sysdate  ,  total_amount ,  tax  , "
              + " withtax_total_amount , rate ) " + " VALUES ( ?  ,  ? ,  ? ,  ? , "
              + " ?  ,  ? ,  ? , ?   ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ?  ,  ? ,  ?  ,  ? , ? )";


          args = new HashMap<String, Object>();
          args.put("id", Long.parseLong(orderBean.getAccount_history_id()));
          args.put("user_id", Long.parseLong(orderBean.getUser_ID()));
          args.put("order_id", Long.parseLong(orderBean.getOrder_id()));
          args.put("add_amount", Float.parseFloat(orderBean.getend_amount()) * -1);
          args.put("old_amount", Balans);
          args.put("date_input", new java.util.Date());
          args.put("date_end", new java.util.Date());
          args.put("complete", true);
          args.put("decsription", "Credit operation for order  N " + orderBean.getOrder_id());
          args.put("currency_id_add", Long.parseLong(orderBean.getOrder_currency_id()));
          args.put("currency_id_old", Long.parseLong(strAccountCurrency_id));
          args.put("currency_id_total", Long.parseLong(strAccountCurrency_id));
          args.put("active", false);
          args.put("account_hist.sysdate", new java.util.Date());
          args.put("total_amount", fltTotal_amount);
          args.put("tax", Double.parseDouble(orderBean.getorder_tax()));
          args.put("withtax_total_amount", fltWithtaxTotal_amount);
          args.put("rate", curr.getRate());
          transactionSupport.getJdbcTemplate().update(query, args);

          query = "update account set amount = ? where  user_id = " + orderBean.getUser_ID();
          args = new HashMap<String, Object>();
          args.put("amount", (double) (Balans - fltEnd_amount));
          transactionSupport.getJdbcTemplate().update(query, args);
          transactionSupport.commitTransaction(trxnStatus);
        }
      } catch (Exception e) {
        transactionSupport.rollbackTransaction(trxnStatus);
      }

      return 0;
    }

    final public int setSaveWithOutDeductMoney(final OrderBean orderBean) throws Exception {
      logger.entry(orderBean);
      String query = "";
      TransactionStatus trxnStatus = transactionSupport.beginTransaction();
      if (orderBean.getCountry_id() == null || orderBean.getCountry_id().length() == 0)
        return 3;
      if (orderBean.getCity_id() == null || orderBean.getCity_id().length() == 0)
        return 4;
      if (orderBean.getshipment_phone() == null || orderBean.getshipment_phone().length() == 0)
        return 5;
      if (orderBean.getContact_person() == null || orderBean.getContact_person().length() == 0)
        return 6;



      query = "update orders  set user_id =  ? , amount = ? , tax = ? , end_amount = ? , "
          + " delivery_amount = ? , paystatus_id = ? , deliverystatus_id = ? , "
          + " delivery_start = ? , currency_id = ? , country_id = ? , city_id = ? , "
          + " address = ? , phone = ? , contact_person = ? , email = ?  , fax = ? , description = ? "
          + " where order_id = " + orderBean.getOrder_id();

      try {
        transactionSupport.getJdbcTemplate().update(query, Long.valueOf(orderBean.getUser_ID()),
            Double.valueOf(orderBean.getorder_amount()), Double.valueOf(orderBean.getorder_tax()),
            Double.valueOf(orderBean.getend_amount()),
            Double.valueOf(orderBean.getDelivery_amoun()),
            Long.valueOf(orderBean.getorder_paystatus()),
            Long.valueOf(orderBean.getDeliverystatus_id()), new java.util.Date(),
            Long.valueOf(orderBean.getOrder_currency_id()), Long.valueOf(orderBean.getCountry_id()),
            Long.valueOf(orderBean.getCity_id()), orderBean.getshipment_address(),
            orderBean.getshipment_phone(), orderBean.getContact_person(),
            orderBean.getshipment_email(), orderBean.getshipment_fax(),
            orderBean.getshipment_description());
        transactionSupport.commitTransaction(trxnStatus);
      } catch (Exception e) {
        transactionSupport.rollbackTransaction(trxnStatus);
      }

      logger.exit();
      return 0;
    }

	final public List<OrderBean> getProducts(final javax.servlet.http.HttpServletRequest request , final OrderBean orderBean ) throws SQLException {


		String query = "";
		query = "SELECT "
		+ "orders.order_id,"
		+ "orders.delivery_timeend,"
		+ "orders.end_amount, "
		+ "orders.amount, "
		+ "orders.tax, "
		+ "orders.delivery_long ,"
		+ "orders.delivery_start,"
		+ "orders.cdate , "
		+ "soft.name, "
		+ "soft.description ,"
		+ "soft.cost, "
		+ "soft.weight, "
		//+ "soft.\"count\" , "
		//+ "orders.amount, "
		+  "soft.count , "
		+ "images.img_url ,"
		+ "currency_product.currency_lable,"
		+ "orders.address,"
		+ "orders.phone,"
		+ "orders.contact_person, "
		+ "orders.email, "
		+ "orders.fax, "
		+ "orders.description, "
		+ "orders.zip, "
		+ "currency_product.currency_id, "
		+ "orders.delivery_amount, "
		+ "orders.country_id, "
		+ "orders.city_id , "
		+ "currency_product.currency_lable As curr_lable ,"
		+ "currency_product.currency_id As curr_cd ,"
		+ "basket.basket_id ,"
		+ "paystatus.lable as order_paystatus ,"
		+ "paystatus.paystatus_id , "
		+ "file.file_id , "
		+ "deliverystatus.deliverystatus_id , "
		+ "deliverystatus.lable as order_status,  "
		+ "basket.quantity "
		+ " FROM orders "
		//+ "RIGHT  JOIN basket  ON orders.order_id = basket.order_id "
		+ " JOIN basket  ON orders.order_id = basket.order_id "
		+ "LEFT  JOIN soft  ON soft.soft_id = basket.product_id "
		+ "LEFT  JOIN file  ON soft.file_id = file.file_id  "
		+ "LEFT  JOIN images ON soft.image_id = images.image_id "
		+ "LEFT OUTER   JOIN currency  currency_order  ON orders.currency_id = currency_order.currency_id "
		+ "LEFT OUTER   JOIN currency  currency_product ON soft.currency = currency_product.currency_id "
		+ "LEFT OUTER   JOIN paystatus  ON orders.paystatus_id = paystatus.paystatus_id  "
		+ "LEFT OUTER   JOIN deliverystatus  ON orders.deliverystatus_id = deliverystatus.deliverystatus_id  "
		+ "WHERE orders.order_id = ? limit ? offset ?" ;// + orderBean.getOrder_id() + " LIMIT 10  OFFSET "	+ orderBean.getOffset();
// GROUP BY orders.order_id,orders.delivery_timeend,orders.end_amount, orders.amount, orders.tax, orders.delivery_long ,orders.delivery_start,orders.cdate ,soft.name ,soft.description ,soft.cost , soft.weight , images.img_url , currency_product.currency_lable , orders.address , orders.phone , orders.contact_person , orders.email , orders.fax , orders.description , orders.zip , orders.zip , currency_product.currency_id , orders.delivery_amount , orders.country_id , orders.country_id , orders.city_id , basket.basket_id , paystatus.lable , paystatus.paystatus_id , file.file_id , deliverystatus.deliverystatus_id , deliverystatus.lable

		try 
		{
			
			
			Object[] args = new Object[1];
			args[0] =  Long.valueOf(orderBean.getOrder_id()) ;
			args[1] = 10;
			args[2] = orderBean.getOffset();
			List<OrderBean> queryForProducts= transactionSupport.getJdbcTemplate().query(query, new OrderProductBeanRowMapper(), args);
			
			orderBean.setPagecount(queryForProducts.size());
			logger.exit();
			return queryForProducts;
		}catch(Exception e) {
		  logger.error(e.getLocalizedMessage());
		}
		return null;
		
	}
	
	
    final public List<HistoryOrder> getOrderlist(long user_id, final OrderListBean orderListBean,
        Locale locale) {


      String query = "";
      query = "SELECT " + "orders.order_id," + "orders.end_amount, " + "orders.cdate , "
          + "paystatus.lable , " + "paystatus.paystatus_id  , " + "currency.currency_lable "
          + " FROM orders "
          + " LEFT  JOIN paystatus ON orders.paystatus_id  =  paystatus.paystatus_id "
          + " LEFT OUTER   JOIN currency ON orders.currency_id = currency.currency_id "
          + "WHERE orders.user_id = ? ORDER BY orders.order_id DESC  " + " LIMIT 10  OFFSET "
          + orderListBean.getOffset();

      Object[] args = new Object[1];
      args[0] = Long.valueOf(user_id);

      List<HistoryOrder> orderHistoryList =
          transactionSupport.getJdbcTemplate().query(query, new HistoryOrderBeanMapper(), args);
      
      return orderHistoryList;

    }
	
	
	
    final public List<HistoryOrder> getOrderlistByDate(final long user_id, final OrderListBean orderListBean,
        final Locale locale, long role_id, String site_id) {
      String query = "";
      Object[] args = null;
      if (role_id == 2) {
        query = "SELECT " + "orders.order_id," + "orders.end_amount, " + "orders.cdate , "
            + "paystatus.lable , " + "paystatus.paystatus_id  , " + "currency.currency_lable "
            + " FROM orders "
            + " LEFT  JOIN paystatus ON orders.paystatus_id  =  paystatus.paystatus_id "
            + " LEFT OUTER   JOIN currency ON orders.currency_id = currency.currency_id "
            + " LEFT JOIN tuser ON orders.user_id = tuser.user_id "
            + "WHERE tuser.site_id  = ? and orders.cdate >= ? and orders.cdate <=  ? "; // LIMIT 10
                                                                                        // OFFSET ?
                                                                                        // " ;

        args = new Object[3];
        args[0] = Long.valueOf(site_id);
        args[1] = orderListBean.getSQLDateFrom();
        args[2] = orderListBean.getSQLDateTo();

      } else {
        query = "SELECT " + "orders.order_id," + "orders.end_amount, " + "orders.cdate , "
            + "paystatus.lable , " + "paystatus.paystatus_id  , " + "currency.currency_lable "
            + " FROM orders "
            + " LEFT  JOIN paystatus ON orders.paystatus_id  =  paystatus.paystatus_id "
            + " LEFT OUTER   JOIN currency ON orders.currency_id = currency.currency_id "
            + "WHERE orders.user_id = ? and orders.cdate >= ? and orders.cdate <=  ? "; // LIMIT 10
                                                                                        // OFFSET ?
                                                                                        // " ;

        args = new Object[3];
        args[0] = Long.valueOf(user_id);
        args[1] = orderListBean.getSQLDateFrom();
        args[2] = orderListBean.getSQLDateTo();

      }

      List<HistoryOrder> orderList = transactionSupport.getJdbcTemplate().query(query, new HistoryOrderBeanMapper(), args);


      return orderList;

    }
	
    final public List<HistoryOrder> getOrderlistByStatus(final String site_id,
        final OrderListBean orderListBean, final Locale locale) {
      logger.entry(site_id, orderListBean, locale);



      String query = "";
      query = "SELECT " + "orders.order_id," + "orders.end_amount, " + "orders.cdate , "
          + "paystatus.lable , " + "paystatus.paystatus_id  , " + "currency.currency_lable "
          + " FROM orders "
          + " LEFT  JOIN paystatus ON orders.paystatus_id  =  paystatus.paystatus_id "
          + " LEFT OUTER   JOIN currency ON orders.currency_id = currency.currency_id "
          + " LEFT JOIN tuser ON orders.user_id = tuser.user_id "
          + "WHERE tuser.site_id  = ? and ( orders.paystatus_id = ? or orders.deliverystatus_id =  ? )"; // LIMIT
                                                                                                         // 10
                                                                                                         // OFFSET
                                                                                                         // ?
                                                                                                         // "
                                                                                                         // ;



      Object[] args = new Object[3];
      args[0] = Long.valueOf(site_id);
      args[1] = orderListBean.getOrder_paystatus_id();
      args[2] = orderListBean.getDeliverystatus_id();
      List<HistoryOrder> orderList = transactionSupport.getJdbcTemplate().query(query, new HistoryOrderBeanMapper(), args);



      logger.exit();
      return orderList;

    }
	

    final public List<AccountHistoryDetalBean> getPaymentlistByDate(final long intUserID,
        final long role_id, final AccountHistoryBean accountHistoryBean) {



      String query =
          "SELECT  -.add_amount, account_hist.old_amount, account_hist.date_input, account_hist.date_end, account_hist.sysdate, account_hist.complete, account_hist.decsription, account_hist.active , account_hist.id  , account_hist.total_amount , "
              + " currency_add.currency_lable , currency_old.currency_lable ,currency_total.currency_lable , account_hist.rezult_cd "
              + " FROM account_hist  "
              + " LEFT OUTER   JOIN currency  currency_add ON account_hist.currency_id_add = currency_add.currency_id "
              + " LEFT OUTER   JOIN currency  currency_old ON account_hist.currency_id_old = currency_old.currency_id "
              + " LEFT OUTER   JOIN currency  currency_total ON account_hist.currency_id_total = currency_total.currency_id "
              + " WHERE account_hist.user_id = ? and account_hist.sysdate >= ? and account_hist.sysdate <=  ? "
              + " ORDER BY account_hist.id DESC "; // limit 10 offset " +
                                                   // accountHistoryBean.getOffset();

      Object[] args = new Object[3];
      args[0] = intUserID;
      args[1] = accountHistoryBean.getSQLDateFrom();
      args[2] = accountHistoryBean.getSQLDateTo();
      List<AccountHistoryDetalBean> accountDetailList =
          transactionSupport.getJdbcTemplate().query(query, new AccountHistoryDetalBeanRowMapper(), args);


      return accountDetailList;

    }


	final public List<AccountHistoryDetalBean> getPaymentlist(final long intUserID,
	    final long role_id, final AccountHistoryBean accountHistoryBean) {


	  String query =
	      "SELECT  account_hist.add_amount, account_hist.old_amount, account_hist.date_input, account_hist.date_end, account_hist.sysdate, account_hist.complete, account_hist.decsription, account_hist.active , account_hist.id  , account_hist.total_amount , "
	          + " currency_add.currency_lable , currency_old.currency_lable ,currency_total.currency_lable , account_hist.rezult_cd "
	          + " FROM account_hist  "
	          + " LEFT OUTER   JOIN currency  currency_add ON account_hist.currency_id_add = currency_add.currency_id "
	          + " LEFT OUTER   JOIN currency  currency_old ON account_hist.currency_id_old = currency_old.currency_id "
	          + " LEFT OUTER   JOIN currency  currency_total ON account_hist.currency_id_total = currency_total.currency_id "
	          + " WHERE account_hist.user_id = ?  and account_hist.complete = true  ORDER BY account_hist.id DESC "; // limit

	  List<AccountHistoryDetalBean> accountDetailList = transactionSupport.getJdbcTemplate().query(query,
	      new AccountHistoryDetalBeanRowMapper(), new Object[] {intUserID});

	  return accountDetailList;


	}
	
    
    public void setStatusInrocess(String order_id) throws Exception {
      logger.entry(order_id);
      TransactionStatus status = transactionSupport.beginTransaction();
      try {
        String query = "update orders  set paystatus_id = 2 where order_id = " + order_id;
        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(status);
      } catch (Exception e) {
        transactionSupport.rollbackTransaction(status);
      }
      logger.exit();

    }


    public AccountHistoryDetalBean getPayment(final long intUserID, final long role_id,
        String amount_id) {
      logger.entry(intUserID, role_id, amount_id);
      AccountHistoryDetalBean accountHistoryDetalBean =null;
      try {
      String query =
          "SELECT  account_hist.add_amount, account_hist.old_amount, account_hist.date_input, account_hist.date_end, account_hist.sysdate, account_hist.complete, account_hist.decsription, account_hist.active , account_hist.id  , account_hist.total_amount , "
              + " currency_add.currency_lable , currency_old.currency_lable ,currency_total.currency_lable , account_hist.user_ip , account_hist.user_header , account_hist.rezult_cd "
              + " FROM account_hist  "
              + " LEFT OUTER   JOIN currency  currency_add ON account_hist.currency_id_add = currency_add.currency_id "
              + " LEFT OUTER   JOIN currency  currency_old ON account_hist.currency_id_old = currency_old.currency_id "
              + " LEFT OUTER   JOIN currency  currency_total ON account_hist.currency_id_total = currency_total.currency_id "
              + " WHERE account_hist.id =  "+Integer.parseInt("12");

      accountHistoryDetalBean  = transactionSupport.getJdbcTemplate().queryForObject(query,
          new AccountHistoryDetalBeanRowMapper());
      }catch(IncorrectResultSizeDataAccessException e) {
        logger.error("Either Data not present in Database or more than one Object Found");
        return null;
      }
      logger.exit();
      return accountHistoryDetalBean;

    }



}
