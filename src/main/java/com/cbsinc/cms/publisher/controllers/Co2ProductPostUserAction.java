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
import com.cbsinc.cms.dto.pages.request.Co2ProductRequest;
import com.cbsinc.cms.dto.pages.response.CMSCo2ProductPostUserModel;
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
public class Co2ProductPostUserAction extends CMSObjects{

    @Value("${is_criteria_by_catalog}")
	private String  is_criteria_by_catalog_string;
    
    @Value("${jsf_admin}")
    private String jsf_admin;
	
	private XLogger logger = XLoggerFactory.getXLogger(Co2ProductPostUserAction.class.getName());
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;

	@Autowired
	public Co2ProductPostUserAction() {
	    logger.entry(is_criteria_by_catalog_string,productPostAllFaced);
	    logger.exit();
	}
	
	@PostMapping("/doPostPageCo2ProductUser")
	public CMSCo2ProductPostUserModel doPost(@RequestBody Co2ProductRequest co2ProductRequestObject)throws Exception {
	    logger.entry(co2ProductRequestObject);
		action(co2ProductRequestObject);
		 PublisherBean publisherBeanId = getPublisherBean().get();
	        CatalogListBean catalogListBeanId = getCatalogListBean().get();
	        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
	        CatalogEditBean catalogEditBeanId =getCatalogEditBean().get();
	        CatalogAddBean catalogAddBeanId = getCatalogAddBean().get();

		// POST
		if (co2ProductRequestObject.getAction() != null) {
			publisherBeanId.setAction(co2ProductRequestObject.getAction());

			if (publisherBeanId.getAction().compareTo("add") == 0) {

				if (co2ProductRequestObject.getName() != null) {
					catalogAddBeanId.setName(co2ProductRequestObject.getName());
				} else
					catalogAddBeanId.setName("");
					catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				if (co2ProductRequestObject.getRow()!= null) {
					int index = catalogListBeanId.stringToInt(co2ProductRequestObject.getRow());
					catalogEditBeanId.setIndx_select(index);
				}
				if (co2ProductRequestObject.getName()!= null) {
					catalogEditBeanId.setName(co2ProductRequestObject.getName());
				}

				if (co2ProductRequestObject.getCatalog_id() != null) {
					authorizationPageBeanId.setCatalog_id(co2ProductRequestObject.getCatalog_id());
				}
				catalogEditBeanId.editCatalog(authorizationPageBeanId);
				publisherBeanId.setAction("");
				return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
			}
		}
		if (publisherBeanId.getAction().compareTo("save") == 0) {

				if (co2ProductRequestObject.getBigimage_id() == null) {
					publisherBeanId.setBigimage_id("-1");
				}
				if (co2ProductRequestObject.getImage_id() == null) {
					publisherBeanId.setImage_id("-1");
				}

				publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());

				if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {

					if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
						authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
						return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
					}
					productPostAllFaced.saveDescSoft(publisherBeanId, authorizationPageBeanId);
					publisherBeanId.setAction("");
				} else {
					productPostAllFaced.updateDescSoft(publisherBeanId, authorizationPageBeanId);
					publisherBeanId.setAction("");
				}

		}else
			publisherBeanId.setAction("");
		
		return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
	}
	
	@GetMapping("/doGetPageCo2ProductUser")
	public CMSCo2ProductPostUserModel doGet(@RequestBody Co2ProductRequest co2ProductRequestObject)throws Exception {
	    PublisherBean publisherBeanId = getPublisherBean().get();
	    CatalogListBean catalogListBeanId = getCatalogListBean().get();
	    AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		String notselected = authorizationPageBeanId.getLocalization(getServletContext()).getString("notselected");
		CatalogEditBean catalogEditBeanId =getCatalogEditBean().get();
		CatalogAddBean catalogAddBeanId = getCatalogAddBean().get();

		action(co2ProductRequestObject);

		productPostAllFaced.initPage(co2ProductRequestObject.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
			return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
		}
		
		Boolean is_criteria_by_catalog=Boolean.valueOf(is_criteria_by_catalog_string);

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

		if (co2ProductRequestObject.getAction() != null) {

			publisherBeanId.setAction(co2ProductRequestObject.getAction());

			if (publisherBeanId.getAction().compareTo("add") == 0) {

				if (co2ProductRequestObject.getName()!= null) {
					catalogAddBeanId.setName(co2ProductRequestObject.getName());
				} else
					catalogAddBeanId.setName("");

					catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				if (co2ProductRequestObject.getRow()!= null) {
					int index = catalogListBeanId.stringToInt(co2ProductRequestObject.getRow());
					catalogEditBeanId.setIndx_select(index);
				}
				if (co2ProductRequestObject.getName()!= null) {
					catalogEditBeanId.setName(co2ProductRequestObject.getName());
				}

				if (co2ProductRequestObject.getCatalog_id()!= null) {
					authorizationPageBeanId.setCatalog_id(co2ProductRequestObject.getCatalog_id());
				}
					catalogEditBeanId.editCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");

			}

		} else
			publisherBeanId.setAction("");

		String jsf_admin_key = jsf_admin;
		if (jsf_admin_key == null || jsf_admin_key.equals(""))
			jsf_admin = String.valueOf(false);
		jsf_admin_key = jsf_admin_key.trim();
		jsf_admin = String.valueOf(jsf_admin_key.equals("true"));
		publisherBeanId.setNameOfPage("Co2ProductPost.jsp");
		return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);   
	}

	private void action(Co2ProductRequest co2ProductRequestObject)throws Exception {

		PublisherBean publisherBeanId = getPublisherBean().get();
		CatalogListBean catalogListBeanId = getCatalogListBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();

		if (publisherBeanId == null || catalogListBeanId == null || authorizationPageBeanId == null
				|| productPostAllFaced == null)
			return;

		

		if (co2ProductRequestObject.getParent_id() != null) {
			authorizationPageBeanId.setCatalogParent_id(co2ProductRequestObject.getParent_id());
		}

		if (co2ProductRequestObject.getType_id()!= null) {
			publisherBeanId.setType_id(co2ProductRequestObject.getType_id());
		}

		if (co2ProductRequestObject.getRow()!= null) {
			int index = catalogListBeanId.stringToInt(co2ProductRequestObject.getRow());
			catalogListBeanId.setIndx_select(index);
		}
		if (co2ProductRequestObject.getDel() != null) {
			int index = catalogListBeanId.stringToInt(co2ProductRequestObject.getDel());
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			co2ProductRequestObject.setDel(null);
		}
		if (co2ProductRequestObject.getOffset() != null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(co2ProductRequestObject.getOffset()));
		}
//		 End Novigator ---

		if (co2ProductRequestObject.getCreteria1_id()!= null)
			publisherBeanId.setCreteria1_id(co2ProductRequestObject.getCreteria1_id());
		if (co2ProductRequestObject.getCreteria2_id()!= null)
			publisherBeanId.setCreteria2_id(co2ProductRequestObject.getCreteria2_id());
		if (co2ProductRequestObject.getCreteria3_id()!= null)
			publisherBeanId.setCreteria3_id(co2ProductRequestObject.getCreteria3_id());
		if (co2ProductRequestObject.getCreteria4_id()!= null)
			publisherBeanId.setCreteria4_id(co2ProductRequestObject.getCreteria4_id());
		if (co2ProductRequestObject.getCreteria5_id()!= null)
			publisherBeanId.setCreteria5_id(co2ProductRequestObject.getCreteria5_id());
		if (co2ProductRequestObject.getCreteria6_id()!= null)
			publisherBeanId.setCreteria6_id(co2ProductRequestObject.getCreteria6_id());
		if (co2ProductRequestObject.getCreteria7_id()!= null)
			publisherBeanId.setCreteria7_id(co2ProductRequestObject.getCreteria7_id());
		if (co2ProductRequestObject.getCreteria8_id()!= null)
			publisherBeanId.setCreteria8_id(co2ProductRequestObject.getCreteria8_id());
		if (co2ProductRequestObject.getCreteria9_id()!= null)
			publisherBeanId.setCreteria9_id(co2ProductRequestObject.getCreteria9_id());
		if (co2ProductRequestObject.getCreteria10_id()!= null)
			publisherBeanId.setCreteria10_id(co2ProductRequestObject.getCreteria10_id());

		if (co2ProductRequestObject.getInsert()!= null) {
			if (co2ProductRequestObject.getInsert().compareTo("true") == 0)
				publisherBeanId.setSoft_id("-1");

		}

		String softname = co2ProductRequestObject.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id = co2ProductRequestObject.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		if (co2ProductRequestObject.getType_id()!= null) {
			publisherBeanId.setType_id(co2ProductRequestObject.getType_id());
		}

		String softcost = co2ProductRequestObject.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = co2ProductRequestObject.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = co2ProductRequestObject.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = co2ProductRequestObject.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = co2ProductRequestObject.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id =co2ProductRequestObject.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (co2ProductRequestObject.getPortlettype_id()!= null) {
			publisherBeanId.setPortlettype_id(co2ProductRequestObject.getPortlettype_id());
		}

		String filename = co2ProductRequestObject.getFilename();
		if (filename != null) {
			publisherBeanId.setFilename(filename);
		}

		String bigimagename = co2ProductRequestObject.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = co2ProductRequestObject.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}


		if (co2ProductRequestObject.getSalelogic_id()!= null)
			publisherBeanId.setProgname_id(co2ProductRequestObject.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
		}

	}

}
