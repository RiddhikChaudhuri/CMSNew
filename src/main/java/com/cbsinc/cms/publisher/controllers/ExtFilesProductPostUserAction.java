package com.cbsinc.cms.publisher.controllers;

import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.ExtFilesProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSExtProductPageModel;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;



@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class ExtFilesProductPostUserAction extends CMSObjects{
    
    private XLogger logger = XLoggerFactory.getXLogger(ExtFilesProductPostUserAction.class.getName());
  
    public ExtFilesProductPostUserAction() {
      logger.entry();
      logger.exit();
    }

  
    @Autowired
	ProductPostAllFaced productPostAllFaced;
    
    @PostMapping("/doPostUserExtFilesProduct")
	public CMSExtProductPageModel doPost(@RequestBody ExtFilesProductRequest extFilesProductRequest)throws Exception {
		logger.entry(extFilesProductRequest);
        action(extFilesProductRequest);
		PublisherBean publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		if (extFilesProductRequest.getBigimage_id() == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (extFilesProductRequest.getImage_id() == null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
				logger.exit();
				return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
			}
			productPostAllFaced.insertRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		} else
			productPostAllFaced.updateRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		//response.sendRedirect("Policy.jsp?policy_byproductid=" + authorizationPageBeanId.getLastProductId());
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
	}
    
    @GetMapping("/doGetUserExtFilesProduct")
	public CMSExtProductPageModel doGet(@RequestBody ExtFilesProductRequest extFilesProductRequest)throws Exception {
        logger.entry(extFilesProductRequest);
        action(extFilesProductRequest);
        PublisherBean publisherBeanId = getPublisherBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		productPostAllFaced.initPage(extFilesProductRequest.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
		}
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
	}

	public void action(ExtFilesProductRequest extFilesProductRequest) throws Exception {
		PublisherBean publisherBeanId = getPublisherBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		logger.entry(extFilesProductRequest,publisherBeanId,authorizationPageBeanId);
		if (publisherBeanId == null || authorizationPageBeanId == null || productPostAllFaced == null) return;
		

		String softname = extFilesProductRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		if (extFilesProductRequest.getType_id() != null) {
			publisherBeanId.setType_id(extFilesProductRequest.getType_id());
		}

		String softcost = extFilesProductRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = extFilesProductRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = extFilesProductRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = extFilesProductRequest.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = extFilesProductRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = extFilesProductRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (extFilesProductRequest.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(extFilesProductRequest.getPortlettype_id());
		}

		String filename = extFilesProductRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = extFilesProductRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = extFilesProductRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (extFilesProductRequest.getSalelogic_id() != null)
			publisherBeanId.setProgname_id(extFilesProductRequest.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
		}
		
		logger.exit();

	}

}
