package com.cbsinc.cms.publisher.controllers;

import javax.servlet.http.HttpSession;
import org.slf4j.ext.XLogger;
import org.slf4j.ext.XLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.cbsinc.cms.dto.pages.request.PolicyRequestBody;
import com.cbsinc.cms.dto.pages.response.CMSPolicyPageModel;
import com.cbsinc.cms.publisher.dao.PolicyFaced;
import com.cbsinc.cms.publisher.dao.ProductlistFaced;
import com.cbsinc.cms.publisher.models.AuthorizationPageBean;
import com.cbsinc.cms.publisher.models.CatalogListBean;
import com.cbsinc.cms.publisher.models.ItemDescriptionBean;


@RestController
public class PolicyAction extends CMSObjects{

  private XLogger logger = XLoggerFactory.getXLogger(PolicyAction.class.getName());
  
  @Autowired
  private PolicyFaced policyFaced;
  
  @Autowired
  private ProductlistFaced productlistFaced;
  
  public PolicyAction() {
    logger.entry();
    logger.exit();
  }

  @PostMapping("/doPostPolicy")
  public @ResponseBody CMSPolicyPageModel doPost(@RequestBody PolicyRequestBody policyRequest)throws Exception {
		return doGet(policyRequest);
  }
  
  @GetMapping("/doGetPolicy")
  public @ResponseBody CMSPolicyPageModel doGet(@RequestBody PolicyRequestBody policyRequest)
			throws Exception {
        logger.entry(policyRequest);
		AuthorizationPageBean authorizationPageBeanId = getAuthorizationPageBean().get();
		ItemDescriptionBean itemDescriptionBeanId = null;
		HttpSession session = getHttpSession();
		boolean isInternet = true;

		itemDescriptionBeanId = new ItemDescriptionBean();

		authorizationPageBeanId = (AuthorizationPageBean) session.getAttribute("authorizationPageBeanId");
		

		if (getHttpServletRequest().getRemoteAddr().startsWith("192."))
			isInternet = false;
		if (getHttpServletRequest().getRemoteAddr().startsWith("10."))
			isInternet = false;

		if (authorizationPageBeanId == null || itemDescriptionBeanId == null)
			return new CMSPolicyPageModel(itemDescriptionBeanId);

		if (policyRequest.getPolicy_byproductid() == null && policyRequest.getPage() == null) {

			if (policyRequest.getRate() != null && isNumber(policyRequest.getRate())) {
				itemDescriptionBeanId.setProduct_id(Long.toString(authorizationPageBeanId.getLastProductId()));
				int rate = Integer.parseInt(policyRequest.getRate());
				policyFaced.setRatring1(rate, itemDescriptionBeanId.getProduct_id());
				return new CMSPolicyPageModel(itemDescriptionBeanId);
			}

			//response.sendRedirect("Productlist.jsp?catalog_id=-2");
			return new CMSPolicyPageModel(itemDescriptionBeanId);
		}

		itemDescriptionBeanId.internet = isInternet;

		if (policyRequest.getPolicy_byproductid() != null
				&& isNumber(policyRequest.getPolicy_byproductid())) {
			policyFaced.mergePolicyBean(authorizationPageBeanId.getIntUserID(),
			    policyRequest.getPolicy_byproductid(), itemDescriptionBeanId);
			itemDescriptionBeanId.setBack_url("Policy.jsp?policy_byproductid=" + itemDescriptionBeanId.getParent_product_id());
			itemDescriptionBeanId.setType_page("policy_byproductid");
			itemDescriptionBeanId.setIntUserID(authorizationPageBeanId.getIntUserID());
			authorizationPageBeanId.setLastProductId(Long.parseLong(itemDescriptionBeanId.getParent_product_id()));

			if (policyRequest.getRate()!= null && isNumber(policyRequest.getRate())) {
				int rate = Integer.parseInt(policyRequest.getRate());
				policyFaced.setRatring1(rate, itemDescriptionBeanId.getProduct_id());
			}

			itemDescriptionBeanId.setRating1_xml(policyFaced.getRatring1Map(itemDescriptionBeanId.getProduct_id()));
			itemDescriptionBeanId.setBalans("" + policyFaced.getBalans(authorizationPageBeanId.getIntUserID()));

			try {
				if (itemDescriptionBeanId.isFistOpen()) {

					policyFaced.payMoneyForShowPage(itemDescriptionBeanId, authorizationPageBeanId, getHttpServletRequest().getRemoteAddr());
					itemDescriptionBeanId.setFistOpen(false);
				} else
					policyFaced.incrementShowPage(itemDescriptionBeanId);
			} catch (Exception ex) {
				logger.error("Billing is not working ", ex);
			}

		} else if (policyRequest.getPage() != null && policyRequest.getPage().compareTo("about") == 0) {
			policyFaced.mergePolicyBeanForAboutPage(authorizationPageBeanId.getSite_id(), itemDescriptionBeanId);// (authorizationPageBeanId.getIntUserID(),
																										// request.getParameter("policy_byproductid")
																										// ,
																										// policyBeanId)
																										// ;
			authorizationPageBeanId.setLastProductId(Long.parseLong(itemDescriptionBeanId.getParent_product_id()));
			itemDescriptionBeanId.setBack_url("Productlist.jsp");
			itemDescriptionBeanId.setType_page("about");
			itemDescriptionBeanId.setIntUserID(authorizationPageBeanId.getIntUserID());
			itemDescriptionBeanId.setRating1_xml(policyFaced.getRatring1Map(itemDescriptionBeanId.getProduct_id()));
			itemDescriptionBeanId.setBalans("" + policyFaced.getBalans(authorizationPageBeanId.getIntUserID()));

			if (itemDescriptionBeanId.isFistOpen()) {
				policyFaced.payMoneyForShowPage(itemDescriptionBeanId, authorizationPageBeanId, getHttpServletRequest().getRemoteAddr());
				itemDescriptionBeanId.setFistOpen(false);
			} else
				policyFaced.incrementShowPage(itemDescriptionBeanId);
		} else if (policyRequest.getPage() != null && policyRequest.getPage().compareTo("pay") == 0) {
			policyFaced.mergePolicyBeanForPayPageInfo(authorizationPageBeanId.getSite_id(), itemDescriptionBeanId);// (authorizationPageBeanId.getIntUserID(),
																											// request.getParameter("policy_byproductid")
																											// ,
																											// policyBeanId)
																											// ;
			authorizationPageBeanId.setLastProductId(Long.parseLong(itemDescriptionBeanId.getParent_product_id()));
			itemDescriptionBeanId.setBack_url("Productlist.jsp");
			itemDescriptionBeanId.setType_page("pay");
			itemDescriptionBeanId.setIntUserID(authorizationPageBeanId.getIntUserID());
			itemDescriptionBeanId.setRating1_xml(policyFaced.getRatring1Map(itemDescriptionBeanId.getProduct_id()));
			itemDescriptionBeanId.setBalans("" + policyFaced.getBalans(authorizationPageBeanId.getIntUserID()));

			if (itemDescriptionBeanId.isFistOpen()) {
				policyFaced.payMoneyForShowPage(itemDescriptionBeanId, authorizationPageBeanId, getHttpServletRequest().getRemoteAddr());
				itemDescriptionBeanId.setFistOpen(false);
			} else
				policyFaced.incrementShowPage(itemDescriptionBeanId);

		}

		itemDescriptionBeanId.setSelectCatalogXMLUrlPath(
				(new CatalogListBean(getServletContext())).getCatalogXMLUrlPath("Productlist.jsp?catalog_id", "parent",
						authorizationPageBeanId.getCatalog_id(), authorizationPageBeanId));
		itemDescriptionBeanId.setSelect_tree_catalog(productlistFaced.getTreeXMLDBList("Productlist.jsp?catalog_id", "catalog",
				authorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable   from catalog   where  active = true and site_id = "
						+ authorizationPageBeanId.getSite_id() + " and parent_id = "
						+ authorizationPageBeanId.getCatalog_parent_id(),
				"select catalog_id , lable   from catalog   where  active = true and parent_id = "
						+ authorizationPageBeanId.getCatalog_id()));
		itemDescriptionBeanId.setSelect_currencies(
				policyFaced.getXMLDBList("Policy.jsp", "currencies", itemDescriptionBeanId.getCurrency_cd(),
						"SELECT currency_cd , currency_desc  FROM currency  WHERE active = true","currency_cd","currency_desc"));
		itemDescriptionBeanId.ext1Adp = productlistFaced.getExtPolicyOneProductlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), itemDescriptionBeanId.getParent_product_id(), itemDescriptionBeanId);
		itemDescriptionBeanId.ext2Adp = productlistFaced.getExtPolicyTwoProductlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), itemDescriptionBeanId.getParent_product_id(), itemDescriptionBeanId);
		itemDescriptionBeanId.extFilesAdp = productlistFaced.getExtPolicyFilesProductlist(
				"" + authorizationPageBeanId.getIntUserID(), authorizationPageBeanId.getSite_id(),
				itemDescriptionBeanId.getParent_product_id(), itemDescriptionBeanId);
		itemDescriptionBeanId.extTabsAdp = productlistFaced.getExtPolicyTabsProductlist(
				"" + authorizationPageBeanId.getIntUserID(), authorizationPageBeanId.getSite_id(),
				itemDescriptionBeanId.getParent_product_id(), itemDescriptionBeanId);
		itemDescriptionBeanId.blogExtAdp = productlistFaced.getBlogExtPolicyProductlist(
				"" + authorizationPageBeanId.getIntUserID(), authorizationPageBeanId.getSite_id(),
				itemDescriptionBeanId.getParent_product_id(), itemDescriptionBeanId);
		// if( policyBeanId.getPortlettype_id() != Layout.PORTLET_TYPE_BOTTOM )
		itemDescriptionBeanId.newsAdp = productlistFaced.getNewslist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId);
		itemDescriptionBeanId.bottomAdp = productlistFaced.getBottomlist("" + authorizationPageBeanId.getIntUserID(),
				authorizationPageBeanId.getSite_id(), authorizationPageBeanId);

		itemDescriptionBeanId.setSelect_menu_catalog(productlistFaced.getMenuXMLDBList("Productlist.jsp?catalog_id", "menu",
				authorizationPageBeanId.getCatalog_id(),
				"select catalog_id , lable , parent_id  from catalog   where  active = true and parent_id = 0 and site_id = "
						+ authorizationPageBeanId.getSite_id() + " and lang_id = "
						+ authorizationPageBeanId.getLang_id()
						+ " or parent_id in (select catalog_id   from catalog   where  active = true and site_id = "
						+ authorizationPageBeanId.getSite_id() + "  and parent_id = 0 )"));
		return new CMSPolicyPageModel(itemDescriptionBeanId);
	}

	public boolean isNumber(String tmp) {
		if (tmp == null)
			return false;
		String IntField = "0123456789";
		for (int i = 0; i < tmp.length(); i++) {

			if (IntField.indexOf(tmp.charAt(i)) == -1) {
				if (tmp.charAt(i) != '-' && i != 0)
					return false;
			}
		}
		return true;
	}
}
