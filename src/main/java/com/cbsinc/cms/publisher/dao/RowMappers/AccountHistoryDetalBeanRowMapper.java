package com.cbsinc.cms.publisher.dao.RowMappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.cbsinc.cms.publisher.models.AccountHistoryDetalBean;

public class AccountHistoryDetalBeanRowMapper implements RowMapper<AccountHistoryDetalBean> {

  @Override
  public AccountHistoryDetalBean mapRow(ResultSet rs, int rowNum) throws SQLException {
    AccountHistoryDetalBean accountHistoryDetalBean = new AccountHistoryDetalBean();

    accountHistoryDetalBean.setAdd_amount(rs.getString("add_amount"));
    accountHistoryDetalBean.setOld_amount(rs.getString("old_amount"));
    accountHistoryDetalBean.setDate_input(rs.getString("date_input"));
    accountHistoryDetalBean.setDate_end(rs.getString("date_end"));
    accountHistoryDetalBean.setSysdate(rs.getString("sysdate"));
    accountHistoryDetalBean.setComplete(rs.getString("complete"));
    accountHistoryDetalBean.setDecsription(rs.getString("decsription"));
    accountHistoryDetalBean.setActive(rs.getString("active"));
    accountHistoryDetalBean.setAmount_id(rs.getString("id"));
    accountHistoryDetalBean.setTotal_amount(rs.getString("total_amount"));
    accountHistoryDetalBean.setCurrency_add_lable(rs.getString("currency_add.currency_lable"));
    accountHistoryDetalBean.setCurrency_old_lable(rs.getString("currency_old.currency_lable"));
    accountHistoryDetalBean.setCurrency_total_lable(rs.getString("currency_total.currency_lable"));
    accountHistoryDetalBean.setUser_ip(rs.getString("account_hist.user_ip"));
    accountHistoryDetalBean.setUser_header(rs.getString("account_hist.user_header"));
    accountHistoryDetalBean.setRezult_cd(rs.getString("account_hist.rezult_cd"));
    return accountHistoryDetalBean;
  }


}
