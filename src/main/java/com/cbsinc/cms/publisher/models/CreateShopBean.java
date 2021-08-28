package com.cbsinc.cms.publisher.models;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletContext;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.controllers.Layout;
import com.cbsinc.cms.publisher.controllers.SiteRole;
import com.cbsinc.cms.publisher.controllers.SpecialCatalog;
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
    @PropertySource(value="classpath:SetupApplicationResources.properties"),
    @PropertySource("classpath:sequence.properties")
  })
public class CreateShopBean implements java.io.Serializable {

	transient private static final long serialVersionUID = -9104239575252904289L;

	//transient static private Logger log = Logger.getLogger(CreateShopBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(CreateShopBean.class.getName());

	private String host = "www.yourcompany.ru";

	private String owner = "0";

	private String site_id = "0";

	private String home_dir = "www.yourcompany.ru";

	private String site_dir = "www.yourcompany.ru";

	private String person = "Ivan Ivanov";

	private String phone = "111-1234";

	private String address = "";

	private String subject_site = "";

	private String nick_site = "";

	private String company_name = "";

	private String country_id = "1";

	private String city_id = "1";

	private String login = "";

	private String passwd = "";

	private String currency_id = "1";

	private String dufaultSite = "localhost";

	private String account_id = "";

	transient ResourceBundle sequences_rs = null;
	

    @Value("${site}")
    private String site_sequence_rs;
    

    @Value("${tuser}")
    private String tuser_sequence_rs;
    
    @Value("${account}")
    private String account_sequence_rs;
    
    @Value("${catalog}")
    private String tcatalog_sequence_rs;
    
    @Value("${soft}")
    private String soft_sequence_rs;
	
	@Autowired
	TransactionSupport transactionSupport;

	
	public void cretareSiteDirWithExtract(String zipfilename, String zipfilepath, String newSiteDir,
			ServletContext applicationContext) {
		try {
			String path = this.getClass().getResource("").getPath();
			path = path.substring(0, path.indexOf("/WEB-INF/"));
			unzipFile(zipfilename, zipfilepath, path + "//xsl//" + newSiteDir, applicationContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		;
	}

	public void unzipFile(String zipfilename, String zipfilepath, String workdirpath,
			ServletContext applicationContext) {
		File oldFile = new File(workdirpath + "_old");
		File origFile = new File(workdirpath);
		origFile.renameTo(oldFile);

		final int BUFFER = 2048;
		try {
			String message = "";
			if (zipfilepath.endsWith(".zip") || zipfilepath.endsWith(".jar")) {
				message = "Р¤Р°РёР» " + zipfilename + "РїСЂР°РІРµР»СЊРЅРѕРіРѕ С‚РёРїР°.";
			} else {
				message = "Р¤Р°РёР» " + zipfilename
						+ "РЅРµ РїСЂР°РІРµР»СЊРЅРѕРіРѕ С‚РёРїР°. Р¤Р°РёР» РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ СѓРїРѕРєРѕРІР°РЅ РІ zip Р°СЂС…РёРІ.";
				logger.info(message);
				return;
			}
			// String zipfilename = workdir.getPath() + File.separator + uploadfilename ;
			FileInputStream fis = new FileInputStream(zipfilepath);
			BufferedOutputStream dest = null;
			ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry;

			while ((entry = zis.getNextEntry()) != null) {
				System.out.println("Extracting: " + entry);
				int count;
				byte data[] = new byte[BUFFER];
				// write the files to the disk
				// String filename =
				// entry.getName().substring(entry.getName().indexOf((char)47)+1) ;
				String filename = entry.getName();
				if (filename.equals("") || filename.endsWith("/"))
					continue;
				File file = new File(workdirpath + File.separator + filename);
				createDirForFile(file);
				file.createNewFile();
				FileOutputStream fos = new FileOutputStream(file);
				dest = new BufferedOutputStream(fos, BUFFER);
				while ((count = zis.read(data, 0, BUFFER)) != -1) {
					dest.write(data, 0, count);
				}

				dest.flush();
				dest.close();
			}
			zis.close();
			message = "file: " + zipfilename + " was instaled as design";
			logger.info(message);
			// getServletContext().getAttribute("ITransformationService") ;
			if (applicationContext.getAttribute("ITransformationService") instanceof ITransformationService) {
				ITransformationService iTransformationService = (ITransformationService) applicationContext
						.getAttribute("ITransformationService");
				iTransformationService.clearAllXSLTemplates();
			}
			// servletContext.setAttribute("ITransformationService",(ITransformationService)this)
			oldFile.delete();
		} catch (Exception e) {
			oldFile.renameTo(origFile);
			e.printStackTrace();
		}

	}

	void createDirForFile(File file) {
		File childfile = file;
		while (!childfile.getParentFile().exists()) {
			if (childfile.getParentFile().isFile())
				continue;
			childfile.getParentFile().mkdir();
			childfile = childfile.getParentFile();
		}
	}

	public void addShopWithExtract_ru_addcontent(AuthorizationPageBean AuthorizationPageBeanId, String product_id,
			ServletContext applicationContext) {
	    logger.entry(AuthorizationPageBeanId,product_id);
	    TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		String file_name = "";
		String file_path = "";
		String query = "";
		try {
			
			if (!AuthorizationPageBeanId.getUser_site().equals("-1")) {
				query = "select   file.name , file.path from soft  LEFT  JOIN file  ON soft.file_id = file.file_id  where soft_id = "
						+ product_id;
				List<Map<String,Object>> listForFile = transactionSupport.getJdbcTemplate().queryForList(query);
				file_name = (String)listForFile.get(0).get("file.name");
				file_path = (String)listForFile.get(0).get("file.path");
				if (!file_path.startsWith("/"))
					file_path = "/" + file_path;
				this.site_id = AuthorizationPageBeanId.getUser_site();
				cretareSiteDirWithExtract(file_name, file_path, site_dir, applicationContext);
				return;
			}

			query = site_sequence_rs;
			
			site_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ?  , ? , ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(site_id),Long.valueOf(AuthorizationPageBeanId.getIntUserID()),host,home_dir,site_dir,person,phone,address,true);

		
			long intUserID = Long.parseLong(transactionSupport.getJdbcTemplate().queryForObject(tuser_sequence_rs, String.class));
			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id, E_MAIL , COMPANY) "
					+ " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? ) ";

	         transactionSupport.getJdbcTemplate().update(query,intUserID,login,passwd,new Date(),true,true,new Date(),SiteRole.ADMINISTRATOR_ID,0, Long.valueOf(AuthorizationPageBeanId.getCountry_id())
	             ,Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id),AuthorizationPageBeanId.getStrEMail(),AuthorizationPageBeanId.getStrCompany());


			account_id = (String) transactionSupport.getJdbcTemplate().queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ?  )";

	         transactionSupport.getJdbcTemplate().update(query,Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(), "new_account",
	             Long.valueOf(AuthorizationPageBeanId.getCountry_id()));

			intUserID = Long.parseLong(transactionSupport.getJdbcTemplate().queryForObject(tuser_sequence_rs, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			 transactionSupport.getJdbcTemplate().update(query,intUserID,SiteRole.GUEST,SiteRole.GUEST_PASSWORD,new Date(), true,true,new Date(),SiteRole.GUEST_ROLE_ID,0,
                 Long.valueOf(AuthorizationPageBeanId.getCountry_id()),Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id));

			account_id = (String) transactionSupport.getJdbcTemplate().queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));

			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "
					+ " values ( ? , ? , ? , ? ,  ? , ? , ? ) ; ";

	         transactionSupport.getJdbcTemplate().update(query,84473,intUserID,"HCO-CENTE-406","91KiBFRtE8fF7VHc8tvr",1,Long.valueOf(site_id),new Date());


			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ";

	          transactionSupport.getJdbcTemplate().update(query, SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),"РќРѕРІРѕСЃС‚Рё",true,1,SpecialCatalog.ROOT_CATALOG);


			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ; ";

            transactionSupport.getJdbcTemplate().update(query, SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),"Р“Р»Р°РІРЅР°СЏ СЃС‚СЂР°РЅРёС†Р°",
                true,1,SpecialCatalog.ROOT_CATALOG);


			String catalog_id = "";
			String parent_catalog_id = "";
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  )";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»1",
                true,1,SpecialCatalog.ROOT_CATALOG);

			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»1",
                true,1,Long.valueOf(parent_catalog_id));

			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?)";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»2",
                true,1,SpecialCatalog.ROOT_CATALOG);

			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»2",
                true,1,Long.valueOf(parent_catalog_id));

			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  )";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»3",
                true,1,SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»3",
                true,1,Long.valueOf(parent_catalog_id));

			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»4",
                true,1,SpecialCatalog.ROOT_CATALOG);

			catalog_id =transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»4",
                true,1,Long.valueOf(parent_catalog_id));

			
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»5",
                true,1,SpecialCatalog.ROOT_CATALOG);

			parent_catalog_id = catalog_id;
			catalog_id =  transactionSupport.getJdbcTemplate().queryForObject(tcatalog_sequence_rs, String.class);


			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»5",
                true,1,Long.valueOf(parent_catalog_id));
			String creteria_id = "";
			query = "SELECT MIN(CRETERIA1_ID) - 1  as ID FROM creteria1";

			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ "VALUES(? , ? , ? , ? , ? , ? , ? )";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");
             

			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? , ? , ? , ? , ? ,? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РўРµСЃС‚1",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");

			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? ,? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РўРµСЃС‚2",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");


			query = "SELECT MIN(CRETERIA2_ID) - 1  as ID FROM creteria2";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria2 (CRETERIA2_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№2");


			query = "SELECT MIN(CRETERIA3_ID) - 1  as ID FROM creteria3";
	
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			// creteria_id = "-".concat(creteria_id);
			query = "INSERT INTO creteria3 (CRETERIA3_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? , ? , ? , ? , ? , ?)";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№3");

			query = "SELECT MIN(CRETERIA4_ID) - 1  as ID FROM creteria4";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			// creteria_id = "-".concat(creteria_id);
			query = "INSERT INTO creteria4 (CRETERIA4_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№4");

			query = "SELECT MIN(CRETERIA5_ID) - 1  as ID FROM creteria5";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria5 (CRETERIA5_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№5");

			query = "SELECT MIN(CRETERIA6_ID) - 1  as ID FROM creteria6";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria6 (CRETERIA6_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№6");


			query = "SELECT MIN(CRETERIA7_ID) - 1  as ID FROM creteria7";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria7 (CRETERIA7_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№7");

			query = "SELECT MIN(CRETERIA8_ID) - 1  as ID FROM creteria8";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria8 (CRETERIA8_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№8");

			query = "SELECT MIN(CRETERIA9_ID) - 1  as ID FROM creteria9";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria9 (CRETERIA9_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№9");

			query = "SELECT MIN(CRETERIA10_ID) - 1  as ID FROM creteria10";
			creteria_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria10 (CRETERIA10_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№10");

			query = soft_sequence_rs;
			String soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);;

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			String name = " РџРѕРёСЃРєРѕРІР°СЏ СЃРёСЃС‚РµР�?Р° РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР°  ";
			String description=
					"<r>РџРѕРёСЃРєРѕРІС‹Рµ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚Рё РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР°</r>";
			String fulldescription = "<r>1. Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ CMS Business One, РІР°Р�? РЅСѓР¶РЅРѕ РЅР°СЃС‚СЂРѕРёС‚СЊ СЂР°СЃС€РёСЂРµРЅРЅС‹Р№ РїРѕРёСЃРє РїРѕ РєСЂРёС‚РµСЂРёСЏР�?</r>"
							+ "<r> РїРѕРґ СЃРІРѕРё Р·Р°РґР°С‡Рё. Р­С‚Рѕ РїРѕР·РІРѕР»РёС‚ РІР°С€РёР�? РєР»РёРµРЅС‚Р°Р�? СЃСЂР°Р·Сѓ РЅР°С…РѕРґРёС‚СЊ РЅСѓР¶РЅС‹Р№ С‚РѕРІР°СЂ, РёРЅС„РѕСЂР�?Р°С†РёСЋ РёР»Рё РґРѕРєСѓР�?РµРЅС‚.</r>"
							+ "<r>2. РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ РЅР°СЃС‚СЂР°РёРІР°С‚СЊ РЅРµ РЅСѓР¶РЅРѕ, РѕРЅ СЂР°Р±РѕС‚Р°РµС‚ РїРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ.</r>"
							+ "<r>3. РџРѕРёСЃРє РїРѕ СЂСѓР±СЂРёРєР°С‚РѕСЂСѓ (С‚РѕР»СЊРєРѕ Сѓ Р·Р°СЂРµРіРёСЃС‚СЂРёСЂРѕРІР°РЅРЅС‹С… РїРѕР»СЊР·РѕРІР°С‚РµР»РµР№) РЅР°СЃС‚СЂР°РёРІР°С‚СЊ РЅРµ РЅСѓР¶РЅРѕ, РѕРЅ СЂР°Р±РѕС‚Р°РµС‚ РїРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ.</r>"
							+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РЅР°СЃС‚СЂРѕР№РєРµ СЂР°СЃС€РёСЂРµРЅРЅРѕРіРѕ РїРѕРёСЃРєР° РїРѕ РєСЂРёС‚РµСЂРёСЏР�?</r>"
							+ "<r>РќР°С€РµР№ СЃРёСЃС‚РµР�?РѕР№ РїСЂРµРґСѓСЃР�?РѕС‚СЂРµРЅРѕ 10 СЂРµРґР°РєС‚РёСЂСѓРµР�?С‹С… РєСЂРёС‚РµСЂРёРµРІ Рё РїРѕРёСЃРє РїРѕ С†РµРЅРµ</r>"
							+ "<r>Р РµРґР°РєС‚РёСЂРѕРІР°РЅРёРµ РєСЂРёС‚РµСЂРёРµРІ РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ РІ С„РѕСЂР�?Рµ РґРѕР±Р°РІР»РµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІ РїРѕРґСЂР°Р·РґРµР»Рµ РЈРЎРўРђРќРћР’РљРђ РљР Р�РўР•Р Р�Р•Р’ Р”Р›РЇ РџРћР�РЎРљРђ Р­РўРћР™ Р�РќР¤РћР РњРђР¦Р�Р� РќРђ РЎРђР™РўР•.</r>"
							+ "<r>Р”Р»СЏ РѕС‚РєСЂС‹С‚РёСЏ СЌС‚РѕРіРѕ РїРѕРґСЂР°Р·РґРµР»Р° РЅР°Р¶Р�?РёС‚Рµ РїР»СЋСЃРёРє РїРµСЂРµРґ Р·Р°РіРѕР»РѕРІРєРѕР�?, Р° РґР»СЏ С‚РѕРіРѕ С‡С‚РѕР±С‹ СЃРІРµСЂРЅСѓС‚СЊ С„РѕСЂР�?Сѓ РЅР°Р¶Р�?РёС‚Рµ Р�?РёРЅСѓСЃРёРє РїРµСЂРµРґ Р·Р°РіРѕР»РѕРІРєРѕР�?.</r>"
							+ "<r>РљСЂРёС‚РµСЂРёРё Р�?РѕРіСѓС‚ Р±С‹С‚СЊ РЅРµР·Р°РІРёСЃРёР�?С‹Р�?Рё. РќР°РїСЂРёР�?РµСЂ: РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ, Р’РёРґ С‚РµС…РЅРёРєРё, Р¦РІРµС‚ - РІСЃРµ СЌС‚Рё РєСЂРёС‚РµСЂРёРё РЅРµР·Р°РІРёСЃРёР�?С‹ РґСЂСѓРі РѕС‚ РґСЂСѓРіР°. </r>"
							+ "<r>Рђ Р�?РѕРіСѓС‚ Р±С‹С‚СЊ Р·Р°РІРёСЃРёР�?С‹Р�?Рё РѕС‚ РєР°РєРѕРіРѕ-С‚Рѕ РєСЂРёС‚РµСЂРёСЏ. РќР°РїСЂРёР�?РµСЂ: РЎС‚СЂР°РЅР°-Р“РѕСЂРѕРґ-Р Р°Р№РѕРЅ. Р—РґРµСЃСЊ РєСЂРёС‚РµСЂРёР№ РЎС‚СЂР°РЅР° - РЅРµР·Р°РІРёСЃРёР�?С‹Р№, РєСЂРёС‚РµСЂРёР№ Р“РѕСЂРѕРґ Р·Р°РІРёСЃРёС‚ РѕС‚ РєСЂРёС‚РµСЂРёСЏ РЎС‚СЂР°РЅР°, Р° РєСЂРёС‚РµСЂРёР№ Р Р°Р№РѕРЅ Р·Р°РІРёСЃРёС‚ РѕС‚ РєСЂРёС‚РµСЂРёСЏ Р“РѕСЂРѕРґ.</r>"
							+ "<r></r>"
							+ "<r>Р—Р°РІРёСЃРёР�?РѕСЃС‚СЊ РєСЂРёС‚РµСЂРёСЏ, РєРѕС‚РѕСЂС‹Р№ РІС‹ РёР·Р�?РµРЅСЏРµС‚Рµ, РѕРїСЂРµРґРµР»СЏРµС‚СЃСЏ РіРѕСЂРёР·РѕРЅС‚Р°Р»СЊРЅРѕР№ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№. Р�Р·Р�?РµРЅСЏРµР�?С‹Р№ РєСЂРёС‚РµСЂРёР№ Р·Р°РІРёСЃРёС‚ РѕС‚ РєСЂРёС‚РµСЂРёСЏ, РІС‹РґРµР»РµРЅРЅРѕРіРѕ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№.</r>"
							+ "<r>Р•СЃР»Рё РІС‹ РёР·Р�?РµРЅСЏРµС‚Рµ РєСЂРёС‚РµСЂРёР№, РІС‹РґРµР»РµРЅС‹Р№ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРѕР№, С‚Рѕ СЌС‚РѕС‚ РєСЂРёС‚РµСЂРёР№ Р·Р°РІРёСЃРёС‚ СЃР°Р�? РѕС‚ СЃРµР±СЏ, С‚.Рµ. РѕРЅ РЅРµР·Р°РІРёСЃРёР�?С‹Р№.</r>"
							+ "<r>РџРѕС€Р°РіРѕРІРѕРµ РѕРїРёСЃР°РЅРёРµ РІРІРѕРґР° РЅРµР·Р°РІРёСЃРёР�?РѕРіРѕ РєСЂРёС‚РµСЂРёСЏ:</r>"
							+ "<r>РџСЂРµРґРїРѕР»РѕР¶РёР�?, С‡С‚Рѕ РЅР°Р�? РЅСѓР¶РЅРѕ РІРІРµСЃС‚Рё РєСЂРёС‚РµСЂРёР№ РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ СЃ РµРіРѕ РїРѕР·РёС†РёСЏР�?Рё.</r>"
							+ "<r>1. Р’С‹РґРµР»СЏРµР�? РЅСѓР¶РЅС‹Р№ РєСЂРёС‚РµСЂРёР№ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№, РЅР°РїСЂРёР�?РµСЂ РљСЂРёС‚РµСЂРёР№1</r>"
							+ "<r>2. РќР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬. РћС‚РєСЂРѕРµС‚СЃСЏ С„РѕСЂР�?Р° РґР»СЏ РёР·Р�?РµРЅРµРЅРёСЏ РљСЂРёС‚РµСЂРёСЏ1.</r>"
							+ "<r>3. Р’ РІРµСЂС…РЅРµР№ С‡Р°СЃС‚Рё С„РѕСЂР�?С‹ Р�?РµРЅСЏРµР�? РЅР°Р·РІР°РЅРёРµ РљСЂРёС‚РµСЂРёР№1 РЅР° РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ Рё РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬. РўРµРїРµСЂСЊ РЅР°С€ РєСЂРёС‚РµСЂРёР№ РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ Р±СѓРґРµС‚ РЅР°Р·С‹РІР°С‚СЊСЃСЏ РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ.</r>"
							+ "<r>4. Р’ РЅРёР¶РЅРµР№ С‡Р°СЃС‚Рё С„РѕСЂР�?С‹ РІРёРґРёР�? РќРµ РІС‹Р±СЂР°РЅРѕ - СЌС‚Р° Р·Р°РїРёСЃСЊ РќР• Р Р•Р”РђРљРўР�Р РЈР•РўРЎРЇ Рё РќР• РЈР”РђР›РЇР•РўРЎРЇ.</r>"
							+ "<r>5. Р§С‚РѕР±С‹ РґРѕР±Р°РІРёС‚СЊ РїРѕР·РёС†РёСЋ РІ РєСЂРёС‚РµСЂРёР№, РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р”РћР�?РђР’Р�РўР¬. Р’ РѕС‚РєСЂС‹РІС€СѓСЋСЃСЏ С„РѕСЂР�?Сѓ РїРёС€РµР�? РЅСѓР¶РЅСѓСЋ РїРѕР·РёС†РёСЋ, РЅР°РїСЂРёР�?РµСЂ РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ1, Рё РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬. Р”СЂСѓРіРёРµ РїРѕР·РёС†РёРё Р·Р°РІРѕРґРёР�? Р°РЅР°Р»РѕРіРёС‡РЅРѕ.</r>"
							+ "<r>Р”Р»СЏ СѓРґР°Р»РµРЅРёСЏ РёР»Рё СЂРµРґР°РєС‚РёСЂРѕРІР°РЅРёСЏ РїРѕР·РёС†РёРё РєСЂРёС‚РµСЂРёСЏ РёСЃРїРѕР»СЊР·СѓР№С‚Рµ СЃРѕРѕС‚РІРµС‚СЃС‚РІРµРЅРЅС‹Рµ РєРЅРѕРїРєРё РЅР°РїСЂРѕС‚РёРІ РїРѕР·РёС†РёРё.</r>"
							+ "<r></r>"
							+ "<r>Р”СЂСѓРіРёРµ РЅРµР·Р°РІРёСЃРёР�?С‹Рµ РєСЂРёС‚РµСЂРёРё Р·Р°РІРѕРґРёР�? Р°РЅР°Р»РѕРіРёС‡РЅРѕ. РЎР�?РѕС‚СЂРё РїСѓРЅРєС‚С‹ 1-5</r>"
							+ "<r></r>"
							+ "<r>РџРѕС€Р°РіРѕРІРѕРµ РѕРїРёСЃР°РЅРёРµ РІРІРѕРґР° Р·Р°РІРёСЃРёР�?РѕРіРѕ РєСЂРёС‚РµСЂРёСЏ:</r>"
							+ "<r>РџСЂРµРґРїРѕР»РѕР¶РёР�?, С‡С‚Рѕ Сѓ РЅР°СЃ СѓР¶Рµ РІРІРµРґРµРЅ РЅРµР·Р°РІРёСЃРёР�?С‹Р№ РєСЂРёС‚РµСЂРёР№ РЎС‚СЂР°РЅР° СЃРѕ СЃРІРѕРёР�?Рё РїРѕР·РёС†РёСЏР�?Рё Рё РЅР°Р�? РЅСѓР¶РЅРѕ РІРІРµСЃС‚Рё Р·Р°РІРёСЃРёР�?С‹Р№ РєСЂРёС‚РµСЂРёР№ Р“РѕСЂРѕРґ.</r>"
							+ "<r>1. Р’С‹РґРµР»СЏРµР�? СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№ РєСЂРёС‚РµСЂРёР№, РѕС‚ РєРѕС‚РѕСЂРѕРіРѕ Р±СѓРґРµС‚ Р·Р°РІРёСЃРµС‚СЊ РЅР°С€ РєСЂРёС‚РµСЂРёР№ Р“РѕСЂРѕРґ, РІ РґР°РЅРЅРѕР�? СЃР»СѓС‡Р°Рµ РєСЂРёС‚РµСЂРёР№ РЎС‚СЂР°РЅР°.</r>"
							+ "<r>Р’С‹Р±РёСЂР°РµР�? РІ РєСЂРёС‚РµСЂРёРё РЎС‚СЂР°РЅР° РЅСѓР¶РЅСѓСЋ РїРѕР·РёС†РёСЋ, РЅР°РїСЂРёР�?РµСЂ РЎС‚СЂР°РЅР°1. </r>"
							+ "<r>2. РќР°РїСЂРѕС‚РёРІ РєСЂРёС‚РµСЂРёСЏ Р“РѕСЂРѕРґ РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬. РћС‚РєСЂРѕРµС‚СЃСЏ С„РѕСЂР�?Р° РґР»СЏ РёР·Р�?РµРЅРµРЅРёСЏ РєСЂРёС‚РµСЂРёСЏ.</r>"
							+ "<r>3. Р’ РІРµСЂС…РЅРµР№ С‡Р°СЃС‚Рё С„РѕСЂР�?С‹ Р�?РµРЅСЏРµР�? РЅР°Р·РІР°РЅРёРµ РєСЂРёС‚РµСЂРёСЏ РЅР° Р“РѕСЂРѕРґ Рё РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬.</r>"
							+ "<r>4. Р—Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Рµ РїРѕР·РёС†РёРё Р“РѕСЂРѕРґ1-1, Р“РѕСЂРѕРґ1-2 Рё С‚.Рґ., СЃРѕРѕС‚РІРµС‚СЃС‚РІСѓСЋС‰РёРµ РІС‹Р±СЂР°РЅРЅРѕР№ РїРѕР·РёС†РёРё РЎС‚СЂР°РЅР°1.</r>"
							+ "<r>Р•СЃР»Рё РЅСѓР¶РЅРѕ РїСЂРѕРґРѕР»Р¶РёС‚СЊ РІРІРѕРґ РіРѕСЂРѕРґРѕРІ РїРѕ РґСЂСѓРіРёР�? СЃС‚СЂР°РЅР°Р�?, С‚Рѕ РІРѕР·РІСЂР°С‰Р°РµР�?СЃСЏ Рє РєСЂРёС‚РµСЂРёСЋ РЎС‚СЂР°РЅР° Рё РІС‹Р±РёСЂР°РµР�? РїРѕР·РёС†РёСЋ РЎС‚СЂР°РЅР°2.</r>"
							+ "<r>Р”Р°Р»РµРµ РїРµСЂРµС…РѕРґРёР�? Рє РїСѓРЅРєС‚Сѓ 2 Рё С‚.Рґ.</r>" + "<r></r>"
							+ "<r>Р•СЃР»Рё Сѓ РІР°СЃ РєРѕР»РёС‡РµСЃС‚РІРѕ РєСЂРёС‚РµСЂРёРµРІ Р�?РµРЅСЊС€Рµ РґРµСЃСЏС‚Рё, С‚Рѕ РѕСЃС‚Р°РІС€РёРµСЃСЏ РєСЂРёС‚РµСЂРёРё Р�?РѕР¶РЅРѕ СЃРєСЂС‹С‚СЊ, РїСѓС‚РµР�? СѓРґР°Р»РµРЅРёСЏ РЅР°Р·РІР°РЅРёСЏ РєСЂРёС‚РµСЂРёСЏ.</r>";
		
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(soft_id),name,description,fulldescription,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
			soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);;
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			name= "Р¤РѕСЂСѓР�?";
			description= "<r> РћР±СЃСѓР¶РґРµРЅРёРµ С‚РѕРІР°СЂР°, СѓСЃР»СѓРіРё, РЅРѕРІРѕСЃС‚Рё </r>";
			fulldescription=  "<r>" + " РњС‹ РІСЃС‚СЂРѕРёР»Рё С„РѕСЂСѓР�? РІ РЅР°С€Сѓ CMS Business One.  </r>"
					+ "<r>"
					+ " Р”Р»СЏ РєР°Р¶РґРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ РёР»Рё РЅРѕРІРѕСЃС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїСЂРµРґСѓСЃР�?РѕС‚СЂРµРЅР° СЃРёСЃС‚РµР�?Р° РѕР±СЃСѓР¶РґРµРЅРёСЏ </r>"
					+ "<r>РЎРёСЃС‚РµР�?Сѓ РѕР±СЃСѓР¶РґРµРЅРёСЏ Р�?РѕР¶РЅРѕ РІРєР»СЋС‡РёС‚СЊ РёР»Рё РІС‹РєР»СЋС‡РёС‚СЊ РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ.  </r>"
					+ "<r>Р’СЃРµ РЅРѕРІС‹Рµ СЃРѕРѕР±С‰РµРЅРёСЏ СЃРѕР±РёСЂР°СЋС‚СЃСЏ РІ С‚РѕРї РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ РґР»СЏ РѕР±Р·РѕСЂР° СЃРІРµР¶РёС… РґРёСЃРєСѓСЃСЃРёР№</r>"
					+ "<r></r>"
					+ "<r>Р’РєР»СЋС‡РµРЅРёРµ С„РѕСЂСѓР�?Р° РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ РІ С„РѕСЂР�?Рµ РґРѕР±Р°РІР»РµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ РёР»Рё РЅРѕРІРѕСЃС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїСѓС‚РµР�? РІС‹Р±РѕСЂР° РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•.</r>"
					+ "<r>РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ С„РѕСЂСѓР�? РІС‹РєР»СЋС‡РµРЅ.</r>";
		
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(soft_id),name,description,fulldescription,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);
			query = soft_sequence_rs;
			soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			name =  " РќРѕРІРѕСЃС‚РЅРѕР№ Р�?РѕРґСѓР»СЊ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР°";
			description = "<r>РќРѕРІРѕСЃС‚РЅРѕР№ Р�?РѕРґСѓР»СЊ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР° </r>";
			fulldescription = "<r>"
					+ " РњС‹ СѓСЃС‚Р°РЅРѕРІРёР»Рё РЅРѕРІРѕСЃС‚РЅРѕР№ Р�?РѕРґСѓР»СЊ РІ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ  </r>"
					+ "<r>РќРѕРІРѕСЃРЅРѕР№ Р�?РѕРґСѓР»СЊ РѕС‚РѕР±СЂР°Р¶Р°РµС‚ С‚РµРєСЃС‚, РєР°СЂС‚РёРЅРєСѓ Рё РґР°С‚Сѓ РґРѕР±Р°РІР»РµРЅРёСЏ.</r>"
					+ "<r> РњРѕРґСѓР»СЊ РЅРѕРІРѕСЃС‚РµР№ РїРѕСЏРІР»СЏРµС‚СЃСЏ РЅР° РІСЃРµС… СЃС‚СЂР°РЅРёС†Р°С… РІРѕ РІСЃРµС… СЂР°Р·РґРµР»Р°С….</r>"
					+ "<r>Р’Р°С€Р° РЅРѕРІРѕСЃС‚СЊ РЅРµ РѕСЃС‚Р°РЅРµС‚СЃСЏ РЅРµР·Р°Р�?РµС‡РµРЅРЅРѕР№, РіРґРµ Р±С‹ РЅРµ РЅР°С…РѕРґРёР»СЃСЏ РІР°С€ РєР»РёРµРЅС‚.</r>"
					+ "<r></r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РЅРѕРІРѕСЃС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ:</r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? РќРћР’РћРЎРўР�.</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р’ РїРѕР»Рµ Р¦Р•РќРђ С†РµРЅСѓ РЅРµ РїСЂРѕСЃС‚Р°РІР»СЏС‚СЊ, РѕСЃС‚Р°РІРёС‚СЊ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ РїРѕ РЅРѕРІРѕСЃС‚Рё.</r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ РЅРѕРІРѕСЃС‚Рё.</r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹.</r>"
					+ "<r>Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµР№ РЅРѕРІРѕСЃС‚Рё РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµР№ РЅРѕРІРѕСЃС‚Рё РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР® РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ СѓСЃС‚Р°РЅРѕРІР»РµРЅРѕ Р·РЅР°С‡РµРЅРёРµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(soft_id),name,description,fulldescription,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);


            query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			name = " Р РµРєР»Р°Р�?Р°  ";
			description=  "<r> Р�?Р»РѕРєРё СЂРµРєР»Р°Р�?С‹ РІ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅРµ </r>";
			fulldescription = "<r>"
					+ "РњС‹ РІСЃС‚СЂРѕРёР»Рё РґР»СЏ РІР°СЃ Р±Р»РѕРєРё СЂРµРєР»Р°Р�?С‹, С‡С‚РѕР±С‹ РІС‹ Р�?РѕРіР»Рё РїСЂРѕРґР°РІР°С‚СЊ СЂРµРєР»Р°Р�?РЅРѕРµ Р�?РµСЃС‚Рѕ РІ РІР°С€РµР�? РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅРµ.</r>"
					+ "<r> РЎРїСЂР°РІР° Рё СЃР»РµРІР° РѕС‚ С†РµРЅС‚СЂР°Р»СЊРЅРѕРіРѕ Р±Р»РѕРєР° РїСЂРµРґСѓСЃР�?РѕС‚СЂРµРЅС‹ СЂРµРєР»Р°Р�?РЅС‹Рµ Р�?РѕРґСѓР»Рё</r>";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(soft_id),name,description,fulldescription,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

            query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			name =  " Р�РЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ ";
			description =  "<r> РЎСЃС‹Р»РєР° РЅР° РІР°С€ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ</r>";
			fulldescription = ""
					+ "<r> Р’Р°С€ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ РЅР°С…РѕРґРёС‚СЃСЏ РїРѕ Р°РґСЂРµСЃСѓ http://www.siteforyou.com/Productlist.jsp?site="
					+ site_id + "  </r>" + "<r> Р’Р°С€ РїРѕС‡С‚РѕРІС‹Р№ СЏС‰РёРє "
					+ AuthorizationPageBeanId.getStrLogin() + "@bossmail.me </r>"
					+ "<r> РЎРѕС…СЂР°РЅРёС‚Рµ РіРґРµ-РЅРёР±СѓРґСЊ СЌС‚Сѓ РёРЅС„РѕСЂР�?Р°С†РёСЋ.</r>";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(soft_id),name,description,fulldescription,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

            query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			HashMap<String,Object> args = new HashMap<String, Object>();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                  SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);
            query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE,Long.valueOf(site_id),AuthorizationPageBeanId.getLang_id(),true);

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р’РѕРїСЂРѕСЃС‹ Рё РѕС‚РІРµС‚С‹ РІР°С€ С„РѕСЂСѓР�?  ");
			args.put("description",
					"<r> Р’С‹ Р�?РѕР¶РµС‚Рµ РІРµСЃС‚Рё РґРёСЃРєСѓСЃРёРё РїРѕ С‚РѕРІР°СЂСѓ РёР»Рё РЅРѕРІРѕСЃС‚СЏР�? Рё РїСЂР°Р№СЃ Р»РёСЃС‚Сѓ.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE,AuthorizationPageBeanId.getLang_id());

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ  ");
			args.put("description",
					"<r> РЎРїР°СЃРёР±Рѕ РѕС‚ Center Business Solutions ltd.  С‡С‚Рѕ РІРѕСЃРїРѕР»СЊР·РѕРІР°Р»РёСЃСЊ РЅР°С€РµР№ CMS .</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE,AuthorizationPageBeanId.getLang_id());

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_LEFTTOP,10,10,AuthorizationPageBeanId.getLang_id());


			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ   ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_RIGHTTOP,10,10,AuthorizationPageBeanId.getLang_id());

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ   ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_RIGHTTOP,AuthorizationPageBeanId.getLang_id());

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ    ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_LEFTTOP,AuthorizationPageBeanId.getLang_id());


			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ    ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_RIGHTTOP,AuthorizationPageBeanId.getLang_id());

			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ   ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),true,Layout.PORTLET_TYPE_RIGHTTOP,AuthorizationPageBeanId.getLang_id());

//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёСЏ Рѕ РєРѕР�?РїР°РЅРёРё   ");
			args.put("description", "<r> Р�РЅС„РѕСЂР�?Р°С†РёСЏ Рѕ РєРѕР�?РїР°РЅРёРё.</r>");
			args.put("fulldescription",
					"<r>" + " РќР°Р·РІР°РЅРёРµ РєРѕР�?РїР°РЅРёРё: " + AuthorizationPageBeanId.getStrCompany() + "</r>"
							+ "<r> РўРµР»РµС„РѕРЅ: " + AuthorizationPageBeanId.getStrPhone() + " </r>"
							+ "<r> Р¤Р°РєСЃ: " + AuthorizationPageBeanId.getStrFax() + " </r>" + "<r> РџРѕС‡С‚Р°: "
							+ AuthorizationPageBeanId.getStrEMail() + " </r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.FOR_EXTERNAL_PAGE,Long.valueOf(site_id),true,Layout.PAGE_ABOUT_COMPANY,AuthorizationPageBeanId.getLang_id());

//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РљР°Рє РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р·   ");
			args.put("description", "<r> РљР°Рє РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р·.</r>");
			args.put("fulldescription", "<r>" + " РћРїР»Р°С‚Р° Р·Р°РєР°Р·Р° "
					+ AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Р Р°СЃС‡РµС‚ РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ РїСѓС‚РµР�? РїРµСЂРµРІРѕРґР° РґРµРЅРµР¶РЅС‹С… СЃСЂРµРґСЃС‚РІ РЅР° СЃС‡РµС‚ РєРѕР�?РїР°РЅРёРё С‡РµСЂРµР· РїР»Р°С‚РµР¶РЅС‹Рµ СЃРёСЃС‚РµР�?С‹ РёР»Рё С‡РµСЂРµР· Р±Р°РЅРє РїСЂСЏР�?С‹Р�? РїР»Р°С‚РµР¶РѕР�? </r>"
					+ "<r> РЎС‡РµС‚ Web Money: 1111222333444 </r>" + "<r> РЎС‡РµС‚ Yandex Money: 333442234455 </r>"
					+ "<r> Р�?Р°РЅРє РћРђРћ Р РѕРіР° Рё РєР°РїС‹С‚Р°  p/c 333334444 k/c 3335335522 </r>");
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),
                SpecialCatalog.FOR_EXTERNAL_PAGE,Long.valueOf(site_id),true,Layout.PAGE_ABOUT_PAY,AuthorizationPageBeanId.getLang_id());

			transactionSupport.commitTransaction(trxnStatus);

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		}
	}


	public void addShopWithExtract(AuthorizationPageBean AuthorizationPageBeanId, String product_id,
			ServletContext applicationContext) {
	  TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		String file_name = "";
		String file_path = "";
		String query = "";
		try {
			
			if (!AuthorizationPageBeanId.getUser_site().equals("-1")) {
				query = "select   file.name , file.path from soft  LEFT  JOIN file  ON soft.file_id = file.file_id  where soft_id = "
						+ product_id;
				List<Map<String,Object>> listForObject = transactionSupport.getJdbcTemplate().queryForList(query);
				file_name = (String)listForObject.get(0).get("file.name");
				file_path =  (String)listForObject.get(0).get("file.path");
				if (!file_path.startsWith("/"))
					file_path = "/" + file_path;
				// dbAdaptor.commit();
				this.site_id = AuthorizationPageBeanId.getUser_site();
				cretareSiteDirWithExtract(file_name, file_path, site_dir, applicationContext);
				return;
			}

			query = site_sequence_rs;
            
            site_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ?  , ? , ? ) ; ";

	         transactionSupport.getJdbcTemplate().update(query,Long.valueOf(site_id),Long.valueOf(AuthorizationPageBeanId.getIntUserID()),host,home_dir,site_dir,person,phone,address,true);
			query = tuser_sequence_rs;
			long intUserID = Long.parseLong((String) transactionSupport.getJdbcTemplate().queryForObject(query, String.class));
			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id, E_MAIL , COMPANY) "
					+ " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? ) ";

			transactionSupport.getJdbcTemplate().update(query,intUserID,login,passwd,new Date(),true,true,new Date(),SiteRole.ADMINISTRATOR_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()),Long.valueOf(site_id),
			    Long.valueOf(city_id),Long.valueOf(country_id),AuthorizationPageBeanId.getStrEMail(),AuthorizationPageBeanId.getStrCompany());
			account_id = (String) transactionSupport.getJdbcTemplate().queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ?  )";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));
			intUserID = Long.parseLong(transactionSupport.getJdbcTemplate().queryForObject(tuser_sequence_rs, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			transactionSupport.getJdbcTemplate().update(query,intUserID,SiteRole.GUEST,SiteRole.GUEST_PASSWORD,new Date(),true,true,new Date(),SiteRole.GUEST_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()),Long.valueOf(site_id),
                Long.valueOf(city_id),Long.valueOf(country_id));
			account_id = (String) transactionSupport.getJdbcTemplate().queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));

			// ==== end add users =========================

			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "
					+ " values ( ? , ? , ? , ? ,  ? , ? , ? ) ; ";

			transactionSupport.getJdbcTemplate().update(query,84473,intUserID,"HCO-CENTE-406","91KiBFRtE8fF7VHc8tvr",1,Long.valueOf(site_id),new Date());

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			transactionSupport.getJdbcTemplate().update(query,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id), "РќРѕРІРѕСЃС‚Рё",true,1,SpecialCatalog.ROOT_CATALOG);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ; ";

			transactionSupport.getJdbcTemplate().update(query,SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id), "Р“Р»Р°РІРЅР°СЏ СЃС‚СЂР°РЅРёС†Р°",true,1,SpecialCatalog.ROOT_CATALOG);

			String catalog_id = "";
			String parent_catalog_id = "";
			
			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  )";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»1",true,1,SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ";
	        transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»1",true,1,Long.valueOf(parent_catalog_id));
			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?)";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»2",true,1,SpecialCatalog.ROOT_CATALOG);

			query =tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»2",true,1,Long.valueOf(parent_catalog_id));


			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»3",true,1, SpecialCatalog.ROOT_CATALOG);


			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»3",true,1,Long.valueOf(parent_catalog_id));
			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»4",true,1, SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»4",true,1,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»5",true,1,SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
            catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»5",true,1,Long.valueOf(parent_catalog_id));
			String creteria_id = "";
			query = "SELECT MIN(CRETERIA1_ID) - 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ "VALUES(? , ? , ? , ? , ? , ? , ? )";
	         transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");


			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? , ? , ? , ? , ? ,? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РўРµСЃС‚1",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");

			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? ,? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РўРµСЃС‚2",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");

			query = "SELECT MIN(CRETERIA2_ID) - 1  as ID FROM creteria2";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria2 (CRETERIA2_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№2");

			query = "SELECT MIN(CRETERIA3_ID) - 1  as ID FROM creteria3";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria3 (CRETERIA3_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES(? , ? , ? , ? , ? , ? , ?)";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№3");

			query = "SELECT MIN(CRETERIA4_ID) - 1  as ID FROM creteria4";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria4 (CRETERIA4_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№4");

			query = "SELECT MIN(CRETERIA5_ID) - 1  as ID FROM creteria5";
			creteria_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria5 (CRETERIA5_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№5");

			query = "SELECT MIN(CRETERIA6_ID) - 1  as ID FROM creteria6";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria6 (CRETERIA6_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id), "РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№6");

			query = "SELECT MIN(CRETERIA7_ID) - 1  as ID FROM creteria7";
			creteria_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria7 (CRETERIA7_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№7");

			query = "SELECT MIN(CRETERIA8_ID) - 1  as ID FROM creteria8";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria8 (CRETERIA8_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№8");

			query = "SELECT MIN(CRETERIA9_ID) - 1  as ID FROM creteria9";
            creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria9 (CRETERIA9_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№9");

			query = "SELECT MIN(CRETERIA10_ID) - 1  as ID FROM creteria10";
            creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria10 (CRETERIA10_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "
					+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№10");
			query = soft_sequence_rs;
			String soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			HashMap<String,Object> args = new HashMap<String, Object>();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РџРѕРёСЃРєРѕРІР°СЏ СЃРёСЃС‚РµР�?Р° РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР°  ");
			args.put("description",
					"<r>РџРѕРёСЃРєРѕРІС‹Рµ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚Рё РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР°</r>");
			args.put("fulldescription",
					"<r>1. Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ CMS Business One, РІР°Р�? РЅСѓР¶РЅРѕ РЅР°СЃС‚СЂРѕРёС‚СЊ СЂР°СЃС€РёСЂРµРЅРЅС‹Р№ РїРѕРёСЃРє РїРѕ РєСЂРёС‚РµСЂРёСЏР�?</r>"
							+ "<r> РїРѕРґ СЃРІРѕРё Р·Р°РґР°С‡Рё. Р­С‚Рѕ РїРѕР·РІРѕР»РёС‚ РІР°С€РёР�? РєР»РёРµРЅС‚Р°Р�? СЃСЂР°Р·Сѓ РЅР°С…РѕРґРёС‚СЊ РЅСѓР¶РЅС‹Р№ С‚РѕРІР°СЂ, РёРЅС„РѕСЂР�?Р°С†РёСЋ РёР»Рё РґРѕРєСѓР�?РµРЅС‚.</r>"
							+ "<r>2. РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ РЅР°СЃС‚СЂР°РёРІР°С‚СЊ РЅРµ РЅСѓР¶РЅРѕ, РѕРЅ СЂР°Р±РѕС‚Р°РµС‚ РїРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ.</r>"
							+ "<r>3. РџРѕРёСЃРє РїРѕ СЂСѓР±СЂРёРєР°С‚РѕСЂСѓ (С‚РѕР»СЊРєРѕ Сѓ Р·Р°СЂРµРіРёСЃС‚СЂРёСЂРѕРІР°РЅРЅС‹С… РїРѕР»СЊР·РѕРІР°С‚РµР»РµР№) РЅР°СЃС‚СЂР°РёРІР°С‚СЊ РЅРµ РЅСѓР¶РЅРѕ, РѕРЅ СЂР°Р±РѕС‚Р°РµС‚ РїРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ.</r>"
							+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РЅР°СЃС‚СЂРѕР№РєРµ СЂР°СЃС€РёСЂРµРЅРЅРѕРіРѕ РїРѕРёСЃРєР° РїРѕ РєСЂРёС‚РµСЂРёСЏР�?</r>"
							+ "<r>РќР°С€РµР№ СЃРёСЃС‚РµР�?РѕР№ РїСЂРµРґСѓСЃР�?РѕС‚СЂРµРЅРѕ 10 СЂРµРґР°РєС‚РёСЂСѓРµР�?С‹С… РєСЂРёС‚РµСЂРёРµРІ Рё РїРѕРёСЃРє РїРѕ С†РµРЅРµ</r>"
							+ "<r>Р РµРґР°РєС‚РёСЂРѕРІР°РЅРёРµ РєСЂРёС‚РµСЂРёРµРІ РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ РІ С„РѕСЂР�?Рµ РґРѕР±Р°РІР»РµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІ РїРѕРґСЂР°Р·РґРµР»Рµ РЈРЎРўРђРќРћР’РљРђ РљР Р�РўР•Р Р�Р•Р’ Р”Р›РЇ РџРћР�РЎРљРђ Р­РўРћР™ Р�РќР¤РћР РњРђР¦Р�Р� РќРђ РЎРђР™РўР•.</r>"
							+ "<r>Р”Р»СЏ РѕС‚РєСЂС‹С‚РёСЏ СЌС‚РѕРіРѕ РїРѕРґСЂР°Р·РґРµР»Р° РЅР°Р¶Р�?РёС‚Рµ РїР»СЋСЃРёРє РїРµСЂРµРґ Р·Р°РіРѕР»РѕРІРєРѕР�?, Р° РґР»СЏ С‚РѕРіРѕ С‡С‚РѕР±С‹ СЃРІРµСЂРЅСѓС‚СЊ С„РѕСЂР�?Сѓ РЅР°Р¶Р�?РёС‚Рµ Р�?РёРЅСѓСЃРёРє РїРµСЂРµРґ Р·Р°РіРѕР»РѕРІРєРѕР�?.</r>"
							+ "<r>РљСЂРёС‚РµСЂРёРё Р�?РѕРіСѓС‚ Р±С‹С‚СЊ РЅРµР·Р°РІРёСЃРёР�?С‹Р�?Рё. РќР°РїСЂРёР�?РµСЂ: РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ, Р’РёРґ С‚РµС…РЅРёРєРё, Р¦РІРµС‚ - РІСЃРµ СЌС‚Рё РєСЂРёС‚РµСЂРёРё РЅРµР·Р°РІРёСЃРёР�?С‹ РґСЂСѓРі РѕС‚ РґСЂСѓРіР°. </r>"
							+ "<r>Рђ Р�?РѕРіСѓС‚ Р±С‹С‚СЊ Р·Р°РІРёСЃРёР�?С‹Р�?Рё РѕС‚ РєР°РєРѕРіРѕ-С‚Рѕ РєСЂРёС‚РµСЂРёСЏ. РќР°РїСЂРёР�?РµСЂ: РЎС‚СЂР°РЅР°-Р“РѕСЂРѕРґ-Р Р°Р№РѕРЅ. Р—РґРµСЃСЊ РєСЂРёС‚РµСЂРёР№ РЎС‚СЂР°РЅР° - РЅРµР·Р°РІРёСЃРёР�?С‹Р№, РєСЂРёС‚РµСЂРёР№ Р“РѕСЂРѕРґ Р·Р°РІРёСЃРёС‚ РѕС‚ РєСЂРёС‚РµСЂРёСЏ РЎС‚СЂР°РЅР°, Р° РєСЂРёС‚РµСЂРёР№ Р Р°Р№РѕРЅ Р·Р°РІРёСЃРёС‚ РѕС‚ РєСЂРёС‚РµСЂРёСЏ Р“РѕСЂРѕРґ.</r>"
							+ "<r></r>"
							+ "<r>Р—Р°РІРёСЃРёР�?РѕСЃС‚СЊ РєСЂРёС‚РµСЂРёСЏ, РєРѕС‚РѕСЂС‹Р№ РІС‹ РёР·Р�?РµРЅСЏРµС‚Рµ, РѕРїСЂРµРґРµР»СЏРµС‚СЃСЏ РіРѕСЂРёР·РѕРЅС‚Р°Р»СЊРЅРѕР№ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№. Р�Р·Р�?РµРЅСЏРµР�?С‹Р№ РєСЂРёС‚РµСЂРёР№ Р·Р°РІРёСЃРёС‚ РѕС‚ РєСЂРёС‚РµСЂРёСЏ, РІС‹РґРµР»РµРЅРЅРѕРіРѕ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№.</r>"
							+ "<r>Р•СЃР»Рё РІС‹ РёР·Р�?РµРЅСЏРµС‚Рµ РєСЂРёС‚РµСЂРёР№, РІС‹РґРµР»РµРЅС‹Р№ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРѕР№, С‚Рѕ СЌС‚РѕС‚ РєСЂРёС‚РµСЂРёР№ Р·Р°РІРёСЃРёС‚ СЃР°Р�? РѕС‚ СЃРµР±СЏ, С‚.Рµ. РѕРЅ РЅРµР·Р°РІРёСЃРёР�?С‹Р№.</r>"
							+ "<r>РџРѕС€Р°РіРѕРІРѕРµ РѕРїРёСЃР°РЅРёРµ РІРІРѕРґР° РЅРµР·Р°РІРёСЃРёР�?РѕРіРѕ РєСЂРёС‚РµСЂРёСЏ:</r>"
							+ "<r>РџСЂРµРґРїРѕР»РѕР¶РёР�?, С‡С‚Рѕ РЅР°Р�? РЅСѓР¶РЅРѕ РІРІРµСЃС‚Рё РєСЂРёС‚РµСЂРёР№ РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ СЃ РµРіРѕ РїРѕР·РёС†РёСЏР�?Рё.</r>"
							+ "<r>1. Р’С‹РґРµР»СЏРµР�? РЅСѓР¶РЅС‹Р№ РєСЂРёС‚РµСЂРёР№ СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№, РЅР°РїСЂРёР�?РµСЂ РљСЂРёС‚РµСЂРёР№1</r>"
							+ "<r>2. РќР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬. РћС‚РєСЂРѕРµС‚СЃСЏ С„РѕСЂР�?Р° РґР»СЏ РёР·Р�?РµРЅРµРЅРёСЏ РљСЂРёС‚РµСЂРёСЏ1.</r>"
							+ "<r>3. Р’ РІРµСЂС…РЅРµР№ С‡Р°СЃС‚Рё С„РѕСЂР�?С‹ Р�?РµРЅСЏРµР�? РЅР°Р·РІР°РЅРёРµ РљСЂРёС‚РµСЂРёР№1 РЅР° РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ Рё РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬. РўРµРїРµСЂСЊ РЅР°С€ РєСЂРёС‚РµСЂРёР№ РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ Р±СѓРґРµС‚ РЅР°Р·С‹РІР°С‚СЊСЃСЏ РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ.</r>"
							+ "<r>4. Р’ РЅРёР¶РЅРµР№ С‡Р°СЃС‚Рё С„РѕСЂР�?С‹ РІРёРґРёР�? РќРµ РІС‹Р±СЂР°РЅРѕ - СЌС‚Р° Р·Р°РїРёСЃСЊ РќР• Р Р•Р”РђРљРўР�Р РЈР•РўРЎРЇ Рё РќР• РЈР”РђР›РЇР•РўРЎРЇ.</r>"
							+ "<r>5. Р§С‚РѕР±С‹ РґРѕР±Р°РІРёС‚СЊ РїРѕР·РёС†РёСЋ РІ РєСЂРёС‚РµСЂРёР№, РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р”РћР�?РђР’Р�РўР¬. Р’ РѕС‚РєСЂС‹РІС€СѓСЋСЃСЏ С„РѕСЂР�?Сѓ РїРёС€РµР�? РЅСѓР¶РЅСѓСЋ РїРѕР·РёС†РёСЋ, РЅР°РїСЂРёР�?РµСЂ РџСЂРѕРёР·РІРѕРґРёС‚РµР»СЊ1, Рё РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬. Р”СЂСѓРіРёРµ РїРѕР·РёС†РёРё Р·Р°РІРѕРґРёР�? Р°РЅР°Р»РѕРіРёС‡РЅРѕ.</r>"
							+ "<r>Р”Р»СЏ СѓРґР°Р»РµРЅРёСЏ РёР»Рё СЂРµРґР°РєС‚РёСЂРѕРІР°РЅРёСЏ РїРѕР·РёС†РёРё РєСЂРёС‚РµСЂРёСЏ РёСЃРїРѕР»СЊР·СѓР№С‚Рµ СЃРѕРѕС‚РІРµС‚СЃС‚РІРµРЅРЅС‹Рµ РєРЅРѕРїРєРё РЅР°РїСЂРѕС‚РёРІ РїРѕР·РёС†РёРё.</r>"
							+ "<r></r>"
							+ "<r>Р”СЂСѓРіРёРµ РЅРµР·Р°РІРёСЃРёР�?С‹Рµ РєСЂРёС‚РµСЂРёРё Р·Р°РІРѕРґРёР�? Р°РЅР°Р»РѕРіРёС‡РЅРѕ. РЎР�?РѕС‚СЂРё РїСѓРЅРєС‚С‹ 1-5</r>"
							+ "<r></r>"
							+ "<r>РџРѕС€Р°РіРѕРІРѕРµ РѕРїРёСЃР°РЅРёРµ РІРІРѕРґР° Р·Р°РІРёСЃРёР�?РѕРіРѕ РєСЂРёС‚РµСЂРёСЏ:</r>"
							+ "<r>РџСЂРµРґРїРѕР»РѕР¶РёР�?, С‡С‚Рѕ Сѓ РЅР°СЃ СѓР¶Рµ РІРІРµРґРµРЅ РЅРµР·Р°РІРёСЃРёР�?С‹Р№ РєСЂРёС‚РµСЂРёР№ РЎС‚СЂР°РЅР° СЃРѕ СЃРІРѕРёР�?Рё РїРѕР·РёС†РёСЏР�?Рё Рё РЅР°Р�? РЅСѓР¶РЅРѕ РІРІРµСЃС‚Рё Р·Р°РІРёСЃРёР�?С‹Р№ РєСЂРёС‚РµСЂРёР№ Р“РѕСЂРѕРґ.</r>"
							+ "<r>1. Р’С‹РґРµР»СЏРµР�? СЂРѕР·РѕРІРѕР№ РїРѕР»РѕСЃРєРѕР№ РєСЂРёС‚РµСЂРёР№, РѕС‚ РєРѕС‚РѕСЂРѕРіРѕ Р±СѓРґРµС‚ Р·Р°РІРёСЃРµС‚СЊ РЅР°С€ РєСЂРёС‚РµСЂРёР№ Р“РѕСЂРѕРґ, РІ РґР°РЅРЅРѕР�? СЃР»СѓС‡Р°Рµ РєСЂРёС‚РµСЂРёР№ РЎС‚СЂР°РЅР°.</r>"
							+ "<r>Р’С‹Р±РёСЂР°РµР�? РІ РєСЂРёС‚РµСЂРёРё РЎС‚СЂР°РЅР° РЅСѓР¶РЅСѓСЋ РїРѕР·РёС†РёСЋ, РЅР°РїСЂРёР�?РµСЂ РЎС‚СЂР°РЅР°1. </r>"
							+ "<r>2. РќР°РїСЂРѕС‚РёРІ РєСЂРёС‚РµСЂРёСЏ Р“РѕСЂРѕРґ РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬. РћС‚РєСЂРѕРµС‚СЃСЏ С„РѕСЂР�?Р° РґР»СЏ РёР·Р�?РµРЅРµРЅРёСЏ РєСЂРёС‚РµСЂРёСЏ.</r>"
							+ "<r>3. Р’ РІРµСЂС…РЅРµР№ С‡Р°СЃС‚Рё С„РѕСЂР�?С‹ Р�?РµРЅСЏРµР�? РЅР°Р·РІР°РЅРёРµ РєСЂРёС‚РµСЂРёСЏ РЅР° Р“РѕСЂРѕРґ Рё РЅР°Р¶РёР�?Р°РµР�? РєРЅРѕРїРєСѓ Р�Р—РњР•РќР�РўР¬.</r>"
							+ "<r>4. Р—Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Рµ РїРѕР·РёС†РёРё Р“РѕСЂРѕРґ1-1, Р“РѕСЂРѕРґ1-2 Рё С‚.Рґ., СЃРѕРѕС‚РІРµС‚СЃС‚РІСѓСЋС‰РёРµ РІС‹Р±СЂР°РЅРЅРѕР№ РїРѕР·РёС†РёРё РЎС‚СЂР°РЅР°1.</r>"
							+ "<r>Р•СЃР»Рё РЅСѓР¶РЅРѕ РїСЂРѕРґРѕР»Р¶РёС‚СЊ РІРІРѕРґ РіРѕСЂРѕРґРѕРІ РїРѕ РґСЂСѓРіРёР�? СЃС‚СЂР°РЅР°Р�?, С‚Рѕ РІРѕР·РІСЂР°С‰Р°РµР�?СЃСЏ Рє РєСЂРёС‚РµСЂРёСЋ РЎС‚СЂР°РЅР° Рё РІС‹Р±РёСЂР°РµР�? РїРѕР·РёС†РёСЋ РЎС‚СЂР°РЅР°2.</r>"
							+ "<r>Р”Р°Р»РµРµ РїРµСЂРµС…РѕРґРёР�? Рє РїСѓРЅРєС‚Сѓ 2 Рё С‚.Рґ.</r>" + "<r></r>"
							+ "<r>Р•СЃР»Рё Сѓ РІР°СЃ РєРѕР»РёС‡РµСЃС‚РІРѕ РєСЂРёС‚РµСЂРёРµРІ Р�?РµРЅСЊС€Рµ РґРµСЃСЏС‚Рё, С‚Рѕ РѕСЃС‚Р°РІС€РёРµСЃСЏ РєСЂРёС‚РµСЂРёРё Р�?РѕР¶РЅРѕ СЃРєСЂС‹С‚СЊ, РїСѓС‚РµР�? СѓРґР°Р»РµРЅРёСЏ РЅР°Р·РІР°РЅРёСЏ РєСЂРёС‚РµСЂРёСЏ.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("lang_id"),args.get("active"));
			query = soft_sequence_rs;
			soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р¤РѕСЂСѓР�?");
			args.put("description", "<r> РћР±СЃСѓР¶РґРµРЅРёРµ С‚РѕРІР°СЂР°, СѓСЃР»СѓРіРё, РЅРѕРІРѕСЃС‚Рё </r>");
			args.put("fulldescription", "<r>" + " РњС‹ РІСЃС‚СЂРѕРёР»Рё С„РѕСЂСѓР�? РІ РЅР°С€Сѓ CMS Business One.  </r>"
					+ "<r>"
					+ " Р”Р»СЏ РєР°Р¶РґРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ РёР»Рё РЅРѕРІРѕСЃС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїСЂРµРґСѓСЃР�?РѕС‚СЂРµРЅР° СЃРёСЃС‚РµР�?Р° РѕР±СЃСѓР¶РґРµРЅРёСЏ </r>"
					+ "<r>РЎРёСЃС‚РµР�?Сѓ РѕР±СЃСѓР¶РґРµРЅРёСЏ Р�?РѕР¶РЅРѕ РІРєР»СЋС‡РёС‚СЊ РёР»Рё РІС‹РєР»СЋС‡РёС‚СЊ РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ.  </r>"
					+ "<r>Р’СЃРµ РЅРѕРІС‹Рµ СЃРѕРѕР±С‰РµРЅРёСЏ СЃРѕР±РёСЂР°СЋС‚СЃСЏ РІ С‚РѕРї РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ РґР»СЏ РѕР±Р·РѕСЂР° СЃРІРµР¶РёС… РґРёСЃРєСѓСЃСЃРёР№</r>"
					+ "<r></r>"
					+ "<r>Р’РєР»СЋС‡РµРЅРёРµ С„РѕСЂСѓР�?Р° РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ РІ С„РѕСЂР�?Рµ РґРѕР±Р°РІР»РµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ РёР»Рё РЅРѕРІРѕСЃС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїСѓС‚РµР�? РІС‹Р±РѕСЂР° РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•.</r>"
					+ "<r>РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ С„РѕСЂСѓР�? РІС‹РєР»СЋС‡РµРЅ.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РќРѕРІРѕСЃС‚РЅРѕР№ Р�?РѕРґСѓР»СЊ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР°");
			args.put("description", "<r>РќРѕРІРѕСЃС‚РЅРѕР№ Р�?РѕРґСѓР»СЊ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅР° </r>");
			args.put("fulldescription", "<r>"
					+ " РњС‹ СѓСЃС‚Р°РЅРѕРІРёР»Рё РЅРѕРІРѕСЃС‚РЅРѕР№ Р�?РѕРґСѓР»СЊ РІ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ  </r>"
					+ "<r>РќРѕРІРѕСЃРЅРѕР№ Р�?РѕРґСѓР»СЊ РѕС‚РѕР±СЂР°Р¶Р°РµС‚ С‚РµРєСЃС‚, РєР°СЂС‚РёРЅРєСѓ Рё РґР°С‚Сѓ РґРѕР±Р°РІР»РµРЅРёСЏ.</r>"
					+ "<r> РњРѕРґСѓР»СЊ РЅРѕРІРѕСЃС‚РµР№ РїРѕСЏРІР»СЏРµС‚СЃСЏ РЅР° РІСЃРµС… СЃС‚СЂР°РЅРёС†Р°С… РІРѕ РІСЃРµС… СЂР°Р·РґРµР»Р°С….</r>"
					+ "<r>Р’Р°С€Р° РЅРѕРІРѕСЃС‚СЊ РЅРµ РѕСЃС‚Р°РЅРµС‚СЃСЏ РЅРµР·Р°Р�?РµС‡РµРЅРЅРѕР№, РіРґРµ Р±С‹ РЅРµ РЅР°С…РѕРґРёР»СЃСЏ РІР°С€ РєР»РёРµРЅС‚.</r>"
					+ "<r></r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РЅРѕРІРѕСЃС‚РЅРѕРіРѕ Р�?РѕРґСѓР»СЏ:</r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? РќРћР’РћРЎРўР�.</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р’ РїРѕР»Рµ Р¦Р•РќРђ С†РµРЅСѓ РЅРµ РїСЂРѕСЃС‚Р°РІР»СЏС‚СЊ, РѕСЃС‚Р°РІРёС‚СЊ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ РїРѕ РЅРѕРІРѕСЃС‚Рё.</r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹.</r>"
					+ "<r>Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ РЅРѕРІРѕСЃС‚Рё.</r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹.</r>"
					+ "<r>Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµР№ РЅРѕРІРѕСЃС‚Рё РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµР№ РЅРѕРІРѕСЃС‚Рё РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР® РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ СѓСЃС‚Р°РЅРѕРІР»РµРЅРѕ Р·РЅР°С‡РµРЅРёРµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("lang_id"),args.get("active"));

            query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р РµРєР»Р°Р�?Р°  ");
			args.put("description", "<r> Р�?Р»РѕРєРё СЂРµРєР»Р°Р�?С‹ РІ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅРµ </r>");
			args.put("fulldescription", "<r>"
					+ "РњС‹ РІСЃС‚СЂРѕРёР»Рё РґР»СЏ РІР°СЃ Р±Р»РѕРєРё СЂРµРєР»Р°Р�?С‹, С‡С‚РѕР±С‹ РІС‹ Р�?РѕРіР»Рё РїСЂРѕРґР°РІР°С‚СЊ СЂРµРєР»Р°Р�?РЅРѕРµ Р�?РµСЃС‚Рѕ РІ РІР°С€РµР�? РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅРµ.</r>"
					+ "<r> РЎРїСЂР°РІР° Рё СЃР»РµРІР° РѕС‚ С†РµРЅС‚СЂР°Р»СЊРЅРѕРіРѕ Р±Р»РѕРєР° РїСЂРµРґСѓСЃР�?РѕС‚СЂРµРЅС‹ СЂРµРєР»Р°Р�?РЅС‹Рµ Р�?РѕРґСѓР»Рё</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("lang_id"),args.get("active"));

            query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ ");
			args.put("description", "<r> РЎСЃС‹Р»РєР° РЅР° РІР°С€ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ</r>");
			args.put("fulldescription", ""
					+ "<r> Р’Р°С€ РёРЅС‚РµСЂРЅРµС‚-Р�?Р°РіР°Р·РёРЅ РЅР°С…РѕРґРёС‚СЃСЏ РїРѕ Р°РґСЂРµСЃСѓ http://www.siteforyou.com/Productlist.jsp?site="
					+ site_id + "  </r>" + "<r> Р’Р°С€ РїРѕС‡С‚РѕРІС‹Р№ СЏС‰РёРє "
					+ AuthorizationPageBeanId.getStrLogin() + "@bossmail.me </r>"
					+ "<r> РЎРѕС…СЂР°РЅРёС‚Рµ РіРґРµ-РЅРёР±СѓРґСЊ СЌС‚Сѓ РёРЅС„РѕСЂР�?Р°С†РёСЋ.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("lang_id"),args.get("active"));
            query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(10));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                    args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));
            query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(11));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
		    transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

		    query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(12));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(13));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(14));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(15));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(16));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(17));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(18));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , cost , image_id , bigimage_id , file_id ,  currency ,  lang_id ,  active ) "
					+ " values (  ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("cost", Float.valueOf(19));
			args.put("image_id", Long.valueOf(-1));
			args.put("bigimage_id", Long.valueOf(-1));
			args.put("file_id", Long.valueOf(-1));
			args.put("currency", Long.valueOf(3));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("type_id"),
                args.get("site_id"),args.get("cost"),args.get("image_id"),args.get("bigimage_id"),args.get("file_id"),args.get("currency"),args.get("lang_id"),args.get("active"));


			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р’РѕРїСЂРѕСЃС‹ Рё РѕС‚РІРµС‚С‹ РІР°С€ С„РѕСЂСѓР�?  ");
			args.put("description",
					"<r> Р’С‹ Р�?РѕР¶РµС‚Рµ РІРµСЃС‚Рё РґРёСЃРєСѓСЃРёРё РїРѕ С‚РѕРІР°СЂСѓ РёР»Рё РЅРѕРІРѕСЃС‚СЏР�? Рё РїСЂР°Р№СЃ Р»РёСЃС‚Сѓ.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());

			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
			    args.get("portlettype_id"),args.get("lang_id"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ  ");
			args.put("description",
					"<r> РЎРїР°СЃРёР±Рѕ РѕС‚ Center Business Solutions ltd.  С‡С‚Рѕ РІРѕСЃРїРѕР»СЊР·РѕРІР°Р»РёСЃСЊ РЅР°С€РµР№ CMS .</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_LEFTTOP);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("image_id"),args.get("bigimage_id"),args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ   ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("image_id"),args.get("bigimage_id"),args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ   ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));

			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ    ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_LEFTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ    ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ   ");
			args.put("description",
					"<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>");
			args.put("fulldescription", "<r></r>"
					+ "<r>Р’ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕР�? Р�?РѕРґСѓР»Рµ Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµС‰Р°С‚СЊ С‚РµРєСЃС‚, РёР·РѕР±СЂР°Р¶РµРЅРёСЏ РЅР° С„РѕСЂР�?Р°С… СЃ РєСЂР°С‚РєРёР�? Рё РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?, РїСЂРёРєСЂРµРїР»СЏС‚СЊ С„Р°Р№Р»С‹, СѓСЃС‚Р°РЅР°РІР»РёРІР°С‚СЊ С†РµРЅСѓ, Р° С‚Р°РєР¶Рµ РІРєР»СЋС‡Р°С‚СЊ РіРѕР»РѕСЃРѕРІР°РЅРёРµ РёР»Рё С„РѕСЂСѓР�? РґР»СЏ РєР°Р¶РґРѕРіРѕ РєРѕРЅРєСЂРµС‚РЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ. </r>"
					+ "<r>Р�РЅСЃС‚СЂСѓРєС†РёСЏ РїРѕ РґРѕР±Р°РІР»РµРЅРёСЋ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ: </r>"
					+ "<r>1. Р—Р°Р№С‚Рё РІ РЈРџР РђР’Р›Р•РќР�Р• РЎРђР™РўРћРњ РЅР°Р¶Р°РІ РєРЅРѕРїРєСѓ РћР�?РќРћР’Р�РўР¬.</r>"
					+ "<r>2. РќР°Р¶Р°С‚СЊ Р”РћР�?РђР’Р�РўР¬ Р�РќР¤РћР РњРђР¦Р�РћРќРќР«Р™ РњРћР”РЈР›Р¬ РЎ РџРћР�РЎРљРћРњ РџРћ РљР Р�РўР•Р Р�РЇРњ.</r>"
					+ "<r>3. РќР°С…РѕРґРёР�? РїРѕРґСЂР°Р·РґРµР» Р¤РћР РњРђ Р”РћР�?РђР’Р›Р•РќР�РЇ Р�Р›Р� Р�Р—РњР•РќР•РќР�РЇ Р�РќР¤РћР РњРђР¦Р�Р�.</r>"
					+ "<r>4. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РїРёС€РµР�? Р·Р°РіРѕР»РѕРІРѕРє.</r>"
					+ "<r>5. Р’ РїРѕР»Рµ Р’Р«Р�?Р РђРќРќР«Р™ Р РђР—Р”Р•Р› РІС‹Р±РёСЂР°РµР�? СЂР°Р·РґРµР», РіРґРµ Р±СѓРґРµС‚ РґР°РЅРЅС‹Рё РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ. Р•СЃР»Рё С‚Р°РєРѕРіРѕ СЂР°Р·РґРµР»Р° РЅРµС‚, С‚Рѕ Р·Р°РІРѕРґРёР�? РЅСѓР¶РЅС‹Р№ СЂР°Р·РґРµР» РІ Р¤РћР РњР• Р”Р›РЇ РџРћРЎРўР РћР•РќР�РЇ РљРђРўРђР›РћР“Рђ (РїРѕРґСЂРѕР±РЅРѕ СЃР�?. РїРѕСЃС‚СЂРѕРµРЅРёРµ РєР°С‚Р°Р»РѕРіР°).</r>"
					+ "<r>6. Р’ РїРѕР»Рµ Р—РђР“РћР›РћР’РћРљ РќРђ Р”Р РЈР“РћРњ РЇР—Р«РљР• Р”Р›РЇ РџРћР�РЎРљРђ РЅРёС‡РµРіРѕ РІРІРѕРґРёС‚СЊ РЅРµ РЅР°РґРѕ. РќР°С…РѕРґРёС‚СЃСЏ РІ СЂР°Р·СЂР°Р±РѕС‚РєРµ.</r>"
					+ "<r>7. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РєСЂР°С‚РєРёР�? РѕРїРёСЃР°РЅРёРµР�?. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р”Р›РЇ РљР РђРўРљРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±.</r>"
					+ "<r>8. Р•СЃР»Рё РІС‹ РїСЂРѕСЃС‚Р°РІР»СЏРµС‚Рµ С†РµРЅСѓ РІ РїРѕР»Рµ Р¦Р•РќРђ, С‚Рѕ РѕРЅР° РѕС‚РѕР±СЂР°Р¶Р°РµС‚СЃСЏ. Р•СЃР»Рё РІР°Р�? С†РµРЅР° РЅРµ РЅСѓР¶РЅР°, С‚Рѕ РѕСЃС‚Р°РІР»СЏРµС‚Рµ РЅРѕР»СЊ.</r>"
					+ "<r>9. Р’ РїРѕР»Рµ РљР РђРўРљРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РєСЂР°С‚РєР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>10. Р’ РїРѕР»Рµ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 РґР°РµС‚СЃСЏ РІРѕР·Р�?РѕР¶РЅРѕСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РёР·РѕР±СЂР°Р¶РµРЅРёРµ РЅР° СЃС‚СЂР°РЅРёС†Сѓ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�?.Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р�?РѕР¶РЅРѕ РґРѕР±Р°РІРёС‚СЊ РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р”Р›РЇ РџРћР”Р РћР�?РќРћР“Рћ - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 Р�Р— Р�?РђР—Р« - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р�Р·РѕР±СЂР°Р¶РµРЅРёРµ Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±С‹С… С„РѕСЂР�?Р°С‚Р°С…, РєРѕС‚РѕСЂС‹Рµ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ РёР·РѕР±СЂР°Р¶РµРЅРёСЏ 1,5РњР±. </r>"
					+ "<r>11. Р’ РїРѕР»Рµ РџРћР”Р РћР�?РќРђРЇ Р�РќР¤РћР РњРђР¦Р�РЇ РїРёС€РµС‚СЃСЏ РїРѕРґСЂРѕР±РЅР°СЏ РёРЅС„РѕСЂР�?Р°С†РёСЏ С‚РѕРІР°СЂР° РёР»Рё СѓСЃР»СѓРіРё. </r>"
					+ "<r>12. Р’ РїРѕР»Рµ РџР Р�РљР Р•РџР�РўР¬ Р¤РђР™Р› РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїРёС‚СЊ С„Р°Р№Р» РґРІСѓР�?СЏ СЃРїРѕСЃРѕР±Р°Р�?Рё: Р—РђРљРђР§РђРўР¬ Р¤РђР™Р› - Р·Р°РєР°С‡Р°С‚СЊ СЃ РєРѕР�?РїСЊСЋС‚РµСЂР° РїРѕР»СЊР·РѕРІР°С‚РµР»СЏ, Р’Р«Р�?Р РђРўР¬ Р¤РђР™Р› - РІС‹Р±СЂР°С‚СЊ РёР· РёР�?РµСЋС‰РµР№СЃСЏ Р±Р°Р·С‹. Р¤Р°Р№Р» Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ РІ Р»СЋР±РѕР�? С„РѕСЂР�?Р°С‚Рµ, РєРѕС‚РѕСЂС‹Р№ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІР°С€ Р±СЂР°СѓР·РµСЂ. РњР°РєСЃРёР�?Р°Р»СЊРЅС‹Р№ СЂР°Р·Р�?РµСЂ С„Р°Р№Р»Р°. 1,5РњР±.</r>"
					+ "<r>13. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ Р“РћР›РћРЎРћР’РђРќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ РіРѕР»РѕСЃРѕРІР°РЅРёРµ. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°. </r>"
					+ "<r>14. РџСЂРё РІС‹Р±РѕСЂРµ РѕРїС†РёРё Р’РљР›Р®Р§Р�РўР¬ РћР�?РЎРЈР–Р”Р•РќР�Р•, Сѓ РІР°С€РµРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РїРѕСЏРІР»СЏРµС‚СЃСЏ С„РѕСЂСѓР�?. РџРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ СЌС‚Р° РѕРїС†РёСЏ РІС‹РєР»СЋС‡РµРЅР°.</r>"
					+ "<r>15. Р’ РѕРїС†РёРё РЈРўР’Р•Р Р–Р”РђР®, РµСЃР»Рё РІС‹Р±РёСЂР°РµС‚Рµ РћРџРЈР�?Р›Р�РљРћР’РђРќРћ, С‚Рѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ С‚РѕР»СЊРєРѕ РІ РєР°С‚Р°Р»РѕРіРµ. Р•СЃР»Рё РІС‹Р±РµСЂРµС‚Рµ РџРћРљРђР—Р«Р’РђРўР¬ РќРђ Р“Р›РђР’РќРћР™ РЎРўР РђРќР�Р¦Р•, С‚Рѕ СЌС‚РѕС‚ Р�?РѕРґСѓР»СЊ Р±СѓРґРµС‚ РѕС‚РѕР±СЂР°Р¶Р°С‚СЊСЃСЏ Рё РІ РєР°С‚Р°Р»РѕРіРµ Рё РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ.</r>"
					+ "<r>16. Р’РІРµРґРёС‚Рµ СЃ РєР°СЂС‚РёРЅРєРё РєРѕРґ.</r>"
					+ "<r>17. РќР°Р¶Р�?РёС‚Рµ РєРЅРѕРїРєСѓ РЎРћРҐР РђРќР�РўР¬.</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));
//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёСЏ Рѕ РєРѕР�?РїР°РЅРёРё   ");
			args.put("description", "<r> Р�РЅС„РѕСЂР�?Р°С†РёСЏ Рѕ РєРѕР�?РїР°РЅРёРё.</r>");
			args.put("fulldescription",
					"<r>" + " РќР°Р·РІР°РЅРёРµ РєРѕР�?РїР°РЅРёРё: " + AuthorizationPageBeanId.getStrCompany() + "</r>"
							+ "<r> РўРµР»РµС„РѕРЅ: " + AuthorizationPageBeanId.getStrPhone() + " </r>"
							+ "<r> Р¤Р°РєСЃ: " + AuthorizationPageBeanId.getStrFax() + " </r>" + "<r> РџРѕС‡С‚Р°: "
							+ AuthorizationPageBeanId.getStrEMail() + " </r>");
			args.put("catalog_id", SpecialCatalog.FOR_EXTERNAL_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PAGE_ABOUT_COMPANY);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));

//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РљР°Рє РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р·   ");
			args.put("description", "<r> РљР°Рє РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р·.</r>");
			args.put("fulldescription", "<r>" + " РћРїР»Р°С‚Р° Р·Р°РєР°Р·Р° "
					+ AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Р Р°СЃС‡РµС‚ РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ РїСѓС‚РµР�? РїРµСЂРµРІРѕРґР° РґРµРЅРµР¶РЅС‹С… СЃСЂРµРґСЃС‚РІ РЅР° СЃС‡РµС‚ РєРѕР�?РїР°РЅРёРё С‡РµСЂРµР· РїР»Р°С‚РµР¶РЅС‹Рµ СЃРёСЃС‚РµР�?С‹ РёР»Рё С‡РµСЂРµР· Р±Р°РЅРє РїСЂСЏР�?С‹Р�? РїР»Р°С‚РµР¶РѕР�? </r>"
					+ "<r> РЎС‡РµС‚ Web Money: 1111222333444 </r>" + "<r> РЎС‡РµС‚ Yandex Money: 333442234455 </r>"
					+ "<r> Р�?Р°РЅРє РћРђРћ Р РѕРіР° Рё РєР°РїС‹С‚Р°  p/c 333334444 k/c 3335335522 </r>");
			args.put("catalog_id", SpecialCatalog.FOR_EXTERNAL_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PAGE_ABOUT_PAY);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),
                args.get("portlettype_id"),args.get("lang_id"));

			query = "select   file.name , file.path from soft  LEFT  JOIN file  ON soft.file_id = file.file_id  where soft_id = "
					+ product_id;

			List<Map<String,Object>> queryForList = transactionSupport.getJdbcTemplate().queryForList(query);
			file_name = (String)queryForList.get(0).get("file.name");
			file_path = (String)queryForList.get(0).get("file.path");
			if (!file_path.startsWith("/"))
				file_path = "/" + file_path;
			transactionSupport.commitTransaction(trxnStatus);
			cretareSiteDirWithExtract(file_name, file_path, site_dir, applicationContext);

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		} 
	}

	public void addShopWithExtract_en(AuthorizationPageBean AuthorizationPageBeanId, String product_id,
			ServletContext applicationContext) {
	    TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		String file_name = "";
		String file_path = "";
		String query = "";
		try {
		    query = site_sequence_rs;          
            site_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ?  , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(site_id),Long.valueOf(AuthorizationPageBeanId.getIntUserID()),host,home_dir,site_dir,person,phone,address,true);
			query = tuser_sequence_rs;
			long intUserID = Long.parseLong(transactionSupport.getJdbcTemplate().queryForObject(query, String.class));
			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id, E_MAIL , COMPANY) "
					+ " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? ) ";
            transactionSupport.getJdbcTemplate().update(query,intUserID,login,passwd,new Date(),true,true,new Date(),SiteRole.ADMINISTRATOR_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id())
                ,Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id),AuthorizationPageBeanId.getStrEMail(),AuthorizationPageBeanId.getStrCompany());
			query = account_sequence_rs;
			account_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id ) "+ " values ( ? , ? , ? , ? , ? ,  ? ,  ?  )";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));

			// add anonymouse user
			query = tuser_sequence_rs;
			intUserID = Long.parseLong(transactionSupport.getJdbcTemplate().queryForObject(query, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			transactionSupport.getJdbcTemplate().update(query,intUserID,SiteRole.GUEST,SiteRole.GUEST_PASSWORD,new Date(),true,true,new Date(),SiteRole.GUEST_ROLE_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()),
			    Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id));

			query = account_sequence_rs;
			account_id =transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ? ) ; ";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));

			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "+ " values ( ? , ? , ? , ? ,  ? , ? , ? ) ; ";
			transactionSupport.getJdbcTemplate().update(query,84473,intUserID,"HCO-CENTE-406","91KiBFRtE8fF7VHc8tvr",1,Long.valueOf(site_id),new Date());

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			transactionSupport.getJdbcTemplate().update(query,SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),"News",true,2,SpecialCatalog.ROOT_CATALOG);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  ) ; ";
			transactionSupport.getJdbcTemplate().update(query,SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),"Main page",true,2,SpecialCatalog.ROOT_CATALOG);

			String catalog_id = "";
			String parent_catalog_id = "";
			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ?  )";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection1",true,2,SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id =transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection1",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection11",true,2,Long.valueOf(parent_catalog_id));

            query = tcatalog_sequence_rs;
            catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection111",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?)";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection2",true,2,SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection2",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection22",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection222",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection3",true,2,SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection3",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection33",true,2,Long.valueOf(parent_catalog_id));
			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection333",true,2,Long.valueOf(parent_catalog_id));
			query = tcatalog_sequence_rs;
			catalog_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection4",true,2,SpecialCatalog.ROOT_CATALOG);
			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection4",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection44",true,2,Long.valueOf(parent_catalog_id));

			query = tcatalog_sequence_rs;
			catalog_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection444",true,2,Long.valueOf(parent_catalog_id));
			String creteria_id = "";
			query = "SELECT MIN(CRETERIA1_ID) - 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ "VALUES(? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(catalog_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion1");
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? ,? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Test1",true,2,0,Long.valueOf(site_id),"Criterion1");

			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? ,? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Test2",true,2,0,Long.valueOf(site_id),"Criterion1");

			query = "SELECT MIN(CRETERIA2_ID) - 1  as ID FROM creteria2";
			creteria_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria2 (CRETERIA2_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion2");

			query = "SELECT MIN(CRETERIA3_ID) - 1  as ID FROM creteria3";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria3 (CRETERIA3_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ " VALUES(? , ? , ? , ? , ? , ? , ?)";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion3");
			query = "SELECT MIN(CRETERIA4_ID) - 1  as ID FROM creteria4";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria4 (CRETERIA4_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion4");

			query = "SELECT MIN(CRETERIA5_ID) - 1  as ID FROM creteria5";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria5 (CRETERIA5_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion5");
			query = "SELECT MIN(CRETERIA6_ID) - 1  as ID FROM creteria6";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria6 (CRETERIA6_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion6");
			query = "SELECT MIN(CRETERIA7_ID) - 1  as ID FROM creteria7";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria7 (CRETERIA7_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "	+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion7");
			query = "SELECT MIN(CRETERIA8_ID) - 1  as ID FROM creteria8";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria8 (CRETERIA8_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion8");
			query = "SELECT MIN(CRETERIA9_ID) - 1  as ID FROM creteria9";
			creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria9 (CRETERIA9_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion9");
			query = "SELECT MIN(CRETERIA10_ID) - 1  as ID FROM creteria10";
			creteria_id =  transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "INSERT INTO creteria10 (CRETERIA10_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            transactionSupport.getJdbcTemplate().update(query,Long.valueOf(creteria_id),"Not chosen",true,2,0,Long.valueOf(site_id),"Criterion10");
			query = soft_sequence_rs;
			String soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			HashMap<String,Object> args = new HashMap<String, Object>();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Search system of Internet shop");
			args.put("description", "<r> Search possibilities of Internet shop </r>");
			args.put("fulldescription",
					"<r> 1. For high-grade work CMS Business One, you need to adjust the expanded search in criteria </r>"
							+ "<r> under the problems. It will allow your clients to find at once the necessary goods, the information or the document. </r>"
							+ "<r> 2. It is not necessary to adjust search in the text, it works by default. </r>"
							+ "<r> 3. (Only at registered users) it is not necessary to adjust search in the subject authority, it works by default. </r>"
							+ "<r> the Instruction on adjustment of the expanded search on criteria </r>"
							+ "<r> Our system provides 10 edited criteria and search in the price </r>"
							+ "<r> Editing of criteria is made in the form of addition of the information module in subsection INSTALLATION of CRITERIA FOR SEARCH of THIS INFORMATION ON the SITE. </r>"
							+ "<r> For opening of this subsection press РїР»СЋСЃРёРє before heading and to curtail the form press Р�?РёРЅСѓСЃРёРє before heading. </r>"
							+ "<r> Criteria can be independent. For example: the Manufacturer, an engineering Kind, Colour - all these criteria are independent from each other. </r>"
							+ "<r> And can be dependent on any criterion. For example: the Country-city-area. Here the criterion the Country - independent, criterion the City depends on criterion the Country, and criterion Area the City depends on criterion. </r>"
							+ "<r> </r>"
							+ "<r> Dependence of criterion which you change, is advanced by a horizontal pink strip. The changeable criterion depends on the criterion allocated with a pink strip. </r>"
							+ "<r> If you change criterion, РІС‹РґРµР»РµРЅС‹Р№ a pink strip this criterion depends on itself, i.e. it independent. </r>"
							+ "<r> the Step-by-step description of input of independent criterion: </r>"
							+ "<r> we Will assume that we need to enter criterion the Manufacturer with its items. </r>"
							+ "<r> 1. We allocate the necessary criterion with a pink strip, for example Kriterij1 </r>"
							+ "<r> 2. We press the button to CHANGE. The form for change of Kriterija1 will open. </r>"
							+ "<r> 3. In the top part of the form we change the name Kriterij1 on the Manufacturer and we press the button to CHANGE. Now our criterion on main page will be called the Manufacturer. </r>"
							+ "<r> 4. In the bottom part of the form it is visible it is not chosen - this record is not edited and DOES NOT LEAVE. </r>"
							+ "<r> 5. To add an item in criterion, we press the button to ADD. To opened form we write the necessary item, for example Proizvoditel1, and we press the button to SAVE. Other items it is got similarly. </r>"
							+ "<r> For removal or editing of an item of criterion use respective buttons opposite to an item. </r>"
							+ "<r> </r>" + "<r> Other independent criteria it is got similarly. Look items 1-5 </r>"
							+ "<r> </r>" + "<r> the Step-by-step description of input of dependent criterion: </r>"
							+ "<r> we Will assume that at us the independent criterion the Country with the items is already entered and we need to enter dependent criterion the City. </r>"
							+ "<r> 1. We allocate with a pink strip criterion on which our criterion the City, in this case criterion the Country will depend. </r>"
							+ "<r> Strana1 Is chosen in criterion the Country the necessary item, for example. </r>"
							+ "<r> 2. Opposite to criterion the City we press the button to CHANGE. The form for criterion change will open. </r>"
							+ "<r> 3. In the top part of the form we change the criterion name for the City and we press the button to CHANGE. </r>"
							+ "<r> 4. We get the necessary items Gorod1-1, Gorod1-2 etc., corresponding to the chosen item Strana1. </r>"
							+ "<r> If it is necessary to continue input of cities on other countries we come back to criterion the Country and we choose an item Strana2. </r>"
							+ "<r> Further we pass to item 2 etc. </r>" + "<r> </r>"
							+ "<r> If at you quantity of criteria less than ten the remained criteria can be hidden, by removal of the name of criterion. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                  (Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));


			query = soft_sequence_rs;
			soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Forum");
			args.put("description", "<r> Discussion of the goods, service, news </r>");
			args.put("fulldescription", "<r>We have built in a forum ours CMS Business One. </r>"
					+ "<r>For each information or news module the discussion system </r>"
					+ "<r> the discussion System can be included or switched off for each concrete module. </r>"
					+ "<r> All new messages gather in С‚РѕРї on main page for the review of fresh discussions </r>"
					+ "<r> </r>"
					+ "<r> forum Inclusion is made in the form of addition of the information or news module by an option choice to INCLUDE DISCUSSION. </r>"
					+ "<r> By default the forum is switched off. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
			soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "News module of Internet shop");
			args.put("description", "<r> News module of Internet shop </r>");
			args.put("fulldescription", "<r>" + "We have established the news module in Internet shop </r>"
					+ "<r> Novosnoj the module displays the text, a picture and addition date. </r>"
					+ "<r> the Module of news appears on all pages in all sections. </r>"
					+ "<r> your news does not remain not noticed where there would be no your client. </r>" + "<r> </r>"
					+ "<r> the Instruction on addition of the news module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press to ADD the INFORMATION MODULE With SEARCH IN CRITERIA. </r>"
					+ "<r> 3. We find subsection the FORM of ADDITION OR INFORMATION CHANGE. </r>"
					+ "<r> 4. In the field HEADING we write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION we choose NEWS. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field Р�Р—РћР�?Р РђР–Р•РќР�Р•1 it is given the chance to add the image on page with the short description. </r>"
					+ "<r> the Image can be added in two methods: ZAKACHAT Р�Р—РћР�?Р РђР–Р•РќР�Р•1 FOR SHORT - Р·Р°РєР°С‡Р°С‚СЊ from the computer of the user, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 FROM BASE - to choose from present base. </r>"
					+ "<r> the Image Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ in any formats which are supported by your browser. The maximum size of the image 1,5РњР±. </r>"
					+ "<r> 8. In the field the PRICE the price not to mark, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written a brief information on news. </r>"
					+ "<r> 10. In the field Р�Р—РћР�?Р РђР–Р•РќР�Р•2 it is given the chance to add the image on page with detailed description. </r>"
					+ "<r> the Image can be added in two methods: ZAKACHAT Р�Р—РћР�?Р РђР–Р•РќР�Р•2 FOR DETAILED - Р·Р°РєР°С‡Р°С‚СЊ from the computer of the user, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 FROM BASE - to choose from present base. </r>"
					+ "<r> the Image Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ in any formats which are supported by your browser. The maximum size of the image 1,5РњР±. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of news. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: Р—РђРљРђР§РђРўР¬ the FILE - Р·Р°РєР°С‡Р°С‚СЊ from the computer of the user to CHOOSE the FILE - to choose from present base. </r>"
					+ "<r> the File Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ in any format which supports your browser. The maximum size of a file. 1,5РњР±. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your news has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your news has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE the significance should be established is published. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));

			query = soft_sequence_rs;
			soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Advertising");
			args.put("description", "<r> Advertising place in Internet shop </r>");
			args.put("fulldescription", "<r>"
					+ "We have built in for you advertising blocks that you could sell the space in your Internet shop. </r>"
					+ "<r> On the right and to the left of the central block advertising modules </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Internet shop");
			args.put("description", "<r>Reference to your Internet shop </r>");
			args.put("fulldescription",
					"" + "<r> your Internet shop is accessible  by link http://www.siteforyou.com/Productlist.jsp?site="
							+ site_id + "</r>" + "<r> also you can buy domain here </r>"
							+ "<r> Save somewhere this information. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "	+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "	+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "	+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(Boolean)args.get("active"));
			String tree_id = soft_id;
			query = soft_sequence_rs;
			soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , tree_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Questions and answers your forum ");
			args.put("description", "<r> You can talk about  goods or news and a price of sheet on your forum.</r>");
			args.put("fulldescription", "<r>" + "  The information module for main page 1." + "</r>"
					+ "<r> You need to change the contents of this module to place here  your information. </r>"
					+ "<r> For this you should enter on site under  your the password  </r>"
					+ "<r> So you can observe buttons:  remove, edit, update (To add the new module). </r>"
					+ "<r> In during  change of the informational unit you can put its other section .</r>"
					+ "<r> You also can edit ,  add, delete this section name. </r>"
					+ "<r> Also you can change parameters  criteria for search of  information  unit </r>"
					+ "<r> For this purpose should change criteria on the form of change of the information with title (Installation of criteria for information search on a site ). </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("tree_id", Long.valueOf(tree_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Long)args.get("tree_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , tree_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "+ " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " The informational modul  ");
			args.put("description", "<r> Thanks from Center Business Solutions Ltd  that  you using  this CMS .</r>");
			args.put("fulldescription", "<r> The information module for main page 1.</r>"
					+ "<r> You need to change the contents of this module to place here  your information. </r>"
					+ "<r> For this you should enter on site under  your the password  </r>"
					+ "<r> So you can observe buttons:  remove, edit, update (To add the new module). </r>"
					+ "<r> In during  change of the informational unit you can put its other section .</r>"
					+ "<r> You also can edit ,  add, delete this section name. </r>"
					+ "<r> Also you can change parameters  criteria for search of  information  unit </r>"
					+ "<r> For this purpose should change criteria on the form of change of the information with title (Installation of criteria for information search on a site ). </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("tree_id", Long.valueOf(tree_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Long)args.get("tree_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_LEFTTOP);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("image_id"),(int)args.get("bigimage_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("image_id"),(int)args.get("bigimage_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id  ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id  ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_LEFTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"));
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("catalog_id"),(Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"));

//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription , site_id , active , portlettype_id , lang_id , catalog_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " About company  ");
			args.put("description", "<r> About company.</r>");
			args.put("fulldescription",
					"<r>" + " Name of company: " + AuthorizationPageBeanId.getStrCompany() + "</r>" + "<r> Phone"
							+ AuthorizationPageBeanId.getStrPhone() + " </r>" + "<r> Fax: "
							+ AuthorizationPageBeanId.getStrFax() + " </r>" + "<r> EMail: "
							+ AuthorizationPageBeanId.getStrEMail() + " </r>");

			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_BOTTOM);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("catalog_id", -2);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"),(Long)args.get("catalog_id"));


//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription , site_id , active , portlettype_id , lang_id , catalog_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?)";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Delivery ");
			args.put("description", "<r> Delivery.</r>");
			args.put("fulldescription", "<r>" + " Delivery from " + AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Payment perform by way  translation of money resources on the company score through payment systems or through bank by direct payment </r>"
					+ "<r> Account Web Money: 1111222333444 </r>" + "<r> Account Yandex Money: 333442234455 </r>"
					+ "<r> Bank Of Test ltd  ...... </r>");

			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_BOTTOM);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("catalog_id", -2);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"),(Long)args.get("catalog_id"));

//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription , site_id , active , portlettype_id , lang_id , catalog_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?  )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Return policy");
			args.put("description", "<r> How can I return item.</r>");
			args.put("fulldescription", "<r>" + " Order payment " + AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Payment perform by way  translation of money resources on the company score through payment systems or through bank by direct payment </r>"
					+ "<r> Account Web Money: 1111222333444 </r>" + "<r> Account Yandex Money: 333442234455 </r>"
					+ "<r> Bank Of Test ltd  ...... </r>");

			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_BOTTOM);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("catalog_id", -2);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"),(Long)args.get("catalog_id"));
//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription , site_id , active , portlettype_id , lang_id , catalog_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ?  )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Contacts us");
			args.put("description", "<r> Contacts us.</r>");
			args.put("fulldescription", "<r>" + " Contacts us " + AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Payment perform by way  translation of money resources on the company score through payment systems or through bank by direct payment </r>"
					+ "<r> Account Web Money: 1111222333444 </r>" + "<r> Account Yandex Money: 333442234455 </r>"
					+ "<r> Bank Of Test ltd  ...... </r>");

			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_BOTTOM);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("catalog_id", -2);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"),(Long)args.get("catalog_id"));

//			AuthorizationPageBeanId
//			Create About Page
			query = soft_sequence_rs;
            soft_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription , site_id , active , portlettype_id , lang_id , catalog_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Careers");
			args.put("description", "<r> Careers.</r>");
			args.put("fulldescription", "<r>" + " Careers with " + AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Payment perform by way  translation of money resources on the company score through payment systems or through bank by direct payment </r>"
					+ "<r> Account Web Money: 1111222333444 </r>" + "<r> Account Yandex Money: 333442234455 </r>"
					+ "<r> Bank Of Test ltd  ...... </r>");

			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_BOTTOM);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("catalog_id", -2);
			transactionSupport.getJdbcTemplate().update(query,(Long)args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),
                (Long)args.get("site_id"),(Boolean)args.get("active"),(int)args.get("portlettype_id"),(int)args.get("lang_id"),(Long)args.get("catalog_id"));

			query = "select   file.name , file.path from soft  LEFT  JOIN file  ON soft.file_id = file.file_id  where soft_id = "
					+ product_id;

			List<Map<String,Object>> queryForList =  transactionSupport.getJdbcTemplate().queryForList(query);
			file_name = (String) queryForList.get(0).get("file.name");
			file_path = (String) queryForList.get(0).get("file.path");
			if (!file_path.startsWith("/"))
				file_path = "/" + file_path;
			transactionSupport.commitTransaction(trxnStatus);
			cretareSiteDirWithExtract(file_name, file_path, site_dir, applicationContext);

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		} 
	}

	public void addShopWithExtract_en_addcontent(AuthorizationPageBean AuthorizationPageBeanId, String product_id,
			ServletContext applicationContext) {
	  TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		String query = "";
		try {
		    JdbcTemplate cmsJdbcTemplate = transactionSupport.getJdbcTemplate();
			query = site_sequence_rs;
			site_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ?  , ? , ? ) ; ";
			cmsJdbcTemplate.update(query, Long.valueOf(site_id), Long.valueOf(AuthorizationPageBeanId.getIntUserID()),host,home_dir,site_dir,person,phone,address,true);

			/////////////// dbAdaptor.executeUpdate(query);
			// add manager user
			// query = "SELECT NEXT VALUE FOR tuser_user_id_seq AS ID FROM ONE_SEQUENCES";
			query = tuser_sequence_rs;
			long intUserID = Long.parseLong(transactionSupport.getJdbcTemplate().queryForObject(query, String.class));
			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id, E_MAIL , COMPANY) "
					+ " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? ) ";
			cmsJdbcTemplate.update(query, intUserID, login,passwd,new Date(),true,true,new Date(),SiteRole.ADMINISTRATOR_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()),Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id)
			    ,AuthorizationPageBeanId.getStrEMail(),AuthorizationPageBeanId.getStrCompany());
			// query = "SELECT NEXT VALUE FOR account_id_seq AS ID FROM ONE_SEQUENCES";
			query = account_sequence_rs;
			account_id = (String) cmsJdbcTemplate.queryForObject(query, String.class);
			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id ) "+ " values ( ? , ? , ? , ? , ? ,  ? ,  ?  )";
	        cmsJdbcTemplate.update(query, Long.valueOf(account_id),intUserID,Double.valueOf("0"),Long.valueOf(AuthorizationPageBeanId.getCountry_id()),new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));
			// add anonymouse user
			query = tuser_sequence_rs;
			intUserID = Long.parseLong((String) cmsJdbcTemplate.queryForObject(query,String.class));
			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query,intUserID,SiteRole.GUEST,SiteRole.GUEST_PASSWORD,new Date(),true,true,new Date(),SiteRole.GUEST_ROLE_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()), Long.valueOf(site_id), 
                    Long.valueOf(city_id), Long.valueOf(country_id));
			query = account_sequence_rs;
			account_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "+ " values ( ? , ? , ? , ? , ? ,  ? ,  ? ) ; ";
            cmsJdbcTemplate.update(query, Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()));

			// ==== end add users =========================

			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "+ " values ( ? , ? , ? , ? ,  ? , ? , ? ) ; ";
            cmsJdbcTemplate.update(query, 84473,intUserID,"HCO-CENTE-406","91KiBFRtE8fF7VHc8tvr",1,Long.valueOf(site_id),new Date());
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			cmsJdbcTemplate.update(query, SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id),"News",true,2,SpecialCatalog.ROOT_CATALOG);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ; ";
            cmsJdbcTemplate.update(query, SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id),"Main page",true,2,SpecialCatalog.ROOT_CATALOG);

			String catalog_id = "";
			String parent_catalog_id = "";
			query = tcatalog_sequence_rs;
			catalog_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  )";
            cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection1",true,2,SpecialCatalog.ROOT_CATALOG);

			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection1",true,2,Long.valueOf(parent_catalog_id));
			query = tcatalog_sequence_rs;
			catalog_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?)";
            cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection2",true,2, SpecialCatalog.ROOT_CATALOG);
			query = tcatalog_sequence_rs;
			parent_catalog_id = catalog_id;
			catalog_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
            cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection2",true,2,Long.valueOf(parent_catalog_id));
			catalog_id = (String) cmsJdbcTemplate.queryForObject(tcatalog_sequence_rs,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  )";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection3",true,2, SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id = (String) cmsJdbcTemplate.queryForObject(tcatalog_sequence_rs,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection3",true,2,Long.valueOf(parent_catalog_id));
            catalog_id = (String) cmsJdbcTemplate.queryForObject(tcatalog_sequence_rs,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection4",true,2, SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
            catalog_id = (String) cmsJdbcTemplate.queryForObject(tcatalog_sequence_rs,String.class);
            query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection4",true,2, Long.valueOf(parent_catalog_id));
            catalog_id = (String) cmsJdbcTemplate.queryForObject(tcatalog_sequence_rs,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Selection5",true,2, SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
            catalog_id = (String) cmsJdbcTemplate.queryForObject(tcatalog_sequence_rs,String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
			cmsJdbcTemplate.update(query, Long.valueOf(catalog_id),Long.valueOf(site_id),"Sub selection5",true,2, Long.valueOf(parent_catalog_id));
			String creteria_id = "";
			query = "SELECT MIN(CRETERIA1_ID) - 1  as ID FROM creteria1";
			creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ "VALUES(? , ? , ? , ? , ? , ? , ? )";
			cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion1");
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "		+ " VALUES(? , ? , ? , ? , ? , ? ,? )";
			cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Test1",true,2,0, Long.valueOf(site_id),"Criterion1");
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? ,? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Test2",true,2,0, Long.valueOf(site_id),"Criterion1");
			query = "SELECT MIN(CRETERIA2_ID) - 1  as ID FROM creteria2";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria2 (CRETERIA2_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion2");
			query = "SELECT MIN(CRETERIA3_ID) - 1  as ID FROM creteria3";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria3 (CRETERIA3_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? , ?)";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion3");
			query = "SELECT MIN(CRETERIA4_ID) - 1  as ID FROM creteria4";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria4 (CRETERIA4_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion4");
			query = "SELECT MIN(CRETERIA5_ID) - 1  as ID FROM creteria5";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria5 (CRETERIA5_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion5");
			query = "SELECT MIN(CRETERIA6_ID) - 1  as ID FROM creteria6";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria6 (CRETERIA6_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion6");
			query = "SELECT MIN(CRETERIA7_ID) - 1  as ID FROM creteria7";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
            query = "INSERT INTO creteria7 (CRETERIA7_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion7");
			query = "SELECT MIN(CRETERIA8_ID) - 1  as ID FROM creteria8";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria8 (CRETERIA8_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion8");
			query = "SELECT MIN(CRETERIA9_ID) - 1  as ID FROM creteria9";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria9 (CRETERIA9_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion9");
			query = "SELECT MIN(CRETERIA10_ID) - 1  as ID FROM creteria10";
            creteria_id = (String) cmsJdbcTemplate.queryForObject(query,String.class);
			query = "INSERT INTO creteria10 (CRETERIA10_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            cmsJdbcTemplate.update(query, Long.valueOf(creteria_id),"Not chosen",true,2,0, Long.valueOf(site_id),"Criterion10");
			String soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			HashMap<String,Object> args = new HashMap<String, Object>();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Search system of Internet shop");
			args.put("description", "<r> Search possibilities of Internet shop </r>");
			args.put("fulldescription",
					"<r> 1. For high-grade work CMS Business One, you need to adjust the expanded search in criteria </r>"
							+ "<r> under the problems. It will allow your clients to find at once the necessary goods, the information or the document. </r>"
							+ "<r> 2. It is not necessary to adjust search in the text, it works by default. </r>"
							+ "<r> 3. (Only at registered users) it is not necessary to adjust search in the subject authority, it works by default. </r>"
							+ "<r> the Instruction on adjustment of the expanded search on criteria </r>"
							+ "<r> Our system provides 10 edited criteria and search in the price </r>"
							+ "<r> Editing of criteria is made in the form of addition of the information module in subsection INSTALLATION of CRITERIA FOR SEARCH of THIS INFORMATION ON the SITE. </r>"
							+ "<r> For opening of this subsection press РїР»СЋСЃРёРє before heading and to curtail the form press Р�?РёРЅСѓСЃРёРє before heading. </r>"
							+ "<r> Criteria can be independent. For example: the Manufacturer, an engineering Kind, Colour - all these criteria are independent from each other. </r>"
							+ "<r> And can be dependent on any criterion. For example: the Country-city-area. Here the criterion the Country - independent, criterion the City depends on criterion the Country, and criterion Area the City depends on criterion. </r>"
							+ "<r> </r>"
							+ "<r> Dependence of criterion which you change, is advanced by a horizontal pink strip. The changeable criterion depends on the criterion allocated with a pink strip. </r>"
							+ "<r> If you change criterion, РІС‹РґРµР»РµРЅС‹Р№ a pink strip this criterion depends on itself, i.e. it independent. </r>"
							+ "<r> the Step-by-step description of input of independent criterion: </r>"
							+ "<r> we Will assume that we need to enter criterion the Manufacturer with its items. </r>"
							+ "<r> 1. We allocate the necessary criterion with a pink strip, for example Kriterij1 </r>"
							+ "<r> 2. We press the button to CHANGE. The form for change of Kriterija1 will open. </r>"
							+ "<r> 3. In the top part of the form we change the name Kriterij1 on the Manufacturer and we press the button to CHANGE. Now our criterion on main page will be called the Manufacturer. </r>"
							+ "<r> 4. In the bottom part of the form it is visible it is not chosen - this record is not edited and DOES NOT LEAVE. </r>"
							+ "<r> 5. To add an item in criterion, we press the button to ADD. To opened form we write the necessary item, for example Proizvoditel1, and we press the button to SAVE. Other items it is got similarly. </r>"
							+ "<r> For removal or editing of an item of criterion use respective buttons opposite to an item. </r>"
							+ "<r> </r>" + "<r> Other independent criteria it is got similarly. Look items 1-5 </r>"
							+ "<r> </r>" + "<r> the Step-by-step description of input of dependent criterion: </r>"
							+ "<r> we Will assume that at us the independent criterion the Country with the items is already entered and we need to enter dependent criterion the City. </r>"
							+ "<r> 1. We allocate with a pink strip criterion on which our criterion the City, in this case criterion the Country will depend. </r>"
							+ "<r> Strana1 Is chosen in criterion the Country the necessary item, for example. </r>"
							+ "<r> 2. Opposite to criterion the City we press the button to CHANGE. The form for criterion change will open. </r>"
							+ "<r> 3. In the top part of the form we change the criterion name for the City and we press the button to CHANGE. </r>"
							+ "<r> 4. We get the necessary items Gorod1-1, Gorod1-2 etc., corresponding to the chosen item Strana1. </r>"
							+ "<r> If it is necessary to continue input of cities on other countries we come back to criterion the Country and we choose an item Strana2. </r>"
							+ "<r> Further we pass to item 2 etc. </r>" + "<r> </r>"
							+ "<r> If at you quantity of criteria less than ten the remained criteria can be hidden, by removal of the name of criterion. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Forum");
			args.put("description", "<r> Discussion of the goods, service, news </r>");
			args.put("fulldescription", "<r>We have built in a forum ours CMS Business One. </r>"
					+ "<r>For each information or news module the discussion system </r>"
					+ "<r> the discussion System can be included or switched off for each concrete module. </r>"
					+ "<r> All new messages gather in С‚РѕРї on main page for the review of fresh discussions </r>"
					+ "<r> </r>"
					+ "<r> forum Inclusion is made in the form of addition of the information or news module by an option choice to INCLUDE DISCUSSION. </r>"
					+ "<r> By default the forum is switched off. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
            cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "News module of Internet shop");
			args.put("description", "<r> News module of Internet shop </r>");
			args.put("fulldescription", "<r>" + "We have established the news module in Internet shop </r>"
					+ "<r> Novosnoj the module displays the text, a picture and addition date. </r>"
					+ "<r> the Module of news appears on all pages in all sections. </r>"
					+ "<r> your news does not remain not noticed where there would be no your client. </r>" + "<r> </r>"
					+ "<r> the Instruction on addition of the news module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press to ADD the INFORMATION MODULE With SEARCH IN CRITERIA. </r>"
					+ "<r> 3. We find subsection the FORM of ADDITION OR INFORMATION CHANGE. </r>"
					+ "<r> 4. In the field HEADING we write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION we choose NEWS. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field Р�Р—РћР�?Р РђР–Р•РќР�Р•1 it is given the chance to add the image on page with the short description. </r>"
					+ "<r> the Image can be added in two methods: ZAKACHAT Р�Р—РћР�?Р РђР–Р•РќР�Р•1 FOR SHORT - Р·Р°РєР°С‡Р°С‚СЊ from the computer of the user, Р�Р—РћР�?Р РђР–Р•РќР�Р•1 FROM BASE - to choose from present base. </r>"
					+ "<r> the Image Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ in any formats which are supported by your browser. The maximum size of the image 1,5РњР±. </r>"
					+ "<r> 8. In the field the PRICE the price not to mark, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written a brief information on news. </r>"
					+ "<r> 10. In the field Р�Р—РћР�?Р РђР–Р•РќР�Р•2 it is given the chance to add the image on page with detailed description. </r>"
					+ "<r> the Image can be added in two methods: ZAKACHAT Р�Р—РћР�?Р РђР–Р•РќР�Р•2 FOR DETAILED - Р·Р°РєР°С‡Р°С‚СЊ from the computer of the user, Р�Р—РћР�?Р РђР–Р•РќР�Р•2 FROM BASE - to choose from present base. </r>"
					+ "<r> the Image Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ in any formats which are supported by your browser. The maximum size of the image 1,5РњР±. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of news. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: Р—РђРљРђР§РђРўР¬ the FILE - Р·Р°РєР°С‡Р°С‚СЊ from the computer of the user to CHOOSE the FILE - to choose from present base. </r>"
					+ "<r> the File Р·Р°РєР°С‡РёРІР°РµС‚СЃСЏ in any format which supports your browser. The maximum size of a file. 1,5РњР±. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your news has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your news has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE the significance should be established is published. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Advertising");
			args.put("description", "<r> Advertising place in Internet shop </r>");
			args.put("fulldescription", "<r>"
					+ "We have built in for you advertising blocks that you could sell the space in your Internet shop. </r>"
					+ "<r> On the right and to the left of the central block advertising modules </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Internet shop");
			args.put("description", "<r>Reference to your Internet shop </r>");
			args.put("fulldescription",
					"" + "<r> your Internet shop is accessible  by link http://www.siteforyou.com/Productlist.jsp?site="
							+ site_id + "</r>" + "<r> also you can buy domain here </r>"
							+ "<r> Save somewhere this information. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id ,type_id , site_id , lang_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("type_id", Layout.SOFTTYPE_SHOW_ON_MAIN_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			args.put("active", true);
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(int)args.get("type_id"),(Long)args.get("site_id"),
                (int)args.get("lang_id"),(boolean)args.get("active"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Questions and answers your forum ");
			args.put("description", "<r> You can talk about  goods or news and a price of sheet on your forum.</r>");
			args.put("fulldescription", "<r>" + "  The information module for main page 1." + "</r>"
					+ "<r> You need to change the contents of this module to place here  your information. </r>"
					+ "<r> For this you should enter on site under  your the password  </r>"
					+ "<r> So you can observe buttons:  remove, edit, update (To add the new module). </r>"
					+ "<r> In during  change of the informational unit you can put its other section .</r>"
					+ "<r> You also can edit ,  add, delete this section name. </r>"
					+ "<r> Also you can change parameters  criteria for search of  information  unit </r>"
					+ "<r> For this purpose should change criteria on the form of change of the information with title (Installation of criteria for information search on a site ). </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(int)args.get("portlettype_id"),
                (int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " The informational modul  ");
			args.put("description", "<r> Thanks from Center Business Solutions Ltd  that  you using  this CMS .</r>");
			args.put("fulldescription", "<r> The information module for main page 1.</r>"
					+ "<r> You need to change the contents of this module to place here  your information. </r>"
					+ "<r> For this you should enter on site under  your the password  </r>"
					+ "<r> So you can observe buttons:  remove, edit, update (To add the new module). </r>"
					+ "<r> In during  change of the informational unit you can put its other section .</r>"
					+ "<r> You also can edit ,  add, delete this section name. </r>"
					+ "<r> Also you can change parameters  criteria for search of  information  unit </r>"
					+ "<r> For this purpose should change criteria on the form of change of the information with title (Installation of criteria for information search on a site ). </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_TOPBLOG_ON_MAINPAGE);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),(int)args.get("portlettype_id"),
                (int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_LEFTTOP);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
			    (int)args.get("portlettype_id"),(int)args.get("image_id"),(int)args.get("bigimage_id"),(int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , lang_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("image_id"),(int)args.get("bigimage_id"),(int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_LEFTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("lang_id"));
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id ) "	+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "Information module");
			args.put("description",
					"<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>");
			args.put("fulldescription", "<r> </r>"
					+ "<r> In the information module  you can  place  the text, to place  images on forms with short and detailed description  and attach files, to set a price, and also to include voting or a forum for each concrete information module. </r>"
					+ "<r> the Instruction on addition of the information module: </r>"
					+ "<r> 1. To come into MANAGEMENT of the SITE having pressed the button to UPDATE. </r>"
					+ "<r> 2. To press (To add the information, the goods, news in the central module with search in criteria)  </r>"
					+ "<r> 3. We find subsection ( Form for an institution or information change on a site ). </r>"
					+ "<r> 4. In the field HEADING we will write heading. </r>"
					+ "<r> 5. In the field CHOSEN SECTION you can chose a section  where is place of  information module.If section is not exist then add new section in (Form for catalogue construction ) for detail see creation catalog. </r>"
					+ "<r> 6. In the field IN OTHER LANGUAGE FOR SEARCH of anything it is not necessary to enter HEADING. Is in working out. </r>"
					+ "<r> 7. In the field PICTURE 1 it is given the chance to add the image on page with the short description. The image can be added in two methods: UPLOAD PICTURE 1 FOR SHORT - upload from the computer of the user, PICTURE 1 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 8. If you mark the price in the field the PRICE it is displayed. If the price is not necessary to you, keep a zero. </r>"
					+ "<r> 9. In the field the BRIEF INFORMATION is written goods or service brief information. </r>"
					+ "<r> 10. In the field PICTURE 2 it is given the chance to add the image on page with detailed description. The image can be added in two methods: UPLOAD PICTURE 2 FOR DETAILED - upload from the computer of the user, PICTURE 2 FROM BASE - to choose from present base. The image uploading in any formats which are supported by your browser. The maximum size of the image 1,5 Mb. </r>"
					+ "<r> 11. In the field the DETAILED INFORMATION is written the detailed information of the goods or service. </r>"
					+ "<r> 12. In the field ATTACH the FILE you can to attach a file in two methods: UPLOAD the FILE - upload from the computer of the user to CHOOSE the FILE - to choose from present base. A file uploading in any format which supports your browser. The maximum size of a file. 1,5 Mb. </r>"
					+ "<r> 13. At an option choice to INCLUDE VOTING, your information module has a voting. By default this option is switched off. </r>"
					+ "<r> 14. At an option choice to INCLUDE DISCUSSION, your information module has a forum. By default this option is switched off. </r>"
					+ "<r> 15. In an option I APPROVE, if choose it is published, the information module will be displayed only in the catalogue. If will choose to SHOW ON MAIN PAGE this module will be displayed both in the catalogue and on main page. </r>"
					+ "<r> 16. Enter from a picture a code. </r>" + "<r> 17. Press the button to SAVE. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PORTLET_TYPE_RIGHTTOP);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("lang_id"));

//			AuthorizationPageBeanId
//			Create About Page
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " About company  ");
			args.put("description", "<r> About company.</r>");
			args.put("fulldescription",
					"<r>" + " Name of company: " + AuthorizationPageBeanId.getStrCompany() + "</r>" + "<r> Phone"
							+ AuthorizationPageBeanId.getStrPhone() + " </r>" + "<r> Fax: "
							+ AuthorizationPageBeanId.getStrFax() + " </r>" + "<r> EMail: "
							+ AuthorizationPageBeanId.getStrEMail() + " </r>");
			args.put("catalog_id", SpecialCatalog.FOR_EXTERNAL_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PAGE_ABOUT_COMPANY);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("lang_id"));
//			AuthorizationPageBeanId
//			Create About Page
            soft_id = (String) cmsJdbcTemplate.queryForObject(soft_sequence_rs,String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , lang_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", "  How can I pay order ");
			args.put("description", "<r> How can I pay order.</r>");
			args.put("fulldescription", "<r>" + " Order payment " + AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> Payment perform by way  translation of money resources on the company score through payment systems or through bank by direct payment </r>"
					+ "<r> Account Web Money: 1111222333444 </r>" + "<r> Account Yandex Money: 333442234455 </r>"
					+ "<r> Bank Of Test ltd  ...... </r>");
			args.put("catalog_id", SpecialCatalog.FOR_EXTERNAL_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PAGE_ABOUT_PAY);
			args.put("lang_id", AuthorizationPageBeanId.getLang_id());
			cmsJdbcTemplate.update(query,(Long) args.get("soft_id"),(String)args.get("name"),(String)args.get("description"),(String)args.get("fulldescription"),(Long)args.get("catalog_id"),(Long)args.get("site_id"),(boolean)args.get("active"),
                (int)args.get("portlettype_id"),(int)args.get("lang_id"));
			transactionSupport.commitTransaction(trxnStatus);

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		}
	}


    public void addShopWithExtract_allLang(AuthorizationPageBean AuthorizationPageBeanId,
        String product_id, ServletContext applicationContext) {
      addShopWithExtract_en_addcontent(AuthorizationPageBeanId, product_id, applicationContext);
      addShopWithExtract_ru_addcontent(AuthorizationPageBeanId, product_id, applicationContext);

      String file_name = "";
      String file_path = "";
      String query = "";
      try {
        query =
            "select   file.name , file.path from soft  LEFT  JOIN file  ON soft.file_id = file.file_id  where soft_id = "
                + product_id;

        List<Map<String, Object>> listForObject =
            transactionSupport.getJdbcTemplate().queryForList(query);
        file_name = (String) listForObject.get(0).get("file.name");
        file_path = (String) listForObject.get(0).get("file.path");
        if (!file_path.startsWith("/"))
          file_path = "/" + file_path;

        cretareSiteDirWithExtract(file_name, file_path, site_dir, applicationContext);

      } catch (Exception ex) {
        logger.debug(ex.getLocalizedMessage());
        logger.error(ex.getLocalizedMessage());
      }

    }
	
	

	public void addSiteMainSite() {

	}

	public void addSite(AuthorizationPageBean AuthorizationPageBeanId) {
		String query = "";
		long intOwnerUserId = 0;
		TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		JdbcTemplate jdbcTemplate =  transactionSupport.getJdbcTemplate();
		try {
			site_id =    jdbcTemplate.queryForObject(site_sequence_rs, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  , ? , ? ) ; ";

			jdbcTemplate.update(query, Long.valueOf(site_id),Long.valueOf(AuthorizationPageBeanId.getIntUserID()),"localhost","localhost","localhost",AuthorizationPageBeanId.getStrFirstName() + " " + AuthorizationPageBeanId.getStrLastName()
			,AuthorizationPageBeanId.getStrPhone(),AuthorizationPageBeanId.getStrCountry() + ", " + AuthorizationPageBeanId.getStrCity()
            + ", " + AuthorizationPageBeanId.getAddress(),true);
			site_id = jdbcTemplate.queryForObject(site_sequence_rs, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  , ? , ? ) ; ";


			jdbcTemplate.update(query, Long.valueOf(site_id),Long.valueOf(AuthorizationPageBeanId.getIntUserID()),AuthorizationPageBeanId.getStrWebsite(),AuthorizationPageBeanId.getStrWebsite(),AuthorizationPageBeanId.getStrWebsite(),
			    AuthorizationPageBeanId.getStrFirstName() + " " + AuthorizationPageBeanId.getStrLastName()
            ,AuthorizationPageBeanId.getStrPhone(),AuthorizationPageBeanId.getStrCountry() + ", " + AuthorizationPageBeanId.getStrCity()
            + ", " + AuthorizationPageBeanId.getAddress(),true);
			
			long intUserID = Long.parseLong(jdbcTemplate.queryForObject(tuser_sequence_rs, String.class));
			intOwnerUserId = intUserID;

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id, E_MAIL , COMPANY) "
					+ " values (? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ?, ? , ? ) ";
			jdbcTemplate.update(query,intUserID,AuthorizationPageBeanId.getStrLogin(),AuthorizationPageBeanId.getStrPasswd(),new Date(),true,true,new Date(),SiteRole.ADMINISTRATOR_ROLE_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()),
			    Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id), AuthorizationPageBeanId.getStrEMail(), AuthorizationPageBeanId.getStrCompany() );

			account_id = jdbcTemplate.queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id , complete ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ? , ? )";
			jdbcTemplate.update(query, Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()),true );

			// add anonymouse user
			intUserID = Long.parseLong(jdbcTemplate.queryForObject(tuser_sequence_rs, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? )";
			jdbcTemplate.update(query,intUserID,SiteRole.GUEST,SiteRole.GUEST_PASSWORD,new Date(),true,true,new Date(),SiteRole.GUEST_ROLE_ID,0,Long.valueOf(AuthorizationPageBeanId.getCountry_id()),
                Long.valueOf(site_id),Long.valueOf(city_id),Long.valueOf(country_id));
			account_id = jdbcTemplate.queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id , complete ) "
					+ " values ( ? , ? , ? , ? , ? ,  ? ,  ? , ?) ; ";
			jdbcTemplate.update(query, Long.valueOf(account_id),intUserID,Double.valueOf("0"),3,new Date(),"new_account",Long.valueOf(AuthorizationPageBeanId.getCountry_id()),true );

			// ==== end add users =========================
			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "+ " values ( ? , ? , ? , ? ,  ? , ? , ? ) ; ";
			jdbcTemplate.update(query, 84473,intUserID,"HCO-CENTE-406","91KiBFRtE8fF7VHc8tvr",1,Long.valueOf(site_id),new Date());
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			jdbcTemplate.update(query, SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG,Long.valueOf(site_id), "РќРѕРІРѕСЃС‚Рё",true,1,SpecialCatalog.ROOT_CATALOG);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ; ";
			jdbcTemplate.update(query, SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID,Long.valueOf(site_id), "Р“Р»Р°РІРЅР°СЏ СЃС‚СЂР°РЅРёС†Р°",true,1,SpecialCatalog.ROOT_CATALOG);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? ,? , ? )";
			jdbcTemplate.update(query, SpecialCatalog.OUTPUT_PAGES_AREA_FROM_USERSITE_TO_MAIN_SITE,Long.valueOf(site_id), "РќР° РіР»Р°РІРЅС‹Р№ СЃР°Р№С‚",true,1,SpecialCatalog.ROOT_CATALOG);

			String catalog_id = "";
			String parent_catalog_id = "";
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  )";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id), "Р Р°Р·РґРµР»1",true,1,SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id), "РџРѕРґ СЂР°Р·РґРµР»1",true,1,Long.valueOf(parent_catalog_id));
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?)";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id), "Р Р°Р·РґРµР»2",true,1,SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id =  jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»2",true,1,Long.valueOf(parent_catalog_id));
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  )";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id),"Р Р°Р·РґРµР»3",true,1,SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»3",true,1,Long.valueOf(parent_catalog_id));
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id), "Р Р°Р·РґРµР»4",true,1,SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ; ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»4",true,1,Long.valueOf(parent_catalog_id));
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ?  ) ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id), "Р Р°Р·РґРµР»5",true,1,SpecialCatalog.ROOT_CATALOG);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) "+ " values ( ? , ? , ? , ? , ? , ? ) ";
			jdbcTemplate.update(query,  Long.valueOf(catalog_id),Long.valueOf(site_id),"РџРѕРґ СЂР°Р·РґРµР»5",true,1,Long.valueOf(parent_catalog_id));
			String creteria_id = "";
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ "VALUES(? , ? , ? , ? , ? , ? , ? )";
			jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? ,? )";
			jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РўРµСЃС‚1",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria1 (CRETERIA1_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? ,? , ? , ? , ? , ? )";
			jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РўРµСЃС‚2",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№1");
			query = "SELECT MAX(CRETERIA2_ID) + 1  as ID FROM creteria2";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria2 (CRETERIA2_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? , ? )";
			jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№2");
			query = "SELECT MAX(CRETERIA3_ID) + 1  as ID FROM creteria3";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria3 (CRETERIA3_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES(? , ? , ? , ? , ? , ? , ?)";
			jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№3");
			query = "SELECT MAX(CRETERIA4_ID) + 1  as ID FROM creteria4";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria4 (CRETERIA4_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
			jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№4");
			query = "SELECT MAX(CRETERIA5_ID) + 1  as ID FROM creteria5";
			creteria_id =  jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria5 (CRETERIA5_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№5");
			query = "SELECT MAX(CRETERIA6_ID) + 1  as ID FROM creteria6";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria6 (CRETERIA6_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№6");
			query = "SELECT MAX(CRETERIA7_ID) + 1  as ID FROM creteria7";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria7 (CRETERIA7_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№7");
			query = "SELECT MAX(CRETERIA8_ID) + 1  as ID FROM creteria8";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria8 (CRETERIA8_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL ) "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№8");
			query = "SELECT MAX(CRETERIA9_ID) + 1  as ID FROM creteria9";
			creteria_id =jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria9 (CRETERIA9_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№9");
			query = "SELECT MAX(CRETERIA10_ID) + 1  as ID FROM creteria10";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			if (creteria_id == null || creteria_id.length() == 0)
				creteria_id = "1";

			query = "INSERT INTO creteria10 (CRETERIA10_ID ,NAME ,ACTIVE ,LANG_ID ,LINK_ID ,CATALOG_ID ,LABEL )  "+ " VALUES( ? , ? , ? , ? , ? , ? , ? )";
            jdbcTemplate.update(query,  Long.valueOf(creteria_id),"РќРµ РІС‹Р±СЂР°РЅРѕ",true,1,0,Long.valueOf(site_id),"РљСЂРёС‚РµСЂРёР№10");
			String soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  ) ; ";

			HashMap<String,Object>args = new HashMap<String, Object>();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РџРѕРёСЃРєРѕРІР°СЏ СЃРёСЃС‚РµР�?Р° Р�?Р°РіР°Р·РёРЅР°  ");
			args.put("description",
					"<r>РќР°СЃС‚СЂРѕР№РєР° РїРѕРёСЃРєР° РїРµСЂРµРґ РЅР°С‡Р°Р»РѕР�? </r><r> СЂР°Р±РѕС‚С‹ Р�?Р°РіР°Р·РёРЅР° .</r>");
			args.put("fulldescription",
					"<r>1. Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ CMS Business One РІР°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРµС‚ С„РѕСЂР�?Сѓ СЂР°Р·С€РёСЂРµРЅРЅРѕРіРѕ РїРѕРёСЃРєР° РїРѕ РєСЂРёС‚РµСЂРёСЏР�? ,</r>"
							+ "<r> Рё РЅР°СЃС‚СЂРѕРёС‚СЊ РµРµ РїРѕРґ РІР°С€Рё РЅСѓР¶РґС‹ Рё С‡С‚РѕР±С‹ РІР°С€Рё РєР»РёРµРЅС‚С‹ Р�?РѕРіР»Рё СЃСЂР°Р·Сѓ РЅР°С…РѕРґРёС‚СЊ РЅСѓР¶РЅС‹Р№ С‚РѕРІР°СЂ РёР»Рё РёРЅС„РѕСЂР�?Р°С†РёСЋ РёР»Рё РґРѕРєСѓР�?РµРЅС‚С‹ .</r>"
							+ "<r> РІС‹ Р�?РѕР¶Рµ РїРѕСЃР�?РѕС‚СЂРµС‚СЊ РЅР°С€ РІРёРґРµРѕ РєСѓСЂСЃ Р±РµСЃРїР»Р°С‚РЅРѕ РєР°Рє РЅР°СЃС‚СЂРѕРёС‚СЊ С„РѕСЂСѓ СЂР°Р·С€РёСЂРµРЅРЅРѕРіРѕ РїРѕРёСЃРєР°.</r>"
							+ "<r>2. РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ. РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ РЅР°СЃС‚СЂР°РёРІР°С‚СЊ РЅРµ РЅСѓР¶РЅРѕ РѕРЅ СЂР°Р±РѕС‚Р°РµС‚ РїРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ </r>"
							+ "<r>3. РџРѕРёСЃРє РїРѕ РїРµСЂРІРѕР№ Р±СѓРєРІРµ. РџРѕРёСЃРє РїРѕ РїРµСЂРІРѕР№ Р±СѓРєРІРµ РґРѕР±Р°РІР»СЏРµС‚СЊСЃСЏ РїРѕ СЃРѕРіР»Р°СЃРѕРІР°РЅРёСЋ СЃ Р·Р°РєР°Р·С‡РёРєРѕР�?. </r>"
							+ "<r>4. РџРѕРёСЃРє РїРѕ СЂСѓР±СЂРёРєР°С‚РѕСЂСѓ. РџРѕРёСЃРє РїРѕ СЂСѓР±СЂРёРєР°С‚РѕСЂСѓ РґРѕР±Р°РІР»СЏРµС‚СЊСЃСЏ РїРѕ СЃРѕРіР»Р°СЃРѕРІР°РЅРёСЋ СЃ Р·Р°РєР°Р·С‡РёРєРѕР�?. </r>"
							+ "<r> РњС‹ РІР°Р�? РїСЂРµРґР»РѕРіР°РµР�? 4 СЃРёСЃС‚РµР�?С‹ РїРѕРёСЃРєР° РµСЃР»Рё РІР°Р�? РЅСѓР¶РЅРѕ Р±РѕР»СЊС€Рµ Р�?С‹ СЃРґРµР»Р°РµР�? РµС‰Рµ. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
			soft_id =jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�Р·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРё РЅР° СЃР°Р№С‚Рµ  ");
			args.put("description",
					"<r> Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ РІР°С€РµРіРѕ Р�?Р°РіР°Р·РёРЅР° РЅР° РѕСЃРЅРѕРІРµ CMS Business One  </r>");
			args.put("fulldescription", "<r>"
					+ " 1. Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ РІР°С€РµРіРѕ Р�?Р°РіР°Р·РёРЅР° РЅР° РѕСЃРЅРѕРІРµ CMS Business One  </r>"
					+ "<r>"
					+ " РІР°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ РІР°С€РµРіРѕ Р�?Р°РіР°Р·РёРЅР°. </r>"
					+ "<r> Р”Р»СЏ СЌС‚РѕРіРѕ СЃСѓС‰РµС‚РІСѓРµС‚ СЃРїРµС†РёР°Р»СЊРЅР°СЏ С„РѕСЂР�?Р° РґР»СЏ РІРІРѕРґР° Рё СЂРµРґР°С‚РёСЂРѕРІР°РЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё </r>"
					+ "<r> Р’ РєР°Р¶РґС‹Р№ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р±Р»РѕРє РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїР»СЏС‚СЊ РєР°СЂС‚РёРЅРєСѓ Рё С‚РµРєСЃС‚ РґР»СЏ РєСЂР°С‚РєРѕРіРѕ РѕРїРёСЃР°РЅРёСЏ</r>"
					+ "<r> РЅР° СЃС‚СЂР°РЅРёС†Рµ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�? Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµСЃС‚СЊ 21 РєР°СЂС‚РёРЅРєСѓ Рё С‚РµРєСЃС‚ Рё РїСЂРёРєСЂРµРїРёС‚СЊ 10  С„Р°Р№Р»РѕРІ .</r>"
					+ "<r> 2. Р�?Р°РіР°Р·РёРЅ Р°РІС‚РѕР�?Р°С‚РёС‡РµСЃРєРё СѓС‡РёС‚С‹РІР°РµС‚ РєРѕР»РёС‡РµСЃС‚РІРѕ РїРѕСЃР�?РѕС‚СЂРѕРІ РІР°С€РµР№ СЃС‚СЂР°РЅРёС†С‹ .</r>"
					+ "<r> 3. Р�?Р°РіР°Р·РёРЅ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РѕС†РµРЅРєСѓ СЂРµР№С‚РёРЅРіР° СЌС‚РѕР№ РёРЅС„РѕСЂР�?Р°С†РёРё </r>"
					+ "<r> С‡С‚Рѕ РїРѕР·РІРѕР»СЏРµС‚ РІР°Р�? РѕС†РµРЅРёСЃР°С‚СЊ РєР°С‡РµСЃС‚РІРѕ РІР°С€РµР№ РёРЅС„РѕСЂР�?Р°С†РёРё .</r>"
					+ "<r> 4 .Р�?Р°РіР°Р·РёРЅ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІРІРѕРґ РєРѕР�?РµРЅС‚Р°СЂРёРµРІ РѕС‚ РїРѕР»Р·РѕРІР°С‚РµР»РµР№. Р¤РѕСЂСѓР�? РІРѕРїСЂРѕСЃРѕРІ Рё РѕС‚РІРµС‚РѕРІ </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
			soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  ) ";

			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р¤РѕСЂСѓР�? ");
			args.put("description", "<r> Р’ Р�?Р°РіР°Р·РёРЅ РІСЃС‚СЂРѕРµРЅ С„РѕСЂСѓР�? </r>");
			args.put("fulldescription", "<r>"
					+ " 1. РњС‹ РІСЃС‚СЂРѕРёР»Рё С„РѕСЂСѓР�? РІ РЅР°С€Сѓ CMS Business One  </r>" + "<r>"
					+ " РљР°Р¶РґС‹Р№ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р�?РѕР¶РµС‚ РѕРїРёСЃС‹РІР°С‚СЊ С‚РµР�?Сѓ С„РѕСЂСѓР�?Р° </r>"
					+ "<r> Рђ РЅР° СЃС‚СЂР°РЅРёС†Рµ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�? Р�?РѕРіСѓС‚ СЂР°Р·РіР°СЂР°С‚СЊСЃСЏ РґРёСЃРєСѓСЃРёРё РЅР° С‚РµР�?Сѓ РѕРїРёСЃР°РЅРЅСѓСЋ РїРѕРґСЂРѕР±РЅС‹Р�? РѕР±СЂР°Р·РѕР�? РґР°Р¶Рµ СЃ РєР°СЂС‚РёРЅРєР°Р�?Рё  </r>"
					+ "<r> Р’СЃРµ РЅРѕРІС‹Рµ СЃРѕРѕР±С‰РµРЅРёСЏ СЃРѕР±РёСЂР°СЋС‚СЊСЃСЏ РІ С‚РѕРї РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ РґР»СЏ РѕР±Р·РѕСЂР° СЃРІРµР¶РёС… РґРёСЃРєСѓСЃРёР№ </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
			soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�?Р»РѕРє РЅРѕРІРѕСЃС‚Рё СЃР°Р№С‚Р°  ");
			args.put("description", "<r> Р�?Р»РѕРє РЅРѕРІРѕСЃС‚Рё СЃР°Р№С‚Р°  </r>");
			args.put("fulldescription", "<r>"
					+ " 1. РњС‹ РІСЃС‚СЂРѕРёР»Рё Р±Р»РѕРє РЅРѕРІРѕСЃС‚Рё СЃР°Р№С‚Р° РІ РЅР°С€Сѓ CMS Business One  </r>"
					+ "<r> РњРѕРґСѓР»СЊ РЅРѕРІРѕСЃС‚РµР№ РїРѕСЏРІР»СЏРµС‚СЊСЃСЏ РЅР° РІСЃРµС… СЃС‚СЂР°РЅРёС†Р°С… РІРѕ РІСЃРµС… СЂР°Р·РґРµР»Р°С… .</r>"
					+ "<r> СЌС‚Рѕ РїРѕР·РІРѕР»СЏРµС‚ РІР°Р�? РґРѕРЅРµСЃС‚Рё РІР°С€Сѓ РЅРѕРІРѕСЃС‚СЊ Р»СЋР±РѕР�?Сѓ РѕР±РѕР·СЂРµРІР°С‚РµР»СЋ СЃР°Р№С‚Р° РіРґРµ РѕРЅ РЅРµ РЅР°С…РѕРґРёР»СЃСЏ .</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р РµРєР»Р°Р�?Р°  ");
			args.put("description", "<r> Р�?Р»РѕРєРё СЂРµРєР»Р°Р�?С‹ РЅР° Р�?Р°РіР°Р·РёРЅРµ </r>");
			args.put("fulldescription", "<r>"
					+ " 1. РњС‹ РІСЃС‚СЂРѕРёР»Рё РґР»СЏ РІР°СЃ Р±Р»РѕРєРё СЂРµРєР»Р°Р�?С‹ С‡С‚РѕР±С‹ РІС‹ Р�?РѕРіР»Рё РїСЂРѕРґР°РІР°С‚СЊ СЂРµРєР»Р°Р�?РЅРѕРµ Р�?РµСЃС‚Рѕ РЅР° РІР°С€РµР�? Р�?Р°РіР°Р·РёРЅРµ .</r>"
					+ "<r> РЎРїСЂР°РІРѕ Рё СЃР»РµРІР° РѕС‚ С†РµРЅС‚СЂР°Р»СЊРЅРѕ Р±Р»РѕРєР° РёРЅС„РѕСЂР�?Р°С†РёРё </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ?  ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РҐРѕСЃС‚РёРЅРі  ");
			args.put("description",
					"<r> Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ Сѓ РЅР°СЃ С…РѕСЃС‚РёРЅРі СЃ РѕРїР»Р°С‚РѕР№ Р·Р° С‚СЂР°С„РёРє </r>");
			args.put("fulldescription", "<r>"
					+ " 1.  Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ Р±РµСЃРїР»Р°С‚РЅРѕ РґРѕР�?РµРЅ РІ РЅР°С€РµР№ Р·РѕРЅРµ." + "</r>"
					+ "<r> 2. Р’С‹ Р�?РѕР¶РµС‚Рµ Р·Р°РєР°С‡Р°С‚СЊ Сѓ РЅР°СЃ РїР»Р°С‚РЅРѕ РґРѕР�?РµРЅ РІ Р·РѕРЅРµ com , net , biz , info.  </r>"
					+ "<r> 3. Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ Р±РµСЃРїР»Р°С‚РЅРѕ Р�?Р°РіР°Р·РёРЅ .  </r>"
					+ "<r> 4. Р’Р°С€ Р�?Р°РіР°Р·РёРЅ РЅР°С…РѕРґРёС‚СЊСЃСЏ РїРѕ Р°РґСЂРµСЃСѓ http://online-spb.com/Productlist.jsp?site="
					+ site_id + "  </r>" + "<r> 5. Р’Р°С€ РїРѕС‡С‚РѕРІС‹Р№ СЏС‰РёРє: " + login + "@online-spb.com  </r>"
					+ "<r> РџРѕС‡С‚Р° Р·РґРµСЃСЊ: http://www.online-spb.com/webmail/  </r>"
					+ "<r> Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ СЂРµРєР»Р°Р�?РЅСѓСЋ СЃС‚СЂР°РЅРёС†Сѓ РІ РЅР°С€РµР�? Р±РёР·РЅРµСЃ СЃРїСЂР°РІРѕС‡РЅРёРєРµ</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_FROM_NEWS_CATALOG);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ 1.  ");
			args.put("description",
					"<r> Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1 </r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
	        soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РљСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёСЃРєР°  ");
			args.put("description",
					"<r> РљР°Рє РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёСЃРєР° Р�?РѕРµРіРѕ РёРЅС„РѕР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р’Р°С€Рё СЂР°Р·РґРµР»С‹  ");
			args.put("description",
					"<r> РљР°Рє РёР·Р�?РµРЅРёС‚СЊ СЂР°Р·РґРµР» РґР»СЏ РїРѕРёСЃРєР° Р�?РѕРµРіРѕ РёРЅС„РѕР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ  ");
			args.put("description",
					"<r> РњР°РіР°Р·РёРЅ РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РїРѕРёСЃРєР° РїРѕ Р·Р°РіРѕР»РѕРІРєСѓ РїРѕР»СѓР»СЏ </r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "+ " values ( ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РЈ РІР°СЃ РµСЃС‚СЊ Р¤РѕСЂСѓР�? !  ");
			args.put("description",
					"<r> РњР°РіР°Р·РёРЅ РїРѕРґРґРµСЂР¶РёРІР°РµС‚  СЃРёСЃС‚РµР�?Сѓ РѕР±СЃСѓР¶РґРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РЈСЃС‚Р°РЅРѕРІРёС‚СЊ РєР°СЂС‚РёРЅРєСѓ   ");
			args.put("description",
					"<r> Р’С‹ Р�?РѕР¶РµС‚Рµ СѓС‚Р°РЅРѕРІРёС‚СЊ РґРѕ 21 РєР°СЂС‚РёРЅРєРё РґР»СЏ РѕРґРЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РєР°Рє  РѕРїРёСЃР°РЅРёСЏ С‚РѕРІР°СЂР° РёР»Рё РЅРѕРІРѕСЃС‚Рё </r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 0);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"),args.get("image_id"),args.get("bigimage_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id , cost , currency , user_id ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РЎРєР°С‡РёРІР°С‚СЊ С„РёР°Р»С‹   ");
			args.put("description",
					"<r> Р’С‹ Р�?РѕР¶РµС‚Рµ СѓС‚Р°РЅРѕРІРёС‚СЊ С„Р°РёР» РґР»СЏ СЃРєР°С‡РёРІР°РЅРёСЏ РєР°Рє  РѕРїРёСЃР°РЅРёСЏ С‚РѕРІР°СЂР° РёР»Рё РЅРѕРІРѕСЃС‚Рё Рё РїСЂР°Р№СЃ Р»РёСЃС‚.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 0);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			args.put("cost", Double.valueOf("10.0"));
			args.put("currency", 3);
			args.put("user_id", intOwnerUserId);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"),args.get("image_id"),
                args.get("bigimage_id"), args.get("cost"), args.get("currency"), args.get("user_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ?) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р’РѕРїСЂРѕСЃС‹ Рё РѕС‚РІРµС‚С‹   ");
			args.put("description",
					"<r> Р’С‹ Р�?РѕР¶РµС‚Рµ РІРµСЃС‚Рё РґРёСЃРєСѓСЃРёРё РїРѕ С‚РѕРІР°СЂСѓ РёР»Рё РЅРѕРІРѕСЃС‚СЏР�? Рё РїСЂР°Р№СЃ Р»РёСЃС‚Сѓ.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 3);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РЎРїР°СЃРёР±Рѕ Р·Р° РІС‹Р±РѕСЂ Р�?РѕРµР№ CMS  ");
			args.put("description",
					"<r> РЎРїР°СЃРёР±Рѕ РѕС‚ Р°РІС‚РѕСЂР° РљРѕРЅСЃС‚Р°РЅС‚РёРЅР° Р“СЂР°Р±РєРѕ С‡С‚Рѕ РІРѕСЃРїРѕР»СЊР·РѕРІР°Р»РёСЃСЊ CMS Business One.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 3);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ?  )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " CMS Рё Р±Р°Р·С‹ РґР°РЅРЅС‹С… ");
			args.put("description",
					"<r> CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІСЃРµ С‚РёРїС‹ Р±Р°Р· РґР°РЅРЅС‹С… С‡РµСЂРµР· ejb3 С‚РµС…РЅРѕР»РѕРіРёСЋ.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 1);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"),args.get("image_id"),
                args.get("bigimage_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
            query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ? , ? , ? ) ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " CMS Рё СЃРµСЂРІРµСЂР° РїСЂРёР»РѕР¶РµРЅРёР№   ");
			args.put("description",
					"<r> CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІСЃРµ С‚РёРїС‹ СЃРµСЂРІРµСЂРѕРІ РїСЂРёР»РѕР¶РµРЅРёР№ cРїРѕРґРґРµР¶РєРѕР№ J2EE .</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 2);
			args.put("image_id", 10);
			args.put("bigimage_id", 10);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"),args.get("image_id"),
                args.get("bigimage_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "
					+ " values ( ? , ? , ? , ? , ? , ? , ? , ?  ) ; ";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " CMS РєР°Рє Р°СЂС…РёРІ РґРѕРєСѓР�?РµРЅС‚РѕРІ    ");
			args.put("description",
					"<r> CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ С„СѓРЅС†РёРё С…Р°СЂР°РЅРµРЅРёСЏ Рё РїРѕРёСЃРєР° РѕС„РёСЃРЅС‹С… РґРѕРєСѓР�?РµРЅС‚РѕРІ Рё СЃРєР°РЅРёСЂРѕРІР°РЅРЅС‹Рµ РґРѕРєСѓР�?РµРЅС‚С‹ С‚РѕР¶Рµ.</r>");
			args.put("fulldescription", "<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 2);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " CMS РєР°Рє РїР»Р°С‚С„РѕСЂР�?Р° РґРѕРєСѓР�?РµРЅС‚Рѕ-РѕР±РѕСЂРѕС‚Р°    ");
			args.put("description",
					"<r> CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ GBS Workflow container РґР»СЏ СЂРµР°Р»РёР·Р°С†РёРё РїСЂРѕС…РѕР¶РґРµРЅРёСЏ РґРѕРєСѓР�?РµРЅС‚Р° РїРѕ Р�?Р°СЂС€СЂСѓС‚Сѓ .</r>");
			args.put("fulldescription", "<r>"
					+ " CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ GBS Workflow container РґР»СЏ СЂРµР°Р»РёР·Р°С†РёРё РїСЂРѕС…РѕР¶РґРµРЅРёСЏ РґРѕРєСѓР�?РµРЅС‚Р° РїРѕ Р�?Р°СЂС€СЂСѓС‚Сѓ ."
					+ "</r>"
					+ "<r> GBS Workflow container РїРѕР·РІРѕР»СЏРµС‚ РѕС‚Р»Р°Р¶РёРІР°С‚СЊ Р�?Р°СЂС€СЂСѓС‚С‹ РЅР° СѓСЂРѕРІРЅРµ Unit С‚РµСЃС‚РёСЂРѕРІР°РЅРёСЏ.  </r>"
					+ "<r> Р­С‚Рѕ РѕР±СЊРµРґРёРЅСЏРµС‚ РґРІРµ СЂР°Р±РѕС‚С‹ РїРѕ РѕС‚Р»Р°РґРєРё Рё РЅР°РїРёСЃР°РЅРёСЋ Unit С‚РµСЃС‚РѕРІ С‡С‚Рѕ РІ РёС‚РѕРіРµ СЌРєРѕРЅРѕР�?РёС‚ РІСЂРµР�?СЏ Рё РґРµРЅСЊРіРё. </r>"
					+ "<r> РўРµС…РЅРѕРіРёСЏ СЂР°Р·РіСЂР°РЅРёС‡РµРЅРёСЏ РґРѕСЃС‚СѓРїР° Р±Р°Р·РёСЂСѓРµС‚СЊСЃСЏ РЅР° JAAS С‚РµС…РЅРѕР»РѕРіРёРё С‡С‚Рѕ РѕС‚РІРµС‡Р°РµС‚ СЃС‚Р°РЅРґР°СЂС‚Р°Р�? JCP. </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 1);
            jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " CMS РїРѕРґРґРµСЂР¶РєР° Portlet specification 2.0    ");
			args.put("description",
					"<r> CMS Business One  РїРѕРґРґРµСЂР¶РёРІР°РµС‚ Portlet СЃРїРµС†РёС„РёРєР°С†РёСЋ JSR-286 С‡РµСЂРµР· Pluto Portal Driver.</r>");
			args.put("fulldescription", "<r>"
					+ " CMS Business One  РїРѕРґРґРµСЂР¶РёРІР°РµС‚ Portlet СЃРїРµС†РёС„РёРєР°С†РёСЋ JSR-286 С‡РµСЂРµР· Pluto Portal Driver."
					+ "</r>' , " + "'<r>"
					+ " Р”Р»СЏ СЌС‚РѕРіРѕ РІС‹  РґРѕР»Р¶РЅС‹ Р±СѓРґРµС‚Рµ СѓРєР°Р·Р°С‚СЊ РІ xsl С€Р°Р±Р»РѕРЅРµ РІ РєР°РєРѕРµ Р�?РµСЃС‚Рѕ Р·Р°РіСЂСѓР¶Р°С‚СЊ  Portlet ."
					+ "</r>" + "<r> РћР±С‹С‡РЅРѕ СѓРєР°Р·С‹РІР°РµС‚СЊСЃСЏ URL РЅР° portlet С‡РµСЂРµР· iframe </r>"
					+ "<r> Р­С‚Рѕ РїРѕР·РІРѕР»СЏРµС‚ РїСЂРµРЅРѕСЃРёС‚СЊ РїСЂРёР»РѕР¶РµРЅРёСЏ РёР· РґСЂСѓРіРёС… РїРѕСЂС‚Р°Р»РѕРІ С‚Р°РєРёС… РєР°Рє IBM WebSphera , Oracle portal , Sun portal </r>"
					+ "<r> С‡С‚Рѕ СЃС‚Р°РІРёС‚ CMS Business One РЅР°  СѓСЂРѕРІРµРЅСЊ РєСЂСѓРїРЅС‹С… РїСЂРѕР�?С‹С€Р»РµРЅС‹С… СЂРµС€РµРЅРёР№. </r>"
					+ "<r> (Pluto Portal Driver) http://portals.apache.org/pluto/faq.html </r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 2);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " CMS РїРѕРґРґРµСЂР¶РєР° Ajax technology    ");
			args.put("description",
					"<r> CMS Business One  РїРѕРґРґРµСЂР¶РёРІР°РµС‚ Ajax technology С‡РµСЂРµР· GBS Ajax framework.</r>");
			args.put("fulldescription", "<r>"
					+ " CMS Business One  РїРѕРґРґРµСЂР¶РёРІР°РµС‚ Portlet СЃРїРµС†РёС„РёРєР°С†РёСЋ JSR-286 С‡РµСЂРµР· Pluto Portal Driver."
					+ "</r>' , " + "'<r>"
					+ " GBS Ajax framework СЌС‚Рѕ РѕР±СЂР°Р±РѕС‚С‡РёРє СЃРѕР±С‹С‚РёР№ РЅР° java РѕС‚ Ajax РёРЅС‚РµСЂС„РµР№СЃР° ."
					+ "</r>"
					+ "<r> РћРЅ РёРЅС‚РµРіСЂРёСЂРѕРІР°РЅ РІ GBS Web famework ( base  by front controller with loading action classes ) </r>"
					+ "<r> СЂР°Р·С€РёРЅРµРЅРЅС‹Р№ РѕС‚ С€Р°Р±Р»РѕРЅР° Front controller СЃ РїРѕРґСЂРµР¶РєРѕР№ СЃРѕР±С‹С‚РёР№РЅС‹С… РєР»Р°СЃСЃРѕРІ Рё СЃРµСЂРІРёСЃР° С‚СЂР°РЅСЃС„РѕСЂР�?Р°С†РёР№ xsl С€Р°Р±Р»РѕРЅРѕРІ Рё РєРµС€РёСЂРѕРІР°РЅРёСЏ </r>"
					+ "<r> РЎР°Р�?РѕРµ РІР°Р¶РЅРѕРµ С‡С‚Рѕ Рє СЃРѕР±С‹С‚РёР№РЅРѕР�?Сѓ РєР»Р°СЃСЃСѓ web famework РґРѕР±Р°РІР»СЏСЋС‚СЃСЏ РєР»Р°СЃСЃС‹ Ajax РєРѕРЅС‚СЂРѕР»РѕРІ Рё РєР»Р°СЃСЃРѕРІ СЂРёСЃРѕРІР°РЅРёСЏ РёС… С‡С‚Рѕ РѕС‡РµРЅСЊ СѓРґРѕР±РЅРѕ РѕС‚Р»Р°Р¶РёРІР°С‚СЊ. </r>"
					+ "<r> Рљ С‚РѕР�?Сѓ Р¶Рµ, html РєРѕРґ РєРѕРЅС‚СЂРѕР»Р° РІСЃС‚Р°РІР»СЏРµС‚СЊСЃСЏ С‡РµСЂРµР·  innerHTML Р�?РµС‚РѕРґ , С‡С‚Рѕ РїРѕР·РІРѕР»СЏРµС‚СЊ РЅРµ РёСЃРїРѕР»СЊРІР°С‚СЊ РіРµРЅРµСЂР°С†РёСЋ РєРѕРЅС‚СЂРѕР»РѕРІ РёР· JavaScipt С‡С‚Рѕ СѓР�?РµРЅСЊС€Р°РµС‚ СЂРёСЃРєРё РїРѕ РІРѕР·РЅРёРєРЅРѕРІРµРЅРёСЋ РѕС€РёР±РѕРє РїРѕРґ РґСЂСѓРіРёР�?Рё Р±СЂР°СѓР·РµСЂР°Р�?Рё .</r>");
			args.put("catalog_id", SpecialCatalog.OUTPUT_PAGES_SORT_BY_SOFT_ID);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", 2);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " Р�РЅС„РѕСЂР�?Р°С†РёСЏ Рѕ РєРѕР�?РїР°РЅРёРё   ");
			args.put("description", "<r> Р�РЅС„РѕСЂР�?Р°С†РёСЏ Рѕ РєРѕР�?РїР°РЅРёРё.</r>");
			args.put("fulldescription",
					"<r>" + " РќР°Р·РІР°РЅРёРµ РєРѕР�?РїР°РЅРёРё: " + AuthorizationPageBeanId.getStrCompany() + "</r>"
							+ "<r> РўРµР»РµС„РѕРЅ: " + AuthorizationPageBeanId.getStrPhone() + " </r>"
							+ "<r> Р¤Р°РєСЃ: " + AuthorizationPageBeanId.getStrFax() + " </r>" + "<r> РџРѕС‡С‚Р°: "
							+ AuthorizationPageBeanId.getStrEMail() + " </r>");
			args.put("catalog_id", SpecialCatalog.FOR_EXTERNAL_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PAGE_ABOUT_COMPANY);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));
            soft_id = jdbcTemplate.queryForObject(soft_sequence_rs, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "+ " values ( ? , ? , ? , ? , ? , ? , ? , ? )";
			args.clear();
			args.put("soft_id", Long.valueOf(soft_id));
			args.put("name", " РљР°Рє РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р·   ");
			args.put("description", "<r> РљР°Рє РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р·.</r>");
			args.put("fulldescription", "<r>" + " Р§С‚Рѕ Р±С‹ РѕРїР»Р°С‚РёС‚СЊ Р·Р°РєР°Р· РєРѕР�?РїР°РЅРёРё: "
					+ AuthorizationPageBeanId.getCompany_name() + "</r>"
					+ "<r> РќСѓР¶РЅРѕ РїРµСЂРµРІРµСЃС‚Рё РґРµРЅСЊ РіРё РЅР° СЃС‡РµС‚ РєРѕР�?РїР°РЅРёРё С‡РµСЂРµР· РїР»Р°С‚РµР¶РЅС‹Рµ СЃРёСЃС‚РµР�?С‹ РёР»Рё С‡РµСЂРµР· Р±Р°РЅРє РїСЂСЏР�?С‹Р�? РїР»Р°С‚РµР¶РѕР�? </r>"
					+ "<r> РЎС‡РµС‚ Web Money: 1111222333444 </r>" + "<r> РЎС‡РµС‚ Yandex Money: 333442234455 </r>"
					+ "<r> Р�?Р°РЅРє РћРђРћ Р РѕРіР° Рё РєР°РїС‹С‚Р°  p/c 333334444 k/c 3335335522 </r>");
			args.put("catalog_id", SpecialCatalog.FOR_EXTERNAL_PAGE);
			args.put("site_id", Long.valueOf(site_id));
			args.put("active", true);
			args.put("portlettype_id", Layout.PAGE_ABOUT_PAY);
			jdbcTemplate.update(query, args.get("soft_id"),args.get("name"),args.get("description"),args.get("fulldescription"),args.get("catalog_id"),args.get("site_id"),args.get("active"),args.get("portlettype_id"));

//			query = "select   file.name , file.path from soft  LEFT  JOIN file  ON soft.file_id = file.file_id  where soft_id = " + product_id ;
//			
//			dbAdaptor.executeQuery(query);
//			file_name = (String) dbAdaptor.getValueAt(0, 0);
//			file_path = (String) dbAdaptor.getValueAt(0, 1);
//			if( !file_path.startsWith("/")) file_path = "/" + file_path ;
//			dbAdaptor.commit();
//			cretareSiteDirWithExtract(file_name, file_path, site_dir);
			transactionSupport.commitTransaction(trxnStatus);
			cretareSiteDir(AuthorizationPageBeanId.getStrWebsite(), AuthorizationPageBeanId.getStrWebsite());

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		} 
	}

	public void addSite(long user_id) {
		String query = "";
		TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
        JdbcTemplate jdbcTemplate =  transactionSupport.getJdbcTemplate();
		try {
			site_id = jdbcTemplate.queryForObject(site_sequence_rs, String.class);

			query = "insert into site (site_id , owner , host , home_dir , site_dir , person , phone  , address , active ) "
					+ " values ( " + "" + site_id + " , " + "" + user_id + " , " + "'" + host + "' , " + "'" + home_dir
					+ "' , " + "'" + site_dir + "' , " + "'" + person + "' , " + "'" + phone + "' , " + "'" + address
					+ "' , " + "true" + " ) ; ";

			jdbcTemplate.update(query);
			long intUserID = Long.parseLong((String)jdbcTemplate.queryForObject(tuser_sequence_rs, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( " + "" + intUserID + " , " + "'" + login + "' , " + "'" + passwd + "' , " + " NULL , "
					+ " true  , " + " true  , " + " NOW , " + "" + 2 + " , " + "" + 0 + " , " + "" + currency_id + ", "
					+ "" + site_id + ", " + "" + city_id + ", " + "" + country_id + " " + " )";
			;

			jdbcTemplate.update(query);
			account_id = jdbcTemplate.queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( " + "" + account_id + " , " + "" + intUserID + " , " + "" + 0 + " , " + "" + 3 + " , "
					+ " NOW , " + "' new_account ', " + "" + currency_id + " " + " ) ; ";

			jdbcTemplate.update(query);
			intUserID = Long.parseLong(jdbcTemplate.queryForObject(tuser_sequence_rs, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( " + "" + intUserID + " , " + "'user' , " + "'user' , " + " NULL , " + " true  , "
					+ " true  , " + " NOW , " + "" + 0 + " , " + "" + 0 + " , " + "" + currency_id + ", " + "" + site_id
					+ ", " + "" + city_id + ", " + "" + country_id + " " + " )";
			;

			jdbcTemplate.update(query);
			account_id =  jdbcTemplate.queryForObject(account_sequence_rs, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( " + "" + account_id + " , " + "" + intUserID + " , " + "" + 0 + " , " + "" + 3 + " , "
					+ " NOW , " + "' new_account ', " + "" + currency_id + " " + " ) ; ";

			jdbcTemplate.update(query);

			// ==== end add users =========================

			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "
					+ " values ( " + "" + 84473 + " , " + "" + intUserID + " , " + "'" + "HCO-CENTE-406" + "' , " + "'"
					+ "91KiBFRtE8fF7VHc8tvr" + "' , " + "" + 1 + " , " + "" + site_id + " , " + " NOW " + " ) ; ";

			jdbcTemplate.update(query);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ "-1 , " + "" + site_id + " , " + "'" + "РќРѕРІРѕСЃС‚Рё" + "' , " + "" + "true" + " , " + "" + 1
					+ " , " + "" + 0 + " ) ; ";

			jdbcTemplate.update(query);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ "-2 , " + "" + site_id + " , " + "'" + "Р“Р»Р°РІРЅР°СЏ СЃС‚СЂР°РЅРёС†Р°" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + 0 + " ) ; ";

			jdbcTemplate.update(query);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ "-3 , " + "" + site_id + " , " + "'" + "РќР° РіР»Р°РІРЅС‹Р№ СЃР°Р№С‚" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + 0 + " ) ; ";

			jdbcTemplate.update(query);

			String catalog_id = "";
			String parent_catalog_id = "";
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "Р Р°Р·РґРµР»1" + "' , " + "" + "true" + " , "
					+ "" + 1 + " , " + "" + 0 + " ) ; ";

            jdbcTemplate.update(query);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "РџРѕРґ СЂР°Р·РґРµР»1" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + parent_catalog_id + " ) ; ";

            jdbcTemplate.update(query);
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "Р Р°Р·РґРµР»2" + "' , " + "" + "true" + " , "
					+ "" + 1 + " , " + "" + 0 + " ) ; ";

            jdbcTemplate.update(query);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "РџРѕРґ СЂР°Р·РґРµР»2" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + parent_catalog_id + " ) ; ";

            jdbcTemplate.update(query);
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "Р Р°Р·РґРµР»3" + "' , " + "" + "true" + " , "
					+ "" + 1 + " , " + "" + 0 + " ) ; ";

			jdbcTemplate.update(query);
			parent_catalog_id = catalog_id;
			catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);


			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "РџРѕРґ СЂР°Р·РґРµР»3" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + parent_catalog_id + " ) ; ";

			jdbcTemplate.update(query);
            catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "Р Р°Р·РґРµР»4" + "' , " + "" + "true" + " , "
					+ "" + 1 + " , " + "" + 0 + " ) ; ";

			jdbcTemplate.update(query);
            parent_catalog_id = catalog_id;
            catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "РџРѕРґ СЂР°Р·РґРµР»4" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + parent_catalog_id + " ) ; ";

			jdbcTemplate.update(query);
            catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "Р Р°Р·РґРµР»5" + "' , " + "" + "true" + " , "
					+ "" + 1 + " , " + "" + 0 + " ) ; ";

			jdbcTemplate.update(query);
            parent_catalog_id = catalog_id;
            catalog_id = jdbcTemplate.queryForObject(tcatalog_sequence_rs, String.class);
			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ catalog_id + ", " + "" + site_id + " , " + "'" + "РџРѕРґ СЂР°Р·РґРµР»5" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + parent_catalog_id + " ) ; ";

	        jdbcTemplate.update(query);
			String creteria_id = "";
			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria1 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id + ",'РљСЂРёС‚РµСЂРёР№1')";
            jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria1 VALUES(" + creteria_id + ",'РўРµСЃС‚1',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№1')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA1_ID) + 1  as ID FROM creteria1";
	        creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria1 VALUES(" + creteria_id + ",'РўРµСЃС‚2',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№1')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA2_ID) + 1  as ID FROM creteria2";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria2 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№2')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA3_ID) + 1  as ID FROM creteria3";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria3 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№3')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA4_ID) + 1  as ID FROM creteria4";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria4 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№4')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA5_ID) + 1  as ID FROM creteria5";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria5 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№5')";
			jdbcTemplate.update(query);


			query = "SELECT MAX(CRETERIA6_ID) + 1  as ID FROM creteria6";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria6 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№6')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA7_ID) + 1  as ID FROM creteria7";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria7 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№7')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA8_ID) + 1  as ID FROM creteria8";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria8 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№8')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA9_ID) + 1  as ID FROM creteria9";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria9 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№9')";
			jdbcTemplate.update(query);

			query = "SELECT MAX(CRETERIA10_ID) + 1  as ID FROM creteria10";
			creteria_id = jdbcTemplate.queryForObject(query, String.class);
			query = "INSERT INTO creteria10 VALUES(" + creteria_id + ",'РќРµ РІС‹Р±СЂР°РЅРѕ',TRUE,NULL,0," + site_id
					+ ",'РљСЂРёС‚РµСЂРёР№10')";
			jdbcTemplate.update(query);

			// insert into catalog (catalog_id , site_id , lable , active
			// ,lang_id , parent_id ) values ( -2 , 2 , 'Р“Р»Р°РІРЅР°СЏ
			// СЃС‚СЂР°РЅРёС†Р°' , true , 1 , 0 ) ;

			query = soft_sequence_rs;
			String soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " РџРѕРёСЃРєРѕРІР°СЏ СЃРёСЃС‚РµР�?Р° РїРѕСЂС‚Р°Р»Р°  "
					+ "' , " + "'"
					+ "<r>РќР°СЃС‚СЂРѕР№РєР° РїРѕРёСЃРєР° РїРµСЂРµРґ РЅР°С‡Р°Р»РѕР�? </r><r> СЂР°Р±РѕС‚С‹ РїРѕСЂС‚Р°Р»Р° .</r>"
					+ "' , " + "'"
					+ "<r>1. Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ CMS Business One РІР°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРµС‚ С„РѕСЂР�?Сѓ СЂР°Р·С€РёСЂРµРЅРЅРѕРіРѕ РїРѕРёСЃРєР° РїРѕ РєСЂРёС‚РµСЂРёСЏР�? ,</r>"
					+ "<r> Рё РЅР°СЃС‚СЂРѕРёС‚СЊ РµРµ РїРѕРґ РІР°С€Рё РЅСѓР¶РґС‹ Рё С‡С‚РѕР±С‹ РІР°С€Рё РєР»РёРµРЅС‚С‹ Р�?РѕРіР»Рё СЃСЂР°Р·Сѓ РЅР°С…РѕРґРёС‚СЊ РЅСѓР¶РЅС‹Р№ С‚РѕРІР°СЂ РёР»Рё РёРЅС„РѕСЂР�?Р°С†РёСЋ РёР»Рё РґРѕРєСѓР�?РµРЅС‚С‹ .</r>"
					+ "<r> РІС‹ Р�?РѕР¶Рµ РїРѕСЃР�?РѕС‚СЂРµС‚СЊ РЅР°С€ РІРёРґРµРѕ РєСѓСЂСЃ Р±РµСЃРїР»Р°С‚РЅРѕ РєР°Рє РЅР°СЃС‚СЂРѕРёС‚СЊ С„РѕСЂСѓ СЂР°Р·С€РёСЂРµРЅРЅРѕРіРѕ РїРѕРёСЃРєР°.</r>"
					+ "<r>2. РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ. РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ РЅР°СЃС‚СЂР°РёРІР°С‚СЊ РЅРµ РЅСѓР¶РЅРѕ РѕРЅ СЂР°Р±РѕС‚Р°РµС‚ РїРѕ СѓР�?РѕР»С‡Р°РЅРёСЋ </r>"
					+ "<r>3. РџРѕРёСЃРє РїРѕ РїРµСЂРІРѕР№ Р±СѓРєРІРµ. РџРѕРёСЃРє РїРѕ РїРµСЂРІРѕР№ Р±СѓРєРІРµ РґРѕР±Р°РІР»СЏРµС‚СЊСЃСЏ РїРѕ СЃРѕРіР»Р°СЃРѕРІР°РЅРёСЋ СЃ Р·Р°РєР°Р·С‡РёРєРѕР�?. </r>"
					+ "<r>4. РџРѕРёСЃРє РїРѕ СЂСѓР±СЂРёРєР°С‚РѕСЂСѓ. РџРѕРёСЃРє РїРѕ СЂСѓР±СЂРёРєР°С‚РѕСЂСѓ РґРѕР±Р°РІР»СЏРµС‚СЊСЃСЏ РїРѕ СЃРѕРіР»Р°СЃРѕРІР°РЅРёСЋ СЃ Р·Р°РєР°Р·С‡РёРєРѕР�?. </r>"
					+ "<r> РњС‹ РІР°Р�? РїСЂРµРґР»РѕРіР°РµР�? 4 СЃРёСЃС‚РµР�?С‹ РїРѕРёСЃРєР° РµСЃР»Рё РІР°Р�? РЅСѓР¶РЅРѕ Р±РѕР»СЊС€Рµ Р�?С‹ СЃРґРµР»Р°РµР�? РµС‰Рµ. </r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
			soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'"
					+ " Р�Р·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРё РЅР° СЃР°Р№С‚Рµ  " + "' , " + "'<r>"
					+ " Р�Р·Р�?РµРЅРµРЅРёРµ СЃРѕРґРµСЂР¶Р°РЅРёСЏ РїРµСЂРµРґ РЅР°С‡Р°Р»РѕР�? </r><r> СЂР°Р±РѕС‚С‹ РїРѕСЂС‚Р°Р»Р°."
					+ "</r>' , " + "'<r>"
					+ " 1. Р”Р»СЏ РїРѕР»РЅРѕС†РµРЅРЅРѕР№ СЂР°Р±РѕС‚С‹ РІР°С€РµРіРѕ РїРѕСЂС‚Р°Р»Р° РЅР° РѕСЃРЅРѕРІРµ CMS Business One  </r>"
					+ "<r>"
					+ " РІР°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ РІР°С€РµРіРѕ РїРѕСЂС‚Р°Р»Р°. </r>"
					+ "<r> Р”Р»СЏ СЌС‚РѕРіРѕ СЃСѓС‰РµС‚РІСѓРµС‚ СЃРїРµС†РёР°Р»СЊРЅР°СЏ С„РѕСЂР�?Р° РґР»СЏ РІРІРѕРґР° Рё СЂРµРґР°С‚РёСЂРѕРІР°РЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё </r>"
					+ "<r> Р’ РєР°Р¶РґС‹Р№ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р±Р»РѕРє РІС‹ Р�?РѕР¶РµС‚Рµ РїСЂРёРєСЂРµРїР»СЏС‚СЊ РєР°СЂС‚РёРЅРєСѓ Рё С‚РµРєСЃС‚ РґР»СЏ РєСЂР°С‚РєРѕРіРѕ РѕРїРёСЃР°РЅРёСЏ</r>"
					+ "<r> РЅР° СЃС‚СЂР°РЅРёС†Рµ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�? Р�?РѕР¶РЅРѕ СЂР°Р·Р�?РµСЃС‚СЊ 21 РєР°СЂС‚РёРЅРєСѓ Рё С‚РµРєСЃС‚ Рё РїСЂРёРєСЂРµРїРёС‚СЊ 10  С„Р°Р№Р»РѕРІ .</r>"
					+ "<r> 2. РџРѕСЂС‚Р°Р» Р°РІС‚РѕР�?Р°С‚РёС‡РµСЃРєРё СѓС‡РёС‚С‹РІР°РµС‚ РєРѕР»РёС‡РµСЃС‚РІРѕ РїРѕСЃР�?РѕС‚СЂРѕРІ РІР°С€РµР№ СЃС‚СЂР°РЅРёС†С‹ .</r>"
					+ "<r> 3. РџРѕСЂС‚Р°Р» РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РѕС†РµРЅРєСѓ СЂРµР№С‚РёРЅРіР° СЌС‚РѕР№ РёРЅС„РѕСЂР�?Р°С†РёРё </r>"
					+ "<r> С‡С‚Рѕ РїРѕР·РІРѕР»СЏРµС‚ РІР°Р�? РѕС†РµРЅРёСЃР°С‚СЊ РєР°С‡РµСЃС‚РІРѕ РІР°С€РµР№ РёРЅС„РѕСЂР�?Р°С†РёРё .</r>"
					+ "<r> 4 .РџРѕСЂС‚Р°Р» РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІРІРѕРґ РєРѕР�?РµРЅС‚Р°СЂРёРµРІ РѕС‚ РїРѕР»Р·РѕРІР°С‚РµР»РµР№. Р¤РѕСЂСѓР�? РІРѕРїСЂРѕСЃРѕРІ Рё РѕС‚РІРµС‚РѕРІ </r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
			soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " Р¤РѕСЂСѓР�? " + "' , " + "'<r>"
					+ " Р’ РїРѕСЂС‚Р°Р» РІСЃС‚СЂРѕРµРЅ С„РѕСЂСѓР�? " + "</r>' , " + "'<r>"
					+ " 1. РњС‹ РІСЃС‚СЂРѕРёР»Рё С„РѕСЂСѓР�? РІ РЅР°С€Сѓ CMS Business One  </r>" + "<r>"
					+ " РљР°Р¶РґС‹Р№ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ Р�?РѕР¶РµС‚ РѕРїРёСЃС‹РІР°С‚СЊ С‚РµР�?Сѓ С„РѕСЂСѓР�?Р° </r>"
					+ "<r> Рђ РЅР° СЃС‚СЂР°РЅРёС†Рµ СЃ РїРѕРґСЂРѕР±РЅС‹Р�? РѕРїРёСЃР°РЅРёРµР�? Р�?РѕРіСѓС‚ СЂР°Р·РіР°СЂР°С‚СЊСЃСЏ РґРёСЃРєСѓСЃРёРё РЅР° С‚РµР�?Сѓ РѕРїРёСЃР°РЅРЅСѓСЋ РїРѕРґСЂРѕР±РЅС‹Р�? РѕР±СЂР°Р·РѕР�? РґР°Р¶Рµ СЃ РєР°СЂС‚РёРЅРєР°Р�?Рё  </r>"
					+ "<r> Р’СЃРµ РЅРѕРІС‹Рµ СЃРѕРѕР±С‰РµРЅРёСЏ СЃРѕР±РёСЂР°СЋС‚СЊСЃСЏ РІ С‚РѕРї РЅР° РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†Рµ РґР»СЏ РѕР±Р·РѕСЂР° СЃРІРµР¶РёС… РґРёСЃРєСѓСЃРёР№ </r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
			soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " Р�?Р»РѕРє РЅРѕРІРѕСЃС‚Рё СЃР°Р№С‚Р° " + "' , "
					+ "'<r>" + " Р�?Р»РѕРє РЅРѕРІРѕСЃС‚Рё СЃР°Р№С‚Р° " + "</r>' , " + "'<r>"
					+ " 1. РњС‹ РІСЃС‚СЂРѕРёР»Рё Р±Р»РѕРє РЅРѕРІРѕСЃС‚Рё СЃР°Р№С‚Р° РІ РЅР°С€Сѓ CMS Business One  </r>"
					+ "<r> РњРѕРґСѓР»СЊ РЅРѕРІРѕСЃС‚РµР№ РїРѕСЏРІР»СЏРµС‚СЊСЃСЏ РЅР° РІСЃРµС… СЃС‚СЂР°РЅРёС†Р°С… РІРѕ РІСЃРµС… СЂР°Р·РґРµР»Р°С… .</r>"
					+ "<r> СЌС‚Рѕ РїРѕР·РІРѕР»СЏРµС‚ РІР°Р�? РґРѕРЅРµСЃС‚Рё РІР°С€Сѓ РЅРѕРІРѕСЃС‚СЊ Р»СЋР±РѕР�?Сѓ РѕР±РѕР·СЂРµРІР°С‚РµР»СЋ СЃР°Р№С‚Р° РіРґРµ РѕРЅ РЅРµ РЅР°С…РѕРґРёР»СЃСЏ .</r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
			soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " Р РµРєР»Р°Р�?Р° " + "' , " + "'<r>"
					+ " Р�?Р»РѕРєРё СЂРµРєР»Р°Р�?С‹ РЅР° РїРѕСЂС‚Р°Р»Рµ " + "</r>' , " + "'<r>"
					+ " 1. РњС‹ РІСЃС‚СЂРѕРёР»Рё РґР»СЏ РІР°СЃ Р±Р»РѕРєРё СЂРµРєР»Р°Р�?С‹ С‡С‚РѕР±С‹ РІС‹ Р�?РѕРіР»Рё РїСЂРѕРґР°РІР°С‚СЊ СЂРµРєР»Р°Р�?РЅРѕРµ Р�?РµСЃС‚Рѕ РЅР° РІР°С€РµР�? РїРѕСЂС‚Р°Р»Рµ .</r>"
					+ "<r>"
					+ " РЎРїСЂР°РІРѕ Рё СЃР»РµРІР° РѕС‚ С†РµРЅС‚СЂР°Р»СЊРЅРѕ Р±Р»РѕРєР° РёРЅС„РѕСЂР�?Р°С†РёРё </r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " РҐРѕСЃС‚РёРЅРі " + "' , " + "'<r>"
					+ " РҐРѕСЃС‚РёРЅРі РїРѕР»СѓС‡Р°РµС‚Рµ Сѓ РЅР°СЃ С…РѕСЃС‚РёРЅРі РїРѕ 300 СЂСѓР±Р»РµР№ Р�?РµСЃСЏС† "
					+ "</r>' , " + "'<r>"
					+ " 1.  Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ Р±РµСЃРїР»Р°С‚РЅРѕ РґРѕР�?РµРЅ РІ РЅР°С€РµР№ Р·РѕРЅРµ." + "</r>"
					+ "<r> 2. Р’С‹ Р�?РѕР¶РµС‚Рµ Р·Р°РєР°С‡Р°С‚СЊ Сѓ РЅР°СЃ РїР»Р°С‚РЅРѕ РґРѕР�?РµРЅ РІ Р·РѕРЅРµ com , net , biz , info.  </r>"
					+ "<r> 3. Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ Р±РµСЃРїР»Р°С‚РЅРѕ РїРѕСЂС‚Р°Р» .  </r>"
					+ "<r> 4. Р’Р°С€ РїРѕСЂС‚Р°Р» РЅР°С…РѕРґРёС‚СЊСЃСЏ РїРѕ Р°РґСЂРµСЃСѓ http://www.online-spb.com/Productlist.jsp?site="
					+ site_id + "  </r>" + "<r> 5. Р’Р°С€ РїРѕС‡С‚РѕРІС‹Р№ СЏС‰РёРє: " + login + "@online-spb.com  </r>"
					+ "<r> РџРѕС‡С‚Р° Р·РґРµСЃСЊ: http://www.online-spb.com/webmail/  </r>"
					+ "<r> Р’С‹ РїРѕР»СѓС‡Р°РµС‚Рµ СЂРµРєР»Р°Р�?РЅСѓСЋ СЃС‚СЂР°РЅРёС†Сѓ РІ РЅР°С€РµР�? Р±РёР·РЅРµСЃ СЃРїСЂР°РІРѕС‡РЅРёРєРµ</r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ 1. "
					+ "' , " + "'<r>"
					+ " Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1"
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " РљСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёСЃРєР° " + "' , "
					+ "'<r>"
					+ " РљР°Рє РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёСЃРєР° Р�?РѕРµРіРѕ РёРЅС„РѕР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ"
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " Р’Р°С€Рё СЂР°Р·РґРµР»С‹ " + "' , " + "'<r>"
					+ " РљР°Рє РёР·Р�?РµРЅРёС‚СЊ СЂР°Р·РґРµР» РґР»СЏ РїРѕРёСЃРєР° Р�?РѕРµРіРѕ РёРЅС„РѕР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ"
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " РџРѕРёСЃРє РїРѕ С‚РµРєСЃС‚Сѓ " + "' , " + "'<r>"
					+ " РџРѕСЂС‚Р°Р» РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РїРѕРёСЃРєР° РїРѕ Р·Р°РіРѕР»РѕРІРєСѓ РїРѕР»СѓР»СЏ"
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "РЈ РІР°СЃ РµСЃС‚СЊ Р¤РѕСЂСѓР�? ! " + "' , " + "'<r>"
					+ " РџРѕСЂС‚Р°Р» РїРѕРґРґРµСЂР¶РёРІР°РµС‚  СЃРёСЃС‚РµР�?Сѓ РѕР±СЃСѓР¶РґРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ."
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "РЈСЃС‚Р°РЅРѕРІРёС‚СЊ РєР°СЂС‚РёРЅРєСѓ " + "' , "
					+ "'<r>"
					+ " Р’С‹ Р�?РѕР¶РµС‚Рµ СѓС‚Р°РЅРѕРІРёС‚СЊ РґРѕ 21 РєР°СЂС‚РёРЅРєРё РґР»СЏ РѕРґРЅРѕРіРѕ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РєР°Рє  РѕРїРёСЃР°РЅРёСЏ С‚РѕРІР°СЂР° РёР»Рё РЅРѕРІРѕСЃС‚Рё"
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "0" + " , " + "10" + " , " + "10"
					+ "" + " ) ; ";

			jdbcTemplate.update(query);

			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "РЎРєР°С‡РёРІР°С‚СЊ С„РёР°Р»С‹ " + "' , " + "'<r>"
					+ " Р’С‹ Р�?РѕР¶РµС‚Рµ СѓС‚Р°РЅРѕРІРёС‚СЊ С„Р°РёР» РґР»СЏ СЃРєР°С‡РёРІР°РЅРёСЏ РєР°Рє  РѕРїРёСЃР°РЅРёСЏ С‚РѕРІР°СЂР° РёР»Рё РЅРѕРІРѕСЃС‚Рё Рё РїСЂР°Р№СЃ Р»РёСЃС‚ "
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "0" + " , " + "10" + " , " + "10"
					+ "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id) "
					+ " values ( " + "" + soft_id + " , " + "'" + "Р’РѕРїСЂРѕСЃС‹ Рё РѕС‚РІРµС‚С‹ " + "' , " + "'<r>"
					+ " Р’С‹ Р�?РѕР¶РµС‚Рµ РІРµСЃС‚Рё РґРёСЃРєСѓСЃРёРё РїРѕ С‚РѕРІР°СЂСѓ РёР»Рё РЅРѕРІРѕСЃС‚СЏР�? Рё РїСЂР°Р№СЃ Р»РёСЃС‚Сѓ "
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "3" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id) "
					+ " values ( " + "" + soft_id + " , " + "'" + "РЎРїР°СЃРёР±Рѕ Р·Р° РІС‹Р±РѕСЂ Р�?РѕРµР№ CMS "
					+ "' , " + "'<r>"
					+ " РЎРїР°СЃРёР±Рѕ РѕС‚ Р°РІС‚РѕСЂР° РљРѕРЅСЃС‚Р°РЅС‚РёРЅР° Р“СЂР°Р±РєРѕ С‡С‚Рѕ РІРѕСЃРїРѕР»СЊР·РѕРІР°Р»РёСЃСЊ CMS Business One"
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "3" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "CMS Рё Р±Р°Р·С‹ РґР°РЅРЅС‹С…  " + "' , " + "'<r>"
					+ " CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІСЃРµ С‚РёРїС‹ Р±Р°Р· РґР°РЅРЅС‹С… С‡РµСЂРµР· ejb3 С‚РµС…РЅРѕР»РѕРіРёСЋ  "
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "1" + " , " + "10" + " , " + "10"
					+ "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id , image_id , bigimage_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "CMS Рё СЃРµСЂРІРµСЂР° РїСЂРёР»РѕР¶РµРЅРёР№  "
					+ "' , " + "'<r>"
					+ " CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ РІСЃРµ С‚РёРїС‹ СЃРµСЂРІРµСЂРѕРІ РїСЂРёР»РѕР¶РµРЅРёР№ cРїРѕРґРґРµР¶РєРѕР№ J2EE "
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "2" + " , " + "10" + " , " + "10"
					+ "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "CMS РєР°Рє Р°СЂС…РёРІ РґРѕРєСѓР�?РµРЅС‚РѕРІ "
					+ "' , " + "'<r>"
					+ " CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ С„СѓРЅС†РёРё С…Р°СЂР°РЅРµРЅРёСЏ Рё РїРѕРёСЃРєР° РѕС„РёСЃРЅС‹С… РґРѕРєСѓР�?РµРЅС‚РѕРІ Рё СЃРєР°РЅРёСЂРѕРІР°РЅРЅС‹Рµ РґРѕРєСѓР�?РµРЅС‚С‹ С‚РѕР¶Рµ."
					+ "</r>' , " + "'<r>"
					+ "  Р�РЅС„РѕСЂР�?Р°С†РёРѕРЅРЅС‹Р№ Р�?РѕРґСѓР»СЊ РґР»СЏ РіР»Р°РІРЅРѕР№ СЃС‚СЂР°РЅРёС†С‹ 1." + "</r>"
					+ "<r> Р’Р°Р�? РЅСѓР¶РЅРѕ РёР·Р�?РµРЅРёС‚СЊ СЃРѕРґРµСЂР¶Р°РЅРёРµ СЌС‚РѕРіРѕ Р�?РѕРґСѓР»СЏ РЅР° СЃРІРѕРµ  </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РІС‹ РґРѕР»Р¶РЅС‹ РІРѕР№С‚Рё РЅР° СЃР°РёС‚ РїРѕРґ СЃРѕРёР�? РїР°СЂРѕР»РµР�?  </r>"
					+ "<r> Сѓ РІР°СЃ РїРѕСЏРІСЏС‚СЊСЃСЏ РєРЅРѕРїРєРё: СѓРґР°Р»РёС‚СЊ,СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ ,РѕР±РЅРѕРІРёС‚СЊ(С‚РѕРµСЃС‚СЊ РґРѕР±Р°РІРёС‚СЊ РЅРѕРІС‹Р№ Р�?РѕРґСѓР»СЊ). </r>"
					+ "<r> РџСЂРё РёР·Р�?РµРЅРµРЅРёРµ РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ РІС‹ Р�?РѕР¶РµС‚Рµ РїРѕР»РѕР¶РёС‚СЊ РµРіРѕ РґСЂСѓРіРѕР№ СЂР°Р·РґРµР» .</r>"
					+ "<r> Р Р°Р·РґРµР»С‹ РІС‹ С‚Р°РєР¶Рµ Р�?РѕР¶РµС‚Рµ СЂРµРґР°РєС‚РёСЂРѕРІР°С‚СЊ : РґРѕР±Р°РІР»СЏС‚СЊ , СѓРґР°Р»СЏС‚СЊ , РёР·Р�?РµРЅСЏС‚СЊ .</r>"
					+ "<r> РўР°РєР¶Рµ РІС‹ Р�?РѕР¶РµС‚Рµ РёР�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РґР»СЏ РїРѕРёРєР° РёРЅС„РѕСЂР�?Р°С†РёРѕРЅРЅРѕРіРѕ Р�?РѕРґСѓР»СЏ </r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ РґРѕР»Р¶РЅС‹ РёР·Р�?РµРЅРёС‚СЊ РєСЂРёС‚РµСЂРёРё РЅР° С„РѕСЂР�?Рµ РёР·Р�?РµРЅРµРЅРёСЏ РёРЅС„РѕСЂР�?Р°С†РёРё. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "2" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'"
					+ "CMS РєР°Рє РїР»Р°С‚С„РѕСЂР�?Р° РґРѕРєСѓР�?РµРЅС‚Рѕ-РѕР±РѕСЂРѕС‚Р° " + "' , " + "'<r>"
					+ " CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ GBS Workflow container РґР»СЏ СЂРµР°Р»РёР·Р°С†РёРё РїСЂРѕС…РѕР¶РґРµРЅРёСЏ РґРѕРєСѓР�?РµРЅС‚Р° РїРѕ Р�?Р°СЂС€СЂСѓС‚Сѓ ."
					+ "</r>' , " + "'<r>"
					+ " CMS Business One CMS РїРѕРґРґРµСЂР¶РёРІР°РµС‚ GBS Workflow container РґР»СЏ СЂРµР°Р»РёР·Р°С†РёРё РїСЂРѕС…РѕР¶РґРµРЅРёСЏ РґРѕРєСѓР�?РµРЅС‚Р° РїРѕ Р�?Р°СЂС€СЂСѓС‚Сѓ ."
					+ "</r>"
					+ "<r> GBS Workflow container РїРѕР·РІРѕР»СЏРµС‚ РѕС‚Р»Р°Р¶РёРІР°С‚СЊ Р�?Р°СЂС€СЂСѓС‚С‹ РЅР° СѓСЂРѕРІРЅРµ Unit С‚РµСЃС‚РёСЂРѕРІР°РЅРёСЏ.  </r>"
					+ "<r> Р­С‚Рѕ РѕР±СЊРµРґРёРЅСЏРµС‚ РґРІРµ СЂР°Р±РѕС‚С‹ РїРѕ РѕС‚Р»Р°РґРєРё Рё РЅР°РїРёСЃР°РЅРёСЋ Unit С‚РµСЃС‚РѕРІ С‡С‚Рѕ РІ РёС‚РѕРіРµ СЌРєРѕРЅРѕР�?РёС‚ РІСЂРµР�?СЏ Рё РґРµРЅСЊРіРё. </r>"
					+ "<r> РўРµС…РЅРѕРіРёСЏ СЂР°Р·РіСЂР°РЅРёС‡РµРЅРёСЏ РґРѕСЃС‚СѓРїР° Р±Р°Р·РёСЂСѓРµС‚СЊСЃСЏ РЅР° JAAS С‚РµС…РЅРѕР»РѕРіРёРё С‡С‚Рѕ РѕС‚РІРµС‡Р°РµС‚ СЃС‚Р°РЅРґР°СЂС‚Р°Р�? JCP. </r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "1" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "CMS РїРѕРґРґРµСЂР¶РєР° Portlet specification 2.0 "
					+ "' , " + "'<r>"
					+ " CMS Business One  РїРѕРґРґРµСЂР¶РёРІР°РµС‚ Portlet СЃРїРµС†РёС„РёРєР°С†РёСЋ JSR-286 С‡РµСЂРµР· Pluto Portal Driver."
					+ "</r>' , " + "'<r>"
					+ " Р”Р»СЏ СЌС‚РѕРіРѕ РІС‹  РґРѕР»Р¶РЅС‹ Р±СѓРґРµС‚Рµ СѓРєР°Р·Р°С‚СЊ РІ xsl С€Р°Р±Р»РѕРЅРµ РІ РєР°РєРѕРµ Р�?РµСЃС‚Рѕ Р·Р°РіСЂСѓР¶Р°С‚СЊ  Portlet ."
					+ "</r>" + "<r> РћР±С‹С‡РЅРѕ СѓРєР°Р·С‹РІР°РµС‚СЊСЃСЏ URL РЅР° portlet С‡РµСЂРµР· iframe </r>"
					+ "<r> Р­С‚Рѕ РїРѕР·РІРѕР»СЏРµС‚ РїСЂРµРЅРѕСЃРёС‚СЊ РїСЂРёР»РѕР¶РµРЅРёСЏ РёР· РґСЂСѓРіРёС… РїРѕСЂС‚Р°Р»РѕРІ С‚Р°РєРёС… РєР°Рє IBM WebSphera , Oracle portal , Sun portal </r>"
					+ "<r> С‡С‚Рѕ СЃС‚Р°РІРёС‚ CMS Business One РЅР°  СѓСЂРѕРІРµРЅСЊ РєСЂСѓРїРЅС‹С… РїСЂРѕР�?С‹С€Р»РµРЅС‹С… СЂРµС€РµРЅРёР№. </r>"
					+ "<r> (Pluto Portal Driver) http://portals.apache.org/pluto/faq.html </r>" + "' , " + "-2 , " + ""
					+ site_id + " , " + "" + "true" + " , " + "2" + "" + " ) ; ";

			jdbcTemplate.update(query);
			query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);
			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active , portlettype_id  ) "
					+ " values ( " + "" + soft_id + " , " + "'" + "CMS РїРѕРґРґРµСЂР¶РєР° Ajax technology " + "' , "
					+ "'<r>"
					+ " CMS Business One  РїРѕРґРґРµСЂР¶РёРІР°РµС‚ Ajax technology С‡РµСЂРµР· GBS Ajax framework."
					+ "</r>' , " + "'<r>"
					+ " GBS Ajax framework СЌС‚Рѕ РѕР±СЂР°Р±РѕС‚С‡РёРє СЃРѕР±С‹С‚РёР№ РЅР° java РѕС‚ Ajax РёРЅС‚РµСЂС„РµР№СЃР° ."
					+ "</r>"
					+ "<r> РћРЅ РёРЅС‚РµРіСЂРёСЂРѕРІР°РЅ РІ GBS Web famework ( base  by front controller with loading action classes ) </r>"
					+ "<r> СЂР°Р·С€РёРЅРµРЅРЅС‹Р№ РѕС‚ С€Р°Р±Р»РѕРЅР° Front controller СЃ РїРѕРґСЂРµР¶РєРѕР№ СЃРѕР±С‹С‚РёР№РЅС‹С… РєР»Р°СЃСЃРѕРІ Рё СЃРµСЂРІРёСЃР° С‚СЂР°РЅСЃС„РѕСЂР�?Р°С†РёР№ xsl С€Р°Р±Р»РѕРЅРѕРІ Рё РєРµС€РёСЂРѕРІР°РЅРёСЏ </r>"
					+ "<r> РЎР°Р�?РѕРµ РІР°Р¶РЅРѕРµ С‡С‚Рѕ Рє СЃРѕР±С‹С‚РёР№РЅРѕР�?Сѓ РєР»Р°СЃСЃСѓ web famework РґРѕР±Р°РІР»СЏСЋС‚СЃСЏ РєР»Р°СЃСЃС‹ Ajax РєРѕРЅС‚СЂРѕР»РѕРІ Рё РєР»Р°СЃСЃРѕРІ СЂРёСЃРѕРІР°РЅРёСЏ РёС… С‡С‚Рѕ РѕС‡РµРЅСЊ СѓРґРѕР±РЅРѕ РѕС‚Р»Р°Р¶РёРІР°С‚СЊ. </r>"
					+ "<r> Рљ С‚РѕР�?Сѓ Р¶Рµ, html РєРѕРґ РєРѕРЅС‚СЂРѕР»Р° РІСЃС‚Р°РІР»СЏРµС‚СЊСЃСЏ С‡РµСЂРµР·  innerHTML Р�?РµС‚РѕРґ , С‡С‚Рѕ РїРѕР·РІРѕР»СЏРµС‚СЊ РЅРµ РёСЃРїРѕР»СЊРІР°С‚СЊ РіРµРЅРµСЂР°С†РёСЋ РєРѕРЅС‚СЂРѕР»РѕРІ РёР· JavaScipt С‡С‚Рѕ СѓР�?РµРЅСЊС€Р°РµС‚ СЂРёСЃРєРё РїРѕ РІРѕР·РЅРёРєРЅРѕРІРµРЅРёСЋ РѕС€РёР±РѕРє РїРѕРґ РґСЂСѓРіРёР�?Рё Р±СЂР°СѓР·РµСЂР°Р�?Рё .</r>"
					+ "' , " + "-2 , " + "" + site_id + " , " + "" + "true" + " , " + "2" + "" + " ) ; ";

			jdbcTemplate.update(query);
			transactionSupport.commitTransaction(trxnStatus);
			cretareSiteDir(dufaultSite, site_dir);

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		} 
	}

	
	public void addSite() {
		String query = "";
		TransactionStatus trxnStatus =  transactionSupport.beginTransaction();
		JdbcTemplate jdbcTemplate = transactionSupport.getJdbcTemplate();
		try {
			query = site_sequence_rs;
			site_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into site (site_id , host , home_dir , site_dir , person , phone  , address , active ) "
					+ " values ( " + "" + site_id + " , " + "'" + host + "' , " + "'" + home_dir + "' , " + "'"
					+ site_dir + "' , " + "'" + person + "' , " + "'" + phone + "' , " + "'" + address + "' , " + "true"
					+ " ) ; ";

			jdbcTemplate.update(query);
			// add manager user
			query = tuser_sequence_rs;
			long intUserID =Long.valueOf(jdbcTemplate.queryForObject(query, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( " + "" + intUserID + " , " + "'" + login + "' , " + "'" + passwd + "' , " + " NULL , "
					+ " true  , " + " true  , " + " NOW , " + "" + 2 + " , " + "" + 0 + " , " + "" + currency_id + ", "
					+ "" + site_id + ", " + "" + city_id + ", " + "" + country_id + " " + " )";
			;

			jdbcTemplate.update(query);
			query = account_sequence_rs;
			account_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into account ( account_id, user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( " + "" + account_id + " , " + "" + intUserID + " , " + "" + 0 + " , " + "" + 0 + " , "
					+ " NOW , " + "' new_account ', " + "" + currency_id + " " + " ) ; ";

			jdbcTemplate.update(query);

			// add anonymouse user
			query = tuser_sequence_rs;

			intUserID = Long.parseLong(jdbcTemplate.queryForObject(query, String.class));

			query = "insert into tuser ( user_id , login , passwd ,birthday,acvive_session ,active ,regdate ,levelup_cd ,bank_cd , currency_id , site_id , city_id , country_id) "
					+ "values ( " + "" + intUserID + " , " + "'user' , " + "'user' , " + " NULL , " + " true  , "
					+ " true  , " + " NOW , " + "" + 0 + " , " + "" + 0 + " , " + "" + currency_id + ", " + "" + site_id
					+ ", " + "" + city_id + ", " + "" + country_id + " " + " )";
			;

	         jdbcTemplate.update(query);
			query = account_sequence_rs;
			account_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into account ( account_id , user_id , amount , curr , date_input ,  description ,  currency_id ) "
					+ " values ( " + "" + account_id + " , " + "" + intUserID + " , " + "" + 0 + " , " + "" + 0 + " , "
					+ " NOW , " + "' new_account ', " + "" + currency_id + " " + " ) ; ";

            jdbcTemplate.update(query);

			// ==== end add users =========================

			query = "insert into shop (shop_cd , owner_id , login , passwd ,  pay_gateway_id , site_id ,cdate ) "
					+ " values ( " + "" + 84473 + " , " + "" + intUserID + " , " + "'" + "HCO-CENTE-406" + "' , " + "'"
					+ "91KiBFRtE8fF7VHc8tvr" + "' , " + "" + 1 + " , " + "" + site_id + " , " + " NOW " + " ) ; ";

            jdbcTemplate.update(query);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ "-1 , " + "" + site_id + " , " + "'" + "РќРѕРІРѕСЃС‚Рё" + "' , " + "" + "true" + " , " + "" + 1
					+ " , " + "" + 0 + " ) ; ";

            jdbcTemplate.update(query);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ "-2 , " + "" + site_id + " , " + "'" + "Р“Р»Р°РІРЅР°СЏ СЃС‚СЂР°РЅРёС†Р°" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + 0 + " ) ; ";

            jdbcTemplate.update(query);

			query = "insert into catalog (catalog_id , site_id , lable , active ,lang_id , parent_id ) " + " values ( "
					+ "-3 , " + "" + site_id + " , " + "'" + "РќР° РіР»Р°РІРЅС‹Р№ СЃР°Р№С‚" + "' , " + "" + "true"
					+ " , " + "" + 1 + " , " + "" + 0 + " ) ; ";

            jdbcTemplate.update(query);

			// insert into catalog (catalog_id , site_id , lable , active
			// ,lang_id , parent_id ) values ( -2 , 2 , 'Р“Р»Р°РІРЅР°СЏ
			// СЃС‚СЂР°РЅРёС†Р°' , true , 1 , 0 ) ;

			query = soft_sequence_rs;
			String soft_id = jdbcTemplate.queryForObject(query, String.class);


			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " Р’Р°С€ РЅРѕРІС‹Р№ Р�?Р°РіР°Р·РёРЅ  " + "' , " + "'"
					+ "<r>РќР°СЃС‚СЂРѕР№РєР° РїРµСЂРµРґ РЅР°С‡Р°Р»РѕР�? </r><r> СЂР°Р±РѕС‚С‹ Р�?Р°РіР°Р·РёРЅР° .</r>"
					+ "' , " + "'"
					+ "<r>1. Р’Р°Р�? РЅСѓР¶РЅРѕ Р·Р°СЂРµРіРёСЃС‚СЂРёСЂРѕРІР°С‚СЃСЏ РІ РїР»Р°С‚РµР¶РЅРѕР�? РіРµР№С‚Рµ ,РЅР°РїСЂРёР�?РµСЂ РІ СЃРёСЃС‚Рµ Assist ,</r>"
					+ "<r> Р·Р°Р№С‚Рё РЅР° СЃР°Р№С‚ РїРѕ Р°РґСЂРµСЃСѓ www.assist.ru  Р·Р°СЂРµРіРёСЃС‚СЂРёСЂРѕРІР°С‚СЊ РёРЅС‚РµСЂРЅРµС‚ Р�?Р°РЅР°Р·РёРЅ  .</r>"
					+ "<r> РІС‹ РїРѕР»СѓС‡РёС‚Рµ РєРѕРґ Р�?Р°РіР°Р·РёРЅР° (shop_cd) , СЃРµРєСЂРµС‚РЅРѕРµ РёР�?СЏ (login) , Рё РїР°СЂРѕР»СЊ (password) РїРѕРІРµСЂСЏСЋС‰РёР№ С‡С‚Рѕ СЃРµРєСЂРµС‚РЅРѕРµ РёР�?СЏ РІР°С€Рµ .</r>"
					+ "<r> 2. Р­С‚Рё РґР°РЅРЅС‹Рµ РґРѕР»Р¶РЅС‹ Р±С‹С‚СЊ РІРЅРµСЃРµРЅРЅС‹ РІ РЅР°СЃС‚РѕР№РєСѓ РІР°С€РµРіРѕ Р�?Р°РіР°Р·РёРЅР° С‡РµСЂРµР· РЅР°С€Сѓ С„РѕСЂР�?Сѓ (РџР»Р°С‚РµР¶РЅС‹Р№ РіРµР№С‚) .</r>"
					+ "<r> 3. Р§С‚РѕР±С‹ РїРѕРєСѓРїР°С‚РµР»СЊ Р�?РѕРі СЃРґРµР»Р°С‚СЊ РїРѕРєСѓРїРєСѓ РІР°Р�? РЅСѓР¶РЅРѕ СЂР°Р·Р�?РµС‚РёС‚СЊ РёРЅРІРѕСЂР�?Р°С†РёСЋ Рѕ С‚РѕРІР°СЂРµ .</r>"
					+ "<r> РґР»СЏ СЌС‚РѕРіРѕ Сѓ РІР°СЃ РґРѕР¶РЅС‹ Р±С‹С‚СЊ РїСЂР°РІР° Р�?РµРЅРµРґР¶РµСЂР° Р�?Р°РіР°Р·РёРЅР° .</r>"
					+ "<r>Р§С‚Рѕ СЂР°Р±РѕС‚Р°С‚СЊ СЃ СЃР°Р№С‚РѕР�? СЃ РїСЂР°РІР°Р�?Рё Р�?РµРЅРµР¶РґРµСЂР°  РІР°Р�? РЅСѓР¶РЅРѕ РІРІРµСЃС‚Рё login Рё password РїСЂР°РІР°Р�?Рё Р�?РµРЅРµР¶РґРµСЂР° (С‚.Рµ РІРѕР№С‚Рё РІ СЃРёСЃС‚РµР�?Сѓ РєР°Рє Р�?РµРЅРµРґР¶РЅСЂ Р�?Р°РіР°Р·РёРЅР°)  .</r>"
					+ "<r>пїЅ? РґРѕР±Р°РІРёС‚СЊ РїРѕР·РёС†РёСЋ С‚РѕРІР°СЂР° РІ Р�?Р°РіР°Р·РёРЅ (РёСЃРїРѕР»СЊР·СѓР№С‚Рµ СЃС‚Р°РЅРёС†Сѓ - РќР°СЃС‚СЂРѕР№РєР° СЃР°Р№С‚Р°)  .</r>"
					+ "<r>РџРѕСЃР»Рµ С‡РµРіРѕ РІР°С€Рё РєР»РёРµРЅС‚С‹ СЃР�?РѕРіСѓС‚  РґРѕР±Р°РІР»СЏС‚СЊ С‚РѕРІР°СЂС‹ РІ РєР°СЂР·РёРЅСѓ Рё Р·Р°РїРѕР»РЅСЏС‚СЊ С„РѕСЂР�?Сѓ Р·Р°РєР°Р·Р° </r>"
					+ "<r> Рё РїР»Р°С‚РёС‚СЊ Р·Р° РЅРµРіРѕ РєСЂРµРґРёС‚РЅС‹Р�?Рё РєР°СЂС‚Р°Р�?Рё Рё СЌР»РµРєС‚СЂРѕРЅРЅС‹Р�?Рё РєР°С€РµР»СЊРєР°Р�?Рё .</r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

            jdbcTemplate.update(query);

            query = soft_sequence_rs;
            soft_id = jdbcTemplate.queryForObject(query, String.class);

			query = "insert into soft (soft_id , name , description , fulldescription ,catalog_id , site_id , active ) "
					+ " values ( " + "" + soft_id + " , " + "'" + " РџРѕРєСѓРїРєР° С‚РѕРІР°СЂР°  " + "' , " + "'<r>"
					+ "РћРїРёСЃР°РЅРёРµ РєР°Рє РєСѓРїРёС‚СЊ С‚РѕРІР°СЂ" + "</r>' , " + "'<r>"
					+ " 1. Р’С‹Р±РѕСЂ С‚РѕРІР°СЂР°. </r>" + "<r>"
					+ " РџРѕРёСЃРє С‚РѕРІР°СЂР° РѕСЃСѓС‰РµСЃС‚РІР»СЏРµС‚СЃСЏ РїРѕ СЂР°Р·РґРµР»Р°Р�? (РѕС‚РґРµР»Р°Р�?, СЂСѓР±СЂРёРєР°Р�?). </r>"
					+ "<r> Р Р°Р·РґРµР» СЃРѕСЃС‚РѕРёС‚ РёР· СЃС‚СЂР°РЅРёС†, РЅР° РєРѕС‚РѕСЂС‹С… РїРѕР�?РµС‰Р°РµС‚СЃСЏ РїРѕ 10 РїРѕР·РёС†РёР№.</r>"
					+ "<r> Р§С‚РѕР±С‹ РїСЂРѕСЃР�?РѕС‚СЂРµС‚СЊ РІСЃРµ СЃС‚СЂР°РЅРёС†С‹ РІ СЂР°Р·РґРµР»Рµ, РїРѕР»СЊР·СѓР№С‚РµСЃСЊ СЃС‚СЂРµР»РєР°Р�?Рё (РІРІРµСЂС…), (РІРЅРёР·).</r>"
					+ "<r> Р§С‚РѕР±С‹ РїРѕР»СѓС‡РёС‚СЊ РїРѕР»РЅСѓСЋ РёРЅС„РѕСЂР�?Р°С†РёСЋ Рѕ С‚РѕРІР°СЂРµ, РґРѕСЃС‚Р°С‚РѕС‡РЅРѕ РєР»РёРєРЅСѓС‚СЊ Р�?С‹С€РєРѕР№ РЅР° (РїРѕРґСЂРѕР±РЅРѕ).</r>"
					+ "<r> Р’С‹Р±РѕСЂ С‚РѕРІР°СЂР° РґР»СЏ Р·Р°РєР°Р·Р° РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ СЃРѕ СЃС‚СЂР°РЅРёС†С‹ РїРѕРґСЂРѕР±РЅРѕРіРѕ РѕРїРёСЃР°РЅРёСЏ РЅР°Р¶Р°С‚РёРµР�? РєРЅРѕРїРєРё (РїРѕР»РѕР¶РёС‚СЊ РІ РєРѕСЂР·РёРЅСѓ).</r>"
					+ "<r> 2. Р”РѕР±Р°РІР»РµРЅРёРµ С‚РѕРІР°СЂР° РІ РєРѕСЂР·РёРЅСѓ РїРѕРєСѓРїРѕРє.</r>"
					+ "<r> РЎР�?РѕС‚СЂРё РїСѓРЅРєС‚ 1.</r>" + "<r> 3. РћС„РѕСЂР�?Р»РµРЅРёРµ Р·Р°РєР°Р·Р°.</r>"
					+ "<r> РџРѕСЃР»Рµ РІС‹Р±РѕСЂР° С‚РѕРІР°СЂР° Р·Р°РєР°Р· С„РѕСЂР�?РёСЂСѓРµС‚СЃСЏ Р°РІС‚РѕР�?Р°С‚РёС‡РµСЃРєРё.</r>"
					+ "<r> РџРѕ Р¶РµР»Р°РЅРёСЋ РєР»РёРµРЅС‚Р° РёР· Р·Р°РєР°Р·Р° Р�?РѕР¶РЅРѕ СѓРґР°Р»РёС‚СЊ РЅРµРЅСѓР¶РЅС‹Рµ РїРѕР·РёС†РёРё С‚РѕРІР°СЂР°.</r>"
					+ "<r> Р”РѕР±Р°РІР»РµРЅРёРµ РІ Р·Р°РєР°Р· С‚РѕРІР°СЂР°, РїСЂРѕРёР·РІРѕРґРёС‚СЃСЏ Р°РІС‚РѕР�?Р°С‚РёС‡РµСЃРєРё РїСЂРё РµРіРѕ РІС‹Р±РѕСЂРµ.</r>"
					+ "<r> 4. РћРїР»Р°С‚Р° С‚РѕРІР°СЂР°.</r>"
					+ "<r> РџРѕСЃР»Рµ С„РѕСЂР�?РёСЂРѕРІР°РЅРёСЏ РїРѕР»РЅРѕРіРѕ Р·Р°РєР°Р·Р° РґР»СЏ РѕРїР»Р°С‚С‹ РЅРµРѕР±С…РѕРґРёР�?Рѕ РЅР°Р¶Р°С‚СЊ РєРЅРѕРїРєСѓ (РћРїР»Р°С‚РёС‚СЊ).</r>"
					+ "' , " + "-1 , " + "" + site_id + " , " + "" + "true" + "" + " ) ; ";

            jdbcTemplate.update(query);

			transactionSupport.commitTransaction(trxnStatus);
			cretareSiteDir(dufaultSite, site_dir);

		} catch (Exception ex) {
			logger.debug(ex.getLocalizedMessage());
			logger.error(ex.getLocalizedMessage());
			transactionSupport.rollbackTransaction(trxnStatus);
		} 
	}

	public void cretareSiteDir(String defaultSite, String newSiteDir) {
		try {
			String path = this.getClass().getResource("").getPath();
			path = path.substring(0, path.indexOf("/WEB-INF/"));
			copyDir(path + File.separatorChar + "xsl" + File.separatorChar + defaultSite,
					path + File.separatorChar + "xsl" + File.separatorChar + newSiteDir);
			copyDir(path + File.separatorChar + "xsl" + File.separatorChar + defaultSite + File.separatorChar + "img",
					path + File.separatorChar + "xsl" + File.separatorChar + newSiteDir + File.separatorChar + "img");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		;
	}

	public void copyDir(String srcDir, String dstDir) throws IOException {
		String sep = "";
		sep = File.separator;

		System.out.println("src Dir = " + srcDir);
		System.out.println("dst Dir = " + dstDir);

		File fdstDir = new File(dstDir);

		if (!fdstDir.exists()) {
			fdstDir.mkdirs();
			System.out.println("creating  " + dstDir);
		}

		String[] fileList = new File(srcDir).list();

		boolean dir;

		for (int i = 0; i < fileList.length; i++) {
			dir = new File(srcDir + sep + fileList[i]).isDirectory();
			if (dir) {
				copyDir(srcDir + sep + fileList[i], dstDir + sep + fileList[i]);
			} else {
				copyFile(srcDir + sep + fileList[i], dstDir + sep + fileList[i]);
			}
		}

	}

	public void copyFile(String srcFile, String dstFile) throws IOException {

		FileInputStream inp = new FileInputStream(srcFile);
		FileOutputStream out = new FileOutputStream(dstFile);

		byte[] buff = new byte[8192];
		int count;
		// read up to buff.length bytes
		while ((count = inp.read(buff)) != -1) {
			out.write(buff, 0, count);
		}

		out.close();
		inp.close();

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSite_id() {
		return site_id;
	}

	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}

	public String getHome_dir() {
		return home_dir;
	}

	public void setHome_dir(String home_dir) {
		this.home_dir = home_dir;
	}

	public String getSite_dir() {
		return site_dir;
	}

	public void setSite_dir(String site_dir) {
		this.site_dir = site_dir;
	}

	public String getPerson() {
		return person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject_site() {
		return subject_site;
	}

	public void setSubject_site(String subject_site) {
		this.subject_site = subject_site;
	}

	public String getNick_site() {
		return nick_site;
	}

	public void setNick_site(String nick_site) {
		this.nick_site = nick_site;
	}

	public String getCompany_name() {
		return company_name;
	}

	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

}