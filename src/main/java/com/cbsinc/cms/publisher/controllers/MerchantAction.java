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



import java.io.File;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.MerchantWebAction;
import com.cbsinc.cms.dto.pages.response.CMSAuthorizationPageModel;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;


@RestController
public class MerchantAction extends CMSObjects{
  
    private XLogger logger = XLoggerFactory.getXLogger(MerchantAction.class.getName());

    
    @Autowired
	private AuthorizationPageFaced authorizationPageFaced ;
	
	public boolean isInternet = true ;
	
	public MerchantAction() {
	  logger.entry();
	  logger.exit();
	}
	   
    @PostMapping(value="/doPostMerchant",produces = "application/json",consumes = "application/json")
	public CMSAuthorizationPageModel postMerchantAction(@RequestBody MerchantWebAction merchantRequest) throws Exception 
	{
		logger.entry(merchantRequest);
		Map<String,String> messageMail ;
		AuthorizationPageBean authorizationPageBeanId ;
		HttpSession session ;
		
		if(getHttpServletRequest().getRemoteAddr().startsWith("192."))isInternet = false ;
		if(getHttpServletRequest().getRemoteAddr().startsWith("10."))isInternet = false ;
		
		session = getHttpSession();
		authorizationPageBeanId = (AuthorizationPageBean)session.getAttribute("authorizationPageBeanId");
		
		messageMail = (Map<String, String>)session.getAttribute("messageMail");
		if( authorizationPageBeanId == null || merchantRequest == null  ) return null;
		
	
		messageMail.clear();
		messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName() ) ;
		messageMail.put("@LastName", authorizationPageBeanId.getStrLastName() ) ;
		messageMail.put("@CName",merchantRequest.getC_name()) ;
		
		messageMail.put("@TradeMark",merchantRequest.getTrademark());
		messageMail.put("@BizDescription",merchantRequest.getBiz_description());
		messageMail.put("@FormationDate",merchantRequest.getFormation_date());
		messageMail.put("@Taxid",merchantRequest.getTaxid());
		messageMail.put("@BizAddress",merchantRequest.getBiz_address());
		messageMail.put("@BizCity",merchantRequest.getBiz_city());
		messageMail.put("@BizCountry",merchantRequest.getBiz_country());
		messageMail.put("@BizZip",merchantRequest.getBiz_zip());
		messageMail.put("@BizPhone",merchantRequest.getBiz_phone());
		messageMail.put("@BizEmail",merchantRequest.getBiz_email());
		
		messageMail.put("@FoName",merchantRequest.getF_o_name());
		messageMail.put("@FoAddress",merchantRequest.getF_o_address());
		messageMail.put("@FoCity" ,merchantRequest.getF_o_city());
		messageMail.put("@FoCountry",merchantRequest.getF_o_country());
		messageMail.put("@FoZip",merchantRequest.getF_o_zip());
		messageMail.put("@FoOwnership",merchantRequest.getF_o_ownership());
		messageMail.put("@FoPhone",merchantRequest.getF_o_phone());
		messageMail.put("@FoEmail",merchantRequest.getF_o_email());
		
		
		messageMail.put("@SoName",merchantRequest.getS_o_name());
		messageMail.put("@SoAddress",merchantRequest.getS_o_address());
		messageMail.put("@SoCity",merchantRequest.getS_o_city());
		messageMail.put("@SoCountry",merchantRequest.getS_o_country());
		messageMail.put("@SoZip",merchantRequest.getS_o_zip());
		messageMail.put("@SoOwnership",merchantRequest.getS_o_ownership());
		messageMail.put("@SoPhone",merchantRequest.getS_o_phone());
		messageMail.put("@SoEmail",merchantRequest.getS_o_email());
		
		messageMail.put("@BankName",merchantRequest.getBank_name());
		messageMail.put("@BankAccountNumber",merchantRequest.getBank_account_number());
		messageMail.put("@BankRoutingNumber",merchantRequest.getBank_routing_number());
		messageMail.put("@BankAddress",merchantRequest.getBank_address());
		messageMail.put("@BankCountry",merchantRequest.getBank_country());
		messageMail.put("@BankCity",merchantRequest.getBank_city());
		messageMail.put("@BankZip",merchantRequest.getBank_zip());
		

		String sitePath = (String)session.getAttribute("site_path");
		String merchant = sitePath +File.separatorChar+"mail" + File.separatorChar + "merchant.txt" ;

		System.out.println("merchant path: "  + merchant   ) ; 
		
        AuthorizationPageBean ownerShop = authorizationPageFaced.getAuthorizationBeanOfRoleAdmin(authorizationPageBeanId.getSite_id());
		
		MessageSender mqSender = new MessageSender( session,SendMailMessageBean.messageQuery) ;
		Message message = new Message();
		message.put("to" , ownerShop.getStrEMail()  ) ;
		message.put("subject" , "Merchant appication") ;
		message.put("pathmessage" , merchant ) ;
		message.put("fields" , messageMail ) ;
		mqSender.send(message);

		authorizationPageBeanId.setStrMessage(" - Appication was sent to manager.");
		return new CMSAuthorizationPageModel(ownerShop);
	}

	
	public boolean isNumber(String tmp) {
		if (tmp == null) return false;
		if (tmp.length() == 0) return false;
		String IntField = "0123456789";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}

}
