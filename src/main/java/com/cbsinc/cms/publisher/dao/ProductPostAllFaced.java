package com.cbsinc.cms.publisher.dao;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PostType;
import com.cbsinc.cms.publisher.models.PublisherBean;



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

@Repository
@PropertySources(value = {
    @PropertySource("classpath:sequence.properties"),
    @PropertySource("classpath:SetupApplicationResources.properties")
  })
public class ProductPostAllFaced extends WebControls implements		java.io.Serializable {

	private static final long serialVersionUID = 4142951782238438413L;
	
    @Autowired
    TransactionSupport transactionSupport;
	/**
	 * 
	 */
	private XLogger logger = XLoggerFactory.getXLogger(ProductPostAllFaced.class.getName());
	
	@Value("${limmit_posted_messages}")
    private String limmit_posted_messages_sequence_rs;

	@Value("${images}")
    private String images_sequence_rs;
	
	@Value("${soft}")
    private String soft_sequence_rs;
	
	@Value("${file}")
    private String file_sequence_rs;
	
	@Value("${big_images}")
    private String big_images_sequence_rs;
	
	public ProductPostAllFaced(){
	  logger.entry(limmit_posted_messages_sequence_rs,images_sequence_rs,soft_sequence_rs,file_sequence_rs,big_images_sequence_rs);
	  logger.exit();
	}
	   
	final public void initPage(final String _soft_id , final PublisherBean publisherBeanId , final AuthorizationPageBean authorizationPageBeanId ) throws UnsupportedEncodingException {
	    logger.entry(_soft_id,publisherBeanId,authorizationPageBeanId);
		String query = "select soft.soft_id , soft.name , soft.description , soft.fulldescription ,  soft.version , "
				+ " soft.cost ,  soft.currency ,  soft.serial_nubmer  ,  soft.file_id ,  soft.catalog_id , "
				+ " soft.image_id ,  soft.bigimage_id , soft.user_id ,  soft.phonemodel_id ,  soft.salelogic_id , "
				+ " soft.site_id ,  soft.product_code ,   soft.portlettype_id , soft.creteria1_id , "
				+ " soft.creteria2_id , soft.creteria3_id , soft.creteria4_id , soft.creteria5_id , soft.creteria6_id , "
				+ " soft.creteria7_id , soft.creteria8_id , soft.creteria9_id , "
				+ " soft.creteria10_id , soft.amount1 , soft.amount2 , soft.amount3 , " 
				+ " soft.search2 , soft.name2 , soft.show_rating1 , soft.show_rating2 , soft.show_rating3 , " 
				+ " soft.show_blog , soft.jsp_url , images.imgname as imgname_s ,  big_images.imgname as imgname_b  , file.name , soft.type_id ,  soft1.portlettype_id  from soft LEFT  JOIN images ON soft.image_id = images.image_id  LEFT  JOIN soft soft1 ON soft.soft_id = soft1.tree_id LEFT  JOIN big_images ON soft.bigimage_id = big_images.big_images_id  LEFT  JOIN file  ON  soft.file_id = file.file_id  where soft.soft_id=" + _soft_id;
		try {
		  transactionSupport.getJdbcTemplate().queryForObject(query,new RowMapper<PublisherBean>() {
              
              @Override
              public PublisherBean mapRow(ResultSet rs, int rowNum) throws SQLException {
				publisherBeanId.setSoft_id((String) rs.getString(0));
				try {
	                publisherBeanId.setStrSoftName((String) rs.getString(1));
	              } catch (UnsupportedEncodingException e) {
	                logger.error(e.getLocalizedMessage(),e);
	              }
				publisherBeanId.setStrSoftDescription((String) rs.getString(2));
				publisherBeanId.setProduct_fulldescription((String) rs.getString(3));
				publisherBeanId.setStrSoftVersion((String) rs.getString(4));
				publisherBeanId.setStrSoftCost((String) rs.getString(5));
				publisherBeanId.setStrCurrency((String) rs.getString(6));
				publisherBeanId.setSerial_nubmer((String) rs.getString(7));
				publisherBeanId.setFile_id((String) rs.getString(8));
				authorizationPageBeanId.setCatalog_id((String) rs.getString(9));
				authorizationPageBeanId.setCatalog_parent_id((String) rs.getString(9));
				publisherBeanId.setImage_id((String) rs.getString(10));
				publisherBeanId.setBigimage_id((String) rs.getString(11));
				publisherBeanId.setUser_id((String) rs.getString(12));
				publisherBeanId.setSalelogic_id((String) rs.getString(14));
				publisherBeanId.setSite_id((String) rs.getString(15));
				publisherBeanId.setProduct_code_id((String) rs.getString(16));
				publisherBeanId.setPortlettype_id((String) rs.getString(17));
				publisherBeanId.setCreteria1_id((String) rs.getString(18));
				publisherBeanId.setCreteria2_id((String) rs.getString(19));
				publisherBeanId.setCreteria3_id((String) rs.getString(20));
				publisherBeanId.setCreteria4_id((String) rs.getString(21));
				publisherBeanId.setCreteria5_id((String) rs.getString(22));
				publisherBeanId.setCreteria6_id((String)rs.getString(23));
				publisherBeanId.setCreteria7_id((String) rs.getString(24));
				publisherBeanId.setCreteria8_id((String) rs.getString(25));
				publisherBeanId.setCreteria9_id((String) rs.getString(26));
				publisherBeanId.setCreteria10_id((String) rs.getString(27));
				
				publisherBeanId.setAmount1((String) rs.getString(28)) ;
				publisherBeanId.setAmount2((String) rs.getString(29)) ;
				publisherBeanId.setAmount3((String) rs.getString(30)) ;
				publisherBeanId.setStrSearch2((String)rs.getString(31));				
				publisherBeanId.setStrSoftName2((String) rs.getString(32)) ;
				publisherBeanId.setStrShow_ratimg1((String) rs.getString(33)) ;
				publisherBeanId.setStrShow_ratimg2((String)rs.getString(34)) ;
				publisherBeanId.setStrShow_ratimg3((String) rs.getString(35)) ;
				publisherBeanId.setStrShow_forum((String) rs.getString(36)) ;
				publisherBeanId.setJsp_url((String) rs.getString(37)) ;

				publisherBeanId.setImgname((String) rs.getString(38)) ;
				publisherBeanId.setBigimgname((String) rs.getString(39)) ;
				publisherBeanId.setFilename((String) rs.getString(40)) ;
				publisherBeanId.setType_id((String) rs.getString(41));
				publisherBeanId.setParent_portlettype_id((String) rs.getString(42));
				return publisherBeanId;
			}
              
			});
		  if(!(publisherBeanId != null)) {
				// add current catalog
				publisherBeanId.setSoft_id("-1");
				authorizationPageBeanId.setCatalogParent_id(authorizationPageBeanId.getCatalog_id());
			}

		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
			logger.exit();
		}


		if ( publisherBeanId.getImage_id().length() > 0)
			setImageNameByImage_ID(publisherBeanId.getImage_id(),publisherBeanId);
		if (publisherBeanId.getBigimage_id().length() > 0)
			setBigImageNameByImage_ID(publisherBeanId.getBigimage_id(), publisherBeanId);
		if (publisherBeanId.getFile_id().length() > 0)
			setFileNameByFile_ID(publisherBeanId.getFile_id(),publisherBeanId);
	}

	
	final public String getCatalogParentId(final AuthorizationPageBean authorizationPageBeanId) {
	    logger.entry(authorizationPageBeanId);
		String query = "";
		String  _parent_id = "0" ;
		try 
		{
			
				query = "select a.catalog_id  from catalog a , catalog  b  where   b.parent_id = a.catalog_id  and b.catalog_id = " + authorizationPageBeanId.getCatalog_id()  ;
				List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
				if (rows.size() > 0) 
				{
				    if( ((String) rows.get(0).get("a.catalog_id")).length() > 0 ) _parent_id =  (String)rows.get(0).get("a.catalog_id");;
				}
		
				//authorizationPageBeanId.setCatalogParent_id( _parent_id < 0?"0":"" +_parent_id ) ;		
		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
			logger.exit();
		}
		
		return _parent_id  ;
	}
	
	final public String updateDescSoft(final PublisherBean publisherBeanId, final AuthorizationPageBean authorizationPageBeanId) throws UnsupportedEncodingException {
		logger.entry(publisherBeanId,authorizationPageBeanId);
		String id = publisherBeanId.getSoft_id();
		String query = "" ;
		TransactionStatus trxnstats =  transactionSupport.beginTransaction();
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) return "";
		
		if(publisherBeanId.getStrSoftName2() != null && publisherBeanId.getStrSoftName2().length() > 0 )  
			publisherBeanId.setStrSearch2(publisherBeanId.getStrSoftName2().substring(0, 1));
		
		try 
		{
			query = " update soft set soft_id = ?, " 
				+ " name = ?, "
				+ " description = ?, "
				+ " fulldescription = ?, "
				+ " version = ?, " 
				+ " cost = ?, " 
				+ " currency = ?, "
				+ " file_id = ?, " 
				+ " catalog_id = ?, " 
				+ " image_id = ?, "
				+ " bigimage_id = ?, " 
				+ " salelogic_id = ?, "
				+ " site_id = ?, " 
				+ " product_code = ?, " 
				+ " search = ?, "
				+ " type_id = ? , "
				+ " portlettype_id = ? , "
				+ " creteria1_id = ? , "
				+ " creteria2_id = ? , "
				+ " creteria3_id = ? , "
				+ " creteria4_id = ? , "
				+ " creteria5_id = ? , "
				+ " creteria6_id = ? , "
				+ " creteria7_id = ? , "
				+ " creteria8_id = ? , "
				+ " creteria9_id = ? , "
				+ " creteria10_id = ? , "
				+ " amount1 = ? , "
				+ " amount2 = ? , "
				+ " amount3 = ? , "
				+ " search2 = ? , "
				+ " name2 = ? , "
				+ " SHOW_RATING1 = ? , "
				+ " SHOW_RATING2 = ? , "
				+ " SHOW_RATING3 = ? , "
				+ " SHOW_BLOG = ? , "
				+ " lang_id = ? , "
				+ " jsp_url = ? , "
				+ " CDATE = ? "
				+ " where soft_id = " + publisherBeanId.getSoft_id() ;
		
		
			transactionSupport.getJdbcTemplate().update(query, Long.valueOf(publisherBeanId.getSoft_id()),publisherBeanId.getStrSoftName(),publisherBeanId.strSoftDescription,publisherBeanId.getStrSoftVersion() ,
			    Double.valueOf(publisherBeanId.getStrSoftCost()),Long.valueOf(publisherBeanId.getStrCurrency()), Long.valueOf(publisherBeanId.getFile_id() ),Long.valueOf(authorizationPageBeanId.getCatalog_id()),
			    Long.valueOf(publisherBeanId.getImage_id()),Long.valueOf(publisherBeanId.getBigimage_id()),Long.valueOf(publisherBeanId.getSalelogic_id()),
			    Long.parseLong(publisherBeanId.getSite_id()),Long.valueOf(publisherBeanId.getProduct_code_id()),publisherBeanId.getStrSoftName().substring(0, 1),Long.valueOf(publisherBeanId.getType_id()),
			    Long.valueOf(publisherBeanId.getPortlettype_id()),Long.valueOf(publisherBeanId.getCreteria1_id()),Long.valueOf(publisherBeanId.getCreteria2_id()),Long.valueOf(publisherBeanId.getCreteria3_id()),
			    Long.valueOf(publisherBeanId.getCreteria4_id()),Long.valueOf(publisherBeanId.getCreteria5_id()),Long.valueOf(publisherBeanId.getCreteria6_id()),Long.valueOf(publisherBeanId.getCreteria7_id()),Long.valueOf(publisherBeanId.getCreteria8_id()),
			    Long.valueOf(publisherBeanId.getCreteria9_id()),Long.valueOf(publisherBeanId.getCreteria10_id()),Double.parseDouble(publisherBeanId.getAmount1()),Double.parseDouble(publisherBeanId.getAmount2()),
			    Double.parseDouble(publisherBeanId.getAmount3()),publisherBeanId.getStrSearch2(),publisherBeanId.getStrSoftName2(),Boolean.valueOf(publisherBeanId.getStrShow_ratimg1()),
			    Boolean.valueOf(publisherBeanId.getStrShow_ratimg2()),Boolean.valueOf(publisherBeanId.getStrShow_ratimg3()),Boolean.valueOf(publisherBeanId.getStrShow_forum()),
			    authorizationPageBeanId.getLang_id(),publisherBeanId.getJsp_url(),new java.util.Date());
			transactionSupport.commitTransaction(trxnstats);
		}
		catch (Exception ex) 
		{
			transactionSupport.rollbackTransaction(trxnstats);
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
			logger.exit();
		}
		
		publisherBeanId.setSoft_id("-1");
		return id;
	}

	
	final public String updateRowWithParent(final String tree_id , final PublisherBean publisherBeanId , final AuthorizationPageBean authorizationPageBeanId) throws UnsupportedEncodingException {
		logger.entry(tree_id,publisherBeanId,authorizationPageBeanId);
		TransactionStatus trxnstatus = transactionSupport.beginTransaction();
		String id = publisherBeanId.getSoft_id();
		String query = "" ;
		
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) return "";
		
		
		if(publisherBeanId.getStrSoftName2() != null && publisherBeanId.getStrSoftName2().length() > 0 )  
			publisherBeanId.setStrSearch2(publisherBeanId.getStrSoftName2().substring(0, 1));
	
		query = " update soft set soft_id = ?, " 
			+ " name = ?, "
			+ " description = ?, "
			+ " fulldescription = ?, "
			+ " version = ?, " 
			+ " cost = ?, " 
			+ " currency = ?, "
			+ " file_id = ?, " 
			+ " catalog_id = ?, " 
			+ " image_id = ?, "
			+ " bigimage_id = ?, " 
			+ " salelogic_id = ?, "
			+ " site_id = ?, " 
			+ " product_code = ?, " 
			+ " search = ?, "
			+ " type_id = ? , "
			+ " portlettype_id = ? , "
			+ " tree_id = ? , "
			+ " creteria1_id = ? , "
			+ " creteria2_id = ? , "
			+ " creteria3_id = ? , "
			+ " creteria4_id = ? , "
			+ " creteria5_id = ? , "
			+ " creteria6_id = ? , "
			+ " creteria7_id = ? , "
			+ " creteria8_id = ? , "
			+ " creteria9_id = ? , "
			+ " creteria10_id = ? , "
			+ " amount1 = ? , "
			+ " amount2 = ? , "
			+ " amount3 = ? , "
			+ " search2 = ? , "
			+ " name2 = ? , "
			+ " SHOW_RATING1 = ? , "
			+ " SHOW_RATING2 = ? , "
			+ " SHOW_RATING3 = ? , "
			+ " SHOW_BLOG = ? , "
			+ " lang_id = ? , "
			+ " jsp_url = ? , "
			+ " CDATE = ? "
			+ " where soft_id = " + publisherBeanId.getSoft_id() ;
	

		try 
		{
		  transactionSupport.getJdbcTemplate().update(query, Long.valueOf(publisherBeanId.getSoft_id()),publisherBeanId.getStrSoftName(),publisherBeanId.strSoftDescription,publisherBeanId.getStrSoftVersion() ,
              Double.valueOf(publisherBeanId.getStrSoftCost()),Long.valueOf(publisherBeanId.getStrCurrency()), Long.valueOf(publisherBeanId.getFile_id() ),Long.valueOf(authorizationPageBeanId.getCatalog_id()),
              Long.valueOf(publisherBeanId.getImage_id()),Long.valueOf(publisherBeanId.getBigimage_id()),Long.valueOf(publisherBeanId.getSalelogic_id()),
              Long.parseLong(publisherBeanId.getSite_id()),Long.valueOf(publisherBeanId.getProduct_code_id()),publisherBeanId.getStrSoftName().substring(0, 1),Long.valueOf(publisherBeanId.getType_id()),
              Long.valueOf(publisherBeanId.getPortlettype_id()),Long.valueOf(publisherBeanId.getCreteria1_id()),Long.valueOf(publisherBeanId.getCreteria2_id()),Long.valueOf(publisherBeanId.getCreteria3_id()),
              Long.valueOf(publisherBeanId.getCreteria4_id()),Long.valueOf(publisherBeanId.getCreteria5_id()),Long.valueOf(publisherBeanId.getCreteria6_id()),Long.valueOf(publisherBeanId.getCreteria7_id()),Long.valueOf(publisherBeanId.getCreteria8_id()),
              Long.valueOf(publisherBeanId.getCreteria9_id()),Long.valueOf(publisherBeanId.getCreteria10_id()),Double.parseDouble(publisherBeanId.getAmount1()),Double.parseDouble(publisherBeanId.getAmount2()),
              Double.parseDouble(publisherBeanId.getAmount3()),publisherBeanId.getStrSearch2(),publisherBeanId.getStrSoftName2(),Boolean.valueOf(publisherBeanId.getStrShow_ratimg1()),
              Boolean.valueOf(publisherBeanId.getStrShow_ratimg2()),Boolean.valueOf(publisherBeanId.getStrShow_ratimg3()),Boolean.valueOf(publisherBeanId.getStrShow_forum()),
              authorizationPageBeanId.getLang_id(),publisherBeanId.getJsp_url(),new java.util.Date());
			transactionSupport.commitTransaction(trxnstatus);
		}
		catch (Exception ex) 
		{
			transactionSupport.rollbackTransaction(trxnstatus);
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
			logger.exit();
		}

		publisherBeanId.setSoft_id("-1");
		return id;
	}

	
	final public String saveDescSoft(final PublisherBean publisherBeanId , final AuthorizationPageBean authorizationPageBeanId) throws UnsupportedEncodingException {
		logger.entry(publisherBeanId,authorizationPageBeanId);
		if(publisherBeanId.getStrSoftName2() != null && publisherBeanId.getStrSoftName2().length() > 0 )  
			publisherBeanId.setStrSearch2(publisherBeanId.getStrSoftName2().substring(0, 1));
		
		TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		String strID = null;
		String query = "";
		try 
		{
		strID = transactionSupport.getJdbcTemplate().queryForObject(soft_sequence_rs,String.class);
        query = "insert into soft ( soft_id , " + " name , " + " description , "
            + " fulldescription , " + " version , " + " cost , " + " currency , " + " file_id , "
            + " catalog_id , " + " active , " + " licence_id  , " + " image_id , "
            + " bigimage_id , " + " user_id , " + " salelogic_id ," + " site_id , "
            + " product_code , " + " search,  " + " portlettype_id  ,  " + " type_id  ,  "
            + " creteria1_id ,  " + " creteria2_id ,  " + " creteria3_id ,  " + " creteria4_id ,  "
            + " creteria5_id ,  " + " creteria6_id ,  " + " creteria7_id ,  " + " creteria8_id ,  "
            + " creteria9_id ,  " + " creteria10_id , " + " show_rating1 ,  " + " show_rating2 ,  "
            + " show_rating3 ,  " + " show_blog ,  " + " search2 ,  " + " amount1 , "
            + " amount2 , " + " amount3 , " + " name2 ," + " lang_id ," + " jsp_url  ) "
            + " VALUES ( ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? ,  " + " ? ,  "
            + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  "
            + " ? ,  " + " ? ,  " + " ? , " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? , "
            + " ?  )";
			String type_id;
			if(authorizationPageBeanId.getCatalog_id().equals("-2")) {
			  type_id =  "3";
			}
			else {
			  type_id = publisherBeanId.getType_id();
			}

		
			transactionSupport.getJdbcTemplate().update(query, Long.valueOf(strID),publisherBeanId.getStrSoftName(),publisherBeanId.strSoftDescription,publisherBeanId.product_fulldescription ,
			    publisherBeanId.getStrSoftVersion(),Double.valueOf(publisherBeanId.getStrSoftCost()), Long.valueOf(publisherBeanId.getStrCurrency()),Long.valueOf(publisherBeanId.getFile_id()),
			    Long.valueOf(authorizationPageBeanId.getCatalog_id()),true,Long.valueOf(publisherBeanId.getLicence_id()),Long.valueOf(publisherBeanId.getImage_id()),Long.valueOf(publisherBeanId.getBigimage_id()),
			    Long.valueOf(publisherBeanId.getUser_id()),Long.valueOf(publisherBeanId.getSalelogic_id()),Long.valueOf(authorizationPageBeanId.getSite_id()),Long.valueOf(publisherBeanId.getProduct_code_id()),
			    publisherBeanId.getStrSoftName().substring(0, 1),Long.valueOf(publisherBeanId.getPortlettype_id()),Long.valueOf(type_id),Long.valueOf(publisherBeanId.getCreteria1_id()),
			    Long.valueOf(publisherBeanId.getCreteria2_id()),Long.valueOf(publisherBeanId.getCreteria3_id()),Long.valueOf(publisherBeanId.getCreteria4_id()),Long.valueOf(publisherBeanId.getCreteria5_id()),
			    Long.valueOf(publisherBeanId.getCreteria6_id()),Long.valueOf(publisherBeanId.getCreteria7_id()),Long.valueOf(publisherBeanId.getCreteria8_id()),Long.valueOf(publisherBeanId.getCreteria9_id()),Long.valueOf(publisherBeanId.getCreteria10_id()),
			    Boolean.valueOf(publisherBeanId.getStrShow_ratimg1()),Boolean.valueOf(publisherBeanId.getStrShow_ratimg2()),Boolean.valueOf(publisherBeanId.getStrShow_ratimg3()),Boolean.valueOf(publisherBeanId.getStrShow_forum()),
			    Double.valueOf(publisherBeanId.getAmount1()),Double.valueOf(publisherBeanId.getAmount2()),Double.valueOf(publisherBeanId.getAmount3()),publisherBeanId.getStrSoftName2(),authorizationPageBeanId.getLang_id(),publisherBeanId.getStrSearch2(),publisherBeanId.getJsp_url());
			transactionSupport.commitTransaction(trxnStatus);
		}
		catch (Exception ex) 
		{
			transactionSupport.rollbackTransaction(trxnStatus);
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
			logger.exit();
		}


		return strID;
	}

    final public String insertRowWithParent(final String tree_id,
        final PublisherBean publisherBeanId, final AuthorizationPageBean authorizationPageBeanId)
        throws UnsupportedEncodingException {

      logger.entry(publisherBeanId, authorizationPageBeanId, tree_id);
      TransactionStatus trxn = transactionSupport.beginTransaction();
      String strID = "";
      // String query = "SELECT NEXT VALUE FOR soft_id_seq AS ID FROM ONE_SEQUENCES";
      String query = soft_sequence_rs;
      try {
        strID = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

        query = "insert into soft ( soft_id , " + " name , " + " description , "
            + " fulldescription , " + " version , " + " cost , " + " currency , " + " file_id , "
            + " catalog_id , " + " active , " + " licence_id  , " + " image_id , "
            + " bigimage_id , " + " user_id , " + " salelogic_id ," + " site_id , "
            + " product_code , " + " search,  " + " portlettype_id  ,  " + " type_id  ,  "
            + " tree_id  ,  " + " creteria1_id ,  " + " creteria2_id ,  " + " creteria3_id ,  "
            + " creteria4_id ,  " + " creteria5_id ,  " + " creteria6_id ,  " + " creteria7_id ,  "
            + " creteria8_id ,  " + " creteria9_id ,  " + " creteria10_id , " + " show_rating1 ,  "
            + " show_rating2 ,  " + " show_rating3 ,  " + " show_blog ,  " + " search2 ,  "
            + " amount1 , " + " amount2 , " + " amount3 , " + " name2 ," + " lang_id ,"
            + " jsp_url  ) " + " VALUES ( ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  "
            + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? , " + " ? ,  " + " ? ,  "
            + " ? ,  " + " ? , " + " ? , " + " ?  )";

        transactionSupport.getJdbcTemplate().update(query, Long.valueOf(strID), publisherBeanId.getStrSoftName(),
            publisherBeanId.strSoftDescription, publisherBeanId.product_fulldescription,
            publisherBeanId.getStrSoftVersion(), Double.valueOf(publisherBeanId.getStrSoftCost()),
            Long.valueOf(publisherBeanId.getStrCurrency()),
            Long.valueOf(publisherBeanId.getFile_id()),
            Long.valueOf(authorizationPageBeanId.getCatalog_id()), true,
            Long.valueOf(publisherBeanId.getLicence_id()),
            Long.valueOf(publisherBeanId.getImage_id()),
            Long.valueOf(publisherBeanId.getBigimage_id()),
            Long.valueOf(publisherBeanId.getUser_id()),
            Long.valueOf(publisherBeanId.getSalelogic_id()),
            Long.valueOf(publisherBeanId.getSite_id()),
            Long.valueOf(publisherBeanId.getProduct_code_id()),
            publisherBeanId.getStrSoftName().substring(0, 1),
            Long.valueOf(publisherBeanId.getPortlettype_id()),
            Long.valueOf(publisherBeanId.getType_id()), Long.valueOf(tree_id),
            Long.valueOf(publisherBeanId.getCreteria1_id()),
            Long.valueOf(publisherBeanId.getCreteria2_id()),
            Long.valueOf(publisherBeanId.getCreteria3_id()),
            Long.valueOf(publisherBeanId.getCreteria4_id()),
            Long.valueOf(publisherBeanId.getCreteria5_id()),
            Long.valueOf(publisherBeanId.getCreteria6_id()),
            Long.valueOf(publisherBeanId.getCreteria7_id()),
            Long.valueOf(publisherBeanId.getCreteria8_id()),
            Long.valueOf(publisherBeanId.getCreteria9_id()),
            Long.valueOf(publisherBeanId.getCreteria10_id()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg1()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg2()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg3()),
            Boolean.valueOf(publisherBeanId.getStrShow_forum()), publisherBeanId.getStrSearch2(),
            Double.valueOf(publisherBeanId.getAmount1()),
            Double.valueOf(publisherBeanId.getAmount2()),
            Double.valueOf(publisherBeanId.getAmount3()), publisherBeanId.getStrSoftName2(),
            authorizationPageBeanId.getLang_id(), publisherBeanId.getJsp_url());

        transactionSupport.commitTransaction(trxn);
      } catch (Exception ex) {
        transactionSupport.rollbackTransaction(trxn);;
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }


      return strID;
    }

	
	
	
	
	
	
	/**
	 *  Save for aprouch administrator
	 * 
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	
	final public String saveInformationWithCheck(final PublisherBean publisherBeanId, final AuthorizationPageBean authorizationPageBeanId) throws UnsupportedEncodingException {

		logger.entry(publisherBeanId,authorizationPageBeanId);
		TransactionStatus trxn = transactionSupport.beginTransaction();
		String strID= "";
		//String query = "SELECT NEXT VALUE FOR soft_id_seq  AS ID  FROM ONE_SEQUENCES";
		String query = soft_sequence_rs;
		try 
		{
		strID =transactionSupport.getJdbcTemplate().queryForObject(query,String.class);
        query = "insert into soft ( soft_id , " + " name , " + " description , "
            + " fulldescription , " + " version , " + " cost , " + " currency , " + " file_id , "
            + " catalog_id , " + " active , " + " licence_id  , " + " image_id , "
            + " bigimage_id , " + " user_id , " + " salelogic_id ," + " site_id , "
            + " product_code , " + " search,  " + " portlettype_id  ,  " + " tree_id  ,  "
            + " creteria1_id ,  " + " creteria2_id ,  " + " creteria3_id ,  " + " creteria4_id ,  "
            + " creteria5_id ,  " + " creteria6_id ,  " + " creteria7_id ,  " + " creteria8_id ,  "
            + " creteria9_id ,  " + " creteria10_id , " + " show_rating1 ,  " + " show_rating2 ,  "
            + " show_rating3 ,  " + " show_blog ,  " + " search2 ,  " + " amount1 , "
            + " amount2 , " + " amount3 , " + " name2 ," + " lang_id ," + " jsp_url  ) "
            + " VALUES ( ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? , "
            + " ? , " + " ? , " + " ? , " + " ? , " + " ? , " + " ? ,  " + " ? ,  " + " ? ,  "
            + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  " + " ? ,  "
            + " ? ,  " + " ? ,  " + " ? , " + " ? ,  " + " ? ,  " + " ? ,  " + " ? , " + " ? , "
            + " ?  )";
		
		transactionSupport.getJdbcTemplate().update(query, Long.valueOf(strID), publisherBeanId.getStrSoftName(),
            publisherBeanId.strSoftDescription, publisherBeanId.product_fulldescription,
            publisherBeanId.getStrSoftVersion(), Double.valueOf(publisherBeanId.getStrSoftCost()),
            Long.valueOf(publisherBeanId.getStrCurrency()),
            Long.valueOf(publisherBeanId.getFile_id()),
            Long.valueOf(authorizationPageBeanId.getCatalog_id()), true,
            Long.valueOf(publisherBeanId.getLicence_id()),
            Long.valueOf(publisherBeanId.getImage_id()),
            Long.valueOf(publisherBeanId.getBigimage_id()),
            Long.valueOf(authorizationPageBeanId.getIntUserID()),
            Long.valueOf(publisherBeanId.getSalelogic_id()),
            Long.valueOf(publisherBeanId.getSite_id()),
            Long.valueOf(publisherBeanId.getProduct_code_id()),
            publisherBeanId.getStrSoftName().substring(0, 1),
            Long.valueOf(publisherBeanId.getPortlettype_id()),
            Long.valueOf(PostType.FOR_APROVE ),
            Long.valueOf(publisherBeanId.getCreteria1_id()),
            Long.valueOf(publisherBeanId.getCreteria2_id()),
            Long.valueOf(publisherBeanId.getCreteria3_id()),
            Long.valueOf(publisherBeanId.getCreteria4_id()),
            Long.valueOf(publisherBeanId.getCreteria5_id()),
            Long.valueOf(publisherBeanId.getCreteria6_id()),
            Long.valueOf(publisherBeanId.getCreteria7_id()),
            Long.valueOf(publisherBeanId.getCreteria8_id()),
            Long.valueOf(publisherBeanId.getCreteria9_id()),
            Long.valueOf(publisherBeanId.getCreteria10_id()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg1()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg2()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg3()),
            Boolean.valueOf(publisherBeanId.getStrShow_forum()), publisherBeanId.getStrSearch2(),
            Double.valueOf(publisherBeanId.getAmount1()),
            Double.valueOf(publisherBeanId.getAmount2()),
            Double.valueOf(publisherBeanId.getAmount3()), publisherBeanId.getStrSoftName2(),
            authorizationPageBeanId.getLang_id(), publisherBeanId.getJsp_url());

		transactionSupport.commitTransaction(trxn);

		}
		catch (Exception ex) 
		{
			transactionSupport.rollbackTransaction(trxn);
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
			logger.exit();
		}
		
		return strID;
	}
	

    final public String updateInformationWithCheck(final PublisherBean publisherBeanId,
        final AuthorizationPageBean authorizationPageBeanId) throws UnsupportedEncodingException {
      logger.entry(authorizationPageBeanId, publisherBeanId);
      TransactionStatus trxn = transactionSupport.beginTransaction();
      String id = publisherBeanId.getSoft_id();
      String query = "";
      if (publisherBeanId.getSoft_id().compareTo("-1") == 0)
        return "";

      if (publisherBeanId.getStrSoftName2() != null
          && publisherBeanId.getStrSoftName2().length() > 0)
        publisherBeanId.setStrSearch2(publisherBeanId.getStrSoftName2().substring(0, 1));


      query = " update soft set soft_id = ?, " + " name = ?, " + " description = ?, "
          + " fulldescription = ?, " + " version = ?, " + " cost = ?, " + " currency = ?, "
          + " file_id = ?, " + " catalog_id = ?, " + " image_id = ?, " + " bigimage_id = ?, "
          + " salelogic_id = ?, " + " site_id = ?, " + " product_code = ?, " + " search = ?, "
          + " type_id = ? , " + " portlettype_id = ? , " + " creteria1_id = ? , "
          + " creteria2_id = ? , " + " creteria3_id = ? , " + " creteria4_id = ? , "
          + " creteria5_id = ? , " + " creteria6_id = ? , " + " creteria7_id = ? , "
          + " creteria8_id = ? , " + " creteria9_id = ? , " + " creteria10_id = ? , "
          + " amount1 = ? , " + " amount2 = ? , " + " amount3 = ? , " + " search2 = ? , "
          + " name2 = ? , " + " SHOW_RATING1 = ? , " + " SHOW_RATING2 = ? , "
          + " SHOW_RATING3 = ? , " + " SHOW_BLOG = ? , " + " lang_id = ? , " + " jsp_url = ? , "
          + " CDATE = ? " + " where soft_id = " + publisherBeanId.getSoft_id();


      try {
        transactionSupport.getJdbcTemplate().update(query, Long.valueOf(publisherBeanId.getSoft_id()),
            publisherBeanId.getStrSoftName(), publisherBeanId.strSoftDescription,
            publisherBeanId.product_fulldescription, publisherBeanId.getStrSoftVersion(),
            Double.valueOf(publisherBeanId.getStrSoftCost()),
            Long.valueOf(publisherBeanId.getStrCurrency()),
            Long.valueOf(publisherBeanId.getFile_id()),
            Long.valueOf(authorizationPageBeanId.getCatalog_id()),
            Long.valueOf(publisherBeanId.getImage_id()),
            Long.valueOf(publisherBeanId.getBigimage_id()),
            Long.valueOf(publisherBeanId.getSalelogic_id()),
            Long.valueOf(publisherBeanId.getSite_id()),
            Long.valueOf(publisherBeanId.getProduct_code_id()),
            publisherBeanId.getStrSoftName().substring(0, 1), PostType.FOR_APROVE,
            Long.valueOf(publisherBeanId.getPortlettype_id()),
            Long.valueOf(publisherBeanId.getCreteria1_id()),
            Long.valueOf(publisherBeanId.getCreteria2_id()),
            Long.valueOf(publisherBeanId.getCreteria3_id()),
            Long.valueOf(publisherBeanId.getCreteria4_id()),
            Long.valueOf(publisherBeanId.getCreteria5_id()),
            Long.valueOf(publisherBeanId.getCreteria6_id()),
            Long.valueOf(publisherBeanId.getCreteria7_id()),
            Long.valueOf(publisherBeanId.getCreteria8_id()),
            Long.valueOf(publisherBeanId.getCreteria9_id()),
            Long.valueOf(publisherBeanId.getCreteria10_id()),
            Double.valueOf(publisherBeanId.getAmount1()),
            Double.valueOf(publisherBeanId.getAmount2()),
            Double.valueOf(publisherBeanId.getAmount3()), publisherBeanId.getStrSearch2(),
            publisherBeanId.getStrSoftName2(),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg1()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg2()),
            Boolean.valueOf(publisherBeanId.getStrShow_ratimg3()),
            Boolean.valueOf(publisherBeanId.getStrShow_forum()),
            authorizationPageBeanId.getLang_id(), publisherBeanId.getJsp_url(),
            new java.util.Date());
        transactionSupport.commitTransaction(trxn);
      } catch (Exception ex) {
        transactionSupport.rollbackTransaction(trxn);
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();;
      }


      publisherBeanId.setSoft_id("-1");
      return id;
    }
	

	
	final public void setFileNameByFile_ID(final String File_ID , final PublisherBean publisherBeanId) {
	  logger.entry(File_ID,publisherBeanId);
	  String query = "select name  from file  where  file_id  = " + File_ID;
		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if( rows.size() > 0 ) publisherBeanId.setFilename( (String)rows.get(0).get("name"));
			else publisherBeanId.setFilename("") ;
			
		}
	    catch (Exception ex) 
	    {
	 	  logger.error(ex.getLocalizedMessage(),ex);
	    }
	    finally
	    {
		logger.exit();    	
	    }

	}

	final public void setImageNameByImage_ID(final String Image_id , final PublisherBean publisherBeanId) {

		logger.entry(Image_id,publisherBeanId);
		String query = "SELECT imgname FROM images WHERE image_id = "+ Image_id;

		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() > 0)publisherBeanId.setImgname((String)rows.get(0).get("imgname"));
		}
		 catch (Exception ex) 
		{
			 logger.error(ex.getLocalizedMessage(),ex);
		}
		finally
		{
		logger.exit();    	
		}

	}

    final public void setBigImageNameByImage_ID(final String Image_id,
        final PublisherBean publisherBeanId) {

      logger.entry(Image_id, publisherBeanId);
      String query = "SELECT imgname FROM big_images WHERE big_images_id = " + Image_id;
      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        if (rows.size() > 0)
          publisherBeanId.setBigimgname((String) rows.get(0).get("imgname"));

      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }
    }


	
	//TODO Need to Check with Darko How to Transform this Classes For restFul Resources
    final public String getComboBoxWithJavaScriptBigImage(final String name, String selected_cd,
        final String query, final String javascript_statment, final PublisherBean publisherBeanId) {
      logger.entry(name, selected_cd, query, javascript_statment, publisherBeanId);
      String strCD = "0";
      String strLable = "Other";
      if (selected_cd == null)
        selected_cd = "";
      StringBuffer table = new StringBuffer();

      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        // onChange=\"return document.forms[0].submit()\"
        table.append("<select name=\"" + name + "\"   id=\"" + name + "\"    " + javascript_statment
            + "   > \n");
        if (rows.size() > 0) {
          strCD = (String) rows.get(0).get(name);
          strLable = (String) rows.get(0).get(selected_cd);
          publisherBeanId.setBigimage_id(strCD);
          publisherBeanId.setBigimgname(strLable);
        }
        for (int i = 0; rows.size() > i; i++) {
          // if(i==0) if(((String)selected_cd).length()==0 ) selected_cd =
          // (String)Adp.getValueAt(i,0) ;
          strCD = (String) rows.get(i).get(name);
          strLable = (String) rows.get(i).get(selected_cd);
          if (selected_cd.compareTo(strCD) == 0) {
            table.append("<option value=\"" + strCD + "\" selected >" + strLable + "\n");
            publisherBeanId.setBigimage_id(strCD);
            publisherBeanId.setBigimgname(strLable);
          } else
            table.append("<option value=\"" + strCD + "\">" + strLable + "\n");
        }
        table.append("</select> \n");
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }
      return table.toString();
    }
	
	
	//TODO Need to Check with Darko How to Transform this Classes For restFul Resources
    final public String getComboBoxWithJavaScriptSmallImage(final String name, String selected_cd,
        final String query, final String javascript_statment, final PublisherBean publisherBeanId) {
      logger.entry(name, selected_cd, query, javascript_statment, publisherBeanId);
      String strCD = "0";
      String strLable = "Other";
      if (selected_cd == null)
        selected_cd = "";

      StringBuffer table = new StringBuffer();

      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        // onChange=\"return document.forms[0].submit()\"
        table.append("<select name=\"" + name + "\"   id=\"" + name + "\"    " + javascript_statment
            + "   > \n");
        if (rows.size() > 0) {
          strCD = (String) rows.get(0).get(name);
          strLable = (String) rows.get(0).get(selected_cd);
          publisherBeanId.setImage_id(strCD);
          publisherBeanId.setImgname(strLable);
        }
        for (int i = 0; rows.size() > i; i++) {

          strCD = (String) rows.get(i).get(name);
          strLable = (String) rows.get(i).get(selected_cd);
          if (selected_cd.compareTo(strCD) == 0) {
            table.append("<option value=\"" + strCD + "\" selected >" + strLable + "\n");
            publisherBeanId.setImage_id(strCD);
            publisherBeanId.setImgname(strLable);
          } else
            table.append("<option value=\"" + strCD + "\">" + strLable + "\n");
        }
        table.append("</select> \n");
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }
      return table.toString();
    }
	
	
	
	
	
	
	
	

	final public String getComboBoxAutoSubmitLocale(final String name,  String selected_cd , final String defaultLabel ,  final String query) 
	{
	    logger.entry(selected_cd,defaultLabel,query,name);
		String strCD = "0";
		String strLable = "Other";
		if (selected_cd == null) selected_cd = "";

		StringBuffer table = new StringBuffer();

		
		try 
		{

		List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);

		table.append("<select name=\"" + name+ "\"   id=\"" + name+ "\"    onChange=\"doChangeCreteria('"+ name + "', this.value)\"    > \n");
		for (int i = 0; rows.size() > i; i++) 
		{

			strCD = (String) rows.get(i).get(name);
			strLable = (String) rows.get(i).get("name");
			if( strCD.equals("0") ) strLable = defaultLabel ;
			
			
			if (selected_cd.compareTo(strCD) == 0)
				
				table.append("<option value=\"" + strCD + "\" selected >"	+ strLable + "\n");
			else
				
				table.append("<option value=\"" + strCD + "\">" + strLable	+ "\n");
		}
		
		table.append("</select> \n");
	}
	catch (Exception ex) 
	{
		logger.error(ex.getLocalizedMessage(),ex);
	}
	finally
	{
	logger.exit();
	}
		return table.toString();
	}


	final public String getOneLabel(final String query) {
		String name = "";
		
		
		try 
		{
			List<Map<String,Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() != 0)	name = (String) rows.get(0).get("label");
		}
		catch (Exception ex) 
		{
			logger.error(ex
			    .getLocalizedMessage(),ex);
		}
		finally
		{
		logger.exit();
		}
		return name ;
	}
	
	
	
	final public boolean isLimmitPostedMessages(final AuthorizationPageBean authorizationBean, final boolean updateStatus) 
	{
	    logger.entry(authorizationBean,updateStatus);
	    Calendar calendar = Calendar.getInstance() ;
		boolean rezult = false ;
		int limmitPostedMessages = 0 ;
		String state_limmit = "" ;
		String state_day = "" ; 
		String limmit = limmit_posted_messages_sequence_rs;
		if(isNumber(limmit)) limmitPostedMessages = Integer.parseInt(limmit);
		else return false ;
		TransactionStatus transactionStatus = transactionSupport.beginTransaction();
		String query = "select state  from tuser where site_id = " + authorizationBean.getSite_id();
			try {
			  List<Map<String,Object>> rows =transactionSupport.getJdbcTemplate().queryForList(query);
			if (rows.size() != 0) 
			{
				String state_field = (String)rows.get(0).get("state");
				String[] arrey  = state_field.split("_") ;
				if(state_field.equals("") || arrey.length != 2)
				{
					state_day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
					state_limmit = "0" ;
				} 
				else 
				{
					state_limmit = arrey[0] ;	
					state_day = arrey[1] ;	
				} 
				
				if(state_day.compareTo(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)))!=0)
				{
					state_limmit = "0" ;
					state_day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
				}
				
				if(isNumber( state_limmit))	authorizationBean.setNumberPostedMessages(Integer.parseInt(state_limmit)+1); // + " " +
				if(limmitPostedMessages < authorizationBean.getNumberPostedMessages() ) rezult =  true ;

				
			}
			
			if(updateStatus)
			{
			query = "update tuser set  state = ?  where site_id = " + authorizationBean.getSite_id();
			transactionSupport.getJdbcTemplate().update(query, authorizationBean.getNumberPostedMessages().toString().concat("_").concat(state_day));
			
			}
			transactionSupport.commitTransaction(transactionStatus);

		}
		catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(),ex);
			transactionSupport.rollbackTransaction(transactionStatus);
		}

		finally {
			logger.exit();
		}
	return rezult ;
	}
	
	
	public long saveSmallImgURL(String fileName, long user_id) {
		logger.entry(fileName,user_id);
		TransactionStatus trxnStatus = transactionSupport.beginTransaction();
		String strID = null;
		String query = images_sequence_rs;
		try 
		{
		strID = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
		query = "insert into images ( image_id ,  imgname , img_url ,  user_id ) VALUES " 
			+ "( ? ,  ? , ? ,  ? )";
			transactionSupport.getJdbcTemplate().update(query, Long.valueOf(strID) ,fileName,"imgpositions/" + strID + fileName.substring(fileName.lastIndexOf(".")) ,Long.valueOf(user_id));
			transactionSupport.commitTransaction(trxnStatus);
		}
		catch (Exception ex) 
		{
				logger.error(ex.getLocalizedMessage(),ex);
				transactionSupport.rollbackTransaction(trxnStatus);
		}
		finally
		{
				logger.exit();
		}
			
		return Long.parseLong(strID);
	}
	
	public String deleteSmallImgURL(long image_id) {
		
		logger.entry(image_id);
		String path = "" ;
		TransactionStatus trxnStatus = transactionSupport.beginTransaction();
		String query = "select img_url from images where image_id = " + image_id ;
		
		try 
		{
		
		path = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
		query = "delete from images where image_id = " + image_id ;
		transactionSupport.getJdbcTemplate().update(query);
		transactionSupport.commitTransaction(trxnStatus);
		}
		catch (Exception ex) 
		{
				logger.error(ex.getLocalizedMessage());
				transactionSupport.rollbackTransaction(trxnStatus);
		}
		finally
		{
				logger.exit();
		}
			
		return path ;
	}
	
    public long saveBigImgURL(String fileName, long user_id) {
      logger.entry(fileName, user_id);;
      TransactionStatus trxn = transactionSupport.beginTransaction();
      String strID = null;
      String query = big_images_sequence_rs;
      try {

        strID = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
        query = "insert into big_images ( big_images_id , imgname ,  img_url ,  user_id  )"
            + " VALUES ( ?, ?, ? , ? )";
        transactionSupport.getJdbcTemplate().update(query, Long.valueOf(strID), fileName,
            "big_imgpositions/" + strID + fileName.substring(fileName.lastIndexOf(".")),
            Long.valueOf(user_id));
        transactionSupport.commitTransaction(trxn);;
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage());
        transactionSupport.rollbackTransaction(trxn);;
      } finally {
        logger.exit();;
      }

      return Long.parseLong(strID);
    }

    public String deleteBigImgURL(long big_images_id) {
      logger.entry(big_images_id);
      String path = "";
      TransactionStatus trxnstatus = transactionSupport.beginTransaction();
      String query = "select img_url from big_images where big_images_id = " + big_images_id;

      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        path = (String) rows.get(0).get("img_url");
        query = "delete from big_images where big_images_id = " + big_images_id;
        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(trxnstatus);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage());
        transactionSupport.rollbackTransaction(trxnstatus);
      } finally {
        logger.exit();
      }

      return path;
    }
	
    public long saveFileURL(String fileName, long user_id) {
      logger.entry(fileName, user_id);
      TransactionStatus trxnStatus = transactionSupport.beginTransaction();
      String strID = "-1";
      String path;
      String query = file_sequence_rs;
      try {
        strID = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

        path = this.getClass().getResource("").getPath();
        path = path.substring(0, path.indexOf("/WEB-INF/"));
        path =
            path.substring(1) + "/files/" + strID + fileName.substring(fileName.lastIndexOf("."));

        query = "insert into file " + "(" + " file_id , " + " name , " + " path , " + " user_id "
            + ")" + " VALUES " + "( " + strID + ", '" + fileName + "', '" + path + "' , " + user_id
            + " )";

        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(trxnStatus);
      } catch (Exception ex) {
        transactionSupport.rollbackTransaction(trxnStatus);
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }

      return Long.parseLong(strID);
    }
	
	
    public String deleteFileURL(long file_id) {
      logger.entry(file_id);
      String path = "";
      TransactionStatus trxnStatus = transactionSupport.beginTransaction();
      String query = "select path from file where file_id = " + file_id;

      try {
        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);
        path = (String) rows.get(0).get("path");
        query = "delete from file where file_id = " + file_id;
        transactionSupport.getJdbcTemplate().update(query);
        transactionSupport.commitTransaction(trxnStatus);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage());
        transactionSupport.rollbackTransaction(trxnStatus);
      } finally {
        logger.exit();
      }

      return path;
    }
	
	
    public String getNavigator(AuthorizationPageBean authorizationPageBeanId, int offset) {
      logger.entry(authorizationPageBeanId, offset);
      boolean folder = false;
      String id = "";
      String name = "";
      StringBuffer table = new StringBuffer();



      String query = "";

      query = "select catalog_id ,  lable   FROM catalog WHERE active = true and lang_id = "
          + authorizationPageBeanId.getLang_id() + " and site_id = "
          + authorizationPageBeanId.getSite_id() + " and parent_id = "
          + authorizationPageBeanId.getCatalog_parent_id() + " limit 50 offset " + offset;

      try {

        List<Map<String, Object>> rows = transactionSupport.getJdbcTemplate().queryForList(query);

        for (int i = 0; rows.size() > i; i++) {
          id = (String) rows.get(0).get("catalog_id");
          name = (String) rows.get(0).get("lable");
          folder = isFolder(id);

        }


      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      } finally {
        logger.exit();
      }

      return table.toString();
    }
	

   public boolean isFolder(String parent_id) 
   {
	String query = "";
	long count = 0 ;
	try 
	{
		
		query = "select count (catalog_id) as number  FROM catalog WHERE  parent_id  = " + parent_id  ;
		count = transactionSupport.getJdbcTemplate().queryForObject(query, Long.class);
	}
	catch (Exception ex) 
	{
		logger.error(ex.getLocalizedMessage()) ;
	}
	finally
	{
		logger.exit();		
	}

return count == 0 ? false:true ;
}
	


public String getCatalogUrlPath(AuthorizationPageBean authorizationPageBeanId) 
{
	//if( localization == null )  localization = PropertyResourceBundle.getBundle("localization", locale);
    logger.entry(authorizationPageBeanId);
    String query = "";
	long  _parent_id = 0 ;
	long parent_id_last = 0 ;
	String lable = "" ;
	//String lable_last = authorizationPageBeanId.getLocalization(applicationContext).getString("section_of_list_catalog")  ;
	String lable_last = "" ;
	boolean doWhile = true ;
	StringBuffer path = new StringBuffer();
	if( authorizationPageBeanId.getCatalog_id().length() > 0 ) _parent_id =  Long.parseLong(authorizationPageBeanId.getCatalog_parent_id());
    if(_parent_id ==  0 ) return lable_last ;
    String item = "" ;
	
	try 
	{
		
		while(doWhile)
		{
		query = "select parent_id , lable   FROM catalog WHERE  catalog_id  = " + _parent_id  ;
		List<Map<String,Object>> rows =  transactionSupport.getJdbcTemplate().queryForList(query);
			if ( rows.size() > 0) 
			{
			    if( ((String) rows.get(0).get("parent_id")).length() > 0 ) parent_id_last =  Long.parseLong((String) rows.get(0).get("parent_id"));
			    lable = (String) rows.get(0).get("lable");
			    doWhile = true ;
			    //path.insert(0,"/" + lable) ;
			    item = "<a href=\"ProductPostCre.jsp?parent_id=" + _parent_id +"\" >"+"/" + lable + "</a>" ;
			    path.insert(0,item) ;
			    _parent_id = parent_id_last ;
			    
			}
			else doWhile = false ;
			//Adp.commit();
		}
	
	    item = "<a href=\"ProductPostCre.jsp?parent_id=" + parent_id_last +"\" >"+"/" + lable_last + "</a>" ;
	    path.insert(0,item) ;
	
	}
	catch (Exception ex) 
	{
		logger.error(ex.getLocalizedMessage(),ex) ;
	}
	finally
	{
		logger.exit();
	}

return path.toString() ;
}

}
