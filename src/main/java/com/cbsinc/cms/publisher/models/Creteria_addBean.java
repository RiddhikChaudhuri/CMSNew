package com.cbsinc.cms.publisher.models;

import java.util.ResourceBundle;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import com.cbsinc.cms.publisher.dao.TransactionSupport;

@Component
@PropertySources(value = {
    @PropertySource(value="classpath:sequence.properties")
  })
public class Creteria_addBean implements java.io.Serializable  
{

	

	private static final long serialVersionUID = 8448583709739712289L;

	//static private Logger log = Logger.getLogger(Creteria_addBean.class);
	private XLogger logger = XLoggerFactory.getXLogger(Creteria_addBean.class.getName());

	private String query;

	private String name = "0";
	
	private String label = "0";

	private String creteria_id = "0";
	
	private Integer indx_select = 0;
	
	private String table_name =  "creteria1" ;
	
	private Integer link_id  = 0 ; 

	transient ResourceBundle sequences_rs = null ;
	
	public Creteria_addBean() 
	{
		logger.entry();
		logger.exit();
	}
	

    @Autowired
    private TransactionSupport transactionSupport;
       

    
    public void addCatalog(String site_id) {
      logger.entry(site_id);
      TransactionStatus status = transactionSupport.beginTransaction();
      String query = "";
      query = "SELECT MAX(" + table_name + "_id ) + 1  as ID FROM " + table_name + "";

      try {
        creteria_id = transactionSupport.getJdbcTemplate().queryForObject(query, String.class);

        query = "insert into " + table_name + " (" + table_name
            + "_id , catalog_id , link_id , name , label , active ) "
            + " values ( ? , ? , ? , ? , ? , ? ) ";

        transactionSupport.getJdbcTemplate().update(query, creteria_id, site_id, link_id, name,
            label, true);
        transactionSupport.commitTransaction(status);

      } catch (Exception ex) {
        logger.error(query, ex);
        logger.error(ex.getLocalizedMessage(), ex);
        transactionSupport.rollbackTransaction(status);
      }

      finally {
        logger.exit();;
      }



      return;
    }

	
	
	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public int getIndx_select() {
		return indx_select;
	}

	public void setIndx_select(int indx_select) {
		this.indx_select = indx_select;
	}



	public String getTable_name() {
		return table_name;
	}



	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}



	public int getLink_id() {
		return link_id;
	}



	public void setLink_id(int link_id) {
		this.link_id = link_id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	
	



}
