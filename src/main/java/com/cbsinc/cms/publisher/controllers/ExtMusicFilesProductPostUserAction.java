package com.cbsinc.cms.publisher.controllers;

import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.ExtMusicFileProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSExtProductPageModel;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;

@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class ExtMusicFilesProductPostUserAction extends CMSObjects{

  private XLogger logger =
      XLoggerFactory.getXLogger(ExtMusicFilesProductPostUserAction.class.getName());

  public ExtMusicFilesProductPostUserAction() {
    logger.entry();
    logger.exit();
  }
    @Autowired
	ProductPostAllFaced productPostAllFaced;
    
    @PostMapping("/doPostExtMusicFilesProduct")
	public CMSExtProductPageModel doPost(@RequestBody ExtMusicFileProductRequest extOfficeFileProductRequest)throws Exception {
        logger.entry(extOfficeFileProductRequest);
        action(extOfficeFileProductRequest);
		PublisherBean publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();

		if (extOfficeFileProductRequest.getBigimage_id() == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (extOfficeFileProductRequest.getImage_id() == null) {
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
		logger.exit();
		//response.sendRedirect("Policy.jsp?policy_byproductid=" + authorizationPageBeanId.getLastProductId());
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
	}
    
    @GetMapping("/doGetExtMusicFilesProduct")
	public CMSExtProductPageModel doGet(@RequestBody ExtMusicFileProductRequest extOfficeFileProductRequest)throws Exception {
        logger.entry(extOfficeFileProductRequest);
		action(extOfficeFileProductRequest);
		 HttpSession session = getHttpSession();
	      PublisherBean publisherBeanId = getPublisherBean().get();

		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session
				.getAttribute("authorizationPageBeanId");
		productPostAllFaced.initPage(extOfficeFileProductRequest.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
			
		}
		logger.exit();
		return new CMSExtProductPageModel(publisherBeanId,authorizationPageBeanId);
    }

	public void action(ExtMusicFileProductRequest extOfficeFileProductRequest)throws Exception {

	  HttpSession session = getHttpSession();
      PublisherBean publisherBeanId = getPublisherBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		logger.entry(publisherBeanId,authorizationPageBeanId,session,extOfficeFileProductRequest);
		if (publisherBeanId == null || authorizationPageBeanId == null || productPostAllFaced == null) return;
		
		

		String softname = extOfficeFileProductRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}
		if (extOfficeFileProductRequest.getType_id() != null) {
			publisherBeanId.setType_id(extOfficeFileProductRequest.getType_id());
		}

		String softcost = extOfficeFileProductRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = extOfficeFileProductRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = extOfficeFileProductRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = extOfficeFileProductRequest.getFulldescription();
		    if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename =extOfficeFileProductRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = extOfficeFileProductRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (extOfficeFileProductRequest.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(extOfficeFileProductRequest.getPortlettype_id());
		}

		String filename = extOfficeFileProductRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = extOfficeFileProductRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id =extOfficeFileProductRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (extOfficeFileProductRequest.getSalelogic_id() != null)
			publisherBeanId.setProgname_id(extOfficeFileProductRequest.getSalelogic_id());

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
