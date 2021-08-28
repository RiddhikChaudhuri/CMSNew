package com.cbsinc.cms.publisher.controllers;

import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.ExtTabsProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSExtProductPageModel;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;


@RestController
public class ExtTabsProductPostAction extends CMSObjects {

  private XLogger logger =   XLoggerFactory.getXLogger(ExtTabsProductPostAction.class.getName());

  public ExtTabsProductPostAction() {
    logger.entry();
    logger.exit();
  }
  
  @Autowired
  ProductPostAllFaced productPostAllFaced;

  @PostMapping("/doPostExtTabsProduct")
  public CMSExtProductPageModel doPost(@RequestBody ExtTabsProductRequest extTabsProductPostRequest)throws Exception {
        logger.entry(extTabsProductPostRequest);
		action(extTabsProductPostRequest);
		PublisherBean publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();

		if (extTabsProductPostRequest.getBigimage_id() == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (extTabsProductPostRequest.getImage_id() == null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
				//response.sendRedirect("PolicyManager.jsp");
				return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
			}
			productPostAllFaced.insertRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		} else
			productPostAllFaced.updateRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
  }
    
    @GetMapping("/doGetExtTabsProduct")
	public CMSExtProductPageModel doGet(@RequestBody ExtTabsProductRequest extTabsProductPostRequest)throws Exception {
        logger.entry(extTabsProductPostRequest);
		action(extTabsProductPostRequest);
		HttpSession session = getHttpSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
		
		productPostAllFaced.initPage(extTabsProductPostRequest.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
		}
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
	}

	public void action(ExtTabsProductRequest extTabsProductPostRequest)throws Exception {

		PublisherBean publisherBeanId;

		HttpSession session = getHttpSession();
		publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		logger.entry(authorizationPageBeanId,publisherBeanId,session);
		
		if (publisherBeanId == null || authorizationPageBeanId == null || productPostAllFaced == null) return;
		
		String softname = extTabsProductPostRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		if (extTabsProductPostRequest.getType_id() != null) {
			publisherBeanId.setType_id(extTabsProductPostRequest.getType_id());
		}

		String softcost = extTabsProductPostRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = extTabsProductPostRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description =extTabsProductPostRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = extTabsProductPostRequest.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = extTabsProductPostRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = extTabsProductPostRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (extTabsProductPostRequest.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(extTabsProductPostRequest.getPortlettype_id());
		}

		String filename = extTabsProductPostRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = extTabsProductPostRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = extTabsProductPostRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (extTabsProductPostRequest.getSalelogic_id() != null)
			publisherBeanId.setProgname_id(extTabsProductPostRequest.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() != 2) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
		}
		logger.exit();

	}

}
