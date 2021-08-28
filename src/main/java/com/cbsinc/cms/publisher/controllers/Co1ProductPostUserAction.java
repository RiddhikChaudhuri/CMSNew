package com.cbsinc.cms.publisher.controllers;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.response.CMSCo1ProductPostUserModel;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.PublisherBean;



@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class Co1ProductPostUserAction {

  private final XLogger logger = XLoggerFactory.getXLogger(AccountHistoryController.class.getName());

  
	@Value("${is_criteria_by_catalog}")
    private String is_criteria_by_catalog_string;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	
	
	@Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;

	public Co1ProductPostUserAction() {
		logger.entry();
		
		logger.exit();
	}

	@PostMapping(value = "doPostCo1ProductUser", consumes = "application/json", produces = "application/json")
	public @ResponseBody CMSCo1ProductPostUserModel postCo1ProductUserAction(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {
		action(request, response, servletContext);
		HttpSession session = request.getSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
		CatalogListBean catalogListBeanId = (CatalogListBean) session.getAttribute("catalogListBeanId");
		CatalogEditBean catalogEditBeanI = (CatalogEditBean) session.getAttribute("catalogEditBeanI");
		CatalogAddBean catalogAddBeanId = (CatalogAddBean) session.getAttribute("catalogAddBeanId");

		// POST
		if (request.getParameter("action") != null) {
			publisherBeanId.setAction(request.getParameter("action"));

			if (publisherBeanId.getAction().compareTo("add") == 0) {

				if (request.getParameter("name") != null) {
					catalogAddBeanId.setName(request.getParameter("name"));
				} else
					catalogAddBeanId.setName("");

				if (request.getMethod().toUpperCase().compareTo("POST") == 0) {
					catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					//response.sendRedirect("ProductPostCre.jsp");
	                   return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);

				}
			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				if (request.getParameter("row") != null) {
					int index = catalogListBeanId.stringToInt(request.getParameter("row"));
					catalogEditBeanI.setIndx_select(index);
				}
				if (request.getParameter("name") != null) {
					catalogEditBeanI.setName(request.getParameter("name"));
				}

				if (request.getParameter("catalog_id") != null) {
					authorizationPageBeanId.setCatalog_id(request.getParameter("catalog_id"));
				}

				if (request.getMethod().toUpperCase().compareTo("POST") == 0) {
					catalogEditBeanI.editCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					//response.sendRedirect("ProductPostCre.jsp");
	                return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);
				}

			}
			if (publisherBeanId.getAction().compareTo("save") == 0) {

				if (request.getParameter("bigimage_id") == null) {
					publisherBeanId.setBigimage_id("-1");
				}
				if (request.getParameter("image_id") == null) {
					publisherBeanId.setImage_id("-1");
				}

				publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());

				if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {

					if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
						authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(servletContext)
								.getString("global_has_limmit_forsite"));
						response.sendRedirect("PostManager.jsp");
		                  return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);
					}

					productPostAllFaced.saveDescSoft(publisherBeanId, authorizationPageBeanId);
					publisherBeanId.setAction("");
					response.sendRedirect(
							"Productlist.jsp?offset=" + 0 + "&catalog_id=" + authorizationPageBeanId.getCatalog_id());
				} else {
					productPostAllFaced.updateDescSoft(publisherBeanId, authorizationPageBeanId);
					publisherBeanId.setAction("");
					response.sendRedirect("Productlist.jsp?offset=" + authorizationPageBeanId.getOffsetLastPage()
							+ "&catalog_id=" + authorizationPageBeanId.getCatalog_id());
				}
			}

		} else
			publisherBeanId.setAction("");
        return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);
	}

	@GetMapping(value = "doGetCo1ProductUser", consumes = "application/json", produces = "application/json")
	public CMSCo1ProductPostUserModel doGet(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {
		HttpSession session = request.getSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");

		String notselected = authorizationPageBeanId.getLocalization(servletContext).getString("notselected");
		CatalogListBean catalogListBeanId = (CatalogListBean) session.getAttribute("catalogListBeanId");
		CatalogEditBean catalogEditBeanI = (CatalogEditBean) session.getAttribute("catalogEditBeanI");
		CatalogAddBean catalogAddBeanId = (CatalogAddBean) session.getAttribute("catalogAddBeanId");

		action(request, response, servletContext);

		productPostAllFaced.initPage(request.getParameter("product_id"), publisherBeanId, authorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("global_has_limmit_forsite"));
			 return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);
	}
		boolean is_criteria_by_catalog =Boolean.valueOf(is_criteria_by_catalog_string);
		
		publisherBeanId.setCriteria1_label(
				productPostAllFaced.getOneLabel("select  label   from creteria1   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria2_label(
				productPostAllFaced.getOneLabel("select  label   from creteria2   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria3_label(
				productPostAllFaced.getOneLabel("select  label   from creteria3   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria4_label(
				productPostAllFaced.getOneLabel("select  label   from creteria4   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria5_label(
				productPostAllFaced.getOneLabel("select  label   from creteria5   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria6_label(
				productPostAllFaced.getOneLabel("select  label   from creteria6   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria7_label(
				productPostAllFaced.getOneLabel("select  label   from creteria7   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria8_label(
				productPostAllFaced.getOneLabel("select  label   from creteria8   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria9_label(
				productPostAllFaced.getOneLabel("select  label   from creteria9   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria10_label(
				productPostAllFaced.getOneLabel("select  label   from creteria10   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));

		publisherBeanId.setSelect_creteria1_id(
				productPostAllFaced.getComboBoxAutoSubmitLocale("creteria1_id", publisherBeanId.getCreteria1_id(),
						notselected, "select creteria1_id , name   from creteria1   where  active = true "
								+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setSelect_creteria2_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria2_id",
				publisherBeanId.getCreteria2_id(), notselected,
				"select creteria2_id , name   from creteria2   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria1_id() + " ) "));
		publisherBeanId.setSelect_creteria3_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria3_id",
				publisherBeanId.getCreteria3_id(), notselected,
				"select creteria3_id , name   from creteria3   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria2_id() + " ) "));
		publisherBeanId.setSelect_creteria4_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria4_id",
				publisherBeanId.getCreteria4_id(), notselected,
				"select creteria4_id , name   from creteria4   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria3_id() + " ) "));
		publisherBeanId.setSelect_creteria5_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria5_id",
				publisherBeanId.getCreteria5_id(), notselected,
				"select creteria5_id , name   from creteria5   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria4_id() + " ) "));
		publisherBeanId.setSelect_creteria6_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria6_id",
				publisherBeanId.getCreteria6_id(), notselected,
				"select creteria6_id , name   from creteria6   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria5_id() + " ) "));
		publisherBeanId.setSelect_creteria7_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria7_id",
				publisherBeanId.getCreteria7_id(), notselected,
				"select creteria7_id , name   from creteria7   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria6_id() + " ) "));
		publisherBeanId.setSelect_creteria8_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria8_id",
				publisherBeanId.getCreteria8_id(), notselected,
				"select creteria8_id , name   from creteria8   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria7_id() + " ) "));
		publisherBeanId.setSelect_creteria9_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria9_id",
				publisherBeanId.getCreteria9_id(), notselected,
				"select creteria9_id , name   from creteria9   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria8_id() + " ) "));
		publisherBeanId.setSelect_creteria10_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria10_id",
				publisherBeanId.getCreteria10_id(), notselected,
				"select creteria10_id , name   from creteria10   where  active = true "
						+ publisherBeanId.getPartCriteria(publisherBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria9_id() + " ) "));

		if (request.getParameter("action") != null) {

			publisherBeanId.setAction(request.getParameter("action"));

			if (publisherBeanId.getAction().compareTo("add") == 0) {

				if (request.getParameter("name") != null) {
					catalogAddBeanId.setName(request.getParameter("name"));
				} else
					catalogAddBeanId.setName("");

				if (request.getMethod().toUpperCase().compareTo("POST") == 0) {
					catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					//response.sendRedirect("ProductPostCre.jsp");
					 return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);
				}
			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				if (request.getParameter("row") != null) {
					int index = catalogListBeanId.stringToInt(request.getParameter("row"));
					catalogEditBeanI.setIndx_select(index);
				}
				if (request.getParameter("name") != null) {
					catalogEditBeanI.setName(request.getParameter("name"));
				}

				if (request.getParameter("catalog_id") != null) {
					authorizationPageBeanId.setCatalog_id(request.getParameter("catalog_id"));
				}

				if (request.getMethod().toUpperCase().compareTo("POST") == 0) {
					catalogEditBeanI.editCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					//response.sendRedirect("ProductPostCre.jsp");
				    return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId); 
				}

			}

		} else
			publisherBeanId.setAction("");

		boolean jsf_admin = false;
		
		String jsf_admin_key = jsf_admin_setup_resouce;
		if (jsf_admin_key == null || jsf_admin_key.equals(""))
			jsf_admin = false;
		jsf_admin_key = jsf_admin_key.trim();
		jsf_admin = jsf_admin_key.equals("true");
		publisherBeanId.setNameOfPage("Co1ProductPost.jsp");
		 
	return new CMSCo1ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanI,catalogListBeanId,authorizationPageBeanId);	

	}

	public void action(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext)
			throws Exception {
		PublisherBean publisherBeanId;
		CatalogListBean catalogListBeanId;
		AuthorizationPageBean authorizationPageBeanId;
		HttpSession session;

		session = request.getSession();
		publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		catalogListBeanId = (CatalogListBean) session.getAttribute("catalogListBeanId");
		authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");


		if (publisherBeanId == null || catalogListBeanId == null || authorizationPageBeanId == null
				|| productPostAllFaced == null) return;

		request.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache"); // HTTP 1.1
		response.setHeader("Pragma", "no-cache"); // HTTP 1.0
		response.setDateHeader("Expires", 0);


		if (request.getParameter("parent_id") != null) {

			authorizationPageBeanId.setCatalogParent_id(request.getParameter("parent_id"));
		}

		if (request.getParameter("type_id") != null) {
			publisherBeanId.setType_id(request.getParameter("type_id"));
		}

		if (request.getParameter("row") != null) {
			int index = catalogListBeanId.stringToInt(request.getParameter("row"));
			catalogListBeanId.setIndx_select(index);
		}
		if (request.getParameter("del") != null) {
			int index = catalogListBeanId.stringToInt(request.getParameter("del"));
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			request.setAttribute("del", null);
		}
		if (request.getParameter("offset") != null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(request.getParameter("offset")));
		}
//		 End Novigator ---

		if (request.getParameter("creteria1_id") != null)
			publisherBeanId.setCreteria1_id(request.getParameter("creteria1_id"));
		if (request.getParameter("creteria2_id") != null)
			publisherBeanId.setCreteria2_id(request.getParameter("creteria2_id"));
		if (request.getParameter("creteria3_id") != null)
			publisherBeanId.setCreteria3_id(request.getParameter("creteria3_id"));
		if (request.getParameter("creteria4_id") != null)
			publisherBeanId.setCreteria4_id(request.getParameter("creteria4_id"));
		if (request.getParameter("creteria5_id") != null)
			publisherBeanId.setCreteria5_id(request.getParameter("creteria5_id"));
		if (request.getParameter("creteria6_id") != null)
			publisherBeanId.setCreteria6_id(request.getParameter("creteria6_id"));
		if (request.getParameter("creteria7_id") != null)
			publisherBeanId.setCreteria7_id(request.getParameter("creteria7_id"));
		if (request.getParameter("creteria8_id") != null)
			publisherBeanId.setCreteria8_id(request.getParameter("creteria8_id"));
		if (request.getParameter("creteria9_id") != null)
			publisherBeanId.setCreteria9_id(request.getParameter("creteria9_id"));
		if (request.getParameter("creteria10_id") != null)
			publisherBeanId.setCreteria10_id(request.getParameter("creteria10_id"));


		if (request.getParameter("insert") != null) {
			if (request.getParameter("insert").compareTo("true") == 0)
				publisherBeanId.setSoft_id("-1");

		}

		String softname = request.getParameter("softname");
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id = request.getParameter("catalog_id");
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
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
			publisherBeanId.setFilename(filename);
		}

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
			//response.sendRedirect("Authorization.jsp");
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(servletContext).getString("post_message_notaccess_admin"));
			response.sendRedirect("Authorization.jsp");
		}

	}

}
