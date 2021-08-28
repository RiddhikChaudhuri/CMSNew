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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.response.CMSBlogExtProductResponseDTO;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.PublisherBean;


@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class ServicePagePostUserAction{
    
    @Autowired
	ProductPostAllFaced productPostAllFaced;
	
	@Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;
	
    @PostMapping(value="/doPostPageService", consumes = "application/json", produces = "application/json")
	public @ResponseBody CMSBlogExtProductResponseDTO doPost(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {
		action(request, response, servletContext);
		HttpSession session = request.getSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
		if (request.getParameter("bigimage_id") == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (request.getParameter("image_id") == null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(
						authorizationPageBeanId.getLocalization(servletContext).getString("global_has_limmit_forsite"));
				response.sendRedirect("PolicyManager.jsp");
		           return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
			}
			productPostAllFaced.insertRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		} else
			productPostAllFaced.updateRowWithParent("" + authorizationPageBeanId.getLastProductId(), publisherBeanId,
					authorizationPageBeanId);
		//response.sendRedirect("Policy.jsp?policy_byproductid=" + authorizationPageBeanId.getLastProductId());
	       return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
    }

    @GetMapping(value="/doGetPageService", consumes = "application/json", produces = "application/json")
	public @ResponseBody CMSBlogExtProductResponseDTO doGet(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {

		action(request, response, servletContext);
		HttpSession session = request.getSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");

		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");

		productPostAllFaced.initPage(request.getParameter("product_id"), publisherBeanId, authorizationPageBeanId);
//		if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("global_has_limmit_forsite"));
			response.sendRedirect("PolicyManager.jsp");
	        return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
		}


		boolean jsf_admin = false;
		String jsf_admin_key = jsf_admin_setup_resouce;
		if (jsf_admin_key == null || jsf_admin_key.equals(""))
			jsf_admin = false;
		jsf_admin_key = jsf_admin_key.trim();
		jsf_admin = jsf_admin_key.equals("true");
		publisherBeanId.setNameOfPage("ServicePagePost.jsp");
		if (jsf_admin)
			response.sendRedirect("admin.jsf");
        return new CMSBlogExtProductResponseDTO(publisherBeanId, authorizationPageBeanId);
	}

	public void action(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {

		PublisherBean publisherBeanId;

		HttpSession session = request.getSession();
		publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");

		if (publisherBeanId == null || authorizationPageBeanId == null || productPostAllFaced == null) return;
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0);

		String softname = request.getParameter("softname");
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}


		if (request.getParameter("type_id") != null) {
			publisherBeanId.setType_id(request.getParameter("type_id"));
		}

		String softcost = request.getParameter("softcost");
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = request.getParameter("currency_id");
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = request.getParameter("description");
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = request.getParameter("fulldescription");
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = request.getParameter("imagename");
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = request.getParameter("image_id");
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (request.getParameter("portlettype_id") != null) {
			publisherBeanId.setPortlettype_id(request.getParameter("portlettype_id"));
		}

		String filename = request.getParameter("filename");
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = request.getParameter("bigimagename");
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = request.getParameter("bigimage_id");
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (request.getParameter("salelogic_id") != null)
			publisherBeanId.setProgname_id(request.getParameter("salelogic_id"));

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("session_time_out"));
			response.sendRedirect("Authorization.jsp");
		} else
			publisherBeanId.setUser_id(String.valueOf(authorizationPageBeanId.getIntUserID()));

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("post_message_notaccess_admin"));
			response.sendRedirect("Authorization.jsp");
		}

	}

}
