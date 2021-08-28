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
import com.cbsinc.cms.dto.pages.request.Ext1ProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSExtProductPageModel;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;



@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class Ext1ProductPostUserAction extends CMSObjects{
    
    private XLogger logger = XLoggerFactory.getXLogger(Ext1ProductPostUserAction.class.getName());
  
    @Autowired
	ProductPostAllFaced productPostAllFaced;
	
	@Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;
	
	public Ext1ProductPostUserAction() {
      logger.entry();
      logger.exit();
    }

    @PostMapping("/doPostPageExt1ProductUser")
	public CMSExtProductPageModel doPost(@RequestBody Ext1ProductRequest ext1ProductRequest)throws Exception {
        logger.entry(ext1ProductRequest);
		action(ext1ProductRequest);
		PublisherBean publisherBeanId = getPublisherBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();

		if (ext1ProductRequest.getBigimage_id()== null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (ext1ProductRequest.getImage_id()== null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
				logger.exit();
				return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
			}
			productPostAllFaced.insertRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,authorizationPageBeanId);
		} else
			productPostAllFaced.updateRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,authorizationPageBeanId);
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
	}

    @GetMapping("/doGetPageExt1ProductUser")
    public CMSExtProductPageModel doGet(@RequestBody Ext1ProductRequest ext1ProductRequest) throws Exception {
      logger.entry(ext1ProductRequest);
      action(ext1ProductRequest);
      PublisherBean publisherBeanId = getPublisherBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
      productPostAllFaced.initPage(ext1ProductRequest.getProduct_id(), publisherBeanId,authorizationPageBeanId);
      // if insert and limmit not add message
      if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
          && publisherBeanId.getSoft_id().compareTo("-1") == 0) {
        authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
      }
      logger.exit();
      return new CMSExtProductPageModel(publisherBeanId, authorizationPageBeanId);



    }

	private void action(Ext1ProductRequest ext1ProductRequest)throws Exception {

	  PublisherBean publisherBeanId = getPublisherBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		
		logger.entry(authorizationPageBeanId,publisherBeanId,ext1ProductRequest);
		if (publisherBeanId == null || authorizationPageBeanId == null || productPostAllFaced == null)
			return;
		String softname = ext1ProductRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}
		if (ext1ProductRequest.getType_id() != null) {
			publisherBeanId.setType_id(ext1ProductRequest.getType_id());
		}

		String softcost = ext1ProductRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = ext1ProductRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = ext1ProductRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = ext1ProductRequest.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = ext1ProductRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = ext1ProductRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (ext1ProductRequest.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(ext1ProductRequest.getPortlettype_id());
		}

		String filename =ext1ProductRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = ext1ProductRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = ext1ProductRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (ext1ProductRequest.getSalelogic_id()!= null)
			publisherBeanId.setProgname_id(ext1ProductRequest.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
		}
		logger.exit();

	}

}
