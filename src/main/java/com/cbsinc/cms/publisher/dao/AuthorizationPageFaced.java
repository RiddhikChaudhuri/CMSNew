package com.cbsinc.cms.publisher.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import com.cbsinc.cms.publisher.controllers.SiteType;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CreateShopBean;
import com.cbsinc.cms.publisher.models.GetValueTool;
import com.cbsinc.cms.publisher.models.TUserDto;
import com.cbsinc.cms.services.tomcat.AddAliase;
import com.cbsinc.cms.services.tomcat.DomainRegister;

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
@PropertySource("classpath:sequence.properties")
public class AuthorizationPageFaced extends WebControls implements java.io.Serializable { 
	

  @Autowired
  private TransactionSupport newTransactionSupport;

  private static final long serialVersionUID = -6909056318575957347L;

  final CreateShopBean createShopBean = new CreateShopBean();
  // sequences_rs = PropertyResourceBundle.getBundle("sequence");
  transient final private ResourceBundle session_scope =
      PropertyResourceBundle.getBundle("session_scope");

  private static XLogger logger = XLoggerFactory.getXLogger(AuthorizationPageFaced.class.getName());
   ConcurrentHashMap controllerMap = new ConcurrentHashMap(1024);
   ConcurrentHashMap transformerMap = new ConcurrentHashMap(1024);
  transient ResourceBundle actions_resources =
      PropertyResourceBundle.getBundle("web_actions");
  transient  ResourceBundle xslt_resources = PropertyResourceBundle.getBundle("web_xslt");
  transient  private ResourceBundle setup_resources =
      PropertyResourceBundle.getBundle("SetupApplicationResources");


  final Document doc = null;


	

	  @Value("${tuser}")
	  private String tuser_sequence_rs;
	  
	  
	  @Value("${account}")
      private String account_sequence_rs;

	//final static private Logger log = Logger.getLogger(AuthorizationPageFaced.class);
	   
	
	//Loader logger = LoggerFactory.getLogger(AuthorizationPageFaced.class);

	public String getCokieSessionId(final HttpServletRequest request, final HttpServletResponse response )
	{
	    logger.info("Cookies Request:"+request.getCookies());
		Cookie[] cookies =  request.getCookies();
		if(cookies != null)
		{
	 	 for(int i = 0 ; i < cookies.length ; i++ )
		  {
			if(cookies[i].getName().equals("session_id"))
			{
				return cookies[i].getValue() ;
			}
		  }
		}
		HttpSession httpSession =  request.getSession() ;
		response.addCookie(new Cookie("session_id",httpSession.getId()));
		return httpSession.getId() ;
	}

	public boolean isCokieSessionIdExists(final HttpServletRequest request, final HttpServletResponse response )
	{
		boolean exists = false ;
		Cookie[] cookies =  request.getCookies();
		if(cookies != null)
		{
	 	 for(int i = 0 ; i < cookies.length ; i++ )
		  {
			if(cookies[i].getName().equals("session_id"))
			{
				return true ;
			}
		  }
		}

		return exists ;
	}
	
	public boolean isLoginCorrect(final String i_strLogin, final String i_strPasswd , final AuthorizationPageBean authorizationBean, String idsession) {
      logger.entry(i_strLogin,i_strPasswd,authorizationBean,idsession);
     // TransactionStatus transactionStatus =  newTransactionSupport.beginTransaction();
      String query ="";
      try {
          if (i_strLogin == null || i_strLogin.length() == 0) return false;
          if (i_strPasswd == null || i_strPasswd.length() == 0)   return false;
          
          
          
          query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  FROM tuser  where  login = '"
                  + i_strLogin + "'   and  passwd = '" + i_strPasswd + "' and  site_id = " + authorizationBean.getSite_id() ;
          List<Map<String, Object>> rows = newTransactionSupport.getJdbcTemplate().queryForList(query);
          if (rows.size() == 1) {
            for(Map row : rows) {
              authorizationBean.setIntUserID(  Long.parseLong(String.valueOf(row.get("user_id"))));
              authorizationBean.setStrLogin(String.valueOf(row.get("login")));
              authorizationBean.setStrPasswd (String.valueOf(row.get("passwd")));
              authorizationBean.setStrFirstName(String.valueOf(row.get("first_name")));
              authorizationBean.setStrLastName(String.valueOf(row.get("last_name")));
              authorizationBean.setStrEMail(String.valueOf(row.get("e_mail")));
              authorizationBean.setStrPhone(String.valueOf(row.get("phone")));
              authorizationBean.setStrMPhone(String.valueOf(row.get("mobil_phone")));
              authorizationBean.setStrFax(String.valueOf(row.get("fax")));
              authorizationBean.setStrIcq(String.valueOf(row.get("icq")));
              authorizationBean.setStrWebsite(String.valueOf(row.get("website")));
              authorizationBean.setStrQuestion(String.valueOf(row.get("question")));
              authorizationBean.setStrAnswer(String.valueOf(row.get("answer")));
              authorizationBean.setIntLevelUp( Integer.parseInt(String.valueOf(row.get("levelup_cd")))) ;
              authorizationBean.setStrCompany(String.valueOf(row.get("company")));
              authorizationBean.setCountry_id (String.valueOf(row.get("country_id")));
              authorizationBean.setCity_id(String.valueOf(row.get("city_id")));
              authorizationBean.setCurrency_id(String.valueOf(row.get("currency_id")));
              authorizationBean.setIntLogined(1);
              
              
              //query = "UPDATE tuser set idsession = '"+  authorizationBean.getId_session() +"'  where  (login = '" + i_strLogin + "' )  and  (passwd = '" + i_strPasswd + "') and  (site_id = " + authorizationBean.getSite_id() + ")";
              query = "UPDATE tuser set idsession = '"+  idsession +"'  where  user_id =" + authorizationBean.getIntUserID() ;
              
              newTransactionSupport.getJdbcTemplate().update(query);
            //  newTransactionSupport.commitTransaction(transactionStatus);
              return true;
            }
          }
          authorizationBean.setIntLogined(2);
          return false;
      }
      catch (Exception ex) {
          //newTransactionSupport.rollbackTransaction(transactionStatus);
          logger.error(ex.getLocalizedMessage());
          return false;
      }
      finally 
      {
          logger.exit();
      }
  }
  
	public String getSiteIdByHost(final String host)
	{  
	    logger.entry(host);
		
		String siteId = "2" ;
		//String query = "select site_id  from site where site_dir = '" + host + "'" ;
		String query = "select site_id  from site where host = '" + host + "' order by site_id DESC limit 1 " ;
		try 
		{
				siteId =  newTransactionSupport.getJdbcTemplate().queryForObject(query, String.class);
		}
		catch(Exception e) {
		  siteId ="2";
		  logger.error(e.getLocalizedMessage());
		}
		finally
		{
			logger.exit();
		}

	  return siteId ;
	}
	
	
	 public void clearCookieFromBD( final AuthorizationPageBean authorizationBean, final String cokie_session_id) {
		logger.entry(authorizationBean,cokie_session_id);
		if(cokie_session_id == null) return ;
		String query ="";
		TransactionStatus transactionStatus =  newTransactionSupport.beginTransaction();
		try 
		{
			//query = "UPDATE tuser set idsession = '"+  authorizationBean.getId_session() +"'  where  (login = '" + i_strLogin + "' )  and  (passwd = '" + i_strPasswd	+ "') and  (site_id = " + authorizationBean.getSite_id() + ")";
			//query = "UPDATE tuser set idsession = ''  where  user_id =" + authorizationBean.getIntUserID() ;
			query = "DELETE  FROM  store_session WHERE  idsession_hash1 = " + getIdsessionHash1(cokie_session_id) + "  and idsession_hash2 = " +  getIdsessionHash2(cokie_session_id) + " and idsession_hash3 = " +  getIdsessionHash3(cokie_session_id) + " and idsession_hash4 = " +  getIdsessionHash4(cokie_session_id)  ;
			newTransactionSupport.getJdbcTemplate().update(query);
			newTransactionSupport.commitTransaction(transactionStatus);
			if(authorizationBean != null )authorizationBean.setIntLogined(2);
		}
		catch (Exception ex) {
			newTransactionSupport.rollbackTransaction(transactionStatus);
			logger.error(ex.getLocalizedMessage());
		}
		finally 
		{
			logger.exit();
		}
	}
	
	
	 String  getCookiesDir(final ServletContext servletContext) 
	{
		return (String)servletContext.getAttribute("cookies_dir");
	}
	
	
	 public boolean isLoginFromCookieFromDir(final String id_session , final HttpSession  session , final ServletContext  servletContext  , final ResourceBundle session_scope  ) {
		logger.entry(id_session,servletContext,session,session_scope);
	  boolean result = false ;
		
		
		String path_dir =  getCookiesDir(servletContext);
		if(!session.isNew()) return session.isNew() ;
		try {
			if (id_session == null || id_session.length() == 0)	return false;
			String file_cookies = path_dir  +  File.separatorChar + id_session ;
			File file = new File(file_cookies);
			if(file.exists())
			{
				Map map = deserializeObject(file) ;
				Set keyList = map.keySet() ;
				Iterator iterator = keyList.iterator() ;
				String type = "" ;
				while(iterator.hasNext())
				{
					String key = (String)iterator.next() ;
					Object obj = map.get(key);
					type = session_scope.getString(key).trim();
					Object newobj =  createObject(type);
					if(newobj == null || obj == null) continue ; 
					BeanUtils.copyProperties(newobj, obj);
					session.setAttribute(key, obj);
					System.out.println("key " + key);
					System.out.println("object_new " + newobj);
					System.out.println("object " + obj);
				}
				
			}
			
		
		} 
		catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(),ex);
			result =  false;
		}
		finally 
		{
			
		}
		
		return result;
	}
	
	
//	long getIdsessionHash1(String id_session )
//	{
//		String hash = id_session.substring(0, 10) ;
//		return  hash.hashCode() ;
//	}
//
//	long getIdsessionHash2(String id_session )
//	{
//		String hash = id_session.substring(10, 20) ;
//		return  hash.hashCode() ;
//	}
//
//	long getIdsessionHash3(String id_session )
//	{
//		String hash = id_session.substring(20, 30) ;
//		return  hash.hashCode() ;
//	}
//
//	long getIdsessionHash4(String id_session )
//	{
//		String hash = id_session.substring(30) ;
//		if(hash.indexOf(".") != -1 )hash = hash.substring(0 ,hash.indexOf(".")) ;
//		return  hash.hashCode() ;
//	}

	


	public boolean isLoginFromCookie_new1(final String id_session , final HttpSession  session , final ResourceBundle session_scope  ) {
	logger.entry(id_session,session,session_scope);
    boolean result = false ;
	String query ="";
	//if(!session.isNew()) return session.isNew() ;

	
	try {
		if (id_session == null || id_session.length() == 0)	return false;
	
		//StandardSession ss = (StandardSession)session ;
		
		String key = "";
		query = "select USER_ID , TYPE , BCLASSBODY  from store_session WHERE  idsession_hash1 = " + getIdsessionHash1(id_session) + "  and idsession_hash2 = " +  getIdsessionHash2(id_session) + " and idsession_hash3 = " +  getIdsessionHash3(id_session) + " and idsession_hash4 = " +  getIdsessionHash4(id_session)  ;
        result = newTransactionSupport.getJdbcTemplate().query(query, new ResultSetExtractor<Boolean>() {
          public Boolean extractData(ResultSet resultSetObj)
              throws SQLException, DataAccessException {
            boolean result = true;
            String key = "";
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            Enumeration enumeration = session_scope.getKeys();
            while (enumeration.hasMoreElements()) {
              key = (String) enumeration.nextElement();
              String type = session_scope.getString(key).trim();
              String typedb = resultSetObj.getString("TYPE");
              if (typedb != null)
                if (typedb.equals(type)) {
                  try {
                    bais = new ByteArrayInputStream(resultSetObj.getBytes("BCLASSBODY"));
                    ois = new ObjectInputStream(bais);
                    Object obj = ois.readObject();
                    session.setAttribute(key, obj);
                  } catch (Exception e) {
                    logger.error(e.getLocalizedMessage());
                  }
                }
            }
            return result;

          }
        });

	}
	catch (Exception ex) {
		logger.error(ex.getLocalizedMessage());
		result =  false;
	}
	
	return result;
}

public boolean loadOldSessionbyLogin(final String user_id , final HttpSession  session , final ResourceBundle session_scope  ) {
    logger.entry(user_id,session,session_scope);
	boolean result = false ;
	String query ="";
	//if(!session.isNew()) return session.isNew() ;
	try {
		if (user_id == null || user_id.length() == 0)	return false;
	
		//StandardSession ss = (StandardSession)session ;
		
		
		query = "select USER_ID , TYPE , CLASSBODY  from store_session WHERE  USER_ID = " + user_id   ;
        result = newTransactionSupport.getJdbcTemplate().query(query, new ResultSetExtractor<Boolean>() {
          public Boolean extractData(ResultSet resultSetObj)
              throws SQLException, DataAccessException {
            Boolean result = true;
            String key = "";
            Enumeration enumeration = session_scope.getKeys();
            while (enumeration.hasMoreElements()) {
              try {
                key = (String) enumeration.nextElement();
                String type = session_scope.getString(key).trim();
                String typedb = resultSetObj.getString("TYPE");
                if (typedb != null)
                  if (typedb.equals(type)) {
                    Object obj = resultSetObj.getObject("CLASSBODY");
                    Object newobj = createObject(type);
                    BeanUtils.copyProperties(newobj, obj);
                    session.setAttribute(key, newobj);
                    obj = null;
                    // System.out.println("load key: " + key + " object: " +
                    // obj.getClass().getName() );
                  }
              } catch (Exception e) {
                logger.error(e.getLocalizedMessage());
              }
            }
            return result;
          }
        });

		
	} 
	catch (Exception ex) {
		logger.error(ex.getLocalizedMessage());
		result =  false;
	}
	finally 
	{
		logger.exit();
	}
	
	return result;
}
	
final public Object createObject(final String className) {
  logger.entry(className);
  Object obj = null;
  try {
    Class cls = Class.forName(className);
    obj = cls.newInstance();
  } catch (Exception ex) {
    logger.error(ex.getLocalizedMessage());
  }
  return obj;
}
	
final public Map deserializeObject(final File fileName) {
  logger.entry(fileName);
  Map map = null;
  ObjectInputStream in = null;
  try {
    in = new ObjectInputStream(new FileInputStream(fileName));
    map = (Map) in.readObject();

  } catch (Exception ex) {
    logger.error(ex.getLocalizedMessage());
  } finally {
    if (in != null)
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    logger.exit();
  }
  return map;
}
	
final public AuthorizationPageBean getAuthorizationBean(final String userId) {
  logger.entry(userId);
  AuthorizationPageBean authorization = new AuthorizationPageBean();
  String query = "";
  try {
    if (userId == null || userId.length() == 0)
      return null;


    // query = "SELECT user_id , login , passwd , first_name , last_name
    // , e_mail , phone ,
    // mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd ,
    // company , country_id , city_id , currency_id FROM tuser where
    // (login = '" + i_strLogin + "' ) and (passwd = '" + i_strPasswd +
    // "')" ;
    query =
        "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  "
            + "FROM tuser  where  user_id = " + userId + "";
    List<Map<String, Object>> rows = newTransactionSupport.getJdbcTemplate().queryForList(query);
    if (rows.size() == 1) {
      for (Map row : rows) {
        authorization.setIntUserID(Integer.parseInt((String) row.get("user_id")));
        authorization.setStrLogin((String) row.get("login"));
        authorization.setStrPasswd((String) row.get("passwd"));
        authorization.setStrFirstName((String) row.get("first_name"));
        authorization.setStrLastName((String) row.get("last_name"));
        authorization.setStrEMail((String) row.get("e_mail"));
        authorization.setStrPhone((String) row.get("phone"));
        authorization.setStrMPhone((String) row.get("mobil_phone"));
        authorization.setStrFax((String) row.get("fax"));
        authorization.setStrIcq((String) row.get("icq"));
        authorization.setStrWebsite((String) row.get("website"));
        authorization.setIntLevelUp(Integer.parseInt((String) row.get("levelup_cd")));
        authorization.setStrCompany((String) row.get("company"));
        authorization.setCountry_id((String) row.get("country_id"));
        authorization.setCity_id((String) row.get("city_id"));
        authorization.setCurrency_id((String) row.get("currency_id"));
        return authorization;
      }
    }
    return authorization;
  } catch (Exception ex) {
    logger.error(ex.getLocalizedMessage());
    return authorization;
  } finally {
    logger.exit();
  }
}

	
final public AuthorizationPageBean getAuthorizationBeanOfRoleAdmin(final String siteId) {
  logger.entry(siteId);
  AuthorizationPageBean authorization = new AuthorizationPageBean();
  String query = "";
  try {
    if (siteId == null || siteId.length() == 0)
      return null;
    // query = "SELECT user_id , login , passwd , first_name , last_name
    // , e_mail , phone ,
    // mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd ,
    // company , country_id , city_id , currency_id FROM tuser where
    // (login = '" + i_strLogin + "' ) and (passwd = '" + i_strPasswd +
    // "')" ;
    query =
        "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  "
            + "FROM tuser  where  levelup_cd = 2 and site_id = " + siteId + "";
    List<Map<String, Object>> rows = newTransactionSupport.getJdbcTemplate().queryForList(query);
    if (rows.size() == 1) {
      for (Map row : rows) {
        authorization.setIntUserID(Integer.parseInt((String) row.get("user_id")));
        authorization.setStrLogin((String) row.get("login"));
        authorization.setStrPasswd((String) row.get("passwd"));
        authorization.setStrFirstName((String) row.get("first_name"));
        authorization.setStrLastName((String) row.get("last_name"));
        authorization.setStrEMail((String) row.get("e_mail"));
        authorization.setStrPhone((String) row.get("phone"));
        authorization.setStrMPhone((String) row.get("mobil_phone"));
        authorization.setStrFax((String) row.get("fax"));
        authorization.setStrIcq((String) row.get("icq"));
        authorization.setStrWebsite((String) row.get("website"));
        authorization.setIntLevelUp(Integer.parseInt((String) row.get("levelup_cd")));
        authorization.setStrCompany((String) row.get("company"));
        authorization.setCountry_id((String) row.get("country_id"));
        authorization.setCity_id((String) row.get("city_id"));
        authorization.setCurrency_id((String) row.get("currency_id"));
        return authorization;
      }
    }
    return authorization;
  } catch (Exception ex) {
    logger.error(ex.getLocalizedMessage());
    return authorization;
  } finally {
    logger.exit();
  }
}

	
final	public AuthorizationPageBean getFromMainSiteUserAuthorizationBean(final String login ) {
		logger.entry(login);
		AuthorizationPageBean authorization = new AuthorizationPageBean();
		String query ="";
		try {
			if (login == null || login.length() == 0)	return null;
			

			query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  " +
					"FROM tuser  where  user_id = '" + login + "' and  site_id = " + SiteType.MAIN_SITE  ;
			 List<Map<String, Object>> rows = newTransactionSupport.getJdbcTemplate().queryForList(query);
			    if (rows.size() == 1) {
			      for (Map row : rows) {
			        authorization.setIntUserID(Integer.parseInt((String) row.get("user_id")));
			        authorization.setStrLogin((String) row.get("login"));
			        authorization.setStrPasswd((String) row.get("passwd"));
			        authorization.setStrFirstName((String) row.get("first_name"));
			        authorization.setStrLastName((String) row.get("last_name"));
			        authorization.setStrEMail((String) row.get("e_mail"));
			        authorization.setStrPhone((String) row.get("phone"));
			        authorization.setStrMPhone((String) row.get("mobil_phone"));
			        authorization.setStrFax((String) row.get("fax"));
			        authorization.setStrIcq((String) row.get("icq"));
			        authorization.setStrWebsite((String) row.get("website"));
			        authorization.setIntLevelUp(Integer.parseInt((String) row.get("levelup_cd")));
			        authorization.setStrCompany((String) row.get("company"));
			        authorization.setCountry_id((String) row.get("country_id"));
			        authorization.setCity_id((String) row.get("city_id"));
			        authorization.setCurrency_id((String) row.get("currency_id"));
			        return authorization;
			      }
			    }
			    return authorization;
		}
		catch (Exception ex) {
		    logger.error(ex.getLocalizedMessage(),ex);
			return authorization;
		}
		finally 
		{
			logger.exit();
		}
	}
	
	
	final public int getFromMainSiteUserId(final String login ) {
	  logger.entry(login);  
	  String query ="";
		try {
			if (login == null || login.length() == 0)	return 0;
			
			query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,levelup_cd ,bank_cd , company , country_id , city_id ,  currency_id  " +
					"FROM tuser  where  user_id = '" + login + "' and  site_id = " + SiteType.MAIN_SITE  ;
			List<Map<String,Object>> response = newTransactionSupport.getJdbcTemplate().queryForList(query);
			if (response.size() == 1) {
				//authorization.setIntUserID(  Integer.parseInt((String) Adp.getValueAt(0, 0)));
				return Integer.parseInt((String) response.get(0).get("user_id"));
			}
			return 0;
		} 
		catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(),ex);
			return 0;
		}
		finally 
		{
			logger.exit();
		}
	}
	
	final public int isRegCorrect(final String i_strLogin, final String i_strPasswd,
			final String i_strCPasswd,final String i_strFName,final String i_strLName,
			final String i_strCompany,final String i_strEMail,final String i_strPhone,
			final String i_strMPhone,final String i_strFax, final String i_strIcq,
			final String i_strWebsite,final String i_strQuestion,final String i_strAnswer,
			final String i_strBirthday,final String i_strCountry_id,final String i_strCity_id,
			final String i_strCurrency_id ,final String cookie_session_id  , final AuthorizationPageBean authorizationBean) {

		logger.entry(i_strLogin,i_strPasswd,i_strCPasswd,i_strFName,i_strLName,i_strCompany,i_strCurrency_id,i_strEMail,cookie_session_id,authorizationBean);
		TransactionStatus transactionStatus = newTransactionSupport.beginTransaction();
		String query = "";
		try {
			if (i_strLogin == null || i_strLogin.length() == 0)
				return 1;
			if (i_strPasswd == null || i_strPasswd.length() == 0)
				return 2;
			if (i_strPasswd.length() < 6)	return 12;
			
			if (i_strCPasswd == null || i_strPasswd.length() == 0)
				return 10;
			if (i_strFName == null || i_strFName.length() == 0)
				return 3;
			if (i_strLName == null || i_strLName.length() == 0)
				return 4;
			if (i_strEMail == null || i_strEMail.length() == 0)
				return 5;
			if (i_strCity_id == null || i_strCity_id.length() == 0  )
				return 11;
			if (i_strEMail.indexOf("@") == -1)
				return 8;
			if (i_strPasswd.compareTo(i_strCPasswd) != 0)
				return 9;
			if (!isEnglish(i_strLogin))	return 13;
			
			query = "SELECT user_id  FROM tuser where  (login = '" + i_strLogin	+ "' )  and  (passwd = '" + i_strPasswd + "' )";
			String user_id = newTransactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			if (!StringUtils.isEmpty(user_id)) {
				authorizationBean.setIntUserID( Long.parseLong(user_id)) ;
				query = "update tuser set " + " login = '" + i_strLogin
						+ "' , " + "  passwd = '" + i_strPasswd + "' , "
						+ "  first_name = '" + i_strFName + "' , "
						+ "  last_name = '" + i_strLName + "' , "
						+ "  e_mail = '" + i_strEMail + "' , " + "  phone = '"
						+ i_strPhone + "' , " + "  mobil_phone = '"
						+ i_strMPhone + "' , " + "  fax = '" + i_strFax
						+ "' , " + "  icq = '" + i_strIcq + "' , "
						+ "  website = '" + i_strWebsite + "' , "
						+ "  question = '" + i_strQuestion + "' , "
						+ "  answer = '" + i_strAnswer + "' , "
						+ "  company  = '" + i_strCompany + "' , "
						+ "  country_id  = " + i_strCountry_id + " , "
						+ "  city_id  = " + i_strCity_id + " , "
						+ "  currency_id = " + i_strCurrency_id + " "
						+ "  where user_id = " + authorizationBean.getIntUserID();
				newTransactionSupport.getJdbcTemplate().update(query);
				authorizationBean.setIntLogined(1);
				newTransactionSupport.commitTransaction(transactionStatus);
				return 0;
			}

			/*
			 * else { intLogined = 2 ; intUserID = 0 ; strLogin = "" ;
			 * Adp.commit(); Adp.close(); return -1 ; }
			 */

			query = "SELECT user_id  FROM tuser  where login  = '" + i_strLogin	+ "'";
			user_id = newTransactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			if (!StringUtils.isEmpty(user_id)) {
				authorizationBean.setIntUserID(Long.parseLong((String) user_id));
				authorizationBean.setIntLogined(3);
				authorizationBean.setIntUserID(0);
				authorizationBean.setStrLogin("");
				newTransactionSupport.commitTransaction(transactionStatus);
				return -1;
			}


			//query = "SELECT NEXT VALUE FOR tuser_user_id_seq  AS ID  FROM ONE_SEQUENCES";
			query = tuser_sequence_rs;
			user_id = newTransactionSupport.getJdbcTemplate().queryForObject(query,String.class);
			authorizationBean.setIntUserID(Long.parseLong((String) user_id));

            query =
                "insert into tuser ( user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone,fax,icq,website,question,answer,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , company , country_id , city_id , currency_id , site_id , idsession ) "
                    + " values ( ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";

            newTransactionSupport.getJdbcTemplate().update(query, authorizationBean.getIntUserID(), i_strLogin, i_strPasswd,
                i_strFName, i_strLName, i_strEMail, i_strPhone, i_strMPhone, i_strFax, i_strIcq,
                i_strWebsite, i_strQuestion, i_strAnswer, true, true, new java.util.Date(), 1, 0,
                i_strCompany, Long.parseLong(i_strCountry_id), Long.parseLong(i_strCity_id),
                Long.parseLong(i_strCurrency_id), Long.parseLong(authorizationBean.getSite_id()),
                cookie_session_id);

			
			//+ "  idsession = '" + cookie_session_id + "' , "
			
			//query = "SELECT NEXT VALUE FOR account_id_seq  AS ID  FROM ONE_SEQUENCES";
			query = account_sequence_rs;
			
			String account_id = newTransactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "
				+ " values (  ? , ? , ? , ? , ? ,  ? ,  ?  ) " ;
			
			newTransactionSupport.getJdbcTemplate().update(query, Long.parseLong(account_id),authorizationBean.getIntUserID(),0,3,new java.util.Date()," new_account ",Long.parseLong(i_strCurrency_id));
			
			authorizationBean.setIntLogined(1);
			authorizationBean.setIntLevelUp(1);
			newTransactionSupport.commitTransaction(transactionStatus);
			return 0;

		 }
		catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(),ex);
			newTransactionSupport.rollbackTransaction(transactionStatus);
			return -2;
		}

		finally 
		{
			logger.exit();
		}

	}


    public void initSiteDir(final String site_id, AuthorizationPageBean authorizationBean) {
      String queryForSite =
          "select site_dir, subject_site , nick_site , company_name , host from site where site_id = "
              + site_id;
      logger.entry(site_id, authorizationBean);
      // Adp.newTransactionSupport.beginTransaction();
      newTransactionSupport.getJdbcTemplate().query(queryForSite,
          new ResultSetExtractor<List<AuthorizationPageBean>>() {
            public List<AuthorizationPageBean> extractData(ResultSet rs)
                throws SQLException, DataAccessException {
              List<AuthorizationPageBean> list = new ArrayList<AuthorizationPageBean>();
              while (rs.next()) {
                authorizationBean.setSite_dir(rs.getString("site_dir"));
                authorizationBean.setSubject_site(rs.getString("subject_site"));
                authorizationBean.setNick_site(rs.getString("nick_site"));
                authorizationBean.setHost(rs.getString("company_name"));
              }
              return list;
            }
          });



    }


	
	

	
		
    public void initUserSite(final long user_id, AuthorizationPageBean authorizationBean) {
      logger.entry(user_id, authorizationBean);
      String query = "select site_id  from site where site.owner = " + user_id
          + " order by site.site_id desc ";
      try {
        List<Map<String, Object>> queryList = newTransactionSupport.getJdbcTemplate().queryForList(query);
        if (queryList.size() > 0) {
          authorizationBean.setUser_site(String.valueOf(queryList.get(0).get("site_id"))); // + " " +
        } else
          authorizationBean.setUser_site("-1");
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
      }

      finally {
        logger.exit();
      }

    }
	
	public int getLengId(final String locale) 
	{
	    logger.entry(locale);
		String leng_id = "-1" ;
		String query = "select lang_id  from lang where lable = '" + locale + "'" ;

		try 
		{
			List<Map<String,Object>> queryForLeng = newTransactionSupport.getJdbcTemplate().queryForList(query);
			if (queryForLeng.size() != 0) {
				leng_id = (String) queryForLeng.get(0).get("lang_id"); // + " " +
			}
			
		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage(),ex);
		}

		finally {
			logger.exit();
		}
		
		return  Integer.parseInt(leng_id);
	}
	
	public void initPaySys_Shop_cd(final String site_id , final AuthorizationPageBean authorizationBean ) {
		String query = "select  shop_cd from shop where site_id = " + site_id;
		
		try 
		{
			List<Map<String,Object>> queryForPaySys_Shop_cd = newTransactionSupport.getJdbcTemplate().queryForList(query);

			if (queryForPaySys_Shop_cd.size() != 0) {
				authorizationBean.setPaysys_shop_cd((String) queryForPaySys_Shop_cd.get(0).get("shop_cd"));
				if (authorizationBean.getPaysys_shop_cd() == null)
					logger.entry(false,
							"ERROR: select shop_cd  from shop where site_id = "
									+ site_id + " \n   shop_cd = null ");
			} else
				logger.entry(false,
						"ERROR: select shop_cd  from shop where site_id = "
								+ site_id + " \n   shop_cd = null ");

		}
		catch (Exception ex) 
		{
			logger.error(ex.getLocalizedMessage());
		}
		finally 
		{
			logger.exit();
		}

	}
	
	public List<TUserDto> getUserList( final AuthorizationPageBean authorizationBean ) {
	  logger.entry(authorizationBean);
	  List<TUserDto> tuserDtoList = new ArrayList<>();
		String query ="";
		
		if(authorizationBean.getIntLevelUp() != 2 ) return null ;
		try {			
			query = "SELECT user_id , login , passwd , first_name , last_name , e_mail , phone , mobil_phone , regdate FROM tuser  where	site_id = " + authorizationBean.getSite_id() + "";
			
			tuserDtoList = newTransactionSupport.getJdbcTemplate().query(query, new RowMapper() {
              
              @Override
              public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
                  TUserDto tuserDto = new TUserDto(rs.getLong(0),rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7),rs.getDate(8));
               
            return tuserDto;
              }
          });
		}
		catch (Exception ex) {
			logger.error(ex.getLocalizedMessage(),ex);
		}
		finally 
		{
			logger.exit();
		}
		
	 return tuserDtoList ;
	}
	
	
	public boolean isNumber(final String tmp) {
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

	final public boolean isEnglish( String tmp) {
		if (tmp == null)
			return false;
		tmp = tmp.toLowerCase() ;
		String IntField = "0123456789qwertyuiopasdfghjklzxcvbnm_-";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}

	
	public final void saveNewDomain(final String host , final String site_id  ) 
	{      
	    logger.entry(host,site_id);
		String query = "" ;
		TransactionStatus transStatus = newTransactionSupport.beginTransaction();
		try {
					query = "UPDATE site SET HOST = ?  where SITE_ID =  " + site_id ;
					newTransactionSupport.getJdbcTemplate().update(query, host);
					newTransactionSupport.commitTransaction(transStatus);		
		}
		catch (Exception ex) 
		{
			newTransactionSupport.rollbackTransaction(transStatus);
			logger.error(ex.getLocalizedMessage(),ex) ;
		}
		finally
		{
			logger.exit();
		}
	}
	
	
		
final	long getIdsessionHash1(final String id_session )
	{
		String hash = id_session.substring(0, 10) ;
		return  hash.hashCode() ;
	}

final	long getIdsessionHash2(final String id_session )
	{
		String hash = id_session.substring(10, 20) ;
		return  hash.hashCode() ;
	}

final	long getIdsessionHash3(final String id_session )
	{
		String hash = id_session.substring(20, 30) ;
		return  hash.hashCode() ;
	}

final	long getIdsessionHash4(final String id_session )
	{
		String hash = id_session.substring(30) ;
		if(hash.indexOf(".") != -1 )hash = hash.substring(0 ,hash.indexOf(".")) ;
		return  hash.hashCode() ;
	}
	

final	public void saveClassesSessionScope_new1(final HttpSession  session  ) 
{
    logger.entry(session);
	AuthorizationPageBean AuthorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
	if( AuthorizationPageBeanId == null ) return ;
	//if( AuthorizationPageBeanId.getStrLogin().equals(SiteRole.GUEST)) return ;
	//if( AuthorizationPageBeanId.getIntLevelUp() == 0 ) return ;
	String query = "" ;
	String key = "";
	Enumeration enumeration;
	String type = null ;
	Object obj = null ;
	String cokie_session_id = (String) session.getAttribute("cokie_session_id");
	session.removeAttribute(cokie_session_id);
	TransactionStatus trxnStatus = newTransactionSupport.beginTransaction();
	try {
		enumeration = session_scope.getKeys();
		while (enumeration.hasMoreElements()) 
		{
			key = (String) enumeration.nextElement();
			 type = session_scope.getString(key).trim();
			 obj = session.getAttribute(key) ;
			 if(obj == null ) continue ;
			 query = "select USER_ID  from store_session WHERE  idsession_hash1 = " + getIdsessionHash1(cokie_session_id) + "  and idsession_hash2 = " +  getIdsessionHash2(cokie_session_id) + " and idsession_hash3 = " +  getIdsessionHash3(cokie_session_id) + " and idsession_hash4 = " +  getIdsessionHash4(cokie_session_id) + " AND TYPE = '" +type+ "'" ;
			//query = "select USER_ID from store_session WHERE USER_ID = " + AuthorizationPageBeanId.getIntUserID() + " AND TYPE = '" +type+ "'" ;
			List<Map<String,Object>> getUserList = newTransactionSupport.getJdbcTemplate().queryForList(query);
			
			if (getUserList.size() > 0) 
			{
				query = "update store_session  set  USER_ID = ? , TYPE = ? , BCLASSBODY = ? , " +
				" ACTIVE = ? WHERE  idsession_hash1 = " + getIdsessionHash1(cokie_session_id) + "  and idsession_hash2 = " +  getIdsessionHash2(cokie_session_id) + " and idsession_hash3 = " +  getIdsessionHash3(cokie_session_id) + " and idsession_hash4 = " +  getIdsessionHash4(cokie_session_id) + " AND TYPE = '" +type+ "'" ;
				newTransactionSupport.getJdbcTemplate().update(query, AuthorizationPageBeanId.getIntUserID(),type,objectToBytes(obj),true);

			}
			else
			{

				query = "insert into store_session ( USER_ID ,  TYPE ,  BCLASSBODY ,  ACTIVE , idsession_hash1 , idsession_hash2 , idsession_hash3 , idsession_hash4 ) "
					+ " VALUES ( ? ,  ? ,  ? , ? , ? ,  ? ,  ? , ?)" ;
				newTransactionSupport.getJdbcTemplate().update(query, AuthorizationPageBeanId.getIntUserID(),type,objectToBytes(obj),true,getIdsessionHash1(cokie_session_id),getIdsessionHash2(cokie_session_id) 
				          ,getIdsessionHash3(cokie_session_id) ,getIdsessionHash4(cokie_session_id) );
				
			} 
			
			
			//session.setAttribute(key,obj) ;
		}
		newTransactionSupport.commitTransaction(trxnStatus);	
	}
	catch (Exception ex) 
	{
		newTransactionSupport.rollbackTransaction(trxnStatus);
		logger.error(ex.getLocalizedMessage(),ex) ;
	}
	finally
	{
		logger.exit();
	}
}


final	public byte[] objectToBytes(final Object obj ) {
	ByteArrayOutputStream baos = null ;
	ObjectOutputStream oos = null ;
	byte[] result = null ;
	//if(!session.isNew()) return session.isNew() ;
	try {
	
		baos = new ByteArrayOutputStream();
		oos = new  ObjectOutputStream(baos);
		oos.writeObject(obj) ;
		result = baos.toByteArray() ;
		
	} 
	catch (Exception ex) {
		logger.error(ex.getLocalizedMessage(),ex);
	}
	finally 
	{
		try {
			if(baos != null ) baos.close();
			if(oos != null ) oos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	return result;
}
	

	final public void AddAliases(final ServletContextEvent sce) {
		logger.entry(sce);
		 
		//SELECT  LAST_DATE  FROM DOMAIN_PROC WHERE DOMAIN_PROC_ID = 0
		GetValueTool tool = new GetValueTool() ;
		Date curent_date = new Date() ;
		Date last_post = new Date() ;
		AddAliase addAliase;
		Document doc ;
		String query = "select host  from site group by host LIMIT ? OFFSET ?" ;
		String count_query = "select count(host) from site where host in ( select host from site group by host )" ;
		String host = "www.siteoneclick.com" ;
		String aliase = "" ;
		
		StringBuffer hosts_for_dns = new StringBuffer();
		File file =  new File("/") ;
		 String server_config = System.getProperty("conf");
		 if(server_config == null || server_config.length() == 0) 
		 {
			 String dir = System.getProperty("user.dir");
			 dir = dir.substring(0 ,dir.indexOf("bin")) + "conf" + File.separatorChar+"server.xml" ;
			//File f = new File("C:/apache-tomcat-6.0.14/conf/server.xml") ;
			file = new File(dir) ;
		 }
		 else 
		 {
			 file = new File(server_config) ; 
		 }

		TransactionStatus transactionStatus = newTransactionSupport.beginTransaction();
		
		try 
		{
			List<Map<String,Object>> rows = newTransactionSupport.getJdbcTemplate().queryForList("SELECT  LAST_DATE  FROM DOMAIN_PROC WHERE DOMAIN_PROC_ID = 0");
			if(rows.size() > 0) 
			{
			String date = (String)rows.get(0).get("LAST_DATE");
			last_post = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			}

			addAliase = new AddAliase();
			doc = addAliase.loadConfig(file);
			addAliase.RemoveAllAliase(doc, host) ;
			
			long size = 0 ;
			int limmit = 20 ;
			rows.clear();
			rows  = newTransactionSupport.getJdbcTemplate().queryForList(count_query);
			if (rows.size() > 0) size = Long.parseLong((String)rows.get(0).get("count(host)"));
			
			rows.clear();
			for (int offset = 0; size > offset; offset = offset + 20)
			{ 
			   rows = newTransactionSupport.getJdbcTemplate().queryForList(query, limmit, offset);
				
				for (int i = 0; rows.size() > i; i++) 
				{
					//aliase =  Adp.getValueAt(i, 0);
					aliase =  tool.getValueAtMap(rows, i, "host");
					int indexof = aliase.indexOf(".irr.bz");
					//if(indexof != -1 && i < 280 && isAnsi(aliase) )
					if(indexof != -1 && isAnsi(aliase) )
					{
					addAliase.AddAliaseToHost(doc, host, aliase) ;
					hosts_for_dns.append(aliase.substring(0,indexof));	
					hosts_for_dns.append(" A 0 78.37.191.193 86400\n");	
					}
				}
			}
			
			addAliase.writeXmlFile(doc,file);	 
			
			if(curent_date.getHours() != last_post.getHours() )
			{
			DomainRegister r = new DomainRegister();
			String auth = r.getAuth() ;
			r.setDomainAliases("irr.bz" , hosts_for_dns.toString() ,auth );
			logger.info(r.getPostCommand(auth));

			 newTransactionSupport.getJdbcTemplate().update("UPDATE DOMAIN_PROC SET LAST_DATE  = ? WHERE DOMAIN_PROC_ID = 0",  new Date());
			}
			newTransactionSupport.commitTransaction(transactionStatus);

			//System.exit(0);
		}
		catch (Exception e) 
		{
			System.out.println(" server config: " + file.getPath());
			newTransactionSupport.rollbackTransaction(transactionStatus);
			logger.error(e.getLocalizedMessage(),e);
		}
		finally
		{
			logger.exit();
			
			
		}

		
	}

	
	final public void PostToDNSServerAliases() {
		
		 
		//SELECT  LAST_DATE  FROM DOMAIN_PROC WHERE DOMAIN_PROC_ID = 0
		Date curent_date = new Date() ;
		Date last_post = new Date() ;
		//AddAliase addAliase;
		//Document doc ;
		String query = "select host  from site group by host" ;
		//String host = "www.siteoneclick.com" ;
		String aliase = "" ;
		StringBuffer hosts_for_dns = new StringBuffer();
		TransactionStatus trxnStatus = newTransactionSupport.beginTransaction();
		try 
		{
			List<Map<String,Object>> queryForlastDate = newTransactionSupport.getJdbcTemplate().queryForList("SELECT  LAST_DATE  FROM DOMAIN_PROC WHERE DOMAIN_PROC_ID = 0");
			if(queryForlastDate.size() > 0) 
			{
			String date = (String)queryForlastDate.get(0).get("LAST_DATE");
			last_post = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			}

			
			
			List<Map<String,Object>> queryForhost = newTransactionSupport.getJdbcTemplate().queryForList(query);
			for (int i = 0; queryForhost.size() > i; i++) 
			{
				aliase = (String) queryForhost.get(i).get("host");
				int indexof = aliase.indexOf(".irr.bz");
				//if(indexof != -1 && i < 280 && isAnsi(aliase) )
				if(indexof != -1 && isAnsi(aliase) )
				{
					
					hosts_for_dns.append(aliase.substring(0,indexof));	
					hosts_for_dns.append(" A 0 78.37.191.193 86400\n");	
				}
			}
			
			
			if(curent_date.getHours() != last_post.getHours() )
			{
			DomainRegister r = new DomainRegister();
			String auth = r.getAuth() ;
			r.setDomainAliases("irr.bz" , hosts_for_dns.toString() ,auth );
			logger.info(r.getPostCommand(auth));
			
			newTransactionSupport.getJdbcTemplate().update("UPDATE DOMAIN_PROC SET LAST_DATE  = ? WHERE DOMAIN_PROC_ID = 0", new Date());
			}
			
			newTransactionSupport.commitTransaction(trxnStatus);
			//System.exit(0);
		}
		catch (Exception e) 
		{
			//System.out.println(" server config: " + file.getPath());
			logger.error(e.getMessage(),e);
			newTransactionSupport.rollbackTransaction(trxnStatus);
		}
		finally
		{
			logger.exit();
		}

		
	}
	
	final public void AddAliasesInFile() {
		
		 
		//SELECT  LAST_DATE  FROM DOMAIN_PROC WHERE DOMAIN_PROC_ID = 0
			AddAliase addAliase;
		Document doc ;
		String query = "select host  from site group by host" ;
		String host = "www.siteforyou.net" ;
		String aliase = "" ;
		
		//StringBuffer hosts_for_dns = new StringBuffer();
		File file =  new File("/") ;
		 String server_config = System.getProperty("conf");
		 if(server_config == null || server_config.length() == 0) 
		 {
			 String dir = System.getProperty("user.dir");
			 dir = dir.substring(0 ,dir.indexOf("bin")) + "conf" + File.separatorChar+"server.xml" ;
			//File f = new File("C:/apache-tomcat-6.0.14/conf/server.xml") ;
			file = new File(dir) ;
		 }
		 else 
		 {
			 file = new File(server_config) ; 
		 }

		try 
		{
			
			
			addAliase = new AddAliase();
			doc = addAliase.loadConfig(file);
			addAliase.RemoveAllAliase(doc, host) ;
			
			List<Map<String,Object>> rows = newTransactionSupport.getJdbcTemplate().queryForList(query);
			for (int i = 0; rows.size() > i; i++) 
			{
				aliase =  (String)rows.get(i).get("host");
				int indexof = aliase.indexOf(".irr.bz");
				//if(indexof != -1 && i < 280 && isAnsi(aliase) )
				if(indexof != -1 && isAnsi(aliase) )
				{
					addAliase.AddAliaseToHost(doc, host, aliase) ;
				}
			}
			addAliase.writeXmlFile(doc,file);	 
			
			//System.exit(0);
		}
		catch (Exception e) 
		{
			System.out.println(" server config: " + file.getPath());
			logger.error(e.getMessage(),e);
		}
		finally
		{
		  logger.exit();
		}
	}
	
final	public boolean isAnsi(String tmp) {
		if (tmp == null)
			return false;
		tmp = tmp.toLowerCase();
		String IntField = "0123456789qwertyuiopasdfghjklzxcvbnm_-.";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}
	


//	public void setResources_cms_settings(ResourceBundle resources_cms_settings) {
//		this.setup_resources = resources_cms_settings;
//	}


 public ResourceBundle getActions_resources() {
  return actions_resources;
}

//public void setActions_resources(ResourceBundle actions_resources) {
//this.actions_resources = actions_resources;
//}

//final   public ResourceBundle getXslt_resources() {
//  return xslt_resources;
//}

	public ResourceBundle getSession_scope() {
		return session_scope;
	}

 public Map getControllerMap() {
  return controllerMap;
}


//	public void setSession_scope(ResourceBundle session_scope) {
//		this.session_scope = session_scope;
//	}


//	public void setSetup_resources(ResourceBundle setup_resources) {
//		this.setup_resources = setup_resources;
//	}

	 public CreateShopBean getCreateShopBean() {
		return createShopBean;
	}
	
	  public Map getTransformerMap() {
      return transformerMap;
  }
	
	final  public ResourceBundle getSetup_resources() {
      return setup_resources;
  }

//	public void setCreateShopBean(CreateShopBean createShopBean) {
//		this.createShopBean = createShopBean;
//	}
}
