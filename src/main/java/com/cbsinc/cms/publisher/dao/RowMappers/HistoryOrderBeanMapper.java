package com.cbsinc.cms.publisher.dao.RowMappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.cbsinc.cms.dto.pages.orderhistory.HistoryOrder;

public class HistoryOrderBeanMapper implements RowMapper<HistoryOrder> {

  @Override
  public HistoryOrder mapRow(ResultSet rs, int rowNum) throws SQLException {
    HistoryOrder historyOrderBean = new HistoryOrder();
    historyOrderBean.setPaystatus_lable(rs.getString("paystatus.lable"));
    historyOrderBean.setPaystatus_id(rs.getString("paystatus.paystatus_id"));
    historyOrderBean.setCdate(rs.getString("orders.cdate"));
    historyOrderBean.setEnd_amount(rs.getString("orders.end_amount"));
    historyOrderBean.setOrder_id(rs.getString("orders.order_id"));
    historyOrderBean.setCurrency_lable(rs.getString("currency.currency_lable"));

    return historyOrderBean;
  }

}
