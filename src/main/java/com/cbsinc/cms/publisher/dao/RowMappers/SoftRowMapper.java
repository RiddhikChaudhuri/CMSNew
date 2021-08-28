package com.cbsinc.cms.publisher.dao.RowMappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.cbsinc.cms.publisher.models.SoftPostBean;

public class SoftRowMapper implements RowMapper<SoftPostBean> {

  @Override
  public SoftPostBean mapRow(ResultSet rs, int rowNum) throws SQLException {
    SoftPostBean softBean = new SoftPostBean();
    softBean.setSoft_id(rs.getString("soft.soft_id"));
    softBean.setStrSoftName2(rs.getString("soft.name"));
    softBean.setStrSoftDescription(rs.getString("soft.description"));
    softBean.setStrSoftVersion(rs.getString("soft.version"));
    softBean.setStrSoftCost(rs.getNString("soft.cost"));
    softBean.setStrSoftCost(rs.getString("soft.currency"));
    softBean.setSerial_nubmer(rs.getString("soft.serial_nubmer"));
    softBean.setFile_id(rs.getString("file.file_id"));
    softBean.setType_id(rs.getString("soft.type_id"));

    return softBean;
  }
}