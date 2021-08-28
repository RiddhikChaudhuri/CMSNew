package com.cbsinc.cms.publisher.dao.RowMappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.cbsinc.cms.publisher.models.OrderBean;

public class OrderBeanRowMapper implements RowMapper<OrderBean>{

  @Override
  public OrderBean mapRow(ResultSet rs, int rowNum) throws SQLException {
      OrderBean orderBean =  new OrderBean();
      orderBean.setAccount_history_id(rs.getString("account_history_id"));
      orderBean.setUser_ID(rs.getString("user_id"));
      orderBean.setorder_amount(rs.getString("user_id"));
      orderBean.setOrder_currency_id(rs.getString("currency_id_add"));
      orderBean.setorder_tax(rs.getString("tax"));
      orderBean.setend_amount(rs.getString("add_amount"));
      orderBean.setOrder_status(rs.getString("order_status"));
    return orderBean;
  }

}
