package com.cbsinc.cms.publisher.controllers;

import java.util.Map;
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
import com.cbsinc.cms.dto.pages.request.ProductPostCreForm;
import com.cbsinc.cms.dto.pages.response.CMSNewBlockPostCreResponseModel;
import com.cbsinc.cms.jms.controllers.Message;
import com.cbsinc.cms.jms.controllers.MessageSender;
import com.cbsinc.cms.jms.controllers.SendMailMessageBean;
import com.cbsinc.cms.publisher.dao.ProductPostAllFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogAddBean;
import com.cbsinc.cms.publisher.models.CatalogEditBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.PublisherBean;
import io.swagger.annotations.ApiOperation;

@RestController
@PropertySources(value = {
    @PropertySource(value="classpath:SetupApplicationResources.properties")
  })
public class ProductUserPostBusinessAction extends CMSObjects {

  
  private XLogger logger = XLoggerFactory.getXLogger(ProductUserPostBusinessAction.class.getName());

	CatalogListBean catalogListBeanId;
	CatalogEditBean catalogEditBeanId;
	CatalogAddBean catalogAddBeanId;
	AuthorizationPageBean authorizationPageBeanId;
	HttpSession session;
	Map messageMail;
	
	@Autowired
	ProductPostAllFaced productPostAllFaced;
	
	String gen_code = "";
	
	PublisherBean publisherBeanId;

	String notselected = "";
	
    @Value("${is_criteria_by_catalog}")
    private String is_criteria_by_catalog_string;
	
	public ProductUserPostBusinessAction() {
      logger.entry();
      logger.exit();
  }

    @PostMapping(value="/doPostProductUserBuisness",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Post For Product User Buisness Controller",notes = "Returns Product User Buisness Controller based on the Request")
	public @ResponseBody CMSNewBlockPostCreResponseModel doPost(@RequestBody  ProductPostCreForm productListWebForm)throws Exception {

		action(productListWebForm);

		if (productListWebForm.getGen_number()!= null) {
			String val1 = productListWebForm.getGen_number().trim();
			if (!val1.equals(gen_code.trim())) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("wrong_gen_number"));
			}
		} else {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("wrong_gen_number"));
		}

		if (productListWebForm.getBigimage_id() == null) {
			publisherBeanId.setBigimage_id("-1");
		}
		if (productListWebForm.getImage_id()== null) {
			publisherBeanId.setImage_id("-1");
		}
		publisherBeanId.setSite_id(authorizationPageBeanId.getSite_id());
		if (publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, true)) {
				authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
				return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
			}

			productPostAllFaced.saveInformationWithCheck(publisherBeanId, authorizationPageBeanId);
		} else
			productPostAllFaced.updateInformationWithCheck(publisherBeanId, authorizationPageBeanId);

		messageMail.clear();
		messageMail.put("@FirstName", authorizationPageBeanId.getStrFirstName());
		messageMail.put("@LastName", authorizationPageBeanId.getStrLastName());
		messageMail.put("@Site", authorizationPageBeanId.getSite_dir());

		String sitePath = (String) getHttpSession().getAttribute("site_path");
		String addinfo = sitePath + "\\mail\\addinfo.txt";
		String attachFile = sitePath + "\\mail\\info.txt";

		MessageSender mqSender = new MessageSender(getHttpSession(), SendMailMessageBean.messageQuery);
		Message message = new Message();
		message.put("to", null);
		message.put("subject", "Has added new info on site ");
		message.put("pathmessage", addinfo);
		message.put("attachFile", attachFile);
		message.put("fields", messageMail);
		mqSender.send(message);
		return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);

	}

    @GetMapping(value="/doGetProductUserBuisness",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Get For Product User Buisness Controller",notes = "Returns Product User Buisness Controller based on the Product ID")
	public @ResponseBody CMSNewBlockPostCreResponseModel doGet( ProductPostCreForm productListWebForm)throws Exception {


		action(productListWebForm);
		productPostAllFaced.initPage(productListWebForm.getProduct_id(), publisherBeanId, authorizationPageBeanId);
		if (productPostAllFaced.isLimmitPostedMessages(authorizationPageBeanId, false)
				&& publisherBeanId.getSoft_id().compareTo("-1") == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("global_has_limmit_forsite"));
			return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
		}
	     boolean is_criteria_by_catalog= Boolean.valueOf(is_criteria_by_catalog_string);
		publisherBeanId.setCriteria1_label(productPostAllFaced
				.getOneLabel("select  label   from creteria1   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria2_label(productPostAllFaced
				.getOneLabel("select  label   from creteria2   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria3_label(productPostAllFaced
				.getOneLabel("select  label   from creteria3   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria4_label(productPostAllFaced
				.getOneLabel("select  label   from creteria4   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria5_label(productPostAllFaced
				.getOneLabel("select  label   from creteria5   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria6_label(productPostAllFaced
				.getOneLabel("select  label   from creteria6   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria7_label(productPostAllFaced
				.getOneLabel("select  label   from creteria7   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria8_label(productPostAllFaced
				.getOneLabel("select  label   from creteria8   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria9_label(productPostAllFaced
				.getOneLabel("select  label   from creteria9   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setCriteria10_label(productPostAllFaced
				.getOneLabel("select  label   from creteria10   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));

		publisherBeanId.setSelect_creteria1_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria1_id",
				publisherBeanId.getCreteria1_id(), notselected,
				"select creteria1_id , name   from creteria1   where  active = true " + publisherBeanId
						.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)));
		publisherBeanId.setSelect_creteria2_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria2_id",
				publisherBeanId.getCreteria2_id(), notselected,
				"select creteria2_id , name   from creteria2   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria1_id() + " ) "));
		publisherBeanId.setSelect_creteria3_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria3_id",
				publisherBeanId.getCreteria3_id(), notselected,
				"select creteria3_id , name   from creteria3   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria2_id() + " ) "));
		publisherBeanId.setSelect_creteria4_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria4_id",
				publisherBeanId.getCreteria4_id(), notselected,
				"select creteria4_id , name   from creteria4   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria3_id() + " ) "));
		publisherBeanId.setSelect_creteria5_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria5_id",
				publisherBeanId.getCreteria5_id(), notselected,
				"select creteria5_id , name   from creteria5   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria4_id() + " ) "));
		publisherBeanId.setSelect_creteria6_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria6_id",
				publisherBeanId.getCreteria6_id(), notselected,
				"select creteria6_id , name   from creteria6   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria5_id() + " ) "));
		publisherBeanId.setSelect_creteria7_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria7_id",
				publisherBeanId.getCreteria7_id(), notselected,
				"select creteria7_id , name   from creteria7   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria6_id() + " ) "));
		publisherBeanId.setSelect_creteria8_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria8_id",
				publisherBeanId.getCreteria8_id(), notselected,
				"select creteria8_id , name   from creteria8   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria7_id() + " ) "));
		publisherBeanId.setSelect_creteria9_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria9_id",
				publisherBeanId.getCreteria9_id(), notselected,
				"select creteria9_id , name   from creteria9   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria8_id() + " ) "));
		publisherBeanId.setSelect_creteria10_id(productPostAllFaced.getComboBoxAutoSubmitLocale("creteria10_id",
				publisherBeanId.getCreteria10_id(), notselected,
				"select creteria10_id , name   from creteria10   where  active = true "
						+ publisherBeanId.getPartCriteria(authorizationPageBeanId.getSite_id(), is_criteria_by_catalog)
						+ " and ( link_id = 0 or link_id = " + publisherBeanId.getCreteria9_id() + " ) "));
		
		return new CMSNewBlockPostCreResponseModel(publisherBeanId, authorizationPageBeanId, catalogEditBeanId, catalogAddBeanId);
	}

	public void action( ProductPostCreForm productListWebForm)	throws Exception {
		gen_code = (String) getHttpSession().getAttribute("gen_number");
		publisherBeanId = getPublisherBean().get();
		catalogListBeanId = getCatalogListBean().get();
		catalogEditBeanId = getCatalogEditBean().get();
		catalogAddBeanId =getCatalogAddBean().get();
		authorizationPageBeanId =getAuthorizationPageBean().get();
		
		messageMail = (Map) session.getAttribute("messageMail");
		
		if (notselected.length() == 0)
			notselected = authorizationPageBeanId.getLocalization(getServletContext()).getString("notselected");

		if (publisherBeanId == null || catalogListBeanId == null || catalogEditBeanId == null || catalogAddBeanId == null
				|| authorizationPageBeanId == null || messageMail == null || productPostAllFaced == null) return;

		if (productListWebForm.getParent_id()!= null) {
			authorizationPageBeanId.setCatalogParent_id(productListWebForm.getParent_id());
		}

		if (productListWebForm.getRow()!= null) {
			int index = catalogListBeanId.stringToInt(productListWebForm.getRow());
			catalogListBeanId.setIndx_select(index);
		}
		if (productListWebForm.getDel()!= null) {
			int index = catalogListBeanId.stringToInt(productListWebForm.getDel());
			String catalog_id = catalogListBeanId.rows[index][0];
			if (catalog_id != null)
				catalogListBeanId.delete(catalog_id, authorizationPageBeanId);
			productListWebForm.setDel(null);
		}
		if (productListWebForm.getOffset() != null) {
			catalogListBeanId.setOffset(catalogListBeanId.stringToInt(productListWebForm.getOffset() ));
		}
//		 End Novigator ---

		if (productListWebForm.getCreteria1_id()!= null)
			publisherBeanId.setCreteria1_id(productListWebForm.getCreteria1_id());
		if (productListWebForm.getCreteria2_id()!= null)
			publisherBeanId.setCreteria2_id(productListWebForm.getCreteria2_id());
		if (productListWebForm.getCreteria3_id()!= null)
			publisherBeanId.setCreteria3_id(productListWebForm.getCreteria3_id());
		if (productListWebForm.getCreteria4_id()!= null)
			publisherBeanId.setCreteria4_id(productListWebForm.getCreteria4_id());
		if (productListWebForm.getCreteria5_id() != null)
			publisherBeanId.setCreteria5_id(productListWebForm.getCreteria5_id());
		if (productListWebForm.getCreteria6_id()!= null)
			publisherBeanId.setCreteria6_id(productListWebForm.getCreteria6_id());
		if (productListWebForm.getCreteria7_id()!= null)
			publisherBeanId.setCreteria7_id(productListWebForm.getCreteria7_id());
		if (productListWebForm.getCreteria8_id()!= null)
			publisherBeanId.setCreteria8_id(productListWebForm.getCreteria8_id());
		if (productListWebForm.getCreteria9_id()!= null)
			publisherBeanId.setCreteria9_id(productListWebForm.getCreteria9_id());
		if (productListWebForm.getCreteria10_id()!= null)
			publisherBeanId.setCreteria10_id(productListWebForm.getCreteria10_id());

		String softname = productListWebForm.getSoftname();
		if (softname != null) {
			publisherBeanId.setStrSoftName(softname);
		}

		String catalog_id =productListWebForm.getCatalog_id();
		if (catalog_id != null) {
			authorizationPageBeanId.setCatalog_id(catalog_id);
		}

		String softcost = productListWebForm.getSoftcost();
		if (softcost != null) {
			publisherBeanId.setStrSoftCost(softcost);
		}

		String currency_id = productListWebForm.getCurrency_id();
		if (currency_id != null) {
			publisherBeanId.setStrCurrency(currency_id);
		}

		String description = productListWebForm.getDescription();
		if (description != null) {
			publisherBeanId.setStrSoftDescription(description);
		}

		String fulldescription = productListWebForm.getFulldescription();
		if (fulldescription != null) {
			publisherBeanId.setProduct_fulldescription(fulldescription);
		}

		String imagename = productListWebForm.getImagename();
		if (imagename != null) {
			publisherBeanId.setImgname(imagename);
		}

		String image_id = productListWebForm.getImage_id();
		if (image_id != null) {
			publisherBeanId.setImage_id(image_id);
		}

		if (productListWebForm.getPortlettype_id()!= null) {
			publisherBeanId.setPortlettype_id(productListWebForm.getPortlettype_id());
		}

		String filename = productListWebForm.getFilename();
		if (filename != null) {
			publisherBeanId.setSample(filename);
		} else {
			publisherBeanId.setSample("");
		}
		filename = null;

		String bigimagename = productListWebForm.getBigimagename();
		if (bigimagename != null) {
			publisherBeanId.setBigimgname(bigimagename);
		} else
			publisherBeanId.setBigimgname("-1");

		String bigimage_id = productListWebForm.getBigimage_id();
		if (bigimage_id != null) {
			publisherBeanId.setBigimage_id(bigimage_id);
		}

		if (productListWebForm.getSalelogic_id()!= null)
			publisherBeanId.setProgname_id(productListWebForm.getSalelogic_id());

		if (authorizationPageBeanId.getIntUserID() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("session_time_out"));
		} else
			publisherBeanId.setUser_id("" + authorizationPageBeanId.getIntUserID());

		if (authorizationPageBeanId.getIntLevelUp() == 0) {
			authorizationPageBeanId.setStrMessage(authorizationPageBeanId.getLocalization(getServletContext()).getString("post_message_notaccess_admin"));
		}

	}
}
