package com.cbsinc.cms.publisher.dao.RowMappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.cbsinc.cms.publisher.models.AuthorizationPageBean;

public class AuthorizationPageBeanRowMapper implements RowMapper<AuthorizationPageBean> {
    
	

	@Override
	public AuthorizationPageBean mapRow(ResultSet rs, int rowNum) throws SQLException {
		AuthorizationPageBean item = new AuthorizationPageBean();
		 
		item.setAddress(rs.getString("address"));
		item.setBalance(rs.getString("balance"));
		item.setCatalog_id(rs.getString("CATALOG_ID"));
		item.setCatalog_parent_id(rs.getString("PARENT_ID"));
		item.setLang_id(rs.getInt("LANG_ID"));
        return item;
	}
}
