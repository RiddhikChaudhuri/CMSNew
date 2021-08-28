package com.cbsinc.cms.publisher.controllers;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.Ext2ProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSExtProductPageModel;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;



@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class Ext2ProductPostAction extends CMSObjects{

    private XLogger logger = XLoggerFactory.getXLogger(Ext2ProductPostAction.class.getName());

    @Autowired
	ProductPostAllFaced productPostAllFaced;
    
    @Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;
      
    @Autowired
    AuthorizationPageFaced authorizationPageFaced;

    public Ext2ProductPostAction() {
      logger.entry();
      logger.exit();
    }

    
    @PostMapping("/doPostPageExt2Product")
	public CMSExtProductPageModel doPost(@RequestBody Ext2ProductRequest ext2ProductRequest)throws Exception {
        logger.entry(ext2ProductRequest);
		action(ext2ProductRequest);
		PublisherBean publisherBeanId = getPublisherBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();

		if (ext2ProductRequest.getBigimage_id() == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (ext2ProductRequest.getImage_id() == null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
				logger.exit();
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

    @GetMapping("/doGetPageExt2Product")
	public CMSExtProductPageModel doGet(@RequestBody Ext2ProductRequest ext2ProductRequest)
			throws Exception {
        logger.entry(ext2ProductRequest);
	    action(ext2ProductRequest);
	    PublisherBean publisherBeanId = getPublisherBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		productPostAllFaced.initPage(ext2ProductRequest.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
			logger.exit();
			return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
		}

		boolean jsf_admin = false;
		
		String jsf_admin_key = jsf_admin_setup_resouce;
		if (jsf_admin_key == null || jsf_admin_key.equals(""))
			jsf_admin = false;
		jsf_admin_key = jsf_admin_key.trim();
		jsf_admin = jsf_admin_key.equals("true");
		publisherBeanId.setNameOfPage("Ext2ProductPost");
		
			
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
    }

	public void action(Ext2ProductRequest ext2ProductRequest)throws Exception {

	    PublisherBean publisherBeanId = getPublisherBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		logger.entry(ext2ProductRequest,publisherBeanId,authorizationPageBeanId);

		if (publisherBeanId == null || authorizationPageBeanId == null || productPostAllFaced == null)
			return;
		

		String softname = ext2ProductRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}


		if (ext2ProductRequest.getType_id() != null) {
			publisherBeanId.setType_id(ext2ProductRequest.getType_id());
		}

		String softcost = ext2ProductRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = ext2ProductRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = ext2ProductRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = ext2ProductRequest.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = ext2ProductRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = ext2ProductRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (ext2ProductRequest.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(ext2ProductRequest.getPortlettype_id());
		}

		String filename = ext2ProductRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = ext2ProductRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = ext2ProductRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (ext2ProductRequest.getSalelogic_id()!= null)
			publisherBeanId.setProgname_id(ext2ProductRequest.getSalelogic_id());

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
