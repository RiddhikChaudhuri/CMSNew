package com.cbsinc.cms.publisher.controllers;

/**
 * <p>
 * Title: Content Manager System
 * </p>
 * <p>
 * Description: System building web application develop by Konstantin Grabko.
 * Konstantin Grabko is Owner and author this code.
 * You can not use it and you cannot change it without written permission from Konstantin Grabko
 * Email: konstantin.grabko@yahoo.com or konstantin.grabko@gmail.com
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.TransactionStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.FDResponseForm;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.TransactionSupport;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PayStatus;



@RestController
public class FDResponceAction extends CMSObjects{

    private XLogger logger = XLoggerFactory.getXLogger(FDResponceAction.class.getName());
    
    @Autowired
    TransactionSupport transactionSupport;
    
    @Autowired
    AuthorizationPageFaced authorizationPageFaced;


	public FDResponceAction() {
	  logger.entry();
	  logger.exit();
	}

	
	@GetMapping("/doGetFDResponse")
	public String action(FDResponseForm fdResponseForm) throws Exception {

		AuthorizationPageBean authorizationPageBean = (AuthorizationPageBean) getHttpSession().getAttribute("authorizationPageBeanId");
		String x_invoice_num =    fdResponseForm.getX_invoice_num();
		String x_response_code = fdResponseForm.getX_response_code();
		String x_po_num = fdResponseForm.getX_po_num();
		String site_id = x_po_num.split("_")[0];
		String x_response_reason_text = fdResponseForm.getX_response_reason_text();
		parserRequest(x_invoice_num, x_response_code, x_response_reason_text,Optional.of(authorizationPageBean));
		logger.info("pay respone: " + getHttpServletRequest().getRequestURI());
		authorizationPageBean.setSite_id(site_id, authorizationPageFaced);
		String url;
		if ((url = getCookiesValue(getHttpServletRequest(), "payment_page")) != null) {
			return "";
		}
		
		return url;

	}

	private String getCookiesValue(HttpServletRequest request, String name) {

		Cookie[] cookies = request.getCookies();
		for (int i = 0; i < cookies.length; i++) {
			Cookie c = cookies[i];
			String _name = c.getName();
			if (name.equals(_name))
				return c.getValue();
		}

		return null;
	}

    public void parserRequest(String i_strNumerOrder, String i_strRezult, String i_strDecsription,
        Optional<AuthorizationPageBean> authorizationPageBean) {
      logger.entry(i_strNumerOrder, i_strDecsription, authorizationPageBean, i_strRezult);
      int approved = 1;
      int declined = 2;
      int error = 3;

      logger.info(
          "invoice Id: " + i_strNumerOrder + " Rezalt: " + i_strRezult + " " + i_strDecsription);
      TransactionStatus transactionStatus = transactionSupport.beginTransaction();
      String query = "";
      i_strRezult = i_strRezult.trim();
      try {

        if (Long.parseLong(i_strRezult) == approved) {
          end_addmoney(i_strNumerOrder, i_strRezult, i_strRezult, authorizationPageBean);
          authorizationPageBean
              .ifPresent(action -> action.setStrMessage("Thank you, your order has been placed"));
        } else if (Long.parseLong(i_strRezult) == declined) {
          query =
              "UPDATE account_hist SET complete = ? , active = ? , rezult_cd = ? , decsription =  ?  WHERE  id =  "
                  + i_strNumerOrder + "";
          transactionSupport.getJdbcTemplate().update(query, false, false, i_strRezult,
              i_strDecsription);
        } else if (Long.parseLong(i_strRezult) == error) {
          query =
              "UPDATE account_hist SET complete = ? , active = ? , rezult_cd = ? , decsription =  ?  WHERE  id =  "
                  + i_strNumerOrder + "";
          transactionSupport.getJdbcTemplate().update(query, false, false, i_strRezult,
              i_strDecsription);

        }
        transactionSupport.commitTransaction(transactionStatus);
      } catch (Exception ex) {
        logger.error(ex.getLocalizedMessage(), ex);
        transactionSupport.rollbackTransaction(transactionStatus);
        return;
      } finally {
        logger.exit();
      }

    }


    private void end_addmoney(String i_strAccountHistory_id, String i_strRezult,
			String i_strDecsription,Optional<AuthorizationPageBean> authorizationPageBean) throws Exception {
	    logger.entry(i_strAccountHistory_id,i_strRezult,authorizationPageBean,i_strDecsription);
	    authorizationPageBean.ifPresent(action -> action.setStrEMail("Your order was processed successfully. Here is your receipt."));
		double add_amount = 0;
		double old_amount = 0;
		double total_amount = 0;
		String user_id = "";
		String query = "";
		String order_id = "";
	    TransactionStatus transactionStatus = transactionSupport.beginTransaction();

		query = "select add_amount , old_amount ,  date_input , rate , date_end , user_id , order_id from  account_hist where  id =  "
				+ i_strAccountHistory_id;
		List<Map<String,Object>> rows =transactionSupport.getJdbcTemplate().queryForList(query);
		if (rows.size() > 0) {
			add_amount =  Double.parseDouble(((String) rows.get(0).get("add_amount")));
			old_amount = Double.parseDouble(((String) rows.get(0).get("old_amount")));
			user_id = (String) rows.get(0).get("user_id");
			order_id = (String) rows.get(0).get("order_id");
			total_amount = old_amount + add_amount;
		}
		
        try {
          query = "UPDATE account_hist SET complete = ? , active = ? , rezult_cd = ?  WHERE  id =  "+ i_strAccountHistory_id + "";
          transactionSupport.getJdbcTemplate().update(query,true,false,i_strRezult);
          query = "UPDATE account SET amount = ? , curr = ? , date_input = ? WHERE  user_id = "+ user_id;
          transactionSupport.getJdbcTemplate().update(query,Double.valueOf(total_amount),Integer.valueOf(authorizationPageBean.get().getCurrency_id()),new java.util.Date());
          query = "update orders  set paystatus_id = " + PayStatus.SUCCESS + " where order_id = " + order_id;
          transactionSupport.getJdbcTemplate().update(query);
          transactionSupport.commitTransaction(transactionStatus);
        } catch (Exception e) {
          transactionSupport.rollbackTransaction(transactionStatus);
          logger.error(e.getLocalizedMessage(),e);
        } finally {
          logger.exit();
        }

		// finally no use it here .

	}

}
