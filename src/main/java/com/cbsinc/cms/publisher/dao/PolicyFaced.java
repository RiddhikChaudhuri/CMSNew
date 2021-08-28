package com.cbsinc.cms.publisher.dao;
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
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.exceptions.LocalException;
import com.cbsinc.cms.publisher.controllers.Layout;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.Currency;
import com.cbsinc.cms.publisher.models.CurrencyHash;
import com.cbsinc.cms.publisher.models.ItemDescriptionBean;
import com.fasterxml.jackson.databind.ObjectMapper;


@Repository
@PropertySources(value = {
  @PropertySource("classpath:sequence.properties"),
  @PropertySource("classpath:SetupApplicationResources.properties")
})
public class PolicyFaced extends WebControls implements
		java.io.Serializable {
	/**
	 * 
	 */
	 private static final long serialVersionUID = -6657917721785944014L;
	 
	  @Autowired
	  TransactionSupport transactionSupport;
	  
	/**
	 * <p>
	 * Title: Content Manager System
	 * </p>
	 * <p>
	 * Description: System building web application develop by Konstantin Grabko.
	 * Konstantin Grabko is Owner and author this code.
	 * Программный код написан Грабко Константином Владимировичем и является его интеллектуальной 
	 * собственностью.
	 * </p>
	 * <p>
	 * Copyright: Copyright (c) 2008
	 * </p>
	 * <p>
	 * Company: Предприниматель Грабко Константин Владимирович
	 * </p>
	 * 
	 * @author Konstantin Grabko
	 * @version 1.0
	 */

	

	

     @Value("${tuser}")
     private String tuser_sequence_rs;
     
     
     @Value("${account_hist}")
     private String account_hist_sequence_rs;
     
     @Value("${pay_for_user_session}")
     private String pay_for_user_session;

	
	 private XLogger logger = XLoggerFactory.getXLogger(PolicyFaced.class.getName());
	float fltEnd_amount = (float) 0.01 ;
	
    public PolicyFaced() {
      String amount = pay_for_user_session;
      fltEnd_amount = Float.parseFloat(amount != null ? amount : "0");

      logger.entry(amount,fltEnd_amount);
      logger.exit();

    }

	
	final public long incrementShowPage(final ItemDescriptionBean policyBean ) throws SQLException {
		logger.entry(policyBean);
		long statictic = 0 ;
		TransactionStatus transactionStatus = transactionSupport.beginTransaction();
		String query = "SELECT STATISTIC_ID  FROM soft where soft_id = " + policyBean.getProduct_id() ;
		try {
		    List<Map<String,Object>> statisticList = transactionSupport.getJdbcTemplate().queryForList(query);
			if(statisticList.size() > 0) 
			{
			statictic = Long.parseLong((String)statisticList.get(0).get("STATISTIC_ID"));
			statictic = statictic + 1;
			
			query = "update soft set  STATISTIC_ID = " + statictic + " where soft_id = " +  policyBean.getProduct_id() ;

			transactionSupport.getJdbcTemplate().update(query);
			}
			transactionSupport.commitTransaction(transactionStatus);
		} catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(),ex);
			logger.error("Method " + "incrementShowPage()");
			logger.error(query);
			transactionSupport.rollbackTransaction(transactionStatus);
			throw ex;
		} finally {
			logger.exit();
		}
		return statictic ;
	}
	
	final	public long payMoneyForShowPage(final ItemDescriptionBean policyBean , final AuthorizationPageBean authorizationPageBean  , final String userIPAddress  ) throws Exception {
		logger.entry(policyBean,authorizationPageBean,userIPAddress);
		if( authorizationPageBean.getIntLevelUp() == 2 ) return 0 ;
		
		
		long statictic = 0 ;
		long ownerSite = 0 ;
		String query = "" ;
		//query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  " +
		//" FROM tuser  where  levelup_cd = 2 and site_id = "	+ authorizationPageBean.getSite_id() + "" ;
		//query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  " +
		//" FROM tuser  where  levelup_cd = 1 and site_id = "	+ authorizationPageBean.getSite_id() + "" ;
		query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id FROM tuser  where  levelup_cd = 1 " +
				" and login in (	SELECT login  FROM tuser  where  levelup_cd = 2 and site_id = "	+ authorizationPageBean.getSite_id() + "  )" ;
		TransactionStatus transactionStatus = transactionSupport.beginTransaction();
		try 
		{
		List<Map<String,Object>> rows= transactionSupport.getJdbcTemplate().queryForList(query);
		
		if (rows.size() > 0) 
		{
			ownerSite =  Long.parseLong((String) rows.get(0).get("user_id"));
		}
		else throw new LocalException() ;
		
		
		query = "SELECT STATISTIC_ID  FROM soft where soft_id = " + policyBean.getProduct_id() ;
		rows.clear();
		 rows= transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() == 0)  throw new LocalException() ;
			statictic = Long.parseLong((String)rows.get(0).get("STATISTIC_ID"));
			statictic = statictic + 1;
			
			query = "update soft set  STATISTIC_ID = " + statictic + " where soft_id = " +  policyBean.getProduct_id() ;

			transactionSupport.getJdbcTemplate().update(query);
			
			//if (orderBean.getAccount_history_id().length() == 0) {
				//int intUser_id = Integer.parseInt(orderBean.getUser_ID());
				float Balans = getBalans(ownerSite);
				
				float fltTotal_amount = (Balans - fltEnd_amount);
				
				float fltWithtaxTotal_amount = fltTotal_amount ;
				//query = "SELECT NEXT VALUE FOR account_hist_id_seq  AS ID  FROM ONE_SEQUENCES";
				//query = account_hist_sequence_rs;

				String account_history_id = transactionSupport.getJdbcTemplate().queryForObject(account_hist_sequence_rs, String.class);
				String strAccountCurrency_id = "-1";
				query = "SELECT currency_id from account WHERE  user_id = " + ownerSite;
				rows.clear();
				rows = transactionSupport.getJdbcTemplate().queryForList(query);
				strAccountCurrency_id = (String)rows.get(0).get("currency_id");
				
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				Currency curr = currencyHash.getCurrency(strAccountCurrency_id);

				query = "insert into account_hist ( id  , "
						+ " user_id ,  order_id ,  add_amount , "
						+ " old_amount  ,  date_input ,  date_end , "
						+ " complete   ,  decsription  , "
						+ " currency_id_add  ,  currency_id_old  , "
						+ " currency_id_total  ,  active  , "
						+ " account_hist.sysdate  ,  total_amount ,  tax  , "
						+ " withtax_total_amount , rate )"
						+ " VALUES " + "( ?  , "
						+ " ?  ,  ? ,  ? , "
						+ " ?  ,  ? ,  ? , "
						+ " ?  ,  ?  , "
						+ " ?  ,  ?  , "
						+ " ?  ,  ?  , "
						+ " ?  ,  ? ,  ?  , "
						+ " ?  , ? )";

				///Adp.executeUpdate(query);
				transactionSupport.getJdbcTemplate().update(query, Long.parseLong(account_history_id),ownerSite,0,fltEnd_amount * -1,Balans,new java.util.Date(),new java.util.Date(),true," Payment  "+ fltEnd_amount +" for user which is visited site from IP  " + userIPAddress,
				    Long.parseLong(strAccountCurrency_id), Long.parseLong(strAccountCurrency_id), Long.parseLong(strAccountCurrency_id),false,new java.util.Date(),fltTotal_amount,
				    1,fltWithtaxTotal_amount,curr.getRate());
				
				query = "update account set amount = ?  where  user_id = " + ownerSite ;
				transactionSupport.getJdbcTemplate().update(query, (Balans - fltEnd_amount) );
				transactionSupport.commitTransaction(transactionStatus);
		} 
		catch (LocalException ex) 
        {
            transactionSupport.rollbackTransaction(transactionStatus);
            logger.error(query);
        }
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage());
			logger.error("Method " + "payMoneyForShowPage()");
			logger.error(query);
			transactionSupport.rollbackTransaction(transactionStatus);
			throw ex;
		}
		
		finally 
		{
			logger.exit();
		}
		
		return statictic ;
	}
	
	final public void setRatring1(final int bal , final String product_id )  {
		logger.entry(bal,product_id);
		TransactionStatus transactionStatus = transactionSupport.beginTransaction();
		long rating_summ = 0 ;
		long rating_count = 0 ;
		long midle_bal = 0 ;
		String query = "SELECT RATING_SUMM1 , COUNTPOST_RATING1  FROM soft where soft_id = " + product_id ;
		try {
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if(rows.size() > 0)
			{
			Map<String,Object> row = rows.get(0);
			rating_summ = Long.parseLong((String)row.get("RATING_SUMM1"));
			rating_summ = rating_summ + bal;
			rating_count = Long.parseLong((String)row.get("COUNTPOST_RATING1"));
			rating_count = rating_count + 1;
			midle_bal = rating_summ / rating_count ;
			
			query = "update soft set  RATING_SUMM1 = " + rating_summ + " , COUNTPOST_RATING1 = " + rating_count + " , MIDLE_BAL1 = "+midle_bal+" where soft_id = " +  product_id;
			transactionSupport.getJdbcTemplate().update(query);
			}
			transactionSupport.commitTransaction(transactionStatus);
		}
		catch (Exception ex) 
		{
			logger.error(query,ex);
			transactionSupport.rollbackTransaction(transactionStatus);
		}
		finally 
		{
			logger.exit();
		}

	}

	

	
    final public String getRatring1Map(final String product_id) {
      logger.entry(product_id);
      long midle_bal = 0;
      int number = 0;
      String query = "SELECT MIDLE_BAL1  FROM soft where soft_id = " + product_id;
      Map<String, Boolean> map = new TreeMap<String, Boolean>();
      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        if (rows.size() == 0)
          return null;
        midle_bal = Long.parseLong((String) rows.get(0).get("MIDLE_BAL1"));

        for (int i = 0; 10 > i; i++) {
          number = i + 1;
          if (midle_bal > i) {
            map.put("show_star_" + number, Boolean.TRUE);
          } else {
            map.put("show_star_" + number, Boolean.FALSE);
          }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(map);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage());
      } finally {
        logger.exit();
      }
      return "0";



    }

	

	/**
	 * Extention info 2 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id - Установить родителя для записи обязательно
	 * @return
	 * @throws Exception 
	 */

	final public void  mergePolicyBean(final long user_id, final String productId	, final ItemDescriptionBean policyBeanId  ) 
	{
		
	  logger.entry(productId,policyBeanId,user_id);
	  String query = "";

		query = "SELECT  soft.soft_id, soft.name,soft.description, soft.version, soft.cost, soft.currency, soft.serial_nubmer, file.file_id, "
		    + "soft.type_id, soft.active , soft.phonetype_id , soft.progname_id  , soft.image_id , images.img_url , soft.fulldescription , "
		    + "big_images.img_url , soft.user_id  , soft.CDATE , soft.STATISTIC_ID , tuser.FIRST_NAME , tuser.LAST_NAME , tuser.COMPANY , "
		    + "soft.tree_id ,  , show_rating1 , show_rating2 , show_rating3 , jsp_url , portlettype_id  FROM soft LEFT  JOIN images ON soft.image_id = images.image_id  LEFT  JOIN big_images ON soft.bigimage_id = big_images.big_images_id  LEFT  JOIN file  ON soft.file_id = file.file_id LEFT  JOIN tuser  ON soft.user_id = tuser.user_id  WHERE  soft.soft_id = " + productId ;

		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() > 0)	
			{
				/*
				size = (String) Adp.getValueAt(0, 0);

				file_id = position.rows[intRow_id][1];
				// file_id = (String)position.newsAdp.getValueAt(intRow_id , 7) == null
				// ?"":(String)position.newsAdp.getValueAt(intRow_id , 7) ;
				if (file_id.length() > 0)
					file_exist = "true";
				else
					file_exist = "";
				if (position.Adp.getRowCount() == 0) {
					return;
				}
				*/
				
				String file_id = (String)rows.get(0).get("file.file_id") ;
				if (file_id.length() > 0) policyBeanId.setFile_exist("true");
				else policyBeanId.setFile_exist("");
				
				policyBeanId.setProduct_id((String)rows.get(0).get("soft.soft_id"));
				
				String portlettype_id = ((String)rows.get(0).get("portlettype_id")).toLowerCase();
				if(portlettype_id != null && portlettype_id.length() > 0)
				policyBeanId.setPortlettype_id(Long.parseLong(portlettype_id));
				//policyBeanId.setParent_product_id(policyBeanId.getProduct_id()) ;
				//593
				String parentId = (String)rows.get(0).get("soft.tree_id");
				if(parentId.equals("") )parentId = (String)rows.get(0).get("soft.soft_id") ;
				policyBeanId.setParent_product_id(parentId) ;
				policyBeanId.setProductName( (String)rows.get(0).get("soft.name") );
				
				String show_blog = ( (String)rows.get(0).get("show_blog")).toLowerCase();
				policyBeanId.setStrShow_forum(show_blog);

				String show_ratimg1 = ( (String)rows.get(0).get("show_rating1")).toLowerCase();
				policyBeanId.setStrShow_ratimg1(show_ratimg1);

				String show_ratimg2 = ( (String)rows.get(0).get("show_rating2")).toLowerCase();
				policyBeanId.setStrShow_ratimg2(show_ratimg2);

				String show_ratimg3 = ( (String)rows.get(0).get("show_rating3")).toLowerCase();
				policyBeanId.setStrShow_ratimg3(show_ratimg3);
				
				String jsp_url = ( (String)rows.get(0).get("jsp_url")).toLowerCase();
				policyBeanId.setJsp_url(jsp_url);

				
				//policyBeanId.setProductURL("downloadservletbyrowid?row=" + 0);
				policyBeanId.setProductURL("downloadservletbyrowid?productid=" + policyBeanId.getProduct_id());
				policyBeanId.setImg_url( (String)rows.get(0).get("images.img_url"));
				if (policyBeanId.getImg_url() != null)
					policyBeanId.setImgURL(policyBeanId.getImg_url());
				else policyBeanId.setImgURL("images/Folder.jpg");
				policyBeanId.setProductDescription((String)rows.get(0).get("soft.fulldescription"));
				int http = policyBeanId.getProductDescription().indexOf("http://") ; 
				if(http != -1)
				{
				int space = policyBeanId.getProductDescription().indexOf(" ",http) ;
				if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",http) ;
				if(space != -1 )
				{
				String link = policyBeanId.getProductDescription().substring(http,space) ;
				String newlink = " <a href='"+link+"' >" + link + "</a>  " ;
				policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink) );
				}
				
				}
				
				int ftp = policyBeanId.getProductDescription().indexOf("ftp://") ; 
				if(ftp != -1)
				{
					int space = policyBeanId.getProductDescription().indexOf(" ",ftp) ;
					if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",ftp) ;
					if(space != -1 )
					{
						String link = policyBeanId.getProductDescription().substring(ftp,space) ;
						//String newlink = "<![CDATA[ <a href='"+link+"' >" + link + "</a> ]]> " ;
						 String newlink = " <a href='"+link+"' >" + link + "</a> " ;
						 policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink)) ;
					}
				}

				int email = policyBeanId.getProductDescription().indexOf("mailto://") ; 
				if(email != -1)
				{
					int space = policyBeanId.getProductDescription().indexOf(" ",email) ;
					if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",email) ;
					if(space != -1 )
					{
				String link = policyBeanId.getProductDescription().substring(email,space) ;
				//String newlink = "<![CDATA[ <a href='"+link+"' >" + link + "</a> ]]> " ;
				String newlink = " <a href='"+link+"' >" + link + "</a>  " ;
				policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink) );
					}
				}
				
				policyBeanId.setBigimgURL((String)rows.get(0).get("big_images.img_url"));
				policyBeanId.setProductVersion((String)rows.get(0).get("soft.version"));
				policyBeanId.setProductCost((String)rows.get(0).get("soft.cost"));
				policyBeanId.setStrCDate((String)rows.get(0).get("soft.CDATE"));
				//policyBeanId.setStrCDate(policyBeanId.getStrCDate().substring(0,10)) ;
				policyBeanId.setStatistic((String)rows.get(0).get("soft.STATISTIC_ID"));
				
				policyBeanId.setCreator_info_user_id( (String)rows.get(0).get("soft.user_id") ) ;
				//creator_info_user_id= (String) query_result.getValueAt(intRow_id, 16);
			
				String currency_id = (String)rows.get(0).get("soft.currency") ;
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				Currency curr = currencyHash.getCurrency(currency_id);
				if (curr == null)
					throw new Exception("Currency curr == null and currency_id " + currency_id );
				policyBeanId.setCurrency_cd(curr.getCode());
				policyBeanId.setCurrency_desc(currencyHash.getCurrency_decs(currency_id));
				// Not momey not href
				if(getBalans(user_id) < 1 ) policyBeanId.setProductURL("");
				

			}
				
			///Adp.commit();
		} 
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
			//Adp.rollback();
			
		} 
		finally 
		{
			logger.exit();
		}


		
	}


	
	/**
	 * Extention info 2 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id - Установить родителя для записи обязательно
	 * @return
	 * @throws Exception 
	 */

	final public void  mergePolicyBeanForAboutPage(final String siteId	, final ItemDescriptionBean policyBeanId  ) 
	{
		logger.entry(siteId,policyBeanId);
		String query = "";

		query = "SELECT  soft.soft_id, soft.name,soft.description, soft.version, soft.cost, soft.currency, soft.serial_nubmer,"
		    + " file.file_id, soft.type_id, soft.active , soft.phonetype_id , soft.progname_id  , soft.image_id , images.img_url , "
		    + "soft.fulldescription , big_images.img_url , soft.user_id  , soft.CDATE , soft.STATISTIC_ID , tuser.FIRST_NAME , "
		    + "tuser.LAST_NAME , tuser.COMPANY , soft.tree_id ,  show_blog , show_rating1 , show_rating2 , show_rating3 , jsp_url  "
		    + "FROM soft LEFT  JOIN images ON soft.image_id = images.image_id  LEFT  JOIN big_images ON soft.bigimage_id = big_images.big_images_id  LEFT  JOIN file  ON soft.file_id = file.file_id LEFT  JOIN tuser  ON soft.user_id = tuser.user_id  WHERE  soft.site_id = " + siteId  + " and soft.portlettype_id = " + Layout.PAGE_ABOUT_COMPANY ;

		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() > 0)	
			{
			    Map<String,Object> row= rows.get(0);
				/*
				size = (String) Adp.getValueAt(0, 0);

				file_id = position.rows[intRow_id][1];
				// file_id = (String)position.newsAdp.getValueAt(intRow_id , 7) == null
				// ?"":(String)position.newsAdp.getValueAt(intRow_id , 7) ;
				if (file_id.length() > 0)
					file_exist = "true";
				else
					file_exist = "";
				if (position.Adp.getRowCount() == 0) {
					return;
				}
				*/
				
				String file_id = (String)row.get("file.file_id");
				if (file_id.length() > 0) policyBeanId.setFile_exist("true");
				else policyBeanId.setFile_exist("");
				
				policyBeanId.setProduct_id((String)row.get("soft.soft_id"));
				//policyBeanId.setParent_product_id(policyBeanId.getProduct_id()) ;
				//593
				String parentId = (String)row.get("soft.tree_id");
				if(parentId.equals("") )parentId = (String)row.get("soft.soft_id") ;
				policyBeanId.setParent_product_id(parentId) ;
				
				String show_blog = ((String)row.get("show_blog")).toLowerCase();
				policyBeanId.setStrShow_forum(show_blog);

				String show_ratimg1 = ((String)row.get("show_rating1")).toLowerCase();
				policyBeanId.setStrShow_ratimg1(show_ratimg1);

				String show_ratimg2 = ((String)row.get("show_rating2")).toLowerCase();
				policyBeanId.setStrShow_ratimg2(show_ratimg2);

				String show_ratimg3 = ((String)row.get("show_rating3")).toLowerCase();
				policyBeanId.setStrShow_ratimg3(show_ratimg3);
				
				String jsp_url = ((String)row.get("jsp_url")).toLowerCase();
				policyBeanId.setJsp_url(jsp_url);
				
				policyBeanId.setProductName((String)row.get("soft.name"));
				//policyBeanId.setProductURL("downloadservletbyrowid?row=" + 0);
				policyBeanId.setProductURL("downloadservletbyrowid?productid=" + policyBeanId.getProduct_id());
				policyBeanId.setImg_url( (String)row.get("images.img_url"));
				if (policyBeanId.getImg_url() != null)
					policyBeanId.setImgURL(policyBeanId.getImg_url());
				else policyBeanId.setImgURL("images/Folder.jpg");
				policyBeanId.setProductDescription((String)row.get("soft.fulldescription"));
				int http = policyBeanId.getProductDescription().indexOf("http://") ; 
				if(http != -1)
				{
				int space = policyBeanId.getProductDescription().indexOf(" ",http) ;
				if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",http) ;
				if(space != -1 )
				{
				String link = policyBeanId.getProductDescription().substring(http,space) ;
				String newlink = " <a href='"+link+"' >" + link + "</a>  " ;
				policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink) );
				}
				
				}
				
				int ftp = policyBeanId.getProductDescription().indexOf("ftp://") ; 
				if(ftp != -1)
				{
					int space = policyBeanId.getProductDescription().indexOf(" ",ftp) ;
					if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",ftp) ;
					if(space != -1 )
					{
						String link = policyBeanId.getProductDescription().substring(ftp,space) ;
						//String newlink = "<![CDATA[ <a href='"+link+"' >" + link + "</a> ]]> " ;
						 String newlink = " <a href='"+link+"' >" + link + "</a> " ;
						 policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink)) ;
					}
				}

				int email = policyBeanId.getProductDescription().indexOf("mailto://") ; 
				if(email != -1)
				{
					int space = policyBeanId.getProductDescription().indexOf(" ",email) ;
					if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",email) ;
					if(space != -1 )
					{
				String link = policyBeanId.getProductDescription().substring(email,space) ;
				//String newlink = "<![CDATA[ <a href='"+link+"' >" + link + "</a> ]]> " ;
				String newlink = " <a href='"+link+"' >" + link + "</a>  " ;
				policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink) );
					}
				}
				
				policyBeanId.setBigimgURL((String)row.get("big_images.img_url"));
				policyBeanId.setProductVersion((String)row.get("soft.version"));
				policyBeanId.setProductCost( (String)row.get("soft.cost"));
				policyBeanId.setStrCDate((String)row.get("soft.CDATE"));
				//policyBeanId.setStrCDate(policyBeanId.getStrCDate().substring(0,10)) ;
				policyBeanId.setStatistic((String)row.get("soft.STATISTIC_ID"));
				
				policyBeanId.setCreator_info_user_id((String)row.get("soft.user_id")) ;
				//creator_info_user_id= (String) query_result.getValueAt(intRow_id, 16);
			
				String currency_id = (String)row.get("soft.currency");
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				Currency curr = currencyHash.getCurrency(currency_id);
				if (curr == null)	throw new Exception("Currency curr == null and currency_id = " + currency_id );
				policyBeanId.setCurrency_cd(curr.getCode());
				policyBeanId.setCurrency_desc(currencyHash.getCurrency_decs(currency_id));
				// Not momey not href
				//if(getBalans(user_id) < 1 ) policyBeanId.setProductURL("");
				

			}
				
			///Adp.commit();
		}  
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
			//Adp.rollback();
			
		} 
		finally 
		{
			logger.exit();
		}


		
	}


	
	/**
	 * Extention info 2 for policy page
	 * 
	 * @param strUser_id
	 * @param site_id
	 * @param tree_id - Установить родителя для записи обязательно
	 * @return
	 * @throws Exception 
	 */

	final public void  mergePolicyBeanForPayPageInfo(final String siteId	, final ItemDescriptionBean policyBeanId  ) 
	{
	    logger.entry(siteId,policyBeanId);
		
		String query = "";

		query = "SELECT  soft.soft_id, soft.name,soft.description, soft.version, soft.cost, soft.currency, soft.serial_nubmer, "
		    + "file.file_id, soft.type_id, soft.active , soft.phonetype_id , soft.progname_id  , soft.image_id , images.img_url , "
		    + "soft.fulldescription , big_images.img_url , soft.user_id  , soft.CDATE , soft.STATISTIC_ID , tuser.FIRST_NAME , "
		    + "tuser.LAST_NAME , tuser.COMPANY , soft.tree_id ,  show_blog , show_rating1 , show_rating2 , show_rating3 , jsp_url  FROM soft LEFT  JOIN images ON soft.image_id = images.image_id  LEFT  JOIN big_images ON soft.bigimage_id = big_images.big_images_id  LEFT  JOIN file  ON soft.file_id = file.file_id LEFT  JOIN tuser  ON soft.user_id = tuser.user_id  WHERE  soft.site_id = " + siteId  + " and soft.portlettype_id = " + Layout.PAGE_ABOUT_PAY  ;

		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() > 0)	
			{
				/*
				size = (String) Adp.getValueAt(0, 0);

				file_id = position.rows[intRow_id][1];
				// file_id = (String)position.newsAdp.getValueAt(intRow_id , 7) == null
				// ?"":(String)position.newsAdp.getValueAt(intRow_id , 7) ;
				if (file_id.length() > 0)
					file_exist = "true";
				else
					file_exist = "";
				if (position.Adp.getRowCount() == 0) {
					return;
				}
				*/
				Map<String,Object> row = rows.get(0);
				String file_id = (String)row.get("file.file_id") ;
				if (file_id.length() > 0) policyBeanId.setFile_exist("true");
				else policyBeanId.setFile_exist("");
				
				policyBeanId.setProduct_id((String)row.get("soft.soft_id"));
				//policyBeanId.setParent_product_id(policyBeanId.getProduct_id()) ;
				//593
				String parentId = (String)row.get(" soft.tree_id ");
				if(parentId.equals("") )parentId = (String)row.get("soft.soft_id") ;
				policyBeanId.setParent_product_id(parentId) ;
				
				String show_blog = ((String)row.get("show_blog")).toLowerCase();
				policyBeanId.setStrShow_forum(show_blog);

				String show_ratimg1 = ((String)row.get("show_rating1")).toLowerCase();
				policyBeanId.setStrShow_ratimg1(show_ratimg1);

				String show_ratimg2 = ((String)row.get("show_rating2")).toLowerCase();
				policyBeanId.setStrShow_ratimg2(show_ratimg2);

				String show_ratimg3 = ((String)row.get("show_rating3")).toLowerCase();
				policyBeanId.setStrShow_ratimg3(show_ratimg3);
				
				String jsp_url = ((String)row.get("jsp_url")).toLowerCase();
				policyBeanId.setJsp_url(jsp_url);
				
				policyBeanId.setProductName( (String)row.get("soft.name"));
				//policyBeanId.setProductURL("downloadservletbyrowid?row=" + 0);
				policyBeanId.setProductURL("downloadservletbyrowid?productid=" + policyBeanId.getProduct_id());
				policyBeanId.setImg_url((String)row.get("images.img_url"));
				if (policyBeanId.getImg_url() != null)
					policyBeanId.setImgURL(policyBeanId.getImg_url());
				else policyBeanId.setImgURL("images/Folder.jpg");
				policyBeanId.setProductDescription((String)row.get("soft.fulldescription"));
				int http = policyBeanId.getProductDescription().indexOf("http://") ; 
				if(http != -1)
				{
				int space = policyBeanId.getProductDescription().indexOf(" ",http) ;
				if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",http) ;
				if(space != -1 )
				{
				String link = policyBeanId.getProductDescription().substring(http,space) ;
				String newlink = " <a href='"+link+"' >" + link + "</a>  " ;
				policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink) );
				}
				
				}
				
				int ftp = policyBeanId.getProductDescription().indexOf("ftp://") ; 
				if(ftp != -1)
				{
					int space = policyBeanId.getProductDescription().indexOf(" ",ftp) ;
					if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",ftp) ;
					if(space != -1 )
					{
						String link = policyBeanId.getProductDescription().substring(ftp,space) ;
						//String newlink = "<![CDATA[ <a href='"+link+"' >" + link + "</a> ]]> " ;
						 String newlink = " <a href='"+link+"' >" + link + "</a> " ;
						 policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink)) ;
					}
				}

				int email = policyBeanId.getProductDescription().indexOf("mailto://") ; 
				if(email != -1)
				{
					int space = policyBeanId.getProductDescription().indexOf(" ",email) ;
					if(space == -1 )space = policyBeanId.getProductDescription().indexOf("/t",email) ;
					if(space != -1 )
					{
				String link = policyBeanId.getProductDescription().substring(email,space) ;
				//String newlink = "<![CDATA[ <a href='"+link+"' >" + link + "</a> ]]> " ;
				String newlink = " <a href='"+link+"' >" + link + "</a>  " ;
				policyBeanId.setProductDescription(policyBeanId.getProductDescription().replaceAll(link,newlink) );
					}
				}
				
				policyBeanId.setBigimgURL((String)row.get("big_images.img_url"));
				policyBeanId.setProductVersion((String)row.get("soft.version"));
				policyBeanId.setProductCost( (String)row.get("soft.cost"));
				policyBeanId.setStrCDate( (String)row.get("soft.CDATE"));
				//policyBeanId.setStrCDate(policyBeanId.getStrCDate().substring(0,10)) ;
				policyBeanId.setStatistic((String)row.get("soft.STATISTIC_ID"));
				
				policyBeanId.setCreator_info_user_id((String)row.get("soft.user_id")) ;
				//creator_info_user_id= (String) query_result.getValueAt(intRow_id, 16);
			
				String currency_id = (String)row.get("soft.currency");
				CurrencyHash currencyHash = CurrencyHash.getInstance();
				Currency curr = currencyHash.getCurrency(currency_id);
				if (curr == null) 	throw new Exception("Currency curr == null and currency_id = " + currency_id );
				policyBeanId.setCurrency_cd(curr.getCode());
				policyBeanId.setCurrency_desc(currencyHash.getCurrency_decs(currency_id));
				// Not momey not href
				//if(getBalans(user_id) < 1 ) policyBeanId.setProductURL("");
				

			}
				
			///Adp.commit();
		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
			//Adp.rollback();
			
		} 
		finally 
		{
			logger.exit();
		}


		
	}

	
	final public String  getPayPageInfoId(final String siteId ) 
	{
	    logger.entry(siteId);
		String query = "";
		String product_id = "0" ;
		query = "SELECT  soft.soft_id, soft.name,soft.description, soft.version, soft.cost, soft.currency, soft.serial_nubmer, file.file_id, soft.type_id, soft.active , soft.phonetype_id , soft.progname_id  , soft.image_id , images.img_url , soft.fulldescription , big_images.img_url , soft.user_id  , soft.CDATE , soft.STATISTIC_ID , tuser.FIRST_NAME , tuser.LAST_NAME , tuser.COMPANY , soft.tree_id ,  show_blog , show_rating1 , show_rating2 , show_rating3 , jsp_url  FROM soft LEFT  JOIN images ON soft.image_id = images.image_id  LEFT  JOIN big_images ON soft.bigimage_id = big_images.big_images_id  LEFT  JOIN file  ON soft.file_id = file.file_id LEFT  JOIN tuser  ON soft.user_id = tuser.user_id  WHERE  soft.site_id = " + siteId  + " and soft.portlettype_id = " + Layout.PAGE_ABOUT_PAY ; ;

		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() > 0)	
			{
				product_id = (String)rows.get(0).get("soft.soft_id") ;
			}
				
		} 
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage());
			//Adp.rollback();
			
		} 
		finally 
		{
			logger.exit();
		}

      return product_id ;
		
	}

	
	final public String  getAboutPageId(final String siteId ) 
	{
	    logger.entry(siteId);
		String query = "";
		String product_id = "0" ;
		query = "SELECT  soft.soft_id, soft.name,soft.description, soft.version, soft.cost, soft.currency, soft.serial_nubmer, file.file_id, soft.type_id, soft.active , soft.phonetype_id , soft.progname_id  , soft.image_id , images.img_url , soft.fulldescription , big_images.img_url , soft.user_id  , soft.CDATE , soft.STATISTIC_ID , tuser.FIRST_NAME , tuser.LAST_NAME , tuser.COMPANY , soft.tree_id ,  show_blog , show_rating1 , show_rating2 , show_rating3 , jsp_url  FROM soft LEFT  JOIN images ON soft.image_id = images.image_id  LEFT  JOIN big_images ON soft.bigimage_id = big_images.big_images_id  LEFT  JOIN file  ON soft.file_id = file.file_id LEFT  JOIN tuser  ON soft.user_id = tuser.user_id  WHERE  soft.site_id = " + siteId  + " and soft.portlettype_id = " + Layout.PAGE_ABOUT_COMPANY ; 

		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() > 0)	
			{
				product_id = (String)rows.get(0).get("soft.soft_id") ;
			}
				
		} 
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
			//Adp.rollback();
			
		} 
		finally 
		{
			logger.exit();
		}

      return product_id ;
		
	}
	

	// --------- Business logic functionality start -----

	final public int stringToInt(final String s) {
		int i;
		try {
			i = Integer.parseInt(s);
		} catch (NumberFormatException ex) {
			i = 0;
		}
		return i;
	}


	
	// --------- Business logic functionality end -----

	

}
