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
import org.springframework.web.bind.annotation.ResponseBody;
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
public class Co2ProductPostAction extends CMSObjects{
    
    private XLogger logger = XLoggerFactory.getXLogger(Co2ProductPostAction.class.getName());
  
    @Autowired
	ProductPostAllFaced productPostAllFaced;
	
	@Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;
	
	@Value("${is_criteria_by_catalog}")
	private String is_criteria_by_catalog_string;
	
	public Co2ProductPostAction() {
	    logger.entry(jsf_admin_setup_resouce,is_criteria_by_catalog_string,productPostAllFaced);
	    logger.exit();
	}

	public void action(Co2ProductRequest co2ProductPostAction)throws Exception {
	    logger.entry(co2ProductPostAction);
	    PublisherBean publisherBeanId = getPublisherBean().get();
        CatalogListBean catalogListBeanId = getCatalogListBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();


		if (publisherBeanId == null || catalogListBeanId == null || authorizationPageBeanId == null
				|| productPostAllFaced == null)
			return;

		if (co2ProductPostAction.getParent_id() != null) {
			authorizationPageBeanId.setCatalogParent_id(co2ProductPostAction.getParent_id());
		}

		if (co2ProductPostAction.getType_id()!= null) {
			publisherBeanId.setType_id(co2ProductPostAction.getType_id());
		}

		if (co2ProductPostAction.getRow() != null) {
			int index = catalogListBeanId.stringToInt(co2ProductPostAction.getRow());
			catalogListBeanId.setIndx_select(index);
		}
		if (co2ProductPostAction.getDel()!= null) {
			int index = catalogListBeanId.stringToInt(co2ProductPostAction.getDel());
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			co2ProductPostAction.setDel(null);
		}
		if (co2ProductPostAction.getOffset()!= null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(co2ProductPostAction.getOffset()));
		}
//		 End Novigator ---

		if (co2ProductPostAction.getCreteria1_id()!= null)
          publisherBeanId.setCreteria1_id(co2ProductPostAction.getCreteria1_id());
      if (co2ProductPostAction.getCreteria2_id()!= null)
          publisherBeanId.setCreteria2_id(co2ProductPostAction.getCreteria2_id());
      if (co2ProductPostAction.getCreteria3_id()!= null)
          publisherBeanId.setCreteria3_id(co2ProductPostAction.getCreteria3_id());
      if (co2ProductPostAction.getCreteria4_id()!= null)
          publisherBeanId.setCreteria4_id(co2ProductPostAction.getCreteria4_id());
      if (co2ProductPostAction.getCreteria5_id()!= null)
          publisherBeanId.setCreteria5_id(co2ProductPostAction.getCreteria5_id());
      if (co2ProductPostAction.getCreteria6_id()!= null)
          publisherBeanId.setCreteria6_id(co2ProductPostAction.getCreteria6_id());
      if (co2ProductPostAction.getCreteria7_id()!= null)
          publisherBeanId.setCreteria7_id(co2ProductPostAction.getCreteria7_id());
      if (co2ProductPostAction.getCreteria8_id()!= null)
          publisherBeanId.setCreteria8_id(co2ProductPostAction.getCreteria8_id());
      if (co2ProductPostAction.getCreteria9_id()!= null)
          publisherBeanId.setCreteria9_id(co2ProductPostAction.getCreteria9_id());
      if (co2ProductPostAction.getCreteria10_id()!= null)
          publisherBeanId.setCreteria10_id(co2ProductPostAction.getCreteria10_id());

		if (co2ProductPostAction.getInsert()!= null) {
			if (co2ProductPostAction.getInsert().compareTo("true") == 0)
				publisherBeanId.setSoft_id("-1");

		}

		String softname = co2ProductPostAction.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id = co2ProductPostAction.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		if (co2ProductPostAction.getType_id() != null) {
			publisherBeanId.setType_id(co2ProductPostAction.getType_id());
		}

		String softcost = co2ProductPostAction.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = co2ProductPostAction.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description =co2ProductPostAction.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = co2ProductPostAction.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = co2ProductPostAction.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = co2ProductPostAction.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}
//		else publisherBeanId.setImage_id( "-1" );

		if (co2ProductPostAction.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(co2ProductPostAction.getPortlettype_id());
		}

		String filename = co2ProductPostAction.getFilename();
		if (filename != null) {
			publisherBeanId.setFilename(filename);
		}

		String bigimagename = co2ProductPostAction.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = co2ProductPostAction.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}
//		else publisherBeanId.setBigimage_id( "-1" );

		if (co2ProductPostAction.getSalelogic_id()!= null)
			publisherBeanId.setProgname_id(co2ProductPostAction.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
			} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() != 2) {
			authorizationPageBeanId.setStrMessage("You don't have access to add position , send mail to grabko@mail.ru for access");
		}

	}

	   @GetMapping(value = "doGetCo2Product", consumes = "application/json", produces = "application/json")
	public @ResponseBody CMSCo2ProductPostUserModel doGet(@RequestBody Co2ProductRequest co2ProductPostAction)throws Exception {
	  PublisherBean publisherBeanId = getPublisherBean().get();
      CatalogListBean catalogListBeanId = getCatalogListBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		String notselected = authorizationPageBeanId.getLocalization(getServletContext()).getString("notselected");
		CatalogEditBean catalogEditBeanId =getCatalogEditBean().get();
        CatalogAddBean catalogAddBeanId = getCatalogAddBean().get();

		action(co2ProductPostAction);
		productPostAllFaced.initPage(co2ProductPostAction.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
			return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);
		}
		boolean is_criteria_by_catalog = is_criteria_by_catalog_string.equals("true");
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

		if (co2ProductPostAction.getAction() != null) {

			publisherBeanId.setAction(co2ProductPostAction.getAction());

			if (publisherBeanId.getAction().compareTo("add") == 0) {
				// catalog_addBean.setParent_id(catalogListBeanId.getParent_id());
				// catalog_addBean.setSite_id(authorizationPageBeanId.getSite_id());

				if (co2ProductPostAction.getName()!= null) {
				  catalogAddBeanId.setName(co2ProductPostAction.getName());
				} else
				  catalogAddBeanId.setName("");

			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				// catalog_editBean.setParent_id(catalogListBeanId.getParent_id());
				// catalog_editBean.setSite_id(authorizationPageBeanId.getSite_id());
				if (co2ProductPostAction.getRow()!= null) {
					int index = catalogListBeanId.stringToInt(co2ProductPostAction.getRow());
					catalogEditBeanId.setIndx_select(index);
				}
				if (co2ProductPostAction.getName()!= null) {
				  catalogEditBeanId.setName(co2ProductPostAction.getName());
				}

				if (co2ProductPostAction.getCatalog_id()!= null) {
					authorizationPageBeanId.setCatalog_id(co2ProductPostAction.getCatalog_id());
				}

			}

		} else
			publisherBeanId.setAction("");

		String jsf_admin_key = jsf_admin_setup_resouce;
		if (jsf_admin_key == null || jsf_admin_key.equals("")) {
    }
		jsf_admin_key = jsf_admin_key.trim();
		publisherBeanId.setNameOfPage("Co2ProductPost.jsp");
		return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);

	}

	   @PostMapping(value = "doPostCo2Product", consumes = "application/json", produces = "application/json")
	public CMSCo2ProductPostUserModel doPost(@RequestBody Co2ProductRequest co2ProductPostAction)	throws Exception {
		action(co2ProductPostAction);
        PublisherBean publisherBeanId = getPublisherBean().get();
        CatalogListBean catalogListBeanId = getCatalogListBean().get();
        AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
        CatalogEditBean catalogEditBeanId = getCatalogEditBean().get();
        CatalogAddBean catalogAddBeanId = getCatalogAddBean().get();

		if (co2ProductPostAction.getAction() != null) {
			publisherBeanId.setAction(co2ProductPostAction.getAction());

			if (publisherBeanId.getAction().compareTo("add") == 0) {
				// catalog_addBean.setParent_id(authorizationPageBeanId.getCatalogParent_id());
				// catalog_addBean.setSite_id(authorizationPageBeanId.getSite_id());

				if (co2ProductPostAction.getName() != null) {
				  catalogAddBeanId.setName(co2ProductPostAction.getName());
				} else
				  catalogAddBeanId.setName("");

				
				catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
			         return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);

			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				// catalog_editBean.setParent_id(catalogListBeanId.getParent_id());
				// catalog_editBean.setSite_id(authorizationPageBeanId.getSite_id());
				if (co2ProductPostAction.getRow()!= null) {
					int index = catalogListBeanId.stringToInt(co2ProductPostAction.getRow());
					catalogEditBeanId.setIndx_select(index);
				}
				if (co2ProductPostAction.getName()!= null) {
				  catalogEditBeanId.setName(co2ProductPostAction.getName());
				}

				if (co2ProductPostAction.getCatalog_id()!= null) {
					authorizationPageBeanId.setCatalog_id(co2ProductPostAction.getCatalog_id());
				}
				  catalogEditBeanId.editCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
                    return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);

				

			}
			if (publisherBeanId.getAction().compareTo("save") == 0) {

				if (co2ProductPostAction.getBigimage_id() == null) {
					publisherBeanId.setBigimage_id("-1");
				}
				if (co2ProductPostAction.getImage_id() == null) {
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
			}

		} else {
			publisherBeanId.setAction("");
		}
        return new CMSCo2ProductPostUserModel(publisherBeanId,catalogAddBeanId,catalogEditBeanId,catalogListBeanId,authorizationPageBeanId);

	}

}
