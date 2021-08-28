package com.cbsinc.cms.publisher.controllers;

import java.util.Map;

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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.ProductUserPostWithoutCatalogRequestForm;
import com.cbsinc.cms.dto.pages.response.CMSNewBlockPostCreResponseModel;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.PublisherBean;

@RestController
public class ProductUserPostWithOutCatalogAction extends CMSObjects {

  private XLogger logger = XLoggerFactory.getXLogger(ProductUserPostWithOutCatalogAction.class.getName());

  
	PublisherBean publisherBeanId;
	CatalogListBean catalogListBeanId;
	CatalogEditBean catalogEditBeanId;
	CatalogAddBean catalogAddBeanId;
	AuthorizationPageBean authorizationPageBeanId;
	HttpSession session;
	Map messageMail;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	
	public ProductUserPostWithOutCatalogAction() {
      logger.entry();
      logger.exit();
    }
	
	String gen_code = "";

	@PostMapping(value="/doPostProductUserPostWithoutCatalog",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSNewBlockPostCreResponseModel doPost(@RequestBody ProductUserPostWithoutCatalogRequestForm productUserWithOutCatalogRequest)
			throws Exception {
	    ServletContext servletContext =  getServletContext();
	    HttpSession httpSession = getHttpSession();
		action(productUserWithOutCatalogRequest,httpSession);

		if (productUserWithOutCatalogRequest.getGen_number() != null) {
			String val1 = productUserWithOutCatalogRequest.getGen_number().trim();
			if (!val1.equals(gen_code.trim())) {
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("wrong_gen_number"));
			}
		} else {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("wrong_gen_number"));
		}

		if (productUserWithOutCatalogRequest.getBigimage_id() == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (productUserWithOutCatalogRequest.getImage_id() == null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("global_has_limmit_forsite"));
				//response.sendRedirect("PostManager.jsp");
				return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
			}
			productPostAllFaced.saveInformationWithCheck(publisherBeanId, authorizationPageBeanId);
		} else
			productPostAllFaced.updateInformationWithCheck(publisherBeanId, authorizationPageBeanId);

		messageMail.clear();
		messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName());
		messageMail.put("@LastName", authorizationPageBeanId.getStrLastName());
		messageMail.put("@Site", authorizationPageBeanId.getSite_dir());

		String sitePath = (String) httpSession.getAttribute("site_path");
		String addinfo = sitePath + "\\mail\\addinfo.txt";
		String attachFile = sitePath + "\\mail\\info.txt";
		// System.out.println("rezalt: " + sendMailAgent.putMessageInPool(null ,"Has
		// added new info on site ", addinfo , attachFile, messageMail ) ) ;
		MessageSender mqSender = new MessageSender(httpSession, SendMailMessageBean.messageQuery);
		Message message = new Message();
		message.put("to", null);
		message.put("subject", "Has added new info on site ");
		message.put("pathmessage", addinfo);
		message.put("attachFile", attachFile);
		message.put("fields", messageMail);
		mqSender.send(message);

		//response.sendRedirect("Productlist.jsp?offset=" + 0 + "&catalog_id=" + authorizationPageBeanId.getCatalog_id());
		return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
	}

	@GetMapping(value="/doGetProductUserPostWithoutCatalog",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSNewBlockPostCreResponseModel doGet(ProductUserPostWithoutCatalogRequestForm productUserWithOutCatalogRequest)
			throws Exception {
		action(productUserWithOutCatalogRequest,getHttpSession());
		ServletContext servletContext =  getServletContext();
		HttpServletRequest httpRequest =  getHttpServletRequest();
		productPostAllFaced.initPage(productUserWithOutCatalogRequest.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(servletContext).getString("global_has_limmit_forsite"));
		    
		}
		 return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);

	}

	public void action( ProductUserPostWithoutCatalogRequestForm productUserWithOutCatalogRequest,HttpSession session)	throws Exception {
		gen_code = (String) session.getAttribute("gen_number");
		publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		catalogListBeanId = getCatalogListBean().get();
		catalogEditBeanId = getCatalogEditBean().get();
		catalogAddBeanId = getCatalogAddBean().get();
		authorizationPageBeanId = getAuthorizationPageBean().get();
		ServletContext servletContext = getServletContext();
		messageMail = getMessageMail().get();
		if (publisherBeanId == null || catalogListBeanId == null || catalogEditBeanId == null || catalogAddBeanId == null
				|| authorizationPageBeanId == null || messageMail == null || productPostAllFaced == null) return;
//		 Start Novigator ---
		if (productUserWithOutCatalogRequest.getParent_id() != null) {
			authorizationPageBeanId.setCatalogParent_id(productUserWithOutCatalogRequest.getParent_id());
		}

		if (productUserWithOutCatalogRequest.getRow() != null) {
			int index = catalogListBeanId.stringToInt(productUserWithOutCatalogRequest.getRow());
			catalogListBeanId.setIndx_select(index);
		}
		if (productUserWithOutCatalogRequest.getDel()!= null) {
			int index = catalogListBeanId.stringToInt(productUserWithOutCatalogRequest.getDel());
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			productUserWithOutCatalogRequest.setDel(null);
		}
		if (productUserWithOutCatalogRequest.getOffset() != null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(productUserWithOutCatalogRequest.getOffset()));
		}
//		 End Novigator ---

		if (productUserWithOutCatalogRequest.getCreteria1_id() != null)
			publisherBeanId.setCreteria1_id(productUserWithOutCatalogRequest.getCreteria1_id());
		if (productUserWithOutCatalogRequest.getCreteria2_id()  != null)
			publisherBeanId.setCreteria2_id(productUserWithOutCatalogRequest.getCreteria2_id());
		if (productUserWithOutCatalogRequest.getCreteria3_id() != null)
			publisherBeanId.setCreteria3_id(productUserWithOutCatalogRequest.getCreteria3_id());
		if (productUserWithOutCatalogRequest.getCreteria4_id() != null)
			publisherBeanId.setCreteria4_id(productUserWithOutCatalogRequest.getCreteria4_id());
		if (productUserWithOutCatalogRequest.getCreteria5_id() != null)
			publisherBeanId.setCreteria5_id(productUserWithOutCatalogRequest.getCreteria5_id());
		if (productUserWithOutCatalogRequest.getCreteria6_id() != null)
			publisherBeanId.setCreteria6_id(productUserWithOutCatalogRequest.getCreteria6_id());
		if (productUserWithOutCatalogRequest.getCreteria7_id() != null)
			publisherBeanId.setCreteria7_id(productUserWithOutCatalogRequest.getCreteria7_id());
		if (productUserWithOutCatalogRequest.getCreteria8_id() != null)
			publisherBeanId.setCreteria8_id(productUserWithOutCatalogRequest.getCreteria8_id());
		if (productUserWithOutCatalogRequest.getCreteria9_id() != null)
			publisherBeanId.setCreteria9_id(productUserWithOutCatalogRequest.getCreteria9_id());
		if (productUserWithOutCatalogRequest.getCreteria10_id()!= null)
			publisherBeanId.setCreteria10_id(productUserWithOutCatalogRequest.getCreteria10_id());

		String softname = productUserWithOutCatalogRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id = productUserWithOutCatalogRequest.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		String softcost = productUserWithOutCatalogRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = productUserWithOutCatalogRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = productUserWithOutCatalogRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription =productUserWithOutCatalogRequest.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = productUserWithOutCatalogRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = productUserWithOutCatalogRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (productUserWithOutCatalogRequest.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(productUserWithOutCatalogRequest.getPortlettype_id());
		}

		String filename = productUserWithOutCatalogRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = productUserWithOutCatalogRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		} else
			publisherBeanId.setBigimgname("-1");

		String bigimage_id = productUserWithOutCatalogRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (productUserWithOutCatalogRequest.getSalelogic_id() != null)
			publisherBeanId.setProgname_id(productUserWithOutCatalogRequest.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(servletContext).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(servletContext).getString("post_message_notaccess_admin"));
		}

	}

}
