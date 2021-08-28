package com.cbsinc.cms.publisher.dao;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


@Component 
public class TransactionSupport {

  
  protected DataSource dataSource;

  protected PlatformTransactionManager transactionManager;

  
  protected JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
	this.dataSource = dataSource ;
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    this.transactionManager = new DataSourceTransactionManager(dataSource);
  }



  public TransactionStatus beginTransaction() throws TransactionException {

    TransactionDefinition def = new DefaultTransactionDefinition(2);
    
    return transactionManager.getTransaction(def);
  }

  public void commitTransaction(TransactionStatus status) throws TransactionException {
    transactionManager.commit(status);
  }

  public void rollbackTransaction(TransactionStatus status) throws TransactionException {
    transactionManager.rollback(status);
  }



  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }



}
