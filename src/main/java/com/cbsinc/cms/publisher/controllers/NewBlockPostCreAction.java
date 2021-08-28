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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.NewBlockPostCreForm;
import com.cbsinc.cms.dto.pages.response.CMSNewBlockPostCreResponseModel;
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
public class NewBlockPostCreAction extends CMSObjects {
	
    private XLogger logger = XLoggerFactory.getXLogger(NewBlockPostCreAction.class.getName());
    
    private String notselected = "";

  
	@Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	
	@Value("${is_criteria_by_catalog}")
    private String is_criteria_by_catalog_string;
    
	
	
	public NewBlockPostCreAction() {
	    logger.entry();
	    logger.exit();
	}

	  @PostMapping(value="/doPostNewBlockCre",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody CMSNewBlockPostCreResponseModel doPost(NewBlockPostCreForm newBlockPostCreForm)
			throws Exception {

		HttpSession session = getHttpSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		CatalogListBean catalogListBeanId = (CatalogListBean) session.getAttribute("catalogListBeanId");
		CatalogEditBean catalogEditBeanId = (CatalogEditBean) session.getAttribute("catalogEditBeanId");
		CatalogAddBean catalogAddBeanId = (CatalogAddBean) session.getAttribute("catalogAddBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");

		action(newBlockPostCreForm);

		if (newBlockPostCreForm.getShow_rating1()!= null) {
			publisherBeanId.setStrShow_ratimg1_checked("CHECKED");
			publisherBeanId.setStrShow_ratimg1("true");
		} else {
			publisherBeanId.setStrShow_ratimg1_checked("");
			publisherBeanId.setStrShow_ratimg1("false");
		}

		if (newBlockPostCreForm.getShow_blog() != null) {
			publisherBeanId.setShow_forum_checked("CHECKED");
			publisherBeanId.setStrShow_forum("true");
		} else {
			publisherBeanId.setShow_forum_checked("");
			publisherBeanId.setStrShow_forum("false");
		}

		if (newBlockPostCreForm.getAction()!= null) {
			publisherBeanId.setAction(newBlockPostCreForm.getAction());

			if (publisherBeanId.getAction().compareTo("add") == 0) {


				if (newBlockPostCreForm.getName() != null) {
					catalogAddBeanId.setName(newBlockPostCreForm.getName());
				} else
					catalogAddBeanId.setName("");

					catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					//response.sendRedirect("ProductPostCre.jsp");
					return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
				
			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {


				if (newBlockPostCreForm.getRow() != null) {
					int index = catalogListBeanId.stringToInt(newBlockPostCreForm.getRow());
					catalogEditBeanId.setIndx_select(index);
				}
				if (newBlockPostCreForm.getName() != null) {
					catalogEditBeanId.setName(newBlockPostCreForm.getName());
				}

				if (newBlockPostCreForm.getCatalog_id() != null) {
					authorizationPageBeanId.setCatalog_id(newBlockPostCreForm.getCatalog_id());
				}

				
					catalogEditBeanId.editCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					//response.sendRedirect("ProductPostCre.jsp");
	             return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
				

			}
			if (publisherBeanId.getAction().compareTo("save") == 0) {

				if (newBlockPostCreForm.getBigimage_id() == null) {
					publisherBeanId.setBigimage_id("-1");
				}
				if (newBlockPostCreForm.getImage_id() == null) {
					publisherBeanId.setImage_id("-1");
				}

				publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());

				if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {

					if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
						authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization()
								.getString("global_has_limmit_forsite"));
						//response.sendRedirect("PostManager.jsp");
		               return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
					}

					productPostAllFaced.saveDescSoft(publisherBeanId, authorizationPageBeanId);
					publisherBeanId.setAction("");
				} else {
					productPostAllFaced.updateDescSoft(publisherBeanId, authorizationPageBeanId);
					publisherBeanId.setAction("");
				}
			}

		} else
			publisherBeanId.setAction("");
	
        return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);

	}

	@GetMapping(value="/doGetNewBlockCre",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody CMSNewBlockPostCreResponseModel doGet(NewBlockPostCreForm newBlockPostCreForm)
			throws Exception {
	    logger.entry(newBlockPostCreForm);
		HttpSession session = getHttpSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		CatalogListBean catalogListBeanId = (CatalogListBean) session.getAttribute("catalogListBeanId");
		CatalogEditBean catalogEditBeanId = (CatalogEditBean) session.getAttribute("catalogEditBeanId");
		CatalogAddBean catalogAddBeanId = (CatalogAddBean) session.getAttribute("catalogAddBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");

		action(newBlockPostCreForm);
		
		productPostAllFaced.initPage(newBlockPostCreForm.getProduct_id(), publisherBeanId, authorizationPageBeanId);

		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
			//response.sendRedirect("PostManager.jsp");
			 return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
		}
		boolean is_criteria_by_catalog= Boolean.valueOf(is_criteria_by_catalog_string);
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

		if (newBlockPostCreForm.getAction() != null) {

			publisherBeanId.setAction(newBlockPostCreForm.getAction());

			if (publisherBeanId.getAction().compareTo("add") == 0) {
	
				if (newBlockPostCreForm.getName() != null) {
					catalogAddBeanId.setName(newBlockPostCreForm.getName());
				} else
					catalogAddBeanId.setName("");

				if (getHttpServletRequest().getMethod().toUpperCase().compareTo("POST") == 0) {
					catalogAddBeanId.addCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					 return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
				}
			}

			if (publisherBeanId.getAction().compareTo("edit") == 0) {

				if (newBlockPostCreForm.getRow() != null) {
					int index = catalogListBeanId.stringToInt(newBlockPostCreForm.getRow());
					catalogEditBeanId.setIndx_select(index);
				}
				if (newBlockPostCreForm.getName() != null) {
					catalogEditBeanId.setName(newBlockPostCreForm.getName());
				}

				if (newBlockPostCreForm.getCatalog_id() != null) {
					authorizationPageBeanId.setCatalog_id(newBlockPostCreForm.getCatalog_id());
				}

				if (getHttpServletRequest().getMethod().toUpperCase().compareTo("POST") == 0) {
					catalogEditBeanId.editCatalog(authorizationPageBeanId);
					publisherBeanId.setAction("");
					 return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
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
		
		 return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
	}

	public void action(NewBlockPostCreForm newBlockPostCreForm)
			throws Exception {
		HttpSession session = getHttpSession();
		PublisherBean publisherBeanId = (PublisherBean) session.getAttribute("publisherBeanId");
		CatalogListBean catalogListBeanId = (CatalogListBean) session.getAttribute("catalogListBeanId");
		AuthorizationPageBean authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");


		if (publisherBeanId == null || catalogListBeanId == null || authorizationPageBeanId == null
				|| productPostAllFaced == null) return;

		if (notselected.length() == 0)
			notselected = authorizationPageBeanId.getLocalization(getServletContext()).getString("notselected");


		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());

		if (newBlockPostCreForm.getParent_id() != null) {
			
			authorizationPageBeanId.setCatalogParent_id(newBlockPostCreForm.getParent_id());
		}

		if (newBlockPostCreForm.getRow() != null) {
			int index = catalogListBeanId.stringToInt(newBlockPostCreForm.getRow() );
			catalogListBeanId.setIndx_select(index);
		}
		if (newBlockPostCreForm.getDel() != null) {
			int index = catalogListBeanId.stringToInt(newBlockPostCreForm.getDel() );
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			newBlockPostCreForm.setDel(null);
		}

		if (newBlockPostCreForm.getOffset()!= null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(newBlockPostCreForm.getOffset()));
		} else
			catalogListBeanId.setOffset(0);

//		 End Novigator ---

//		 start novigator action ---

		if (newBlockPostCreForm.getCreteria1_id() != null)
			publisherBeanId.setCreteria1_id(newBlockPostCreForm.getCreteria1_id());
		if (newBlockPostCreForm.getCreteria2_id() != null)
			publisherBeanId.setCreteria2_id(newBlockPostCreForm.getCreteria2_id());
		if (newBlockPostCreForm.getCreteria3_id() != null)
			publisherBeanId.setCreteria3_id(newBlockPostCreForm.getCreteria3_id());
		if (newBlockPostCreForm.getCreteria4_id() != null)
			publisherBeanId.setCreteria4_id(newBlockPostCreForm.getCreteria4_id());
		if (newBlockPostCreForm.getCreteria5_id() != null)
			publisherBeanId.setCreteria5_id(newBlockPostCreForm.getCreteria5_id());
		if (newBlockPostCreForm.getCreteria6_id() != null)
			publisherBeanId.setCreteria6_id(newBlockPostCreForm.getCreteria6_id());
		if (newBlockPostCreForm.getCreteria7_id() != null)
			publisherBeanId.setCreteria7_id(newBlockPostCreForm.getCreteria7_id());
		if (newBlockPostCreForm.getCreteria8_id() != null)
			publisherBeanId.setCreteria8_id(newBlockPostCreForm.getCreteria8_id());
		if (newBlockPostCreForm.getCreteria9_id() != null)
			publisherBeanId.setCreteria9_id(newBlockPostCreForm.getCreteria9_id());
		if (newBlockPostCreForm.getCreteria10_id() != null)
			publisherBeanId.setCreteria10_id(newBlockPostCreForm.getCreteria10_id());


		String softname = newBlockPostCreForm.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String softname2 = newBlockPostCreForm.getSoftname2();
		if (softname2 != null) {
			publisherBeanId.setStrSoftName2(softname2);
		}

		String jsp_url = newBlockPostCreForm.getJsp_url();
		if (jsp_url != null) {
			publisherBeanId.setJsp_url(jsp_url);
		}

		String catalog_id = newBlockPostCreForm.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		if (newBlockPostCreForm.getType_id() != null) {
			publisherBeanId.setType_id(newBlockPostCreForm.getType_id());
		}

		String softcost = newBlockPostCreForm.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = newBlockPostCreForm.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = newBlockPostCreForm.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = newBlockPostCreForm.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = newBlockPostCreForm.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = newBlockPostCreForm.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (newBlockPostCreForm.getPortlettype_id() != null) {
			publisherBeanId.setPortlettype_id(newBlockPostCreForm.getPortlettype_id() );
		}

		String file_id = newBlockPostCreForm.getFile_id();
		if (file_id != null) {
			publisherBeanId.setFile_id(file_id);
		}

		String filename = newBlockPostCreForm.getFilename();
		if (filename != null) {
			publisherBeanId.setFilename(filename);
		}

		String bigimagename = newBlockPostCreForm.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = newBlockPostCreForm.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (newBlockPostCreForm.getSalelogic_id()!= null)
			publisherBeanId.setProgname_id(newBlockPostCreForm.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() != 2) {
			authorizationPageBeanId.setStrMessage(
					authorizationPageBeanId.getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
		}

	}

}
