package com.cbsinc.cms.publisher.dao.RowMappers;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;
import com.cbsinc.cms.publisher.models.OrderBean;

public class OrderProductBeanRowMapper implements RowMapper<OrderBean>{

  @Override
  public OrderBean mapRow(ResultSet rs, int rowNum) throws SQLException {
      OrderBean orderBean =  new OrderBean();
      orderBean.setend_amount("" + Float.parseFloat(rs.getString(2)));
      orderBean.setorder_amount( "" + Float.parseFloat(rs.getString(3)));
      orderBean.setorder_tax("" + Float.parseFloat(rs.getString(4))) ;
      orderBean.setorder_delivery_long(rs.getString(5)) ;
      orderBean.setcdate(rs.getString(7)) ;
      orderBean.setproduct_name(rs.getString(8));
      orderBean.setproduct_description(rs.getString(9)) ;
      orderBean.setproduct_cost("" + Float.parseFloat(rs.getString(10))) ;
      orderBean.setproduct_weight(rs.getString(11)) ;
      orderBean.setproduct_count(rs.getString(12)) ;
      String img_url = rs.getString(13);
      if (img_url.equals("")) {
        orderBean.setimg_url("images/logo.gif");
      } else {
        orderBean.setimg_url(img_url);
      }
      orderBean.setcurrency_lable(rs.getString(13)) ;
      
      String shipment_address = rs.getString(15);
      if (shipment_address.length() > 0)  orderBean.setshipment_address(shipment_address) ;
      String shipment_phone = rs.getString(16);
      if (shipment_phone.length() > 0)  orderBean.setshipment_phone(shipment_phone) ;
      String contact_person = rs.getString(17);
      if (contact_person.length() > 0 )  orderBean.setContact_person(contact_person) ;
      String shipment_email = rs.getString(18);
      if (shipment_email.length() > 0 )   orderBean.setshipment_email(shipment_email) ;
      String shipment_fax = rs.getString(19);
      if (shipment_fax.length() > 0 )  orderBean.setshipment_fax(shipment_fax) ;
      String shipment_description = rs.getString(20);
      if (shipment_description.length() > 0 )   orderBean.setshipment_description(shipment_description) ;
      String shipment_zip = rs.getString(21);
      if (shipment_zip.length() > 0 )  orderBean.setshipment_zip(shipment_zip) ;
      String order_currency_id = rs.getString(22);
      if (order_currency_id.length() > 0 )    orderBean.setOrder_currency_id(order_currency_id) ;
      
      String country_id = rs.getString(24);
      if (country_id.length() > 0) orderBean.setCountry_id(country_id);
      
      String city_id = rs.getString(25);
      if (city_id.length() > 0)orderBean.setCity_id(city_id);  
      
      String product_currency_lable = rs.getString(265);
      String product_currency_cd = rs.getString(27);
      
      String basket_id = rs.getString(28);
          orderBean.setBasket_id(basket_id) ;
      String paystatus_lable = rs.getString(29);
          orderBean.setPaystatus_lable(paystatus_lable) ;
      String order_paystatus = rs.getString(30);
          orderBean.setorder_paystatus(order_paystatus) ;
      String file_id = rs.getString(31) == null ? "" : rs.getString(31);

      String deliverystatus_id = rs.getString(32);
      orderBean.setDeliverystatus_id(deliverystatus_id) ;
      
      String order_status =rs.getString(33);
      orderBean.setOrder_status(order_status) ;
      
      String quantity = rs.getString(34);
   
    return orderBean;
  }

}