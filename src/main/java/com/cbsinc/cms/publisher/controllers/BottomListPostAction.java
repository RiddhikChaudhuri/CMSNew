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
import com.cbsinc.cms.dto.pages.request.BottomListRequest;
import com.cbsinc.cms.dto.pages.response.CMSBottomListProductResponseDTO;
import com.cbsinc.cms.publisher.dao.AuthorizationPageFaced;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.PublisherBean;


@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class BottomListPostAction extends CMSObjects{

  private final XLogger logger = XLoggerFactory.getXLogger(BottomListPostAction.class.getName());

  
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	
	@Autowired
	AuthorizationPageFaced authorizationPageFaced;
	
	@Value("${jsf_admin}")
    private String jsf_admin_setup_resouce;

	public BottomListPostAction() {
	  logger.entry();
	  logger.exit();
	}

    @PostMapping("/doPostPageBottomList")
	public CMSBottomListProductResponseDTO doPost(@RequestBody BottomListRequest bottomListRequestObject)throws Exception {
		action(bottomListRequestObject);
		PublisherBean publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		CatalogListBean catalogListBeanId= getCatalogListBean().get();

		if (bottomListRequestObject.getAction() != null) {
			publisherBeanId.setAction(bottomListRequestObject.getAction());

			if (publisherBeanId.getAction().compareTo("save") == 0) {

				if (bottomListRequestObject.getBigimage_id() == null) {
					publisherBeanId.setBigimage_id("-1");
				}
				if (bottomListRequestObject.getImage_id() == null) {
					publisherBeanId.setImage_id("-1");
				}

				publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());

				publisherBeanId.setStrSoftDescription("");
				if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
					if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
						authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
					      return new CMSBottomListProductResponseDTO(publisherBeanId, authorizationPageBeanId, catalogListBeanId);
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
		return new CMSBottomListProductResponseDTO(publisherBeanId, authorizationPageBeanId, catalogListBeanId);
	}

    @GetMapping("/doGetPageBottomList")
	public CMSBottomListProductResponseDTO doGet(@RequestBody BottomListRequest bottomListRequestObject)
			throws Exception {
		PublisherBean publisherBeanId = getPublisherBean().get();
		AuthorizationPageBean AuthorizationPageBeanId = getAuthorizationPageBean().get();
	    CatalogListBean catalogListBeanId=getCatalogListBean().get();

	      
		action(bottomListRequestObject);
		productPostAllFaced.initPage(bottomListRequestObject.getProduct_id(), publisherBeanId, AuthorizationPageBeanId);
		// if insert and limmit not add message
		if (productPostAllFaced.isLimmitPostedMessages(AuthorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			AuthorizationPageBeanId.setStrMessage(AuthorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
		     return new CMSBottomListProductResponseDTO(publisherBeanId, AuthorizationPageBeanId, catalogListBeanId);
		}

		if (bottomListRequestObject.getAction() != null) {
			publisherBeanId.setAction(bottomListRequestObject.getAction());
		} else
			publisherBeanId.setAction("");

		String jsf_admin_key = jsf_admin_setup_resouce;
		if (jsf_admin_key == null || jsf_admin_key.equals("")) {
        }
		jsf_admin_key = jsf_admin_key.trim();
		publisherBeanId.setNameOfPage("BottomListPost.jsp");
		
        return new CMSBottomListProductResponseDTO(publisherBeanId, AuthorizationPageBeanId, catalogListBeanId);
	}

	public void action(BottomListRequest bottomListRequestObject)throws Exception {

	  PublisherBean publisherBeanId = getPublisherBean().get();
      AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
      CatalogListBean catalogListBeanId=getCatalogListBean().get();

	

		if (bottomListRequestObject.getParent_id() != null) {
			authorizationPageBeanId.setCatalogParent_id(bottomListRequestObject.getParent_id());
		}

		if (bottomListRequestObject.getType_id()  != null) {
			publisherBeanId.setType_id(bottomListRequestObject.getType_id());
		}
		if (bottomListRequestObject.getRow() != null) {
			int index = catalogListBeanId.stringToInt(bottomListRequestObject.getRow());
			catalogListBeanId.setIndx_select(index);
		}
		
		if (bottomListRequestObject.getDel() != null) {
			int index = catalogListBeanId.stringToInt(bottomListRequestObject.getDel());
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			bottomListRequestObject.setDel(null);
		}
		
		if (bottomListRequestObject.getOffset() != null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(bottomListRequestObject.getOffset()));
		}
		
//		 End Novigator ---

		if (bottomListRequestObject.getCreteria1_id() != null)
			publisherBeanId.setCreteria1_id(bottomListRequestObject.getCreteria1_id());
		if (bottomListRequestObject.getCreteria2_id() != null)
			publisherBeanId.setCreteria2_id(bottomListRequestObject.getCreteria2_id());
		if (bottomListRequestObject.getCreteria3_id()!= null)
			publisherBeanId.setCreteria3_id(bottomListRequestObject.getCreteria3_id());
		if (bottomListRequestObject.getCreteria4_id()!= null)
			publisherBeanId.setCreteria4_id(bottomListRequestObject.getCreteria4_id());
		if (bottomListRequestObject.getCreteria5_id()!= null)
			publisherBeanId.setCreteria5_id(bottomListRequestObject.getCreteria5_id());
		if (bottomListRequestObject.getCreteria6_id()!= null)
			publisherBeanId.setCreteria6_id(bottomListRequestObject.getCreteria6_id());
		if (bottomListRequestObject.getCreteria7_id()!= null)
			publisherBeanId.setCreteria7_id(bottomListRequestObject.getCreteria7_id());
		if (bottomListRequestObject.getCreteria8_id()!= null)
			publisherBeanId.setCreteria8_id(bottomListRequestObject.getCreteria8_id());
		if (bottomListRequestObject.getCreteria9_id() != null)
			publisherBeanId.setCreteria9_id(bottomListRequestObject.getCreteria9_id());
		if (bottomListRequestObject.getCreteria10_id() != null)
			publisherBeanId.setCreteria10_id(bottomListRequestObject.getCreteria10_id());

		if (bottomListRequestObject.getInsert() != null) {
			if (bottomListRequestObject.getInsert().compareTo("true") == 0)
				publisherBeanId.setSoft_id("-1");

		}

		String softname = bottomListRequestObject.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id = bottomListRequestObject.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		if (bottomListRequestObject.getType_id() != null) {
			publisherBeanId.setType_id(bottomListRequestObject.getType_id());
		}

		String softcost = bottomListRequestObject.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id =bottomListRequestObject.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description =bottomListRequestObject.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = bottomListRequestObject.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = bottomListRequestObject.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = bottomListRequestObject.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (bottomListRequestObject.getPortlettype_id()!= null) {
			publisherBeanId.setPortlettype_id(bottomListRequestObject.getPortlettype_id());
		}

		String filename = bottomListRequestObject.getFilename();
		if (filename != null) {
			publisherBeanId.setFilename(filename);
		}

		String bigimagename =bottomListRequestObject.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		}

		String bigimage_id = bottomListRequestObject.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (bottomListRequestObject.getSalelogic_id() != null)
			publisherBeanId.setProgname_id(bottomListRequestObject.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() != 2) {
			authorizationPageBeanId.setStrMessage("You don't have access to add position , send mail to grabko@mail.ru for access");
		}

	}

}
