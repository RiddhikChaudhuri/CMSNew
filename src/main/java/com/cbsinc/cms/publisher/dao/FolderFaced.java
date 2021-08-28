package com.cbsinc.cms.publisher.dao;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko. Konstantin Grabko is
 * Owner and author this code. You can not use it and you cannot change it without written
 * permission from Konstantin Grabko Email: konstantin.grabko@yahoo.com or
 * konstantin.grabko@gmail.com
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
import java.io.Serializable;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.RowMappers.AuthorizationPageBeanRowMapper;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;


@Repository
@PropertySource("classpath:sequence.properties")
public class FolderFaced implements Serializable {

  private static final long serialVersionUID = -195162207974958694L;

  @Value("${catalog}")
  private String sequences_rs;

  @Autowired
  TransactionSupport transactionSupport;


  private XLogger log = XLoggerFactory.getXLogger(FolderFaced.class.getName());

  public FolderFaced() {
    log.entry();
    log.exit();
  }

  public String addFolder(final String name, String catalogParent_id) {
    log.entry(name, catalogParent_id);
    TransactionStatus txStatus = transactionSupport.beginTransaction();
    JdbcTemplate jdbcTemplate = transactionSupport.getJdbcTemplate();
    String query = "SELECT * FROM EMPLOYEE WHERE ID = ?";
    AuthorizationPageBean authorizationBean = jdbcTemplate.queryForObject(query,
        new Object[] {name}, new AuthorizationPageBeanRowMapper());
    String catalog_id = "";
    query = sequences_rs;
    try {
      catalog_id = jdbcTemplate.queryForObject(query, String.class);
      query =
          "insert into catalog (catalog_id , parent_id , site_id , tax , lable , lang_id ,active ) "
              + " values ( ? , ? , ? , ? , ? , ? , ? ) ";

      jdbcTemplate.update(query, Long.valueOf(catalog_id), Long.valueOf(catalogParent_id),
          Long.valueOf(authorizationBean.getSite_id()), 1, name, authorizationBean.getLang_id(),
          true);
      transactionSupport.commitTransaction(txStatus);
    } catch (Exception e) {
      transactionSupport.rollbackTransaction(txStatus);
    }
    log.exit(catalog_id);
    return catalog_id;
  }


  public void editFolder(final String name) {

    log.entry(name);
    TransactionStatus txStatus = transactionSupport.beginTransaction();
    JdbcTemplate jdbcTemplate = transactionSupport.getJdbcTemplate();
    String query = "SELECT * FROM EMPLOYEE WHERE ID = ?";
    try {
      AuthorizationPageBean authorizationBean = jdbcTemplate.queryForObject(query,
          new Object[] {name}, new AuthorizationPageBeanRowMapper());

      query = "update catalog set  lable = ? , lang_id = ?  where catalog_id = "
          + authorizationBean.getCatalog_id() + " and site_id = " + authorizationBean.getSite_id();


      jdbcTemplate.update(query, name, authorizationBean.getLang_id());
      transactionSupport.commitTransaction(txStatus);
    } catch (Exception e) {
      transactionSupport.rollbackTransaction(txStatus);
    }

    log.exit();
    return;
  }

  public void deleteFolder(String selectedCatalog_id) {

    log.entry(selectedCatalog_id);
    TransactionStatus txStatus = transactionSupport.beginTransaction();
    JdbcTemplate jdbcTemplate = transactionSupport.getJdbcTemplate();
    try {
      String query = "SELECT * FROM EMPLOYEE WHERE ID = ?";
      AuthorizationPageBean authorizationBean = jdbcTemplate.queryForObject(query,
          new Object[] {selectedCatalog_id}, new AuthorizationPageBeanRowMapper());


      if (selectedCatalog_id.startsWith("-") || selectedCatalog_id.equals("2"))
        return;

      query = "delete FROM catalog WHERE site_id = " + authorizationBean.getSite_id()
          + " and catalog_id = " + selectedCatalog_id;
      jdbcTemplate.update(query);
      transactionSupport.commitTransaction(txStatus);
    } catch (Exception e) {
      transactionSupport.rollbackTransaction(txStatus);
    }
    log.exit();
  }

}
