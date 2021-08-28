package com.cbsinc.cms.publisher.controllers;

import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.BlogExtProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSBlogExtProductResponseDTO;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;



@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class BlogExtProductPostAction extends CMSObjects{
  
  private XLogger logger = XLoggerFactory.getXLogger(BlogExtProductPostAction.class.getName());

    @Autowired
	ProductPostAllFaced productPostAllFaced;
    
    @Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;
    
    public BlogExtProductPostAction() {
      logger.entry();
      logger.exit();
    }
    
    @PostMapping("/doPostPageBlogExtProduct")
	public @ResponseBody CMSBlogExtProductResponseDTO doPost(@RequestBody BlogExtProductRequest blogExtProductRequest)throws Exception {
        logger.entry(blogExtProductRequest);
		HttpSession session = getHttpSession();
		String gen_code = (String) session.getAttribute("gen_number");
		PublisherBean publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		if (publisherBeanId == null || authorizationPageBeanId == null || gen_code == null)
			return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
		if (blogExtProductRequest.getGen_number() != null) {
			String val1 = blogExtProductRequest.getGen_number().trim();

			if (!val1.equals(gen_code.trim())) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization().getString("wrong_gen_number"));
				return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
			}
		} else {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("wrong_gen_number"));
			return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
		}

		action(blogExtProductRequest);

		if (blogExtProductRequest .getBigimage_id()== null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (blogExtProductRequest.getImage_id()== null) {
			publisherBeanId.setImage_id("-1");
		}

		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0)
			productPostAllFaced.insertRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		else
			productPostAllFaced.updateRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		logger.exit();
		return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
	}
    
    @GetMapping(name = "/doGetPageBlogExtProduct",produces = {MediaType.APPLICATION_JSON_VALUE})
	public CMSBlogExtProductResponseDTO doGet(@RequestBody BlogExtProductRequest blogExtProductRequest)throws Exception {
		HttpSession session = getHttpSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session
				.getAttribute("authorizationPageBeanId");

		action(blogExtProductRequest);
		productPostAllFaced.initPage(blogExtProductRequest.getProduct_id(), publisherBeanId, authorizationPageBeanId);

		if (authorizationPageBeanId.getIntLevelUp() == 2) {

			String jsf_admin_key = jsf_admin_setup_resouce;
			if (jsf_admin_key == null || jsf_admin_key.equals("")) {
      }
			jsf_admin_key = jsf_admin_key.trim();
			publisherBeanId.setNameOfPage("BlogExtProductPost.jsp");

		}
      return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
	}

	public void action(BlogExtProductRequest blogExtProductRequest)	throws Exception {

		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		PublisherBean publisherBeanId = getPublisherBean().get();
		String softname =blogExtProductRequest.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id = blogExtProductRequest.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		if (blogExtProductRequest.getType_id() != null) {
			publisherBeanId.setType_id(blogExtProductRequest.getType_id());
		}

		String softcost = blogExtProductRequest.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = blogExtProductRequest.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = blogExtProductRequest.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = blogExtProductRequest.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = blogExtProductRequest.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = blogExtProductRequest.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (blogExtProductRequest.getPortlettype_id()!= null) {
			publisherBeanId.setPortlettype_id(blogExtProductRequest.getPortlettype_id());
		}

		String filename = blogExtProductRequest.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename =blogExtProductRequest.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id =blogExtProductRequest.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (blogExtProductRequest.getSalelogic_id() != null)
			publisherBeanId.setProgname_id(blogExtProductRequest.getSalelogic_id());
		// post_forum_notaccess
		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
			return;
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_forum_notaccess"));
			return;
		}

		if (authorizationPageBeanId.getStrLogin().compareTo("user") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_forum_notaccess"));
			return;
		}

	}

}
